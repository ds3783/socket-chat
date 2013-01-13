/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-9-19
 * Time: 下午11:07
 * To change this template use File | Settings | File Templates.
 */
package net.ds3783.chatserver {
import flash.events.EventDispatcher;
import flash.net.registerClassAlias;

import net.ds3783.chatserver.messages.ChannelListMessage;
import net.ds3783.chatserver.messages.ChannelLostMessage;
import net.ds3783.chatserver.messages.ClientJoinChannelMessage;
import net.ds3783.chatserver.messages.ClientListMessage;
import net.ds3783.chatserver.messages.ClientLostMessage;
import net.ds3783.chatserver.messages.CommandMessage;
import net.ds3783.chatserver.messages.LoginMessage;
import net.ds3783.chatserver.messages.PrivateMessage;
import net.ds3783.chatserver.messages.PublicMessage;
import net.ds3783.chatserver.messages.SystemReplyMessage;
import net.ds3783.chatserver.messages.model.ChannelModel;
import net.ds3783.chatserver.messages.model.ClientModel;

public class ChatServerClient extends EventDispatcher {

    public static const LOGINED:String = "LOGINED";
    public static const CONNECTED:String = "CONNECTED";
    public static const CONNECTION_LOST:String = "CONNECTION_LOST";
    public static const CONNECTED_ERROR:String = "CONNECTED_ERROR";
    public static const LOGIN_ERROR:String = "LOGIN_ERROR";
    public static const CHANNEL_LIST_UPDATE:String = "CHANNEL_LIST_UPDATE";
    public static const CLIENT_LIST_UPDATE:String = "CLIENT_LIST_UPDATE";
    public static const CHANNEL_JOINED:String = "CHANNEL_JOINED";
    public static const CLIENT_LOST:String = "CLIENT_LOST";
    public static const CHANNEL_LOST:String = "CHANNEL_LOST";
    public static const CLIENT_JOIN:String = "CLIENT_JOIN";
    public static const MESSAGE:String = "MESSAGE";
    public static const ERROR:String = "ERROR";
    public static const DISCONNECT:String = "DISCONNECT";

    public function ChatServerClient(autoJoin:Boolean = true) {


        registerClassAlias("net.ds3783.chatserver.Message", Message);
        registerClassAlias("net.ds3783.chatserver.messages.LoginMessage", LoginMessage);
        registerClassAlias("net.ds3783.chatserver.messages.SystemReplyMessage", SystemReplyMessage);
        registerClassAlias("net.ds3783.chatserver.messages.CommandMessage", CommandMessage);
        registerClassAlias("net.ds3783.chatserver.messages.ChannelListMessage", ChannelListMessage);
        registerClassAlias("net.ds3783.chatserver.messages.PublicMessage", PublicMessage);
        registerClassAlias("net.ds3783.chatserver.messages.PrivateMessage", PrivateMessage);
        registerClassAlias("net.ds3783.chatserver.messages.ClientListMessage", ClientListMessage);
        registerClassAlias("net.ds3783.chatserver.messages.model.ChannelModel", ChannelModel);
        registerClassAlias("net.ds3783.chatserver.messages.model.ClientModel", ClientModel);
        registerClassAlias("net.ds3783.chatserver.messages.ClientLostMessage", ClientLostMessage);
        registerClassAlias("net.ds3783.chatserver.messages.ChannelLostMessage", ChannelLostMessage);
        registerClassAlias("net.ds3783.chatserver.messages.ClientJoinChannelMessage", ClientJoinChannelMessage);

        connType = CONN_TYPE_SOCKET;
        socket = new SocketClient();
        socket.addEventListener(SocketClient.EVENT_CONNECTED, onConnected);
        socket.addEventListener(SocketClient.EVENT_AUTHERROR, onConnectFail);
        socket.addEventListener(SocketClient.EVENT_LOGIN, onLogined);
        socket.addEventListener(SocketClient.EVENT_LOGIN_FAIL, onLoginFail);
        socket.addEventListener(SocketClient.EVENT_CONNECTION_LOST, onConnectionLost);
        socket.addEventListener(SocketClient.EVENT_CLIENTMESSAGE, onChatMessage);
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
        socket.addEventListener(SocketClient.EVENT_CLIENT_LOST, onClientLost);
        socket.addEventListener(SocketClient.EVENT_CHANNEL_LOST, onChannelLost);
        socket.addEventListener(SocketClient.EVENT_OTHER_CLIENT_JOIN, onOtherClientJoin);
        socket.addEventListener(SocketClient.EVENT_USERERROR, onError);
        socket.addEventListener(SocketClient.EVENT_DISCONNECT, onDisconnect);
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

    private function onConnectFail(e:SocketEvent):void {
        connected = true;
        dispatchEvent(new SocketEvent(CONNECTED_ERROR));
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

    private function onLoginFail(e:SocketEvent):void {
        logined = false;
        var evt:ChatEvent = new ChatEvent(LOGIN_ERROR);
        evt.message = e.message;
        dispatchEvent(evt);
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
        //尚未测试
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


    public function sendPrivateMessage(id:String, recvName:String, text:String):PrivateMessage {
        if (!text || !id) {
            return null;
        }
        var message:PrivateMessage = new PrivateMessage();
        message.reveiverId = id;
        message.content = text;
        message.reveiverName = recvName;
        socket.sendMessage(message);
        return message;
    }

    public function sendMessage(id:String, text:String):PublicMessage {
        if (!text || !id) {
            return null;
        }
        var message:PublicMessage = new PublicMessage();
        message.channelId = Number(id);
        message.content = text;
        socket.sendMessage(message);
        return message;
    }



    public function disconnect():void {
        var msg:CommandMessage =new CommandMessage();
        msg.command=CommandType.DISCONNECT;
        socket.sendMessage(msg);
    }

    private function onChannelListUpdate(e:SocketEvent):void {
        var list:ChannelListMessage = e.message as ChannelListMessage;
        if (list) {
            if (list.listeningChannels) {
                //与缓存内容找出差异
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
                //如果存在差异
                if (misMach) {
                    this.listeningChannels = list.listeningChannels;
                    //发送update client list指令
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

    private function onClientLost(e:SocketEvent):void {
        var msg:ClientLostMessage = e.message as ClientLostMessage;
        var event:ChatEvent = new ChatEvent(CLIENT_LOST);
        event.message = msg;
        dispatchEvent(event);
    }

    private function onDisconnect(e:SocketEvent):void {
        var msg:SystemReplyMessage = e.message as SystemReplyMessage;
        var event:ChatEvent = new ChatEvent(DISCONNECT);
        event.message = msg;
        dispatchEvent(event);
    }

    private function onChannelLost(e:SocketEvent):void {
        var msg:ClientLostMessage = e.message as ClientLostMessage;
        var event:ChatEvent = new ChatEvent(CHANNEL_LOST);
        event.message = msg;
        dispatchEvent(event);
    }

    private function onOtherClientJoin(e:SocketEvent):void {
        var msg:ClientJoinChannelMessage = e.message as ClientJoinChannelMessage;
        var event:ChatEvent = new ChatEvent(CLIENT_JOIN);
        event.message = msg;
        dispatchEvent(event);
    }

    private function onChatMessage(e:SocketEvent):void {
        var msg:Message = e.message as Message;
        var event:ChatEvent = new ChatEvent(MESSAGE);
        event.message = msg;
        dispatchEvent(event);
    }

    private function onError(e:SocketEvent):void {
        var msg:SystemReplyMessage = e.message as SystemReplyMessage;
        var event:ChatEvent = new ChatEvent(ERROR);
        event.message = msg;
        dispatchEvent(event);
    }

    private function onConnectionLost(e:SocketEvent):void {
        dispatchEvent(new ChatEvent(CONNECTION_LOST));
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
