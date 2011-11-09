package net.ds3783.chatserver.communicate.protocol;

import net.ds3783.chatserver.Message;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2010-2-24
 * Time: 22:59:19
 */
public abstract class OutputProtocal {
    protected List<Message> messages = new ArrayList<Message>();

    /**
     * ���һ����Ϣ
     *
     * @param msg ��Ϣ
     */
    public void addMessage(Message msg) {
        messages.add(msg);
    }

    /**
     * ���������Ϣ
     *
     * @param msgs ��Ϣ�ļ���
     */
    public void addMessages(Collection<Message> msgs) {
        if (msgs != null)
            messages.addAll(msgs);
    }

    /**
     * ����
     *
     * @return ������������ϵ��ֽ�����
     */
    public abstract byte[] marshal() throws MarshalException;

}
