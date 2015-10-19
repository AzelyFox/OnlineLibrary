package kr.devs.onlinelibrary;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by TK in DEVS
 */
public class OnlineMember {

    private int APP_ID;
    private int APP_CODE;
    private int APP_NUM;
    private String USR_ID;
    private String USR_PW;
    private String USR_NICK;
    private String USR_EMAIL;
    private int USR_VERIFY;

    private OnlineMemberConnectCallbacks mConnectCallbacks;
    private OnlineMemberLoginCallbacks mLoginCallbacks;
    private OnlineMemberFindCallbacks mFindCallbacks;
    private OnlineMemberRegisterCallbacks mRegisterCallbacks;
    private OnlineMemberSignoutCallbacks mSignoutCallbacks;

    public OnlineMember(int appid,int appcode){
        APP_ID = appid;
        APP_CODE = appcode;
        if (APP_ID > 9999 | APP_ID < 1000){
            APP_ID = 1000;
        }
        if (APP_CODE > 9999 | APP_CODE < 1000){
            APP_CODE = 1000;
        }
        APP_NUM = Integer.parseInt(Integer.toString(APP_ID) + Integer.toString(APP_CODE));
        Log.d("DEVSLIB_MEMBER", "LIB :: MEMBER MADE");
    }

    public void connect(){
        connectServerTask cST = new connectServerTask();
        cST.execute();
        Log.d("DEVSLIB_MEMBER", "LIB :: CONNECT");
    }

    public void login(String USER_ID,String USER_PW){
        USR_ID = USER_ID;
        USR_PW = USER_PW;
        doLoginTask dLT = new doLoginTask();
        dLT.execute();
        Log.d("DEVSLIB_MEMBER", "LIB :: LOGIN");
    }

    public void findaccount(String USER_EMAIL){
        USR_EMAIL = USER_EMAIL;
        findAccTask fAT = new findAccTask();
        fAT.execute();
        Log.d("DEVSLIB_MEMBER", "LIB :: FINDACCOUNT");
    }

    public void register(String USER_ID,String USER_PW,String USER_NICK,String USER_EMAIL,boolean USE_EMAIL_VERIFY){
        USR_ID = USER_ID;
        USR_PW = USER_PW;
        USR_NICK = USER_NICK;
        USR_EMAIL = USER_EMAIL;
        if (USE_EMAIL_VERIFY == true){
            USR_VERIFY = 0;
        } else {
            USR_VERIFY = 1;
        }
        doRegisterTask dRT = new doRegisterTask();
        dRT.execute();
        Log.d("DEVSLIB_MEMBER", "LIB :: REGISTER");
    }

    public void signout(String USER_ID,String USER_PW){
        USR_ID = USER_ID;
        USR_PW = USER_PW;
        signOutTask sOT = new signOutTask();
        sOT.execute();
        Log.d("DEVSLIB_MEMBER", "LIB :: SIGNOUT");
    }

    public void setOnlineMemberConnectCallbacks(OnlineMemberConnectCallbacks listener){mConnectCallbacks = listener;}

    public void setOnlineMemberLoginCallbacks(OnlineMemberLoginCallbacks listener){mLoginCallbacks = listener;}

    public void setOnlineMemberFindCallbacks(OnlineMemberFindCallbacks listener){mFindCallbacks = listener;}

    public void setOnlineMemberRegisterCallbacks(OnlineMemberRegisterCallbacks listener){mRegisterCallbacks = listener;}

    public void setOnlinMemberSignoutCallbacks(OnlineMemberSignoutCallbacks listener){mSignoutCallbacks = listener;}

    public void onConnectSuccess(){mConnectCallbacks.onConnectionSuccess();}

    public void onConnectFailed(int ERROR_CODE){mConnectCallbacks.onConnectionFailed(ERROR_CODE);}

    public void onLoginSuccess(String USER_NICK,String USER_EMAIL){mLoginCallbacks.onLoginSuccess(USER_NICK, USER_EMAIL);}

    public void onLoginFailed(int ERROR_CODE){mLoginCallbacks.onLoginFailed(ERROR_CODE);}

    public void onFindAccountSuccess(){mFindCallbacks.onFindAccountSuccess();}

    public void onFindAccountFailed(int ERROR_CODE){mFindCallbacks.onFindAccountFailed(ERROR_CODE);}

    public void onRegisterSuccess(){mRegisterCallbacks.onRegisterSuccess();}

    public void onRegisterFailed(int ERROR_CODE){mRegisterCallbacks.onRegisterFailed(ERROR_CODE);}

    public void onSignoutSuccess(){mSignoutCallbacks.onSignoutSuccess();}

    public void onSignoutFailed(int ERROR_CODE){mSignoutCallbacks.onSignoutFailed(ERROR_CODE);}


