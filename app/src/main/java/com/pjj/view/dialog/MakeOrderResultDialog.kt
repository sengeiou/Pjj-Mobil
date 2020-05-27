package com.pjj.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.gson.reflect.TypeToken
import com.pjj.R
import com.pjj.module.MakeOrderScreenBean
import com.pjj.module.xspad.XspManage
import com.pjj.utils.*
import kotlinx.android.synthetic.main.layout_make_order_result_item.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MakeOrderResultDialog(context: Context, themeResId: Int = 0) : FullWithNoTitleDialog(context, themeResId) {
    private var adapterResult = ResultAdapter()
    private var oldCanUseSumDay = 0
    private var textComFrom = false
    private var textList: MutableList<MakeOrderScreenBean.ScreenBean>? = null
    private var tagUnNext = false

    override fun getDialogGravity(): Int {
        return Gravity.CENTER
    }

    override fun isFullScreen(): Boolean {
        return false
    }

    override fun getWindowBgDrawable(): Drawable {
        return ColorDrawable(Color.TRANSPARENT)
    }

    init {
        setContentView(R.layout.layout_make_order_result_item)
        rv_result.layoutManager = LinearLayoutManager(context)
        rv_result.adapter = adapterResult
        tv_reselect.setOnClickListener(onClick)
        tv_next_order.setOnClickListener(onClick)
        setCanceledOnTouchOutside(false)
        setCancelable(false)
    }

    override fun onClick(view: View) {
        super.onClick(view)
        when (view.id) {
            R.id.tv_reselect -> {
                if (!textComFrom && tv_next_order.isEnabled)
                    filterUnUsedScreenDay()
                onMakeOrderResultListener?.reSelect()
                dismiss()
            }
            R.id.tv_next_order -> {
                if (!textComFrom && tv_next_order.isEnabled)
                    filterUnUsedScreenDay()
                onMakeOrderResultListener?.nextMakeOrder()
                dismiss()
            }
        }
    }

    private var unUsedScreen: MutableList<MakeOrderScreenBean.ScreenBean>? = null
    /**
     * 数据来自日期弹窗
     * 第一次判断
     */
    fun addFullScreenData(useFullScreen: MutableList<MakeOrderScreenBean.ScreenBean>?) {
        textList = useFullScreen
        //combinationList(useFullScreenList)
        adapterResult.list = combinationList(useFullScreen)
        textComFrom = true
    }

    private fun filterUnUsedScreenDay(tag: Boolean = true): Boolean {
        XspManage.getInstance().newMediaData.dates?.let {
            val type = object : TypeToken<HashMap<String, String>>() {
            }.type
            val map: HashMap<String, String> = JsonUtils.gson.fromJson<HashMap<String, String>>(it, type)
            if (TextUtils.isNotEmptyList(unUsedScreen)) {
                (unUsedScreen!!.size - 1 downTo 0).forEach { index ->
                    val screenBean = unUsedScreen!![index]
                    val screenId = screenBean.screenId
                    if (map.containsKey(screenId)) {
                        val orderTime = screenBean.orderTime
                        if (TextUtils.isEmpty(orderTime)) {//离线
                            map.remove(screenId)
                        } else {//投满 ，过滤
                            val value = filterMapDay(map[screenId]!!, orderTime)
                            if (null == value) {
                                map.remove(screenId)
                            } else {
                                map[screenId] = value
                            }
                        }
                    }
                }
            }
            if (tag)
                XspManage.getInstance().newMediaData.dates = JsonUtils.toJsonString(map)
            return map.isNotEmpty()
        }
        return false
    }


    private fun filterMapDay(strings: String, orderTime: String): String? {
        val dates = strings.split("&")
        val buffer = StringBuffer()
        dates.forEach {
            if (!it.contains(orderTime)) {
                buffer.append(it)
                buffer.append("&")
            }
        }
        if (buffer.isNotEmpty()) {
            buffer.deleteCharAt(buffer.length - 1)
            return buffer.toString()
        }
        return null
    }

    private fun changeNextStatue(tagUse: Boolean) {
        if (tagUse) {
            tagUnNext = false
            if (!tv_next_order.isEnabled) {
                tv_next_order.isEnabled = true
                tv_next_order.background = ColorDrawable(ViewUtils.getColor(R.color.color_theme))
            }
        } else {
            tagUnNext = true
            if (tv_next_order.isEnabled) {
                tv_next_order.isEnabled = false
                tv_next_order.background = ColorDrawable(ViewUtils.getColor(R.color.color_d2d2d2))
            }
        }
    }

    fun setData(useFullScreen: MutableList<MakeOrderScreenBean.ScreenBean>?, offLineScreen: MutableList<MakeOrderScreenBean.ScreenBean>?) {
        val list = ArrayList<MakeOrderScreenBean.ScreenBean>()
        textComFrom = false
        val useFullScreenList = ArrayList<MakeOrderScreenBean.ScreenBean>().apply {
            if (TextUtils.isNotEmptyList(offLineScreen) && TextUtils.isNotEmptyList(textList)) {
                textList!!.forEach {
                    if (!containList(it.screenId, offLineScreen!!)) {
                        this.add(it)
                    }
                }
            }
            useFullScreen?.let {
                addAll(it)
            }
        }
        list.addAll(combinationList(useFullScreenList))
        if (TextUtils.isNotEmptyList(offLineScreen)) {
            list.addAll(offLineScreen!!)
        }
//        list.forEach {
//            Log.e("TAG", "forEach: ${it.orderTime}")
//        }
        //提交订单，返回不可用屏幕数量
        //val nextUnUsedSumDay = getListSize(useFullScreenList) + getListSize(offLineScreen)
        unUsedScreen = ArrayList<MakeOrderScreenBean.ScreenBean>().apply {
            useFullScreen?.let {
                addAll(it)
            }
            offLineScreen?.let {
                addAll(it)
            }
        }
        //val nextCanUseTag = oldCanUseSumDay > nextUnUsedSumDay
        adapterResult.list = list
        changeNextStatue(filterUnUsedScreenDay(false))
        if (!isShowing) {
            show()
        }
    }

    fun setDayNum(dateCount: Int) {
        oldCanUseSumDay = dateCount
    }

    private fun containList(s: String, list: MutableList<MakeOrderScreenBean.ScreenBean>): Boolean {
        list.forEach {
            if (s == it.screenId) {
                return true
            }
        }
        return false
    }

    private fun <T> getListSize(list: MutableList<T>?): Int {
        return list?.size ?: 0
    }

    private fun combinationList(useFullScreen: MutableList<MakeOrderScreenBean.ScreenBean>?): MutableList<MakeOrderScreenBean.ScreenBean> {
        val datas = ArrayList<MakeOrderScreenBean.ScreenBean>()
        if (TextUtils.isNotEmptyList(useFullScreen)) {
            //val datas = ArrayList<MakeOrderScreenBean.ScreenBean>()
            var screenId: String? = null
            //同一个屏，合并日期
            useFullScreen!!.forEach {
                if (screenId != it.screenId) {
                    screenId = it.screenId
                    val bean = it.clone()
                    bean.dates = ArrayList<String>().apply {
                        add(it.orderTime)
                    }
                    datas.add(bean)
                } else {
                    datas[datas.size - 1].dates.add(it.orderTime)
                }
            }
            Log.e("TAG", "combinationList: ${datas.size} ")
            datas.forEach {
                it.orderTime = dealWithDatas(it.dates)
                it.dates = null
            }
        }
        return datas
    }

    private fun dealWithDatas(list: MutableList<String>?): String? {
        if (!TextUtils.isNotEmptyList(list)) {
            return null
        }
        TextUtils.sort(list) { o1, o2 ->
            DateUtils.compare(DateUtils.getDate(o1), DateUtils.getDate(o2))
        }
        var old = list!![0]
        val buff = StringBuffer()
        var start: String? = null
        var end: String? = null
        if (list.size > 1) {
            (1 until list.size).forEach {
                val date = list[it]
                if (checkIsNext(old, date)) {
                    if (null == start) {
                        start = old
                    }
                    end = date
                    if (it == list.size - 1) {
                        buff.append("、")
                        buff.append(start!!.replace("-", "."))
                        buff.append("-")
                        buff.append(end!!.replace("-", "."))
                    }
                } else {
                    if (null != start && null != end) {
                        buff.append("、")
                        buff.append(start!!.replace("-", "."))
                        buff.append("-")
                        buff.append(end!!.replace("-", "."))
                        start = null
                        end = null
                    } else {
                        buff.append("、")
                        buff.append(old)
                    }
                    if (it == list.size - 1) {
                        buff.append("、")
                        buff.append(date)
                    }
                }
                old = date
            }
            if (buff.isNotEmpty())
                buff.deleteCharAt(0)
        } else {
            buff.append(old)
        }
        return buff.toString()
    }

    private fun checkIsNext(date: String, date1: String): Boolean {
        val num = turnCalendar(date)
        val num1 = turnCalendar(date1)
        val dayOffset = DateUtils.dayOffset(num, num1)
        return Math.abs(dayOffset) == 1
    }

    private fun turnCalendar(date: String): Calendar {
        return Calendar.getInstance().apply {
            time = DateUtils.getDate(date)
        }
    }

    private class ResultAdapter : RecyclerView.Adapter<ResultAdapter.ResultHolder>() {
        var list: MutableList<MakeOrderScreenBean.ScreenBean>? = null
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultHolder {
            return ResultHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_building_result_item, parent, false))
        }

        override fun getItemCount(): Int {
            return list?.size ?: 0
        }

        override fun onBindViewHolder(holder: ResultHolder, position: Int) {
            list!![position].run {
                holder.tv_build.text = screenName
                val date = TextUtils.clean(orderTime)
                Log.e("TAG", "date=$date")
                if (date.isEmpty()) {
                    holder.tv_date.visibility = View.GONE
                } else {
                    holder.tv_date.text = date
                    holder.tv_date.visibility = View.VISIBLE
                }
                holder.tv_statue.text = if (date.isEmpty()) "离线" else "已投满"
            }
        }

        class ResultHolder(view: View) : RecyclerView.ViewHolder(view) {
            var tv_build = view.findViewById<TextView>(R.id.tv_building)
            var tv_statue = view.findViewById<TextView>(R.id.tv_statue)
            var tv_date = view.findViewById<TextView>(R.id.tv_date)
        }
    }

    var onMakeOrderResultListener: OnMakeOrderResultListener? = null

    interface OnMakeOrderResultListener {
        fun reSelect()
        fun nextMakeOrder()
    }
}
