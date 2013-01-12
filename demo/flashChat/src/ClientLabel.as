/**
 * Created with IntelliJ IDEA.
 * User: hongyu.pi
 * Date: 13-1-6
 * Time: 下午3:50
 * To change this template use File | Settings | File Templates.
 */
package {
import flash.events.MouseEvent;

import mx.controls.Label;

import net.ds3783.chatserver.messages.model.ClientModel;

public class ClientLabel extends Label {
    public function ClientLabel(model:ClientModel) {
        super();
        this.text = model.name;
        this.name = model.id;
        if (model.mySelf) {
            this.setStyle('color', 'blue');
        } else if (model.admin) {
            this.setStyle('color', 'red');
        }
        this.setStyle("paddingLeft", "10");
        this.data = {id: model.id, name: model.name};

        this.useHandCursor = true;
        this.mouseChildren = false;
        this.buttonMode = true;

        this.addEventListener(MouseEvent.CLICK, onClick);
    }

    private function onClick(event:MouseEvent):void {
        var evt:ChannelChangeEvent = new ChannelChangeEvent(ChannelChangeEvent.CHANGE_CHANNEL);
        evt.id = this.data.id;
        evt.name = this.data.name;
        evt.isPrivate = true;
        this.dispatchEvent(evt);
    }

    public function am(client:ClientModel):Boolean {
        if (!client || !this.data) {
            return false;
        }
        return client.id == this.data.id;
    }
}
}
