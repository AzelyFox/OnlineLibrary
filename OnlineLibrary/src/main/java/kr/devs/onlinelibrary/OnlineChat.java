package kr.devs.onlinelibrary;

import android.util.Log;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

/**
* Created by TK in DEVS
*/

public class OnlineChat {
    public int ROOM_NUM;
    public String USER_NAME;
    public Socket mSocket;
    private OnlineChatCallbacks mCallbacks;

    public OnlineChat(int room_num){
        ROOM_NUM = room_num;
        Log.d("DEVSLIB_CHAT", "LIB :: CHAT NEW");
    }

    public void make(){
        try {
            mSocket = IO.socket(OnlineDatas.ONLINE_CHAT_SERVER);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("login", onLogin);
        mSocket.on("new message", onNewMessage);
        mSocket.on("user joined", onUserJoined);
        mSocket.on("user left", onUserLeft);
        mSocket.on("typing", onTyping);
        mSocket.on("stop typing", onStopTyping);
        Log.d("DEVSLIB_CHAT", "LIB :: CHAT MADE");
    }

    public void connect(String user_nick){
        mSocket.connect();
        USER_NAME = user_nick;
        setUsername(user_nick);
        mSocket.emit("add user", ROOM_NUM);
        Log.d("DEVSLIB_CHAT", "LIB :: CHAT CONNECTED");
    }

    public void setUsername(String user_nick){
        mSocket.emit("set username", user_nick);
    }

    public void send(String message){
        mSocket.emit("new message", message);
        Log.d("DEVSLIB_CHAT", "LIB :: CHAT SENT");
    }

    public void istyping(){
        mSocket.emit("typing");
        Log.d("DEVSLIB_CHAT", "LIB :: TYPING SENT");
    }

    public void stoptyping(){
        mSocket.emit("stop typing");
        Log.d("DEVSLIB_CHAT", "LIB :: TYPING STOP SENT");
    }

    public void disconnect(){
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("login", onLogin);
        mSocket.off("new message", onNewMessage);
        mSocket.off("user joined", onUserJoined);
        mSocket.off("user left", onUserLeft);
        mSocket.off("typing", onTyping);
        mSocket.off("stop typing", onStopTyping);
        Log.d("DEVSLIB_CHAT", "LIB :: CHAT DISCONNECTED");
    }

    public boolean connected(){
        return mSocket.connected();
    }

    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("DEVSLIB_CHAT", "LIB :: ONCONNECTERROR");
            onConnectError();
        }
    };

    private Emitter.Listener onLogin = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.d("DEVSLIB_CHAT", "LIB :: ONLOGIN");
            JSONObject data = (JSONObject) args[0];
            int numUsers;
            try {
                numUsers = data.getInt("numUsers");
            } catch (JSONException e) {
                return;
            }
            onLoginSuccess(numUsers);
        }
    };

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d("DEVSLIB_CHAT", "LIB :: ONNEWMESSAGE");
            JSONObject data = (JSONObject) args[0];
            String username;
            String message;
            try {
                username = data.getString("username");
                message = data.getString("message");
            } catch (JSONException e) {
                return;
            }
            onNewMessage(username,message);
        }
    };

    private Emitter.Listener onUserJoined = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d("DEVSLIB_CHAT", "LIB :: ONUSERJOIN");
            JSONObject data = (JSONObject) args[0];
            String username;
            int numUsers;
            try {
                username = data.getString("username");
                numUsers = data.getInt("numUsers");
            } catch (JSONException e) {
                return;
            }
            onUserJoin(username,numUsers);
        }
    };

    private Emitter.Listener onUserLeft = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d("DEVSLIB_CHAT", "LIB :: ONUSERLEFT");
            JSONObject data = (JSONObject) args[0];
            String username;
            int numUsers;
            try {
                username = data.getString("username");
                numUsers = data.getInt("numUsers");
            } catch (JSONException e) {
                return;
            }
            onUserLeft(username,numUsers);
        }
    };

    private Emitter.Listener onTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d("DEVSLIB_CHAT", "LIB :: ONTYPING");
            JSONObject data = (JSONObject) args[0];
            String username;
            try {
                username = data.getString("username");
            } catch (JSONException e) {
                return;
            }
            onStartTyping(username);
        }
    };

    private Emitter.Listener onStopTyping = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            Log.d("DEVSLIB_CHAT", "LIB :: ONSTOPTYPING");
            JSONObject data = (JSONObject) args[0];
            String username;
            try {
                username = data.getString("username");
            } catch (JSONException e) {
                return;
            }
            onStopTyping(username);
        }
    };

    public void setOnlineChatCallbacks(OnlineChatCallbacks listener){
        mCallbacks = listener;
        Log.d("DEVSLIB_CHAT", "LIB :: CALLBACK SET");
    }

    public void onLoginSuccess(int total_num){
        mCallbacks.onLoginSuccess(total_num);
    }

    public void onNewMessage(String user_nick,String message){
        mCallbacks.onNewMessage(user_nick,message);
    }

    public void onUserJoin(String user_nick,int total_num){
        mCallbacks.onUserJoin(user_nick,total_num);
    }

    public void onUserLeft(String user_nick,int total_num){
        mCallbacks.onUserLeft(user_nick,total_num);
    }

    public void onStartTyping(String user_nick){
        mCallbacks.onStartTyping(user_nick);
    }

    public void onStopTyping(String user_nick){
        mCallbacks.onStopTyping(user_nick);
    }

    public void onConnectError(){
        mCallbacks.onConnectError();
    }

}
