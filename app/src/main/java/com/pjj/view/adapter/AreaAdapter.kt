package com.pjj.view.adapter

import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.pjj.R
import com.pjj.module.AreaBean
import com.pjj.utils.ViewUtils

/**
 * Created by XinHeng on 2018/12/19.
 * describe：
 */
class AreaAdapter : RecyclerView.Adapter<AreaAdapter.AreaHolder>() {
    var list: MutableList<AreaBean.CountyListBean>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaHolder {
        return AreaHolder(TextView(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_38))
            setPadding(ViewUtils.getDp(R.dimen.dp_37), 0, 0, 0)
            gravity = Gravity.CENTER_VERTICAL
            setTextColor(ViewUtils.getColor(R.color.color_444444))
            setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_12))
            setOnClickListener(onClickListener)
        })
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: AreaHolder, position: Int) {
        list!![position].run {
            holder.tv.text = areaName
            holder.tv.tag = position
        }
    }

    private var onClickListener = View.OnClickListener {
        var position = it.tag as Int
        var countyListBean = list!![position]
        onItemClickListener?.itemClick(countyListBean.areaCode, countyListBean.areaName)
    }
    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun itemClick(areaCode: String, areaName: String)
    }

    class AreaHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv = view as TextView
    }
}