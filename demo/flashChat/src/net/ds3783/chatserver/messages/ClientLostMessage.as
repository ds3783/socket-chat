/**
 * Created with IntelliJ IDEA.
 * User: Ds.3783
 * Date: 13-1-8
 * Time: 下午11:10
 * To change this template use File | Settings | File Templates.
 */
package net.ds3783.chatserver.messages {
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.MessageType;
import net.ds3783.chatserver.messages.model.ClientModel;

public class ClientLostMessage extends SystemReplyMessage implements Message {
    public var client:ClientModel;

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