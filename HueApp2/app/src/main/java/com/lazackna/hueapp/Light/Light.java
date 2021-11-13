package com.lazackna.hueapp.Light;

import java.io.Serializable;

public abstract class Light implements Serializable {
    public int id;
    public String uniqueId;
    public String name;
    public PowerState powerState;

    public Light(int id, String uniqueId, String name, PowerState powerState) {
        this.id = id;
        this.uniqueId = uniqueId;
        this.name = name;
        this.powerState = powerState;
    }

    public enum PowerState{
        ON,
        OFF
    }


}
