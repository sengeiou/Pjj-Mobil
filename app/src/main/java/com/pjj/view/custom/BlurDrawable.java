package com.pjj.view.custom;

/**
 * Created by XinHeng on 2019/01/07.
 * describe：
 */

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;

import com.pjj.utils.BitmapBlurHelper;

/**
 * 模糊drawable
 */
public class BlurDrawable{
    //上下两层图片
    private Drawable[] array = new Drawable[2];
    //层叠图片
    private LayerDrawable la;

    /**
     * @param context
     * @param res
     * @param bitmap
     */
    public BlurDrawable(Context context, Resources res, Bitmap bitmap,int alpha) {
        array[0] = new BitmapDrawable(res,bitmap);
        array[1] = new BitmapDrawable(res,BitmapBlurHelper.doBlur(context,bitmap,25));//生产模糊图片
        array[1].setAlpha(alpha);
        la = new LayerDrawable(array);
        la.setLayerInset(0, 0, 0, 0, 0);//层叠
        la.setLayerInset(1, 0, 0, 0, 0);
    }

    /**
     * 返回层叠以后的图片
     * @return
     */
    public LayerDrawable getBlurDrawable() {
        return la;
    }

    /**
     * 获得模糊系数，本质上是透明度
     * @return
     */
    public int getBlur(){
        return array[1].getAlpha();
    }

    /**
     * 设置模糊系数
     * @param alpha
     */
    public void setBlur(int alpha){
        array[1].setAlpha(alpha);
    }
}

