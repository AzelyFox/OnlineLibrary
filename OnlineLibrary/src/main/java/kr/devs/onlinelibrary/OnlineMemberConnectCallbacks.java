package kr.devs.onlinelibrary;

/**
 * Created by TK in DEVS
 */
public interface OnlineMemberConnectCallbacks {
    void onConnectionSuccess();
    void onConnectionFailed(int ERROR_CODE);
}
