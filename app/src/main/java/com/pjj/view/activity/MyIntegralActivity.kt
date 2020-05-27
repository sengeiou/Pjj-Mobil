package com.pjj.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

import com.pjj.R
import com.pjj.contract.MyIntegralContract
import com.pjj.present.MyIntegralPresent
import com.pjj.utils.TextUtils
import com.pjj.view.adapter.OrderCommonNavigatorAdapter
import com.pjj.view.fragment.IntegralFragment
import kotlinx.android.synthetic.main.activity_myintegral.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import java.util.*

/**
 * Create by xinheng on 2019/04/04 15:32。
 * describe：我的金币
 */
class MyIntegralActivity : BaseActivity<MyIntegralPresent>(), MyIntegralContract.View {
    private val mDataList = Arrays.asList("全部", "金币获得", "金币消耗")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myintegral)
        setBlackTitle("我的金币")
        mPresent = MyIntegralPresent(this).apply {
            loadMyIntegralTask()
        }
        title_right.setOnClickListener(onClick)
        magic.navigator = CommonNavigator(this).apply {
            isAdjustMode = true
            adapter = OrderCommonNavigatorAdapter(mDataList).apply {
                onCurrentItemListener = object : OrderCommonNavigatorAdapter.OnCurrentItemListener {
                    override fun currentItem(index: Int) {
                        this@MyIntegralActivity.vp_integral.setCurrentItem(index, false)
                    }
                }
            }
        }
        ViewPagerHelper.bind(magic, vp_integral)
        vp_integral.offscreenPageLimit = 3
        vp_integral.adapter = MyIntegralViewpagerAdapter(supportFragmentManager)
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.title_right -> startActivity(Intent(this, IntegralRulesActivity::class.java))
        }
    }

    @SuppressLint("SetTextI18n")
    override fun updateMyIntegral(integral: String?) {
        tv_my_integral.text = "我的金币：${TextUtils.clean(integral)}金币"
    }

    override fun getStatusBarColor(): Int {
        return Color.WHITE
    }

    private class MyIntegralViewpagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> IntegralFragment.newInstance()
                1 -> IntegralFragment.newInstance("1")
                else -> IntegralFragment.newInstance("2")
            }
        }

        override fun getCount(): Int {
            return 3
        }

    }
}
