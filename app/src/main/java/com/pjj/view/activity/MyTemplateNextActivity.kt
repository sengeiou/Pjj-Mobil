package com.pjj.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.pjj.R
import com.pjj.present.BasePresent
import kotlinx.android.synthetic.main.activity_my_template_next.*

class MyTemplateNextActivity : BaseActivity<BasePresent<*>>() {
    companion object {
        //用途类型:1 DIY类型 2便民信息 3填空传媒
        val TEM_TYPE = "tem_type"
        val DIY_TEM = 1
        val SUI_JI_TEM_DIY = 3
        val SUI_JI_TEM_BM = 4
        val BIAN_MIN_TEM = 2
        fun start(context: Context, type: Int) {
            context.startActivity(Intent(context, MyTemplateNextActivity::class.java).putExtra(TEM_TYPE, type))
        }
    }

    private var temType: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_template_next)
        temType = intent.getIntExtra(TEM_TYPE, temType)
        handle(temType)
        iv1.setOnClickListener(onClick)
        iv2.setOnClickListener(onClick)
    }

    private fun handle(intExtra: Int) {
        var texts = "模板"
        var imgResourse = when (intExtra) {
            DIY_TEM -> {
                //texts = "DIY信息模板"
                R.mipmap.template_diy
            }
            SUI_JI_TEM_DIY -> {
                //texts = "随机信息模板"
                R.mipmap.template_suiji
            }
            SUI_JI_TEM_BM -> {
                //texts = "随机便民模板"
                R.mipmap.template_suiji_bm
            }
            else -> {
                //texts = "便民信息模板"
                R.mipmap.template_bm
            }
        }
        setTitle(texts)
        iv1.setImageResource(imgResourse)
        iv2.setImageResource(imgResourse)
        name_tv1.text = "个人$texts"
        name_tv2.text = "商家$texts"
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.iv1 -> startActivity(Intent(this, TemplateListActivity::class.java)
                    .putExtra("title_text", name_tv1.text.toString())
                    .putExtra("identity_type", "1")
                    .putExtra("ad_type", temType))
            else -> startActivity(Intent(this, TemplateListActivity::class.java)
                    .putExtra("title_text", name_tv2.text.toString())
                    .putExtra("identity_type", "2")
                    .putExtra("ad_type", temType))
        }
    }
}
