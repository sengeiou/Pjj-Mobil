package com.pjj.view.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.pjj.PjjApplication
import com.pjj.utils.Log

/**
 * Created by XinHeng on 2018/11/24.
 * describe：底部 全屏 白色
 */
open class FullWithNoTitleDialog @JvmOverloads constructor(context: Context, themeResId: Int = 0) : Dialog(context, themeResId) {
    init {
        window.requestFeature(Window.FEATURE_NO_TITLE)
        window.setGravity(getDialogGravity())
        //Log.e("TAG","FullWithNoTitleDialog-init")
        setCancelable(true)
        setCanceledOnTouchOutside(true)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setFullScreen()
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        setFullScreen()
    }

    private fun setFullScreen() {
        var attributes = window.attributes
        if (isFullScreen()) {
            val displayMetrics = context.resources.displayMetrics
            attributes.width = displayMetrics.widthPixels
            val heightRate = getHeightRate()
            val height: Int
            height = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                PjjApplication.application.screenHeight
//                WindowManager.LayoutParams.MATCH_PARENT
            } else {
                displayMetrics.heightPixels
            }
            when {
                heightRate > 0 -> attributes.height = (heightRate * height).toInt()
                getOtherHeightSize() > 0 -> attributes.height = displayMetrics.heightPixels - getOtherHeightSize()
                else -> attributes.height = WindowManager.LayoutParams.WRAP_CONTENT
            }
        }
        if (isCleanBg()) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND or WindowManager.LayoutParams.FLAG_DIM_BEHIND)
        }
        Log.e("TAG", "dialog height=${attributes.height}")
        window.attributes = attributes
        window.decorView.setPadding(0, 0, 0, 0)
        window.setBackgroundDrawable(getWindowBgDrawable())
    }

    protected open fun getDialogGravity(): Int {
        return Gravity.BOTTOM
    }

    protected open fun getHeightRate(): Float {
        return -1f
    }

    protected open fun getOtherHeightSize(): Int {
        return 0
    }

    protected open fun isFullScreen(): Boolean {
        return true
    }

    protected open fun getWindowBgDrawable(): Drawable {
        return ColorDrawable(Color.WHITE)
    }

    protected open fun isCleanBg(): Boolean {
        return false
    }

    protected open var onClick = View.OnClickListener {
        onClick(it)
    }

    protected open fun onClick(view: View) {

    }
}