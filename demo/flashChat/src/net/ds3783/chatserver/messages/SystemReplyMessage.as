/**
 * Created by IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 12-2-2
 * Time: 上午11:45
 * To change this template use File | Settings | File Templates.
 */
package net.ds3783.chatserver.messages {
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.MessageType;

public class SystemReplyMessage implements Message {
    public static const  CODE_100:int = 100;

    public static const  CODE_200:int = 200;
    public static const  CODE_LOGIN_SUCCESS:int = 201;//登录成功

    public static const  CODE_USER_ONLINE:int = 301;//其他用户登录

    public static const  CODE_ERROR_NOT_LOGIN:int = 401;//尚未登录
    public static const  CODE_ERROR_WRONG_PASSWORD:int = 402;//登录用户名或密码错误
    public static const  CODE_ERROR_BLACKLIST:int = 403;//用户被禁言

    public function SystemReplyMessage() {
    }


    public var code:int;
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
