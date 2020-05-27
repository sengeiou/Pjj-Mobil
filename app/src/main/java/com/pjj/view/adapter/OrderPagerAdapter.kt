package com.pjj.view.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.pjj.view.fragment.OrderFragment

/**
 * Created by XinHeng on 2018/12/07.
 * describe：
 */
class OrderPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    var fragments = arrayOf(OrderFragment.newInstance(OrderFragment.WAITE_PAY)/*, OrderFragment.newInstance(OrderFragment.AUDIT)*/
            , OrderFragment.newInstance(OrderFragment.RELEASE), OrderFragment.newInstance(OrderFragment.COMPLETE))

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> fragments[0]
            1 -> fragments[1]
            2 -> fragments[2]
            else -> fragments[3]
        }
    }

    override fun getCount(): Int {
        return fragments.size
    }

}