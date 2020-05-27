package com.pjj.view.custom

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RelativeLayout
import com.pjj.BuildConfig
import com.pjj.R
import com.pjj.utils.Log
import com.pjj.utils.SharedUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.dialog.FullWithNoTitleDialog
import kotlinx.android.synthetic.main.layout_guide_item.*

/**
 * Created by XinHeng on 2019/01/05.
 * describe：新手指导
 */
class GuideViewDialog(context: Context, themeResId: Int = 0) : FullWithNoTitleDialog(context, themeResId) {
    private var stub = 0
    override fun getDialogGravity(): Int {
        return Gravity.TOP
    }

    private val nextView = ImageView(context).apply {
        layoutParams = RelativeLayout.LayoutParams(ViewUtils.getDp(R.dimen.dp_168), ViewUtils.getDp(R.dimen.dp_48))
        setImageResource(R.mipmap.guide_next)
    }

    override fun isCleanBg(): Boolean {
        return true
    }

    override fun getWindowBgDrawable(): Drawable {
        return ColorDrawable(Color.TRANSPARENT)
    }

    override fun getHeightRate(): Float {
        return 1f
    }

    private lateinit var rlBottom: View
    private lateinit var rBPress: View
    private var rlBottomHeight = 0
    private var parent: LinearLayout
    private var empty: View? = null
    private var onClickListener = View.OnClickListener {
        if (it.id == R.id.iv_jump) {
            SharedUtils.saveForXml(SharedUtils.NEW_GUIDE, BuildConfig.VERSION_NAME)
            cancel()
            return@OnClickListener
        }
        ++stub
        if (stub > 5) {
            SharedUtils.saveForXml(SharedUtils.NEW_GUIDE, BuildConfig.VERSION_NAME)
            cancel()
        } else {
            start()
        }
    }
    var parseColor = Color.parseColor("#B3000000")

    //BitmapDrawable(res,BitmapBlurHelper.doBlur(context,bitmap,25))
    /*private var gb = BitmapDrawable(context.resources, BitmapBlurHelper.fastblur(Bitmap.createBitmap(10, 10, Bitmap.Config.ARGB_8888).apply {
        var canvas = Canvas(this)
        canvas.drawColor(ViewUtils.getColor(R.color.color_000000_60))
    }, 25))*/
    init {
        if (Build.VERSION.SDK_INT >= 28) {//9.0
            var lp = window.attributes
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            //设置页面延伸到刘海区显示
            window.attributes = lp
        }
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
            //设置页面全屏显示
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(R.layout.layout_guide_item)
        parent = findViewById(R.id.parent)
        initMainDrawableTop()
        //parent.setOnClickListener(onClickListener)
        iv_jump.setOnClickListener(onClickListener)
        iv_first_next.setOnClickListener(onClickListener)
        parent.setOnClickListener(onClickListener)
        first_empty.background = ColorDrawable(parseColor)
        last_empty.background = ColorDrawable(parseColor)
        rl_text.background = ColorDrawable(parseColor)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    fun showDialog(height: Int) {
        var params = window.attributes
        params.height = height
        window.attributes = params
        show()
        start()
    }

    private fun start() {
        when (stub) {
            0 -> stub0()
            5 -> {
                first_empty.removeAllViews()
                last_empty.removeAllViews()
                stub5()
            }
            else -> {
                first_empty.removeAllViews()
                last_empty.removeAllViews()
                setLastEmptyHeight(0, 1f)
                onGuideListener?.guide(stub)
            }
        }

    }

    /**
     * 设置第几部
     * @param index 步骤 0开始
     * @param emptyHeight 透明view高度
     * @param emptyTop  透明view距手机顶部的高度
     */
    fun setStub(index: Int, emptyHeight: Int, emptyTop: Int) {
        when (index) {
            1 -> stub1(emptyHeight, emptyTop)
            2 -> stub2(emptyHeight, emptyTop)
            3 -> stub3(emptyHeight, emptyTop)
            4 -> stub4(emptyHeight, emptyTop)
        }
    }

    private fun stub4(emptyHeight: Int, emptyTop: Int) {
        var layoutParams = iv_guide.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = ViewUtils.getDp(R.dimen.dp_163)
        layoutParams.height = ViewUtils.getDp(R.dimen.dp_115)
        iv_guide.layoutParams = layoutParams
        iv_guide.setImageResource(R.mipmap.guide_fifth)
        iv_arrow.setImageResource(R.mipmap.guide_arrow)
        var params = iv_arrow.layoutParams as RelativeLayout.LayoutParams
        params.removeRule(RelativeLayout.ALIGN_TOP)
        params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.iv_guide)
        iv_arrow.layoutParams = params
        first_empty.visibility = View.VISIBLE
        last_empty.visibility = View.VISIBLE
        setFirstEmptyHeight(emptyTop - layoutParams.topMargin - layoutParams.bottomMargin - layoutParams.height)
        addNext(first_empty, ViewUtils.getDp(R.dimen.dp_78), ViewUtils.getDp(R.dimen.dp_123))
        addEmptyView(2, emptyHeight)
    }

