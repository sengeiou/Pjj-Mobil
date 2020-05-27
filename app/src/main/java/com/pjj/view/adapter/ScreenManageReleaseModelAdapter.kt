package com.pjj.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.module.ScreenModelBean

class ScreenManageReleaseModelAdapter : RecyclerView.Adapter<ScreenManageReleaseModelAdapter.ScreenManageReleaseModelHolder>() {
    init {
        setHasStableIds(true)
    }

    var list: MutableList<ScreenModelBean.DataBean>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var selectId = ""
    private var selectPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenManageReleaseModelHolder {
        return ScreenManageReleaseModelHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_screen_manage_release_model_item, parent, false)).apply {
            iv.setOnClickListener(onClick)
            tv_select.setOnClickListener(onClick)
            tv_delete.setOnClickListener(onClick)
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun getItemId(position: Int): Long {
        return list!![position].hashCode().toLong()
    }

    override fun onBindViewHolder(holder: ScreenManageReleaseModelHolder, position: Int) {
        val bean = list!![position]
        Glide.with(holder.itemView).load(PjjApplication.filePath + bean.fileName).into(holder.iv)
        holder.iv.setTag(R.id.position, position)
        holder.tv_select.setTag(R.id.position, position)
        holder.tv_delete.setTag(R.id.position, position)
        if (bean.partnerFileId == selectId) {
            selectPosition = position
            holder.iv_select.visibility = View.VISIBLE
        } else {
            holder.iv_select.visibility = View.GONE
        }
        if (bean.fileType == "1") {
            holder.iv_video.visibility = View.GONE
        } else {
            holder.iv_video.visibility = View.VISIBLE
        }
    }

    private val onClick = View.OnClickListener {
        val positon = it.getTag(R.id.position) as Int
        when (it.id) {
            R.id.tv_select, R.id.iv -> {
                if (positon == selectPosition) {
                    selectPosition = -1
                    selectId = ""
                    notifyItemChanged(positon)
                    onScreenManageReleaseModelListener?.select(null)
                    return@OnClickListener
                }
                if (list!![positon].partnerFileId == selectId) {
                    selectPosition = positon
                    return@OnClickListener
                }
                val old = selectPosition
                selectPosition = positon
                selectId = list!![positon].partnerFileId
                if (old >= 0)
                    notifyItemChanged(old)
                notifyItemChanged(selectPosition)
                onScreenManageReleaseModelListener?.select(selectId)
            }
            R.id.tv_delete -> {
                selectPosition = -1
                onScreenManageReleaseModelListener?.delete(list!![positon])
            }
        }
    }

    class ScreenManageReleaseModelHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv = view.findViewById<ImageView>(R.id.iv)
        var iv_select = view.findViewById<ImageView>(R.id.iv_select)
        var iv_video = view.findViewById<ImageView>(R.id.iv_video)
        var tv_select = view.findViewById<TextView>(R.id.tv_select)
        var tv_delete = view.findViewById<TextView>(R.id.tv_delete)
    }

    var onScreenManageReleaseModelListener: OnScreenManageReleaseModelListener? = null

    interface OnScreenManageReleaseModelListener {
        fun delete(bean: ScreenModelBean.DataBean)
        fun select(partnerFileId: String?)
    }
}