    private class connectServerTask extends AsyncTask<String, Void, String> {

        String sResult;
        @Override
        protected String doInBackground(String... sId) {
            try{
                String body = "appcode="+APP_NUM;
                URL u = new URL(OnlineDatas.ONLINE_MEMBER_STATUS);
                HttpURLConnection huc = (HttpURLConnection) u.openConnection();
                huc.setReadTimeout(6000); huc.setConnectTimeout(6000);
                huc.setRequestMethod("POST"); huc.setDoInput(true); huc.setDoOutput(true);
                huc.setRequestProperty("utf-8", "application/x-www-form-urlencoded");
                OutputStream os = huc.getOutputStream();
                os.write( body.getBytes("utf-8")); os.flush(); os.close();
                BufferedReader is = new BufferedReader(new InputStreamReader(huc.getInputStream(), "utf-8"));
                int ch; StringBuffer sb = new StringBuffer();
                while ((ch = is.read()) != -1)
                    sb.append((char) ch);
                if (is != null)
                    is.close();
                sResult = sb.toString();
            }catch (Exception e){ }
            return sResult;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute ( result );
            try {
                if (sResult.contains("SUCCESS")) {
                    onConnectSuccess();
                } else {
                    Log.d("DEVSLIB_MEMBER", "LIB :: CONNECT FAIL : " + sResult);
                    onConnectFailed(-1);
                }
            } catch (NullPointerException err){
                Log.d("DEVSLIB_MEMBER", "LIB :: CONNECT FAIL : NULL");
                onConnectFailed(-2);
            }
        }
    }

    private class doLoginTask extends AsyncTask<String, Void, String> {

        String sResult;
        @Override
        protected String doInBackground(String... sId) {
            try{
                String body = "appcode="+APP_NUM+"&userid="+USR_ID+"&userpw="+USR_PW;
                URL u = new URL(OnlineDatas.ONLINE_MEMBER_LOGIN);
                HttpURLConnection huc = (HttpURLConnection) u.openConnection();
                huc.setReadTimeout(6000); huc.setConnectTimeout(6000);
                huc.setRequestMethod("POST"); huc.setDoInput(true); huc.setDoOutput(true);
                huc.setRequestProperty("utf-8", "application/x-www-form-urlencoded");
                OutputStream os = huc.getOutputStream();
                os.write( body.getBytes("utf-8")); os.flush(); os.close();
                BufferedReader is = new BufferedReader(new InputStreamReader(huc.getInputStream(), "utf-8"));
                int ch; StringBuffer sb = new StringBuffer();
                while ((ch = is.read()) != -1)
                    sb.append((char) ch);
                if (is != null)
                    is.close();
                sResult = sb.toString();
            }catch (Exception e){ }
            return sResult;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute ( result );
            try {
                if (sResult.contains("SUCCESS")) {
                    String unick, uemail;
                    try {
                        unick = sResult.substring(sResult.indexOf("&ID:") + 4, sResult.indexOf("&EMAIL:"));
                        uemail = sResult.substring(sResult.indexOf("&EMAIL:") + 7, sResult.length());
                        onLoginSuccess(unick, uemail);
                    } catch (Exception err) {
                        Log.d("DEVSLIB_MEMBER", "LIB :: CONNECT FAIL : " + sResult);
                        onLoginFailed(-4);
                    }
                } else if (sResult.contains("NO ID")) {
                    onLoginFailed(-2);
                } else if (sResult.contains("NO VERIFY")) {
                    onLoginFailed(-3);
                } else if (sResult.contains("NO MATCH")) {
                    onLoginFailed(-1);
                } else {
                    Log.d("DEVSLIB_MEMBER", "LIB :: LOGIN FAIL : " + sResult);
                    onLoginFailed(-4);
                }
            } catch (NullPointerException err){
                Log.d("DEVSLIB_MEMBER", "LIB :: LOGIN FAIL : NULL");
                onLoginFailed(-5);
            }
        }
    }


    private class findAccTask extends AsyncTask<String, Void, String> {

