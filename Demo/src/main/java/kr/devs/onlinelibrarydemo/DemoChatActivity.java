package kr.devs.onlinelibrarydemo;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import kr.devs.onlinelibrary.OnlineChat;
import kr.devs.onlinelibrary.OnlineChatCallbacks;

/**
 * Created by TK in DEVS
 */

public class DemoChatActivity extends Activity {

    ScrollView scroller;
    TextView chatview,typeview;
    EditText editor,editor_nick;
    Button btn_send;

    OnlineChat mChat;
    int ROOM_NUMBER = 1234;
    String USER_NICK = "ABC";

    private boolean mTyping = false;
    private Handler mTypingHandler = new Handler();
    private static final int TYPING_TIMER_LENGTH = 700;

    @Override
    public void onCreate(Bundle SavedInstanceState){
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.demochat);

        Log.d("DEVSLIB_CHAT", "ACTIVITY STARTED");



        setViewIds();

        if (!getNETWORK()){
            addMessage("NETWORK NOT CONNECTED!");
            return;
        }

        setOnlineChat();

        editor_nick.setText(USER_NICK);

        editor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!mChat.connected()) return;

                if (!mTyping){
                    mTyping = true;
                    mChat.istyping();
                }

                mTypingHandler.removeCallbacks(onTypingTimeout);
                mTypingHandler.postDelayed(onTypingTimeout, TYPING_TIMER_LENGTH);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChat.setUsername(editor_nick.getText().toString());
                if (editor.getText().toString().length() > 0){
                    mChat.send(editor.getText().toString());
                    editor.setText("");
                }
            }
        });

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mChat.disconnect();
        Log.d("DEVSLIB_CHAT", "CHAT DISCONNECT");
    }

    private void setOnlineChat(){
        Log.d("DEVSLIB_CHAT","SET ONLINECHAT");
        Log.d("DEVSLIB_CHAT","TRY NEW CHAT");
        mChat = new OnlineChat(ROOM_NUMBER);
        Log.d("DEVSLIB_CHAT","CALL MAKE");
        mChat.make();
        Log.d("DEVSLIB_CHAT", "CALL CONNECT");
        mChat.connect(USER_NICK);

        mChat.setOnlineChatCallbacks(new OnlineChatCallbacks() {
            @Override
            public void onLoginSuccess(int total_num){
                Log.d("DEVSLIB_CHAT","CALLBACK :: LOGIN SUCCESS");
                addMessage("TOTAL USERS : " + Integer.toString(total_num));
            }

            @Override
            public void onNewMessage(String user_nick, String message) {
                Log.d("DEVSLIB_CHAT","CALLBACK :: NEW MESSAGE");
                addMessage(user_nick + "\t: " + message);
            }
            @Override
            public void onUserJoin(String user_nick, int total_num) {
                Log.d("DEVSLIB_CHAT","CALLBACK :: USER JOIN");
                addMessage(user_nick + " Joined!");
                addMessage("TOTAL USERS : " + Integer.toString(total_num));
            }
            @Override
            public void onUserLeft(String user_nick, int total_num) {
                Log.d("DEVSLIB_CHAT","CALLBACK :: USER LEFT");
                addMessage(user_nick + " Left");
                addMessage("TOTAL USERS : " + Integer.toString(total_num));
            }
            @Override
            public void onStartTyping(String user_nick) {
                Log.d("DEVSLIB_CHAT","CALLBACK :: START TYPING");
                showTyping(user_nick);
            }
            @Override
            public void onStopTyping(String user_nick) {
                Log.d("DEVSLIB_CHAT","CALLBACK :: STOP TYPING");
                stopTyping(user_nick);
            }
            @Override
            public void onConnectError() {
                Log.d("DEVSLIB_CHAT","CALLBACK :: CONNECT ERROR");
                addMessage("CONNECT ERROR OCCURRED");
            }
        });
    }

    private void addMessage(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                chatview.append(message + "\n");
                scroller.post(new Runnable() {
                    @Override
                    public void run() {
                        scroller.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        });
    }

    private void showTyping(final String user_nick){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                typeview.setText(user_nick + " is typing...");
            }
        });
    }

    private void stopTyping(final String user_nick){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                typeview.setText("");
            }
        });
    }

    private void setViewIds(){
        scroller = (ScrollView) findViewById(R.id.scr_chat_scrollview);
        chatview = (TextView) findViewById(R.id.scr_chat_chatview);
        typeview = (TextView) findViewById(R.id.scr_chat_typing);
        editor = (EditText) findViewById(R.id.scr_chat_editor);
        editor_nick = (EditText) findViewById(R.id.scr_chat_nick);
        btn_send = (Button) findViewById(R.id.scr_chat_send);
    }

    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            if (!mTyping) return;
            mTyping = false;
            mChat.stoptyping();
        }
    };

    public boolean getNETWORK(){
        boolean NETWORK = false,NETWORK_WIFI = false,NETWORK_MOBILE = false;
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(networkInfo != null)
            NETWORK_WIFI = networkInfo.isConnected();
        networkInfo = connMgr.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(networkInfo != null)
            NETWORK_MOBILE = networkInfo.isConnected();
        if(NETWORK_WIFI||NETWORK_MOBILE){
            NETWORK = true;
        }
        return NETWORK;
    }

}
