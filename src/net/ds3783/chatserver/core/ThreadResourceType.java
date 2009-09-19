package net.ds3783.chatserver.core;

public class ThreadResourceType {
    public static ThreadResourceType SERVER_THREAD = new ThreadResourceType("SERVER_THREAD", "�����������߳�");
    public static ThreadResourceType INPUT_THREAD = new ThreadResourceType("INPUT_THREAD", "���ݶ�ȡ�߳�");
    public static ThreadResourceType OUTPUT_THREAD = new ThreadResourceType("OUTPUT_THREAD", "���ݷ����߳�");
    public static ThreadResourceType PROCESS_THREAD = new ThreadResourceType("PROCESS_THREAD", "ҵ�����߳�");

    private String name;
    private String description;

    private ThreadResourceType(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ThreadResourceType that = (ThreadResourceType) o;

        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}