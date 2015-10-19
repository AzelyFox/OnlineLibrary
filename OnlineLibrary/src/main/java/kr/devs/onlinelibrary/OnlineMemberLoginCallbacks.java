package kr.devs.onlinelibrary;

/**
 * Created by TK in DEVS
 */
public interface OnlineMemberLoginCallbacks {
    void onLoginSuccess(String USER_NICK,String USER_EMAIL);
    void onLoginFailed(int ERROR_CODE);
}
