package net.ds3783.chatserver.core;

/**
 * Created by IntelliJ IDEA.
 * User: Ds.3783
 * Date: 2010-4-6
 * Time: 13:40:34
 * To change this template use File | Settings | File Templates.
 */
public interface Switchable<T> {

    /**
     * ȡ��Ȩ��
     * Ȩ�ع�ϵ��ѡ������ѡ����
     *
     * @return
     */
    public int getWeight();

    /**
     * ��������
     *
     * @param data
     */
    public void receive(T data);
}
