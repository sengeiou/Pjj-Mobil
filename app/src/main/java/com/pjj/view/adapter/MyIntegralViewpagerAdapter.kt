package com.pjj.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.pjj.module.MallClassificationBean
import com.pjj.view.fragment.IntegralMallFragment

class MyIntegralViewpagerAdapter(private var datas: MutableList<MallClassificationBean.DataBean>, fm: FragmentManager, private var mallTag: Boolean = false) : FragmentPagerAdapter(fm) {
    private val list: MutableList<IntegralMallFragment>

    init {
        list = ArrayList(datas.size)
        datas.forEach {
            list.add(IntegralMallFragment.newInstance(it.integraGoodsTypeId, mallTag))
        }
    }

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    override fun getCount(): Int {
        return datas.size
    }

    fun controlPullfresh(tag: Boolean) {
        list.forEach {
            it.changeRefreshAddLoadMoreStatue(tag)
        }
    }

    fun setRecycleViewCanScrollStatue(tag: Boolean) {
        list.forEach {
            it.setRecycleViewCanScrollStatue(tag)
        }
    }

    var onIntegralMallFragmentListener: IntegralMallFragment.OnIntegralMallFragmentListener? = null
        set(value) {
            field = value
            list.forEach {
                it.onIntegralMallFragmentListener = value
                it.setRecycleViewCanScrollStatue(false)
            }
        }

    fun getCurrentItemFragmnet(currentItem: Int): IntegralMallFragment {
        return list[currentItem]
    }
}