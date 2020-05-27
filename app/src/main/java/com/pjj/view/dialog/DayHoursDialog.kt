package com.pjj.view.dialog

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.pjj.R
import com.pjj.module.ElevatorTimeBean
import com.pjj.utils.ViewUtils
import com.pjj.view.custom.MaxHeightRecyclerView

/**
 * Created by XinHeng on 2018/12/05.
 * describe：
 */
class DayHoursDialog(context: Context, themeResId: Int = 0) : FullWithNoTitleDialog(context, themeResId) {
    private val LESS_COUNT = 4
    private lateinit var recyclerView: MaxHeightRecyclerView
    private lateinit var adapter: DialogAdapter
    private lateinit var tv_xspname: TextView
    private lateinit var iv_more: ImageView
    private lateinit var list: MutableList<ElevatorTimeBean.DayHours>
    override fun getDialogGravity(): Int {
        return Gravity.CENTER
    }

    init {
        var ll = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            var dp15 = ViewUtils.getDp(R.dimen.dp_15)
            setPadding(dp15, 0, dp15, 0)
        }
        ll.addView(ViewUtils.createTextView(context, "时间详情").apply {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_14))
            setTextColor(ViewUtils.getColor(R.color.color_333333))
            setPadding(0, ViewUtils.getDp(R.dimen.dp_19), 0, ViewUtils.getDp(R.dimen.dp_8))
        })
        var dp10 = ViewUtils.getDp(R.dimen.dp_10)
        ll.addView(ViewUtils.createTextView(context, "").apply {
            gravity = Gravity.LEFT
            setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_12))
            setTextColor(ViewUtils.getColor(R.color.color_theme))
            setPadding(0, 0, 0, dp10)
            tv_xspname = this
        })
        adapter = DialogAdapter(null)
        ll.addView(MaxHeightRecyclerView(context).apply {
            layoutManager = LinearLayoutManager(context)
            mMaxHeight=ViewUtils.getDp(R.dimen.dp_500)
            recyclerView = this
            adapter = this@DayHoursDialog.adapter
        })
        ll.addView(ImageView(context).apply {
            iv_more = this
            layoutParams = LinearLayout.LayoutParams(ViewUtils.getDp(R.dimen.dp_38), ViewUtils.getDp(R.dimen.dp_33)).apply {
                gravity = Gravity.CENTER_HORIZONTAL
            }
            setImageDrawable(ViewUtils.getDrawable(R.mipmap.more))
            setPadding(dp10, dp10, dp10, dp10)
            setOnClickListener {
                adapter.update(list)
                this.visibility = View.GONE
            }
        })
        setContentView(ll)
    }

    fun updateContent(xspName: String, list: MutableList<ElevatorTimeBean.DayHours>) {
        tv_xspname.text = xspName
        this.list = list
        if (list.size > 4) {
            adapter.update(list.subList(0, 4))
            iv_more.visibility = View.VISIBLE
        } else {
            adapter.update(list)
            iv_more.visibility = View.GONE
        }
        show()
    }

    private class DialogAdapter(private var list: List<ElevatorTimeBean.DayHours>? = null) : RecyclerView.Adapter<DialogAdapter.DialogHolder>() {
        fun update(list: List<ElevatorTimeBean.DayHours>) {
            this.list = list
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DialogHolder {
            return DialogHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_days_hours_item, parent, false))
        }

        override fun getItemCount(): Int {
            return list?.size ?: 0
        }

        override fun onBindViewHolder(holder: DialogHolder, position: Int) {
            list!![position].run {
                holder.tv_date.text = date
                holder.tv_hours.text = hours
            }
        }

        class DialogHolder(view: View) : RecyclerView.ViewHolder(view) {
            var tv_date = view.findViewById<TextView>(R.id.tv_date)
            var tv_hours = view.findViewById<TextView>(R.id.tv_hours)
        }
    }

}