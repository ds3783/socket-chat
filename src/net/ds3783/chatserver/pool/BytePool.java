package net.ds3783.chatserver.pool;

/**
 * Created by IntelliJ IDEA.
 * User: ds3783
 * Date: 2010-2-24
 * Time: 23:06:38
 */
public interface BytePool {

    /**
     * �򻺴�ĩβ�������
     *
     * @param clientId �ͻ���ID
     * @param data     ����
     */
    public void offerBytes(String clientId, byte[] data);

    /**
     * �ӻ����л�ȡ���ݣ�һ����ȡ�������е����ݽ������
     *
     * @param clientId �ͻ���ID
     * @return �����е�����
     */
    public byte[] poolBytes(String clientId);

    /**
     * �ӻ����л�ȡ�̶��������ݣ�һ����ȡ�������е����ݽ������
     * ������������ݳ��Ȳ���Ҫ�󳤶ȣ��򷵻ػ���������ʣ�����ݡ�
     * ע�⣺���ؽ����length���Բ�һ���ʹ��������lengthһ��
     *
     * @param clientId �ͻ���ID
     * @param length   ����ȡ���ݳ���
     * @return �����е�����
     */
    public byte[] poolBytes(String clientId, int length);

    /**
     * �޸Ļ�������
     *
     * @param clientId �ͻ���ID
     * @param data     ����
     */
    public void setBytes(String clientId, byte[] data);

    /**
     * ȡ�û������ݣ��������������
     *
     * @param clientId �ͻ���ID
     * @return ��������
     */
    public byte[] getBytes(String clientId);

    /**
     * ȡ�ù̶����ȵĻ������ݣ��������������
     * ������������ݳ��Ȳ���Ҫ�󳤶ȣ��򷵻ػ���������ʣ�����ݡ�
     * ע�⣺���ؽ����length���Բ�һ���ʹ��������lengthһ��
     *
     * @param clientId �ͻ���ID
     * @param length   ����ȡ���ݳ���
     * @return ��������
     */
    public byte[] getBytes(String clientId, int length);

    /**
     * ȡ�û������ݵĳ���
     *
     * @param clientId �ͻ���ID
     * @return ���泤��
     */
    public int getCachedSize(String clientId);
}
