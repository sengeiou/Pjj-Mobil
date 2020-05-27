package com.pjj.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
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
import com.pjj.module.MediaOrderInfBean
import com.pjj.module.NewMediaBuildingBean
import com.pjj.module.NewMediaScreenBean
import com.pjj.module.xspad.NewMediaData
import com.pjj.module.xspad.XspManage
import com.pjj.utils.CalculateUtils
import com.pjj.utils.Log
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils

/**
 * Created by XinHeng on 2018/12/03.
 * describe：
 */
class BuildingInfNewMediaMoreAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var hotData: MutableList<NewMediaBuildingBean.CommunityListBean>? = null
        set(value) {
            field = value
            restore()
        }
    var listOld: MutableList<NewMediaBuildingBean.CommunityListBean>? = null
        set(value) {
            field = value
            list = value
        }
    var filterList: MutableList<NewMediaBuildingBean.CommunityListBean>? = null
    private var newList = ArrayList<NewMediaBuildingBean.CommunityListBean>()
    private val colorLine = ViewUtils.getColor(R.color.color_f1f1f1)
    private val dp1 = ViewUtils.getDp(R.dimen.dp_1)
    private val buildingChildInfHolder = BuildingChildInfHolder()
    private var list: MutableList<NewMediaBuildingBean.CommunityListBean>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
            if (!hasClickTag()) {
                onItemSelectListener?.selectAllStatue(false, unClickTag = true)
            }
        }

    fun restore() {
        newList.clear()
        listOld?.let {
            newList.addAll(it)
        }
        var tag = "暂无更多搜索结果，为您推荐热门大厦"
        if (newList.size == 0) {
            tag = "暂无资源，为您推荐热门大厦"
        }
        if (TextUtils.isNotEmptyList(hotData)) {
            newList.add(NewMediaBuildingBean.CommunityListBean().apply {
                areaCode = tag
            })
            newList.addAll(hotData!!)
        }

        list = newList
    }

    fun filter(name: String) {
        newList.clear()
        val list_ = ArrayList<NewMediaBuildingBean.CommunityListBean>()
        listOld?.let {
            list_.addAll(it)
        }
        hotData?.let {
            list_.addAll(it)
        }
        filterList = NewMediaBuildingBean.filterAdd(list_, newList, name)
        list = newList
    }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return list!![position].hashCode().toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            -1 -> {
                BuildingFliterNoInfHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_list_no_date, parent, false))
            }
            0 -> {
                GuessHolder(LinearLayout(parent.context).apply {
                    layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_31))
                    orientation = LinearLayout.VERTICAL
                    background = ColorDrawable(ViewUtils.getColor(R.color.color_f1f1f1))
                    addView(TextView(parent.context).apply {
                        text = "暂无更多搜索结果，为您推荐热门大厦"
                        setTextColor(ViewUtils.getColor(R.color.color_666666))
                        setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_12))
                        layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
                        setPadding(ViewUtils.getDp(R.dimen.dp_19), 0, 0, 0)
                        gravity = Gravity.CENTER_VERTICAL
                    })
                })
            }
            else -> {
                LinearLayout(parent.context).apply {
                    layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
                    orientation = LinearLayout.VERTICAL
                    addView(LayoutInflater.from(context).inflate(R.layout.layout_area_building_item, this, false))
                    addView(getLineView(context))
                    addView(LinearLayout(context).apply {
                        layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
                        orientation = LinearLayout.VERTICAL
                    })
                }.let {
                    BuildingInfHolder(it).apply {
                        iv_select.setOnClickListener(onClickListener)
                        iv_show.setOnClickListener(onClickListener)
                    }
                }
            }
        }
    }

    private fun getLineView(context: Context): View {
        return View(context).apply {
            background = ColorDrawable(colorLine)
            layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, dp1)
        }
    }

    override fun getItemViewType(position: Int): Int {
        /*if (position == 0 && list!![0].communityName == null) {
            return -1 //过滤后无数据
        }*/
        return when (list!![position].communityName) {
            null -> 0 //猜你所想
            else -> 1 //数据
        }
    }

    override fun getItemCount(): Int {
        return (list?.size ?: 0) + 0
    }

    private val requestOptions = RequestOptions().error(R.mipmap.building)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.tag = position
        when (holder) {
            is BuildingInfHolder -> {
                list!![position].run {
                    Glide.with(holder.iv_building).load(PjjApplication.filePath + imgName).apply(requestOptions).into(holder.iv_building)
                    holder.tv_building_name.text = communityName
                    holder.tv_building_local.text = this.position
                    holder.tv_screen_count.text = "$screenNum 面屏幕"
                    holder.tv_price.text = "¥${CalculateUtils.m1(price)}"
                    holder.childParent.removeAllViews()

                    holder.iv_select.setTag(R.id.position, position)
                    holder.iv_show.setTag(R.id.position, position)
                    val parentClickTag = if (isShow) {
                        holder.iv_show.setImageResource(R.mipmap.unshowshang)
                        addChildView(holder.childParent, screenList, position)
                    } else {
                        holder.iv_show.setImageResource(R.mipmap.showxia)
                        getParentClick(screenList)
                    }
                    if (parentClickTag) {
                        holder.iv_select.setImageResource(if (isSelect) R.mipmap.select else R.mipmap.unselect)
                    } else {
                        holder.iv_select.setImageResource(R.mipmap.unclickselect)
                    }
                }
            }
            is GuessHolder -> {
                holder.tv.text = list!![position].areaCode
            }
        }
    }

    private fun getParentClick(screenList: List<NewMediaScreenBean.DataBean.ScreenListBean>?): Boolean {
        screenList?.forEach {
            if (it.screenStatus != "2") {
                return true
            }
        }
        return false
    }

    @SuppressLint("SetTextI18n")
    private fun addChildView(childParent: ViewGroup, screenList: List<NewMediaScreenBean.DataBean.ScreenListBean>?, parentPosition: Int): Boolean {
        var parentClickTag = false
        screenList?.forEachIndexed { index, it ->
            val childView = LayoutInflater.from(childParent.context).inflate(R.layout.layout_area_building_child_item, childParent, false)
            childParent.addView(childView)
            childParent.addView(getLineView(childParent.context))
            if (it.screenStatus != "2") {
                parentClickTag = true
            }
            buildingChildInfHolder.update(childView).run {
                if (it.screenStatus != "2")
                    iv_child_select?.setImageResource(if (it.isSelect) R.mipmap.select else R.mipmap.unselect)
                else
                    iv_child_select?.setImageResource(R.mipmap.unclickselect)
                iv_child_select?.setTag(R.id.position, parentPosition)
                iv_child_select?.setTag(R.id.child_position, index)
                Log.e("TAG", "parentPosition=$parentPosition  child=$index")
                tv_screen_name?.text = it.screenName
                iv_child_select?.setOnClickListener(onClickListener)
                tv_screen_price?.text = CalculateUtils.m1(it.finalXspPrice) + "元/天"
                /*tv_screen_price_type?.text = when (it.cooperationMode) {
                    "1" -> "地方自营"
                    "2" -> "联合运营"
                    else -> "公司运营"
                }*/
                tv_statue?.let { view ->
                    when (it.screenStatus) {// 1正常 2离线 3今日已满
                        "2" -> {//离线
                            view.visibility = View.VISIBLE
                            view.setTextColor(ViewUtils.getColor(R.color.color_ea4a4a))
                            view.text = "离线"
                            view.background = ColorDrawable(ViewUtils.getColor(R.color.color_fde6e6))
                        }
                        "3" -> {//今日已满
                            view.visibility = View.VISIBLE
                            view.setTextColor(ViewUtils.getColor(R.color.color_666666))
                            view.text = "今日已满"
                            view.background = ColorDrawable(ViewUtils.getColor(R.color.color_f4f5f9))
                        }
                        else -> {
                            view.visibility = View.GONE
                        }
                    }
                }
            }
        }
        return parentClickTag
    }

    class BuildingInfHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv_select = view.findViewById<ImageView>(R.id.iv_select)
        var iv_show = view.findViewById<ImageView>(R.id.iv_show)
        var iv_building = view.findViewById<ImageView>(R.id.iv_building)
        var tv_building_name = view.findViewById<TextView>(R.id.tv_building_name)
        var tv_building_local = view.findViewById<TextView>(R.id.tv_building_local)
        var tv_screen_count = view.findViewById<TextView>(R.id.tv_screen_count)
        var tv_price = view.findViewById<TextView>(R.id.tv_price)
        var childParent = (view as ViewGroup).getChildAt(2) as ViewGroup
    }

    class BuildingChildInfHolder {
        //child
        var iv_child_select: ImageView? = null
        var tv_screen_name: TextView? = null
        var tv_screen_type: TextView? = null
        var tv_screen_price_type: TextView? = null
        var tv_screen_price: TextView? = null
        var tv_statue: TextView? = null
        fun update(view: View): BuildingChildInfHolder {
            iv_child_select = view.findViewById<ImageView>(R.id.iv_child_select)
            tv_screen_name = view.findViewById<TextView>(R.id.tv_screen_name)
            tv_screen_type = view.findViewById<TextView>(R.id.tv_screen_type)
            tv_screen_price_type = view.findViewById<TextView>(R.id.tv_screen_price_type)
            tv_screen_price = view.findViewById<TextView>(R.id.tv_price)
            tv_statue = view.findViewById<TextView>(R.id.tv_statue)
            return this
        }
    }

    class BuildingFliterNoInfHolder(view: View) : RecyclerView.ViewHolder(view)
    class GuessHolder(view: ViewGroup) : RecyclerView.ViewHolder(view) {
        //猜你所想
        var tv = view.getChildAt(0) as TextView
    }

    var onItemSelectListener: OnItemSelectListener? = null
    private var onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.iv_show -> {
                val parentPosition = it.getTag(R.id.position) as Int
                list!![parentPosition].isShow = !list!![parentPosition].isShow
                notifyItemChanged(parentPosition)
            }
            R.id.iv_select -> {
                val parentPosition = it.getTag(R.id.position) as Int
                val bean = list!![parentPosition]
                val screenList = bean.screenList
                if (checkChildIsAllNotOnLine(screenList)) {//子元素全部离线
                    return@OnClickListener
                }
                if (bean.isSelect) {
                    bean.isSelect = false
                    screenList?.forEach { childIt ->
                        if (childIt.screenStatus != "2")
                            childIt.isSelect = false
                    }
                    checkIsAll(true)
                } else {
                    bean.isSelect = true
                    screenList?.forEach { childIt ->
                        if (childIt.screenStatus != "2")
                            childIt.isSelect = true
                    }
                    checkIsAll()
                }
                notifyItemChanged(parentPosition)
            }
            R.id.iv_child_select -> {
                val parentPosition = it.getTag(R.id.position) as Int
                val childPosition = it.getTag(R.id.child_position) as Int
                val bean = list!![parentPosition]
                val screenList = bean.screenList!!
                if (screenList[childPosition].screenStatus == "2") {//离线
                    return@OnClickListener
                }
                if (screenList[childPosition].isSelect) {
                    screenList[childPosition].isSelect = false
                    bean.isSelect = checkChildSelectAll(screenList)
                    checkIsAll(true)
                } else {
                    screenList[childPosition].isSelect = true
                    bean.isSelect = checkChildSelectAll(screenList)
                    checkIsAll()
                }
                //Log.e("TAG", "iv_child_select parentPosition=$parentPosition  child=$childPosition")
                notifyItemChanged(parentPosition)
            }
        }
    }

    private fun checkChildIsAllNotOnLine(list: MutableList<NewMediaScreenBean.DataBean.ScreenListBean>?): Boolean {
        list?.forEach {
            if (it.screenStatus != "2") {
                return false
            }
        }
        return true
    }

    private var selectAll = false
    private fun checkIsAll(mustNotAll: Boolean = false) {
        if (mustNotAll) {
            if (selectAll) {
                selectAll = false
                onItemSelectListener?.selectAllStatue(false)
            }
        } else {
            var selectAll_ = true
            var tagOnline = false
            list?.forEach loop@{ first ->
                first.screenList?.forEach {
                    if (it.screenStatus != "2") {
                        tagOnline = true
                        if (!it.isSelect) {
                            selectAll_ = false
                            return@loop
                        }
                    }
                }
            }
            if (!tagOnline) {
                selectAll_ = false
            }
            if (selectAll_ != this.selectAll) {
                selectAll = selectAll_
                onItemSelectListener?.selectAllStatue(selectAll)
            }
        }
    }

    private fun checkChildSelectAll(screenList: List<NewMediaScreenBean.DataBean.ScreenListBean>?): Boolean {
        var selectAll_ = true
        screenList?.forEach {
            if (!it.isSelect && it.screenStatus != "2") {
                selectAll_ = false
                return@forEach
            }
        }
        return selectAll_
    }

    fun hasClickTag(): Boolean {
        list?.forEach {
            it.screenList?.forEach { child ->
                if (child.screenStatus != "2") {
                    return true
                }
            }
        }
        return false
    }

    fun changeAllSelect() {
        var onLine = false
        if (selectAll) {
            selectAll = false
            list?.forEach {
                var childItOnline = false
                it.screenList?.forEach { child ->
                    if (child.screenStatus != "2") {
                        onLine = true
                        childItOnline = true
                        child.isSelect = false
                    }
                }
                if (childItOnline)
                    it.isSelect = false
            }
        } else {
            selectAll = true
            list?.forEach {
                var childItOnline = false
                it.screenList?.forEach { child ->
                    if (child.screenStatus != "2") {
                        onLine = true
                        childItOnline = true
                        child.isSelect = true
                    }
                }
                if (childItOnline)
                    it.isSelect = true
            }
        }
        notifyDataSetChanged()
        if (selectAll && !onLine)
            selectAll = false
        else
            onItemSelectListener?.selectAllStatue(selectAll)
    }

    fun getSelectScreenIds(): Boolean {
        val selectList = ArrayList<NewMediaData.ScreenBean>()
        val communityList = ArrayList<MediaOrderInfBean.OrderScreenListBean>()
        var childSize: Int
        list?.forEach {
            childSize = 0
            var min = 200000000f
            it.screenList?.forEach { child ->
                if (child.isSelect) {
                    ++childSize
                    if (min > child.finalXspPrice) {
                        min = child.finalXspPrice
                    }
                    selectList.add(NewMediaData.ScreenBean().apply {
                        srceenId = child.screenId
                        price = child.finalXspPrice
                        screenName = child.screenName
                        maxAdCount = child.userCap
                    })
                }
            }
            if (childSize > 0) {
                communityList.add(it.cloneInf(childSize,min))
            }
        }
        if (communityList.size > 0) {
            XspManage.getInstance().newMediaData.communityList = communityList
            XspManage.getInstance().newMediaData.screenIdList = selectList
            return true
        }
        return false
    }

    interface OnItemSelectListener {
        fun selectAllStatue(isAll: Boolean, unClickTag: Boolean = false)
    }
}