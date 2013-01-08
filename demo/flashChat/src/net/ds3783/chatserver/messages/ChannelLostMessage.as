/**
 * Created with IntelliJ IDEA.
 * User: Ds.3783
 * Date: 13-1-8
 * Time: 下午11:11
 * To change this template use File | Settings | File Templates.
 */
package net.ds3783.chatserver.messages {
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.MessageType;

public class ChannelLostMessage extends SystemReplyMessage implements Message {
    public var channels:Array;

    public function ClientLostMessage() {
    }


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