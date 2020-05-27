package com.pjj.view.dialog

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.pjj.R
import com.pjj.utils.ViewUtils

/**
 * Created by XinHeng on 2018/12/06.
 * describe：等待弹窗
 */
class WaiteDialog(context: Context, themeResId: Int = 0) : FullWithNoTitleDialog(context, themeResId) {
    private var handlerMain: Handler
    private val DELAY_TIME = 60 * 2000L
    private var runnable = Runnable {
        if (!(context as Activity).isFinishing && isShowing) {
            dismiss()
            //TODO 接口网络停止
            onWaiteListener?.timeOutCancel()
        }
    }

    init {
        /*var dp40 = ViewUtils.getDp(R.dimen.dp_60)
        var ll = LinearLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT).apply {
                gravity = Gravity.CENTER
            }
            background = ColorDrawable(Color.TRANSPARENT)
        }
        var progressBar = ProgressBar(context).apply {
            layoutParams = LinearLayout.LayoutParams(dp40, dp40)

        }
        ll.addView(progressBar)*/
        handlerMain = Handler(Looper.getMainLooper())
        setContentView(R.layout.waite_dialog_item)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    override fun isFullScreen(): Boolean {
        return false
    }
    override fun getDialogGravity(): Int {
        return Gravity.CENTER
    }

    override fun show() {
        super.show()
        handlerMain.postDelayed(runnable, DELAY_TIME)
    }

    override fun cancel() {
        onWaiteListener?.initiativeCancel()
    }

    override fun dismiss() {
        super.dismiss()
        handlerMain.removeCallbacks(runnable)
    }

    var onWaiteListener: OnWaiteListener? = null

    interface OnWaiteListener {
        fun timeOutCancel()
        fun initiativeCancel()
    }
}