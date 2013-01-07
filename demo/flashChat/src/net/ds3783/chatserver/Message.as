/**
 * Created by IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 12-2-2
 * Time: 上午11:32
 *
 */
package net.ds3783.chatserver {
/**
 *  消息接口，所有通信的消息必须实现此接口
 */
public interface Message {
    /**
     * @return 消息类型，用于标识该消息的具体类型
     */
    function getType():String;

    /**
     * @return 是否可序列化
     */
    function isSerializable():Boolean;

    /**
     * @return 消息的内容，某些协议对不可序列化的消息会使用其内容作为传输内容
     */
    function get content():String;

    function set content(value:String):void;
}
}
