/**
 * Created with IntelliJ IDEA.
 * User: Ds.3783
 * Date: 12-12-31
 * Time: ÏÂÎç9:37
 * To change this template use File | Settings | File Templates.
 */
package {
import mx.collections.ArrayCollection;
import mx.controls.ComboBox;

import net.ds3783.chatserver.messages.model.ChannelModel;

public class ChannelSelect extends ComboBox {

    private var secertChannels:Array;
    private var MAX_SRCERT:int = 5;

    public function ChannelSelect() {
        super();
        secertChannels = [];
    }

    public function setChannelList(channels:Array):void {
        var channelModel:ChannelModel;
        var secertItem:ChannelLabel = null;
        var newData:Array = [];


        for each (channelModel in channels) {
            newData.push(new ChannelLabel(channelModel.id.toString(10), channelModel.name, false));
        }

        for each(var channelObj:ChannelLabel  in this.secertChannels) {
            if (!(secertItem && ChannelLabel.equals(channelObj, secertItem))) {
                newData.push(channelObj);
            }
        }
        updateData(newData, null);
    }


    public function selectUser(userName:String, userId:String) {
        if (
                this.secertChannels.some(function (element:ChannelLabel, index:int, arr:Array) {
                })
                ) {
            if (this.secertChannels.length > this.MAX_SRCERT && this.secertChannels.length > 0) {
                this.secertChannels.sort(secertSort);
                var idx:int = this.secertChannels.length - 1;
                for (; idx >= 0; idx--) {
                    var last:ChannelLabel = this.secertChannels[idx];
                    var selected:ChannelLabel = this.selectedItem as ChannelLabel;
                    if (!ChannelLabel.equals(last, selected)) {
                        this.secertChannels.splice(idx, 1);
                        break;
                    }
                }

            }
        }

        this.secertChannels.push(new ChannelLabel(userId, "Secert[" + userName + "]", true));
        var newData:Array = [], channel:ChannelLabel;

        for each(channel in this.data) {
            if (!channel.isSecert) {
                newData.push(channel);
            }
        }
        for each(channel in this.secertChannels) {
            newData.push(channel);
        }
        updateData(newData, null);
    }


    public function selectChannel(channelId:Number) {

    }

    public function getCurrentChannel():ChannelLabel {
        return this.selectedItem as ChannelLabel;
    }

    private function updateData(newData:Array, selectedItem:ChannelLabel):void {
        if (!newData) {
            newData = [];
        }
        if (!selectedItem) {
            selectedItem = this.selectedItem as ChannelLabel;
        }

        newData.sort(dataSort);
        var newIdx = -1;
        for (var i:int = 0; i < newData.length; i++) {
            var value:ChannelLabel = newData[i];
            if (ChannelLabel.equals(value, selectedItem)) {
                newIdx = i;
                break;
            }
        }
        this.dataProvider = new ArrayCollection(newData);
        if (newIdx < 0 && newData.length > 0) {
            newIdx = 0;
        }
        this.selectedIndex = newIdx;
    }

    private static function dataSort(a:ChannelLabel, b:ChannelLabel):int {
        if (a == null) {
            return -1;
        }
        if (b == null) {
            return 1;
        }
        if (!a.isSecert && !b.isSecert) {
            return 0;
        }
        if (a.isSecert && !b.isSecert) {
            return -1;
        }
        if (!a.isSecert && b.isSecert) {
            return 1;
        }
        if (a.lastAccessTime < b.lastAccessTime) {
            return -1;
        }
        if (a.lastAccessTime > b.lastAccessTime) {
            return 1;
        }
        return 0;
    }


    private static function secertSort(a:ChannelLabel, b:ChannelLabel):int {
        if (a == null) {
            return -1;
        }
        if (b == null) {
            return 1;
        }
        if (a.lastAccessTime < b.lastAccessTime) {
            return -1;
        }
        if (a.lastAccessTime > b.lastAccessTime) {
            return 1;
        }
        return 0;
    }

}
}
