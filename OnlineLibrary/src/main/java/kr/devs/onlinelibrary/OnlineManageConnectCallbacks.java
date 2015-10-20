package kr.devs.onlinelibrary;

/**
 * Created by TK in DEVS
 */
public interface OnlineManageConnectCallbacks {
    void onConnectionSuccess();
    void onConnectionFailed(int ERROR_CODE);
}
