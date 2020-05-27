package com.pjj.view.fragment


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.pjj.R
import com.pjj.present.BasePresent
import com.pjj.view.adapter.OrderCommonNavigatorAdapter
import kotlinx.android.synthetic.main.fragment_mall_order_list.*
import kotlinx.android.synthetic.main.layout_head_title.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import java.util.*
import kotlin.math.min

/**
 * A simple [Fragment] subclass.
 *
 */
class MallOrderListFragment : BaseFragment<BasePresent<*>>() {
    private val mDataList = Arrays.asList("待支付", "待收货", "已完成")

    override fun getLayoutRes(): Int {
        return R.layout.fragment_mall_order_list
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleView.setTextMiddle("订单列表", Color.BLACK)
        titleView.background = ColorDrawable(Color.WHITE)
        magic.navigator = CommonNavigator(activity).apply {
            isAdjustMode = true
            adapter = OrderCommonNavigatorAdapter(mDataList).apply {
                onCurrentItemListener = object : OrderCommonNavigatorAdapter.OnCurrentItemListener {
                    override fun currentItem(index: Int) {
                        this@MallOrderListFragment.vp_integral.setCurrentItem(index, false)
                    }
                }
            }
        }
        ViewPagerHelper.bind(magic, vp_integral)
        vp_integral.offscreenPageLimit = 3
        vp_integral.adapter = MyIntegralViewpagerAdapter(childFragmentManager)
    }

    private val onMallOrderFragmentListener = object : MallOrderFragment.OnMallOrderFragmentListener {
        override fun startOtherFragment(tag: Int) {
            vp_integral.currentItem = min(tag, 2)
        }
    }

    private inner class MyIntegralViewpagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> MallOrderFragment.newInstance(0).apply {
                    onMallOrderFragmentListener = this@MallOrderListFragment.onMallOrderFragmentListener
                }
                1 -> MallOrderFragment.newInstance(1).apply {
                    onMallOrderFragmentListener = this@MallOrderListFragment.onMallOrderFragmentListener
                }
                else -> MallOrderFragment.newInstance(3).apply {
                    onMallOrderFragmentListener = this@MallOrderListFragment.onMallOrderFragmentListener
                }
            }
        }

        override fun getCount(): Int {
            return 3
        }

    }
}
