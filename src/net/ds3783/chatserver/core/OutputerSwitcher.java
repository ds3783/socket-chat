package net.ds3783.chatserver.core;

import net.ds3783.chatserver.Client;
import net.ds3783.chatserver.Message;
import net.ds3783.chatserver.dao.ClientDao;

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

    /**
     * 发送消息
     *
     * @param message 发送消息
     */
    public void switchTo(Message message) {
        if (message == null || message.getDestUserUids() == null) return;
        int messgeDestCount = message.getDestUserUids().size();
        List<CommonRunnable> outputer = threadResource.getThreads(ThreadResourceType.OUTPUT_THREAD);
        int outputerCount = outputer.size();
        if (messgeDestCount > outputerCount) {
            for (CommonRunnable runnable : outputer) {
                OutputThread ot = (OutputThread) runnable;
                ot.send(message);
            }
        } else {
            for (String uid : message.getDestUserUids().keySet()) {
                Client c = clientDao.getClient(uid);
                OutputThread ot = (OutputThread) threadResource.getThread(c.getWriteThread());
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
}
