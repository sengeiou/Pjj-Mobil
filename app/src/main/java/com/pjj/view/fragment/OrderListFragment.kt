package com.pjj.view.fragment

import android.os.Bundle
import android.support.v4.view.ViewPager
import android.view.View

import com.pjj.R
import com.pjj.contract.OrderContract
import com.pjj.present.OrderPresent
import com.pjj.utils.Log
import com.pjj.view.adapter.OrderCommonNavigatorAdapter
import com.pjj.view.adapter.OrderPagerAdapter

import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

import java.util.Arrays

/**
 * Create by xinheng on 2018/12/07 10:26。
 * describe：订单
 */
class OrderListFragment : BaseFragment<OrderPresent>(), OrderContract.View {
    private var mViewPager: ViewPager? = null
    private var orderPagerAdapter: OrderPagerAdapter? = null
    private val mDataList = Arrays.asList(*CHANNELS)
    var index = 0
        set(value) {
            field = value
            if (value >= 0 && mViewPager != null) {
                mViewPager?.setCurrentItem(value, false)
                Log.e("TAG", "setIndex - ${orderPagerAdapter == null}")
                orderPagerAdapter?.let {
                    it.fragments[value].needUpdate()
                }
            }
        }

    override fun getLayoutRes(): Int {
        return R.layout.activity_order
    }

    override fun isHeadPaddingStatue(): Boolean {
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        var index = intent.getIntExtra("index", 0)
        val magicIndicator = view.findViewById<MagicIndicator>(R.id.magic)
        mViewPager = view.findViewById(R.id.viewPager)
        mViewPager?.offscreenPageLimit = 3
        orderPagerAdapter = OrderPagerAdapter(childFragmentManager)
        mViewPager?.adapter = orderPagerAdapter
        val commonNavigator = CommonNavigator(activity)
        commonNavigator.isAdjustMode = true
        val adapter = OrderCommonNavigatorAdapter(mDataList).apply {
            onCurrentItemListener = object : OrderCommonNavigatorAdapter.OnCurrentItemListener {
                override fun currentItem(index: Int) {
                    mViewPager?.setCurrentItem(index, false)
                }
            }
        }
        commonNavigator.adapter = adapter
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, mViewPager)
        if (index > 0) {
            mViewPager?.setCurrentItem(index, false)
        }
    }

    companion object {
        private val CHANNELS = arrayOf("待支付",/* "审核中",*/ "发布中", "已结束")
    }
}
