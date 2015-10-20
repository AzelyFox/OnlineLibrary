package kr.devs.onlinelibrary;

/**
 * Created by TK in DEVS
 */
public class OnlineDatas {
    public static String ONLINE_SERVER_URL = "http://www.devs.kr";
    public static String ONLINE_CHAT_SERVER = "http://www.devs.kr:3001";
    public static String ONLINE_MEMBER_SERVER = ONLINE_SERVER_URL + "/sub/lib/Online_member/";
    public static String ONLINE_MEMBER_STATUS = ONLINE_MEMBER_SERVER + "status.php";
    public static String ONLINE_MEMBER_REGISTER = ONLINE_MEMBER_SERVER + "register.php";
    public static String ONLINE_MEMBER_LOGIN = ONLINE_MEMBER_SERVER + "login.php";
    public static String ONLINE_MEMBER_FIND_ACC = ONLINE_MEMBER_SERVER + "findacc.php";
    public static String ONLINE_MEMBER_SIGNOUT = ONLINE_MEMBER_SERVER + "signout.php";
    public static String ONLINE_MANAGE_SERVER = ONLINE_SERVER_URL + "/sub/lib/Online_manage/";
    public static String ONLINE_MANAGE_STATUS = ONLINE_MANAGE_SERVER + "status.php";
    public static String ONLINE_MANAGE_NOTICE_SET = ONLINE_MANAGE_SERVER + "notice_set.php";
    public static String ONLINE_MANAGE_SERVERSTATE_SET = ONLINE_MANAGE_SERVER + "serverstate_set.php";
    public static String ONLINE_MANAGE_NOTICE_GET = ONLINE_MANAGE_SERVER + "notice_get.php";
    public static String ONLINE_MANAGE_SERVERSTATE_GET = ONLINE_MANAGE_SERVER + "serverstate_get.php";
}
