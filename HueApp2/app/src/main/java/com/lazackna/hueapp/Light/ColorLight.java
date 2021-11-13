package com.lazackna.hueapp.Light;

public class ColorLight extends Light{

    public int bri;
    public int hue;
    public int sat;
    double[] xy;

    public ColorLight(String id, String name, Light.PowerState powerState, int bri, int hue, int sat, double[] xy ) {
        super(id, name, powerState);
        this.bri = bri;
        this.hue = hue;
        this.sat = sat;
        this.xy = xy;
    }
}
