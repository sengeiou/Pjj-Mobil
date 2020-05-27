package com.pjj.view.custom;

import android.view.animation.Interpolator;

/**
 * Created by XinHeng on 2019/02/25.
 * describe：
 */
public class CustomeInterpolator implements Interpolator {
    @Override
    public float getInterpolation(float x) {
        if(x<0.6){
            return x;
        }
        float factor = 0.3f;
        return (float) (Math.pow(2, -10 * x) * Math.sin((x - factor / 4) * (2 * Math.PI) / factor) + 1f);
    }
}
