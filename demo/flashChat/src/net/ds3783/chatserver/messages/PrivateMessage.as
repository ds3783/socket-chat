/**
 * Created with IntelliJ IDEA.
 * User: Ds.3783
 * Date: 13-1-1
 * Time: ÏÂÎç9:57
 * To change this template use File | Settings | File Templates.
 */
package net.ds3783.chatserver.messages {
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.MessageType;

public class PrivateMessage implements Message {
    public function PrivateMessage() {
    }

    private var _content:String;
    public var reveiverId:String;
    public var reveiverName:String;
    public var senderId:String;
    public var senderName:String;
    public var timestamp:Date;

    public function getType():String {
        return MessageType.CHAT_MESSAGE;
    }

    public function isSerializable():Boolean {
        return true;
    }

    public function get content():String {

        return _content;
    }

    public function set content(value:String):void {
        this._content = value;
    }
}
}
