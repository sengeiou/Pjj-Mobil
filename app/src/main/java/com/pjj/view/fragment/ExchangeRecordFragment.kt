package com.pjj.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pjj.contract.ExchangeRecordContract
import com.pjj.module.ExchangeRecordBean
import com.pjj.present.ExchangeRecordPresent
import com.pjj.view.activity.ExchangeRecordInfActivity
import com.pjj.view.adapter.ExchangeRecordListAdapter
import com.pjj.view.adapter.ListViewAdapter

/**
 * Create by xinheng on 2019/04/11 10:27。
 * describe：兑换记录
 */
class ExchangeRecordFragment : ListViewFragment<ExchangeRecordBean.ExchangeRecordData, ExchangeRecordPresent>(), ExchangeRecordContract.View {
    private var statue: String? = "3"

    companion object {
        private const val EXCHANGE_RECORD_STATUE = "exchange_record_statue"
        @JvmStatic
        fun newInstance(statue: String): ExchangeRecordFragment = ExchangeRecordFragment().apply {
            arguments = Bundle().apply {
                putString(EXCHANGE_RECORD_STATUE, statue)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            statue = it.getString(EXCHANGE_RECORD_STATUE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mPresent = ExchangeRecordPresent(this)
        super.onViewCreated(view, savedInstanceState)
        setNoDataText("暂无兑换记录信息")
    }

    override fun getAdapter(): ListViewAdapter<ExchangeRecordBean.ExchangeRecordData, *> {
        return ExchangeRecordListAdapter().apply {
            onExchangeRecordAdapterListener = object : ExchangeRecordListAdapter.OnExchangeRecordAdapterListener {
                override fun itemClick(data: ExchangeRecordBean.ExchangeRecordData) {
                    when (statue) {
                        "2" -> ExchangeRecordInfActivity.startActivityForResult(activity!!, data, 303)
                        else -> ExchangeRecordInfActivity.startActivity(activity!!, data)
                    }
                }
            }
        }
    }

    override fun loadData(start: Int, num: Int) {
        mPresent?.loadExchangeRecordTask(start, num, statue!!)
    }

    override fun updateExchangeRecord(list: MutableList<ExchangeRecordBean.ExchangeRecordData>?) {
        setData(list)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == 303 && resultCode == Activity.RESULT_OK -> pullToRefresh?.autoRefresh()
        }
    }
}
