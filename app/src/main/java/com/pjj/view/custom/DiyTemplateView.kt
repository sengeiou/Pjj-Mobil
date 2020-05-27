package com.pjj.view.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.pjj.R
import com.pjj.module.DiyDataBean
import com.pjj.utils.TextUtils
import kotlin.math.min

/**
 * Create by xinheng on 2018/11/09。
 * describe：diy模板
 */
class DiyTemplateView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : ViewGroup(context, attrs, defStyleAttr) {
    private var mDiyDataBean: DiyDataBean? = null
    /**
     * 当前点击的view
     */
    private var clickView: View? = null
    private val KEY_TAG = R.id.diy_template
    private lateinit var mOnClickListener: OnViewClickListener
    private var mChildClickListener = OnClickListener {
        clickView = it
        mDiyDataBean?.run {
            mOnClickListener.onClick(data[it.getTag(KEY_TAG).toString().toInt()], it)
        }
    }
    private var imageWidth = 0
    private var rate = 1f
    fun setRateWidth(width: Int) {
        imageWidth = width
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        measureChildren(widthMeasureSpec, heightMeasureSpec)
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (imageWidth > 0) {
            rate = measuredWidth * 1f / imageWidth
        }
        Log.e("TAG", "onMeasure rate=$rate width=$measuredWidth height=$measuredHeight")
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (childCount > 0) {
            var childAt: View
            var data = mDiyDataBean?.data!!
            var dataBean: DiyDataBean.DataBean
            var left: Int
            var top: Int
            (0 until childCount).forEach {
                childAt = getChildAt(it)
                dataBean = data[it]
                left = (dataBean.x.toInt() * rate).toInt()
                top = (dataBean.y.toInt() * rate).toInt()
                /*if (childAt is TextView) {
                } else {
                    childAt.layout(left, top, (left + dataBean.wide.toInt() * rate).toInt(), (top + dataBean.high.toInt() * rate).toInt())
                }*/
                Log.e("TAG", "left=$left top=$top")
                childAt.layout(left, top, (left + childAt.measuredWidth), (top + childAt.measuredHeight))
            }
        }
    }

    fun addViews(diyDataBean: DiyDataBean) {
        mDiyDataBean = diyDataBean
        if (childCount > 0) {
            removeAllViews()
        }
        diyDataBean.data?.run {
            for ((index, value) in this.withIndex()) {
                value?.let {
                    addView(getChildView(it).apply {
                        setTag(KEY_TAG, index)
                    })
                }
            }
        }
    }

    private fun getChildView(dataBean: DiyDataBean.DataBean): View {
        var width = (dataBean.wide.toInt() * rate).toInt()
        var layoutParams = LayoutParams(width, (dataBean.high.toInt() * rate).toInt())
        var view = when (dataBean.elementType) {
            "1" -> getImageView(dataBean)
            "2" -> {
                getTextView(dataBean)
            }
            else -> getCustomShapeView(dataBean)
        }
        view.layoutParams = layoutParams
        view.setOnClickListener(mChildClickListener)
        return view
    }

    private fun getTextView(dataBean: DiyDataBean.DataBean): View {
        return with(dataBean) {
            val parseColor = try {
                Color.parseColor(wordColour)
            } catch (e: Exception) {
                Color.BLACK
            }
            return MyTextView(context).apply {
                gravity = wordAlign
                val size = wordSize?.toFloat() ?: 10f
                //Log.e("TAG", "getTextView：size=$size")
                textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, size - 0.4f, resources.displayMetrics)
                color = parseColor
//                text = "请输入"
                var hint = dataBean.hint
                if (TextUtils.isEmpty(hint)) {
                    hint = "请输入"
                }
                setHintText(hint)
                //background = ColorDrawable(Color.RED)
                //background = ContextCompat.getDrawable(context, R.drawable.dotted_line_slide)
                //includeFontPadding = false
            }
        }
    }

    private fun getTextGravity(type: String?): Int {
        return when (type) {
            "2" -> Gravity.RIGHT or Gravity.CENTER_VERTICAL
            "0" -> Gravity.LEFT or Gravity.CENTER_VERTICAL
            else -> Gravity.CENTER
        }
    }


    private fun getImageView(dataBean: DiyDataBean.DataBean): View {
        return ImageView(context).apply {
            val drawableDraw: Drawable?
            when (dataBean.isCircle) {
                "0" -> drawableDraw = ContextCompat.getDrawable(context, R.drawable.dotted_line_slide)
                "1" -> {
                    if (dataBean.wide != dataBean.high) {
                        val min = min(dataBean.wide.toFloat(), dataBean.high.toFloat())
                        dataBean.wide = min.toString()
                        dataBean.high = dataBean.wide
                    }
                    drawableDraw = ContextCompat.getDrawable(context, R.drawable.dotted_line_slide_circle)
                }
                else -> {
                    drawableDraw = ContextCompat.getDrawable(context, R.drawable.dotted_line_slide_circle)
                }
            }
            setImageDrawable(drawableDraw)
        }
    }

    private fun getCustomShapeView(dataBean: DiyDataBean.DataBean): View {
        var customShapeView = CustomShapeView(context)
        customShapeView.layoutParams = layoutParams
        customShapeView.setShape(dataBean)
        return customShapeView
    }

    /**
     * 更新点击的view的ui
     */
    fun updateClickView(path: String) {
        clickView?.let {
            val tag = it.getTag(KEY_TAG)
            if (tag is Int && childCount > 0) {
                mDiyDataBean!!.data[tag].isHasSelect = true
            }
        }
        when (clickView) {
            is ImageView -> Glide.with(this).load(path).into(clickView as ImageView)
            is TextView -> {
                (clickView as TextView).text = path
                (clickView as TextView).background = ColorDrawable(Color.TRANSPARENT)
            }
            is MyTextView -> {
                (clickView as MyTextView).text = path
                //(clickView as MyTextView).background = ColorDrawable(Color.TRANSPARENT)
            }
            is CustomShapeView -> {
                Glide.with(this).asBitmap().load(path).into(object : CustomViewTarget<CustomShapeView, Bitmap>(clickView as CustomShapeView) {
                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        Log.e("TAG", "glide 加载失败")
                    }

                    override fun onResourceCleared(placeholder: Drawable?) {
                        //TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        //this.view.setBitmap(resource)
                    }
                })
            }
        }
    }

    fun setOnViewClickListener(l: OnViewClickListener) {
        this.mOnClickListener = l
    }

    fun hasAllSelect(): String? {
        mDiyDataBean?.data?.forEach {
            if (!it.isHasSelect) {
                var notice = it.notice
                if (TextUtils.isEmpty(notice)) {
                    notice = if (it.elementType == "1") "您还未上传图片" else "您还未输入相关内容"
                }
                return notice
            }
        }
        return null
    }

    fun getBitmap(): Bitmap {
        val bit = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bit)
        draw(canvas)
        return bit
    }

    interface OnViewClickListener {
        fun onClick(dataBean: DiyDataBean.DataBean, view: View)
    }
}