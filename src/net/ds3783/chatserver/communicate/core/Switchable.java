package net.ds3783.chatserver.communicate.core;

/**
 * Created by IntelliJ IDEA.
 * User: Ds.3783
 * Date: 2010-4-6
 * Time: 13:40:34
 * �ɱ�����ӿڣ�����ĳ����Դ�ķ��䣬���б�����Ŀ�������Ҫʵ�ִ˽ӿ�
 */
public interface Switchable<T> {

    /**
     * ȡ��Ȩ��
     * Ȩ�ع�ϵ��ѡ������ѡ����
     *
     * @return Ȩ��
     */
    public int getWeight();

    /**
     * ��������
     *
     * @param data ������ഫ�͵�����
     */
    public void receive(T data);
}