    private fun stub0() {
        var layoutParams = iv_guide.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = ViewUtils.getDp(R.dimen.dp_168)
        layoutParams.height = ViewUtils.getDp(R.dimen.dp_120)
        iv_guide.layoutParams = layoutParams
        iv_guide.setImageResource(R.mipmap.guide_first)
        iv_arrow.setImageResource(R.mipmap.guide_arrow)
        //mainMenu.addChildMenu("便民\n信息发布", "随机便民\n信息发布", "DIY\n信息发布", "随机\n信息发布", "拼屏\n信息发布")
        mainMenu.addChildMenu("电梯\n传媒发布", "广告\n传媒发布", "自营\n传媒发布")
        rlBottom = findViewById(R.id.rl_bottom)
        rBPress = findViewById(R.id.rbPress)
        mainMenu.post {
            var height = rl_bottom.bottom - mainMenu.top
            var params = last_empty.layoutParams as LinearLayout.LayoutParams
            params.height = height
            last_empty.layoutParams = params
            setFirstEmptyHeight(0, 1f)
            rlBottomHeight = rlBottom.measuredHeight
        }
        //setFirstEmptyHeight(0, 1f)
    }

    private fun stub1(emptyHeight: Int, emptyTop: Int) {
        var layoutParams = iv_guide.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = ViewUtils.getDp(R.dimen.dp_168)
        layoutParams.height = ViewUtils.getDp(R.dimen.dp_101)
        iv_guide.layoutParams = layoutParams
        iv_guide.setImageResource(R.mipmap.guide_second)
        iv_arrow.setImageResource(R.mipmap.guide_arrow_up)
        var params = iv_arrow.layoutParams as RelativeLayout.LayoutParams
        params.addRule(RelativeLayout.ALIGN_TOP, R.id.iv_guide)
        params.addRule(RelativeLayout.ALIGN_BOTTOM, -1)
        last_empty.visibility = View.VISIBLE
        first_empty.visibility = View.GONE
        addEmptyView(1, emptyHeight, -emptyTop)
        addNext(last_empty, ViewUtils.getDp(R.dimen.dp_15), ViewUtils.getDp(R.dimen.dp_37))
    }

    private fun stub2(emptyHeight: Int, emptyTop: Int) {
        var layoutParams = iv_guide.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = ViewUtils.getDp(R.dimen.dp_163)
        layoutParams.height = ViewUtils.getDp(R.dimen.dp_115)
        var dp10 = ViewUtils.getDp(R.dimen.dp_10)
        iv_guide.setImageResource(R.mipmap.guide_third)
        iv_guide.layoutParams = layoutParams
        iv_arrow.setImageResource(R.mipmap.guide_arrow_up)
        var params = iv_arrow.layoutParams as RelativeLayout.LayoutParams
        params.addRule(RelativeLayout.ALIGN_TOP, R.id.iv_guide)
        first_empty.visibility = View.VISIBLE
        last_empty.visibility = View.VISIBLE
        setFirstEmptyHeight(emptyTop - dp10 / 2)
        addEmptyView(1, emptyHeight + dp10)
        addNext(last_empty, ViewUtils.getDp(R.dimen.dp_18), ViewUtils.getDp(R.dimen.dp_37))
    }

