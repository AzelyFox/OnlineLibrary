package kr.devs.onlinelibrary;

/**
 * Created by TK in DEVS
 */
public interface OnlineMemberRegisterCallbacks {
    void onRegisterSuccess();
    void onRegisterFailed(int ERROR_CODE);
}
