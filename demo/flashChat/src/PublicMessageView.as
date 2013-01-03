/**
 * Created with IntelliJ IDEA.
 * User: Ds.3783
 * Date: 13-1-3
 * Time: ÉÏÎç12:02
 * To change this template use File | Settings | File Templates.
 */
package {
import mx.containers.Box;
import mx.containers.BoxDirection;
import mx.controls.Label;
import mx.controls.Text;
import mx.core.LayoutDirection;
import mx.formatters.DateFormatter;

import net.ds3783.chatserver.messages.PublicMessage;

public class PublicMessageView extends Box {
    private var dateTime:Label;
    private var channel:Label;
    private var sender:Label;
    private var text:Text;


    public function PublicMessageView(msg:PublicMessage) {
        this.layoutDirection = LayoutDirection.LTR;
        this.direction = BoxDirection.HORIZONTAL;
        this.setStyle("horizontalGap", 0);
        this.setStyle("verticalGap", 0);
        dateTime = new Label();
        dateTime.setStyle("color", "#CCCCCC");
        var fr:DateFormatter = new DateFormatter();
        fr.formatString = "MM-DD JJ:NN:SS";
        dateTime.text = fr.format(msg.timestamp);
        this.addChild(dateTime);

        channel = new Label();
        channel.text = "[" + msg.channelName + "]";
        channel.name = msg.channelId.toString();
        this.addChild(channel);

        sender = new Label();
        sender.text = msg.senderName + ":";
        sender.name = msg.senderId;
        //sender.x = this.width + 1;
        this.addChild(sender);

        text = new Text();
        text.width = 240;
        text.setStyle("leading", 0);
        text.text = msg.content || "";
        //text.x = this.width + 1;
        this.addChild(text);
    }
}
}
