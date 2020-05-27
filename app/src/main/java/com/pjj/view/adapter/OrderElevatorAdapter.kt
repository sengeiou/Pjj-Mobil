package com.pjj.view.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.pjj.R
import com.pjj.module.OrderElevatorBean
import com.pjj.module.OrderInfBean
import com.pjj.view.dialog.DayHoursDialog

/**
 * Created by XinHeng on 2018/12/08.
 * describe：
 */
class OrderElevatorAdapter(private var c: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var head: View? = null

    var list: MutableList<OrderElevatorBean>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            -10 -> object : RecyclerView.ViewHolder(head) {}
            1 -> OrderHeadHolder(LayoutInflater.from(c).inflate(R.layout.layout_order_elevator_head, parent, false))
            else -> OrderScreenHolder(LayoutInflater.from(c).inflate(R.layout.layout_order_elevator_screen, parent, false)).apply {
                tv_inf.setOnClickListener(click)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return -10
        }
        return list!![position + (if (head == null) 0 else -1)].level
    }

    override fun getItemCount(): Int {
        return (list?.size ?: 0) + (if (head == null) 0 else 1)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var position = position
        if (head != null) {
            if (position == 0) {
                return
            } else {
                position -= 1
            }
        }
        list!![position].run {
            when (holder) {
                is OrderHeadHolder -> {
                    holder.tv_local.text = title
                    holder.tv_xsp_num.text = "屏幕 x$childSize"
                }
                is OrderScreenHolder -> {
                    holder.tv_screen.text = title
                    holder.tv_inf.tag = position
                }
            }
        }
    }

    private var click = View.OnClickListener {
        var position = it.tag as Int
        var objects = list!![position].objects
        objects?.run {
            if (this is OrderInfBean.OrderScreenListBean.ElevatorListBean.ScreenListBean) {
                daysHoursDialog.updateContent(screenName, dayHours)
            }
        }
    }

    class OrderHeadHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_local = view.findViewById<TextView>(R.id.tv_local)!!
        var tv_xsp_num = view.findViewById<TextView>(R.id.tv_xsp_num)!!
    }

    class OrderScreenHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_inf = view.findViewById<TextView>(R.id.tv_inf)!!
        var tv_screen = view.findViewById<TextView>(R.id.tv_screen)!!
    }

    private val daysHoursDialog by lazy {
        DayHoursDialog(c)
    }
}