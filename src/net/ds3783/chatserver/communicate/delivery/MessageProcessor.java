package net.ds3783.chatserver.communicate.delivery;


/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-6-19
 * Time: 上午12:04
 * 消息处理
 */
public interface MessageProcessor {
    public void process(Event evt, long now);
}
