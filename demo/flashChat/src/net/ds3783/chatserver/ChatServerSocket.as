/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-10-23
 * Time: ÏÂÎç7:44
 * To change this template use File | Settings | File Templates.
 */
package net.ds3783.chatserver {
import flash.events.EventDispatcher;

public class ChatServerSocket extends EventDispatcher{
     public static const LOGINED:String = "LOGINED";
    public static const CONNECTED:String = "CONNECTED";
    public static const CONNECTED_ERROR:String = "CONNECTED_ERROR";
    public static const LOGIN_ERROR:String = "LOGIN_ERROR";
    public static const CHANNEL_LIST_UPDATE:String = "CHANNEL_LIST_UPDATE";
    public static const CHANNEL_JOINED:String = "CHANNEL_JOINED";
    public static const MESSAGE:String = "MESSAGE";
    public static const BEFORE_DISCONNECT:String = "BEFORE_DISCONNECT";

    public function ChatServerSocket() {
        connType = CONN_TYPE_SOCKET;
        socket = new SocketClient();
        socket.addEventListener(SocketClient.EVENT_CONNECTED, onConnected);
        socket.addEventListener(SocketClient.EVENT_LOGIN, onLogined);
    }


    private static const CONN_TYPE_SOCKET:String = "SOCKET";
    private var connType:String;
    private var _connHost:String;
    private var _connPort:int;
    private var _username:String;
    private var _password:String;
    private var connected:Boolean;
    private var logined:Boolean;

    private var socket:SocketClient;

    public function connect(host:String, port:int, username:String, password:String) {
        _connHost = host;
        _connPort = port;
        this._username = username;
        this._password = password;
        socket.connect(_connHost, _connPort);
    }

    public function updateChannelList() {

    }


    private function onConnected(e:ChatEvent):void {
        connected = true;
        dispatchEvent(new ChatEvent(CONNECTED));
        switch (connType) {
            case CONN_TYPE_SOCKET:
                socket.login(_username, _password);
                break;
        }
    }

    private function onLogined(e:ChatEvent):void {
        logined = true;
        dispatchEvent(new ChatEvent(LOGINED));
    }


    public function get connHost():String {
        return _connHost;
    }

    public function get connPort():int {
        return _connPort;
    }

    public function get username():String {
        return _username;
    }

    public function get password():String {
        return _password;
    }
}
}
