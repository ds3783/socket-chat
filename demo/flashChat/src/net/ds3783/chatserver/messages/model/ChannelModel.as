/**
 * Created by IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 12-3-17
 * Time: 上午11:39
 * To change this template use File | Settings | File Templates.
 */
package net.ds3783.chatserver.messages.model {
public class ChannelModel {
    public function ChannelModel() {
    }

    public var id:Number;
    public var name:String;
    public var internal:Boolean;
    public var maxMember:int;
    public var defaultChannel:Boolean;
}
}
