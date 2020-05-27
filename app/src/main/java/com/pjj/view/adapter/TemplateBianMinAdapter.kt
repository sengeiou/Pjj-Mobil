package com.pjj.view.adapter

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.pjj.R
import com.pjj.module.BianMinBean
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils

/**
 * Created by XinHeng on 2018/12/10.
 * describe：
 */
class TemplateBianMinAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var deletePosition: Int = -1
    var list: MutableList<BianMinBean.DataBean>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun upateForDelete() {
        if (deletePosition > -1 && list != null) {
            list?.removeAt(deletePosition)
            if (itemCount == 0) {
                notifyDataSetChanged()
            } else {
                notifyItemRangeRemoved(deletePosition * 2, 2)
                notifyItemRangeChanged(deletePosition, itemCount - deletePosition)
            }
        }
    }

    var dp24 = ViewUtils.getDp(R.dimen.dp_24)
    var dp15 = ViewUtils.getDp(R.dimen.dp_15)

    private var dp23 = ViewUtils.getDp(R.dimen.dp_23)
    private var dp20 = ViewUtils.getDp(R.dimen.dp_20)
    private var dp18 = ViewUtils.getDp(R.dimen.dp_18)
    private var dp17 = ViewUtils.getDp(R.dimen.dp_17)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            0 -> LayoutInflater.from(parent.context).inflate(R.layout.layout_bian_min_head, parent, false).let {
                TemplateHead(it).apply {
                    tv_title.setCompoundDrawables(ViewUtils.getDrawable(R.mipmap.write1).apply {
                        setBounds(0, 0, dp20, dp18)
                    }, null, null, null)
                    tv_deal_with.setCompoundDrawables(ViewUtils.getDrawable(R.mipmap.write2).apply {
                        setBounds(0, 0, dp17, dp20)
                    }, null, null, null)
                    tv_look.setCompoundDrawables(ViewUtils.getDrawable(R.mipmap.preview).apply {
                        setBounds(0, 0, dp20, dp20)
                    }, null, null, null)
                    tv_delete.setCompoundDrawables(ViewUtils.getDrawable(R.mipmap.delete_white).apply {
                        setBounds(0, 0, dp23, dp20)
                    }, null, null, null)
                    tv_deal_with.setOnClickListener(onClickListener)
                    tv_look.setOnClickListener(onClickListener)
                    tv_delete.setOnClickListener(onClickListener)
                }
            }
            -1 -> {
                TemplateContent(TextView(parent.context).apply {
                    setPadding(dp24, dp15, dp24, dp15)
                    //2019年2月26日15:22:45 修改
                    layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 0)
                    setTextColor(ViewUtils.getColor(R.color.color_999999))
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_12))
                    text = "创建模板"
                    setOnClickListener { onItemClickListener?.createTemplate() }
                })
            }
            else -> TemplateContent(LinearLayout(parent.context).apply {
                orientation = LinearLayout.VERTICAL
                layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
                addView(View(parent.context).apply {
                    layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_1))
                })
                addView(TextView(parent.context).apply {
                    setPadding(dp24, dp15, dp24, dp15)
                    setTextColor(ViewUtils.getColor(R.color.color_333333))
                    background=ColorDrawable(Color.WHITE)
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_12))
                })
                addView(View(parent.context).apply {
                    layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_5))
                })
            })
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0) {
            return -1
        }
        return when ((position - 1) % 2) {
            0 -> 0
            else -> 1
        }
    }

    override fun getItemCount(): Int {
        return if (TextUtils.isNotEmptyList(list)) {
            (list?.size ?: 0) * 2 + 1
        } else {
            0
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0) {
            return
        }
        var i = (position - 1) / 2
        with(list!![i]) {
            when (holder) {
                is TemplateHead -> {
                    holder.tv_title.text = title
                    holder.tv_look.tag = i
                    holder.tv_deal_with.tag = i
                    holder.tv_delete.tag = i
                }
                is TemplateContent -> ViewUtils.addEmpty2Text(holder.tv_context, info)
            }
        }
    }

    class TemplateHead(view: View) : RecyclerView.ViewHolder(view) {
        var tv_title = view.findViewById<TextView>(R.id.tv_title)!!
        var tv_deal_with = view.findViewById<TextView>(R.id.tv_deal_with)!!
        var tv_look = view.findViewById<TextView>(R.id.tv_look)!!
        var tv_delete = view.findViewById<TextView>(R.id.tv_delete)!!
    }

    class TemplateContent(view: View) : RecyclerView.ViewHolder(view) {
        var tv_context = if (view is TextView) {
            view
        } else {
            (view as ViewGroup).getChildAt(1) as TextView
        }
    }

    private var onClickListener = View.OnClickListener {
        var position = it.tag as Int
        var bean = list!![position]
        when (it.id) {
            R.id.tv_deal_with -> onItemClickListener?.editor(bean)
            R.id.tv_delete -> {
                deletePosition = position
                onItemClickListener?.delete(bean)
            }
            else -> onItemClickListener?.preview(bean)
        }
    }
    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun editor(bean: BianMinBean.DataBean)
        fun preview(bean: BianMinBean.DataBean)
        fun delete(bean: BianMinBean.DataBean)
        fun createTemplate()
    }
}
