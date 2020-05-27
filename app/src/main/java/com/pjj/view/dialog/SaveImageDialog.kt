package com.pjj.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.intent.RetrofitService
import com.pjj.utils.FileUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.custom.ClipImageView
import java.io.File.separator
import android.os.Environment.DIRECTORY_DCIM
import android.os.Environment.getExternalStorageDirectory
import android.os.Environment
import java.io.File


class SaveImageDialog(context: Context) : FullWithNoTitleDialog(context, 0) {
    private lateinit var tvCancel: TextView
    private lateinit var tvSave: TextView

    init {
        setContentView(LinearLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            background = getWindowBgDrawable()
            orientation = LinearLayout.VERTICAL
            addView(TextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_40))
                tvSave = this
                setOnClickListener {
                    onDownloadListener?.showWaiteNotice()
                    //系统相册目录
                    val galleryPath = Environment.getExternalStorageDirectory().toString() + separator + DIRECTORY_DCIM + separator + "Camera" + separator
                    RetrofitService.getInstance().downloadFile(path, galleryPath, onDownloadListener)
                }
                setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_16))
                text = "保存图片"
                gravity = Gravity.CENTER
                background = ColorDrawable(Color.WHITE)
                setTextColor(ViewUtils.getColor(R.color.color_333333))
            })
            addView(View(context).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_10))
            })
            addView(TextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_40))
                tvCancel = this
                setOnClickListener {
                    cancel()
                }
                setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_16))
                text = "取消"
                gravity = Gravity.CENTER
                background = ColorDrawable(Color.WHITE)
                setTextColor(ViewUtils.getColor(R.color.color_333333))
            })
        })
    }

    override fun getWindowBgDrawable(): Drawable {
        return ColorDrawable(Color.TRANSPARENT)
    }

    var path: String = ""

    var onDownloadListener: OnSaveImageDialogListener? = null

    interface OnSaveImageDialogListener : FileUtils.OnDownloadListener {
        fun showWaiteNotice()
    }
}