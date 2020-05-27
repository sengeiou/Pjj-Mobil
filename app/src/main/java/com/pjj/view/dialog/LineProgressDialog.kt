package com.pjj.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.widget.LinearLayout
import com.pjj.R
import com.pjj.utils.ViewUtils
import com.pjj.view.custom.LineProgress

/**
 * Created by XinHeng on 2019/01/14.
 * describe：进度条
 */
class LineProgressDialog(context: Context, themeResId: Int = 0) : FullWithNoTitleDialog(context, themeResId) {
    private var lineProgress: LineProgress
    override fun isFullScreen(): Boolean {
        return false
    }

    override fun getDialogGravity(): Int {
        return Gravity.CENTER
    }

    override fun getWindowBgDrawable(): Drawable {
        return ColorDrawable(Color.TRANSPARENT)
    }

    init {
        setContentView(LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            addView(LineProgress(context).apply {
                lineProgress = this
                layoutParams = LinearLayout.LayoutParams(ViewUtils.getDp(R.dimen.dp_283), LinearLayout.LayoutParams.WRAP_CONTENT).apply {
                    gravity = Gravity.CENTER_HORIZONTAL
                }
            })
        })
        setCanceledOnTouchOutside(false)
        setCancelable(false)
    }

    fun setProgress(progressReal: Float) {
        var progress = progressReal
        if (progress < 0) {
            progress = 0f
        }
        if (progress > 1) {
            progress = 1f
        }
        lineProgress.progress = progress
    }

    var onProgressListener: OnProgressListener? = null

    interface OnProgressListener {
        fun complete()
    }
}