package com.pjj.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.pjj.R
import com.pjj.module.BuildingBean
import com.pjj.module.xspad.XspManage

/**
 * Created by XinHeng on 2018/12/12.
 * describe：
 */
class RandomAreaAdapter : BaseAdapter() {
    var list: MutableList<BuildingBean.CommunityListBean>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        var holder: MyHolder
        if (null == convertView) {
            holder = MyHolder()
            convertView = LayoutInflater.from(parent.context).inflate(R.layout.area_xsp_elevator_count_item, parent, false).apply {
                holder.tv_local = findViewById(R.id.tv_local)
                holder.tv_xsp_num = findViewById(R.id.tv_xsp_num)
                holder.tv_delete = findViewById(R.id.tv_delete)
                holder.tv_delete!!.setOnClickListener(onClickListener)
            }
            convertView.tag = holder
        } else {
            holder = convertView.tag as MyHolder
        }
        holder.tv_delete!!.tag = position
        list!![position].run {
            holder.tv_local!!.text = communityName
            holder.tv_xsp_num!!.text = "屏幕x$screenNum"
        }
        return convertView!!
    }

    override fun getItem(position: Int): Any {
        return list!![position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return list?.size ?: 0
    }

    private var onClickListener = View.OnClickListener {
        XspManage.getInstance().buildList.removeAt(it.tag as Int)
        onDeleteItemListener?.deleteItem()
    }
    var onDeleteItemListener: OnDeleteItemListener? = null

    interface OnDeleteItemListener {
        fun deleteItem()
    }

    class MyHolder {
        var tv_local: TextView? = null
        var tv_xsp_num: TextView? = null
        var tv_delete: TextView? = null
    }
}
