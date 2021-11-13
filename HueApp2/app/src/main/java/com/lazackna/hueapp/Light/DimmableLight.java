package com.lazackna.hueapp.Light;

public class DimmableLight extends Light{

    public int bri;

    public DimmableLight(String id, String name, int bri, Light.PowerState powerState) {
        super(id, name, powerState);
        this.bri = bri;
    }
}
