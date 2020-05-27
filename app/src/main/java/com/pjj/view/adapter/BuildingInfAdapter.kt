package com.pjj.view.adapter

import android.graphics.drawable.shapes.RoundRectShape
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.module.BuildingBean

/**
 * Created by XinHeng on 2018/12/03.
 * describe：
 */
class BuildingInfAdapter(var localHidden: Boolean = false) : RecyclerView.Adapter<BuildingInfAdapter.BuildingInfHolder>() {
    var list: MutableList<BuildingBean.CommunityListBean>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingInfAdapter.BuildingInfHolder {
        return LayoutInflater.from(parent.context).inflate(R.layout.layout_elevator_area_inf, parent, false).run {
            setOnClickListener(onClickListener)
            BuildingInfHolder(this)
        }.apply {
            if (localHidden) {
                tv_local.visibility = View.GONE
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> 1
            else -> 1
        }
    }

    override fun getItemCount(): Int {
        return (list?.size ?: 0) + 0
    }

    private var requestOptions = RequestOptions().error(R.mipmap.building)
    override fun onBindViewHolder(holder: BuildingInfAdapter.BuildingInfHolder, position: Int) {
        holder.itemView.tag = position
        list!![position].run {
            Glide.with(holder.iv_building).load(PjjApplication.filePath + imgName).apply(requestOptions).into(holder.iv_building)
            holder.tv_building.text = communityName
            holder.tv_local.text = this.position
            holder.tv_elevator_num.text = "$elevatorNum 部电梯"
            holder.tv_xsp_num.text = "$screenNum 面屏幕"
            if (price == null) {
                holder.tv_xsp_price.visibility = View.GONE
                holder.tv_xsp_price_.visibility = View.GONE

            } else {
                holder.tv_xsp_price.text = "$price 元/小时"
                holder.tv_xsp_price.visibility = View.VISIBLE
                holder.tv_xsp_price_.visibility = View.VISIBLE
            }
        }
    }

    class BuildingInfHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv_building = view.findViewById<ImageView>(R.id.iv_building1)
        var tv_building = view.findViewById<TextView>(R.id.tv_building)
        var tv_local = view.findViewById<TextView>(R.id.tv_local)
        var tv_elevator_num = view.findViewById<TextView>(R.id.tv_elevator_num)
        var tv_xsp_num = view.findViewById<TextView>(R.id.tv_xsp_num)
        var tv_xsp_price = view.findViewById<TextView>(R.id.tv_xsp_price)
        var tv_xsp_price_ = view.findViewById<TextView>(R.id.tv_xsp_price_)
    }

    var onItemSelectListener: OnItemSelectListener? = null
    private var onClickListener = View.OnClickListener {
        var i = it.tag as Int
        list!![i].run {
            var toIntOrNull = 0
            try {
                toIntOrNull = screenNum.toInt()
            } catch (e: Exception) {
            }

            if (toIntOrNull > 0) {
                onItemSelectListener?.itemClick(communityId, communityName)
            } else {
                onItemSelectListener?.noXsp()
            }
        }
    }

    interface OnItemSelectListener {
        fun itemClick(communityId: String, communityName: String)
        fun noXsp()
    }
}