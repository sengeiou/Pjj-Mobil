package com.pjj.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import com.pjj.R
import com.pjj.contract.SpeedScreenContract
import com.pjj.module.SpeedBean
import com.pjj.module.SpeedDataBean
import com.pjj.module.xspad.XspManage
import com.pjj.present.BasePresent
import com.pjj.present.SpeedScreenPresent
import com.pjj.utils.ViewUtils
import com.pjj.view.adapter.OrderCommonNavigatorAdapter
import com.pjj.view.fragment.SpeedFragment
import net.lucode.hackware.magicindicator.MagicIndicator
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import com.pjj.utils.Log
import kotlinx.android.synthetic.main.activity_speed_screen.*

/**
 * 创建拼屏模板
 * 模板类型列表
 */
class SpeedScreenActivity : BaseActivity<SpeedScreenPresent>(), SpeedScreenContract.View {

    private var mViewPager: ViewPager? = null
    private var releaseTag = false

    companion object {
        @JvmStatic
        fun newInstance(activity: Activity, releaseTag: Boolean = false) {
            activity.startActivity(Intent(activity, SpeedScreenActivity::class.java).putExtra("releaseTag", releaseTag))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speed_screen)
        setTitle("拼屏信息模板")
        val magicIndicator = findViewById<MagicIndicator>(R.id.magic)
        mViewPager = findViewById(R.id.viewPager)
        mViewPager?.offscreenPageLimit = 0
        releaseTag = intent.getBooleanExtra("releaseTag", releaseTag)
        val commonNavigator = CommonNavigator(this)
        //commonNavigator.isAdjustMode = true
        var mDataList = ArrayList<String>(11)
        (2..12).forEach {
            mDataList.add("${it}人拼")
        }
        val adapter = OrderCommonNavigatorAdapter(mDataList).apply {
            onCurrentItemListener = object : OrderCommonNavigatorAdapter.OnCurrentItemListener {
                override fun currentItem(index: Int) {
                    mViewPager?.setCurrentItem(index, false)
                }
            }
        }
        commonNavigator.adapter = adapter
        commonNavigator.leftPadding = ViewUtils.getDp(R.dimen.dp_10)
        commonNavigator.rightPadding = ViewUtils.getDp(R.dimen.dp_10)
        commonNavigator.isSkimOver = true
        magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(magicIndicator, mViewPager)
        mPresent = SpeedScreenPresent(this).apply {
            loadSpeedDataTask()
        }
    }

    private var dataBean: SpeedBean.DateBean? = null
    override fun updateSpeedData(dataBean: SpeedBean.DateBean) {
        this.dataBean = dataBean
        var orderPagerAdapter = OrderPagerAdapter(supportFragmentManager)
        mViewPager?.adapter = orderPagerAdapter
    }

    override fun onDestroy() {
        //XspManage.getInstance().speedScreenData.templateData = null
        //XspManage.getInstance().cleanSpeedData()
        super.onDestroy()
    }

    inner class OrderPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
        override fun getItem(position: Int): Fragment {
            /* val method01 = dataBean!!::class.java.declaredMethods // 返回public方法
             method01.forEach {
                 Log.e("TAG", "method: $it")
             }*/
            return SpeedFragment.newInstance(position + 2).apply {
                dataList = dataBean?.getData(position)
                showCreate = !releaseTag
            }
        }

        override fun getCount(): Int {
            return 11
        }
    }
}
