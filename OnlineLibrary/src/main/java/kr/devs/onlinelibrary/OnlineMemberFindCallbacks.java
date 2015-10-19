package kr.devs.onlinelibrary;

/**
 * Created by TK in DEVS
 */
public interface OnlineMemberFindCallbacks {
    void onFindAccountSuccess();
    void onFindAccountFailed(int ERROR_CODE);
}
