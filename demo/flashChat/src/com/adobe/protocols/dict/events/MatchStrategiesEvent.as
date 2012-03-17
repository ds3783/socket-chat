package com.adobe.protocols.dict.events {
import com.adobe.protocols.dict.Dict;

import flash.events.Event;

public class MatchStrategiesEvent extends Event {
    private var _strategies:Array;

    public function MatchStrategiesEvent() {
        super(Dict.MATCH_STRATEGIES);
    }

    public function set strategies(strategies:Array):void {
        this._strategies = strategies;
    }

    public function get strategies():Array {
        return this._strategies;
    }
}
}