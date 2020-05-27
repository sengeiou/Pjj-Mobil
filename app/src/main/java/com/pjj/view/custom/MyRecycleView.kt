package com.pjj.view.custom

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import com.pjj.utils.Log

class MyRecycleView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    var canScroll = true
        set(value) {
            field = value
            moveStatue = !value
        }
    private var moveStatue = false

    override fun onTouchEvent(e: MotionEvent): Boolean {
        when (e.action) {
            MotionEvent.ACTION_DOWN -> {
                if (moveStatue) {
                    return false
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (!canScroll) {
                    return false
                }
            }
        }
        val onTouchEvent = super.onTouchEvent(e)
        Log.e("TAG_MyRecycleView", "onTouchEvent: $onTouchEvent ${MotionEvent.actionToString(e.action)}")
        return onTouchEvent
    }
}