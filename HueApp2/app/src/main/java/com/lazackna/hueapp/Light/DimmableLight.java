package com.lazackna.hueapp.Light;

import android.os.Bundle;
import android.widget.SeekBar;

import com.lazackna.hueapp.R;

public class DimmableLight extends Light{

    public int bri;

    public DimmableLight(int id, String uniqueId, String name, int bri, Light.PowerState powerState) {
        super(id, uniqueId, name, powerState);
        this.bri = bri;
    }


    public void SeekbarChanged() {
        SeekBar seekbar = (SeekBar) findViewByID(com.lazackna.hueapp.R.id.dimmable_brightness);
        int seekbarValue = seekbar.getProgress();
        this.bri = seekbarValue;
    }

}
