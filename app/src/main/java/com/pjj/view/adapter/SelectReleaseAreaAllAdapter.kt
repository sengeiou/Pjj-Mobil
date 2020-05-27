package com.pjj.view.adapter

import android.annotation.SuppressLint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.module.AllBuildingScreenBean
import com.pjj.module.BuildingElevatorBean
import com.pjj.module.NewMediaScreenBean
import com.pjj.module.xspad.NewMediaData
import com.pjj.module.xspad.XspManage
import com.pjj.utils.CalculateUtils
import com.pjj.utils.ViewUtils

class SelectReleaseAreaAllAdapter : RecyclerView.Adapter<SelectReleaseAreaAllAdapter.SelectReleaseAreaHolder>() {
    var listOld: MutableList<AllBuildingScreenBean.DataBean>? = null
        set(value) {
            field = value
            list = listOld
        }

    fun initAllStatue() {
        selectAllTag = -1
        list?.forEach {
            if (selectAllTag == -1 && it.canUseCount > 0) {
                selectAllTag = 0
            }
        }
        onSelectReleaseAreaAllListener?.selectAllStatue(selectAllTag == 1, selectAllTag == -1)
    }

    private var list: MutableList<AllBuildingScreenBean.DataBean>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    private var positionClick = -1
    private var selectAllTag = -1
    fun updateData() {
        if (positionClick > -1) {
            notifyItemChanged(positionClick)
            checkIsAllSelect(true)
        }
    }

    private fun getPositionData(position: Int): AllBuildingScreenBean.DataBean {
        return list!![position]
    }

    private val onClick = View.OnClickListener {
        val position = it.getTag(R.id.position) as Int
        val bean = getPositionData(position)
        when (it.id) {
            R.id.iv_select -> {
                if (bean.isCanClick) {
                    bean.isSelect = !bean.isSelect
                    if (bean.selectCount == bean.canUseCount) {
                        setElevatorScreenSelectStatue(false, bean.elevatorList)
                        setMediaScreenSelectStatue(false, bean.screenList)
                        bean.selectCount = 0
                    } else {
                        setElevatorScreenSelectStatue(true, bean.elevatorList)
                        setMediaScreenSelectStatue(true, bean.screenList)
                        bean.selectCount = bean.canUseCount
                    }
                    checkIsAllSelect(bean.isSelect)
                    notifyItemChanged(position)
                }
            }
            else -> {
                positionClick = position
                if (bean.inListTag) {
                    XspManage.getInstance().newMediaData.selectAllBuildBean = bean
                    onSelectReleaseAreaAllListener?.inList(bean)
                } else {
                    onSelectReleaseAreaAllListener?.notice("该大厦暂无广告屏")
                }
            }
        }
    }

    fun toggleAllSelect() {
        when (selectAllTag) {
            1 -> {//已全选
                list!!.forEach {
                    it.selectCount = 0
                    it.isSelect = false
                    setElevatorScreenSelectStatue(false, it.elevatorList)
                    setMediaScreenSelectStatue(false, it.screenList)
                }
                selectAllTag = 0
                notifyDataSetChanged()
                onSelectReleaseAreaAllListener?.selectAllStatue(isAll = false, selectAllStatue = false)
            }
            0 -> {//未全选
                list!!.forEach {
                    it.selectCount = it.canUseCount
                    setElevatorScreenSelectStatue(true, it.elevatorList)
                    it.isSelect = true
                    setMediaScreenSelectStatue(true, it.screenList)
                }
                selectAllTag = 1
                notifyDataSetChanged()
                onSelectReleaseAreaAllListener?.selectAllStatue(isAll = true, selectAllStatue = false)
            }
            else -> {
                //所有显示屏均不可用
            }
        }
    }

    private fun setElevatorScreenSelectStatue(select: Boolean, elevatorList: MutableList<BuildingElevatorBean.DataBean.ElevatorListBean>?) {
        elevatorList?.forEach { elevator ->
            elevator.selectCount = if (select) elevator.canUseCount else 0
            elevator.screenList?.forEach { screen ->
                if (screen.screenStatus != "2") {
                    screen.isSelect = select
                }
            }
        }
    }

    private fun setMediaScreenSelectStatue(select: Boolean, screenList: MutableList<NewMediaScreenBean.DataBean.ScreenListBean>?) {
        screenList?.forEach { screen ->
            if (screen.screenStatus != "2") {
                screen.isSelect = select
            }
        }
    }

