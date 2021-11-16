package com.lazackna.hueapp.Light;

public class ColorLight extends Light{

    public int bri;
    public int hue;
    public int sat;
    public float[] xy;

    public ColorLight(int id, String uniqueId, String name, Light.PowerState powerState, int bri, int hue, int sat, float[] xy, String model ) {
        super(id, uniqueId, name, powerState, model);
        this.bri = bri;
        this.hue = hue;
        this.sat = sat;
        this.xy = xy;
    }
}
