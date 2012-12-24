/**
 * Created by IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 12-3-17
 * Time: 上午11:47
 * To change this template use File | Settings | File Templates.
 */
package net.ds3783.chatserver {
public class CommandType {
    public function CommandType() {
    }

    public static const LIST_CHANNELS:String = "LIST_CHANNELS";
    public static const CREATE_CHANNEL:String = "CREATE_CHANNEL";
    public static const JOIN_CHANNEL:String = "JOIN_CHANNEL";
    public static const EXIT_CHANNEL:String = "EXIT_CHANNEL";
    //更新客户列表
    public static const UPDATE_CLIENT_LIST:String = "UPDATE_CLIENT_LIST";
}
}
