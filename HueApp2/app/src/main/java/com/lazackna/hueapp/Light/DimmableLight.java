package com.lazackna.hueapp.Light;

public class DimmableLight extends Light{

    public int bri;

    public DimmableLight(int id, String uniqueId, String name, int bri, Light.PowerState powerState, String model) {
        super(id, uniqueId, name, powerState, model);
        this.bri = bri;
    }
}
