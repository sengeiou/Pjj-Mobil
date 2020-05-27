package com.pjj.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.ScrollView
import com.pjj.utils.Log

class MyScrollView @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ScrollView(context, attrs, defStyleAttr) {
    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        myScrollY = t
        onMyScrollViewListener?.onScrollChange(this, l, t, oldl, oldt)
    }

    var onMyScrollViewListener: OnMyScrollViewListener? = null

    var allowDealWithEvent = 1 //0永不拦截 1 看情况 2 永远拦截
    var myScrollY = 0
    var interceptMove = false
    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_MOVE && interceptMove) {
            Log.e("TAG_Scroll", "onInterceptTouchEvent11: true ${getAction(ev)}")
            return true
        }
        return when (allowDealWithEvent) {
            0 -> false
            2 -> true
            else -> {
                val onInterceptTouchEvent = super.onInterceptTouchEvent(ev)
                Log.e("TAG_Scroll", "onInterceptTouchEvent: $onInterceptTouchEvent ${getAction(ev)}")
                onInterceptTouchEvent
            }
        }
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return when (allowDealWithEvent) {
            0 -> false
            2 -> {
                super.onTouchEvent(ev)
                true
            }
            else -> {
                val onTouchEvent = super.onTouchEvent(ev)
                Log.e("TAG_Scroll", "onTouchEvent: $onTouchEvent ${getAction(ev)}")
                onTouchEvent
            }
        }
    }

    private fun getAction(ev: MotionEvent): String {
        return MotionEvent.actionToString(ev.action)
    }

    interface OnMyScrollViewListener {
        fun onScrollChange(v: View, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int)
    }
}