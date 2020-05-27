package com.pjj.view.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.util.AttributeSet
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.module.SpeedDataBean
import com.pjj.utils.*

/**
 * Created by XinHeng on 2019/03/08.
 * describe：拼屏
 */
class SpeedViewGroup @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {
    var videoSizeText: String? = null
    var imageSizeText: String? = null
    /**
     * 点击标志
     * 在speedData之前调用
     */
    var childClickTag = false
    var textSize = resources.getDimension(R.dimen.sp_13)
    var speedData: SpeedDataBean? = null
        set(value) {
            field = value
            removeAllViews()
            value?.let { item ->
                val viewSizeBeanList = item.viewSizeBeanList
                viewSizeBeanList?.forEachIndexed { index, viewSizeBean ->
                    addView(getTextImageView(viewSizeBean.filePath, viewSizeBean.type).apply {
                        if (childClickTag) {
                            setOnClickListener {
                                onSpeedViewChildClickListener?.childClick(this, index, viewSizeBean.type)
                            }
                        }
                        lineBottom = (viewSizeBean.y + viewSizeBean.height) < speedData!!.proportionY
                        lineRight = (viewSizeBean.x + viewSizeBean.width) < speedData!!.proportionX
                        var textHead = "图片 "
                        background = when (viewSizeBean.type) {
                            2 -> {
                                text = "视频"
                                if (null == videoSizeText) {
                                    videoSizeText = "视频 ${viewSizeBean.width * 1080 / speedData!!.proportionX}*${viewSizeBean.height * 1800 / speedData!!.proportionY}px"
                                }
                                ColorDrawable(ViewUtils.getColor(R.color.color_f2f2f2))
                            }
                            3 -> {
                                text = "便民"
                                if (null == videoSizeText) {
                                    videoSizeText = "便民 ${viewSizeBean.width * 1080 / speedData!!.proportionX}*${viewSizeBean.height * 1800 / speedData!!.proportionY}px"
                                }
                                ColorDrawable(ViewUtils.getColor(R.color.color_e3e3e3))
                            }
                            else -> {
                                text = "图片"
                                if (speedData!!.proportionX == viewSizeBean.width) {
                                    if (null == videoSizeText) {
                                        videoSizeText = "大图 ${viewSizeBean.width * 1080 / speedData!!.proportionX}*${viewSizeBean.height * 1800 / speedData!!.proportionY}px"
                                        if (null != imageSizeText) {
                                            imageSizeText = "小图" + imageSizeText!!.substring(2, imageSizeText!!.length)
                                        }
                                    }
                                } else {
                                    if (null == imageSizeText) {
                                        if (null != videoSizeText && videoSizeText!!.contains("大")) {
                                            textHead = "小图 "
                                        }
                                        imageSizeText = "$textHead ${viewSizeBean.width * 1080 / speedData!!.proportionX}*${viewSizeBean.height * 1800 / speedData!!.proportionY}px"
                                    }
                                }
                                ColorDrawable(ViewUtils.getColor(R.color.color_e3e3e3))
                            }
                        }
                    })
                }
            }
        }

    init {
//        var s = "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":1,\"height\":1,\"type\":2},{\"x\":0,\"y\":1,\"width\":1,\"height\":1},{\"x\":1,\"y\":0,\"width\":1,\"height\":1},{\"x\":1,\"y\":1,\"width\":1,\"height\":1}],\"proportionX\":2,\"proportionY\":2,\"size\":4}"
//        var bean = JsonUtils.parse(s, SpeedDataBean::class.java)
//        Log.e("TAG", "bean: $bean ")
//        speedData = bean
    }

    @SuppressLint("DrawAllocation")
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (childCount > 0) {
            var widthProportion = measuredWidth * 1f / speedData!!.proportionX
            var heightProportion = measuredHeight * 1f / speedData!!.proportionY
            var viewSizeBeanList = speedData!!.viewSizeBeanList!!
            viewSizeBeanList.forEachIndexed { index, viewSizeBean ->
                var childAt = getChildAt(index)
                childAt.layoutParams = ViewGroup.LayoutParams((viewSizeBean.width * widthProportion).toInt(), (viewSizeBean.height * heightProportion).toInt())
            }
            measureChildren(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount > 0) {
            var widthProportion = measuredWidth * 1f / speedData!!.proportionX
            var heightProportion = measuredHeight * 1f / speedData!!.proportionY
            Log.e("TAG", "onLayout: widthProportion=$widthProportion heightProportion=$heightProportion")
            speedData!!.viewSizeBeanList!!.forEachIndexed { index, viewSizeBean ->
                var childAt = getChildAt(index)
                var left = viewSizeBean.x * widthProportion
                var top = viewSizeBean.y * heightProportion
                childAt.layout(left.toInt(), top.toInt(), (left + childAt.measuredWidth).toInt(), (top + childAt.measuredHeight).toInt())
            }
        }
    }

    private fun getTextImageView(path: String? = null, type: Int = 0): TextImageView {
        Log.e("TAG", "getTextImageView: path=$path")
        return TextImageView(context).apply {
            textSize = this@SpeedViewGroup.textSize
            if (!TextUtils.isEmpty(path)) {
                var with = Glide.with(this)
                this.fileType = type
                if (type == 2) {
                    BitmapUtils.loadFirstImageForVideo(with, PjjApplication.filePath + path, this)
                } else {
                    with.load(PjjApplication.filePath + path).into(this)
                }
            }
        }
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        ViewUtils.getDrawable(R.drawable.shape_b5b5b5_bg).apply {
            setBounds(0, 0, measuredWidth, measuredHeight)
        }.draw(canvas)
    }

    var onSpeedViewChildClickListener: OnSpeedViewChildClickListener? = null

    interface OnSpeedViewChildClickListener {
        /**
         * 子view点击
         * @param view
         * @param index 位置
         * @param fileType 文件类型
         */
        fun childClick(view: TextImageView, index: Int, fileType: Int)
    }
}