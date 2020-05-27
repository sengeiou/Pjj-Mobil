package com.pjj.view.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.pjj.R
import com.pjj.module.BianMinBean
import com.pjj.module.xspad.XspManage
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils

/**
 * Created by XinHeng on 2018/12/04.
 * describe：
 */
class BianMianAdapter(private var list: List<BianMinBean.DataBean>? = null) : BaseAdapter() {
    var selectPosition = -1
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var color = ViewUtils.getColor(R.color.color_444444)
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var holder: ViewHolder
        if (convertView == null) {
            holder = ViewHolder()
            convertView = LayoutInflater.from(parent.context).inflate(R.layout.layout_list_bian_min_item, parent, false).apply {
                holder.ll_title = findViewById(R.id.ll_title)
                holder.tv_preview = findViewById<TextView>(R.id.tv_preview).apply {
                    setOnClickListener(onClickListener)
                }
                holder.tv_title = findViewById(R.id.tv_title)
                holder.tv_content = findViewById(R.id.tv_content)
                holder.iv_select = this.findViewById(R.id.iv_select)
                setOnClickListener(onClickListener)
            }
            convertView.tag = holder
        } else {
            holder = convertView.tag as ViewHolder
        }
        if (position == selectPosition) {
            holder.tv_preview?.visibility = View.VISIBLE
            holder.ll_title?.background = ColorDrawable(ViewUtils.getColor(R.color.color_theme))
            holder.tv_title?.setTextColor(Color.WHITE)
            holder.iv_select?.setImageResource(R.mipmap.select_white)
        } else {
            holder.tv_preview?.visibility = View.GONE
            holder.ll_title?.background = ColorDrawable(Color.TRANSPARENT)
            holder.tv_title?.setTextColor(color)
            holder.iv_select?.setImageResource(R.mipmap.unselect)
        }
        convertView!!.setTag(R.id.position, position)
        holder.tv_preview?.tag = position
        holder.tv_content?.text = list!![position].info
        holder.tv_title?.text = TextUtils.clean(list!![position].title)
        return convertView!!
    }

    override fun getItem(position: Int): Any {
        return list?.get(position)!!
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list?.size ?: 0
    }

    private var onClickListener = View.OnClickListener {
        if (it.tag is Int) {
            when (it.id) {
                R.id.tv_preview -> onItemSelectListener?.preview(list!![selectPosition].info)
            }
        } else {
            var position = it.getTag(R.id.position) as Int
            if (selectPosition != position) {
                selectPosition = position
                XspManage.getInstance().orderContent = list!![selectPosition].peopleInfoId
                notifyDataSetChanged()
            }
        }
    }

    private inner class ViewHolder {
        internal var iv_select: ImageView? = null
        internal var ll_title: View? = null
        internal var tv_title: TextView? = null
        internal var tv_preview: TextView? = null
        internal var tv_content: TextView? = null
    }

    var onItemSelectListener: OnItemSelectListener? = null

    interface OnItemSelectListener {
        fun preview(content: String)
    }
}