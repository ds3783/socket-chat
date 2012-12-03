/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-9-19
 * Time: ÏÂÎç11:07
 * To change this template use File | Settings | File Templates.
 */
package net.ds3783.chatserver {
import flash.events.EventDispatcher;

import net.ds3783.chatserver.messages.ChannelListMessage;
import net.ds3783.chatserver.messages.CommandMessage;
import net.ds3783.chatserver.messages.LoginMessage;
import net.ds3783.chatserver.messages.model.ChannelModel;

public class ChatServerClient extends EventDispatcher {

    public static const LOGINED:String = "LOGINED";
    public static const CONNECTED:String = "CONNECTED";
    public static const CONNECTED_ERROR:String = "CONNECTED_ERROR";
    public static const LOGIN_ERROR:String = "LOGIN_ERROR";
    public static const CHANNEL_LIST_UPDATE:String = "CHANNEL_LIST_UPDATE";
    public static const CHANNEL_JOINED:String = "CHANNEL_JOINED";
    public static const MESSAGE:String = "MESSAGE";
    public static const BEFORE_DISCONNECT:String = "BEFORE_DISCONNECT";

    public function ChatServerClient(autoJoin:Boolean = true) {
        connType = CONN_TYPE_SOCKET;
        socket = new SocketClient();
        socket.addEventListener(SocketClient.EVENT_CONNECTED, onConnected);
        socket.addEventListener(SocketClient.EVENT_LOGIN, onLogined);
        autoJoinDefaultChannel = autoJoin;
    }


    private static const CONN_TYPE_SOCKET:String = "SOCKET";
    private var connType:String;
    private var _connHost:String;
    private var _connPort:int;
    private var _username:String;
    private var _password:String;
    private var connected:Boolean;
    private var logined:Boolean;
    private var autoJoinDefaultChannel:Boolean;

    private var socket:SocketClient;

    public function connect(host:String, port:int, username:String, password:String) {
        _connHost = host;
        _connPort = port;
        this._username = username;
        this._password = password;
        socket.addEventListener(SocketClient.EVENT_CHANNEL_LIST_UPDATE, onChannelListUpdate);
        socket.connect(_connHost, _connPort);
    }


    private function onConnected(e:SocketEvent):void {
        connected = true;
        dispatchEvent(new SocketEvent(CONNECTED));
        switch (connType) {
            case CONN_TYPE_SOCKET:
                login(_username, _password);
                break;
        }
    }

    public function login(username:String, password:String):void {
        var message:LoginMessage = new LoginMessage();
        message.username = username;
        message.password = password;
        socket.sendMessage(message);
    }

    private function onLogined(e:SocketEvent):void {
        logined = true;
        dispatchEvent(new ChatEvent(LOGINED));
        listChannels();
    }

    public function listChannels():void {
        var message:CommandMessage = new CommandMessage();
        message.command = CommandType.LIST_CHANNELS;
        message.content = '';
        socket.sendMessage(message);
    }

    public function joinChannel(channelId:String):void {
        var message:CommandMessage = new CommandMessage();
        message.command = CommandType.JOIN_CHANNEL;
        message.content = channelId;
        socket.sendMessage(message);
    }

    public function exitChannel(channelId:String):void {
        //todo::  ÉÐÎ´²âÊÔ
        var message:CommandMessage = new CommandMessage();
        message.command = CommandType.EXIT_CHANNEL;
        message.content = channelId;
        socket.sendMessage(message);
    }

    public function createRoom(text:String):void {
        if (!text){
            throw new ChatServerError("Empty room name!");
        }
        var message:CommandMessage = new CommandMessage();
        message.command = CommandType.CREATE_CHANNEL;
        message.content = text;
        socket.sendMessage(message);
    }

    private function onChannelListUpdate(e:SocketEvent):void {
        var list:ChannelListMessage = e.message as ChannelListMessage;
        if (list) {
            if (!(list.listeningChannels && list.listeningChannels.length > 0) && autoJoinDefaultChannel) {
                //joinChannel
                for each (var channelModel:ChannelModel in list.channels) {
                    if (channelModel.defaultChannel) {
                        var message:CommandMessage = new CommandMessage();
                        message.command = CommandType.JOIN_CHANNEL;
                        message.content = channelModel.id.toString(10);
                        socket.sendMessage(message);
                        break;
                    }
                }
            }
            var event:ChatEvent = new ChatEvent(CHANNEL_LIST_UPDATE);
            event.message = list;
            dispatchEvent(event);
        }

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
