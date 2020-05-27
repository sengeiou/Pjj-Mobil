package com.pjj.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.pjj.R;
import com.pjj.utils.TextUtils;
import com.pjj.utils.ViewUtils;

/**
 * Create by xinheng on 2018/11/06 16:03。
 * describe：标题
 */
public class TitleView extends View {
    private String textMiddle;
    private int textMiddleSize = ViewUtils.getDp(R.dimen.sp_16);
    private int textMiddleColor = Color.BLACK;
    private Drawable drawableLeft;
    private Paint paint;
    private Rect rect;
    private boolean touchInter = false;

    public String getTextMiddle() {
        return textMiddle;
    }

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitleView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TitleView, defStyle, 0);
        int indexCount = typedArray.getIndexCount();
        for (int i = 0; i < indexCount; i++) {
            int index = typedArray.getIndex(i);
            switch (index) {
                case R.styleable.TitleView_text_middle:
                    textMiddle = typedArray.getString(index);
                    break;
                case R.styleable.TitleView_text_middle_size:
                    textMiddleSize = typedArray.getDimensionPixelSize(index, textMiddleSize);
                    break;
                case R.styleable.TitleView_text_middle_color:
                    textMiddleColor = typedArray.getColor(index, textMiddleColor);
                    break;
                case R.styleable.TitleView_src_left:
                    drawableLeft = typedArray.getDrawable(index);
                    break;
            }
        }
        typedArray.recycle();
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //标题固定宽高 ，无需计算 //42dp
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (null != drawableLeft) {
            int dp18 = getResources().getDimensionPixelSize(R.dimen.dp_18);
            //int dp10 = getResources().getDimensionPixelSize(R.dimen.dp_10);
            int dp14 = getResources().getDimensionPixelSize(R.dimen.dp_14);
            // 10*18 46-18=28 14dp
            int right = dp14 + dp18;
            drawableLeft.setBounds(dp14, dp14, right, right);
            if (null == rect)
                rect = new Rect();
            drawableLeft.setColorFilter(textMiddleColor, PorterDuff.Mode.MULTIPLY);
            drawableLeft.draw(canvas);
            rect.set(0, 0, right + 20, getMeasuredHeight());
        } else {
            rect = null;
        }
        if (!TextUtils.isEmpty(textMiddle)) {
            paint.setTextSize(textMiddleSize);
            paint.setColor(textMiddleColor);
            paint.setTextAlign(Paint.Align.CENTER);
            Paint.FontMetrics fontMetrics = paint.getFontMetrics();
            float baseLine = getMeasuredHeight() / 2f - fontMetrics.bottom / 2 - fontMetrics.top / 2;
            canvas.drawText(textMiddle, getMeasuredWidth() / 2f, baseLine, paint);
        }
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        onClickListener = l;
    }

    private View.OnClickListener onClickListener;

    public void setTouchInter(boolean touchInter) {
        this.touchInter = touchInter;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(touchInter);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            if (null != rect && rect.contains(x, y) && null != onClickListener) {
                onClickListener.onClick(this);
                return true;
            }
        }
        return super.onTouchEvent(event);
    }

    public void setTextMiddle(String textMiddle, int color) {
        this.textMiddle = textMiddle;
        this.textMiddleColor = color;
        if (getMeasuredWidth() > 0) {
            invalidate();
        }
    }

    public void setDrawableLeft(Drawable drawableLeft) {
        this.drawableLeft = drawableLeft;
        if (getMeasuredWidth() > 0) {
            invalidate();
        }
    }
}
