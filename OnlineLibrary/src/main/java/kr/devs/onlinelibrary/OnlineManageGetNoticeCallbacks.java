package kr.devs.onlinelibrary;

/**
 * Created by TK in DEVS
 */
public interface OnlineManageGetNoticeCallbacks {
    void onGetNoticeSuccess(String TITLE,String CONTENT,int RCODE);
    void onGetNoticeFailed(int ERROR_CODE);
}
