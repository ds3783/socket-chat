/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-9-19
 * Time: ����11:07
 * To change this template use File | Settings | File Templates.
 */
package net.ds3783.chatserver {
import flash.events.EventDispatcher;

import net.ds3783.chatserver.messages.ChannelListMessage;
import net.ds3783.chatserver.messages.ClientListMessage;
import net.ds3783.chatserver.messages.CommandMessage;
import net.ds3783.chatserver.messages.LoginMessage;
import net.ds3783.chatserver.messages.PublicMessage;
import net.ds3783.chatserver.messages.SystemReplyMessage;
import net.ds3783.chatserver.messages.model.ChannelModel;

public class ChatServerClient extends EventDispatcher {

    public static const LOGINED:String = "LOGINED";
    public static const CONNECTED:String = "CONNECTED";
    public static const DISCONNECTED:String = "DISCONNECTED";
    public static const CONNECTED_ERROR:String = "CONNECTED_ERROR";
    public static const LOGIN_ERROR:String = "LOGIN_ERROR";
    public static const CHANNEL_LIST_UPDATE:String = "CHANNEL_LIST_UPDATE";
    public static const CLIENT_LIST_UPDATE:String = "CLIENT_LIST_UPDATE";
    public static const CHANNEL_JOINED:String = "CHANNEL_JOINED";
    public static const MESSAGE:String = "MESSAGE";
    public static const ERROR:String = "ERROR";
    public static const BEFORE_DISCONNECT:String = "BEFORE_DISCONNECT";

    public function ChatServerClient(autoJoin:Boolean = true) {
        connType = CONN_TYPE_SOCKET;
        socket = new SocketClient();
        socket.addEventListener(SocketClient.EVENT_CONNECTED, onConnected);
        socket.addEventListener(SocketClient.EVENT_LOGIN, onLogined);
        socket.addEventListener(SocketClient.EVENT_DISCONNECTED, onDisconnected);
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
    private var listeningChannels:Array;

    private var autoJoinDefaultChannel:Boolean;

    private var socket:SocketClient;

    public function connect(host:String, port:int, username:String, password:String) {
        _connHost = host;
        _connPort = port;
        this._username = username;
        this._password = password;
        this.listeningChannels = new Array();
        socket.addEventListener(SocketClient.EVENT_CHANNEL_LIST_UPDATE, onChannelListUpdate);
        socket.addEventListener(SocketClient.EVENT_CLIENT_LIST_UPDATE, onClientListUpdate);
        socket.addEventListener(SocketClient.EVENT_USERERROR, onError);
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
        //todo::  ��δ����
        var message:CommandMessage = new CommandMessage();
        message.command = CommandType.EXIT_CHANNEL;
        message.content = channelId;
        socket.sendMessage(message);
    }

    public function createRoom(text:String):void {
        if (!text) {
            throw new ChatServerError("Empty room name!");
        }
        var message:CommandMessage = new CommandMessage();
        message.command = CommandType.CREATE_CHANNEL;
        message.content = text;
        socket.sendMessage(message);
    }


    public function sendPrivateMessage(id:String, text:String):void {

    }

    public function sendMessage(id:String, text:String):void {
        if (!text || !id) {
            return;
        }
        var message:PublicMessage = new PublicMessage();
        message.channelId = Number(id);
        message.content = text;
        socket.sendMessage(message);
    }

    private function onChannelListUpdate(e:SocketEvent):void {
        var list:ChannelListMessage = e.message as ChannelListMessage;
        if (list) {
            if (list.listeningChannels) {
                //�뻺�������ҳ�����
                var misMach:Boolean = false;
                if (this.listeningChannels.length != list.listeningChannels.length) {
                    misMach = true;
                }
                if (!misMach) {
                    var copy = list.listeningChannels.slice(0, list.listeningChannels.length);
                    for each(var channel:Number in this.listeningChannels) {
                        if (copy.indexOf(channel) >= 0) {
                            copy.splice(copy.indexOf(channel), 1);
                        } else {
                            misMach = true;
                            break;
                        }
                    }
                }
                //������ڲ���
                if (misMach) {
                    this.listeningChannels = list.listeningChannels;
                    //����update client listָ��
                    var message:CommandMessage = new CommandMessage();
                    message.command = CommandType.UPDATE_CLIENT_LIST;
                    socket.sendMessage(message);
                }
            }
            if (autoJoinDefaultChannel && !(list.listeningChannels && list.listeningChannels.length > 0)) {
                //auto join default Channel
                for each (var channelModel:ChannelModel in list.channels) {
                    if (channelModel.defaultChannel) {
                        var msg:CommandMessage = new CommandMessage();
                        msg.command = CommandType.JOIN_CHANNEL;
                        msg.content = channelModel.id.toString(10);
                        socket.sendMessage(msg);
                        break;
                    }
                }
            }
            var event:ChatEvent = new ChatEvent(CHANNEL_LIST_UPDATE);
            event.message = list;
            dispatchEvent(event);
        }

    }

    private function onClientListUpdate(e:SocketEvent):void {
        var msg:ClientListMessage = e.message as ClientListMessage;
        var event:ChatEvent = new ChatEvent(CLIENT_LIST_UPDATE);
        event.message = msg;
        dispatchEvent(event);
    }

    private function onError(e:SocketEvent):void {
        var msg:SystemReplyMessage = e.message as SystemReplyMessage;
        var event:ChatEvent = new ChatEvent(ERROR);
        event.message = msg;
        dispatchEvent(event);
    }

    private function onDisconnected(e:SocketEvent):void {
        dispatchEvent(new ChatEvent(DISCONNECTED));
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
