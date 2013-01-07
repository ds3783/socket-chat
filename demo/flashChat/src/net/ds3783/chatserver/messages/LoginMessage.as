/**
 * Created by IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 12-2-2
 * Time: 上午11:36
 * To change this template use File | Settings | File Templates.
 */
package net.ds3783.chatserver.messages {
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.MessageType;

public class LoginMessage implements Message {
    public function LoginMessage() {
    }

    public var username:String;
    public var password:String;

    public function getType():String {
        return MessageType.COMMAND_MESSAGE;
    }

    public function isSerializable():Boolean {
        return true;
    }

    public function get content():String {
        return username;
    }

    public function set content(value:String):void {
    }

}
}
