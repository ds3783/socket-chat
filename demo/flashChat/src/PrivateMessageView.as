/**
 * Created with IntelliJ IDEA.
 * User: Ds.3783
 * Date: 13-1-3
 * Time: 上午12:02
 * To change this template use File | Settings | File Templates.
 */
package {
import flash.events.MouseEvent;

import mx.containers.Box;
import mx.containers.BoxDirection;
import mx.controls.Label;
import mx.controls.Text;
import mx.core.LayoutDirection;
import mx.formatters.DateFormatter;

import net.ds3783.chatserver.messages.PrivateMessage;

import net.ds3783.chatserver.messages.PublicMessage;

public class PrivateMessageView extends Box {
    private var dateTime:Label;
    private var secert:Label;
    private var sender:Label;
    private var text:Text;


    public function PrivateMessageView(msg:PrivateMessage, ismyself:Boolean) {
        this.layoutDirection = LayoutDirection.LTR;
        this.direction = BoxDirection.HORIZONTAL;
        this.setStyle("horizontalGap", 0);
        this.setStyle("verticalGap", 0);
        if (!msg.timestamp) {
            msg.timestamp = new Date();
        }
        dateTime = new Label();
        dateTime.setStyle("color", "#CCCCCC");
        var fr:DateFormatter = new DateFormatter();
        fr.formatString = "JJ:NN:SS";
        dateTime.text = fr.format(msg.timestamp);
        this.addChild(dateTime);
        if (ismyself) {
            secert = new Label();
            secert.text = "我悄悄对";
            secert.setStyle("color", "#ef5bff");
            this.addChild(secert);
            sender = new Label();
            sender.setStyle("color", "#ef5bff");
            sender.useHandCursor = true;
            sender.mouseChildren = false;
            sender.buttonMode = true;
            sender.data = {id: msg.reveiverId, name: msg.reveiverName};
            sender.addEventListener(MouseEvent.CLICK, onSenderClick);
            sender.text = "[" + msg.reveiverName + "]";
            this.addChild(sender);
            secert = new Label();
            secert.setStyle("color", "#ef5bff");
            secert.text = "说:";
            this.addChild(secert);
        } else {
            sender = new Label();
            sender.setStyle("color", "#ef5bff");
            sender.text = "[" + msg.senderName + "]";
            sender.data = {id: msg.senderId, name: msg.senderName};
            sender.useHandCursor = true;
            sender.mouseChildren = false;
            sender.buttonMode = true;
            sender.addEventListener(MouseEvent.CLICK, onSenderClick);
            this.addChild(sender);


            secert = new Label();
            secert.setStyle("color", "#ef5bff");
            secert.text = "悄悄对我说:";
            this.addChild(secert);
        }


        text = new Text();
        text.width = 240;
        text.setStyle("color", "#ef5bff");
        text.setStyle("leading", 0);
        text.text = msg.content || "";
        this.addChild(text);
    }

    private function onSenderClick(event:MouseEvent):void {
        var channel:Label = event.target as Label;
        if (channel) {
            var evt:ChannelChangeEvent = new ChannelChangeEvent(ChannelChangeEvent.CHANGE_CHANNEL);
            evt.id = channel.data.id;
            evt.name = channel.data.name;
            evt.isPrivate = true;
            this.dispatchEvent(evt);
        }
    }
}
}
