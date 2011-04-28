package net.ds3783.chatserver {
import flash.events.Event;

public class ChatEvent extends Event {
    private var _message:Message;


    public function ChatEvent(type:String, bubbles:Boolean = false, cancelable:Boolean = false) {
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