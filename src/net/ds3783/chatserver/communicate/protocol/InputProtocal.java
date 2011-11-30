package net.ds3783.chatserver.communicate.protocol;

import net.ds3783.chatserver.messages.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2010-2-24
 * Time: 22:30:56
 */
public abstract class InputProtocal {
    protected byte[] data;
    protected byte[] remains;
    protected List<Message> messages;

    protected InputProtocal() {
        messages = new ArrayList<Message>();
    }

    /**
     * 输入流的数据
     *
     * @param data 网络上读取的二进制数组
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * 取得解码后消息的数量，必须在unmarshal调用后执行
     *
     * @return 解码后的消息数量
     */
    public int messageCount() {
        if (messages != null) {
            return messages.size();
        } else {
            return 0;
        }
    }

    /**
     * 取得解码后的消息
     *
     * @return 解码后的消息
     */
    public List<Message> getMessages() {
        if (messages != null) {
            return new ArrayList<Message>(messages);
        } else {
            return new ArrayList<Message>();
        }
    }

    /**
     * 取得解码后剩余未处理的字节
     *
     * @return
     */
    public byte[] getRemains() {
        if (remains != null) {
            return remains;
        } else {
            return new byte[0];
        }
    }

    public void reset() {
        data = new byte[0];
        remains = new byte[0];
        messages.clear();
    }

    /**
     * 解码
     */
    public abstract void unmarshal() throws UnmarshalException;
}
