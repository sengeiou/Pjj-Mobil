package com.pjj.view.activity

import android.content.Intent
import android.content.pm.ActivityInfo
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle

import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import com.pjj.BuildConfig
import com.pjj.PjjApplication

import com.pjj.R
import com.pjj.present.BasePresent
import com.pjj.utils.Log
import com.pjj.utils.SharedUtils
import com.pjj.utils.StatusBarUtil
import com.pjj.utils.ViewUtils
import com.pjj.view.custom.TitleView
import com.pjj.view.dialog.NoticeDialog
import com.pjj.view.dialog.WaiteDialog
import com.umeng.analytics.MobclickAgent


/**
 * Create by xinheng on 2018/10/29 15:42。
 * describe：
 */
open class BaseActivity<P : BasePresent<*>> : AppCompatActivity() {
    protected var mPresent: P? = null
    private var mToast: Toast? = null
    protected open var smillTag = false
    protected open val waiteDialog: WaiteDialog by lazy {
        WaiteDialog(this).apply {
            onWaiteListener = object : WaiteDialog.OnWaiteListener {
                override fun timeOutCancel() {
                    waiteTimeOutCancel()
                }

                override fun initiativeCancel() {
                    waiteInitiativeCancel()
                }
            }
        }
    }
    protected open val noticeDialog: NoticeDialog by lazy {
        NoticeDialog(this)

    }
    protected lateinit var titleView: TitleView
    val color: Int by lazy {
        ContextCompat.getColor(application, R.color.color_theme)
    }
    val bgColorDrawable: Drawable by lazy {
        ColorDrawable(color)
    }
    /**
     * onCreate方法执行标志
     */
    protected open var onCreateTag: Boolean = false

    protected open fun isSetHeadPaddingStatue(): Boolean {
        return true
    }

