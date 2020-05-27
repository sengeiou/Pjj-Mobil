package com.pjj.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.PopupWindow
import android.widget.TextView
import com.pjj.R
import com.pjj.module.AreaTypeBean
import com.pjj.utils.ViewUtils

class AreaTypePopupWindow(context: Context) : PopupWindow(context) {
    private val dp58 = ViewUtils.getDp(R.dimen.dp_58)
    private val dp29 = ViewUtils.getDp(R.dimen.dp_29)
    private val dp24 = ViewUtils.getDp(R.dimen.dp_24)
    val textColor = ViewUtils.getColor(R.color.color_333333)
    private val color6 = ViewUtils.getColor(R.color.color_666666)
    val textColorSelect = ViewUtils.getColor(R.color.color_theme)
    private val adapterType = TypeAdapter(context)
    private var selectPosition = 0
    private val spaceView: View
    private val rvType: RecyclerView
    private val onClick = View.OnClickListener {
        when (it.id) {
            R.id.tv_reset -> {
                if (selectPosition > 0) {
                    selectPosition = 0
                    adapterType.notifyDataSetChanged()
                }
                onAreaScopeListener?.reset()
                dismiss()
                return@OnClickListener
            }
            R.id.tv_complete -> {
                if (-1 == selectPosition) {
                    onAreaScopeListener?.notice("请选择小区类型")
                    return@OnClickListener
                }
                onAreaScopeListener?.select(adapterType.list!![selectPosition].typeName, adapterType.list!![selectPosition].typeId)
                dismiss()
                return@OnClickListener
            }
        }
        if (it is TextView) {
            val position = it.tag as Int
            if (selectPosition != position) {
                it.setTextColor(Color.WHITE)
                it.background = selectBg()
                val old = selectPosition
                selectPosition = position
                if (old > -1)
                    adapterType.notifyItemChanged(old)
            }
        } else {
            dismiss()
        }
    }

    init {
        width = WindowManager.LayoutParams.MATCH_PARENT
        height = WindowManager.LayoutParams.WRAP_CONTENT
        isFocusable = true
        contentView = LayoutInflater.from(context).inflate(R.layout.layout_area_type_item, null, false)
        rvType = contentView.findViewById(R.id.rv_type)
        spaceView = contentView.findViewById(R.id.space_view)
        spaceView.setOnClickListener(onClick)
        contentView.findViewById<TextView>(R.id.tv_reset).setOnClickListener(onClick)
        contentView.findViewById<TextView>(R.id.tv_complete).setOnClickListener(onClick)
        isOutsideTouchable = true
        setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        setOnDismissListener {
            onAreaScopeListener?.dismiss()
        }
        rvType.layoutManager = GridLayoutManager(context, 4)
        rvType.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                outRect.right = dp24
            }
        })
        rvType.adapter = adapterType
    }

    fun setData(list: MutableList<AreaTypeBean.DataBean.TypeListBean>) {
        adapterType.list = list
    }

    var onAreaScopeListener: OnAreaScopeListener? = null

    private inner class TypeAdapter(private var context: Context) : RecyclerView.Adapter<TypeAdapter.TypeHolder>() {
        var list: MutableList<AreaTypeBean.DataBean.TypeListBean>? = null
            set(value) {
                field = value
                field?.let {
                    it.add(0, AreaTypeBean.DataBean.TypeListBean().apply {
                        typeName = "不限"
                        typeId = null
                    })
                }
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeHolder {
            return TypeHolder(createTextView(""))
        }

        override fun getItemCount(): Int {
            return list?.size ?: 0
        }

        override fun onBindViewHolder(holder: TypeHolder, position: Int) {
            holder.tv.text = list!![position].typeName
            holder.tv.tag = position
            if (selectPosition == position) {
                holder.tv.setTextColor(Color.WHITE)
                holder.tv.background = selectBg()
            } else {
                holder.tv.setTextColor(textColor)
                holder.tv.background = defaultBg()
            }
        }

        inner class TypeHolder(view: TextView) : RecyclerView.ViewHolder(view) {
            var tv = view
        }

        private fun createTextView(content: String): TextView {
            return TextView(context).apply {
                layoutParams = RecyclerView.LayoutParams(dp58, dp29)
                gravity = Gravity.CENTER
                setTextColor(textColor)
                background = GradientDrawable().apply {
                    setColor(Color.WHITE)
                    setStroke(ViewUtils.getDp(R.dimen.dp_1), color6)
                    cornerRadius = ViewUtils.getFDp(R.dimen.dp_3)
                }
                setOnClickListener(onClick)
            }
        }
    }

    private fun selectBg(): Drawable {
        return GradientDrawable().apply {
            setColor(textColorSelect)
            cornerRadius = ViewUtils.getFDp(R.dimen.dp_3)
        }
    }

    private fun defaultBg(): Drawable {
        return GradientDrawable().apply {
            setColor(Color.WHITE)
            setStroke(ViewUtils.getDp(R.dimen.dp_1), color6)
            cornerRadius = ViewUtils.getFDp(R.dimen.dp_3)
        }
    }

    interface OnAreaScopeListener {
        fun notice(msg: String)
        fun select(text: String, id: String?)
        fun dismiss()
        fun reset()
    }
}