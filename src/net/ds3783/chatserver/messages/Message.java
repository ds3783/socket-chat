package net.ds3783.chatserver.messages;

/**
 * Created by IntelliJ IDEA.
 * User: Ds3783
 * Date: 11-11-17
 * Time: ����10:16
 * ��Ϣ�ӿڣ�����ͨ�ŵ���Ϣ����ʵ�ִ˽ӿ�
 */
public interface Message {
    /**
     * @return ��Ϣ���ͣ����ڱ�ʶ����Ϣ�ľ�������
     */
    public String getType();

    /**
     * @return �Ƿ�����л�
     */
    public boolean isSerializable();

    /**
     * @return ��Ϣ�����ݣ�ĳЩЭ��Բ������л�����Ϣ��ʹ����������Ϊ��������
     */
    public String getContent();


}
