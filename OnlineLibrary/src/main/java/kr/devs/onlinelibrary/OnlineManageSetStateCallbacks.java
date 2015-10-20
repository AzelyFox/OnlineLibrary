package kr.devs.onlinelibrary;

/**
 * Created by TK in DEVS
 */
public interface OnlineManageSetStateCallbacks {
    void onSetStateSuccess();
    void onSetStateFailed(int ERROR_CODE);
}
