package com.pjj.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pjj.R
import com.pjj.module.UserTempletBean
import com.pjj.module.xspad.XspManage
import com.pjj.utils.BitmapUtils

/**
 * Created by XinHeng on 2018/11/29.
 * describe：
 */
class TemplateAdapter(private var context: Context) : RecyclerView.Adapter<TemplateAdapter.TemplateViewHolder>() {
    var selectPosition: Int = -1
    private var mList: List<UserTempletBean.DataBean>? = null
    /**
     * 更新标志，防止重复加载图片
     */
    //private var updateCount = 0

    fun setList(list: List<UserTempletBean.DataBean>) {
        this.mList = list
        /*var templateId = XspManage.getInstance().templateId
        if (!TextUtils.isEmpty(templateId) && TextUtils.isNotEmptyList(list)) {
            list.forEachIndexed { index, it ->
                if (it.templet_id == templateId) {
                    selectPosition = index
                    XspManage.getInstance().orderConten = templateId
                    return@forEachIndexed
                }
            }
        }*/
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateViewHolder {
        var context = parent.context
        return LayoutInflater.from(context).inflate(R.layout.layout_template_item, parent, false).run {
            var tv_preview = findViewById<View>(R.id.tv_preview)
            tv_preview.setOnClickListener(onClick)
            setOnClickListener(onClick)
            TemplateViewHolder(this)
        }
    }

    override fun getItemCount(): Int {
        return mList?.size ?: 0
    }

    private var requestOptions = RequestOptions().error(R.mipmap.audit_icon)
    override fun onBindViewHolder(holder: TemplateViewHolder, position: Int) {
        holder.tv_preview.setTag(R.id.group_position, position)
        holder.itemView.setTag(R.id.group_position, position)
        if (position == selectPosition) {
            holder.tv_preview.visibility = View.VISIBLE
            holder.iv_play.visibility = View.VISIBLE
        } else {
            holder.tv_preview.visibility = View.GONE
            holder.iv_play.visibility = View.GONE
        }
        /*if (updateCount > 0) {
            --updateCount
            Log.e("TAG", "updateCount=$updateCount")
            return
        }*/
        mList?.get(position)?.run {
            holder.tv_explain.text = templetName
            var glide = Glide.with(this@TemplateAdapter.context)
            when (templetType) {
                "1" -> {
                    glide.load(fileList[0].fileUrl).apply(requestOptions).into(holder.iv)
                    holder.iv_video.visibility = View.GONE
                }
                else -> {
                    holder.iv_video.visibility = View.VISIBLE
                    BitmapUtils.loadFirstImageForVideo(glide, fileList[0].fileUrl, holder.iv)
                }
            }
            return@run
        }
    }

    private var onClick = View.OnClickListener {
        var position = it.getTag(R.id.group_position) as Int
        when (it.id) {
            R.id.tv_preview -> {
                //TODO 文件下载路径 跳转
                mList?.get(position)?.run {
                    onPreviewListener?.preview(fileList[0].fileUrl, templetType)
                }
            }
            else -> {
                if (position != selectPosition) {
                    XspManage.getInstance().orderContent = mList!![position].templet_id
                    var selectPosition1 = selectPosition
                    selectPosition = position
                    if (selectPosition1 != -1) {
                        notifyItemChanged(selectPosition1)
                    }
                    notifyItemChanged(selectPosition)
                }
            }
        }
    }

    class TemplateViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var iv: ImageView = itemView.findViewById<ImageView>(R.id.iv)
        var tv_explain: TextView = itemView.findViewById(R.id.tv_explain)
        var tv_preview: TextView = itemView.findViewById(R.id.tv_preview)
        var iv_play: ImageView = itemView.findViewById(R.id.iv_play)
        var iv_video: ImageView = itemView.findViewById(R.id.iv_video)
    }

    var onPreviewListener: OnPreviewListener? = null

    interface OnPreviewListener {
        fun preview(path: String, type: String)
    }
}