/**
 * Created by IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 12-3-30
 * Time: обнГ3:45
 * To change this template use File | Settings | File Templates.
 */
package net.ds3783.chatserver {
import flash.events.Event;

public class ChatEvent extends Event {

    public function ChatEvent(type:String, bubbles:Boolean = false, cancelable:Boolean = false) {
        super(type, bubbles, cancelable);
    }

    private var _message:Message;


    public function get message():Message {
        return _message;
    }

    public function set message(value:Message):void {
        _message = value;
    }
}
}
