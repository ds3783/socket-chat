package net.ds3783.chatserver.core;

import java.util.*;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by IntelliJ IDEA.
 * User: Ds.3783
 * Date: 2010-4-7
 * Time: 12:30:49
 * 负载均衡选择器
 * 选择器会根据每个进程的权重做出相应的选择，权重越低，被选择概率越大
 * 权重临界值：当任意一个进程权重低于临界值时，则不遵循权重选择算法，直接选择该进程处理数据
 * 权重临界值默认为10
 */
public class LoadBalanceSwitcher<T> implements Switcher<T> {
    /**
     * 权重临界值
     */
    private int weightCritical = 10;

    private long lastInitialTime = 0;

    private long initialInterval = 30000;

    private int runTimeInterval = 200;

    private Map<Integer, Switchable<T>> switchers = new HashMap<Integer, Switchable<T>>();

    private PriorityBlockingQueue<SwitchableContainer<T>> potanceQueue = new PriorityBlockingQueue<SwitchableContainer<T>>();

    /**
     * 将数据发送到目标
     *
     * @param data 数据
     */
    public void switchData(T data) {
        if (--runTimeInterval < 0) {
            long now = System.currentTimeMillis();
            if (now - lastInitialTime > initialInterval) {
                init();
            }
        }
        SwitchableContainer<T> c = potanceQueue.peek();
        if (c.getWeight() < weightCritical) {
            c.switchData(data);
            return;
        }
        if (c.getLeftweight() >= 0) {
            c.switchData(data);
            return;
        }
        c = potanceQueue.poll();
        potanceQueue.offer(c);
        potanceQueue.peek().switchData(data);
    }

    private void init() {
        runTimeInterval = 200;
        ArrayList<SwitchableContainer<T>> temp = new ArrayList<SwitchableContainer<T>>(potanceQueue);
        potanceQueue.clear();
        for (SwitchableContainer<T> container : temp) {
            container.reset();
            potanceQueue.offer(container);
        }
        SwitchableContainer<T> last = null;
        for (SwitchableContainer<T> container : potanceQueue) {
            if (last != null) {
                int left = container.getWeight() - last.getWeight();
                last.setLeftweight(left);
            }
            last = container;
        }
    }

    /**
     * 将数据发送到目标
     *
     * @param datas 数据
     */
    public void switchData(List<T> datas) {
        if (datas != null) {
            for (T data : datas) {
                this.switchData(data);
            }
        }
    }

    /**
     * 设置可选择的目标，目标必须在选择过程前设置
     *
     * @param targets
     */
    public void setTargets(Collection<? extends Switchable> targets) {
        int idcounter = 0;
        switchers.clear();
        if (targets != null) {
            for (Switchable<T> target : targets) {
                int id = ++idcounter;
                switchers.put(id, target);
                this.potanceQueue.add(new SwitchableContainer<T>(target));
            }
        }
    }

    public void setWeightCritical(int weightCritical) {
        this.weightCritical = weightCritical;
    }

    private class SwitchableContainer<T> implements Comparable<SwitchableContainer> {
        private int weight;
        private int leftweight;
        private Switchable<T> target;

        public SwitchableContainer(Switchable<T> target) {
            this.target = target;
            leftweight = 0;
            weight = target.getWeight();
        }

        public void switchData(T data) {
            weight++;
            leftweight--;
            target.receive(data);
        }

        public int getWeight() {
            return weight;
        }

        public void setWeight(int weight) {
            this.weight = weight;
        }

        public int getLeftweight() {
            return leftweight;
        }

        public void setLeftweight(int leftweight) {
            this.leftweight = leftweight;
        }

        public Switchable<T> getTarget() {
            return target;
        }

        public void setTarget(Switchable<T> target) {
            this.target = target;
        }

        public int compareTo(SwitchableContainer other) {
            int otherWeight = other.getWeight();
            if (this.weight < otherWeight) {
                return -1;
            } else if (this.weight > otherWeight) {
                return 1;
            } else {
                return 0;
            }
        }

        public void reset() {
            this.weight = target.getWeight();
            this.leftweight = 0;
        }
    }
}
