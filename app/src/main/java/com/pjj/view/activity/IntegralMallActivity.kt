package com.pjj.view.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

import com.pjj.R
import com.pjj.contract.IntegralMallContract
import com.pjj.module.GoodsListBean
import com.pjj.module.MallClassificationBean
import com.pjj.present.IntegralMallPresent
import com.pjj.utils.PullRefreshHelper
import com.pjj.utils.ViewUtils
import com.pjj.view.adapter.IntegralMallAdapter
import com.pjj.view.adapter.MyIntegralViewpagerAdapter
import com.pjj.view.adapter.OrderCommonNavigatorAdapter
import com.pjj.view.fragment.IntegralMallFragment
import kotlinx.android.synthetic.main.activity_integralmall.*
import kotlinx.android.synthetic.main.activity_myintegral.magic
import kotlinx.android.synthetic.main.activity_myintegral.title_right
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator

/**
 * Create by xinheng on 2019/04/04 13:30。
 * describe：金币商城
 */
class IntegralMallActivity : BaseActivity<IntegralMallPresent>(), IntegralMallContract.View {
    private lateinit var commonNavigator: CommonNavigator
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_integralmall)
        setBlackTitle("新零售商城")
        title_right.setOnClickListener(onClick)
        mPresent = IntegralMallPresent(this, false).apply {
            loadClassificationTask()
        }
        commonNavigator = CommonNavigator(this)
        magic.navigator = commonNavigator
        ViewPagerHelper.bind(magic, vp_mall)
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.title_right -> startActivity(Intent(this, ExchangeRecordActivity::class.java))
        }
    }


    override fun updateList(list: MutableList<GoodsListBean.GoodsListData>?) {
    }

    override fun updateClassificationData(data: MutableList<MallClassificationBean.DataBean>) {
        val list = ArrayList<String>()
        data.forEach {
            list.add(it.categoryName)
        }
        commonNavigator.adapter = OrderCommonNavigatorAdapter(list).apply {
            onCurrentItemListener = object : OrderCommonNavigatorAdapter.OnCurrentItemListener {
                override fun currentItem(index: Int) {
                    this@IntegralMallActivity.vp_mall.setCurrentItem(index, false)
                }
            }
        }
        vp_mall.adapter = MyIntegralViewpagerAdapter(data, supportFragmentManager)
    }

    override fun getStatusBarColor(): Int {
        return Color.WHITE
    }


}
