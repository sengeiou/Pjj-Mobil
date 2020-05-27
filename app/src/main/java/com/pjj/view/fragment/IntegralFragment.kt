package com.pjj.view.fragment

import android.os.Bundle
import android.view.View
import com.pjj.R
import com.pjj.contract.IntegralContract
import com.pjj.module.IntegralBean
import com.pjj.present.IntegralPresent
import com.pjj.view.adapter.IntegralListAdapter
import com.pjj.view.adapter.ListViewAdapter

/**
 * Create by xinheng on 2019/04/10 15:49。
 * describe：我的金币
 */

class IntegralFragment : ListViewFragment<IntegralBean.IntegralData, IntegralPresent>(), IntegralContract.View {
    companion object {
        private const val TYPE = "integral_type"
        @JvmStatic
        fun newInstance(type: String? = null): IntegralFragment {
            return IntegralFragment().apply {
                type?.let {
                    arguments = Bundle().apply {
                        putString(TYPE, it)
                    }
                }
            }
        }
    }

    private var type: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            type = it.getString(TYPE)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mPresent = IntegralPresent(this)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getAdapter(): ListViewAdapter<IntegralBean.IntegralData, *> {
        return IntegralListAdapter()
    }

    override fun loadData(start: Int, num: Int) {
        mPresent?.loadIntegral(start, num, type)
    }

    override fun updateData(list: MutableList<IntegralBean.IntegralData>?) {
        setData(list)
    }

}
