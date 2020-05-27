package com.pjj.view.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.pjj.R
import com.pjj.module.AreaBean
import com.pjj.utils.ViewUtils

/**
 * Created by XinHeng on 2018/12/01.
 * describe：
 */
class AreaListAdapter() : RecyclerView.Adapter<AreaListAdapter.AreaViewHolder>() {
    private var dataList: List<AreaBean.CountyListBean>? = null

    fun setList(dataList: List<AreaBean.CountyListBean>) {
        this.dataList = dataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaViewHolder {

        return AreaViewHolder(TextView(parent.context).apply {
            setTextColor(Color.BLACK)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_13))
            setOnClickListener(onClick)
        })
    }

    override fun getItemCount(): Int {
        return dataList?.size ?: 0
    }

    override fun onBindViewHolder(holder: AreaViewHolder, position: Int) {
        dataList?.let {
            holder.textView.text = it[position].areaName
        }
    }

    class AreaViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var textView = view as TextView
    }

    private var onClick = View.OnClickListener {
        var position = it.tag as Int
        dataList?.let { it ->
            onClickListener?.itemClick(it[position].areaCode)
        }
    }
    var onClickListener: OnClickListener? = null

    interface OnClickListener {
        fun itemClick(cityCode: String)
    }
}