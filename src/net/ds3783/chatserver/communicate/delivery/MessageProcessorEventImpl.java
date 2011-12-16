package net.ds3783.chatserver.communicate.delivery;


import net.ds3783.chatserver.communicate.ContextHelper;
import net.ds3783.chatserver.messages.Message;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-6-19
 * Time: 上午12:11
 * 通过Event模式实现消息的处理
 */
public class MessageProcessorEventImpl implements MessageProcessor {
    private MessageDispatcher messageDispatcher;
    private ContextHelper contextHelper;


    public void processMsg(Message msg, long now) {
        Event evt = new Event();
        evt.setName(msg.getType());
        evt.setMessage(msg);
        messageDispatcher.dispatchEvent(evt);
        contextHelper.forget(msg);

    }

    public void setMessageDispatcher(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }

    public void setContextHelper(ContextHelper contextHelper) {
        this.contextHelper = contextHelper;
    }
}
