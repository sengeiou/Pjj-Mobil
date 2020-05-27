package com.pjj.view.adapter

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.module.NewMediaBuildingBean
import com.pjj.utils.ViewUtils

/**
 * Created by XinHeng on 2018/12/03.
 * describe：
 */
class BuildingInfNewMediaAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var listOld: MutableList<NewMediaBuildingBean.CommunityListBean>? = null
        set(value) {
            field = value
            list = value
        }
    private var newList = ArrayList<NewMediaBuildingBean.CommunityListBean>()
    private var list: MutableList<NewMediaBuildingBean.CommunityListBean>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    fun restore() {
        list = listOld
    }

    fun filter(name: String) {
        newList.clear()
        NewMediaBuildingBean.filterAdd(listOld, newList, name)
        list = newList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            -1 -> {
                BuildingFliterNoInfHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_list_no_date, parent, false))
            }
            0 -> {
                GuessHolder(LinearLayout(parent.context).apply {
                    orientation = LinearLayout.VERTICAL
                    addView(View(parent.context).apply {
                        layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_5))
                        background = ColorDrawable(ViewUtils.getColor(R.color.color_f1f1f1))
                    })
                    addView(TextView(parent.context).apply {
                        text = "猜你想找"
                        setTextColor(ViewUtils.getColor(R.color.color_333333))
                        setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_14))
                        layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_48))
                        setPadding(ViewUtils.getDp(R.dimen.dp_20), 0, 0, 0)
                        gravity = Gravity.CENTER_VERTICAL
                    })
                })
            }
            else -> {
                LayoutInflater.from(parent.context).inflate(R.layout.layout_elevator_area_inf, parent, false).run {
                    setOnClickListener(onClickListener)
                    BuildingInfHolder(this)
                }.apply {
                    tv_local.visibility = View.GONE
                    tv_elevator_num.visibility = View.GONE
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (position == 0 && list!![0].communityName == null) {
            return -1 //过滤后无数据
        }
        return when (list!![position].communityName) {
            null -> 0 //猜你所想
            else -> 1 //数据
        }
    }

    override fun getItemCount(): Int {
        return (list?.size ?: 0) + 0
    }

    private var requestOptions = RequestOptions().error(R.mipmap.building)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.tag = position
        when (holder) {
            is BuildingInfHolder -> {
                list!![position].run {
                    Glide.with(holder.iv_building).load(PjjApplication.filePath + imgName).apply(requestOptions).into(holder.iv_building)
                    holder.tv_building.text = communityName
                    holder.tv_local.text = this.position
                    holder.tv_xsp_num.text = "$screenNum 面屏幕"
                    holder.tv_xsp_price.text = "$price 元/天"
                    holder.tv_xsp_price.visibility = View.VISIBLE
                    holder.tv_xsp_price_.visibility = View.VISIBLE
                }
            }
        }
    }

    class BuildingInfHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv_building = view.findViewById<ImageView>(R.id.iv_building1)
        var tv_building = view.findViewById<TextView>(R.id.tv_building)
        var tv_local = view.findViewById<TextView>(R.id.tv_local)
        var tv_elevator_num = view.findViewById<TextView>(R.id.tv_elevator_num)
        var tv_xsp_num = view.findViewById<TextView>(R.id.tv_xsp_num)
        var tv_xsp_price = view.findViewById<TextView>(R.id.tv_xsp_price)
        var tv_xsp_price_ = view.findViewById<TextView>(R.id.tv_xsp_price_)
    }

    class BuildingFliterNoInfHolder(view: View) : RecyclerView.ViewHolder(view)
    class GuessHolder(view: View) : RecyclerView.ViewHolder(view)//猜你所想

    var onItemSelectListener: OnItemSelectListener? = null
    private var onClickListener = View.OnClickListener {
        var i = it.tag as Int
        list!![i].run {
            var toIntOrNull = 0
            try {
                toIntOrNull = screenNum.toInt()
            } catch (e: Exception) {
            }

            if (toIntOrNull > 0) {
                onItemSelectListener?.itemClick(communityId, communityName)
            } else {
                onItemSelectListener?.noXsp()
            }
        }
    }

    interface OnItemSelectListener {
        fun itemClick(communityId: String, communityName: String)
        fun noXsp()
    }
}