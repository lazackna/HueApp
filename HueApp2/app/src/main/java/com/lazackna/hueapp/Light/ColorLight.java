package com.lazackna.hueapp.Light;

public class ColorLight extends Light{

    public int bri;
    public int hue;
    public int sat;
    public double[] xy;

    public int getBri() {
        return this.bri;
    }

    public int getHue() {
        return this.hue;
    }

    public int getSat() {
        return this.sat;
    }

    public double[] getXy() {
        return this.xy;
    }

    public ColorLight(int id, String uniqueId, String name, Light.PowerState powerState, int bri, int hue, int sat, double[] xy ) {
        super(id, uniqueId, name, powerState);
        this.bri = bri;
        this.hue = hue;
        this.sat = sat;
        this.xy = xy;
    }
}
