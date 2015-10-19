package kr.devs.onlinelibrarydemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import kr.devs.onlinelibrary.OnlineMember;
import kr.devs.onlinelibrary.OnlineMemberConnectCallbacks;
import kr.devs.onlinelibrary.OnlineMemberFindCallbacks;
import kr.devs.onlinelibrary.OnlineMemberLoginCallbacks;
import kr.devs.onlinelibrary.OnlineMemberRegisterCallbacks;
import kr.devs.onlinelibrary.OnlineMemberSignoutCallbacks;

/**
 * Created by TK in DEVS
 */
public class DemoMemberActivity extends Activity {

    OnlineMember mMember;

    EditText reg_id,reg_pw,reg_nick,reg_email;
    CheckBox reg_verify;
    Button reg_register;

    EditText log_id,log_pw,log_email;
    Button log_login,log_signout,log_findacc;

    TextView reg_changer,log_changer;
    LinearLayout reg_layout,log_layout;

    ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.demomember);

        setViewIds();

        mMember = new OnlineMember(1234,1234);
        mMember.setOnlineMemberConnectCallbacks(new OnlineMemberConnectCallbacks() {
            @Override
            public void onConnectionSuccess() {
                dismissProgressDialog();
                showMessage("CONNECTED TO SERVER!");
            }

            @Override
            public void onConnectionFailed(int ERROR_CODE) {
                dismissProgressDialog();
                // ERROR CODE == -1 => UNKNOWN ERROR
                // ERROR CODE == -2 => SERVER TIMEOUT
                showMessage("SERVER IS OFF :(");
            }
        });
        mMember.setOnlineMemberLoginCallbacks(new OnlineMemberLoginCallbacks() {
            @Override
            public void onLoginSuccess(String USER_NICK, String USER_EMAIL) {
                dismissProgressDialog();
                showMessage("LOGIN SUCCESS!\n"+USER_NICK+"\n"+USER_EMAIL);
            }

            @Override
            public void onLoginFailed(int ERROR_CODE) {
                dismissProgressDialog();
                // ERROR CODE == -1 => PASSWORD NOT MATCH
                // ERROR CODE == -2 => ID NOT EXIST
                // ERROR CODE == -3 => NEED EMAIL VERIFICATION
                // ERROR CODE == -4 => UNKNOWN ERROR
                // ERROR CODE == -5 => SERVER TIMEOUT
                if (ERROR_CODE == -1){
                    showMessage("PASSWORD NOT MATCH");
                } else if (ERROR_CODE == -2){
                    showMessage("ID NOT EXIST");
                } else if (ERROR_CODE == -3){
                    showMessage("YOU NEED EMAIL VERIFICATION\nCHECK YOUR EMAIL!");
                } else {
                    showMessage("UNKNOWN ERROR :(");
                }
            }
        });
        mMember.setOnlineMemberRegisterCallbacks(new OnlineMemberRegisterCallbacks() {
            @Override
            public void onRegisterSuccess() {
                dismissProgressDialog();
                showMessage("REGISTER SUCCESS!");
            }

            @Override
            public void onRegisterFailed(int ERROR_CODE) {
                dismissProgressDialog();
                // ERROR CODE == -1 => ID DUPLICATED
                // ERROR CODE == -2 => NICK DUPLICATED
                // ERROR CODE == -3 => EMAIL DUPLICATED
                // ERROR CODE == -4 => UNKNOWN ERROR
                // ERROR CODE == -5 => SERVER TIMEOUT
                if (ERROR_CODE == -1){
                    showMessage("ID ALREADY EXIST");
                } else if (ERROR_CODE == -2){
                    showMessage("NICK ALREADY EXIST");
                } else if (ERROR_CODE == -3){
                    showMessage("EMAIL ALREADY EXIST");
                } else {
                    showMessage("UNKNOWN ERROR :(");
                }
            }
        });
        mMember.setOnlineMemberFindCallbacks(new OnlineMemberFindCallbacks() {
            @Override
            public void onFindAccountSuccess() {
                dismissProgressDialog();
                showMessage("EMAIL SENT!\nCHECK YOUR EMAIL");
            }

            @Override
            public void onFindAccountFailed(int ERROR_CODE) {
                dismissProgressDialog();
                // ERROR CODE == -1 => EMAIL NOT EXIST
                // ERROR CODE == -2 => UNKNOWN ERROR
                // ERROR CODE == -3 => SERVER TIMEOUT
                if (ERROR_CODE == -1){
                    showMessage("EMAIL NOT EXIST");
                } else {
                    showMessage("UNKNOWN ERROR :(");
                }
            }
        });
        mMember.setOnlinMemberSignoutCallbacks(new OnlineMemberSignoutCallbacks() {
            @Override
            public void onSignoutSuccess() {
                dismissProgressDialog();
                showMessage("YOUR ACCOUNT HAS BEEN DELETED!");
            }

            @Override
            public void onSignoutFailed(int ERROR_CODE) {
                dismissProgressDialog();
                // ERROR CODE == -1 => PASSWORD NOT MATCH
                // ERROR CODE == -2 => ID NOT EXIST
                // ERROR CODE == -3 => UNKNOWN ERROR
                // ERROR CODE == -4 => SERVER TIMEOUT
                if (ERROR_CODE == -1){
                    showMessage("PASSWORD NOT MATCH");
                } else if (ERROR_CODE == -2){
                    showMessage("ID NOT EXIST");
                } else {
                    showMessage("UNKNOWN ERROR :(");
                }
            }
        });

        mMember.connect();

        reg_changer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reg_layout.setVisibility(LinearLayout.GONE);
                log_layout.setVisibility(LinearLayout.VISIBLE);
            }
        });

        log_changer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log_layout.setVisibility(LinearLayout.GONE);
                reg_layout.setVisibility(LinearLayout.VISIBLE);
            }
        });

        reg_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String USER_ID = reg_id.getText().toString();
                String USER_PW = reg_pw.getText().toString();
                String USER_NICK = reg_nick.getText().toString();
                String USER_EMAIL = reg_email.getText().toString();
                boolean USER_VERIFY = reg_verify.isChecked();
                if (USER_ID.length()<3 | USER_PW.length()<4 | USER_NICK.length()<2 | USER_EMAIL.length()<5){
                    Toast.makeText(DemoMemberActivity.this,"FILL ALL EDIT FORMS!",Toast.LENGTH_SHORT).show();
                } else {
                    mMember.register(USER_ID,USER_PW,USER_NICK,USER_EMAIL,USER_VERIFY);
                    showProgressDialog("Trying to Register...");
                }
            }
        });

        log_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String USER_ID = log_id.getText().toString();
                String USER_PW = log_pw.getText().toString();
                if (USER_ID.length()<3 | USER_PW.length()<4){
                    Toast.makeText(DemoMemberActivity.this,"FILL ALL EDIT FORMS!",Toast.LENGTH_SHORT).show();
                } else {
                    mMember.login(USER_ID, USER_PW);
                    showProgressDialog("Trying to Login...");
                }
            }
        });

        log_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String USER_ID = log_id.getText().toString();
                String USER_PW = log_pw.getText().toString();
                if (USER_ID.length()<3 | USER_PW.length()<4){
                    Toast.makeText(DemoMemberActivity.this,"FILL ALL EDIT FORMS!",Toast.LENGTH_SHORT).show();
                } else {
                    mMember.signout(USER_ID, USER_PW);
                    showProgressDialog("Trying to Sign Out...");
                }
            }
        });

        log_findacc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String USER_EMAIL = log_email.getText().toString();
                if (USER_EMAIL.length()<5){
                    Toast.makeText(DemoMemberActivity.this,"FILL ALL EDIT FORMS!",Toast.LENGTH_SHORT).show();
                } else {
                    mMember.findaccount(USER_EMAIL);
                    showProgressDialog("Trying to Find Account...");
                }
            }
        });

    }

    void setViewIds(){
        reg_id = (EditText) findViewById(R.id.scr_member_reg_id);
        reg_pw = (EditText) findViewById(R.id.scr_member_reg_pw);
        reg_nick = (EditText) findViewById(R.id.scr_member_reg_nickname);
        reg_email = (EditText) findViewById(R.id.scr_member_reg_email);
        reg_verify = (CheckBox) findViewById(R.id.scr_member_reg_verify);
        reg_register = (Button) findViewById(R.id.scr_member_reg_register);
        log_id = (EditText) findViewById(R.id.scr_member_log_id);
        log_pw = (EditText) findViewById(R.id.scr_member_log_pw);
        log_email = (EditText) findViewById(R.id.scr_member_log_email);
        log_login = (Button) findViewById(R.id.scr_member_log_login);
        log_signout = (Button) findViewById(R.id.scr_member_log_signout);
        log_findacc = (Button) findViewById(R.id.scr_member_log_findacc);
        reg_changer = (TextView) findViewById(R.id.scr_member_reg_changer);
        log_changer = (TextView) findViewById(R.id.scr_member_log_changer);
        reg_layout = (LinearLayout) findViewById(R.id.scr_member_reg_layout);
        log_layout = (LinearLayout) findViewById(R.id.scr_member_log_layout);
    }

    void showProgressDialog(String msg){
        if (mProgressDialog != null){
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        mProgressDialog = ProgressDialog.show(DemoMemberActivity.this,"connecting",msg,true);
    }

    void dismissProgressDialog(){
        if (mProgressDialog != null){
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    void showMessage(String msg){
        Toast.makeText(DemoMemberActivity.this,msg,Toast.LENGTH_SHORT).show();
    }

}
