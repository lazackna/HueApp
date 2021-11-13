package com.lazackna.hueapp.Light;

public abstract class Light {
    public String id;
    public String name;
    public PowerState powerState;

    public Light(String id, String name, PowerState powerState) {
        this.id = id;
        this.name = name;
        this.powerState = powerState;
    }

    public enum PowerState{
        ON,
        OFF
    }


}
