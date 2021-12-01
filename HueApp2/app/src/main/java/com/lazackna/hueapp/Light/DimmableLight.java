package com.lazackna.hueapp.Light;

public class DimmableLight extends Light{

    public int bri;

    public int getBri() {return  this.bri; }

    public DimmableLight(int id, String uniqueId, String name, int bri, Light.PowerState powerState) {
        super(id, uniqueId, name, powerState);
        this.bri = bri;
        if (bri < 0) this.bri = 0;
        if (bri > 254) this.bri = 254;
    }
}
