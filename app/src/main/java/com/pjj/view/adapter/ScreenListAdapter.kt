package com.pjj.view.adapter

import android.annotation.SuppressLint
import android.content.Context
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
import com.pjj.module.AllBuildingScreenBean
import com.pjj.module.BuildingElevatorBean
import com.pjj.utils.CalculateUtils
import com.pjj.utils.Log
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils

class ScreenListAdapter(private var context: Context) : RecyclerView.Adapter<ScreenListAdapter.ScreenListHolder>() {
    var data: AllBuildingScreenBean.DataBean? = null
    var top: View? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private val onClick = View.OnClickListener {
        val position = it.getTag(R.id.position) as Int
        when (it.id) {
            R.id.iv_select -> {
                val bean = data!!.elevatorList!![position - (if (hasTop()) 1 else 0)]
                if (bean.canUseCount == 0) return@OnClickListener
                bean.isSelect = !bean.isSelect
                bean.selectCount = if (bean.isSelect) bean.canUseCount else 0
                updateElevatorChildSelectStatue(bean.isSelect, bean.screenList)
                notifyItemChanged(position)
                notifyHeadChanged()
            }
            R.id.iv_click -> {
                val bean = data!!.elevatorList!![position - (if (hasTop()) 1 else 0)]
                if (bean.screenNum == 0) return@OnClickListener
                bean.isShow = !bean.isShow
                notifyItemChanged(position)
            }
            R.id.iv_select_media -> {
                val tag = (data!!.elevatorList?.size ?: 0) + (if (hasTop()) 1 else 0)
                val bean = data!!.screenList[position - tag]
                if (bean.screenStatus == "2") return@OnClickListener
                val select = bean.isSelect
                bean.isSelect = !select
                val symbol = if (select) -1 else 1
                val sum = data!!.sumPrice
                data!!.sumPrice = sum + symbol * bean.finalXspPrice
                data!!.selectCount = data!!.selectCount + symbol
                notifyItemChanged(position)
                notifyHeadChanged()
            }
            R.id.iv_child_select -> {
                val beanElevator = data!!.elevatorList!![position - (if (hasTop()) 1 else 0)]
                val childPosition = it.getTag(R.id.child_position) as Int
                val bean = beanElevator.screenList!![childPosition]
                if (bean.screenStatus == "2") return@OnClickListener
                val select = bean.isSelect
                bean.isSelect = !select
                updateElevatorGroup(beanElevator)
                val symbol = if (select) -1 else 1
                val sum = data!!.sumPrice
                data!!.sumPrice = sum + symbol * bean.finalXspPrice
                data!!.selectCount = data!!.selectCount + symbol
                notifyItemChanged(position)
                notifyHeadChanged()
            }
        }
    }

    private fun notifyHeadChanged() {
        if (!hasTop()) {
            return
        }
        var colorSelect = "#999999"
        var colorSum = "#999999"
        var colorSelectName = "#999999"
        val bean = data!!
        if (bean.canUseCount > 0) {
            colorSelect = "#FF4C4C"
            colorSum = "#333333"
            colorSelectName = "#555555"
        }
        ViewUtils.setHtmlText(top!!.findViewById(R.id.tv_select), "<font color=\"$colorSelectName\">已选屏幕：</font><font color=\"$colorSelect\">${bean.selectCount}</font><font color=\"$colorSum\">/${bean.screenNum}</font>")
        //ViewUtils.setHtmlText(tv_price, "¥<font\"$colorPrice\">${CalculateUtils.m1(bean.price)}</font>")
        //ViewUtils.setHtmlText(tv_sum_price_name, "<font\"$colorSelect\">总价：</font>")
        ViewUtils.setHtmlText(top!!.findViewById(R.id.tv_sum_price), "<font color=\"$colorSelect\">¥${CalculateUtils.m1(bean.sumPrice)}</font>")
    }

    private fun updateElevatorChildSelectStatue(statue: Boolean, screenList: MutableList<BuildingElevatorBean.DataBean.ElevatorListBean.ScreenListBean>?) {
        screenList?.forEach { screen ->
            if (screen.screenStatus != "2" && screen.isSelect != statue) {
                screen.isSelect = statue
                val symbol = if (statue) 1 else -1
                data!!.sumPrice = data!!.sumPrice + symbol * screen.finalXspPrice
                data!!.selectCount = data!!.selectCount + symbol
            }
        }
    }

