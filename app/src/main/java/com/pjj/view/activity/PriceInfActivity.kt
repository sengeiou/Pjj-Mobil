package com.pjj.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.pjj.R
import com.pjj.module.MediaOrderInfBean
import com.pjj.present.BasePresent
import com.pjj.utils.CalculateUtils
import com.pjj.utils.SpaceItemDecoration
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_price_inf.*

class PriceInfActivity : BaseActivity<BasePresent<*>>() {

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_price_inf)
        setTitle("价格明细")
        val rate = intent.getIntExtra("rate", 1)
        rv_price.layoutManager = LinearLayoutManager(this)
        val list = intent.getParcelableArrayListExtra<MediaOrderInfBean.ScreenPriceDetailBean>("list_data").apply {
            forEach {
                //val length = it.orderTimeList.size
                it.price = it.price * rate
            }
        }
        val adapter = PriceAdapter()
        rv_price.adapter = adapter
        rv_price.addItemDecoration(SpaceItemDecoration(this, LinearLayoutManager.VERTICAL, ViewUtils.getDp(R.dimen.dp_15), ViewUtils.getColor(R.color.color_f1f1f1)))
        adapter.list = list
        tv_sure.setOnClickListener(onClick)
        tv_sum_price.text = "¥${CalculateUtils.m1(intent.getFloatExtra("sum_price", 0f))}"
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_sure -> finish()
        }
    }

    private class PriceAdapter : RecyclerView.Adapter<PriceHolder>() {
        var list: MutableList<MediaOrderInfBean.ScreenPriceDetailBean>? = null
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PriceHolder {
            return PriceHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_price_inf_item, parent, false))
        }

        override fun getItemCount(): Int {
            return list?.size ?: 0
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: PriceHolder, position: Int) {
            list!![position].run {
                holder.tv_name.text = screenName
                holder.tv_date.text = getDateString(orderTimeList, holder.tv_count)
                holder.tv_price.text = "¥" + CalculateUtils.m1(price)
            }
        }

        @SuppressLint("SetTextI18n")
        private fun getDateString(dates: MutableList<String>?, tv: TextView): String {
            val buff = StringBuffer()
            var size = 0
            if (TextUtils.isNotEmptyList(dates)) {
                size = dates!!.size
                buff.append(dates[0].replace("-", "."))
                if (size > 1) {
                    buff.append("-")
                    buff.append(dates[size - 1].replace("-", "."))
                }
                if (size > 2) {
                    buff.append("...")
                }
                buff.append("（")
                buff.append(size)
                buff.append("天")
                buff.append("）")
            }
            tv.text = "x$size"
            return buff.toString()
        }
    }

    private class PriceHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv_name = view.findViewById<TextView>(R.id.tv_name)
        var tv_date = view.findViewById<TextView>(R.id.tv_date)
        var tv_price = view.findViewById<TextView>(R.id.tv_price)
        var tv_count = view.findViewById<TextView>(R.id.tv_count)
    }
}
