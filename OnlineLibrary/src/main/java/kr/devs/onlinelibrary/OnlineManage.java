package kr.devs.onlinelibrary;

import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

/**
 * Created by TK in DEVS
 */
public class OnlineManage {

    int APP_NUM;
    String NTITLE,NCONTENT;
    int NCODE;
    int SSTATE;
    String SEXPLAIN;

    private OnlineManageConnectCallbacks mConnectCallbacks;
    private OnlineManageGetNoticeCallbacks mGetNoticeCallbacks;
    private OnlineManageGetStateCallbacks mGetStateCallbacks;
    private OnlineManageSetNoticeCallbacks mSetNoticeCallbacks;
    private OnlineManageSetStateCallbacks mSetStateCallbacks;

    public OnlineManage(int appid,int appcode){
        APP_NUM = Integer.parseInt(Integer.toString(appid)+Integer.toString(appcode));
    }

    public void connect(){
        connectServerTask cST = new connectServerTask();
        cST.execute();
    }

    public void setNotice(String title,String content){
        Random generator = new Random();
        int NOTICE_CODE = generator.nextInt(9999)+1;
        NTITLE = title;
        NCONTENT = content;
        NCODE = NOTICE_CODE;
        setNoticeTask sNT = new setNoticeTask();
        sNT.execute();
    }

    public void getNotice(){
        getNoticeTask gNT = new getNoticeTask();
        gNT.execute();
    }

    public void setServerState(int SERVER_STATE,String EXPLAIN){
        // SERVER_STATE == 0 => NORMAL
        // SERVER_STATE == 1 => FIX (service available)
        // SERVER_STATE == 2 => FIX (service not available)
        // SERVER_STATE == 3 => SERVER DOWN
        SSTATE = SERVER_STATE;
        SEXPLAIN = EXPLAIN;
        setServerTask sST = new setServerTask();
        sST.execute();
    }

    public void getServerState(){
        getServerTask gST = new getServerTask();
        gST.execute();
    }

    public void setOnlineManageConnectCallbacks(OnlineManageConnectCallbacks listener){mConnectCallbacks = listener;}
    public void setOnlineManageGetNoticeCallbacks(OnlineManageGetNoticeCallbacks listener){mGetNoticeCallbacks = listener;}
    public void setOnlineManageGetStateCallbacks(OnlineManageGetStateCallbacks listener){mGetStateCallbacks = listener;}
    public void setOnlineManageSetNoticeCallbacks(OnlineManageSetNoticeCallbacks listener){mSetNoticeCallbacks = listener;}
    public void setOnlineManageSetStateCallbacks(OnlineManageSetStateCallbacks listener){mSetStateCallbacks = listener;}

    public void onConnectSuccess(){mConnectCallbacks.onConnectionSuccess();}
    public void onConnectFailed(int ERROR_CODE){mConnectCallbacks.onConnectionFailed(ERROR_CODE);}
    public void onGetNoticeSuccess(String title,String content,int RCODE){mGetNoticeCallbacks.onGetNoticeSuccess(title, content,RCODE);}
    public void onGetNoticeFailed(int ERROR_CODE){mGetNoticeCallbacks.onGetNoticeFailed(ERROR_CODE);}
    public void onGetStateSuccess(int CURRENT_STATE,String STATE_EXPLAIN){mGetStateCallbacks.onGetStateSuccess(CURRENT_STATE, STATE_EXPLAIN);}
    public void onGetStateFailed(int ERROR_CODE){mGetStateCallbacks.onGetStateFailed(ERROR_CODE);}
    public void onSetNoticeSuccess(){mSetNoticeCallbacks.onSetNoticeSuccess();}
    public void onSetNoticeFailed(int ERROR_CODE){mSetNoticeCallbacks.onSetNoticeFailed(ERROR_CODE);}
    public void onSetStateSuccess(){mSetStateCallbacks.onSetStateSuccess();}
    public void onSetStateFailed(int ERROR_CODE){mSetStateCallbacks.onSetStateFailed(ERROR_CODE);}


    private class connectServerTask extends AsyncTask<String, Void, String> {

