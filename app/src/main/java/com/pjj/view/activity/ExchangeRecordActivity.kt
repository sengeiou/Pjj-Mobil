package com.pjj.view.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.pjj.R
import com.pjj.present.BasePresent
import com.pjj.view.adapter.OrderCommonNavigatorAdapter
import com.pjj.view.fragment.ExchangeRecordFragment
import kotlinx.android.synthetic.main.activity_exchange_record.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import java.util.*

class ExchangeRecordActivity : BaseActivity<BasePresent<*>>() {
    private val mDataList = Arrays.asList("待发货", "待收货", "已完成")
    private var fragmentFirst: ExchangeRecordFragment? = null
    private var makeOrderTag = false
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        fragmentFirst?.startRefresh()
        intent?.let {
            makeOrderTag = it.getBooleanExtra("makeOrderTag", makeOrderTag)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exchange_record)
        setBlackTitle("兑换记录")
        makeOrderTag = intent.getBooleanExtra("makeOrderTag", makeOrderTag)
        magic.navigator = CommonNavigator(this).apply {
            isAdjustMode = true
            adapter = OrderCommonNavigatorAdapter(mDataList).apply {
                onCurrentItemListener = object : OrderCommonNavigatorAdapter.OnCurrentItemListener {
                    override fun currentItem(index: Int) {
                        this@ExchangeRecordActivity.vp_integral.setCurrentItem(index, false)
                    }
                }
            }
        }
        ViewPagerHelper.bind(magic, vp_integral)
        vp_integral.offscreenPageLimit = 3
        vp_integral.adapter = MyIntegralViewpagerAdapter(supportFragmentManager)
    }

    override fun getStatusBarColor(): Int {
        return Color.WHITE
    }

    override fun finish() {
        if (makeOrderTag) {
            startActivity(Intent(this, IntegralMallActivity::class.java))
        }
        super.finish()
    }

    override fun onBackPressed() {
        if (makeOrderTag) {
            finish()
            return
        }
        super.onBackPressed()
    }

    private inner class MyIntegralViewpagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            return when (position) {
                0 -> ExchangeRecordFragment.newInstance("1").apply {
                    fragmentFirst = this
                }
                1 -> ExchangeRecordFragment.newInstance("2")
                else -> ExchangeRecordFragment.newInstance("3")
            }
        }

        override fun getCount(): Int {
            return 3
        }

    }
}
