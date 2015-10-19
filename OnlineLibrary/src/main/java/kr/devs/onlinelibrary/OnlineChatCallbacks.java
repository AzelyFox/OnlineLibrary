package kr.devs.onlinelibrary;

/**
 * Created by TK in DEVS
 */
public interface OnlineChatCallbacks {
    void onLoginSuccess(int total_num);
    void onNewMessage(String user_nick,String message);
    void onUserJoin(String user_nick,int total_num);
    void onUserLeft(String user_nick,int total_num);
    void onStartTyping(String user_nick);
    void onStopTyping(String user_nick);
    void onConnectError();
}