    private fun stub3(emptyHeight: Int, emptyTop: Int) {
        var layoutParams = iv_guide.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = ViewUtils.getDp(R.dimen.dp_163)
        layoutParams.height = ViewUtils.getDp(R.dimen.dp_115)
        iv_guide.layoutParams = layoutParams
        iv_guide.setImageResource(R.mipmap.guide_forth)
        iv_arrow.setImageResource(R.mipmap.guide_arrow)
        var params = iv_arrow.layoutParams as RelativeLayout.LayoutParams
        params.addRule(RelativeLayout.ALIGN_TOP, -1)
        params.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.iv_guide)
        first_empty.visibility = View.VISIBLE
        last_empty.visibility = View.VISIBLE
        setFirstEmptyHeight(emptyTop - layoutParams.topMargin - layoutParams.bottomMargin - layoutParams.height)
        addEmptyView(2, emptyHeight)
        addNext(last_empty, ViewUtils.getDp(R.dimen.dp_29), ViewUtils.getDp(R.dimen.dp_37))
    }

    private fun stub5() {
        var layoutParams = iv_guide.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = ViewUtils.getDp(R.dimen.dp_163)
        layoutParams.height = ViewUtils.getDp(R.dimen.dp_96)
        layoutParams.marginEnd = ViewUtils.getDp(R.dimen.dp_67)
        iv_guide.layoutParams = layoutParams
        iv_guide.setImageResource(R.mipmap.guide_sixth)
        iv_arrow.setImageResource(R.mipmap.guide_arrow)
        var params = iv_arrow.layoutParams as RelativeLayout.LayoutParams
        params.addRule(RelativeLayout.ALIGN_TOP, R.id.iv_guide)
        params.addRule(RelativeLayout.ALIGN_BOTTOM, -1)
        params.topMargin = ViewUtils.getDp(R.dimen.dp_56)
        params.height = ViewUtils.getDp(R.dimen.dp_80)
        iv_arrow.layoutParams = params
        first_empty.visibility = View.VISIBLE
        last_empty.visibility = View.VISIBLE
        var params1 = nextView.layoutParams as RelativeLayout.LayoutParams
        params1.rightMargin = ViewUtils.getDp(R.dimen.dp_35)
        params1.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        params1.bottomMargin = ViewUtils.getDp(R.dimen.dp_25)
        params1.topMargin = 0
        nextView.layoutParams = params1
        first_empty.addView(nextView)
        last_empty.addView(rlBottom)
        last_empty.addView(rBPress)
        val ivArrow = iv_arrow
        val ivGuide = iv_guide
        rl_text.removeAllViews()
        empty?.let {
            parent.removeView(it)
        }
        parent.removeView(rl_text)
        last_empty.addView(ivArrow)
        last_empty.addView(ivGuide)
        setLastEmptyHeight(rlBottomHeight + ViewUtils.getDp(R.dimen.dp_146))
        setFirstEmptyHeight(0, 1f)
    }

    private fun addEmptyView(index: Int, height: Int, marTop: Int = 0, tagBg: Boolean = true) {
        empty?.let {
            parent.removeView(it)
        }
        parent.addView(View(context).apply {
            empty = this
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height).apply {
                topMargin = marTop
            }
            background = if (tagBg) {
                ViewUtils.getDrawable(R.mipmap.guide_light_bg)
            } else {
                ColorDrawable(Color.TRANSPARENT)
            }
            post {
                Log.e("TAG", "post :${empty!!.height} ${empty!!.width} ${empty!!.top}")
            }
        }, index)
    }

    private fun setFirstEmptyHeight(height: Int, weight: Float = 0f) {
        var layoutParams = first_empty.layoutParams as LinearLayout.LayoutParams
        layoutParams.height = height
        layoutParams.weight = weight
        first_empty.layoutParams = layoutParams
        first_empty.post {
            Log.e("TAG", "setFirstEmptyHeight : height=${first_empty.height}")
            Log.e("TAG", "setFirstEmptyHeight : last_empty height=${last_empty.height}")
        }
    }

    private fun setLastEmptyHeight(height: Int, weight: Float = 0f) {
        var layoutParams = last_empty.layoutParams as LinearLayout.LayoutParams
        layoutParams.height = height
        layoutParams.weight = weight
        last_empty.layoutParams = layoutParams
        last_empty.post {
            Log.e("TAG", "setLastHeight : height=${last_empty.height}")
        }
    }

    private fun addNext(parent: RelativeLayout, topMargin: Int, rightMargin: Int) {
        var params = nextView.layoutParams as RelativeLayout.LayoutParams
        params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT)
        params.rightMargin = rightMargin
        params.topMargin = topMargin
        parent.addView(nextView)
    }

    private fun initMainDrawableTop() {
        setMainDrawableTop(rb_home, ViewUtils.getDrawable(R.drawable.selector_main_home_btn_home).apply {
            setBounds(0, 0, ViewUtils.getDp(R.dimen.dp_23), ViewUtils.getDp(R.dimen.dp_20))
        }, R.dimen.dp_3)
        setMainDrawableTop(rb_works, ViewUtils.getDrawable(R.drawable.selector_etemplate_btn_home).apply {
            setBounds(0, 0, ViewUtils.getDp(R.dimen.dp_18), ViewUtils.getDp(R.dimen.dp_19))
        }, R.dimen.dp_4)
        setMainDrawableTop(rb_order, ViewUtils.getDrawable(R.drawable.selector_main_order_btn_home).apply {
            setBounds(0, 0, ViewUtils.getDp(R.dimen.dp_20), ViewUtils.getDp(R.dimen.dp_17))
        }, R.dimen.dp_4)
        setMainDrawableTop(rb_mine, ViewUtils.getDrawable(R.drawable.selector_mine_btn).apply {
            setBounds(0, 0, ViewUtils.getDp(R.dimen.dp_16), ViewUtils.getDp(R.dimen.dp_20))
        }, R.dimen.dp_3)
    }

    private fun setMainDrawableTop(rb: RadioButton, drawable: Drawable, id: Int) {
        rb.setCompoundDrawables(null, drawable, null, null)
        rb.compoundDrawablePadding = ViewUtils.getDp(id)
    }

    var onGuideListener: OnGuideListener? = null

    interface OnGuideListener {
        fun guide(index: Int)
    }
}