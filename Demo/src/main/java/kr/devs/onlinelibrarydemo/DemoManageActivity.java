package kr.devs.onlinelibrarydemo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import kr.devs.onlinelibrary.OnlineManage;
import kr.devs.onlinelibrary.OnlineManageConnectCallbacks;
import kr.devs.onlinelibrary.OnlineManageGetNoticeCallbacks;
import kr.devs.onlinelibrary.OnlineManageGetStateCallbacks;
import kr.devs.onlinelibrary.OnlineManageSetNoticeCallbacks;
import kr.devs.onlinelibrary.OnlineManageSetStateCallbacks;

/**
 * Created by TK in DEVS
 */
public class DemoManageActivity extends Activity {

    LinearLayout set_layout,check_layout;
    TextView set_switcher,check_switcher;
    Button set_notice_apply,set_state_apply,check_get_notice,check_get_state;
    EditText set_notice_title,set_notice_content,set_state_state,set_state_explain;

    OnlineManage mManage;

    ProgressDialog mProgressDialog;

    @Override
    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.demomanage);

        setViewIds();

        mManage = new OnlineManage(1234,1234);
        mManage.setOnlineManageConnectCallbacks(new OnlineManageConnectCallbacks() {
            @Override
            public void onConnectionSuccess() {
                dismissProgressDialog();
                showMessage("SERVER CONNECTED");
            }

            @Override
            public void onConnectionFailed(int ERROR_CODE) {
                dismissProgressDialog();
                // ERROR CODE == -1 => UNKNOWN ERROR
                // ERROR CODE == -2 => SERVER TIMEOUT
                if (ERROR_CODE == -1){
                    showMessage("UNKNOWN ERROR :(");
                } else {
                    showMessage("SERVER TIMEOUT :(");
                }
            }
        });
        mManage.setOnlineManageSetNoticeCallbacks(new OnlineManageSetNoticeCallbacks() {
            @Override
            public void onSetNoticeSuccess() {
                dismissProgressDialog();
                showMessage("SET NOTICE SUCCESS!");
            }

            @Override
            public void onSetNoticeFailed(int ERROR_CODE) {
                dismissProgressDialog();
                // ERROR CODE == -1 => UNKNOWN ERROR
                // ERROR CODE == -2 => SERVER TIMEOUT
                if (ERROR_CODE == -1) {
                    showMessage("UNKNOWN ERROR :(");
                } else {
                    showMessage("SERVER TIMEOUT :(");
                }
            }
        });
        mManage.setOnlineManageGetNoticeCallbacks(new OnlineManageGetNoticeCallbacks() {
            @Override
            public void onGetNoticeSuccess(String TITLE, String CONTENT, int RCODE) {
                dismissProgressDialog();
                showMessage("TITLE : " + TITLE + "\n" + "CONTENT : " + CONTENT);
            }

            @Override
            public void onGetNoticeFailed(int ERROR_CODE) {
                dismissProgressDialog();
                // ERROR CODE == -1 => NO NOTICE SET
                // ERROR CODE == -2 => UNKNOWN ERROR
                // ERROR CODE == -3 => SERVER TIMEOUT
                if (ERROR_CODE == -1) {
                    showMessage("NO NOTICE HAS BEEN SET");
                } else if (ERROR_CODE == -2) {
                    showMessage("UNKNOWN ERROR :(");
                } else {
                    showMessage("SERVER TIMEOUT :(");
                }
            }
        });
        mManage.setOnlineManageSetStateCallbacks(new OnlineManageSetStateCallbacks() {
            @Override
            public void onSetStateSuccess() {
                dismissProgressDialog();
                showMessage("SET STATE SUCCESS!");
            }

            @Override
            public void onSetStateFailed(int ERROR_CODE) {
                dismissProgressDialog();
                // ERROR CODE == -1 => UNKNOWN ERROR
                // ERROR CODE == -2 => SERVER TIMEOUT
                if (ERROR_CODE == -1) {
                    showMessage("UNKNOWN ERROR :(");
                } else {
                    showMessage("SERVER TIMEOUT :(");
                }
            }
        });
        mManage.setOnlineManageGetStateCallbacks(new OnlineManageGetStateCallbacks() {
            @Override
            public void onGetStateSuccess(int CURRENT_STATE, String STATE_EXPLAIN) {
                dismissProgressDialog();
                showMessage("STATE : " + Integer.toString(CURRENT_STATE) + "\n" + "EXPLAIN : " + STATE_EXPLAIN);
            }

            @Override
            public void onGetStateFailed(int ERROR_CODE) {
                dismissProgressDialog();
                // ERROR CODE == -1 => NO STATE SET
                // ERROR CODE == -2 => UNKNOWN ERROR
                // ERROR CODE == -3 => SERVER TIMEOUT
                if (ERROR_CODE == -1) {
                    showMessage("NO STATE HAS BEEN SET");
                } else if (ERROR_CODE == -2) {
                    showMessage("UNKNOWN ERROR :(");
                } else {
                    showMessage("SERVER TIMEOUT :(");
                }
            }
        });

        set_notice_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = set_notice_title.getText().toString();
                String content = set_notice_content.getText().toString();
                if (title.length() < 1 | content.length() < 1) {
                    showMessage("FILL ALL FORMS!");
                } else {
                    mManage.setNotice(title, content);
                    showProgressDialog("Trying to Set Notice...");
                }
            }
        });

        set_state_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String state_dummy = set_state_state.getText().toString();
                if (state_dummy != null && state_dummy.length() == 1){
                    int STATE = Integer.parseInt(set_state_state.getText().toString());
                    String explain = set_state_explain.getText().toString();
                    mManage.setServerState(STATE,explain);
                    showProgressDialog("Trying to Set State...");
                } else {
                    showMessage("FILL ALL FORMS!");
                }
            }
        });

        check_get_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mManage.getNotice();
                showProgressDialog("Trying to Get Notice...");
            }
        });

        check_get_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mManage.getServerState();
                showProgressDialog("Trying to Get State...");
            }
        });

        set_switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                set_layout.setVisibility(LinearLayout.GONE);
                check_layout.setVisibility(LinearLayout.VISIBLE);
            }
        });

        check_switcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check_layout.setVisibility(LinearLayout.GONE);
                set_layout.setVisibility(LinearLayout.VISIBLE);
            }
        });
    }

    void setViewIds(){
        set_layout = (LinearLayout) findViewById(R.id.scr_manage_set_layout);
        check_layout = (LinearLayout) findViewById(R.id.scr_manage_check_layout);
        set_switcher = (TextView) findViewById(R.id.scr_manage_set_changer);
        check_switcher = (TextView) findViewById(R.id.scr_manage_check_changer);
        set_notice_apply = (Button) findViewById(R.id.scr_manage_set_notice_apply);
        set_state_apply = (Button) findViewById(R.id.scr_manage_set_server_apply);
        check_get_notice = (Button) findViewById(R.id.scr_manage_check_get_notice);
        check_get_state = (Button) findViewById(R.id.scr_manage_check_get_state);
        set_notice_title = (EditText) findViewById(R.id.scr_manage_set_notice_title);
        set_notice_content = (EditText) findViewById(R.id.scr_manage_set_notice_content);
        set_state_state = (EditText) findViewById(R.id.scr_manage_set_server_state);
        set_state_explain = (EditText) findViewById(R.id.scr_manage_set_server_explain);
    }

    void showProgressDialog(String msg){
        if (mProgressDialog != null){
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
        mProgressDialog = ProgressDialog.show(DemoManageActivity.this, "connecting", msg, true);
    }

    void dismissProgressDialog(){
        if (mProgressDialog != null){
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
    void showMessage(String msg){
        Toast.makeText(DemoManageActivity.this,msg,Toast.LENGTH_SHORT).show();
    }
}
