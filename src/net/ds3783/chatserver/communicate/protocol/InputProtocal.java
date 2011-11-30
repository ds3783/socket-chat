package net.ds3783.chatserver.communicate.protocol;

import net.ds3783.chatserver.messages.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2010-2-24
 * Time: 22:30:56
 */
public abstract class InputProtocal {
    protected byte[] data;
    protected byte[] remains;
    protected List<Message> messages;

    protected InputProtocal() {
        messages = new ArrayList<Message>();
    }

    /**
     * ������������
     *
     * @param data �����϶�ȡ�Ķ���������
     */
    public void setData(byte[] data) {
        this.data = data;
    }

    /**
     * ȡ�ý������Ϣ��������������unmarshal���ú�ִ��
     *
     * @return ��������Ϣ����
     */
    public int messageCount() {
        if (messages != null) {
            return messages.size();
        } else {
            return 0;
        }
    }

    /**
     * ȡ�ý�������Ϣ
     *
     * @return ��������Ϣ
     */
    public List<Message> getMessages() {
        if (messages != null) {
            return new ArrayList<Message>(messages);
        } else {
            return new ArrayList<Message>();
        }
    }

    /**
     * ȡ�ý����ʣ��δ������ֽ�
     *
     * @return
     */
    public byte[] getRemains() {
        if (remains != null) {
            return remains;
        } else {
            return new byte[0];
        }
    }

    public void reset() {
        data = new byte[0];
        remains = new byte[0];
        messages.clear();
    }

    /**
     * ����
     */
    public abstract void unmarshal() throws UnmarshalException;
}
