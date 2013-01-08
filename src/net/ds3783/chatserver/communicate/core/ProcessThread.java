package net.ds3783.chatserver.communicate.core;

import com.google.gson.Gson;
import net.ds3783.chatserver.EventConstant;
import net.ds3783.chatserver.communicate.delivery.Event;
import net.ds3783.chatserver.communicate.delivery.MessageEvent;
import net.ds3783.chatserver.communicate.delivery.MessageProcessor;
import net.ds3783.chatserver.messages.Message;
import net.ds3783.chatserver.tools.Utils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2009-9-16
 * Time: 15:40:48
 */
public class ProcessThread extends CommonRunnable implements Runnable, Switchable<Event> {
    private Log logger = LogFactory.getLog(ProcessThread.class);
    private LinkedBlockingQueue<Event> receivedMessages = new LinkedBlockingQueue<Event>();
    private LinkedBlockingQueue<Event> enmergencyMessages = new LinkedBlockingQueue<Event>();

    private MessageProcessor messageProcessor;
    private long maxMessageInQueue = 100;
    private int maxMessagePerTime = 100;
    private Gson gson;

    public void addMessage(Event event) {
        try {
            if (event instanceof MessageEvent) {
                Message message = ((MessageEvent) event).getMessage();
                logger.debug("收到消息:" + gson.toJson(message));
                if ((EventConstant.AUTH_MESSAGE.equals(message.getType()) || EventConstant.LOGIN_MESSAGE.equals(message.getType()))) {
                    enmergencyMessages.put(event);
                    return;
                }
            }
            if (receivedMessages.size() > maxMessageInQueue) {
                //超出最大消息数量限制
                logger.fatal("系统负载大,待处理消息数量:" + receivedMessages.size());
                logger.warn("Dropped Message:" + Utils.describeBean(event));
            } else {
                receivedMessages.put(event);
            }

        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    public void doRun() throws Exception {
        while (true) {
            boolean nothingtodo = true;

            if (!enmergencyMessages.isEmpty()) nothingtodo = false;
            long now = System.currentTimeMillis();
            while (!enmergencyMessages.isEmpty()) {
                Event evt = enmergencyMessages.poll();
                deliverEvent(evt, now);
            }


            if (!receivedMessages.isEmpty()) nothingtodo = false;
            now = System.currentTimeMillis();
            int counter = 0;
            while (!receivedMessages.isEmpty()) {
                Event evt = receivedMessages.poll();
                deliverEvent(evt, now);
                counter++;
                if (counter > maxMessagePerTime) {
                    break;
                }
            }
            if (nothingtodo) {
                try {
                    Thread.sleep(sleeptime);
                } catch (InterruptedException e) {
                    logger.error(e.getMessage(), e);
                    break;
                }
            }

        }

    }

    public void deliverEvent(Event evt, long now) {
        if (evt == null) return;
        messageProcessor.process(evt, now);
    }

    public void destroy() throws Exception {

    }

    public void cleanuUp() throws Exception {

    }

    public void setMessageProcessor(MessageProcessor messageProcessor) {
        this.messageProcessor = messageProcessor;
    }


    public void setMaxMessageInQueue(long maxMessageInQueue) {
        this.maxMessageInQueue = maxMessageInQueue;
    }

    public void setMaxMessagePerTime(int maxMessagePerTime) {
        this.maxMessagePerTime = maxMessagePerTime;
    }

    public void setGson(Gson gson) {
        this.gson = gson;
    }

    /**
     * 取得权重
     * 权重关系到选择器的选择结果
     *
     * @return 权重
     */
    public int getWeight() {
        return this.receivedMessages.size();
    }

    /**
     * 接收数据
     *
     * @param data 传给控制器的消息
     */
    public void receive(Event data) {
        this.addMessage(data);
    }
}
