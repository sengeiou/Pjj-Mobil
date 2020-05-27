package com.pjj.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.pjj.view.custom.ClipImageView

class ImageDialog(context: Context) : FullWithNoTitleDialog(context, 0) {
    private var clipImageView: ClipImageView
    override fun getHeightRate(): Float {
        return 1f
    }

    private var path: String? = null

    init {
        setContentView(FrameLayout(context).apply {
            layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            background = ColorDrawable(Color.BLACK)
            addView(ClipImageView(context).apply {
                layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                clipImageView = this
                setOnClickListener {
                    cancel()
                }
                setOnLongClickListener {
                    onImageDialogListener?.saveImage(path)
                    true
                }
            })
        })
    }

    fun setImagePath(path: String, height: Int) {
//        val params = window.attributes
//        params.height = PjjApplication.application.screenHeight
//        window.attributes = params
        show()
        this.path = path
        clipImageView.post {
            val widthIv = clipImageView.measuredWidth
            val heightOld = clipImageView.measuredHeight
            //val lp = clipImageView.layoutParams
            Glide.with(context).asDrawable().load(path).into(object : DrawableImageViewTarget(clipImageView) {
                override fun setResource(resource: Drawable?) {
                    resource?.let {
                        val height = widthIv * it.intrinsicHeight / it.intrinsicWidth
                        if (heightOld > height) {
                            val top = (heightOld - height) / 2f
                            clipImageView.clipBorder = RectF(0f, top, widthIv.toFloat(), height + top)
                        } else {
                            val width = heightOld * it.intrinsicWidth / it.intrinsicHeight
                            //clipImageView.layoutParams = lp
                            val left = (widthIv - width) / 2f
                            clipImageView.clipBorder = RectF(left, 0f, width + left, heightOld.toFloat())
                        }
                    }
                    super.setResource(resource)
                    //reScrollLayout()
                }
            })
        }
    }

    var onImageDialogListener: OnImageDialogListener? = null

    interface OnImageDialogListener {
        fun saveImage(path: String?)
    }
}