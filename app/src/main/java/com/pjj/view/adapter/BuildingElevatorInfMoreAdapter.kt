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
import com.pjj.module.BuildingElevatorBean
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
class BuildingElevatorInfMoreAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var hotData: MutableList<BuildingElevatorBean.DataBean>? = null
        set(value) {
            field = value
            restore()
        }
    var filterList: MutableList<BuildingElevatorBean.DataBean>? = null
    var listOld: MutableList<BuildingElevatorBean.DataBean>? = null
        set(value) {
            selectAll = false
            field = value
            list = value
        }
    private var newList = ArrayList<BuildingElevatorBean.DataBean>()
    private val colorLine = ViewUtils.getColor(R.color.color_f1f1f1)
    private val dp1 = ViewUtils.getDp(R.dimen.dp_1)
    private val buildingChildInfHolder = BuildingChildInfHolder()
    private var list: MutableList<BuildingElevatorBean.DataBean>? = null
        set(value) {
            selectAll = false
            field = value
            notifyDataSetChanged()
            if (!hasClick()) {
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
            //communityNewList.add(new DataBean());
            tag = "暂无资源，为您推荐热门大厦"
        }
        if (TextUtils.isNotEmptyList(hotData)) {
            newList.add(BuildingElevatorBean.DataBean().apply {
                areaCode = tag
            })
            newList.addAll(hotData!!)
        }
        list = newList
    }

    fun filter(name: String) {
        newList.clear()
        val list_ = ArrayList<BuildingElevatorBean.DataBean>()
        listOld?.let {
            list_.addAll(it)
        }
        hotData?.let {
            list_.addAll(it)
        }
        filterList = BuildingElevatorBean.filterAdd(listOld, newList, name)
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
                    orientation = LinearLayout.VERTICAL
                    layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_31))
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
                    holder.tv_elevator_count.text = "$elevatorNum 部电梯"
                    holder.tv_price.text = "¥${CalculateUtils.m1(price)}"
                    holder.childParent.removeAllViews()
                    if (isCanClick) {
                        holder.iv_select.setImageResource(if (isSelect) R.mipmap.select else R.mipmap.unselect)
                    } else {
                        holder.iv_select.setImageResource(R.mipmap.unclickselect)
                    }
                    holder.iv_select.setTag(R.id.position, position)
                    holder.iv_show.setTag(R.id.position, position)
                    if (isShow) {
                        holder.iv_show.setImageResource(R.mipmap.unshowshang)
                        addElevatorChildView(holder.childParent, elevatorList, position)
                    } else {
                        holder.iv_show.setImageResource(R.mipmap.showxia)
                    }
                }
            }
            is GuessHolder -> {
                holder.tv.text = list!![position].areaCode
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun addElevatorChildView(childParent: ViewGroup, elevatorList: List<BuildingElevatorBean.DataBean.ElevatorListBean>?, parentPosition: Int) {
        elevatorList?.forEachIndexed { index, it ->
            val childView = LayoutInflater.from(childParent.context).inflate(R.layout.layout_area_elevator_child_item, childParent, false)
            childParent.addView(childView).apply {
                var iv_elevator_show = childView.findViewById<ImageView>(R.id.iv_elevator_show)
                var iv_elevator_select = childView.findViewById<ImageView>(R.id.iv_elevator_select)
                childView.findViewById<TextView>(R.id.tv_elevator_name).text = it.eleName
                childView.findViewById<TextView>(R.id.tv_screen_count).text = "${it.screenNum}面屏幕"
                iv_elevator_select.setOnClickListener(onClickListener)
                iv_elevator_show.setOnClickListener(onClickListener)
                iv_elevator_select.setTag(R.id.position, parentPosition)
                iv_elevator_show.setTag(R.id.position, parentPosition)
                iv_elevator_select.setTag(R.id.group_position, index)
                iv_elevator_show.setTag(R.id.group_position, index)
                if (it.isCanClick) {
                    iv_elevator_select.setImageResource(if (it.isSelect) R.mipmap.select else R.mipmap.unselect)
                } else {
                    iv_elevator_select.setImageResource(R.mipmap.unclickselect)
                }
                if (it.isShow) {
                    iv_elevator_show.setImageResource(R.mipmap.unshowshang)
                } else {
                    iv_elevator_show.setImageResource(R.mipmap.showxia)
                }
            }
            childParent.addView(getLineView(childParent.context))
            childParent.addView(LinearLayout(childParent.context).apply {
                layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
                orientation = LinearLayout.VERTICAL
                if (it.isShow)
                    addChildView(this, it.screenList, parentPosition, index)
            })
        }
    }

    @SuppressLint("SetTextI18n")
    private fun addChildView(childParent: ViewGroup, screenList: MutableList<BuildingElevatorBean.DataBean.ElevatorListBean.ScreenListBean>, firstPosition: Int, parentPosition: Int) {
        screenList.forEachIndexed { index, it ->
            val childView = LayoutInflater.from(childParent.context).inflate(R.layout.layout_area_building_child_item, childParent, false)
            childParent.addView(childView)
            childParent.addView(getLineView(childParent.context))
            buildingChildInfHolder.update(childView).run {
                if (it.isCanClick) {
                    iv_child_select?.setImageResource(if (it.isSelect) R.mipmap.select else R.mipmap.unselect)
                } else {
                    iv_child_select?.setImageResource(R.mipmap.unclickselect)
                }
                //iv_child_select?.setImageResource(if (it.isSelect) R.mipmap.select else R.mipmap.unselect)
                iv_child_select?.setTag(R.id.position, firstPosition)
                iv_child_select?.setTag(R.id.group_position, parentPosition)
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
                    when (it.screenStatus) { // 1正常 2离线 3今日已满
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
    }

    class BuildingInfHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv_select = view.findViewById<ImageView>(R.id.iv_select)
        var iv_show = view.findViewById<ImageView>(R.id.iv_show)
        var iv_building = view.findViewById<ImageView>(R.id.iv_building)
        var tv_building_name = view.findViewById<TextView>(R.id.tv_building_name)
        var tv_building_local = view.findViewById<TextView>(R.id.tv_building_local)
        var tv_elevator_count = view.findViewById<TextView>(R.id.tv_elevator_count)
        var tv_screen_count = view.findViewById<TextView>(R.id.tv_screen_count)
        var tv_price = view.findViewById<TextView>(R.id.tv_price)
        var childParent = (view as ViewGroup).getChildAt(2) as ViewGroup

        init {
            tv_elevator_count.visibility = View.VISIBLE
        }
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
            R.id.iv_elevator_show -> {
                val parentPosition = it.getTag(R.id.position) as Int
                val group_position = it.getTag(R.id.group_position) as Int
                list!![parentPosition].elevatorList[group_position].isShow = !list!![parentPosition].elevatorList[group_position].isShow
                notifyItemChanged(parentPosition)
            }
            R.id.iv_elevator_select -> {
                val parentPosition = it.getTag(R.id.position) as Int
                val group_position = it.getTag(R.id.group_position) as Int
                var dataBean = list!![parentPosition]
                var elevatorBean = dataBean.elevatorList!![group_position]
                if (checkElevatorListBeanIsAllNotOnLine(elevatorBean.screenList)) {
                    return@OnClickListener
                }
                if (elevatorBean.isSelect) {
                    elevatorBean.isSelect = false
                    elevatorBean.screenList?.forEach { screen ->
                        if (screen.screenStatus != "2")
                            screen.isSelect = false
                    }
                    dataBean.isSelect = checkElevatorSelectAll(dataBean.elevatorList)
                    checkIsAll(true)
                } else {
                    elevatorBean.isSelect = true
                    elevatorBean.screenList?.forEach { screen ->
                        if (screen.screenStatus != "2")
                            screen.isSelect = true
                    }
                    dataBean.isSelect = checkElevatorSelectAll(dataBean.elevatorList)
                    checkIsAll()
                }
                notifyItemChanged(parentPosition)
            }
            R.id.iv_select -> {
                val parentPosition = it.getTag(R.id.position) as Int
                val bean = list!![parentPosition]
                val elevatorList = bean.elevatorList
                if (checkParentElevatorListBeanIsAllNotOnLine(elevatorList)) {
                    return@OnClickListener
                }
                if (bean.isSelect) {
                    bean.isSelect = false
                    elevatorList?.forEach { childIt ->
                        childIt.isSelect = false
                        childIt.screenList?.forEach { it ->
                            if (it.screenStatus != "2")
                                it.isSelect = false
                        }
                    }
                    checkIsAll(true)
                } else {
                    bean.isSelect = true
                    elevatorList?.forEach { childIt ->
                        childIt.isSelect = true
                        childIt.screenList?.forEach { it ->
                            if (it.screenStatus != "2")
                                it.isSelect = true
                        }
                    }
                    checkIsAll()
                }
                notifyItemChanged(parentPosition)
            }
            R.id.iv_child_select -> {
                val parentPosition = it.getTag(R.id.position) as Int
                val group_position = it.getTag(R.id.group_position) as Int
                val childPosition = it.getTag(R.id.child_position) as Int
                var buildingBean = list!![parentPosition]
                val bean = buildingBean.elevatorList!![group_position]//一部电梯
                val screenList = bean.screenList!!
                if (screenList[childPosition].screenStatus == "2") {//离线
                    return@OnClickListener
                }
                if (screenList[childPosition].isSelect) {
                    screenList[childPosition].isSelect = false
                    bean.isSelect = checkChildSelectAll(screenList)
                    buildingBean.isSelect = checkElevatorSelectAll(buildingBean.elevatorList)
                    checkIsAll(true)
                } else {
                    screenList[childPosition].isSelect = true
                    bean.isSelect = checkChildSelectAll(screenList)
                    buildingBean.isSelect = checkElevatorSelectAll(buildingBean.elevatorList)
                    checkIsAll()
                }
                //Log.e("TAG", "iv_child_select parentPosition=$parentPosition  child=$childPosition")
                notifyItemChanged(parentPosition)
            }
        }
    }

    private fun checkParentElevatorListBeanIsAllNotOnLine(elevatorList: List<BuildingElevatorBean.DataBean.ElevatorListBean>?): Boolean {
        elevatorList?.forEach { elevator ->
            elevator.screenList?.forEach {
                if (it.screenStatus != "2") {
                    return false
                }
            }
        }
        return true
    }

    private fun checkElevatorListBeanIsAllNotOnLine(list: MutableList<BuildingElevatorBean.DataBean.ElevatorListBean.ScreenListBean>?): Boolean {
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
/*            list?.forEach {
                if (!it.isSelect) {
                    selectAll_ = false
                    return@forEach
                }
            }*/
            var tagOnline = false
            list?.forEach loop@{ build ->
                build.elevatorList?.forEach { elevator ->
                    elevator.screenList?.forEach {
                        if (it.screenStatus != "2") {
                            tagOnline = true
                            if (!it.isSelect) {
                                selectAll_ = false
                                return@loop
                            }
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

    fun hasClick(): Boolean {
        list?.forEach {
            it.elevatorList?.forEach { elevator ->
                elevator.screenList?.forEach { screen ->
                    if (screen.screenStatus != "2") {
                        return true
                    }
                }
            }
        }
        return false
    }

    private fun checkElevatorSelectAll(elevatorList: MutableList<BuildingElevatorBean.DataBean.ElevatorListBean>?): Boolean {
        var selectAll_ = true
        elevatorList?.forEach {
            if (!it.isSelect) {
                selectAll_ = false
                return@forEach
            }
        }
        return selectAll_
    }

    private fun checkChildSelectAll(screenList: MutableList<BuildingElevatorBean.DataBean.ElevatorListBean.ScreenListBean>): Boolean {
        var selectAll_ = true
        screenList?.forEach {
            if (!it.isSelect && it.screenStatus != "2") {
                selectAll_ = false
                return@forEach
            }
        }
        return selectAll_
    }

    fun changeAllSelect() {
        var onLineScreen = false
        if (selectAll) {
            selectAll = false
            list?.forEach {
                var itOnline = false
                it.elevatorList?.forEach { child ->
                    var elevatorOnline = false
                    child.screenList?.forEach { screen ->
                        if (screen.screenStatus != "2") {
                            itOnline = true
                            elevatorOnline = true
                            onLineScreen = true
                            screen.isSelect = false
                        }
                    }
                    if (elevatorOnline)
                        child.isSelect = false
                }
                if (itOnline)
                    it.isSelect = false
            }
        } else {
            selectAll = true
            list?.forEach {
                it.elevatorList?.forEach { child ->
                    child.screenList?.forEach { screen ->
                        if (screen.screenStatus != "2") {
                            onLineScreen = true
                            screen.isSelect = true
                        }
                    }
                    if (onLineScreen)
                        child.isSelect = true
                }
                if (onLineScreen)
                    it.isSelect = true
            }
        }
        notifyDataSetChanged()
        if (selectAll && !onLineScreen) {
            selectAll = false
        } else {
            onItemSelectListener?.selectAllStatue(selectAll)
        }
    }

    fun getSelectScreenIds(): Boolean {
        val selectList = ArrayList<NewMediaData.ScreenBean>()
        val communityList = ArrayList<OrderElevatorInfAdapter.OrderElevatorInfParent>()
        var screenSize: Int
        var elevatorSize: Int
        list?.forEach {
            screenSize = 0
            elevatorSize = 0
            var min=20000000f
            it.elevatorList?.forEach { elevator ->
                var hasChildSelect = false
                elevator.screenList?.forEach { child ->
                    if (child.isSelect) {
                        ++screenSize
                        if (min > child.finalXspPrice) {
                            min = child.finalXspPrice
                        }
                        hasChildSelect = true
                        selectList.add(NewMediaData.ScreenBean().apply {
                            srceenId = child.screenId
                            price = child.finalXspPrice
                            screenName = child.screenName
                            maxAdCount = child.userCap
                        })
                    }
                }
                if (hasChildSelect)
                    ++elevatorSize
            }
            if (screenSize > 0) {
                communityList.add(it.cloneInf(elevatorSize, screenSize,min))
            }
        }
        if (communityList.size > 0) {
            XspManage.getInstance().newMediaData.elevatorCommunityList = communityList
            XspManage.getInstance().newMediaData.screenIdList = selectList
            return true
        }
        return false
    }

    interface OnItemSelectListener {
        fun selectAllStatue(isAll: Boolean, unClickTag: Boolean = false)
    }
}