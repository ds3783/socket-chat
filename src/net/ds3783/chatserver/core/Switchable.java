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
     * 取得权重
     * 权重关系到选择器的选择结果
     *
     * @return
     */
    public int getWeight();

    /**
     * 接收数据
     *
     * @param data
     */
    public void receive(T data);
}
