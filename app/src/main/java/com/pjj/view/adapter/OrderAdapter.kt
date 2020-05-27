package com.pjj.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Outline
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pjj.R
import com.pjj.module.OrderResultBean
import com.pjj.utils.DateUtils
import com.pjj.utils.Log
import com.pjj.utils.ViewUtils
import java.util.*

/**
 * Created by XinHeng on 2018/12/07.
 * describe：
 */
abstract class OrderAdapter(protected var context: Context) : RecyclerView.Adapter<OrderAdapter.OrderHolder>() {
    var list: MutableList<OrderResultBean.DataBean>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun addMore(addList: MutableList<OrderResultBean.DataBean>?) {
        addList?.let {
            if (null != list) {
                val count = itemCount
                val changeCount = it.size
                list!!.addAll(it)
                notifyItemRangeChanged(count, changeCount)
            } else {
                list = it
            }
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        return OrderHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_order_list_item, parent, false).apply {
            outlineProvider = object : ViewOutlineProvider() {
                override fun getOutline(view: View, outline: Outline) {
                    //Log.e("TAG", ": measuredWidth=${view.measuredWidth}")
                    outline.setRoundRect(0, 0, view.measuredWidth, view.measuredHeight, ViewUtils.getFDp(R.dimen.dp_3))
                }
            }
            clipToOutline = true
        }).apply {
            tv_left_order.setOnClickListener(onClick)
            tv_right_order.setOnClickListener(onClick)
            tv_tuiguang.setOnClickListener(onClick)
            tv_order_statue.setTextColor(getOrderStatueColor())
        }
    }

    private var op = RequestOptions().error(R.mipmap.building).diskCacheStrategy(DiskCacheStrategy.ALL)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        list!![position].run {
            with(holder) {
                Glide.with(context).load(imgSrc).apply(op).into(iv_building)
                tv_building.text = name
                tv_start_date_name.text = "$dateName："
                tv_building_more.text = when (communityNum) {
                    0 -> ""
                    1 -> "(等${communityNum}小区)"
                    else -> "(等${communityNum}小区)"
                }
                tv_release_name.text = releaseName
                tv_type.text = screenType
                tv_order_time.text = "订单时间：${DateUtils.getSf1(Date(createTime))}"
                val rightStatueTitle = if (getOrderStatue() == "订单发布中") {
                    if ("1" == isPlay) getOrderStatue() else "待发布"
                } else if ("订单已结束" == getOrderStatue()) {
                    "订单" + when (statusX) {
                        "0" -> "待审核"
                        "1" -> "审核未通过"
                        "2" -> "发布中"
                        "4" -> "审核过期"
                        "5" -> "被撤回"
                        "6" -> "待支付"
                        "7" -> "支付过期"
                        "8" -> "主动取消"
                        else -> "已结束" //3
                    }
                } else getOrderStatue()
                tv_order_statue.text = rightStatueTitle
                tv_start_date.text = playDate
                if ("广告传媒发布" == releaseName || "电梯传媒发布" == releaseName) {
                    tv_start_time.visibility = View.GONE
                    tv_start_time_name.visibility = View.GONE
                } else {
                    tv_start_time.visibility = View.VISIBLE
                    tv_start_time_name.visibility = View.VISIBLE
                    tv_start_time.text = releaseTime
                }
                tv_left_order.text = getLeftText()
                tv_right_order.text = getRightText()
                tv_left_order.setTag(R.id.position, position)
                tv_right_order.setTag(R.id.position, position)
                tv_tuiguang.setTag(R.id.position, position)

                if ((getLeftText() == "再次发布" || "监 播" == getLeftText()) && (("广告传媒发布" == releaseName || "电梯传媒发布" == releaseName))) {
                    tv_tuiguang.visibility = View.VISIBLE
                    tv_tuiguang.text = when (isTop) {
                        "1" -> "已推广"
                        else -> "推广"
                    }
                } else {
                    tv_tuiguang.visibility = View.GONE
                }
                if (isJianBo())
                    if (isPlay == "0") {
                        tv_left_order.isEnabled = false
                        tv_left_order.setTextColor(ViewUtils.getColor(R.color.color_d2d2d2))
                    } else {
                        tv_left_order.setTextColor(ViewUtils.getColor(R.color.color_777777))
                        tv_left_order.isEnabled = true
                    }
            }
        }
    }

    abstract fun getOrderStatue(): String
    abstract fun getOrderStatueColor(): Int
    abstract fun getLeftText(): String
    abstract fun getRightText(): String
    abstract fun getOnLeftClick(date: OrderResultBean.DataBean)
    open fun getOnRightClick(date: OrderResultBean.DataBean) {
        onItemClickListener?.rightClick(date.orderId, date.orderType, date.isPlay)
    }

    protected open fun isJianBo(): Boolean {
        return false
    }

    protected open fun tuiGuang(date: OrderResultBean.DataBean) {

    }

    private var onClick = View.OnClickListener {
        var position = it.getTag(R.id.position) as Int
        var dataBean = list!![position]
        when (it.id) {
            R.id.tv_left_order -> getOnLeftClick(dataBean)
            R.id.tv_right_order -> getOnRightClick(dataBean)
            R.id.tv_tuiguang -> tuiGuang(dataBean)
        }
    }

    class OrderHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv_building = view.findViewById<ImageView>(R.id.iv_building1)!!
        var tv_type = view.findViewById<TextView>(R.id.tv_type)!!
        var tv_building = view.findViewById<TextView>(R.id.tv_building)!!
        var tv_building_more = view.findViewById<TextView>(R.id.tv_building_more)!!

        var tv_release_name = view.findViewById<TextView>(R.id.tv_release_name)!!

        var tv_order_time = view.findViewById<TextView>(R.id.tv_order_time)!!
        var tv_order_statue = view.findViewById<TextView>(R.id.tv_order_statue)!!

        var tv_start_date = view.findViewById<TextView>(R.id.tv_start_date)!!
        var tv_start_time = view.findViewById<TextView>(R.id.tv_start_time)!!
        var tv_start_time_name = view.findViewById<TextView>(R.id.tv_start_time_name)!!

        var tv_left_order = view.findViewById<TextView>(R.id.tv_left_order)
        var tv_right_order = view.findViewById<TextView>(R.id.tv_right_order)

        var tv_start_date_name = view.findViewById<TextView>(R.id.tv_start_date_name)
        var tv_tuiguang = view.findViewById<TextView>(R.id.tv_tuiguang)
    }

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun leftClick()
        fun rightClick(orderId: String, type: String, isPlay: String)
    }
}