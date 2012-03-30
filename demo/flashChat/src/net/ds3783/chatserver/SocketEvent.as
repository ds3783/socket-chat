package net.ds3783.chatserver {
import flash.events.Event;

public class SocketEvent extends Event {
    private var _message:Message;


    public function SocketEvent(type:String, bubbles:Boolean = false, cancelable:Boolean = false) {
        super(type, bubbles, cancelable);
    }


    public function get message():Message {
        return _message;
    }

    public function set message(value:Message):void {
        _message = value;
    }
}
}