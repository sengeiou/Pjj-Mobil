package com.pjj.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pjj.R

/**
 * Created by XinHeng on 2019/03/18.
 * describe：
 */
class ImageTextAdapter: RecyclerView.Adapter<ImageTextAdapter.ImageTextHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageTextHolder {
        return ImageTextHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_img_text,parent,false))
    }

    override fun getItemCount(): Int {
        return 0
    }

    override fun onBindViewHolder(holder: ImageTextHolder, position: Int) {
        holder.tv.text="模板${position+1}"
    }

    class ImageTextHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv = view.findViewById<ImageView>(R.id.iv)!!
        var tv = view.findViewById<TextView>(R.id.tv)!!
    }
}