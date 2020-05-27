package com.pjj.view.activity

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.pjj.BuildConfig
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.present.BasePresent
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_about_self.*

class AboutSelfActivity : BaseActivity<BasePresent<*>>() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_self)
        setTitle("关于")
        tv_code.text = getString(R.string.app_name) + BuildConfig.VERSION_NAME
        tv_join.setOnClickListener {
            WebActivity.newInstance(this@AboutSelfActivity, "全国区域独家代理", PjjApplication.AD_JOIN, "5")
        }
        parent_scroll.background = ColorDrawable(ViewUtils.getColor(R.color.color_f1f1f1))
    }
}
