package com.lazackna.hueapp;

public class Light {
    public int id;
    public int bri;
    public int hue;
    public int sat;
    public double[] xy;
    public String name;


    public Light(int id, int bri, int hue, int sat, double[] xy, String name) {
        this.id = id;
        this.bri = bri;
        this.hue = hue;
        this.sat = sat;
        this.xy = xy;
        this.name = name;
    }


}
