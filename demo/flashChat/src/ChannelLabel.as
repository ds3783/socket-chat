/**
 * Created with IntelliJ IDEA.
 * User: Ds.3783
 * Date: 13-1-1
 * Time: ÏÂÎç3:53
 * To change this template use File | Settings | File Templates.
 */
package {
public class ChannelLabel {
    public function ChannelLabel(id:String, name:String, b:Boolean) {
        this.id = id;
        this.label = name;
        this.isSecert = b;
        this.lastAccessTime = new Date().getTime();
    }


    public var id:String;
    public var label:String;
    public var isSecert:Boolean;
    public var lastAccessTime:Number;

    public static function equals(a:ChannelLabel, b:ChannelLabel):Boolean {
        if (a == null || b == null) {
            return false;
        }
        return a.isSecert == b.isSecert && a.id == b.id;
    }
}
}
