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
 * ���ڴ��������Ϣ�ķַ�
 * ������Ϣ��Ŀ������������߳��������ж�
 * �����Ե㷢��Ϣ��ֱ����������߳�
 * �������Ⱥ������ϢȺ������������߳�
 */
public class OutputerSwitcher {
    private ThreadResource threadResource;
    private ClientDao clientDao;

    /**
     * ������Ϣ
     *
     * @param message ������Ϣ
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
