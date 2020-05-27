package com.pjj.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.module.UserTempletBean
import com.pjj.utils.Log
import com.pjj.utils.ViewUtils
import com.pjj.view.custom.TitleView

class ZhiDingAdapter(private var context: Context, private var zhiDingTag: Boolean = false) : ListViewAdapter<UserTempletBean.DataBean, RecyclerView.ViewHolder>() {
    var topView: View? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val context = context
        return when (viewType) {
            -1 -> ZhiDingTuiJianHolder(TextView(context).apply {
                text = "推荐搜索："
                gravity = Gravity.CENTER_VERTICAL
                setPadding(ViewUtils.getDp(R.dimen.dp_23), 0, 0, 0)
                setTextColor(ViewUtils.getColor(R.color.color_theme))
                setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_14))
                layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_40))
            })
            -2 -> ZhiDingEmptyHolder(LayoutInflater.from(context).inflate(R.layout.layout_empty_zhi_ding_item, parent, false))
            1 -> ZhiDingOneHolder(LayoutInflater.from(context).inflate(R.layout.layout_one_zhi_ding_item, parent, false)).apply {
                itemView.setOnClickListener(onClick)
            }
            -3 -> ZhiDingTuiJianHolder(topView!!)
            3 -> ZhiDingLinkHolder(LayoutInflater.from(context).inflate(R.layout.layout_one_zhi_ding_item, parent, false)).apply {
                itemView.setOnClickListener(onClick)
                iv.setImageResource(R.mipmap.video_pre_tag)
            }
            else -> ZhiDingTwoHolder(LayoutInflater.from(context).inflate(R.layout.layout_tow_zhi_ding_item, parent, false)).apply {
                itemView.setOnClickListener(onClick)
            }
        }
    }

    private val onClick = View.OnClickListener {
        val position = it.getTag(R.id.position) as Int - tag()
        val bean = list!![position]
        onZhiDingItemClickListener?.itemClick(bean)
    }

    override fun getItemCount(): Int {
        return (list?.size ?: 0) + (if (hasTop()) 1 else 0)
    }

    private fun hasTop(): Boolean {
        return null != topView
    }

    override fun getItemViewType(position: Int): Int {
        if (hasTop() && position == 0) {
            return -3
        }
        val position = position - tag()
        val bean = list!![position]
        return when {
            null == bean.templetName -> -2
            bean.templetName == "recommended" && bean.templet_id == "recommended" -> -1
            bean.templetType == "6" -> 3
            /*bean.templetType == "3" &&*/ bean.fileList != null && bean.fileList.size == 2 -> 0
            else -> 1
        }
    }

    private fun tag(): Int {
        return if (hasTop()) 1 else 0
    }

    private val requestOptions = RequestOptions().error(R.mipmap.media_error)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0 && hasTop()) {
            return
        }
        var index = position - tag()
        val bean = list!![index]
        bean.fileList
        holder.itemView.setTag(R.id.position, position)
        //Log.e("TAG", "${bean == null}")
        when (holder) {
            is ZhiDingOneHolder -> {
                holder.tv_name.text = bean.templetName
                Glide.with(context).load(bean.fileList[0].fileUrl).apply(requestOptions).into(holder.iv)
                if (zhiDingTag) {
                    holder.iv_zhiding_tag.visibility = if (bean.isTop == "1") View.VISIBLE else View.GONE
                }
            }
            is ZhiDingLinkHolder -> {
                holder.tv_name.text = bean.templetName
                if (zhiDingTag) {
                    holder.iv_zhiding_tag.visibility = if (bean.isTop == "1") View.VISIBLE else View.GONE
                }
            }
            is ZhiDingTwoHolder -> {
                holder.tv_name.text = bean.templetName
                Glide.with(context).load(bean.fileList[0].fileUrl).apply(requestOptions).into(holder.ivFirst)
                Glide.with(context).load(bean.fileList[1].fileUrl).apply(requestOptions).into(holder.ivSecond)
                if (zhiDingTag) {
                    holder.iv_zhiding_tag.visibility = if (bean.isTop == "1") View.VISIBLE else View.GONE
                }
            }
        }
    }

    class ZhiDingEmptyHolder(view: View) : RecyclerView.ViewHolder(view)
    class ZhiDingTuiJianHolder(view: View) : RecyclerView.ViewHolder(view)
    class ZhiDingLinkHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv = itemView.findViewById<ImageView>(R.id.iv)
        var tv_name = itemView.findViewById<TextView>(R.id.tv_name)
        var iv_zhiding_tag = itemView.findViewById<ImageView>(R.id.iv_zhiding_tag)
    }

    class ZhiDingOneHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv = itemView.findViewById<ImageView>(R.id.iv)
        var tv_name = itemView.findViewById<TextView>(R.id.tv_name)
        var iv_zhiding_tag = itemView.findViewById<ImageView>(R.id.iv_zhiding_tag)
    }

    class ZhiDingTwoHolder(view: View) : RecyclerView.ViewHolder(view) {
        var ivFirst = itemView.findViewById<ImageView>(R.id.ivFirst)
        var ivSecond = itemView.findViewById<ImageView>(R.id.ivSecond)
        var tv_name = itemView.findViewById<TextView>(R.id.tv_name)
        var iv_zhiding_tag = itemView.findViewById<ImageView>(R.id.iv_zhiding_tag)
    }

    var onZhiDingItemClickListener: OnZhiDingItemClickListener? = null

    interface OnZhiDingItemClickListener {
        fun itemClick(bean: UserTempletBean.DataBean)
    }
}