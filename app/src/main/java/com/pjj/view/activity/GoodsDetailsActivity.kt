package com.pjj.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pjj.PjjApplication

import com.pjj.R
import com.pjj.contract.GoodsDetailsContract
import com.pjj.module.GoodsListBean
import com.pjj.module.xspad.XspManage
import com.pjj.present.GoodsDetailsPresent
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.activity_goodsdetails.*

/**
 * Create by xinheng on 2019/04/04 14:35。
 * describe：商品详情
 */
class GoodsDetailsActivity : BaseActivity<GoodsDetailsPresent>(), GoodsDetailsContract.View {
    private var goodsIntegral: String? = null
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goodsdetails)
        setBlackTitle("商品详情")
        title_right.setOnClickListener(onClick)
        tv_submit.setOnClickListener(onClick)
        intent.getParcelableExtra<GoodsListBean.GoodsListData>("goods_inf")?.run {
            //Glide.with(this@GoodsDetailsActivity).load(PjjApplication.integralFilePath + goodsPicture).into(iv_goods)
            XspManage.getInstance().integralGoods.goodsPicture = goodsPicture
            tv_goods_name.text = goodsName
            XspManage.getInstance().integralGoods.describe = goodsName
            this@GoodsDetailsActivity.goodsIntegral = goodsIntegral.toString()
            XspManage.getInstance().integralGoods.integral = goodsIntegral.toString()
            tv_integral.text = "${goodsIntegral.toInt()}金币"
            XspManage.getInstance().integralGoods.postCost = postCost
            tv_freight.text = "运费：${postCost}元"
            tv_goods_describe.text = goodsDescribe
            var paths = ArrayList<String>()
            var names = ArrayList<String>()
            if (TextUtils.isNotEmptyList(inseralGoodsDescribeList)) {
                inseralGoodsDescribeList!!.forEachIndexed { index, it ->
                    paths.add(PjjApplication.integralFilePath + it.goodsPicture)
                    names.add(index.toString())
                }
            } else {
                paths.add(PjjApplication.integralFilePath + goodsPicture)
                names.add("1")
            }
            initBanner(paths, names)
        }
        title_right.setOnClickListener(onClick)
        tv_submit.setOnClickListener(onClick)
        mPresent = GoodsDetailsPresent(this)
        mPresent?.loadIntegralRule()
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.title_right -> startActivity(Intent(this, MyIntegralActivity::class.java))
            R.id.tv_submit -> goodsIntegral?.let {
                showWaiteStatue()
                mPresent?.loadCheckIntegral(it)
            }
        }
    }

    override fun passCheckIntegral() {
        cancelWaiteStatue()
        startActivity(Intent(this, SureOrderActivity::class.java))
    }

    override fun updateIntegralRule(text: String?) {
        tv_explain.text = text
    }

    private fun initBanner(list_path: ArrayList<String>, list_title: ArrayList<String>) {
        iv_goods.setImageLoader(object : ImageLoader() {
            override fun displayImage(context: Context, path: Any, imageView: ImageView) {
                Glide.with(context).load(path as String).into(imageView)
            }
        })
        var numIndicator = iv_goods.findViewById<TextView>(com.youth.banner.R.id.numIndicator)
        var viewGroup = numIndicator.parent as ViewGroup
        viewGroup.removeView(numIndicator)
        var params = FrameLayout.LayoutParams(ViewUtils.getDp(R.dimen.dp_58), ViewUtils.getDp(R.dimen.dp_29), Gravity.BOTTOM)
        params.leftMargin = ViewUtils.getDp(R.dimen.dp_278)
        params.bottomMargin = ViewUtils.getDp(R.dimen.dp_19)
        numIndicator.layoutParams = params
        numIndicator.background = ViewUtils.getDrawable(R.drawable.shape_000_70_bg_19)
        iv_goods.addView(numIndicator)
        iv_goods.setBannerStyle(BannerConfig.NUM_INDICATOR)
        //设置指示器的位置，小点点，左中右。
        iv_goods.setIndicatorGravity(BannerConfig.CENTER)
        //设置图片网址或地址的集合
        iv_goods.setImages(list_path)
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        iv_goods.setBannerAnimation(Transformer.Default)
        //设置轮播图的标题集合
        iv_goods.setBannerTitles(list_title)
        //设置轮播间隔时间
        iv_goods.setDelayTime(3000)
        //设置是否为自动轮播，默认是“是”。
        iv_goods.isAutoPlay(true)
        //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
        //iv_goods.setOnBannerListener { }
        //必须最后调用的方法，启动轮播图。
        iv_goods.start()
    }

    override fun getStatusBarColor(): Int {
        return Color.WHITE
    }
}
