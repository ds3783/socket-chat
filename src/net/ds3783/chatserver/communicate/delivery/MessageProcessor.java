package net.ds3783.chatserver.communicate.delivery;


import net.ds3783.chatserver.messages.Message;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-6-19
 * Time: 上午12:04
 * 消息处理
 */
public interface MessageProcessor {
    public void processMsg(Message msg, long now);
}
