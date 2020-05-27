package com.pjj.view.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.pjj.PjjApplication

import com.pjj.R
import com.pjj.contract.MallHomepageContract
import com.pjj.module.MallClassificationBean
import com.pjj.module.MallHomepageGoodsBean
import com.pjj.present.MallHomepagePresent
import com.pjj.utils.ViewUtils
import com.pjj.view.activity.IntegralMallActivity
import com.pjj.view.adapter.MyIntegralViewpagerAdapter
import com.pjj.view.adapter.OrderCommonNavigatorAdapter
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.fragment_mallhomepage.*
import net.lucode.hackware.magicindicator.ViewPagerHelper
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator
import android.widget.LinearLayout
import com.pjj.utils.Log
import com.pjj.view.activity.MallGoodsDetailsActivity
import com.pjj.view.custom.MyScrollView
import com.pjj.view.pulltorefresh.BaseRefreshListener


/**
 * Create by xinheng on 2019/05/08 16:16。
 * describe：新零售首页
 */
class MallHomepageFragment : BaseFragment<MallHomepagePresent>(), MallHomepageContract.View {
    private lateinit var commonNavigator: CommonNavigator
    private var myIntegralViewpagerAdapter: MyIntegralViewpagerAdapter? = null
    var scrollTag = true
        private set

    override fun getLayoutRes(): Int {
        return R.layout.fragment_mallhomepage
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mallTitleView.setTextMiddle("购物商城", Color.BLACK)
        mallTitleView.setDrawableLeft(ViewUtils.getDrawable(R.mipmap.back_icon_white).apply { setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP) })
        mallTitleView.setOnClickListener(onClick)
        //pullToRefresh.setCanLoadMore(false)
        vp_mall.post {
            val heihgtParent = (mallTitleView.parent as LinearLayout).measuredHeight
            val empty = heihgtParent - mallTitleView.measuredHeight - magic.measuredHeight - ViewUtils.getDp(R.dimen.dp_1)
            val lp = vp_mall.layoutParams
            lp.height = empty
            vp_mall.layoutParams = lp
        }
        //myScrollView.allowDealWithEvent = 2
        myScrollView.onMyScrollViewListener = object : MyScrollView.OnMyScrollViewListener {
            override fun onScrollChange(v: View, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int) {
                val measuredHeight = rl_top.measuredHeight
                Log.e("TAG", "myScrollView measuredHeight=$measuredHeight , scrollY=$scrollY")
                //myScrollView.interceptMove = scrollY < measuredHeight
                if (scrollY == measuredHeight) {
                    //myIntegralViewpagerAdapter?.setRecycleViewCanScrollStatue(true)
                    //myScrollView.allowDealWithEvent = 1
                    pullToRefresh.setCanLoadMore(false)
                } else {
                    pullToRefresh.setCanLoadMore(true)
                    //if (scrollY == 0)
                    //myIntegralViewpagerAdapter?.setRecycleViewCanScrollStatue(false)
                    //myScrollView.allowDealWithEvent = 2
                }
            }
        }
        pullToRefresh.setRefreshListener(object : BaseRefreshListener {
            override fun refresh() {
                this@MallHomepageFragment.refresh()
            }

            override fun loadMore() {
                this@MallHomepageFragment.loadMore()
            }

        })
        initBanner()
        commonNavigator = CommonNavigator(activity)
        magic.navigator = commonNavigator
        ViewPagerHelper.bind(magic, vp_mall)
        mPresent = MallHomepagePresent(this).apply {
            loadClassificationTask()
        }
        //tv_left.setOnClickListener(onClick)
        tv_right.setOnClickListener(onClick)
    }

    fun backFinish() {
        if (scrollTag) {
            activity!!.finish()
        } else {
            scrollTag = true
            mallTitleView.setTouchInter(false)
        }
    }

    private fun refresh() {
        val currentItem = vp_mall.currentItem
        myIntegralViewpagerAdapter?.getCurrentItemFragmnet(currentItem)?.pullRefreshHelper?.refresh()
    }

    private fun loadMore() {
        val currentItem = vp_mall.currentItem
        myIntegralViewpagerAdapter?.getCurrentItemFragmnet(currentItem)?.pullRefreshHelper?.loadMore()
    }

    private val onClick = View.OnClickListener {
        when (it.id) {
            R.id.mallTitleView -> backFinish()
            R.id.tv_right -> startActivity(Intent(activity!!, IntegralMallActivity::class.java))

        }
    }


    override fun updateBanner(list: MutableList<MallHomepageGoodsBean.DataBean.AdverBannerBean>?) {
        list?.let {
            val list_path = ArrayList<String>(list.size)
            val list_title = ArrayList<String>(list.size)
            it.forEach { index ->
                list_path.add(PjjApplication.integralFilePath + index.bannerPicture)
                list_title.add("")
            }
            banner.setOnBannerListener { position ->
                val goodsId = it[position].goodsId
                startActivity(Intent(activity!!, MallGoodsDetailsActivity::class.java).putExtra("goodsId", goodsId))
            }
            banner.update(list_path, list_title)
        }
    }

    private fun initBanner() {
        var list_path = ArrayList<String>()
        var list_title = ArrayList<String>()
        list_path.add("file:///android_asset/top_head1.png")
        list_path.add("file:///android_asset/top_head2.png")
        list_path.add("file:///android_asset/top_head3.png")
        list_title.add("")
        list_title.add("")
        list_title.add("")
        //banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE_INSIDE)
        banner.setImageLoader(object : ImageLoader() {
            override fun displayImage(context: Context, path: Any, imageView: ImageView) {
                Glide.with(context).load(path as String).into(imageView)
            }
        })
        banner.setOnBannerListener { }
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
        //设置图片网址或地址的集合
        banner.setImages(list_path)
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default)
        //设置轮播图的标题集合
        banner.setBannerTitles(list_title)
        //设置轮播间隔时间
        banner.setDelayTime(4000)
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true)
        //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
        banner.setOnBannerListener { }
        //必须最后调用的方法，启动轮播图。
        banner.start()
    }

    override fun updateTitleList(data: MutableList<MallClassificationBean.DataBean>) {
        val list = ArrayList<String>()
        data.forEach {
            list.add(it.categoryName)
        }
        commonNavigator.adapter = OrderCommonNavigatorAdapter(list).apply {
            onCurrentItemListener = object : OrderCommonNavigatorAdapter.OnCurrentItemListener {
                override fun currentItem(index: Int) {
                    this@MallHomepageFragment.vp_mall.setCurrentItem(index, false)
                }
            }
        }
        myIntegralViewpagerAdapter = MyIntegralViewpagerAdapter(data, childFragmentManager, true).apply {
            onIntegralMallFragmentListener = object : IntegralMallFragment.OnIntegralMallFragmentListener {
                override fun parentIsNeedIntercept(): Boolean {
                    Log.e("TAG", "parentIsNeedIntercept: ${myScrollView.myScrollY} ${this@MallHomepageFragment.rl_top.measuredHeight}")
                    return myScrollView.myScrollY < this@MallHomepageFragment.rl_top.measuredHeight
                }

                override fun setCanLoadMoreStatue(statue: Boolean) {
                    pullToRefresh.setCanLoadMore(true)
                }

                override fun finishLoad() {
                    pullToRefresh.finishRefresh()
                    pullToRefresh.finishLoadMore()
                }
            }
        }
        vp_mall.adapter = myIntegralViewpagerAdapter
        //myIntegralViewpagerAdapter?.controlPullfresh(false)
    }
}
