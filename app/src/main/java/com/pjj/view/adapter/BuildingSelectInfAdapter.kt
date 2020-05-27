package com.pjj.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.module.BuildingBean
import com.pjj.utils.TextUtils

/**
 * Created by XinHeng on 2018/12/03.
 * describe：
 */
class BuildingSelectInfAdapter(var localHidden: Boolean = false) : ABSelectRecycleAdapter<BuildingBean.CommunityListBean, BuildingSelectInfAdapter.BuildingInfHolder>() {

    override fun isEffective(t: BuildingBean.CommunityListBean, index: Int): Boolean {
        var toIntOrNull = 0
        try {
            toIntOrNull = t.screenNum.toInt()
        } catch (e: Exception) {
        }
        return toIntOrNull > 0
    }

    private var listSelect = ArrayList<Int>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuildingSelectInfAdapter.BuildingInfHolder {
        return BuildingInfHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_elevator_area_select_inf, parent, false)).apply {
            iv_select.setOnClickListener(onClickListener)
            if (localHidden) {
                tv_local.visibility = View.GONE
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> 1
            else -> 1
        }
    }

    override fun onBindViewHolder(holder: BuildingSelectInfAdapter.BuildingInfHolder, position: Int) {
        holder.iv_select.tag = position
        listAdapter!![position].run {
            bean?.run {
                Glide.with(holder.iv_building).load(PjjApplication.filePath + imgName).into(holder.iv_building)
                holder.tv_building.text = communityName
                holder.tv_local.text = this.position
                holder.tv_elevator_num.text = "$elevatorNum 部电梯"
                holder.tv_xsp_num.text = "$screenNum 面屏幕"
                if (price == null) {
                    holder.tv_xsp_price.visibility = View.GONE
                    holder.tv_xsp_price_.visibility = View.GONE
                } else {
                    holder.tv_xsp_price.text = "$price 元/小时"
                    holder.tv_xsp_price.visibility = View.VISIBLE
                    holder.tv_xsp_price_.visibility = View.VISIBLE
                }
            }
            if (isEffective) {
                holder.iv_select.visibility = View.VISIBLE
                if (isSelect) {
                    holder.iv_select.setImageResource(R.mipmap.select)
                } else {
                    holder.iv_select.setImageResource(R.mipmap.unselect)
                }
            } else {
                holder.iv_select.visibility = View.GONE
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
        var iv_select = view.findViewById<ImageView>(R.id.iv_select)
    }

    var onItemSelectListener: OnItemSelectListener? = null
    private var onClickListener = View.OnClickListener {
        var imageView = it as ImageView
        var i = it.tag as Int
        if (listSelect.contains(i)) {
            listSelect.remove(i)
            listAdapter!![i].isSelect = false
            imageView.setImageResource(R.mipmap.unselect)
        } else {
            listSelect.add(i)
            listAdapter!![i].isSelect = true
            imageView.setImageResource(R.mipmap.select)
        }
        onItemSelectListener?.select(listSelect.size == listAdapter!!.size)
    }

    /**
     * 全选与取消
     */
    fun setAllSelectStatue(tag: Boolean) {
        if (TextUtils.isNotEmptyList(listAdapter)) {
            listSelect.clear()
            if (tag) {
                listAdapter!!.forEachIndexed { index, it ->
                    if (it.isEffective) {
                        it.isSelect = true
                        listSelect.add(index)
                    }
                }
            } else {
                listAdapter!!.forEach { it ->
                    it.isSelect = false
                }
            }
            notifyDataSetChanged()
        }
    }

    fun getSelectBuildingList(): MutableList<BuildingBean.CommunityListBean>? {
        if (listSelect.size > 0) {
            TextUtils.compareSmallToMore(listSelect)
            var listS = ArrayList<BuildingBean.CommunityListBean>()
            listSelect.forEach {
                listS.add(getItemBean(it)!!)
            }
            return listS
        }
        return null
    }

    interface OnItemSelectListener {
        fun select(isAll: Boolean)
    }
}