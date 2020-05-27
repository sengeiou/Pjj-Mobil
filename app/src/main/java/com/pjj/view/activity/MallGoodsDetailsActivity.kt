package com.pjj.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.pjj.PjjApplication

import com.pjj.R
import com.pjj.contract.MallGoodsDetailsContract
import com.pjj.module.GoodsDetailBean
import com.pjj.module.ShopCarBean
import com.pjj.module.xspad.XspManage
import com.pjj.present.MallGoodsDetailsPresent
import com.pjj.utils.*
import com.pjj.view.custom.TitleView
import com.pjj.view.dialog.GoodsDescribeDialog
import com.pjj.view.dialog.GoodsDetailsSelectDialog
import com.pjj.view.dialog.PhoneDialog
import com.pjj.view.pulltorefresh.BaseRefreshListener
import com.pjj.view.pulltorefresh.view.FooterView
import com.pjj.view.pulltorefresh.view.HeadView
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.activity_mallgoodsdetails.*
import kotlin.math.min

/**
 * Create by xinheng on 2019/05/08 16:16。
 * describe：商品详情
 */
class MallGoodsDetailsActivity : BaseActivity<MallGoodsDetailsPresent>(), MallGoodsDetailsContract.View {
    private var data: GoodsDetailBean.DataBean? = null
    private var goodsId: String? = null
    private var noGoodsTag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mallgoodsdetails)
        titleView = findViewById(R.id.titleView11)
        titleView.setTextMiddle("图文详情", Color.BLACK)
        titleView.setDrawableLeft(ContextCompat.getDrawable(this, R.mipmap.back_icon_white)?.apply {
            setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_ATOP)
        })
        titleView.setOnClickListener(onClick)
        setStatueWhite()
        fl_title.setPadding(0, statue, 0, 0)
        titleView.visibility = View.GONE
        tv_add_shop_car.setOnClickListener(onClick)
        tv_buy.setOnClickListener(onClick)
        iv_finish.setOnClickListener(onClick)
        fl_up_more.setOnClickListener(onClick)
        tv_ke_fu.setOnClickListener(onClick)
        mPresent = MallGoodsDetailsPresent(this)
        val goodsId = savedInstanceState?.getString("goodsId") ?: intent.getStringExtra("goodsId")
        goodsId?.let {
            mPresent?.loadGoodsDetail(it)
        }
        pullToRefreshScroll.setCanRefresh(false)
        pullToRefreshScroll.setRefreshListener(object : BaseRefreshListener {
            override fun refresh() {

            }

            override fun loadMore() {
                pullToRefreshScroll.finishLoadMore()
                fl_title.background = ColorDrawable(Color.WHITE)
                titleView.visibility = View.VISIBLE
                iv_finish.visibility = View.GONE
                pullToRefreshScrollImage.visibility = View.VISIBLE
                pullToRefreshScroll.visibility = View.GONE
            }
        })
        pullToRefreshScroll.setFooterView(object : FooterView {
            val view = TextView(this@MallGoodsDetailsActivity).apply {
                layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_60))
                background = ColorDrawable(ViewUtils.getColor(R.color.color_f4f4f4))
            }

            override fun begin() {
            }

            override fun progress(progress: Float, all: Float) {
            }

            override fun finishing(progress: Float, all: Float) {
            }

            override fun loading() {
            }

            override fun normal() {
            }

            override fun getView(): View {
                return view
            }
        })
        pullToRefreshScrollImage.setCanLoadMore(false)
        pullToRefreshScrollImage.setRefreshListener(object : BaseRefreshListener {
            override fun refresh() {
                pullToRefreshScrollImage.finishRefresh()
                fl_title.background = ColorDrawable(Color.TRANSPARENT)
                titleView.visibility = View.GONE
                iv_finish.visibility = View.VISIBLE
                pullToRefreshScroll.visibility = View.VISIBLE
                pullToRefreshScroll.postDelayed({
                    pullToRefreshScroll.scrollTo(0, 0)
                }, 200)
                pullToRefreshScrollImage.visibility = View.GONE
            }

            override fun loadMore() {

            }
        })
        pullToRefreshScrollImage.setHeaderView(object : HeadView {
            val view = TextView(this@MallGoodsDetailsActivity).apply {
                background = ColorDrawable(ViewUtils.getColor(R.color.color_f4f4f4))
                setTextColor(ViewUtils.getColor(R.color.color_555555))
                gravity = Gravity.CENTER
                setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_12))
                layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_60))
            }

            override fun begin() {
            }

            override fun progress(progress: Float, all: Float) {
                if (progress >= all - 10) {
                    view.text = "释放回到“商品详情"
                } else {
                    view.text = "下拉回到“商品详情"
                }
            }

            override fun finishing(progress: Float, all: Float) {
            }

            override fun loading() {
            }

            override fun normal() {
            }

            override fun getView(): View {
                return view
            }

        })
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putString("goodsId", goodsId)
        super.onSaveInstanceState(outState)
    }

    override fun fitSystemWindow(): Boolean {
        return false
    }

    private fun noGoodsImageView(): ImageView {
        return ImageView(this).apply {
            layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
            setImageResource(R.mipmap.no_goods)
            scaleType = ImageView.ScaleType.FIT_XY
        }
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.iv_finish, R.id.titleView11 -> titleLeftClick()
            R.id.tv_ke_fu -> callDialog.show()
            R.id.fl_up_more -> {
                //先不用点击
            }
            R.id.tv_add_shop_car -> {
//                showGoodsDialog(GoodsDetailsSelectDialog.SHOP_CAR)
                if (noGoodsTag) {
                    showNotice("抱歉,商品库存不足，\n暂不能加入购物车")
                    return
                }
                data?.let {
                    mPresent?.loadAddShopCar(it.goodsId, null, 1)
                }
            }
            R.id.tv_buy -> {
                //showGoodsDialog(GoodsDetailsSelectDialog.BUY_GOODS)
                if (null == data) {
                    return
                }
                if (noGoodsTag) {
                    showNotice("抱歉,商品库存不足，\n暂不能购买")
                    return
                }
                val integralGoods = XspManage.getInstance().integralGoods
                integralGoods.count = 1
                val it = data!!
                //&${it.specificId}
                integralGoods.integraGoodsId = "{\\\"${it.storeId}\\\":\\\"${it.goodsId}&1\\\"}"
                integralGoods.integral = it.finalPrice.toString()
                integralGoods.goods = ArrayList<ShopCarBean.DataBean>().apply {
                    add(ShopCarBean.DataBean().apply {
                        goodsList = ArrayList<ShopCarBean.GoodsListBean>().apply {
                            add(ShopCarBean.GoodsListBean().apply {
                                goodsId = it.goodsId
                                //goodsNum = 1
                                goodsPrice = it.minGoodsPrice
                            })
                        }
                    })
                }
                XspManage.getInstance().integralGoods.postCost = data?.postCost.toString()
                XspManage.getInstance().integralGoods.goodsPicture = data?.goodsPicture
                XspManage.getInstance().integralGoods.describe = data?.goodsName
                XspManage.getInstance().integralGoods.storeId = data?.storeId

                val goods = XspManage.getInstance().integralGoods.goods!!
                goods[0].storeId = data?.storeId
                goods[0].storeName = data?.storeName
                goods[0].goodsList[0].let {
                    it.goodsName = data?.goodsName
                    it.goodsPicture = data?.goodsPicture
                }
                startActivity(Intent(this@MallGoodsDetailsActivity, MallSureOrderActivity::class.java))
            }
        }
    }

    @SuppressLint("SetTextI18n")
    override fun updateGoods(data: GoodsDetailBean) {
        val dataBean = data.data
        this.data = dataBean
        if (dataBean.goodsNumberCount == 0) {
            iv_goods.addView(noGoodsImageView())
            tv_add_shop_car.background = ColorDrawable(ViewUtils.getColor(R.color.color_429af1_40))
            tv_buy.background = ColorDrawable(ViewUtils.getColor(R.color.color_fe8024_40))
            noGoodsTag = true
        } else {
            noGoodsTag = false
        }
        tv_goods_name.text = dataBean.goodsName
        tv_integral.text = "¥${CalculateUtils.m1(dataBean.minGoodsPrice)}"
        var color = "#FE8024"
        val yunfei = when (dataBean.postStatus) {//1包邮 2收费  3运费到付
            "1" -> {
                color = "#666666"
                "包邮"
            }
            "2" -> dataBean.postCost.toString() + "元"
            else -> "到付"
        }
        ViewUtils.setHtmlText(tv_freight, "运费：${ViewUtils.getHtmlText(yunfei, color)}")
        JsonUtils.toJsonArray(dataBean.goodsInfo)?.let {
            fillGoodsIntroduce(it)
        }
        dataBean.goodsDescribeList?.forEach {
            ll_image.addView(createImageView(it))
        }
        dataBean.goodsBannerList?.let {
            val listTitle = ArrayList<String>(it.size)
            it.forEach { _ ->
                listTitle.add("")
            }
            updateBanner(it, listTitle)
        }

    }

    override fun addShopCarSuccess() {
        smillTag = true
        showNotice("添加成功\n在购物车等您")
        smillTag = false
    }

    override fun refresh() {
        goodsId?.let {
            mPresent?.loadGoodsDetail(it)
        }
    }

    private fun updateBanner(list_path: MutableList<String>, list_title: ArrayList<String>) {
        iv_goods.background = ColorDrawable(ViewUtils.getColor(R.color.color_f1f1f1))
        iv_goods.setImageLoader(object : ImageLoader() {
            override fun displayImage(context: Context, path: Any, imageView: ImageView) {
                Glide.with(context).load(PjjApplication.integralFilePath + (path as String)).into(imageView)
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

    private fun fillGoodsIntroduce(jsonArray: JsonArray) {
        goodsDescribeDialog.setJsonArray(jsonArray)
        fillGoods3(jsonArray)
    }

    private fun fillGoods3(jsonArray: JsonArray) {
        val count = jsonArray.size()
        val tagMore = count > 3
        val relCount = min(3, count)
        if (count == 0)
            return
        ll_content3.addView(fillGoods3First(jsonArray[0].asJsonObject, tagMore))
        (1 until relCount).forEach {
            ll_content3.addView(createLine())
            ll_content3.addView(fillGoods3First(jsonArray[it].asJsonObject, false))
        }
    }

    private fun fillGoods3First(jsonObject: JsonObject, tag: Boolean): View {
        return when (tag) {
            false -> createTextView(getStringFromJsonObject(jsonObject, "title") + "：" + getStringFromJsonObject(jsonObject, "content"))
            else -> {
                LinearLayout(this).apply {
                    orientation = LinearLayout.HORIZONTAL
                    addView(createTextView(getStringFromJsonObject(jsonObject, "title") + "：" + getStringFromJsonObject(jsonObject, "content")).apply {
                        val lp = layoutParams as LinearLayout.LayoutParams
                        lp.width = 0
                        lp.weight = 1f
                        layoutParams = lp
                    })
                    addView((ImageView(this@MallGoodsDetailsActivity).apply {
                        layoutParams = LinearLayout.LayoutParams(ViewUtils.getDp(R.dimen.dp_31), LinearLayout.LayoutParams.MATCH_PARENT)
                        setPadding(ViewUtils.getDp(R.dimen.dp_12), ViewUtils.getDp(R.dimen.dp_16), ViewUtils.getDp(R.dimen.dp_12), ViewUtils.getDp(R.dimen.dp_15))
                        setOnClickListener {
                            showGoodsInfDialog()
                        }
                        setImageResource(R.mipmap.right)
                    }))
                }
            }
        }
    }

    private fun getStringFromJsonObject(jsonObject: JsonObject, key: String): String {
        val jsonElement = jsonObject[key]
        return jsonElement?.asString ?: "未知$key"
    }

    private fun showGoodsInfDialog() {
        goodsDescribeDialog.show()
    }

    private val goodsDescribeDialog: GoodsDescribeDialog by lazy {
        GoodsDescribeDialog(this)
    }

    private fun createTextView(text: String): TextView {
        return TextView(this).apply {
            this.text = text
            maxLines = 1
            setTextColor(ViewUtils.getColor(R.color.color_333333))
            gravity = Gravity.CENTER_VERTICAL
            setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_12))
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_43))
        }
    }

    private fun createLine(): View {
        return View(this).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_1))
            background = ColorDrawable(ViewUtils.getColor(R.color.color_eeeeee))
        }
    }

    private fun createImageView(path: String): ImageView {
        return ImageView(this).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            scaleType = ImageView.ScaleType.FIT_CENTER
            Glide.with(this@MallGoodsDetailsActivity).load(PjjApplication.integralFilePath + path).into(this)
        }
    }

    private var goodsDetailsSelectDialog: GoodsDetailsSelectDialog? = null

    private fun showGoodsDialog(type: String) {
        if (null == goodsDetailsSelectDialog) {
            goodsDetailsSelectDialog = GoodsDetailsSelectDialog(this).apply {
                onGoodsDetailsSelectDialogListener = object : GoodsDetailsSelectDialog.OnGoodsDetailsSelectDialogListener {
                    override fun addShopCar(goodsId: String, specificId: String?, goodsNum: Int) {
                        mPresent?.loadAddShopCar(goodsId, specificId, goodsNum)
                    }

                    override fun nowBuy() {
                        XspManage.getInstance().integralGoods.postCost = data?.postCost.toString()
                        XspManage.getInstance().integralGoods.goodsPicture = data?.goodsPicture
                        XspManage.getInstance().integralGoods.describe = data?.goodsName
                        XspManage.getInstance().integralGoods.storeId = data?.storeId
                        val goods = XspManage.getInstance().integralGoods.goods!!
                        goods[0].storeId = data?.storeId
                        goods[0].storeName = data?.storeName
                        goods[0].goodsList[0].let {
                            it.goodsName = data?.goodsName
                            it.goodsPicture = data?.goodsPicture
                        }
                        startActivity(Intent(this@MallGoodsDetailsActivity, MallSureOrderActivity::class.java))
                    }

                    override fun showNotice(msg: String) {
                        this@MallGoodsDetailsActivity.showNotice(msg)
                    }

                }
            }
            data?.let {
                goodsDetailsSelectDialog!!.updateData(it.storeId, it.goodSpecificType, it.goodSpecificInfoAll,
                        it.goodsNumberCount, PjjApplication.integralFilePath + it.goodsPicture,
                        it.goodsName, CalculateUtils.m1(it.minGoodsPrice))
            }
        }
        goodsDetailsSelectDialog!!.updateType(type)
        goodsDetailsSelectDialog!!.show()
    }

    override fun onDestroy() {
        if (callDialog.isShowing) {
            callDialog.dismiss()
        }
        super.onDestroy()
    }

    private val callDialog: PhoneDialog by lazy {
        PhoneDialog(this).apply {
            val phone = "17310588763"
            onCallPhoneDialogListener = object : PhoneDialog.OnCallPhoneDialogListener {
                override fun callClick() {
                    startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("tel:$phone")))
                }
            }
        }
    }
}
