package com.lazackna.hueapp.Light;

public class DimmableLight extends Light{

    public int bri;

    public DimmableLight(int id, String uniqueId, String name, int bri, Light.PowerState powerState) {
        super(id, uniqueId, name, powerState);
        this.bri = bri;
    }
}
