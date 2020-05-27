package com.pjj.view.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.module.ScreenManageOrderListBean
import com.pjj.utils.DateUtils
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import java.util.*

class ScreenManageOrderListAdapter : RecyclerView.Adapter<ScreenManageOrderListAdapter.ScreenManageOrderListHolder>() {
    private var deletePosition = -1
    var list: MutableList<ScreenManageOrderListBean.DataBean>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenManageOrderListHolder {
        return ScreenManageOrderListHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_screen_manage_order_item, parent, false)).apply {
            tv_preview.setOnClickListener(onClick)
            tv_delete.setOnClickListener(onClick)
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ScreenManageOrderListHolder, position: Int) {
        holder.tv_preview.setTag(R.id.position, position)
        holder.tv_delete.setTag(R.id.position, position)
        list!![position].run {
            Glide.with(holder.itemView).load(PjjApplication.filePath + fileName).into(holder.iv_screen)
            holder.tv_create_date.text = "创建时间：" + DateUtils.getSfPoint(Date(createTime))
            if (type != "1") {
                holder.tv_release_date.visibility = View.VISIBLE
                holder.tv_release_date.text = "发布日期：$playDate"
            } else {
                holder.tv_release_date.visibility = View.GONE
            }
            holder.tv_building.text =  orderName
            if (fileType == "1") {
                holder.iv_video.visibility = View.GONE
            } else {
                holder.iv_video.visibility = View.VISIBLE
            }
            if ("2" != status) {
                holder.tv_delete.setTextColor(ViewUtils.getColor(R.color.color_d2d2d2))
            } else {
                holder.tv_delete.setTextColor(ViewUtils.getColor(R.color.color_666666))
            }
        }
    }

/*    private fun getSizeScreen(size: Int): String {
        return if (size > 1) {
            "等" + size + "面屏幕"
        } else {
            ""
        }
    }*/

    fun deleteOrder() {
        list!!.removeAt(deletePosition)
        notifyItemRemoved(deletePosition)
        notifyItemRangeChanged(deletePosition, itemCount - deletePosition)
    }

    fun deleteAll() {
        list = null
    }

    private val onClick = View.OnClickListener {
        val position = it.getTag(R.id.position) as Int
        val bean = list!![position]
        when (it.id) {
            R.id.tv_delete -> {
                if ("2" == bean.status) {
                    deletePosition = position
                    onScreenManageOrderListAdapterListener?.deleteOrder(bean.ownOrderId)
                }
            }
            R.id.tv_preview -> {
                onScreenManageOrderListAdapterListener?.preview(bean.fileType, PjjApplication.filePath + bean.fileName, bean.type)
            }
        }
    }

    class ScreenManageOrderListHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv_screen = view.findViewById<ImageView>(R.id.iv_screen)
        var iv_video = view.findViewById<ImageView>(R.id.iv_video)
        var tv_building = view.findViewById<TextView>(R.id.tv_building)
        var tv_release_date = view.findViewById<TextView>(R.id.tv_release_date)
        var tv_create_date = view.findViewById<TextView>(R.id.tv_create_date)

        var tv_delete = view.findViewById<TextView>(R.id.tv_delete)
        var tv_preview = view.findViewById<TextView>(R.id.tv_preview)
    }

    var onScreenManageOrderListAdapterListener: OnScreenManageOrderListAdapterListener? = null

    interface OnScreenManageOrderListAdapterListener {
        fun deleteOrder(orderId: String)
        fun preview(mediaType: String, path: String, orderType: String)
    }
}