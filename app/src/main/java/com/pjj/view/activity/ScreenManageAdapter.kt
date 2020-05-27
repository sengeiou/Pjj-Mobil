package com.pjj.view.activity

import android.annotation.SuppressLint
import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.module.ScreenBean
import com.pjj.utils.CalculateUtils
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils

class ScreenManageAdapter(private var wuyeTag: Boolean) : RecyclerView.Adapter<ScreenManageAdapter.ScreenManageHolder>() {
    var selectAllTag = false
        private set
    var list: MutableList<ScreenBean.DataBean>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenManageHolder {
        return ScreenManageHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_screen_manage_item, parent, false)).apply {
            iv_select.setOnClickListener(onClick)
            if (!wuyeTag) {
                tv_screen_online.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ScreenManageHolder, position: Int) {
        holder.iv_select.setTag(R.id.position, position)
        list!![position].run {
            with(holder) {
                tv_price_after.visibility = View.VISIBLE
                tv_price.visibility = View.VISIBLE
                Glide.with(itemView).load(PjjApplication.filePath + screenUrlName).into(iv_screen)
                tv_screen_name.text = screenName
                tv_local.text = "$communityName（${areaName}）"
                tv_screen_type.text = when (cooperationMode) {
                    "1" -> "地方自营"
                    "2" -> "联合运营"
                    "3" -> "公司运营"
                    else -> {
                        tv_price.visibility = View.GONE
                        tv_price_after.visibility = View.GONE
                        "自用"
                    }
                }
                if (connectStatus == "0") {
                    iv_select.setImageResource(R.mipmap.unselect)
                    tv_screen_online.text = "离线"
                    tv_screen_online.setTextColor(ViewUtils.getColor(R.color.color_ea4a4a))
                    tv_screen_online.background = ColorDrawable(ViewUtils.getColor(R.color.color_fde6e6))
                } else {
                    tv_screen_online.setTextColor(ViewUtils.getColor(R.color.color_04be46))
                    tv_screen_online.background = ColorDrawable(ViewUtils.getColor(R.color.color_e6fdf0))
                    iv_select.setImageResource(if (isSelect) R.mipmap.select else R.mipmap.unselect)
                    tv_screen_online.text = "在线"

                }
                tv_price.text = CalculateUtils.m1(mediaPrice)
                if (TextUtils.isEmpty(switchingDate)) {
                    ll_hint.visibility = View.GONE
                } else {
                    ll_hint.visibility = View.VISIBLE
                    tv_hint.text = "提示：本屏幕将于${switchingDate}日切换为"
                }
            }
        }
    }

    private val onClick = View.OnClickListener {
        when (it.id) {
            R.id.iv_select -> {
                val position = it.getTag(R.id.position) as Int
                val iv = it as ImageView
                val dataBean = list!![position]
                if (dataBean.connectStatus == "0") {
                    //onScreenManageAdapterListener?.notice("有广告屏不在线，暂时无法发布")
                    return@OnClickListener
                }
                val tag = !dataBean.isSelect
                dataBean.isSelect = tag
                iv.setImageResource(if (tag) R.mipmap.select else R.mipmap.unselect)
                val tagAll = isAll()
                if (selectAllTag != tagAll) {
                    selectAllTag = tagAll
                    onScreenManageAdapterListener?.isAllSelect(selectAllTag)
                }
            }
        }
    }

    fun getSelectData(): ArrayList<ScreenBean.DataBean> {
        val list1 = ArrayList<ScreenBean.DataBean>()
        list?.forEach {
            if (it.isSelect) {
                list1.add(it)
            }
        }
        return list1
    }

    fun changeAllSelect(): Boolean {
        selectAllTag = !selectAllTag
        var noticeTag = false
        list?.forEach {
            if (it.connectStatus != "0") {
                it.isSelect = selectAllTag
            } else {
                noticeTag = true
            }
        }
        if (selectAllTag && noticeTag) {
            onScreenManageAdapterListener?.notice("有广告屏不在线，暂时无法发布")
        }
        onScreenManageAdapterListener?.isAllSelect(selectAllTag)
        notifyDataSetChanged()
        return selectAllTag
    }

    private fun isAll(): Boolean {
        list?.forEach {
            if (!it.isSelect && it.connectStatus != "0") {
                return false
            }
        }
        return true
    }

    fun cleanSelect() {
        list?.forEach {
            it.isSelect = false
        }
        notifyDataSetChanged()
        selectAllTag = false
        onScreenManageAdapterListener?.isAllSelect(false)
    }

    class ScreenManageHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv_select = view.findViewById<ImageView>(R.id.iv_select)
        var iv_screen = view.findViewById<ImageView>(R.id.iv_screen)
        var tv_screen_name = view.findViewById<TextView>(R.id.tv_screen_name)
        var tv_local = view.findViewById<TextView>(R.id.tv_local)
        var tv_screen_type = view.findViewById<TextView>(R.id.tv_screen_type)
        var tv_screen_online = view.findViewById<TextView>(R.id.tv_screen_online)
        var tv_price_after = view.findViewById<TextView>(R.id.tv_price_after)
        var tv_price = view.findViewById<TextView>(R.id.tv_price)
        var tv_hint = view.findViewById<TextView>(R.id.tv_hint)
        var ll_hint = view.findViewById<LinearLayout>(R.id.ll_hint)
    }

    var onScreenManageAdapterListener: OnScreenManageAdapterListener? = null

    interface OnScreenManageAdapterListener {
        fun isAllSelect(isAll: Boolean)
        fun notice(msg: String)
    }
}