package com.adobe.protocols.dict.events {
import com.adobe.protocols.dict.Definition;
import com.adobe.protocols.dict.Dict;

import flash.events.Event;

public class DefinitionEvent extends Event {
    private var _definition:Definition;

    public function DefinitionEvent() {
        super(Dict.DEFINITION);
    }

    public function set definition(definition:Definition):void {
        this._definition = definition;
    }

    public function get definition():Definition {
        return this._definition;
    }
}
}