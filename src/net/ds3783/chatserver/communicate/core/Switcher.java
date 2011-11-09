package net.ds3783.chatserver.communicate.core;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ds.3783
 * Date: 2010-4-2
 * Time: 23:27:11
 * �߳�ѡ����
 */
public interface Switcher<T> {
    /**
     * �����ݷ��͵�Ŀ��
     *
     * @param data ����
     */
    public void switchData(T data);

    /**
     * �����ݷ��͵�Ŀ��
     *
     * @param datas ����
     */
    public void switchData(List<T> datas);

    /**
     * ���ÿ�ѡ���Ŀ�꣬Ŀ�������ѡ�����ǰ����
     *
     * @param targets ��ѡ��Ŀ��
     */
    public void setTargets(Collection<? extends Switchable> targets);
}
