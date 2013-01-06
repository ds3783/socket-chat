package net.ds3783.chatserver.communicate.core;

import java.util.Collection;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ds.3783
 * Date: 2010-4-2
 * Time: 23:27:11
 * 线程选择器
 */
public interface Switcher<T> {
    /**
     * 将数据发送到目标
     *
     * @param data 数据
     */
    public void switchData(T data);

    /**
     * 将数据发送到目标
     *
     * @param datas 数据
     */
    public void switchData(List<T> datas);

    /**
     * 设置可选择的目标，目标必须在选择过程前设置
     *
     * @param targets 可选择目标
     */
    public void setTargets(Collection<? extends Switchable> targets);
}
