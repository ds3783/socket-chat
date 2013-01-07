package net.ds3783.chatserver.communicate.delivery;


import com.google.gson.Gson;
import net.ds3783.chatserver.communicate.ContextHelper;
import net.ds3783.chatserver.communicate.core.OutputerSwitcher;
import net.ds3783.chatserver.extension.ClientException;
import net.ds3783.chatserver.extension.ExtensionException;
import net.ds3783.chatserver.messages.Message;
import net.ds3783.chatserver.messages.MessageContext;
import net.ds3783.chatserver.messages.SystemReplyMessage;
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
    protected OutputerSwitcher outputerSwitcher;


    public void processMsg(Message msg, long now) {
        MessageEvent evt = new MessageEvent();
        evt.setName(msg.getType());
        evt.setMessage(msg);
        try {
            messageDispatcher.dispatchEvent(evt);
        } catch (ExtensionException e) {
            logger.warn("Extension error occored during dispatchEvent:" + gson.toJson(evt) + "\r\n original Message is :" + gson.toJson(msg) + "\r\n errMsg:" + e.getMessage(), e);
        } catch (ClientException e) {
            SystemReplyMessage reply = new SystemReplyMessage();
            reply.setCode(SystemReplyMessage.CODE_ERROR_USER_CUSTOM);
            reply.setContent(e.getMessage());
            MessageContext senderContext = contextHelper.getContext(msg);
            MessageContext context = contextHelper.registerMessage(reply, senderContext.getSender());
            context.getReceivers().add(senderContext.getSender());
            outputerSwitcher.switchTo(reply);
            logger.debug("Client error occored during dispatchEvent:" + gson.toJson(evt) + "\r\n original Message is :" + gson.toJson(msg) + "\r\n errMsg:" + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("System error occored during dispatchEvent:" + gson.toJson(evt) + "\r\n original Message is :" + gson.toJson(msg) + "\r\n errMsg:" + e.getMessage(), e);
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

    public void setOutputerSwitcher(OutputerSwitcher outputerSwitcher) {
        this.outputerSwitcher = outputerSwitcher;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }
}
