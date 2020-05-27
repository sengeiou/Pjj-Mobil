package com.pjj.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pjj.R
import com.pjj.module.MyReleaseBean
import com.pjj.utils.DateUtils
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.layout_my_release_item.view.*
import java.util.*

class MyReleaseAdapter(private var context: Context) : ListViewAdapter<MyReleaseBean.DataBean, MyReleaseAdapter.MyReleaseHolder>() {
    private val onClick = View.OnClickListener {
        val position = it.getTag(R.id.position) as Int
        val bean = list!![position]
        when (it.id) {
            R.id.tv_delete -> {
                onMyReleaseAdapterListener?.delete(bean)
            }
            R.id.tv_pre -> {
                onMyReleaseAdapterListener?.pre(bean)
            }
            R.id.tv_right -> {
                when (bean.statusX) {
                    "2" -> onMyReleaseAdapterListener?.error(bean)
                    "3" -> onMyReleaseAdapterListener?.recover(bean)
                    else -> onMyReleaseAdapterListener?.tuiGuang(bean)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyReleaseHolder {
        return MyReleaseHolder(LayoutInflater.from(context).inflate(R.layout.layout_my_release_item, parent, false).apply {
            tv_delete.setOnClickListener(onClick)
            tv_pre.setOnClickListener(onClick)
            tv_right.setOnClickListener(onClick)
        })
    }

    private val requestOptions = RequestOptions().error(R.mipmap.ic_launcher)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyReleaseHolder, position: Int) {
        val bean = list!![position]
        var videoTag = false
        when (bean.orderType) {
            "3" -> {
                videoTag = true
                holder.tv_type.text = "外部视频链接"
                holder.iv_video_tag.visibility = View.GONE
                Glide.with(context).load(R.mipmap.video_pre_tag).apply(requestOptions).into(holder.iv)
            }
            "2" -> {
                holder.tv_type.text = "文字发布"
                holder.iv_video_tag.visibility = View.GONE
                Glide.with(context).load(bean.filePath).apply(requestOptions).into(holder.iv)
            }
            else -> {
                Glide.with(context).load(bean.filePath).apply(requestOptions).into(holder.iv)
                holder.tv_type.text = if (bean.fileType == "1") "图片发布" else "视频发布"
                holder.iv_video_tag.visibility = if (bean.fileType == "1") View.GONE else View.VISIBLE
            }
        }
        holder.tv_release_name.text = bean.title
        holder.tv_release_no.text = "订单编号：" + bean.freeOrderId
        holder.tv_release_time.text = "发布时间：" + DateUtils.getSfPoint(Date(bean.createTime))
        val topTag = bean.isTop == "1"
        if (topTag) {
            holder.tv_release_statue.text = "推广中"
            holder.tv_release_statue.setTextColor(ViewUtils.getColor(R.color.color_ff4c4c))
        }
        when (bean.statusX) {
            "1" -> {
                if (!topTag) {
                    holder.tv_release_statue.text = "已发布"
                    holder.tv_release_statue.setTextColor(ViewUtils.getColor(R.color.color_999999))
                }
                holder.tv_delete.visibility = View.VISIBLE
                if (videoTag || topTag) {
                    holder.tv_right.visibility = View.GONE
                    holder.tv_pre.background = ViewUtils.getDrawable(R.drawable.shape_theme_bg_3)
                    holder.tv_pre.setTextColor(Color.WHITE)
                } else {
                    holder.tv_right.visibility = View.VISIBLE
                    holder.tv_right.text = "推广"
                }
            }
            "2" -> {
                if (!topTag) {
                    holder.tv_release_statue.text = "违规下架"
                    holder.tv_release_statue.setTextColor(ViewUtils.getColor(R.color.color_fe8024))
                }
                holder.tv_delete.visibility = View.GONE
                holder.tv_pre.background = ViewUtils.getDrawable(R.drawable.shape_theme_side_3)
                holder.tv_pre.setTextColor(ViewUtils.getColor(R.color.color_theme))
                holder.tv_right.visibility = View.VISIBLE
                holder.tv_right.text = "查看原因"
            }
            else -> {
                if (!topTag) {
                    holder.tv_release_statue.text = "已删除"
                    holder.tv_release_statue.setTextColor(ViewUtils.getColor(R.color.color_999999))
                }
                holder.tv_delete.visibility = View.GONE
                holder.tv_pre.background = ViewUtils.getDrawable(R.drawable.shape_theme_side_3)
                holder.tv_pre.setTextColor(ViewUtils.getColor(R.color.color_theme))
                holder.tv_right.visibility = View.VISIBLE
                holder.tv_right.text = "恢复"
            }
        }
        holder.tv_delete.setTag(R.id.position, position)
        holder.tv_pre.setTag(R.id.position, position)
        holder.tv_right.setTag(R.id.position, position)
    }

    class MyReleaseHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_type = itemView.findViewById<TextView>(R.id.tv_type)
        var tv_release_statue = itemView.findViewById<TextView>(R.id.tv_release_statue)
        var tv_release_name = itemView.findViewById<TextView>(R.id.tv_release_name)
        var tv_release_no = itemView.findViewById<TextView>(R.id.tv_release_no)
        var tv_release_time = itemView.findViewById<TextView>(R.id.tv_release_time)

        var iv = itemView.findViewById<ImageView>(R.id.iv)
        var iv_video_tag = itemView.findViewById<ImageView>(R.id.iv_video_tag)

        var tv_delete = itemView.findViewById<TextView>(R.id.tv_delete)
        var tv_pre = itemView.findViewById<TextView>(R.id.tv_pre)
        var tv_right = itemView.findViewById<TextView>(R.id.tv_right)
    }

    var onMyReleaseAdapterListener: OnMyReleaseAdapterListener? = null

    interface OnMyReleaseAdapterListener {
        fun pre(bean: MyReleaseBean.DataBean)
        fun delete(bean: MyReleaseBean.DataBean)
        fun error(bean: MyReleaseBean.DataBean)
        fun recover(bean: MyReleaseBean.DataBean)
        fun tuiGuang(bean: MyReleaseBean.DataBean)
    }
}