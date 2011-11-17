package net.ds3783.chatserver.messages;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-11-17
 * Time: 下午10:16
 * 消息接口，所有通信的消息必须实现此接口
 */
public interface Message {
    /**
     * @return 消息类型，用于标识该消息的具体类型
     */
    public String getType();

    /**
     * @return 是否可序列化
     */
    public boolean isSerializable();

    /**
     * @return 消息的内容，某些协议对不可序列化的消息会使用其内容作为传输内容
     */
    public String getContent();


}
