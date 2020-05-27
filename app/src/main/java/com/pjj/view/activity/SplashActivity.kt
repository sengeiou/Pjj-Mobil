package com.pjj.view.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pjj.BuildConfig
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.present.BasePresent
import com.pjj.receiver.JPushReceiver
import com.pjj.utils.Log
import com.pjj.utils.SharedUtils
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_splash.*
import java.lang.ref.WeakReference

class SplashActivity : BaseActivity<BasePresent<*>>() {
    private lateinit var tv_jump: TextView
    private lateinit var iv_splash: ImageView
    private var ivStartAdClick = false
    private lateinit var mHandler: MyHandler
    private val images = arrayOf(R.mipmap.we1, R.mipmap.we2, R.mipmap.we3, R.mipmap.we4)
    private lateinit var views: ArrayList<View>
    private var mDotDis: Int = ViewUtils.getDp(R.dimen.dp_20)//小圆点间的距离
    private var colorStart = Color.parseColor("#000066")
    private var screenHeight = 0

    private class MyHandler(activity: SplashActivity) : Handler() {
        private val weakReference = WeakReference<SplashActivity>(activity)
        private var count: Int = 3
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                1 -> {
                    weakReference.get()?.let {
                        it.iv_splash.visibility = View.GONE
                        it.mHandler.sendEmptyMessage(0)
                    }
                }
                0 -> {
                    weakReference.get()?.let {
                        it.tv_jump.text = "跳过($count)"
                    }
                    --count
                    if (count < 0) {
                        this.removeMessages(0)
                        weakReference.get()?.let {
                            it.autoJump()
                        }
                        return
                    }
                    this.sendEmptyMessageDelayed(0, 1000)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
            return
        }
        val stringExtra = intent.getStringExtra(JPushReceiver.KEY_TYPE)
        stringExtra?.let {
            startActivity(Intent(this, TestActivity::class.java).putExtra(JPushReceiver.KEY_TYPE, it))
            finish()
        }
        setContentView(R.layout.activity_splash)
        tv_jump = findViewById(R.id.tv_jump)
        iv_splash = findViewById(R.id.iv_splash)
        iv_start_up.setOnClickListener(onClick)
        (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0).background = ColorDrawable(Color.TRANSPARENT)
        PjjApplication.application.statueHeight = statue
        var welcome = SharedUtils.getXmlForKey(SharedUtils.USE_WELCOME)
        //Log.e("TAG", "welcome: $welcome - ${BuildConfig.VERSION_CODE} ")
        if (BuildConfig.VERSION_NAME != welcome) {
            //startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
            //finish()
            initWelcome()
            return
        }
        mHandler = MyHandler(this)
        Glide.with(this).load(R.mipmap.start_ad).into(iv_start_up)
        mHandler.sendEmptyMessageDelayed(0, 0)
        parentRl.setOnClickListener(onClick)
        tv_jump.setOnClickListener(onClick)
    }

    private fun autoJump() {
        if (!ivStartAdClick) {
            mHandler.removeMessages(0)
            ivStartAdClick = true
            var welcome = SharedUtils.getXmlForKey(SharedUtils.USE_WELCOME)
            //Log.e("TAG", "welcome: $welcome - ${BuildConfig.VERSION_CODE} ")
            if (BuildConfig.VERSION_NAME == welcome) {
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
            } else {
                startActivity(Intent(this@SplashActivity, WelcomeActivity::class.java))
            }
            finish()
        }
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.iv_start_up -> {
            }
            R.id.tv_jump -> autoJump()
            R.id.parentRl -> {
                //jumpAd()
            }
            R.id.tv_jump_wel, R.id.bt_start -> {
                SharedUtils.saveForXml(SharedUtils.USE_WELCOME, BuildConfig.VERSION_NAME)
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun initWelcome() {
        rl_welcome.visibility = View.VISIBLE
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
        vp_welcome.adapter = WelcomeActivity.WelcomeAdapter(views)
        tv_jump_wel.setOnClickListener(onClick)
        bt_start.setOnClickListener(onClick)
    }

    private fun jumpAd() {
        mHandler.removeMessages(0)
        ivStartAdClick = true
        WebActivity.newInstance(this, "全国区域独家代理", PjjApplication.AD_JOIN, "1", true)
        finish()
    }

    override fun allowBackFinish(): Boolean {
        return false
    }

    override fun fitSystemWindow(): Boolean {
        return false
    }

    override fun onBackPressed() {
        mHandler.removeMessages(0)
        ivStartAdClick = true
        super.onBackPressed()
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
