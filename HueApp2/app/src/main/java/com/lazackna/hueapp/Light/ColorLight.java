package com.lazackna.hueapp.Light;

import androidx.annotation.IntRange;

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

    public ColorLight(int id, String uniqueId, String name, Light.PowerState powerState,@IntRange(from = 0, to = 65535) int bri,@IntRange(from = 0, to = 254) int hue,@IntRange(from = 0, to = 254) int sat,double[] xy ) {
        super(id, uniqueId, name, powerState);
        this.bri = bri;
        if (bri < 0) this.bri = 0;
        if (bri > 254) this.bri = 254;
        this.hue = hue;
        if (hue < 0) this.hue = 0;
        if (hue > 65535) this.hue = 65535;
        this.sat = sat;
        if (sat < 0) this.sat = 0;
        if (sat > 254) this.sat = 254;
        this.xy = xy;
        if (xy[0] > 1) this.xy[0] = 1;
        if (xy[0] < 0) this.xy[0] = 0;
        if (xy[1] > 1) this.xy[1] = 1;
        if (xy[1] < 0) this.xy[1] = 0;
    }
}