    /**
     * 如果需要内容紧贴着StatusBar
     * 应该在对应的xml布局文件中，设置根布局fitsSystemWindows=true。
     */
    private lateinit var contentViewGroup: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreateTag = true
        if (allowBackFinish() && BuildConfig.DEBUG) {
//            val kugouLayout = KugouLayout(this)
//            kugouLayout.setAnimType(KugouLayout.REBOUND_ANIM)
//            //kugouLayout.setAnimType(KugouLayout.ALWAYS_REBOUND)
//            kugouLayout.attach(this)
//            kugouLayout.setLayoutCloseListener { finish() }
        }
    }


    override fun onResume() {
        super.onResume()
        if (BuildConfig.APP_TYPE)
            MobclickAgent.onResume(this)
    }

    protected open fun getStatusBarColor(): Int {
        return ViewUtils.getColor(R.color.color_theme)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        var fitSystemWindow = fitSystemWindow()
        if (fitSystemWindow) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.statusBarColor = getStatusBarColor()
            }
        } else {
            setStatusBarFullTransparent()
            //为了适配8.0系统
            //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED// 设置未指定类型
        }
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            try {
                requestedOrientation = requestedOrientation// 设置为竖屏屏模式
            } catch (e: Exception) {
                e.printStackTrace()
            }
        } else {
            //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
//            try {
//                requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_NOSENSOR
//            } catch (e: Exception) {
//            }
        }
        setFitSystemWindow(fitSystemWindow)
        /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
             window.navigationBarColor = Color.WHITE
             window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
         }*/
    }

    protected open fun fitSystemWindow(): Boolean {
        return true
    }

    protected open fun allowBackFinish(): Boolean {
        return true
    }


    /**
     * 全透状态栏
     */
    protected fun setStatusBarFullTransparent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {//9.0
            val lp = window.attributes
            ////设置页面延伸到刘海区显示
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = lp
//            val displayCutout = window.decorView.rootWindowInsets?.displayCutout
//            Log.e("TAG", "displayCutout: $displayCutout")
        }
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            val window = window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.TRANSPARENT
        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //虚拟键盘也透明
            //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    open fun showWaiteStatue() {
        if (!waiteDialog.isShowing && !isFinishing)
            waiteDialog.show()
    }

    open fun cancelWaiteStatue() {
        if (!isFinishing && waiteDialog.isShowing)
            waiteDialog.dismiss()
    }

    open fun showNotice(error: String?) {
        cancelWaiteStatue()
        error?.let {
            if (it.contains("用户验证不通过")) {
                cancelWaiteStatue()
                SharedUtils.saveForXml(SharedUtils.USER_ID, "")
                SharedUtils.saveForXml(SharedUtils.TOKEN, "")
                val mToast = Toast.makeText(PjjApplication.application, "登录已过期，请重新登录", Toast.LENGTH_LONG)
                mToast.setGravity(Gravity.CENTER, 0, 0)
                mToast.show()
                startActivity(Intent(this@BaseActivity, LoginActivity::class.java).putExtra("time_out", true).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                })
                finish()
                return
            }
        }

        var error = error
        if (null == error) {
            //msg = " msg = null";
            error = "失败-000"
        } else if (error.contains("<html>")) {
            error = "服务器开小差了"
        }
        noticeDialog.updateImage(if (smillTag) R.mipmap.smile else R.mipmap.cry_white)
        noticeDialog.setNotice(error)
    }

    /**
     * 半透明状态栏
     */
    private fun setHalfTransparent() {
        if (Build.VERSION.SDK_INT >= 21) {//21表示5.0
            val decorView = window.decorView
            val option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            decorView.systemUiVisibility = option
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        } else if (Build.VERSION.SDK_INT >= 19) {//19表示4.4
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            //虚拟键盘也透明
            // getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }

    protected open val statue: Int by lazy {
        var resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        resources.getDimensionPixelSize(resourceId)
    }

    protected fun setFitSystemWindow(fitSystemWindow: Boolean) {
        contentViewGroup = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0)
        contentViewGroup.fitsSystemWindows = fitSystemWindow
        val background = contentViewGroup.background
        if (null == background) {
            contentViewGroup.background = ColorDrawable(Color.WHITE)
        }
        //if (isSetHeadPaddingStatue()) {
        //contentViewGroup.setPadding(contentViewGroup.paddingLeft, contentViewGroup.paddingTop + statue, contentViewGroup.paddingRight, contentViewGroup.paddingBottom)
        // }
    }

    protected open fun onClick(view: View) {
        val id = view.id
        if (id == R.id.titleView) {
            titleLeftClick()
            return
        }
        onClick(id)
    }

    protected open val onClick: View.OnClickListener by lazy {
        View.OnClickListener {
            onClick(it)
        }
    }

    protected open fun onClick(viewId: Int) {

    }

    override fun getRequestedOrientation(): Int {
        return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    /**
     * 设置标题
     * @title 标题
     * @color 颜色（标题与左边图片过滤颜色）
     * @res 左边图片地址（resource）
     */
    protected fun setTitle(title: String, color: Int = Color.WHITE, res: Int = 0): TitleView {
        titleView = findViewById(R.id.titleView)
        titleView.run {
            setTextMiddle(title, color)//R.mipmap.back_icon_white
            when (res) {
                -1 -> {
                }
                0 -> setDrawableLeft(ContextCompat.getDrawable(this@BaseActivity, R.mipmap.back_icon_white))
                else -> setDrawableLeft(ContextCompat.getDrawable(this@BaseActivity, res))
            }
            if (res != -1) {

            }
            setOnClickListener(onClick)
        }
        titleView.background = ColorDrawable(getStatusBarColor())
        return titleView
    }

    protected fun setBlackTitle(title: String) {
        titleView = findViewById(R.id.titleView)
        titleView.setTextMiddle(title, Color.BLACK)
        titleView.setDrawableLeft(ContextCompat.getDrawable(this@BaseActivity, R.mipmap.back_icon_white)?.apply {
            setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
        })
        titleView.background = ColorDrawable(Color.WHITE)
        titleView.setOnClickListener(onClick)
        //BarTextColorUtils.StatusBarLightMode(this)
        setStatueWhite()
    }

    protected fun setStatueWhite() {
        StatusBarUtil.setTranslucentStatus(this)
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0x55000000)
        }
    }

    protected open fun titleLeftClick() {
        finish()
    }

    protected fun toast(msg1: String?) {
        msg1?.let {
            if (it.contains("用户验证不通过")) {
                SharedUtils.saveForXml(SharedUtils.USER_ID, "")
                SharedUtils.saveForXml(SharedUtils.TOKEN, "")
                showToast("登录已过期，请重新登录")
                startActivity(Intent(this@BaseActivity, LoginActivity::class.java).putExtra("time_out", true).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                })
                finish()
                return
            }
        }

        var msg = msg1
        if (null == msg) {
            msg = " msg = null"
        }
        if (mToast == null) {
            mToast = Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT)
            mToast?.setGravity(Gravity.CENTER, 0, 0)
        } else {
            mToast!!.setText(msg)
        }
        mToast!!.show()
    }

    fun showToast(s: String) {
        val mToast = Toast.makeText(PjjApplication.application, s, Toast.LENGTH_LONG)
        mToast.setGravity(Gravity.CENTER, 0, 0)
        mToast.show()
    }

    /**
     * 等待框超时，取消的回调
     */
    protected open fun waiteTimeOutCancel() {
        mPresent?.recycle()
    }

    protected open fun waiteInitiativeCancel() {

    }

    override fun onPause() {
        mToast?.cancel()
        onCreateTag = false
        super.onPause()
        if (BuildConfig.APP_TYPE)
            MobclickAgent.onPause(this)
    }

    override fun onDestroy() {
        if (null != mPresent) {
            mPresent!!.recycle()
            mPresent = null
        }
        mToast = null
        if (noticeDialog.isShowing) {
            noticeDialog.dismiss()
        }
        cancelWaiteStatue()
        super.onDestroy()
    }
}