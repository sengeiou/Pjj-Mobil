package com.pjj.view.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.ImageView
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.pjj.BuildConfig
import com.pjj.R
import com.pjj.present.BasePresent
import com.pjj.utils.Log
import com.pjj.utils.SharedUtils
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_welcome.*

class WelcomeActivity : BaseActivity<BasePresent<*>>() {
    //private val images = arrayOf(R.mipmap.welcome_r1, R.mipmap.welcome_r2, R.mipmap.welcome_r3, R.mipmap.welcome_r4)
    private val images = arrayOf(R.mipmap.we1, R.mipmap.we2, R.mipmap.we3, R.mipmap.we4)

    //private val topColors = arrayOf(Color.parseColor("#EEE2ED"), Color.parseColor("#FCEEEF"), Color.parseColor("#D0DDFD"), Color.parseColor("#D0E3FD"))
    //private val bottomColors = arrayOf(Color.parseColor("#D1B5CF"), Color.parseColor("#E0BABD"), Color.parseColor("#8C98E4"), Color.parseColor("#84AFE0"))
    private lateinit var views: ArrayList<View>
    private var mDotDis: Int = ViewUtils.getDp(R.dimen.dp_20)//小圆点间的距离
    private var colorStart = Color.parseColor("#000066")
    private var screenHeight = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        views = ArrayList(images.size)
        var displayMetrics = resources.displayMetrics
        //Log.e("TAG", ": ${displayMetrics.toString()}")
        var fl = displayMetrics.widthPixels / 750f
        var height: Int = (fl * 1122).toInt()
        var height1: Int = (fl * 76).toInt()
        screenHeight = displayMetrics.heightPixels
        images.forEachIndexed { index, it ->
            views.add(createChildView(it, colorStart, Color.BLACK, height, height1))
        }
        var layoutParams1 = rl_point.layoutParams as RelativeLayout.LayoutParams
        layoutParams1.bottomMargin = height1 + ViewUtils.getDp(R.dimen.dp_5)
        rl_point.layoutParams = layoutParams1

        var layoutParams2 = bt_start.layoutParams as RelativeLayout.LayoutParams
        layoutParams2.bottomMargin = height1 + ViewUtils.getDp(R.dimen.dp_34)
        bt_start.layoutParams = layoutParams2

        vp_welcome.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                // 滚动过程中
                // 红色小圆点的移动距离=移动百分比*两个圆点的间距
                // 更新小红点距离
                //Log.e("TAG", "onPageScrolled: position=$position ,positionOffset=$positionOffset ,positionOffsetPixels=$positionOffsetPixels")
                //Log.e("TAG", "onPageScrolled: mDotDis=$mDotDis")
                var dis = (mDotDis * positionOffset) + position * mDotDis
                //Log.e("TAG", "onPageScrolled: dis=$dis")
                var layoutParams = view_move.layoutParams as RelativeLayout.LayoutParams
                layoutParams.leftMargin = dis.toInt()
                view_move.layoutParams = layoutParams
            }

            override fun onPageSelected(position: Int) {
                if (position == views.size - 1) {
                    bt_start.visibility = View.VISIBLE
                } else {
                    bt_start.visibility = View.GONE
                }
            }

        })
        vp_welcome.adapter = WelcomeAdapter(views)
        tv_jump.setOnClickListener(onClick)
        bt_start.setOnClickListener(onClick)
    }

    override fun fitSystemWindow(): Boolean {
        return false
    }

    override fun allowBackFinish(): Boolean {
        return false
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_jump, R.id.bt_start -> {
                SharedUtils.saveForXml(SharedUtils.USE_WELCOME, BuildConfig.VERSION_NAME)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    class WelcomeAdapter(private var list: MutableList<View>) : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            return list.size
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(list[position])
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            var view = list[position]
            container.addView(view)
            return view
        }
    }

    //private var colorStart=Color.parseColor("#000066")
    private fun createChildView(resource: Int, colorTop: Int, colorBottom: Int, height: Int, heightBottom: Int): View {
        return LayoutInflater.from(this).inflate(R.layout.layout_welcome_item, null).apply {
            background = GradientDrawable().apply {
                gradientType = GradientDrawable.RADIAL_GRADIENT
                colors = intArrayOf(colorStart, Color.BLACK)
                gradientRadius = screenHeight.toFloat()
            }
            var imageView = findViewById<ImageView>(R.id.iv_welcome)
            imageView.setImageResource(resource)
            var params = imageView.layoutParams
            params.height = height
            imageView.layoutParams = params
            var viewBotom = findViewById<View>(R.id.iv_bottom)
            var layoutParams = viewBotom.layoutParams
            layoutParams.height = heightBottom
            viewBotom.layoutParams = layoutParams
            //findViewById<View>(R.id.view_top).setBackgroundColor(colorTop)
            //findViewById<View>(R.id.view_bottom).setBackgroundColor(colorBottom)
        }
    }
}
