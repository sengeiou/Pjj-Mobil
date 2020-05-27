package com.pjj.view.custom

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.support.v4.content.res.ResourcesCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.pjj.R
import com.pjj.utils.ViewUtils

/**
 * Created by XinHeng on 2018/12/13.
 * describe：主页菜单
 */
class MainMenuView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : RelativeLayout(context, attrs, defStyleAttr) {
    private var insidePaddingLeft: Int = ViewUtils.getDp(R.dimen.dp_31)
    private var insidePaddingRight: Int = ViewUtils.getDp(R.dimen.dp_31)
    private var insidePaddingTop: Int = ViewUtils.getDp(R.dimen.dp_31)
    private var dp48 = ViewUtils.getDp(R.dimen.dp_64)//62
    private var sp10 = ViewUtils.getFDp(R.dimen.sp_12)
    private var r = dp48 / 2
    private var A = ViewUtils.getFDp(R.dimen.dp_210).toDouble()
    private var B = ViewUtils.getFDp(R.dimen.dp_95).toDouble()
    private var cx = 0.toDouble()
    private var cy = 0.toDouble()
    /**
     * 菜单显示的半径
     */
    private var mRadius = 0.toDouble()

    init {
        //setWillNotDraw(false)
        setBackground()
    }

    private fun setBackground() {
        background = object : Drawable() {
            override fun draw(canvas: Canvas) {
                var background = ViewUtils.getDrawable(R.mipmap.main_menu_bg).apply {
                    setBounds(insidePaddingLeft, insidePaddingTop, measuredWidth - insidePaddingRight, measuredHeight)
                }
                background.draw(canvas)
            }

            override fun setAlpha(alpha: Int) {
            }

            override fun getOpacity(): Int {
                return PixelFormat.UNKNOWN
            }

            override fun setColorFilter(colorFilter: ColorFilter?) {
            }

        }
    }

    private fun getPaint(): Paint {
        return Paint().apply {
            isDither = true
            isAntiAlias = true
        }
    }

    fun addChildMenu(vararg s: String) {
        removeAllViews()
        s.forEachIndexed { index, s ->
            addView(createChildView(s).apply {
                tag = index
                var params = layoutParams as LayoutParams
                when (index) {
                    0 -> {
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                        params.marginStart = ViewUtils.getDp(R.dimen.dp_20)
                        params.bottomMargin = ViewUtils.getDp(R.dimen.dp_35)
                    }
                    1 -> {
                        /*params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
                        params.marginStart = ViewUtils.getDp(R.dimen.dp_48)
                        params.bottomMargin = ViewUtils.getDp(R.dimen.dp_64)*/
                        params.addRule(RelativeLayout.ALIGN_PARENT_TOP)
                        params.addRule(RelativeLayout.CENTER_HORIZONTAL)
                    }
                    2 -> {
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                        params.marginEnd = ViewUtils.getDp(R.dimen.dp_20)
                        params.bottomMargin = ViewUtils.getDp(R.dimen.dp_35)
                    }
                    /*3 -> {
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                        params.marginEnd = ViewUtils.getDp(R.dimen.dp_48)
                        params.bottomMargin = ViewUtils.getDp(R.dimen.dp_64)
                    }
                    4 -> {
                        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
                        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
                    }*/
                }
                layoutParams = params
            })
        }
    }


    private fun createChildView(s: String): TextView {
        /*return ViewUtils.createTextView(context, s).apply {
            layoutParams = RelativeLayout.LayoutParams(dp48, dp48)
            background = ViewUtils.getDrawable(R.mipmap.main_menu)
            setTextColor(Color.WHITE)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, sp10)
            typeface = Typeface.defaultFromStyle(Typeface.BOLD)
            setOnClickListener(onChildClickListener)
        }*/
        return (LayoutInflater.from(context).inflate(R.layout.mune_child_item, this, false) as TextView).apply {
            layoutParams = LayoutParams(dp48, dp48)
            background = if (s.contains("自营")) {
                ViewUtils.getDrawable(R.mipmap.un_ziying)
            } else {
                ViewUtils.getDrawable(R.mipmap.main_menu)
            }
            text = s
            typeface = Typeface.createFromAsset(context.assets, "Source_Han_Sans_CN_Bold_Bold.ttf")
            setLineSpacing(3f, 1f)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, sp10)
            setOnClickListener(onChildClickListener)
            //typeface = Typeface.createFromAsset(context.assets, "IMPACT.TTF")
        }
    }

    var onItemClickListener: OnItemClickListener? = null
    private var onChildClickListener = View.OnClickListener {
        var position = it.tag as Int
        onItemClickListener?.itemClick(position)
    }

    interface OnItemClickListener {
        fun itemClick(index: Int)
    }
}