/**
 * Created with IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 13-1-6
 * Time: обнГ3:33
 * To change this template use File | Settings | File Templates.
 */
package {
import flash.events.Event;

public class ChannelChangeEvent extends Event {
    public var id:String;
    public var name:String;
    public var isPrivate:Boolean;
    public static const CHANGE_CHANNEL:String = "CHANGE_CHANNEL";

    public function ChannelChangeEvent(type:String, bubbles:Boolean = false, cancelable:Boolean = false) {
        super(type, bubbles, cancelable);
    }


}
}
