/**
 * Created by IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 12-3-16
 * Time: ÏÂÎç3:42
 * To change this template use File | Settings | File Templates.
 */
package net.ds3783.chatserver.messages {
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.MessageType;

public class CommandMessage implements Message {
    public function CommandMessage() {
    }

    public var command:String;
    private var _content:String;

    public function getType():String {
        return MessageType.COMMAND_MESSAGE;
    }

    public function isSerializable():Boolean {
        return true;
    }

    public function get content():String {
        return _content;
    }

    public function set content(value:String):void {
        _content = value;

    }
}
}
