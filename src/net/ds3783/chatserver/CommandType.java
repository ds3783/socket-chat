package net.ds3783.chatserver;

/**
 * Created by IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 12-3-15
 * Time: 下午1:52
 * To change this template use File | Settings | File Templates.
 */
public class CommandType {
    public static final String LIST_CHANNELS = "LIST_CHANNELS";
    public static final String CREATE_CHANNEL = "CREATE_CHANNEL";
    public static final String JOIN_CHANNEL = "JOIN_CHANNEL";
    public static final String EXIT_CHANNEL = "EXIT_CHANNEL";


    //更新客户列表
    public static final String UPDATE_CLIENT_LIST = "UPDATE_CLIENT_LIST";

    public static String LOGOUT = "LOGOUT";
}
