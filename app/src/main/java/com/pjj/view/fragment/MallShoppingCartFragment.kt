package com.pjj.view.fragment

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

import com.pjj.R
import com.pjj.contract.MallShoppingCartContract
import com.pjj.module.ShopCarBean
import com.pjj.module.xspad.XspManage
import com.pjj.present.MallShoppingCartPresent
import com.pjj.utils.*
import com.pjj.view.activity.MallSureOrderActivity
import com.pjj.view.adapter.ShopCarAdapter
import com.pjj.view.custom.ItemSlideRecycleView
import com.pjj.view.pulltorefresh.BaseRefreshListener
import kotlinx.android.synthetic.main.fragment_mallshoppingcart.*

/**
 * Create by xinheng on 2019/05/08 16:16。
 * describe：商城购物车
 */
class MallShoppingCartFragment : BaseFragment<MallShoppingCartPresent>(), MallShoppingCartContract.View {

    override fun getLayoutRes(): Int {
        return R.layout.fragment_mallshoppingcart
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        titleView.setTextMiddle("购物车", Color.BLACK)
        rv_shop_car.addItemDecoration(SpaceItemDecoration(activity, LinearLayoutManager.VERTICAL, ViewUtils.getDp(R.dimen.dp_5), ViewUtils.getColor(R.color.color_f1f1f1)))
        rv_shop_car.layoutManager = LinearLayoutManager(activity)
        rv_shop_car.adapter = mallAdapter
        iv_all_select.setOnClickListener(onClick)
        tv_submit.setOnClickListener(onClick)
        pullToRefresh.setCanLoadMore(false)
        pullToRefresh.setRefreshListener(object : BaseRefreshListener {
            override fun refresh() {
                mPresent?.loadShopCarList()
            }

            override fun loadMore() {}

        })
        rv_shop_car.itemAnimator = null
        rv_shop_car.onItemSlideRecycleListener = object : ItemSlideRecycleView.OnItemSlideRecycleListener {
            override fun getRealCanSlideMenuView(view: View): ViewGroup? {
                return null
            }

            override fun getItemRealTouchMuneView(parent: ViewGroup, x: Int, y: Int): ViewGroup? {
                val viewGroup = parent.findViewById<LinearLayout>(R.id.ll_parent)
                val frame = Rect()
                viewGroup.getHitRect(frame)
                if (!frame.contains(x, y)) {
                    return null
                }
                val childCount = viewGroup.childCount
                for (i in childCount - 1 downTo 0) {
                    val child = viewGroup.getChildAt(i)
                    if (child.visibility == View.VISIBLE && child is ViewGroup) {
                        child.getHitRect(frame)
                        if (frame.contains(x, y)) {
                            //当前触碰的view
                            if (child.childCount == 2) {
                                (0 until child.childCount).forEach {
                                    var childAt = child.getChildAt(it)
                                    //Log.e("TAG", "getItemRealTouchMuneView chid $childAt")
                                }
                                return child
                            }
                        }
                    }
                }
                return null
            }

        }
        mPresent = MallShoppingCartPresent(this).apply {
            loadShopCarList()
        }
        ViewUtils.setHtmlText(tv_all_price, "合计：" + ViewUtils.getHtmlText("¥0.00", "#FF4C4C"))
    }

    override fun onResume() {
        if (XspManage.getInstance().refreshTag.mallGoodsRefreshTag) {
            mPresent?.loadShopCarList()
            XspManage.getInstance().refreshTag.mallGoodsRefreshTag = false
        }
        super.onResume()
    }

    private val onClick = View.OnClickListener {
        when (it.id) {
            R.id.iv_all_select -> {
                mallAdapter.changeAllSelectStatue()
                iv_all_select.setImageResource(if (mallAdapter.allTag) R.mipmap.select else R.mipmap.unselect)
            }
            R.id.tv_submit -> {
                val json = mallAdapter.getSelectedGoods()
                if (TextUtils.isEmpty(json)) {
                    showNotice("您还没有选择商品")
                } else {
                    mPresent?.loadCheckGoodsStockTask(json!!)
                }
            }
        }
    }

    override fun checkPass(json: String) {
        // 购买物品
        XspManage.getInstance().integralGoods.integraGoodsId = json
        startActivity(Intent(activity, MallSureOrderActivity::class.java)
                .putExtra("shoppingCar", true))
    }

    override fun updateShopCar(list: MutableList<ShopCarBean.DataBean>?) {
        cancelWaiteStatue()
        pullToRefresh.finishRefresh()
        mallAdapter.list = list
        if (TextUtils.isNotEmptyList(list)) {
            ll_no_data.visibility = View.GONE
            ll_bottom.visibility = View.VISIBLE
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            mPresent?.loadShopCarList()
        }
        super.onHiddenChanged(hidden)
    }

    override fun addSuccess() {
        mallAdapter.notifyCount()
    }

    override fun refresh() {
        pullToRefresh.autoRefresh()
    }

    override fun deleteSuccess() {
        rv_shop_car.closeMenu()
        mallAdapter.notifyDelete()
    }

    override fun showNotice(msg: String?) {
        pullToRefresh.finishRefresh()
        super.showNotice(msg)
    }

    private var mallAdapter = ShopCarAdapter().apply {
        onShopCarListener = object : ShopCarAdapter.OnShopCarListener {
            override fun notice(msg: String) {
                showNotice(msg)
            }

            override fun isAll(select: Boolean) {
                iv_all_select.setImageResource(if (select) R.mipmap.select else R.mipmap.unselect)
            }

            @SuppressLint("SetTextI18n")
            override fun updatePrice(price: Float, post: Float) {
                val m1 = CalculateUtils.m1(post)
                XspManage.getInstance().integralGoods.postCost = m1
                tv_post.text = "(含运费：¥$m1)"
                val sumPrice = CalculateUtils.m1(price + post)
                XspManage.getInstance().integralGoods.integral = sumPrice
//                tv_all_price.text = "¥$sumPrice"
                ViewUtils.setHtmlText(tv_all_price, "合计：" + ViewUtils.getHtmlText("¥$sumPrice", "#FF4C4C"))
            }

            override fun changeCount(shoppingCartId: String, goodsNum: Int, groupPosition: Int, childPosition: Int, tag: Boolean) {
                mPresent?.loadAddShopCar(shoppingCartId, tag, goodsNum)
            }

            override fun delete(shoppingCartId: String) {
                mPresent?.loadDeleteShopCarGoods(shoppingCartId)
            }

            override fun noData() {
                ll_no_data.visibility = View.VISIBLE
                ll_bottom.visibility = View.GONE
            }
        }
    }
}
