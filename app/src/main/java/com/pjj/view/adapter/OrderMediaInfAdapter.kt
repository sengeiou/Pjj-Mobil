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
import com.pjj.module.MediaOrderInfBean
import com.pjj.utils.CalculateUtils
import com.pjj.utils.TextUtils

/**
 * Created by XinHeng on 2019/04/19.
 * describe：
 */
class OrderMediaInfAdapter(private var comeFromFragment: Boolean = false) : RecyclerView.Adapter<OrderMediaInfAdapter.OrderMediaParentHolder>() {
    var list: MutableList<SelectReleaseAreaAllAdapter.OrderAllInfParent>? = null
    //private var showQi=true
    private var bottomView: View? = null
    private var topView: View? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderMediaParentHolder {
        return when (viewType) {
            0 -> OrderMediaInfHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_media_order_inf_item, parent, false)).apply {
                //tv_look.setOnClickListener(onClick)
            }
            -1 -> OrderMediaParentHolder(topView!!)
            else -> OrderMediaParentHolder(bottomView!!)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            position == 0 && hasTop() -> -1
            position == itemCount - 1 && hasBottom() -> 1
            else -> 0
        }
        //return if (position == itemCount - 1 && hasBottom()) 1 else 0
    }

    private fun hasBottom(): Boolean {
        return bottomView != null
    }

    private fun hasTop(): Boolean {
        return topView != null
    }

    override fun getItemCount(): Int {
        return (list?.size ?: 0) + (if (hasBottom()) 1 else 0) + (if (hasTop()) 1 else 0)
    }

    fun addBottomView(view: View) {
        bottomView = view
        notifyDataSetChanged()
    }

    fun addTopAddBottomView(viewTop: View, viewBottom: View) {
        this.topView = viewTop
        this.bottomView = viewBottom
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: OrderMediaParentHolder, position: Int) {
        when (holder) {
            is OrderMediaInfHolder -> {
                list!![position - (if (hasTop()) 1 else 0)].run {
                    Glide.with(holder.itemView).load(PjjApplication.filePath + getBuildingImageName()).into(holder.iv)
                    holder.tv_build_name.text = getBuildingName()
                    setTvText(holder.tv_screen_count, getScreenCount(), if (comeFromFragment) "屏幕数量：" else "室内屏数量：")
                    setTvText(holder.tv_elevator_screen_count, if (comeFromFragment) 0 else getElevatorCount(), "电梯屏数量：")
                    val price = CalculateUtils.m1(this.getPrice())
                    holder.tv_price.text = "¥${price} 起"
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setTvText(tv: TextView, count: Int, head: String) {
        if (count > 0) {
            tv.visibility = View.VISIBLE
            tv.text = "$head${count}面"
        } else {
            tv.visibility = View.GONE
        }
    }

    open class OrderMediaParentHolder(view: View) : RecyclerView.ViewHolder(view)
    class OrderMediaInfHolder(view: View) : OrderMediaParentHolder(view) {
        var iv = view.findViewById<ImageView>(R.id.iv_building)
        var tv_build_name = view.findViewById<TextView>(R.id.tv_building_name)
        var tv_screen_count = view.findViewById<TextView>(R.id.tv_screen_count)
        var tv_price = view.findViewById<TextView>(R.id.tv_price)
        var tv_elevator_screen_count = view.findViewById<TextView>(R.id.tv_elevator_screen_count)
    }
}