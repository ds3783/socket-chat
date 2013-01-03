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

public class PublicMessage implements Message {
    public function PublicMessage() {
    }

    private var _content:String;
    public var senderId:String;
    public var senderName:String;
    public var channelId:Number;
    public var channelName:String;
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
