package com.pjj.view.adapter

import android.graphics.drawable.ColorDrawable
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import android.widget.TextView
import com.pjj.R
import com.pjj.module.AddressBean
import com.pjj.utils.ViewUtils

/**
 * Created by XinHeng on 2019/04/12.
 * describe：
 */
class AddressAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var list: MutableList<AddressBean.AddressData>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == 0)
            AddressHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_address_item, parent, false)).apply {
                tvWrite.setOnClickListener(onClick)
                itemView.setOnClickListener(onClick)
            }
        else
            object : RecyclerView.ViewHolder(TextView(parent.context).apply {
                layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_68))
                text = "已经到底啦"
                gravity = Gravity.CENTER
                background = ColorDrawable(ViewUtils.getColor(R.color.color_f1f1f1))
            }) {}
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == itemCount - 1) 1 else 0
    }

    override fun getItemCount(): Int {
        return (list?.size ?: 0) + 1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is AddressHolder -> {
                holder.itemView.tag = position
                holder.tvWrite.tag = position
                list!![position].run {
                    holder.tvAddress.text = this.position + describe
                    holder.tvName.text = name
                    holder.tvPhone.text = phone
                    if (isDefault == "1") {
                        holder.tvDefault.visibility = View.VISIBLE
                    } else {
                        holder.tvDefault.visibility = View.GONE
                    }
                }
            }
            else -> {
            }
        }
    }

    class AddressHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tvName = itemView.findViewById<TextView>(R.id.tv_name)
        var tvPhone = itemView.findViewById<TextView>(R.id.tv_phone)
        var tvDefault = itemView.findViewById<TextView>(R.id.tv_default)
        var tvAddress = itemView.findViewById<TextView>(R.id.tv_address)
        var tvWrite = itemView.findViewById<TextView>(R.id.tv_write)
    }

    private val onClick = View.OnClickListener {
        val position = it.tag as Int
        var data = list!![position]
        when (it.id) {
            R.id.tv_write -> onAddressAdapterListener?.updateAddress(data)
            else -> onAddressAdapterListener?.itemClick(data)
        }

    }
    var onAddressAdapterListener: OnAddressAdapterListener? = null

    interface OnAddressAdapterListener {
        fun itemClick(data: AddressBean.AddressData)
        fun updateAddress(data: AddressBean.AddressData)
    }
}