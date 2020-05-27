package com.pjj.view.adapter

import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.pjj.R
import com.pjj.utils.ViewUtils

/**
 * Created by XinHeng on 2019/04/02.
 * describe：ranking 1 2 3
 */
class AllCityAdapter(private var ranking: Int) : RecyclerView.Adapter<AllCityAdapter.AllCityHolder>() {
    private var oldPosition = -1
    var list: JsonArray? = null
        set(value) {
            field = value
            oldPosition = -1
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllCityHolder {
        return AllCityHolder(TextView(parent.context).apply {
            layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_40))
            gravity = Gravity.CENTER
            setTextColor(ViewUtils.getColor(R.color.color_333333))
            setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_13))
            setOnClickListener(onClickListener)
        })
    }

    override fun getItemCount(): Int {
        return list?.size() ?: 0
    }

    override fun onBindViewHolder(holder: AllCityHolder, position: Int) {
        holder.tv.tag = position
        holder.tv.text = list!![position].asJsonObject.get("areaName").asString
        if (position == oldPosition) {
            holder.tv.setTextColor(ViewUtils.getColor(R.color.color_theme))
        } else {
            holder.tv.setTextColor(ViewUtils.getColor(R.color.color_333333))
        }
    }

    class AllCityHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv = itemView as TextView
    }

    private var onClickListener = View.OnClickListener {
        var position = it.tag as Int
        if (position != oldPosition) {
            var old = oldPosition
            oldPosition = position
            notifyItemChanged(old)
            notifyItemChanged(oldPosition)
        } else {
            return@OnClickListener
        }
        var jsonElement = list!![position] as JsonObject
        var element: JsonElement?
        if (ranking == 3) {
            element = null
        } else {
            element = jsonElement.get("childAreaList")
        }
        onAllCityItemClickListener?.itemClick(ranking, jsonElement.get("areaId").asString, jsonElement.get("areaName").asString, if (null == element) null else (element as JsonArray))
    }
    var onAllCityItemClickListener: OnAllCityItemClickListener? = null

    interface OnAllCityItemClickListener {
        fun itemClick(ranking: Int, areaId: String, text: String, array: JsonArray?)
    }
}