package net.ds3783.chatserver.communicate.core;

/**
 * Created by IntelliJ IDEA.
 * User: Ds.3783
 * Date: 2010-4-6
 * Time: 13:40:34
 * 可被分配接口，用于某种资源的分配，所有被分配的控制类需要实现此接口
 */
public interface Switchable<T> {

    /**
     * 取得权重
     * 权重关系到选择器的选择结果
     *
     * @return 权重
     */
    public int getWeight();

    /**
     * 接收数据
     *
     * @param data 向控制类传送的数据
     */
    public void receive(T data);
}
