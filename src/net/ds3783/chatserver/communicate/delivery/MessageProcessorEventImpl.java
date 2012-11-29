package net.ds3783.chatserver.communicate.delivery;


import com.google.gson.Gson;
import net.ds3783.chatserver.communicate.ContextHelper;
import net.ds3783.chatserver.messages.Message;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-6-19
 * Time: 上午12:11
 * 通过Event模式实现消息的处理
 */
public class MessageProcessorEventImpl implements MessageProcessor {
    private static Log logger = LogFactory.getLog(MessageProcessorEventImpl.class);
    private Gson gson;
    private MessageDispatcher messageDispatcher;
    private ContextHelper contextHelper;


    public void processMsg(Message msg, long now) {
        Event evt = new Event();
        evt.setName(msg.getType());
        evt.setMessage(msg);
        try {
            messageDispatcher.dispatchEvent(evt);
        } catch (Exception e) {
            logger.error("error occored during dispatchEvent:" + gson.toJson(evt) + "\r\n original Message is :" + gson.toJson(msg) + "\r\n errMsg:" + e.getMessage(), e);
        } finally {
            contextHelper.forget(msg);
        }

    }

    public void setMessageDispatcher(MessageDispatcher messageDispatcher) {
        this.messageDispatcher = messageDispatcher;
    }

    public void setContextHelper(ContextHelper contextHelper) {
        this.contextHelper = contextHelper;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