    private fun checkIsAllSelect(childSelectStatue: Boolean) {
        if (selectAllTag == 1 && !childSelectStatue) {
            selectAllTag = 0
            onSelectReleaseAreaAllListener?.selectAllStatue(isAll = false, selectAllStatue = false)
        } else {
            if (childSelectStatue) {
                var allSelect = true
                list?.forEach {
                    if (allSelect && it.canUseCount != it.selectCount) {
                        allSelect = false
                        //return@loop
                    }
                }
                if (selectAllTag == 0 && allSelect) {
                    selectAllTag = 1
                    onSelectReleaseAreaAllListener?.selectAllStatue(isAll = true, selectAllStatue = false)
                } else if (selectAllTag == 1 && !allSelect) {
                    selectAllTag = 0
                    onSelectReleaseAreaAllListener?.selectAllStatue(isAll = false, selectAllStatue = false)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectReleaseAreaHolder {
        return SelectReleaseAreaHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_select_release_area_all_item, parent, false)).apply {
            iv_select.setOnClickListener(onClick)
            itemView.setOnClickListener(onClick)
            tv_building_address.setCompoundDrawables(ViewUtils.getDrawable(R.mipmap.local).apply {
                setBounds(0, 0, ViewUtils.getDp(R.dimen.dp_11), ViewUtils.getDp(R.dimen.dp_13))
            }, null, null, null)
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: SelectReleaseAreaHolder, position: Int) {
        val bean = list!![position]
        var colorSelectName = ViewUtils.getColor(R.color.color_555555)
        var selectNumColor = ViewUtils.getColor(R.color.color_ff4c4c)
        var allNumColor = ViewUtils.getColor(R.color.color_333333)
        var priceColor = ViewUtils.getColor(R.color.color_ff8c19)
        if (bean.canUseCount == 0) {
            colorSelectName = ViewUtils.getColor(R.color.color_999999)
            selectNumColor = colorSelectName
            allNumColor = colorSelectName
            priceColor = colorSelectName
        }
        with(holder) {
            iv_select.setImageResource(when {
                !bean.isCanClick -> R.mipmap.unclickselect
                bean.isCanClick && bean.selectCount.toString() == bean.screenNum -> R.mipmap.select_red
                bean.isCanClick && bean.selectCount > 0 -> R.mipmap.unselect_tag
                else -> R.mipmap.unselect
            })
            iv_select.setTag(R.id.position, position)
            itemView.setTag(R.id.position, position)
            Glide.with(itemView).load(PjjApplication.filePath + bean.imgName).into(iv_building)
            tv_building.text = bean.communityName
            tv_select_count.text = bean.selectCount.toString()
            tv_select_count.setTextColor(selectNumColor)
            tv_can_select_count.setTextColor(allNumColor)
            tv_price.setTextColor(priceColor)
            tv_sum_price.setTextColor(selectNumColor)
            tv_price_end.setTextColor(selectNumColor)
            tv_select_name.setTextColor(colorSelectName)
            tv_can_select_count.text = "/${bean.screenNum}"
            tv_price.text = CalculateUtils.m1(bean.price)
            tv_sum_price.text = "总价：¥" + CalculateUtils.m1(bean.sumPrice)
            tv_building_address.text = bean.street
        }
    }

    fun getSelectScreenIds(): Boolean {
        val selectList = ArrayList<NewMediaData.ScreenBean>()
        val communityList = ArrayList<OrderAllInfParent>()
        var screenSize: Int
        var elevatorSize: Int
        list?.forEach {
            screenSize = 0
            elevatorSize = 0
            var min = 20000000f
            it.elevatorList?.forEach { elevator ->
                elevator.screenList?.forEach { screen ->
                    if (screen.isSelect) {
                        ++elevatorSize
                        if (min > screen.finalXspPrice) {
                            min = screen.finalXspPrice
                        }
                        selectList.add(NewMediaData.ScreenBean().apply {
                            srceenId = screen.screenId
                            price = screen.finalXspPrice
                            screenName = it.communityName + elevator.eleName + screen.screenName
                            maxAdCount = screen.userCap
                        })
                    }
                }
            }
            it.screenList?.forEach { screen ->
                if (screen.isSelect) {
                    ++screenSize
                    if (min > screen.finalXspPrice) {
                        min = screen.finalXspPrice
                    }
                    selectList.add(NewMediaData.ScreenBean().apply {
                        srceenId = screen.screenId
                        price = screen.finalXspPrice
                        screenName = it.communityName + screen.screenName
                        maxAdCount = screen.userCap
                    })
                }
            }
            if (it.selectCount > 0) {
                communityList.add(it.cloneInf(elevatorSize, screenSize, min))
            }
        }
        if (communityList.size > 0) {
            XspManage.getInstance().newMediaData.communityAllList = communityList
            XspManage.getInstance().newMediaData.screenIdList = selectList
            return true
        }
        return false
    }

    fun filter(string: String) {
        val filterList = ArrayList<AllBuildingScreenBean.DataBean>()
        val filterOtherList = ArrayList<AllBuildingScreenBean.DataBean>()
        listOld?.forEach {
            if (it.communityName.contains(string)) {
                filterList.add(it)
            } else {
                filterOtherList.add(it)
            }
        }
        filterList.addAll(filterOtherList)
        list = filterList
    }

    fun restore() {
        list = listOld
    }

    class SelectReleaseAreaHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv_select = itemView.findViewById<ImageView>(R.id.iv_select)
        var iv_building = itemView.findViewById<ImageView>(R.id.iv_building)
        var tv_building = itemView.findViewById<TextView>(R.id.tv_building)
        var tv_select_count = itemView.findViewById<TextView>(R.id.tv_select_count)
        var tv_can_select_count = itemView.findViewById<TextView>(R.id.tv_can_select_count)
        var iv_more = itemView.findViewById<ImageView>(R.id.iv_more)
        var tv_price = itemView.findViewById<TextView>(R.id.tv_price)
        var tv_sum_price = itemView.findViewById<TextView>(R.id.tv_sum_price)
        var tv_building_address = itemView.findViewById<TextView>(R.id.tv_building_address)
        var tv_select_name = itemView.findViewById<TextView>(R.id.tv_select_name)
        var tv_price_end = itemView.findViewById<TextView>(R.id.tv_price_end)
    }

    var onSelectReleaseAreaAllListener: OnSelectReleaseAreaAllListener? = null

    interface OnSelectReleaseAreaAllListener {
        fun notice(msg: String)
        fun inList(data: AllBuildingScreenBean.DataBean)
        fun selectAllStatue(isAll: Boolean, selectAllStatue: Boolean)

    }

    interface OrderAllInfParent {
        fun getScreenNum(): Int
        fun getBuildingImageName(): String?
        fun getBuildingName(): String?
        fun getElevatorCount(): Int
        fun getScreenCount(): Int
        fun getPrice(): Float
    }
}