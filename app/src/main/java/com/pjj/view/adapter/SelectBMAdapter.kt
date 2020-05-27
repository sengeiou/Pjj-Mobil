package com.pjj.view.adapter

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.ViewUtils
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.pjj.R
import com.pjj.module.BmTypeBean

/**
 * Created by XinHeng on 2019/01/18.
 * describe：拼屏便民类型选择
 */
class SelectBMAdapter : RecyclerView.Adapter<SelectBMAdapter.SelectBmHolder>() {
    private val dp14 = com.pjj.utils.ViewUtils.getDp(R.dimen.dp_14)
    private val dp41 = com.pjj.utils.ViewUtils.getDp(R.dimen.dp_41)
    private val sp12 = com.pjj.utils.ViewUtils.getFDp(R.dimen.sp_12)
    var list: MutableList<BmTypeBean.DataBean>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var onClickListener = View.OnClickListener {
        var text = (it as TextView).text.toString()
        onBMSelectListener?.itemClick(text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectBmHolder {
        return SelectBmHolder(TextView(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, dp41)
            setPadding(dp14, 0, 0, 0)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, sp12)
            gravity = Gravity.CENTER_VERTICAL
            setOnClickListener(onClickListener)
            setTextColor(com.pjj.utils.ViewUtils.getColor(R.color.color_333333))
        })
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: SelectBmHolder, position: Int) {
        holder.tv.text = list!![position].dicName
    }

    class SelectBmHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv: TextView = view as TextView
    }

    var onBMSelectListener: OnBMSelectListener? = null

    interface OnBMSelectListener {
        fun itemClick(name: String)
    }
}