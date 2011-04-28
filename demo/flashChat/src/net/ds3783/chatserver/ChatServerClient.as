package net.ds3783.chatserver {
import flash.events.Event;
import flash.events.EventDispatcher;
import flash.events.IOErrorEvent;
import flash.events.ProgressEvent;
import flash.events.SecurityErrorEvent;
import flash.net.ObjectEncoding;
import flash.net.Socket;
import flash.utils.ByteArray;

public class ChatServerClient extends EventDispatcher {
    public function ChatServerClient() {
    }

    private var socket:Socket = null;
    private var logined:Boolean = false;
    private var dataCache:ByteArray = new ByteArray();
    private var messageBuffer:Array = new Array();

    public static const EVENT_CONNECTED:String = "ON_CONNECTED";
    public static const EVENT_LOGIN:String = "ON_LOGIN";
    public static const EVENT_CLIENTMESSAGE:String = "ON_CLIENT";
    public static const EVENT_SERVERMESSAGE:String = "ON_SERVER";
    public static const EVENT_NETERROR:String = "ON_NETERROR";
    public static const EVENT_AUTHERROR:String = "ON_AUTHERROR";

    public function connect(host:String, port:int):void {
        socket = new Socket();
        socket.addEventListener(Event.CONNECT, onStocketConnected);
        socket.addEventListener(ProgressEvent.SOCKET_DATA, onSocketData);
        socket.addEventListener(IOErrorEvent.IO_ERROR, onSocketIOError);
        socket.addEventListener(IOErrorEvent.NETWORK_ERROR, onSocketIOError);
        socket.addEventListener(SecurityErrorEvent.SECURITY_ERROR, onSecurityError);
        socket.objectEncoding = ObjectEncoding.AMF3;


        socket.connect(host, port);

    }


    public function isConnected():Boolean {
        return socket.connected;
    }


    public function isLogin():Boolean {
        return this.logined;
    }

    public function login(username:String, password:String):void {
        var message:Message = new Message();
        message.type = MessageType.LOGIN;
        message.userUuid = username;
        message.content = password;
        this.send(message);
    }

    private function send(data:Message):void {
        if (!socket.connected) {
            throw new ChatServerError("Not Connected!");
        }
        var binaryData:ByteArray = new ByteArray();
        var serialized:ByteArray = new ByteArray();
        serialized.writeObject(data);
        var length:int = serialized.length;
        binaryData.writeInt(length);
        binaryData.writeBytes(serialized);
        socket.writeBytes(binaryData);
    }

    private function onStocketConnected(evt:Event):void {
        var evt2:ChatEvent = new ChatEvent(EVENT_CONNECTED);
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
                if (dataCache.bytesAvailable >= bint) {
                    dataCache.readBytes(newbytes, 0, bint);
                    newbytes.uncompress();
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
            switch (message.type) {
                case MessageType.AUTH:
                    break;
                case MessageType.LOGIN:
                    var evt1:ChatEvent = new ChatEvent(EVENT_LOGIN);
                    evt1.message = message;
                    this.dispatchEvent(evt1);
                    break;
                case MessageType.CLIENT:
                    var evt2:ChatEvent = new ChatEvent(EVENT_CLIENTMESSAGE);
                    evt2.message = message;
                    this.dispatchEvent(evt2);
                    break;
            }
        }
    }

    public function sendMessage(msg:Message):void {
        if (!logined) {
            throw new ChatServerError("Not Logined!");
        }
        msg.type = MessageType.CLIENT;
        send(msg);
    }

    private function onSocketIOError(evt:IOErrorEvent):void {
        trace('Stocket Error::' + evt.text);
        var evt1:ChatEvent = new ChatEvent(EVENT_NETERROR);
        this.dispatchEvent(evt1);
    }

    private function onSecurityError(evt:SecurityErrorEvent):void {
        trace('Security Error::' + evt.text);
        var evt1:ChatEvent = new ChatEvent(EVENT_AUTHERROR);
        this.dispatchEvent(evt1);
    }

}
}