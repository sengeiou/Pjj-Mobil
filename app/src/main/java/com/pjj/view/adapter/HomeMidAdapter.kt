package com.pjj.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.module.PicBean

/**
 * Created by hank on 18-9-21.
 */
class HomeMidAdapter(var context: Context) : RecyclerView.Adapter<HomeMidAdapter.MyHolder>() {
    var list: MutableList<PicBean.DataBean.`_$1Bean`>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var onClickListener = View.OnClickListener {
        var position = it.getTag(R.id.position) as Int
        onHomeMidListener?.itemClick(list!![position].pictureName, list!![position].linkUrl)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(LayoutInflater.from(parent.context).inflate(R.layout.h_item_home_mid, parent, false)).apply {
            iv.setOnClickListener(onClickListener)
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.iv.setTag(R.id.position, position)
        var fileName = list!![position].fileName
        if (!fileName.startsWith("file:///android")) {
            fileName = PjjApplication.filePath + fileName
        }
        Glide.with(context).load(fileName).into(holder.iv)
    }

    class MyHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv = view.findViewById<ImageView>(R.id.img)!!
    }

    var onHomeMidListener: OnHomeMidListener? = null

    interface OnHomeMidListener {
        fun itemClick(title: String, linkUrl: String)
    }
}