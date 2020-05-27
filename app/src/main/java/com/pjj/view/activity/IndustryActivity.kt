package com.pjj.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager

import com.pjj.R
import com.pjj.contract.IndustryContract
import com.pjj.module.IndustryBean
import com.pjj.present.IndustryPresent
import com.pjj.view.adapter.IndustryAdapter
import kotlinx.android.synthetic.main.activity_industry.*

/**
 * Create by xinheng on 2018/12/11 16:03。
 * describe：行业类型
 */
class IndustryActivity : BaseActivity<IndustryPresent>(), IndustryContract.View {
    private lateinit var industryAdapter: IndustryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_industry)
        setTitle("选择行业类型")
        industryAdapter = IndustryAdapter().apply {
            onItemClickListener = object : IndustryAdapter.OnItemClickListener {
                override fun itemClick(dataBean: IndustryBean.DataBean) {
                    setResult(200, Intent()
                            .putExtra("indestry_type_id", dataBean.dicId)
                            .putExtra("industry", dataBean.dicName))
                    finish()
                }

            }
        }
        mPresent = IndustryPresent(this).apply {
            loadIndustryTask()
        }
        rv_industry.run {
            adapter = industryAdapter
            layoutManager = LinearLayoutManager(this@IndustryActivity)
        }
    }

    override fun updateList(list: MutableList<IndustryBean.DataBean>) {
        industryAdapter.list = list
    }
}
