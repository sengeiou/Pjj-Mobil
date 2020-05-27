package com.pjj.view.custom

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.widget.ImageView

/**
 * Create by xinheng on 2018/11/09。
 * describe：
 */
class CropImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ImageView(context, attrs, defStyleAttr) {
    private val mPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        mPaint.isAntiAlias = true
        mPaint.isDither = true
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.e("TAG", "Matrix=$imageMatrix, isIdentity=${imageMatrix.isIdentity}")
        mPaint.color = Color.RED
        val cx = measuredWidth / 2f
        val cy = measuredHeight / 2f
        val radius = Math.min(cx, cy)
        Log.e("TAG", "measuredWidth=$measuredWidth, measuredHeight=$measuredHeight, radius=$radius")
        canvas.drawCircle(cx, cy, radius, mPaint)
    }
}