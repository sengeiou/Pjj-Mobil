package com.pjj.view.adapter

import android.annotation.SuppressLint
import android.graphics.Outline
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.module.GoodsListBean
import com.pjj.module.xspad.XspManage
import com.pjj.utils.TextUtils
import com.pjj.view.custom.PriceTextView

/**
 * Created by XinHeng on 2019/04/04.
 * describe：
 */
class IntegralMallAdapter(private var mallTag: Boolean) : ListViewAdapter<GoodsListBean.GoodsListData, IntegralMallAdapter.IntegralMallHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntegralMallHolder {
        return IntegralMallHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_mall_item, parent, false)).apply {
            itemView.setOnClickListener(onClick)
        }
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: IntegralMallHolder, position: Int) {
        list!![position].run {
            Glide.with(holder.itemView).load(PjjApplication.integralFilePath + goodsPicture).into(holder.iv)
            holder.tv_name.text = goodsName
            if (mallTag) {
                holder.tv_integral.setPrice(goodsIntegral)
            } else {
                holder.tv_integral.text = "${getInt(goodsIntegral)}金币"
            }
            holder.itemView.setTag(R.id.position, position)
        }
    }

    private fun getInt(price: Float): String {
        val intPrice = price.toInt()
        val i = price - intPrice
        return if (i == 0f) intPrice.toString() else price.toString()
    }

    private var onClick = View.OnClickListener {
        var position = it.getTag(R.id.position) as Int
        val bean = list!![position]
        XspManage.getInstance().integralGoods.integraGoodsId = bean.integraGoodsId
        onIntegralMallAdapterListener?.itemClick(bean.goodsPicture!!, bean)
    }

    class IntegralMallHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv = itemView.findViewById<ImageView>(R.id.iv)
        var tv_name = itemView.findViewById<TextView>(R.id.tv_name)
        var tv_integral = itemView.findViewById<PriceTextView>(R.id.tv_integral)
    }

    var onIntegralMallAdapterListener: OnIntegralMallAdapterListener? = null

    interface OnIntegralMallAdapterListener {
        fun itemClick(ivPath: String, goods: GoodsListBean.GoodsListData)
    }
}