        String sResult;
        @Override
        protected String doInBackground(String... sId) {
            try{
                String body = "appcode="+APP_NUM+"&useremail="+USR_EMAIL;
                URL u = new URL(OnlineDatas.ONLINE_MEMBER_FIND_ACC);
                HttpURLConnection huc = (HttpURLConnection) u.openConnection();
                huc.setReadTimeout(6000); huc.setConnectTimeout(6000);
                huc.setRequestMethod("POST"); huc.setDoInput(true); huc.setDoOutput(true);
                huc.setRequestProperty("utf-8", "application/x-www-form-urlencoded");
                OutputStream os = huc.getOutputStream();
                os.write( body.getBytes("utf-8")); os.flush(); os.close();
                BufferedReader is = new BufferedReader(new InputStreamReader(huc.getInputStream(), "utf-8"));
                int ch; StringBuffer sb = new StringBuffer();
                while ((ch = is.read()) != -1)
                    sb.append((char) ch);
                if (is != null)
                    is.close();
                sResult = sb.toString();
            }catch (Exception e){ }
            return sResult;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute ( result );
            try {
                if (sResult.contains("SUCCESS")) {
                    onFindAccountSuccess();
                } else if (sResult.contains("NO EMAIL")) {
                    onFindAccountFailed(-1);
                } else {
                    Log.d("DEVSLIB_MEMBER", "LIB :: FINDACCOUNT FAIL : " + sResult);
                    onFindAccountFailed(-2);
                }
            } catch (NullPointerException err){
                Log.d("DEVSLIB_MEMBER", "LIB :: FINDACCOUNT FAIL : NULL");
                onFindAccountFailed(-3);
            }
        }
    }

    private class doRegisterTask extends AsyncTask<String, Void, String> {

        String sResult;
        @Override
        protected String doInBackground(String... sId) {
            try{
                String body = "appcode="+APP_NUM+"&userid="+USR_ID+"&userpw="+USR_PW+"&usernick="+USR_NICK+"&useremail="+USR_EMAIL+"&userverify="+USR_VERIFY;
                URL u = new URL(OnlineDatas.ONLINE_MEMBER_REGISTER);
                HttpURLConnection huc = (HttpURLConnection) u.openConnection();
                huc.setReadTimeout(6000); huc.setConnectTimeout(6000);
                huc.setRequestMethod("POST"); huc.setDoInput(true); huc.setDoOutput(true);
                huc.setRequestProperty("utf-8", "application/x-www-form-urlencoded");
                OutputStream os = huc.getOutputStream();
                os.write( body.getBytes("utf-8")); os.flush(); os.close();
                BufferedReader is = new BufferedReader(new InputStreamReader(huc.getInputStream(), "utf-8"));
                int ch; StringBuffer sb = new StringBuffer();
                while ((ch = is.read()) != -1)
                    sb.append((char) ch);
                if (is != null)
                    is.close();
                sResult = sb.toString();
            }catch (Exception e){ }
            return sResult;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute ( result );
            try {
                if (sResult.contains("SUCCESS")) {
                    onRegisterSuccess();
                } else if (sResult.contains("DUPLICATE ID")) {
                    onRegisterFailed(-1);
                } else if (sResult.contains("DUPLICATE NICK")) {
                    onRegisterFailed(-2);
                } else if (sResult.contains("DUPLICATE EMAIL")) {
                    onRegisterFailed(-3);
                } else {
                    Log.d("DEVSLIB_MEMBER", "LIB :: REGISTER FAIL : " + sResult);
                    onRegisterFailed(-4);
                }
            } catch (NullPointerException err){
                Log.d("DEVSLIB_MEMBER", "LIB :: REGISTER FAIL : NULL");
                onRegisterFailed(-5);
            }
        }
    }

    private class signOutTask extends AsyncTask<String, Void, String> {

        String sResult;
        @Override
        protected String doInBackground(String... sId) {
            try{
                String body = "appcode="+APP_NUM+"&userid="+USR_ID+"&userpw="+USR_PW;
                URL u = new URL(OnlineDatas.ONLINE_MEMBER_SIGNOUT);
                HttpURLConnection huc = (HttpURLConnection) u.openConnection();
                huc.setReadTimeout(6000); huc.setConnectTimeout(6000);
                huc.setRequestMethod("POST"); huc.setDoInput(true); huc.setDoOutput(true);
                huc.setRequestProperty("utf-8", "application/x-www-form-urlencoded");
                OutputStream os = huc.getOutputStream();
                os.write( body.getBytes("utf-8")); os.flush(); os.close();
                BufferedReader is = new BufferedReader(new InputStreamReader(huc.getInputStream(), "utf-8"));
                int ch; StringBuffer sb = new StringBuffer();
                while ((ch = is.read()) != -1)
                    sb.append((char) ch);
                if (is != null)
                    is.close();
                sResult = sb.toString();
            }catch (Exception e){ }
            return sResult;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute ( result );
            try {
                if (sResult.contains("SUCCESS")) {
                    onSignoutSuccess();
                } else if (sResult.contains("NO ID")) {
                    onSignoutFailed(-2);
                } else if (sResult.contains("NO MATCH")) {
                    onSignoutFailed(-1);
                } else {
                    Log.d("DEVSLIB_MEMBER", "LIB :: SIGNOUT FAIL : " + sResult);
                    onSignoutFailed(-3);
                }
            } catch (NullPointerException err){
                Log.d("DEVSLIB_MEMBER", "LIB :: SIGNOUT FAIL : NULL");
                onSignoutFailed(-4);
            }
        }
    }

}
