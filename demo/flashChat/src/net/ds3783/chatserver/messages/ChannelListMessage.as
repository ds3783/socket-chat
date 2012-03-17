/**
 * Created by IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 12-3-16
 * Time: ÏÂÎç3:45
 * To change this template use File | Settings | File Templates.
 */
package net.ds3783.chatserver.messages {
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.MessageType;

public class ChannelListMessage extends SystemReplyMessage implements Message {
    public function ChannelListMessage() {
    }

    public var channels:Array;

    override public function getType():String {
        return MessageType.COMMAND_MESSAGE;
    }

    override public function isSerializable():Boolean {
        return true;
    }

    override public function get content():String {
        return "";
    }

    override public function set content(value:String):void {
    }
}
}
