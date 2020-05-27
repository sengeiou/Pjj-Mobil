package com.pjj.view.dialog

import android.content.Context
import android.view.Gravity
import android.view.View
import com.pjj.R
import kotlinx.android.synthetic.main.layout_update_app_dialog.*

/**
 * Created by XinHeng on 2019/01/04.
 * describe：升级
 */
class UpdateAppDialog(context: Context, themeResId: Int = 0) : FullWithNoTitleDialog(context, themeResId) {
    private lateinit var updateUrl: String
    private var onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.tv_upload -> {
                onUpdateAppListener?.downLoadApp(updateUrl)
            }
            R.id.tv_close -> {
                dismiss()
            }
        }
    }

    override fun getDialogGravity(): Int {
        return Gravity.CENTER
    }

    override fun isFullScreen(): Boolean {
        return false
    }
    init {
        setContentView(R.layout.layout_update_app_dialog)
        tv_upload.setOnClickListener(onClickListener)
        tv_close.setOnClickListener(onClickListener)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
    }

    fun setUpdateText(url: String, msg: String, updateTag: Boolean = false) {
        updateUrl = url
        if (updateTag) {
            tv_close.visibility = View.GONE
        } else {
            tv_close.visibility = View.VISIBLE
        }
        tv_update_text.text = msg
        if (!isShowing) {
            show()
        }
    }

    var onUpdateAppListener: OnUpdateAppListener? = null

    interface OnUpdateAppListener {
        fun downLoadApp(updateUrl:String)
    }
}