package com.adobe.protocols.dict.events {
import com.adobe.protocols.dict.Dict;

import flash.events.Event;

public class DatabaseEvent extends Event {
    private var _databases:Array;

    public function DatabaseEvent() {
        super(Dict.DATABASES);
    }

    public function set databases(databases:Array):void {
        this._databases = databases;
    }

    public function get databases():Array {
        return this._databases;
    }
}
}