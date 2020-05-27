package com.pjj.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pjj.R
import com.pjj.module.MyTuiGuangBean
import com.pjj.utils.CalculateUtils
import com.pjj.utils.DateUtils
import com.pjj.utils.ViewUtils
import java.util.*

class MyTuiGuangAdapter(private var context: Context) : ListViewAdapter<MyTuiGuangBean.DataBean, MyTuiGuangAdapter.MyTuiGuangHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyTuiGuangHolder {
        val inflater = LayoutInflater.from(context)
        return when (viewType) {
            1 -> MyTuiGuangTowHolder(inflater.inflate(R.layout.layout_my_tui_guang_two_item, parent, false))
            else -> MyTuiGuangOneHolder(inflater.inflate(R.layout.layout_my_tui_guang_item, parent, false))
        }.apply {
            tv_right.setOnClickListener(onClick)
            tv_pre.setOnClickListener(onClick)
            tv_cancel.setOnClickListener(onClick)
        }
    }

    private val options = RequestOptions().error(R.mipmap.ic_launcher)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyTuiGuangHolder, position: Int) {
        val bean = list!![position]
        val templet = bean.templet
        holder.tv_name.text = templet.templetName
        val timeLength = bean.dataTimeLong
        val sfPoint = DateUtils.getSfPoint(Date(bean.orderStartTime))
        holder.tv_time_length.text = "置顶时长：" + when (timeLength > 1) {
            false -> sfPoint + "(${timeLength}天)"
            else -> sfPoint + "...(${timeLength}天)"
        }
        holder.tv_create_date.text = "创建时间：${DateUtils.getSfPoint(Date(bean.createTime))}"
        holder.tv_sum_price.text = "总价：¥${CalculateUtils.m1(bean.price)}"
        when (bean.statusX) {
            "0" -> {
                holder.tv_right.text = "去支付"
                holder.tv_cancel.visibility = View.GONE
                updateTv(holder.tv_left, "待支付", R.color.color_ff4c4c, R.color.white)
            }
            "1" -> {
                holder.tv_right.text = "续时推广"
                holder.tv_cancel.visibility = View.VISIBLE
                updateTv(holder.tv_left, "推广中", R.color.color_ff4c4c, R.color.white)
            }
            "2" -> {
                holder.tv_right.text = "再次推广"
                holder.tv_cancel.visibility = View.GONE
                updateTv(holder.tv_left, "已结束", R.color.color_999999, R.color.white)
            }
            "3" -> {
                holder.tv_right.text = "重新下单"
                holder.tv_cancel.visibility = View.GONE
                updateTv(holder.tv_left, "已取消", R.color.color_999999, R.color.white)
            }
            "5"->{
                holder.tv_right.text = "重新下单"
                holder.tv_cancel.visibility = View.GONE
                updateTv(holder.tv_left, "已取消", R.color.color_999999, R.color.white)
            }
            else -> {
                holder.tv_right.text = "查看原因"
                holder.tv_cancel.visibility = View.GONE
                updateTv(holder.tv_left, "违规下架", R.color.color_999999, R.color.white)
            }
        }
        val fileList = templet.fileList
        when (holder) {
            is MyTuiGuangTowHolder -> {
                Glide.with(context).load(fileList[0].fileUrl).apply(options).into(holder.ivFirst)
                Glide.with(context).load(fileList[1].fileUrl).apply(options).into(holder.ivSecond)
            }
            is MyTuiGuangOneHolder -> Glide.with(context).load(fileList[0].fileUrl).apply(options).into(holder.iv)
        }
        holder.tv_right.setTag(R.id.position, position)
        holder.tv_pre.setTag(R.id.position, position)
        holder.tv_cancel.setTag(R.id.position, position)
    }

    override fun getItemViewType(position: Int): Int {

        val bean = list!![position]
        return when (bean.templet.fileList?.size ?: 0) {
            2 -> 1
            else -> 0
        }
    }

    private fun updateTv(tv: TextView, text: String, colorR: Int, colorBgR: Int) {
        tv.setTextColor(ViewUtils.getColor(colorR))
        tv.background = createBg(ViewUtils.getColor(colorBgR))
        tv.text = text
    }

    private fun createBg(color: Int): Drawable {
        return GradientDrawable().apply {
            setColor(color)
            cornerRadius = ViewUtils.getFDp(R.dimen.dp_3)
        }
    }

    open class MyTuiGuangHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_name = view.findViewById<TextView>(R.id.tv_name)
        var tv_time_length = view.findViewById<TextView>(R.id.tv_time_length)
        var tv_create_date = view.findViewById<TextView>(R.id.tv_create_date)
        var tv_sum_price = view.findViewById<TextView>(R.id.tv_sum_price)
        var tv_left = view.findViewById<TextView>(R.id.tv_left)
        var tv_right = view.findViewById<TextView>(R.id.tv_right)
        var tv_cancel = view.findViewById<TextView>(R.id.tv_cancel)
        var tv_pre = view.findViewById<TextView>(R.id.tv_pre)
    }

    class MyTuiGuangOneHolder(view: View) : MyTuiGuangHolder(view) {
        var iv = view.findViewById<ImageView>(R.id.iv)
    }

    class MyTuiGuangTowHolder(view: View) : MyTuiGuangHolder(view) {
        var ivFirst = view.findViewById<ImageView>(R.id.ivFirst)
        var ivSecond = view.findViewById<ImageView>(R.id.ivSecond)
    }

    private val onClick = View.OnClickListener {
        val position = it.getTag(R.id.position) as Int
        val bean = list!![position]
        when (it.id) {
            R.id.tv_pre -> onMyTuiGuangAdapterListener?.pre(bean)
            R.id.tv_cancel -> onMyTuiGuangAdapterListener?.cancel(bean)
            R.id.tv_right -> {
                when (bean.statusX) {
                    "0" -> {
                        val size = bean.communityList?.size ?: 0
                        val buildName = if (size > 0) bean.communityList[0].communityName else bean.templet.templetName
                        val fileList = bean.templet.fileList
                        val second = if (size >= 2) fileList[1].fileUrl else null
                        onMyTuiGuangAdapterListener?.toPay(bean.price, bean.orderId, bean.topId, bean.topOrderId, fileList[0].fileUrl, second, buildName + getAreaSizeName(size),
                                -1, bean.lastTopOrderTime)
                    }
                    "2", "1" -> {
                        val size = bean.communityList?.size ?: 0
                        val buildName = if (size > 0) bean.communityList[0].communityName else bean.templet.templetName
                        val fileList = bean.templet.fileList
                        val second = if (size >= 2) fileList[1].fileUrl else null
                        onMyTuiGuangAdapterListener?.reLoadOrder(true, fileList[0].fileUrl, second, buildName + getAreaSizeName(size),
                                -1, bean.lastTopOrderTime, bean.orderId)

                    }
                    "5","3" -> {
                        val size = bean.communityList?.size ?: 0
                        val buildName = if (size > 0) bean.communityList[0].communityName else bean.templet.templetName
                        val fileList = bean.templet.fileList
                        val second = if (fileList.size >= 2) fileList[1].fileUrl else null
                        onMyTuiGuangAdapterListener?.reLoadOrder(false, fileList[0].fileUrl, second, buildName + getAreaSizeName(size),
                                -1, bean.lastTopOrderTime, bean.orderId)
                    }
                    else -> {
                        onMyTuiGuangAdapterListener?.showError(bean.revokeMsg)
                    }
                }
            }
        }
    }

    private fun getAreaSizeName(cout: Int): String {
        if (cout == 0) {
            return ""
        }
        return "(等${cout}小区)"
    }

    var onMyTuiGuangAdapterListener: OnMyTuiGuangAdapterListener? = null

    interface OnMyTuiGuangAdapterListener {
        fun toPay(price: Float, orderId: String, topId: String, topOrderId: String?, first: String, second: String?, communityNum: String, releaseTime: Long, createTime: Long)
        fun reLoadOrder(reNext: Boolean, first: String, second: String?, communityNum: String, releaseTime: Long, createTime: Long, orderId: String)
        fun showError(msg: String)
        fun pre(bean: MyTuiGuangBean.DataBean)
        fun cancel(bean: MyTuiGuangBean.DataBean)
    }
}
