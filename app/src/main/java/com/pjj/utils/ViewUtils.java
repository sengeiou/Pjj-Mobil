package com.pjj.utils;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pjj.PjjApplication;
import com.pjj.R;
import com.pjj.module.xspad.XspManage;

import static android.text.Html.FROM_HTML_MODE_LEGACY;

public class ViewUtils {
    public static void releaseView(View view) {

    }

    public static void setTopDrawable(int resource, TextView tv) {
        Drawable drawable = ContextCompat.getDrawable(PjjApplication.application, resource);
        int dp13 = PjjApplication.application.getResources().getDimensionPixelSize(R.dimen.dp_13);
        int dp70 = PjjApplication.application.getResources().getDimensionPixelSize(R.dimen.dp_70);
        drawable.setBounds(dp13, 0, dp13 + dp70, dp70);
        tv.setCompoundDrawables(null, drawable, null, null);
    }

    public static Drawable getBgDrawable(int color, int colorSide, int radius) {
        int dp1 = getDp(R.dimen.dp_1);
        if (radius <= 0) {
            radius = dp1;
        }
        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(color);
        gradientDrawable.setStroke(dp1, colorSide);
        gradientDrawable.setCornerRadius(radius);
        return gradientDrawable;
    }

    /**
     * select 动态生成
     *
     * @param resourceId
     * @param norId
     * @param state      如 android.R.attr.state_pressed ;android.R.attr.state_checked
     * @return
     */
    public static Drawable createSelectDrawable(int resourceId, int norId, int state) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{state}, getDrawable(resourceId));
        drawable.addState(new int[]{}, getDrawable(norId));
        return drawable;
    }

    public static TextView createTextView(Context context, String text) {
        TextView tv = new TextView(context);
        tv.setText(text);
        tv.setGravity(Gravity.CENTER);
        tv.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return tv;
    }

    public static View createLineHView(Context context) {
        View view = new View(context);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_1)));
        return view;
    }

    public static int getDp(int resource) {
        return PjjApplication.application.getResources().getDimensionPixelSize(resource);
    }

    public static float getFDp(int resource) {
        return PjjApplication.application.getResources().getDimension(resource);
    }

    public static int getColor(int resource) {
        return ContextCompat.getColor(PjjApplication.application, resource);
    }

    public static Drawable getDrawable(int resource) {
        return ContextCompat.getDrawable(PjjApplication.application, resource);
    }

    /**
     * 给定文字的center获取文字的base line
     */
    public static float getTextBaseLineByCenter(float center, TextPaint paint, int size) {
        paint.setTextSize(size);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        float height = fontMetrics.bottom - fontMetrics.top;
        return center + height / 2 - fontMetrics.bottom;
    }

    public static void setHtmlText(TextView tv, String colorText) {
        //<font color=#ff0000>
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv.setText(Html.fromHtml(colorText, FROM_HTML_MODE_LEGACY));
        } else {
            tv.setText(Html.fromHtml(colorText));
        }
    }

    public static String getHtmlText(String text, String color) {
        return "<font color=\"" + color + "\">" + text + "</font>";
    }

    /**
     * 选中广告屏的信息
     *
     * @param view
     * @param time  时长
     * @param price 价格
     */
    public static void setXspInf(View view, String time, String price) {
        String count = XspManage.getInstance().getXspNum() + "";
        TextView tv_xsp_count = view.findViewById(R.id.tv_xsp_count);
        TextView tv_xsp_time = view.findViewById(R.id.tv_xsp_time);
        TextView tv_xsp_price = view.findViewById(R.id.tv_xsp_price);
        setHtmlText(tv_xsp_count, "屏幕数量：<font color=\"#40BBF7\">" + count + "</font> 面");
        setHtmlText(tv_xsp_time, "发布时长：<font color=\"#40BBF7\">" + time + "</font> 小时");
        setHtmlText(tv_xsp_price, "订单金额：<font color=\"#40BBF7\">" + price + "</font> 元");
    }

    public static void addEmpty2Text(TextView tv, String s) {
        SpannableStringBuilder span = new SpannableStringBuilder("缩进" + s);
        span.setSpan(new ForegroundColorSpan(Color.TRANSPARENT), 0, 2,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv.setText(span);
    }
}