        String sResult;
        @Override
        protected String doInBackground(String... sId) {
            try{
                String body = "appcode="+APP_NUM;
                URL u = new URL(OnlineDatas.ONLINE_MANAGE_STATUS);
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
                    Log.d("DEVSLIB_MANAGE", "LIB :: CONNECT FAIL : " + sResult);
                    onConnectFailed(-1);
                }
            } catch (NullPointerException err){
                Log.d("DEVSLIB_MANAGE", "LIB :: CONNECT FAIL : NULL");
                onConnectFailed(-2);
            }
        }
    }

    private class getNoticeTask extends AsyncTask<String, Void, String> {

        String sResult;
        @Override
        protected String doInBackground(String... sId) {
            try{
                String body = "appcode="+APP_NUM;
                URL u = new URL(OnlineDatas.ONLINE_MANAGE_NOTICE_GET);
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
                    String title,content;
                    int rcode;
                    try {
                        title = sResult.substring(sResult.indexOf("&TITLE:") + 7, sResult.indexOf("&CONTENT:"));
                        content = sResult.substring(sResult.indexOf("&CONTENT:") + 9, sResult.indexOf("&RCODE:"));
                        rcode = Integer.parseInt(sResult.substring(sResult.indexOf("&RCODE:")+7,sResult.length()));
                        onGetNoticeSuccess(title, content, rcode);
                    } catch (Exception err){
                        Log.d("DEVSLIB_MANAGE", "LIB :: GET NOTICE FAIL : " + sResult);
                        onGetNoticeFailed(-2);
                    }
                } else if (sResult.contains("NO NOTICE")){
                    onGetNoticeFailed(-1);
                } else {
                    Log.d("DEVSLIB_MANAGE", "LIB :: GET NOTICE FAIL : " + sResult);
                    onGetNoticeFailed(-2);
                }
            } catch (NullPointerException err){
                Log.d("DEVSLIB_MANAGE", "LIB :: GET NOTICE FAIL : NULL");
                onGetNoticeFailed(-3);
            }
        }
    }

    private class getServerTask extends AsyncTask<String, Void, String> {

        String sResult;
        @Override
        protected String doInBackground(String... sId) {
            try{
                String body = "appcode="+APP_NUM;
                URL u = new URL(OnlineDatas.ONLINE_MANAGE_SERVERSTATE_GET);
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
                    int server_state;
                    String explain;
                    try {
                        server_state = Integer.parseInt(sResult.substring(sResult.indexOf("&STATE:")+7,sResult.indexOf("&EXPLAIN:")));
                        explain = sResult.substring(sResult.indexOf("&EXPLAIN:") + 9, sResult.length());
                        onGetStateSuccess(server_state, explain);
                    } catch (Exception err){
                        Log.d("DEVSLIB_MANAGE", "LIB :: GET STATE FAIL : " + sResult);
                        onGetStateFailed(-2);
                    }
                } else if (sResult.contains("NO STATE")){
                    onGetStateFailed(-1);
                } else {
                    Log.d("DEVSLIB_MANAGE", "LIB :: GET STATE FAIL : " + sResult);
                    onGetStateFailed(-2);
                }
            } catch (NullPointerException err){
                Log.d("DEVSLIB_MANAGE", "LIB :: GET STATE FAIL : NULL");
                onGetStateFailed(-3);
            }
        }
    }

    private class setNoticeTask extends AsyncTask<String, Void, String> {

        String sResult;
        @Override
        protected String doInBackground(String... sId) {
            try{
                String body = "appcode="+APP_NUM+"&title="+NTITLE+"&content="+NCONTENT+"&rcode="+Integer.toString(NCODE);
                URL u = new URL(OnlineDatas.ONLINE_MANAGE_NOTICE_SET);
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
                    onSetNoticeSuccess();
                } else {
                    Log.d("DEVSLIB_MANAGE", "LIB :: SET NOTICE FAIL : " + sResult);
                    onSetNoticeFailed(-1);
                }
            } catch (NullPointerException err){
                Log.d("DEVSLIB_MANAGE", "LIB :: SET NOTICE FAIL : NULL");
                onSetNoticeFailed(-2);
            }
        }
    }

    private class setServerTask extends AsyncTask<String, Void, String> {

        String sResult;
        @Override
        protected String doInBackground(String... sId) {
            try{
                String body = "appcode="+APP_NUM+"&state="+Integer.toString(SSTATE)+"&ps="+SEXPLAIN;
                URL u = new URL(OnlineDatas.ONLINE_MANAGE_SERVERSTATE_SET);
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
                    onSetStateSuccess();
                } else {
                    Log.d("DEVSLIB_MANAGE", "LIB :: SET STATE FAIL : " + sResult);
                    onSetStateFailed(-1);
                }
            } catch (NullPointerException err){
                Log.d("DEVSLIB_MANAGE", "LIB :: SET STATE FAIL : NULL");
                onSetStateFailed(-2);
            }
        }
    }

}
