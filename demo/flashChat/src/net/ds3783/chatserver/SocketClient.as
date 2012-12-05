package net.ds3783.chatserver {
import com.adobe.serialization.json.JSON;

import flash.events.Event;
import flash.events.EventDispatcher;
import flash.events.IOErrorEvent;
import flash.events.ProgressEvent;
import flash.events.SecurityErrorEvent;
import flash.net.ObjectEncoding;
import flash.net.Socket;
import flash.net.registerClassAlias;
import flash.utils.ByteArray;

import net.ds3783.chatserver.messages.ChannelListMessage;
import net.ds3783.chatserver.messages.CommandMessage;
import net.ds3783.chatserver.messages.LoginMessage;
import net.ds3783.chatserver.messages.SystemReplyMessage;
import net.ds3783.chatserver.messages.model.ChannelModel;

public class SocketClient extends EventDispatcher {
    public function SocketClient() {
        registerClassAlias("net.ds3783.chatserver.MessageOld", MessageOld);

        registerClassAlias("net.ds3783.chatserver.Message", Message);
        registerClassAlias("net.ds3783.chatserver.messages.LoginMessage", LoginMessage);
        registerClassAlias("net.ds3783.chatserver.messages.SystemReplyMessage", SystemReplyMessage);
        registerClassAlias("net.ds3783.chatserver.messages.CommandMessage", CommandMessage);
        registerClassAlias("net.ds3783.chatserver.messages.ChannelListMessage", ChannelListMessage);
        registerClassAlias("net.ds3783.chatserver.messages.model.ChannelModel", ChannelModel);
    }

    private var socket:Socket = null;
    private var host:String;
    private var port:int;
    private var logined:Boolean = false;
    private var connected:Boolean = false;
    private var dataCache:ByteArray = new ByteArray();
    private var messageBuffer:Array = new Array();

    public static const EVENT_CONNECTED:String = "ON_CONNECTED";
    public static const EVENT_DISCONNECTED:String = "ON_DISCONNECTED";
    public static const EVENT_LOGIN:String = "ON_LOGIN";
    public static const EVENT_LOGIN_FAIL:String = "ON_LOGIN_FAIL";
    public static const EVENT_CLIENTMESSAGE:String = "ON_CLIENT";
    public static const EVENT_SERVERMESSAGE:String = "ON_SERVER";
    public static const EVENT_NETERROR:String = "ON_NETERROR";
    public static const EVENT_AUTHERROR:String = "ON_AUTHERROR";
    public static const EVENT_USERERROR:String = "ON_USERERROR";
    public static const EVENT_CHANNEL_LIST_UPDATE:String = "ON_CHANNEL_LIST_UPDATE";

    public function connect(host:String, port:int):void {
        socket = new Socket();
        socket.addEventListener(Event.CONNECT, onStocketConnected);
        socket.addEventListener(ProgressEvent.SOCKET_DATA, onSocketData);
        socket.addEventListener(IOErrorEvent.IO_ERROR, onSocketIOError);
        socket.addEventListener(IOErrorEvent.NETWORK_ERROR, onSocketIOError);
        socket.addEventListener(SecurityErrorEvent.SECURITY_ERROR, onSecurityError);
        socket.objectEncoding = ObjectEncoding.AMF3;

        this.host = host;
        this.port = port;
        socket.connect(host, port);

    }

    public function close():void {
        this.connected = false;
        this.logined = false;
        if (socket.connected) {
            socket.close();
        }

        socket.removeEventListener(Event.CONNECT, onStocketConnected);
        socket.removeEventListener(ProgressEvent.SOCKET_DATA, onSocketData);
        socket.removeEventListener(IOErrorEvent.IO_ERROR, onSocketIOError);
        socket.removeEventListener(IOErrorEvent.NETWORK_ERROR, onSocketIOError);
        socket.removeEventListener(SecurityErrorEvent.SECURITY_ERROR, onSecurityError);
        socket = null;
    }


    public function isConnected():Boolean {
        return socket.connected;
    }


    public function isLogin():Boolean {
        return this.logined;
    }

    private function send(data:Message):void {
        if (!socket.connected) {
            if (this.connected) {
                this.connected = false;
                dispatchEvent(new SocketEvent(EVENT_DISCONNECTED));
            } else {
                throw new ChatServerError("Not Connected!");
            }
        }
        var binaryData:ByteArray = new ByteArray();
        var serialized:ByteArray = new ByteArray();
        serialized.writeObject(data);
        var length:int = serialized.length;
        binaryData.writeInt(length);
        binaryData.writeBytes(serialized);
        binaryData.position = 0;

        trace("send message:" + JSON.encode(data));
        socket.writeBytes(binaryData);
        socket.flush();
    }

    private function onStocketConnected(evt:Event):void {
        trace("Socket Connected to " + host + ":" + port);
        this.connected = true;
        var evt2:SocketEvent = new SocketEvent(EVENT_CONNECTED);
        this.dispatchEvent(evt2);
    }

    private function onSocketData(evt:Event):void {
        //TODO::
        try {
            while (socket.bytesAvailable > 0) {
                socket.readBytes(dataCache, 0, socket.bytesAvailable);
            }
            while (dataCache.length > 0) {
                var bytes:ByteArray = new ByteArray();
                var newbytes:ByteArray = new ByteArray();
                var pos:uint = dataCache.position;
                dataCache.position = 0;
                var bint:int = dataCache.readInt();
                trace("message length:" + bint);
                if (dataCache.bytesAvailable >= bint) {
                    dataCache.readBytes(newbytes, 0, bint);
                    trace("message content:" + newbytes.toString());
                    trace("message len2:" + newbytes.length);
                    newbytes.position = 0;
                    messageBuffer.push(newbytes.readObject());
                    if (dataCache.bytesAvailable > 0) {
                        dataCache.readBytes(bytes);
                    }
                    dataCache = new ByteArray();
                    bytes.position = 0;
                    if (bytes.bytesAvailable > 0) {
                        bytes.readBytes(dataCache, 0, bytes.bytesAvailable);
                    }

                } else {
                    dataCache.position = pos;
                    break;
                }
            }
            this.onData();
        } catch (err:Error) {
            trace(err.getStackTrace())
        }
    }


    private function onData():void {
        while (messageBuffer.length > 0) {
            var message:Message = messageBuffer.shift();
            switch (message.getType()) {
                case MessageType.AUTH_MESSAGE:
                    break;
                case MessageType.LOGIN_MESSAGE:
                    break;
                case MessageType.COMMAND_MESSAGE:
                    var sysMsg:SystemReplyMessage = message as SystemReplyMessage;
                    onCommand(sysMsg);
                    break;
                case MessageType.CHAT_MESSAGE:
                    var evt2:SocketEvent = new SocketEvent(EVENT_CLIENTMESSAGE);
                    evt2.message = message;
                    this.dispatchEvent(evt2);
                    break;
            }
        }
    }


    public function sendMessage(msg:Message):void {
        if (msg == null) {
            return;
        }
        if ((!logined && (msg as LoginMessage) == null)) {
            throw new ChatServerError("Not Logined!");
        }
        send(msg);
    }

    private function onSocketIOError(evt:IOErrorEvent):void {
        trace('Stocket Error::' + evt.text);
        var evt1:SocketEvent = new SocketEvent(EVENT_NETERROR);
        this.dispatchEvent(evt1);
    }

    private function onSecurityError(evt:SecurityErrorEvent):void {
        trace('Security Error::' + evt.text);
        var evt1:SocketEvent = new SocketEvent(EVENT_AUTHERROR);
        this.dispatchEvent(evt1);
    }

    private function onCommand(sysMsg:SystemReplyMessage):void {
        var event:SocketEvent;
        switch (sysMsg.code) {
            case SystemReplyMessage.CODE_LOGIN_SUCCESS:
                logined = true;
                dispatchEvent(new SocketEvent(EVENT_LOGIN));
                break;
            case SystemReplyMessage.CODE_ERROR_WRONG_PASSWORD:
                dispatchEvent(new SocketEvent(EVENT_LOGIN_FAIL));
                break;
            case SystemReplyMessage.CODE_ERROR_USER_CUSTOM:
                event = new SocketEvent(EVENT_USERERROR);
                event.message = sysMsg;
                dispatchEvent(event);
                break;
            case SystemReplyMessage.CODE_CHANNEL_LIST:
                event = new SocketEvent(EVENT_CHANNEL_LIST_UPDATE);
                event.message = sysMsg;
                dispatchEvent(event);
                break;
        }
        trace(JSON.encode(sysMsg));
    }

}
}