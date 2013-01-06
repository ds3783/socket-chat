package net.ds3783.chatserver.communicate.core;

import net.ds3783.chatserver.communicate.ContextHelper;
import net.ds3783.chatserver.dao.Client;
import net.ds3783.chatserver.dao.ClientDao;
import net.ds3783.chatserver.messages.Message;
import net.ds3783.chatserver.messages.MessageContext;

import java.util.HashSet;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ds.3783
 * Date: 2010-5-4
 * Time: 20:28:49
 * 用于处理输出消息的分发
 * 根据消息的目标数量和输出线程数量做判断
 * 如果点对点发消息则直接搜索输出线程
 * 如果大量群发则将消息群发给所有输出线程
 */
public class OutputerSwitcher {
    private ThreadResource threadResource;
    private ClientDao clientDao;
    private ContextHelper contextHelper;

    /**
     * 发送消息
     *
     * @param message 发送消息
     */
    public void switchTo(Message message) {

        MessageContext context = contextHelper.getContext(message);
        if (message == null || context.getReceivers() == null || context.getReceivers().size() == 0) return;
        int messgeDestCount = context.getReceivers().size();
        List<CommonRunnable> outputer = threadResource.getThreads(ThreadResourceType.OUTPUT_THREAD);
        int outputerCount = outputer.size();
        //tricks: 因为每个OutputThread都只对应一个用户所以可以这么玩
        if (messgeDestCount > outputerCount) {
            for (CommonRunnable runnable : outputer) {
                OutputThread ot = (OutputThread) runnable;
                ot.send(message);
            }
        } else {
            //此处排重必须做
            HashSet<String> ots = new HashSet<String>();
            for (Client dest : context.getReceivers()) {
                if (ots.contains(dest.getWriteThread())) {
                    continue;
                }
                ots.add(dest.getWriteThread());
                OutputThread ot = (OutputThread) threadResource.getThread(dest.getWriteThread());
                if (ot != null) {
                    ot.send(message);
                }
            }
        }
    }

    public void setThreadResource(ThreadResource threadResource) {
        this.threadResource = threadResource;
    }

    public void setClientDao(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    public void setContextHelper(ContextHelper contextHelper) {
        this.contextHelper = contextHelper;
    }
}
