package net.ds3783.chatserver.core;

public class ThreadResourceType {
    public static ThreadResourceType SERVER_THREAD = new ThreadResourceType("SERVER_THREAD", "服务器监听线程");
    public static ThreadResourceType INPUT_THREAD = new ThreadResourceType("INPUT_THREAD", "数据读取线程");
    public static ThreadResourceType OUTPUT_THREAD = new ThreadResourceType("OUTPUT_THREAD", "数据发送线程");
    public static ThreadResourceType PROCESS_THREAD = new ThreadResourceType("PROCESS_THREAD", "业务处理线程");
    public static ThreadResourceType GUARD_THREAD = new ThreadResourceType("GUARD_THREAD", "业务处理线程");

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