package com.pjj.view.adapter

import android.annotation.SuppressLint
import android.media.Image
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.module.DiyDataBean
import com.pjj.module.MallOrderListBean
import com.pjj.module.xspad.XspManage
import com.pjj.utils.CalculateUtils
import com.pjj.utils.Log

class MallOrderAdapter(private var payTag: Int = 0) : ListViewAdapter<MallOrderListBean.DataBean, MallOrderAdapter.MallOrderHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MallOrderHolder {
        return MallOrderHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_mall_goods_list_item, parent, false)).apply {
            when (payTag) {//0待支付 1待收货 3已完成
                0 -> {
                    tv_cancel_order.setOnClickListener(onClick)
                    tv_pay.setOnClickListener(onClick)
                }
                1 -> {
                    tv_pay.text = "确认收货"
                    tv_cancel_order.visibility = View.GONE
                    tv_pay.visibility = View.GONE
                    tv_pay.setOnClickListener(onClick)
                }
                else -> {
                    tv_pay.text = "删除订单"
                    tv_pay.setOnClickListener(onClick)
                    tv_cancel_order.visibility = View.GONE
                    tv_pay.visibility = View.VISIBLE
                }
            }
            itemView.setOnClickListener(onClick)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MallOrderHolder, position: Int) {
        holder.tv_cancel_order.setTag(R.id.position, position)
        holder.tv_pay.setTag(R.id.position, position)
        holder.itemView.tag = position
        list!![position].run {
            //0待支付 1未发货 2已发货 3已完成  4订单取消
            holder.tv_statue.text = when (statusX) {
                "0" -> {
                    "待支付"
                }
                "1" -> {
                    holder.tv_pay.visibility = View.GONE
                    "待发货"
                }
                "2" -> {
                    holder.tv_pay.visibility = View.VISIBLE
                    "待收货"
                }
                "3" -> {
                    holder.tv_pay.visibility = View.GONE
                    "已完成"
                }
                else -> {
                    holder.tv_pay.visibility = View.VISIBLE
                    "订单取消"
                }
            }
            holder.tv_sum_price.text = "合计：¥${CalculateUtils.m1(goodsPrice)}"
            holder.tv_freight.text = "运费：${postage}元"
            addChildView(holder.parentView, goodsOrderDetail)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun addChildView(parent: ViewGroup, goodsList: MutableList<MallOrderListBean.DataBean.GoodsOrderDetailBean>?) {
        if (parent.childCount > 0) {
            parent.removeAllViews()
        }
        val oneTag = (goodsList?.size ?: 0) == 1
        goodsList?.forEach {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_mall_goods_list_child_item, parent, false)
            val imageView = view.findViewById<ImageView>(R.id.iv_goods)
            Glide.with(parent).load(PjjApplication.integralFilePath + it.goodsPicture).into(imageView)
            view.findViewById<TextView>(R.id.tv_goods_name).text = it.goodsName
            //view.findViewById<TextView>(R.id.tv_describe).text=it.goodsName
            view.findViewById<TextView>(R.id.tv_goods_num).text = "x" + it.goodsNumber
            if (oneTag) {
                view.findViewById<TextView>(R.id.tv_price).visibility = View.GONE
            } else {
                view.findViewById<TextView>(R.id.tv_price).text = "¥" + CalculateUtils.m1(it.goodsPrice)
            }
            parent.addView(view)
        }
    }

    private val onClick = View.OnClickListener {
        val tag = it.getTag(R.id.position)
        if (tag !is Int) {
            val i = it.tag as Int
            onMallOrderAdapterListener?.itemClick(list!![i].goodOrderId, list!![i].storeId)
            return@OnClickListener
        }
        Log.e("TAG", "onClick: tag=$tag")
        val bean = list!![tag]
        when (it.id) {
            R.id.tv_cancel_order -> {
                onMallOrderAdapterListener?.cancelOrder(bean.goodOrderId, bean.storeId)
            }
            R.id.tv_pay -> {
                if (bean.statusX == "1") {
                    onMallOrderAdapterListener?.notice("请等待卖家发货")
                    return@OnClickListener
                }
                if (bean.statusX == "4") {
                    onMallOrderAdapterListener?.deleteOrder(bean.goodOrderId, bean.storeId)
                    return@OnClickListener
                }
                //XspManage.getInstance().integralGoods.integraGoodsId="{\\\"${bean.storeId}\\\":\\\"${bean.goodsId}&${bean.specificId}&${bean.goodsNum}\\\"}"
                onMallOrderAdapterListener?.payNow(bean.goodOrderId, bean.storeId, CalculateUtils.m1(bean.goodsPrice))
            }
        }
    }

    class MallOrderHolder(view: View) : RecyclerView.ViewHolder(view) {
        var parentView = view.findViewById<LinearLayout>(R.id.ll_parent)!!
        var tv_statue = view.findViewById<TextView>(R.id.tv_goods_statue)
        var tv_cancel_order = view.findViewById<TextView>(R.id.tv_cancel_order)
        var tv_pay = view.findViewById<TextView>(R.id.tv_pay)
        var tv_freight = view.findViewById<TextView>(R.id.tv_freight)
        var tv_sum_price = view.findViewById<TextView>(R.id.tv_sum_price)
    }

    var onMallOrderAdapterListener: OnMallOrderAdapterListener? = null

    interface OnMallOrderAdapterListener {
        fun cancelOrder(goodOrderId: String, storeId: String)
        fun deleteOrder(goodOrderId: String, storeId: String)
        fun payNow(goodOrderId: String, storeId: String, allPrice: String)
        fun itemClick(goodOrderId: String, storeId: String)
        fun notice(msg: String)
    }
}