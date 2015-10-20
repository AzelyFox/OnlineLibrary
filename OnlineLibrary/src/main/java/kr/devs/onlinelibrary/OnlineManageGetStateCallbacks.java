package kr.devs.onlinelibrary;

/**
 * Created by TK in DEVS
 */
public interface OnlineManageGetStateCallbacks {
    void onGetStateSuccess(int CURRENT_STATE,String STATE_EXPLAIN);
    void onGetStateFailed(int ERROR_CODE);
}
