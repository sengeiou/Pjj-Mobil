package com.pjj.view.custom;

/**
 * Created by XinHeng on 2019/01/24.
 * describe：每一行的文字垂直居中
 */

import android.content.Context;
import android.graphics.Canvas;
import android.text.Editable;
import android.util.AttributeSet;
import android.view.Gravity;
public class LineTextView extends android.support.v7.widget.AppCompatTextView {
    private float ITEM_HEIGHT = 125;
    boolean reLayout = false;
    TextWatcher textWatcher;

    public LineTextView(Context context) {
        this(context, null);
    }

    public LineTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (textWatcher != null) {
                    textWatcher.beforeTextChanged(charSequence, i, i1, i2);
                }
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                float add = ITEM_HEIGHT;
                setLineSpacing(0f, 1f);
                setLineSpacing(add, 0);
                setIncludeFontPadding(false);
                setGravity(Gravity.CENTER_VERTICAL);
                int top = (int) ((add - getTextSize()) * 0.5f);
                setPadding(getPaddingLeft(), top, getPaddingRight(), -top);
                if (textWatcher != null) {
                    textWatcher.onTextChanged(charSequence, i, i1, i2);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (textWatcher != null) {
                    textWatcher.afterTextChanged(editable);
                }
            }
        });
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!reLayout) {
            reLayout = true;
            setIncludeFontPadding(false);
            setGravity(Gravity.CENTER_VERTICAL);
            setLineSpacing(ITEM_HEIGHT, 0);
            int top = (int) ((ITEM_HEIGHT - getTextSize()) * 0.5f);
            setPadding(getPaddingLeft(), top, getPaddingRight(), -top);
            requestLayout();
            invalidate();
        }
    }

    public void addTextWatcher(TextWatcher textWatcher) {
        this.textWatcher = textWatcher;
    }

    public interface TextWatcher {
        void beforeTextChanged(CharSequence var1, int var2, int var3, int var4);

        void onTextChanged(CharSequence var1, int var2, int var3, int var4);

        void afterTextChanged(Editable var1);
    }
}
