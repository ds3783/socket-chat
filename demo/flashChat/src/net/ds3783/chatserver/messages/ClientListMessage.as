/**
 * Created with IntelliJ IDEA.
 * User: Ds.3783
 * Date: 12-12-29
 * Time: ÏÂÎç10:01
 * To change this template use File | Settings | File Templates.
 */
package net.ds3783.chatserver.messages {
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.MessageType;

public class ClientListMessage extends SystemReplyMessage implements Message {
    public var clients:Array;

    public function ClientListMessage() {
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
