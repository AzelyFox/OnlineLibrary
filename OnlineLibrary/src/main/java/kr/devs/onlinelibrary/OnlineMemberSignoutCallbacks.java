package kr.devs.onlinelibrary;

/**
 * Created by TK in DEVS
 */
public interface OnlineMemberSignoutCallbacks {
    void onSignoutSuccess();
    void onSignoutFailed(int ERROR_CODE);
}
