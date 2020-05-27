package com.pjj.view.activity

import android.graphics.Color
import android.os.Bundle
import com.pjj.R
import com.pjj.intent.IntegralRetrofitService
import com.pjj.intent.RetrofitService
import com.pjj.module.GoodsExplainBean
import com.pjj.present.BasePresent
import kotlinx.android.synthetic.main.activity_integral_rules.*

class IntegralRulesActivity : BaseActivity<BasePresent<*>>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_integral_rules)
        setBlackTitle("金币规则")
        //TODO 添加接口
        IntegralRetrofitService.instance.getIntegralRule("2", object : RetrofitService.CallbackClassResult<GoodsExplainBean>(GoodsExplainBean::class.java) {
            override fun successResult(t: GoodsExplainBean) {
                t.data.content?.let {
                    this@IntegralRulesActivity.tv_content.text = it
                }
            }

            override fun fail(error: String?) {
                super.fail(error)
                showNotice(error)
            }
        })
    }

    override fun getStatusBarColor(): Int {
        return Color.WHITE
    }
}
