package net.ds3783.chatserver.communicate.core;

import net.ds3783.chatserver.communicate.ContextHelper;
import net.ds3783.chatserver.dao.Client;
import net.ds3783.chatserver.dao.ClientDao;
import net.ds3783.chatserver.messages.Message;
import net.ds3783.chatserver.messages.MessageContext;

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
    private ContextHelper contextHelper;

    /**
     * ������Ϣ
     *
     * @param message ������Ϣ
     */
    public void switchTo(Message message) {

        MessageContext context = contextHelper.getContext(message);
        if (message == null || context.getReceivers() == null || context.getReceivers().size() == 0) return;
        int messgeDestCount = context.getReceivers().size();
        List<CommonRunnable> outputer = threadResource.getThreads(ThreadResourceType.OUTPUT_THREAD);
        int outputerCount = outputer.size();
        if (messgeDestCount > outputerCount) {
            for (CommonRunnable runnable : outputer) {
                OutputThread ot = (OutputThread) runnable;
                ot.send(message);
            }
        } else {
            for (Client dest : context.getReceivers()) {
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
