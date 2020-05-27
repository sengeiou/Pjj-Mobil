package com.pjj.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.pjj.R
import com.pjj.module.NewMediaScreenBean
import com.pjj.module.xspad.XspManage

/**
 * Created by XinHeng on 2019/03/27.
 * describe：
 */
class ScreenMediaAdapter : RecyclerView.Adapter<ScreenMediaAdapter.ScreenMediaHolder>() {
    var list: MutableList<NewMediaScreenBean.DataBean.ScreenListBean>? = null
    private var selectPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenMediaHolder {
        return ScreenMediaHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_screen_media_item, parent, false)).apply {
            itemView.setOnClickListener(onClickListener)
        }
    }

    private var onClickListener = View.OnClickListener {
        var position = it.tag as Int
        if (selectPosition != position) {
            var sele_ = selectPosition
            selectPosition = position
            notifyItemChanged(sele_)
            notifyItemChanged(selectPosition)
            var bean = list!![position]
            XspManage.getInstance().newMediaData.buildingName = bean.screenName
            XspManage.getInstance().newMediaData.screenId = bean.screenId
            XspManage.getInstance().newMediaData.price = bean.mediaPrice * bean.finalMediaDiscount
            onScreenMediaAdapterListener?.hasSelect()
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    fun getSelectInf(): NewMediaScreenBean.DataBean.ScreenListBean? {
        if (selectPosition == -1) {
            return null
        }
        return list!![selectPosition]
    }

    override fun onBindViewHolder(holder: ScreenMediaHolder, position: Int) {
        holder.tvName.text = list!![position].screenName
        holder.itemView.tag = position
        if (position == selectPosition) {
            holder.iv.setImageResource(R.mipmap.select)
        }
    }

    class ScreenMediaHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvName = itemView.findViewById<TextView>(R.id.tv_name)!!
        var iv = itemView.findViewById<ImageView>(R.id.iv)!!
    }

    var onScreenMediaAdapterListener: OnScreenMediaAdapterListener? = null

    interface OnScreenMediaAdapterListener {
        fun hasSelect()
    }
}