    private fun updateElevatorGroup(beanElevator: BuildingElevatorBean.DataBean.ElevatorListBean) {
        var selectCount = 0
        var sumPrice = 0f
        beanElevator.screenList?.forEach {
            if (it.isSelect) {
                ++selectCount
                sumPrice += it.finalXspPrice
            }
        }
        beanElevator.selectCount = selectCount
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenListHolder {
        return when (viewType) {
            1 -> ScreenElevatorHolder(LayoutInflater.from(context).inflate(R.layout.layout_screen_elevator_item, parent, false)).apply {
                iv_select.setOnClickListener(onClick)
                iv_click.setOnClickListener(onClick)
            }
            2 -> ScreenMediaHolder(LayoutInflater.from(context).inflate(R.layout.layout_screen_media_item_all, parent, false)).apply {
                iv_select_media.setOnClickListener(onClick)
            }
            else -> ScreenTop(top!!)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val hasTop = hasTop()
        if (hasTop && position == 0)
            return 0
        val elevatorList = data!!.elevatorList
        val index = if (hasTop) position - 1 else position
        if (TextUtils.isNotEmptyList(elevatorList) && elevatorList!!.size > index)
            return 1
        return 2
    }

    override fun getItemCount(): Int {
        val hasTop = if (hasTop()) 1 else 0
        if (null == data)
            return hasTop
        val screenList = data!!.screenList
        val elevatorList = data!!.elevatorList
        return hasTop + (screenList?.size ?: 0) + (elevatorList?.size ?: 0)
    }

    private fun hasTop(): Boolean {
        return null != top
    }

    override fun onBindViewHolder(holder: ScreenListHolder, position: Int) {
        val top = if (hasTop()) 1 else 0
        when (holder) {
            is ScreenTop -> {
                Log.e("TAG", "ScreenTop")
            }
            is ScreenMediaHolder -> {
                val tag = (data!!.elevatorList?.size ?: 0) + top
                val bean = data!!.screenList!![position - tag]
                holder.iv_select_media.setImageResource(when {
                    bean.screenStatus == "2" -> R.mipmap.unclickselect
                    bean.isSelect -> R.mipmap.select_red
                    else -> R.mipmap.unselect
                })
                holder.tv_screen_name.text = bean.screenName
                val effiect = data!!.canUseCount == 0
                holder.tv_screen_name.setTextColor(if (effiect) ViewUtils.getColor(R.color.color_999999) else ViewUtils.getColor(R.color.color_333333))
                holder.tv_screen_statue.visibility = if (bean.screenStatus == "2") View.VISIBLE else View.GONE
                if (!effiect) {
                    holder.tv_screen_statue.setTextColor(ViewUtils.getColor(R.color.color_ea4a4a))
                    holder.tv_screen_statue.background = ViewUtils.getDrawable(R.drawable.shape_fde6e6_bg_3)

                    holder.tv_screen_price.setTextColor(ViewUtils.getColor(R.color.color_theme))
                    holder.tv_screen_price.background = ViewUtils.getDrawable(R.drawable.shape_theme_side_3)
                } else {
                    holder.tv_screen_statue.setTextColor(ViewUtils.getColor(R.color.color_999999))
                    holder.tv_screen_statue.background = ViewUtils.getDrawable(R.drawable.shape_999999_side_3)

                    holder.tv_screen_price.setTextColor(ViewUtils.getColor(R.color.color_999999))
                    holder.tv_screen_price.background = ViewUtils.getDrawable(R.drawable.shape_999999_side_3)
                }
                holder.iv_select_media.setOnClickListener(onClick)
                holder.iv_select_media.setTag(R.id.position, position)
            }
            is ScreenElevatorHolder -> {
                val bean = data!!.elevatorList!![position - top]
                var colorSelct = "#999999"
                var colorSum = "#333333"
                holder.iv_select.setImageResource(when {
                    bean.canUseCount == 0 -> {
                        colorSum = "#999999"
                        R.mipmap.unclickselect
                    }
                    bean.screenNum == bean.selectCount -> {
                        colorSelct = "#FF4C4C"
                        R.mipmap.select_red
                    }
                    bean.selectCount == 0 -> {
                        colorSelct = "#FF4C4C"
                        R.mipmap.unselect
                    }
                    else -> {
                        colorSelct = "#FF4C4C"
                        R.mipmap.unselect_tag
                    }
                })
                holder.iv_select.setTag(R.id.position, position)
                holder.tv_elevator_name.text = bean.eleName
                ViewUtils.setHtmlText(holder.tv_elevator_name, ViewUtils.getHtmlText(bean.eleName, colorSum))
                ViewUtils.setHtmlText(holder.tv_select, "已选屏幕：<font color=\"$colorSelct\">${bean.selectCount}</font><font color=\"$colorSum\">/${bean.screenNum}</font>")
                if (bean.isShow) {
                    holder.line.visibility = View.VISIBLE
                    holder.ll_parent.visibility = View.VISIBLE
                    addChildView(holder.ll_parent, bean.screenList, position, bean.canUseCount == 0)
                    holder.iv_click.setImageResource(R.mipmap.up_fill)
                } else {
                    holder.line.visibility = View.GONE
                    holder.ll_parent.visibility = View.GONE
                    holder.iv_click.setImageResource(R.mipmap.down_fill)
                }
                holder.iv_click.setTag(R.id.position, position)
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun addChildView(parent: ViewGroup, screenList: MutableList<BuildingElevatorBean.DataBean.ElevatorListBean.ScreenListBean>?, position: Int, notUse: Boolean) {
        parent.removeAllViews()
        val size = screenList?.size ?: 0
        val colorName = if (notUse) ViewUtils.getColor(R.color.color_999999) else ViewUtils.getColor(R.color.color_333333)
        val priceColor = if (notUse) ViewUtils.getColor(R.color.color_999999) else ViewUtils.getColor(R.color.color_theme)
        val priceBg = if (notUse) ViewUtils.getDrawable(R.drawable.shape_999999_side_3) else ViewUtils.getDrawable(R.drawable.shape_theme_side_3)
        screenList?.forEachIndexed { index, screenListBean ->
            val view = LayoutInflater.from(context).inflate(R.layout.layout_screen_elevator_child_item, parent, false)
            val iv_child_select = view.findViewById<ImageView>(R.id.iv_child_select)
            val tv_screen_name = view.findViewById<TextView>(R.id.tv_screen_name)
            val tv_screen_price = view.findViewById<TextView>(R.id.tv_screen_price)
            val tv_screen_statue = view.findViewById<TextView>(R.id.tv_screen_statue)
            parent.addView(view)
            tv_screen_statue.visibility = View.GONE
            iv_child_select.setImageResource(when {
                notUse || screenListBean.screenStatus == "2" -> {
                    tv_screen_statue.visibility = View.VISIBLE
                    if (notUse) {
                        tv_screen_statue.setTextColor(ViewUtils.getColor(R.color.color_999999))
                        tv_screen_statue.background = ViewUtils.getDrawable(R.drawable.shape_999999_side_3)
                    } else {
                        tv_screen_statue.setTextColor(ViewUtils.getColor(R.color.color_ea4a4a))
                        tv_screen_statue.background = ViewUtils.getDrawable(R.drawable.shape_fde6e6_bg_3)
                    }
                    R.mipmap.unclickselect
                }
                screenListBean.isSelect -> R.mipmap.select_red
                else -> R.mipmap.unselect
            })
            iv_child_select.setOnClickListener(onClick)
            iv_child_select.setTag(R.id.position, position)
            iv_child_select.setTag(R.id.child_position, index)
            tv_screen_name.text = screenListBean.screenName
            tv_screen_name.setTextColor(colorName)
            tv_screen_price.setTextColor(priceColor)
            tv_screen_price.background = priceBg
            tv_screen_price.text = "${CalculateUtils.m1(screenListBean.finalXspPrice)} 元 / 天"
            if (index < size - 1) {
                parent.addView(getLineView())
            }
        }
    }

    private val dp1 = ViewUtils.getDp(R.dimen.dp_1)
    private fun getLineView(): View {
        return View(context).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp1)
            background = ColorDrawable(ViewUtils.getColor(R.color.color_e3e3e3))
        }
    }

    open class ScreenListHolder(view: View) : RecyclerView.ViewHolder(view)

    class ScreenTop(view: View) : ScreenListHolder(view) {
        //var iv_top= itemView.findViewById<ImageView>(R.id.iv_top)
        //var tv_building= itemView.findViewById<TextView>(R.id.tv_building)
        var tv_select = itemView.findViewById<TextView>(R.id.tv_select)
        var tv_sum_price = itemView.findViewById<TextView>(R.id.tv_sum_price)
    }

    class ScreenMediaHolder(view: View) : ScreenListHolder(view) {
        var iv_select_media = itemView.findViewById<ImageView>(R.id.iv_select_media)
        var tv_screen_name = itemView.findViewById<TextView>(R.id.tv_screen_name)
        var tv_screen_price = itemView.findViewById<TextView>(R.id.tv_screen_price)
        var tv_screen_statue = itemView.findViewById<TextView>(R.id.tv_screen_statue)
    }

    class ScreenElevatorHolder(view: View) : ScreenListHolder(view) {
        var iv_select = itemView.findViewById<ImageView>(R.id.iv_select)
        var tv_elevator_name = itemView.findViewById<TextView>(R.id.tv_elevator_name)
        var tv_select = itemView.findViewById<TextView>(R.id.tv_select)
        var iv_click = itemView.findViewById<ImageView>(R.id.iv_click)
        var line = itemView.findViewById<View>(R.id.line)
        var ll_parent = itemView.findViewById<LinearLayout>(R.id.ll_parent)
    }
}