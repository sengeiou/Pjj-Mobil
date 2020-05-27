package com.pjj.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.view.View
import android.webkit.*
import android.widget.FrameLayout
import com.pjj.BuildConfig
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.intent.RetrofitService
import com.pjj.present.BasePresent
import com.pjj.utils.SharedUtils
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_web.*

class WebActivity : BaseActivity<BasePresent<*>>() {
    companion object {
        fun newInstance(activity: Activity, title: String, url: String, from: String = "1", startPage: Boolean = false) {
            activity.startActivity(Intent(activity, WebActivity::class.java)
                    .putExtra("title", title)
                    .putExtra("url", url)
                    .putExtra("from_type", from)
                    .putExtra("startPage", startPage)
            )
        }
    }

    private lateinit var webView: WebView
    private var startPage = false
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        var title = intent.getStringExtra("title")
        var hidden = true
        if (null == title) {
            title = "屏加加医学微视"
        }
        var url = intent.getStringExtra("url")
        var from = intent.getStringExtra("from_type")
        startPage = intent.getBooleanExtra("startPage", startPage)
        if (null == url) {
            hidden = false
            url = "http://www.mvyxws.com/"
        }
        setTitle(title)
        addWebView()
//        progress.set
        webView.webViewClient = object : WebViewClient() {
            @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
            override fun shouldOverrideUrlLoading(view: WebView, request: WebResourceRequest): Boolean {
                var urlPath = request.url.toString()
                if (urlPath.startsWith("tel:")) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlPath)))
                } else {
                    view.loadUrl(urlPath)
                }
                return true
            }

            override fun shouldOverrideUrlLoading(view: WebView, urlPath: String): Boolean {
                if (urlPath.startsWith("tel:")) {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(urlPath)))
                } else {
                    view.loadUrl(urlPath)
                }
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                cancelWaiteStatue()
                changeStatueColor()
            }
        }
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if (newProgress == 100) {
                    progress.visibility = View.GONE
                } else {
                    if (progress.visibility == View.GONE)
                        progress.visibility = View.VISIBLE
                    progress.progress = newProgress
                }
                super.onProgressChanged(view, newProgress)
            }
        }
        var settings = webView.settings

        settings.useWideViewPort = true // 关键点
        settings.allowFileAccess = true// 允许访问文件
        settings.setSupportZoom(true) // 支持缩放
        settings.loadWithOverviewMode = true
        settings.cacheMode = WebSettings.LOAD_NO_CACHE // 不加载缓存内容
        //关键在于这一句，具体作用还不清楚。
        settings.domStorageEnabled = true
        settings.javaScriptEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        webView.loadUrl(url)
        iv_right.rotation = 180f
        iv_left.setColorFilter(ViewUtils.getColor(R.color.color_999999))
        iv_right.setColorFilter(ViewUtils.getColor(R.color.color_999999))
        iv_left.setOnClickListener(onClick)
        iv_right.setOnClickListener(onClick)

        if (hidden) {
            ll.visibility = View.GONE
            line.visibility = View.GONE
            if (url == PjjApplication.AD_JOIN) {
                var mac = getMac()
                var phone: String? = null
                if (null == mac) {
                    phone = SharedUtils.getXmlForKey(SharedUtils.USER_PHONE)
                }
                //访问招商统计
                RetrofitService.getInstance().loadZhaoShangTask(mac, phone, from, object : RetrofitService.MyCallback() {
                    override fun success(s: String?) {
                    }
                })
            }
        } else {
            var mac = getMac()
            var phone: String? = null
            if (null == mac) {
                phone = SharedUtils.getXmlForKey(SharedUtils.USER_PHONE)
            }
            //访问医学统计
            RetrofitService.getInstance().loadYiXueWeiShiTask(mac, phone, "1", object : RetrofitService.MyCallback() {
                override fun success(s: String?) {
                }
            })
        }
    }

    private fun addWebView() {
        fl_webView.addView(WebView(this).apply {
            this@WebActivity.webView = this
        }, FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun titleLeftClick() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else {
            if (startPage) {
                var welcome = SharedUtils.getXmlForKey(SharedUtils.USE_WELCOME)
                if (BuildConfig.VERSION_NAME == welcome) {
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    startActivity(Intent(this, WelcomeActivity::class.java))
                }
            }
            finish()
        }
    }

    override fun onBackPressed() {
        /*if (webView.canGoBack()) {
            webView.goBack()
        } else {
            super.onBackPressed()
        }*/
        titleLeftClick()
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.iv_left -> {
                if (webView.canGoBack()) {
                    webView.goBack()
                }
            }
            R.id.iv_right -> {
                if (webView.canGoForward()) {
                    webView.goForward()
                }
            }
        }
    }

    private fun changeStatueColor() {
        if (webView.canGoBack())
            iv_left.setColorFilter(ViewUtils.getColor(R.color.color_999999))
        else
            iv_left.setColorFilter(ViewUtils.getColor(R.color.color_d5d5d5))

        if (webView.canGoForward())
            iv_right.setColorFilter(ViewUtils.getColor(R.color.color_999999))
        else
            iv_right.setColorFilter(ViewUtils.getColor(R.color.color_d5d5d5))
    }

    override fun onDestroy() {
        webView.clearHistory()
        webView.clearCache(true)
        webView.loadUrl("about:blank")
        webView.freeMemory()
        fl_webView.removeAllViews()
        webView.destroy()
        super.onDestroy()
    }

    private fun getMac(): String? {
        if (SharedUtils.checkLogin()) {
            return null
        }
        var wifi = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        var winfo = wifi.connectionInfo
        var mac = winfo.macAddress
        return mac
    }
}
