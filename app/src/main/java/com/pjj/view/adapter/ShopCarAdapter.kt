package com.pjj.view.adapter

import android.annotation.SuppressLint
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
import com.pjj.module.GoodsListBean
import com.pjj.module.SelectParent
import com.pjj.module.ShopCarBean
import com.pjj.module.xspad.XspManage
import com.pjj.utils.*

class ShopCarAdapter(private var unSelect: Boolean = false) : RecyclerView.Adapter<ShopCarAdapter.ShopCarHolder>() {
    private var childHolder: ShopCarChildHolder
    private var positionGroup = -1
    private var positionChild = -1
    private var goodsCount = -1
    private val dp5 = ViewUtils.getDp(R.dimen.dp_5)
    var allTag = false
        private set
    var list: MutableList<ShopCarBean.DataBean>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
            if (allTag) {
                allTag = false
                onShopCarListener?.isAll(false)
            }
        }

    init {
        childHolder = ShopCarChildHolder()
        //setHasStableIds(true)
    }

    fun notifyCount() {
        list!![positionGroup].goodsList!![positionChild].goodsNum = goodsCount
        if (!list!![positionGroup].goodsList!![positionChild].isSelect) {
            childNotify(positionGroup, positionChild, false)
        } else {
            notifyItemChanged(positionGroup)
        }
        updateMoney()
    }

    fun notifyDelete() {
        val goodsList = list!![positionGroup].goodsList!!
        goodsList.removeAt(positionChild)
        var groupDelete = false
        if (goodsList.isEmpty()) {
            groupDelete = true
            list!!.removeAt(positionGroup)
        }
        if (!TextUtils.isNotEmptyList(list)) {
            notifyDataSetChanged()
            onShopCarListener?.noData()
        } else {
            if (groupDelete) {
                notifyItemRemoved(positionGroup)
                notifyItemRangeChanged(positionGroup, itemCount - positionGroup)
            } else {
                notifyItemChanged(positionGroup)
            }
        }
        updateMoney()
    }

    fun changeAllSelectStatue() {
        allTag = !allTag
        setAllTag()
    }

    private fun setAllTag() {
        list?.forEach {
            var tag = false
            it.goodsList?.forEach { child ->
                if (child.goodsNumberCount > 0) {
                    tag = true
                }
                child.isSelect = allTag
            }
            if (tag) {
                it.isSelect = allTag
            }
        }
        updateMoney()
        notifyDataSetChanged()
    }

    fun getSelectedGoods(): String? {
        val hashMap = HashMap<String, String>()
        val listArray = ArrayList<ShopCarBean.DataBean>()
        var storeId: String? = null
        list?.forEach {
            val list = ArrayList<ShopCarBean.GoodsListBean>()
            val value = StringBuffer()
            it.goodsList?.forEach { child ->
                if (child.isSelect && child.goodsNumberCount > 0) {
                    list.add(child)
                    //&${child.specificId}
                    value.append("${child.goodsId}&${child.goodsNum}")
                    value.append(",")
                }
            }
            storeId = it.storeId
            if (list.isNotEmpty()) {
                value.deleteCharAt(value.length - 1)
                hashMap[it.storeId] = value.toString()
                listArray.add(ShopCarBean.DataBean().apply {
                    storeId = it.storeId
                    storeName = it.storeName
                    goodsList = list
                })
            }
        }
        XspManage.getInstance().integralGoods.storeId = storeId
        XspManage.getInstance().integralGoods.goods = listArray
        return if (hashMap.isEmpty()) {
            null
        } else {
            JsonUtils.toJsonString(hashMap).replace("\"", "\\\"")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopCarHolder {
        return ShopCarHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_shop_car_item, parent, false)).apply {
            if (unSelect) {
                iv_all_select.visibility = View.INVISIBLE
                val layoutParams = iv_all_select.layoutParams
                layoutParams.width = dp5
                iv_all_select.layoutParams = layoutParams
            } else {
                iv_all_select.setOnClickListener(onClick)
            }
        }
    }

    override fun getItemCount(): Int {
        return list?.size ?: 0
    }

    override fun onBindViewHolder(holder: ShopCarHolder, position: Int) {
        holder.iv_all_select.setTag(R.id.position, position)
        holder.iv_all_select.setImageResource(if (list!![position].isSelect) R.mipmap.select else R.mipmap.unselect)
        Log.e("TAG", "onBindViewHolder ${holder.ll_parent.childCount}  position=$position")
        createChildView(holder.ll_parent, position)
    }

    @SuppressLint("SetTextI18n")
    private fun createChildView(parent: ViewGroup, position: Int) {
        val bean = list!![position].goodsList
        if (parent.childCount > 0)
            parent.removeAllViews()
        bean?.forEachIndexed { positionChild, it ->
            val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_shop_car_child_item, parent, false)
            childHolder.updateView(view)
            parent.addView(view)
            childHolder.iv_select.setImageResource(if (it.isSelect) R.mipmap.select else R.mipmap.unselect)
            childHolder.tv_price.text = "¥${CalculateUtils.m1(it.goodsPrice)}"
            childHolder.tv_count.text = it.goodsNum.toString()
            childHolder.tv_goods.text = it.goodsName
            Glide.with(parent).load(PjjApplication.integralFilePath + it.goodsPicture).into(childHolder.iv_goods)
            //添加监听
            childHolder.tv_add.setOnClickListener(onClick)
            childHolder.tv_subtraction.setOnClickListener(onClick)
            childHolder.iv_select.setOnClickListener(onClick)
            childHolder.tv_delete.setOnClickListener(onClick)
            //父类标识
            childHolder.tv_add.setTag(R.id.position, position)
            childHolder.tv_subtraction.setTag(R.id.position, position)
            childHolder.iv_select.setTag(R.id.position, position)
            childHolder.tv_delete.setTag(R.id.position, position)
            //子类标识
            childHolder.tv_add.setTag(R.id.child_position, positionChild)
            childHolder.tv_subtraction.setTag(R.id.child_position, positionChild)
            childHolder.iv_select.setTag(R.id.child_position, positionChild)
            childHolder.tv_delete.setTag(R.id.child_position, positionChild)
            if (it.goodsNumberCount == 0) {
                childHolder.iv_no_goods.visibility = View.VISIBLE
                childHolder.ll_count.visibility = View.GONE
                childHolder.tv_goods.setTextColor(ViewUtils.getColor(R.color.color_999999))
                childHolder.tv_price.setTextColor(ViewUtils.getColor(R.color.color_999999))
                childHolder.tv_goods_return_type.setTextColor(ViewUtils.getColor(R.color.color_999999))
                childHolder.tv_goods_return_type.background = ColorDrawable(ViewUtils.getColor(R.color.color_e3e3e3))
                childHolder.iv_select.setImageResource(R.mipmap.unclickselect)
            } else {
                childHolder.tv_goods.setTextColor(ViewUtils.getColor(R.color.color_333333))
                childHolder.tv_price.setTextColor(ViewUtils.getColor(R.color.color_333333))
                childHolder.tv_goods_return_type.setTextColor(ViewUtils.getColor(R.color.color_theme))
                childHolder.tv_goods_return_type.background = ColorDrawable(ViewUtils.getColor(R.color.color_e6f6fd))
                childHolder.iv_no_goods.visibility = View.GONE
                childHolder.ll_count.visibility = View.VISIBLE
            }
            childHolder.tv_subtraction.setImageResource(if (it.goodsNum == 1) R.mipmap.mall_jian_un else R.mipmap.mall_jian)
            if (unSelect) {
                childHolder.tv_count.visibility = View.GONE
                childHolder.tv_subtraction.visibility = View.GONE
                childHolder.tv_add.visibility = View.GONE

                val layoutParams = childHolder.iv_select.layoutParams
                layoutParams.width = dp5
                childHolder.iv_select.layoutParams = layoutParams
                childHolder.iv_select.visibility = View.INVISIBLE

            }
        }
    }


    private val onClick = View.OnClickListener {
        when (it.id) {
            R.id.tv_delete -> {
                val position = it.getTag(R.id.position) as Int
                val child_position = it.getTag(R.id.child_position) as Int
                positionGroup = position
                positionChild = child_position
                onShopCarListener?.delete(list!![position].goodsList!![child_position]!!.shoppingCartId)
            }
            R.id.iv_all_select -> {
                val position = it.getTag(R.id.position) as Int
                val shopCarBean = list!![position]
                val tag = !shopCarBean.isSelect
                shopCarBean.isSelect = tag
                changeSelectStatue(tag, shopCarBean.goodsList)
                notifyItemChanged(position)
                checkAllSelect(tag)
                updateMoney()
            }
            R.id.iv_select -> {
                val position = it.getTag(R.id.position) as Int
                val child_position = it.getTag(R.id.child_position) as Int
                if (list!![position].goodsList!![child_position].goodsNumberCount > 0) {
                    childNotify(position, child_position)
                    updateMoney()
                }
            }
            R.id.tv_add -> {
                val position = it.getTag(R.id.position) as Int
                val child_position = it.getTag(R.id.child_position) as Int
                val dataBean = list!![position]
                val bean = dataBean.goodsList!![child_position]
                val count = bean.goodsNum
                val max = bean.goodsNumberCount
                if (max == count) {
                    //最大值
                    onShopCarListener?.notice("已达到最大库存")
                } else {
                    goodsCount = count + 1
                    positionGroup = position
                    positionChild = child_position
                    onShopCarListener?.changeCount(bean.shoppingCartId, 1, position, child_position, true)
                }
            }
            R.id.tv_subtraction -> {
                val position = it.getTag(R.id.position) as Int
                val child_position = it.getTag(R.id.child_position) as Int
                val dataBean = list!![position]
                val bean = dataBean.goodsList!![child_position]
                val count = bean.goodsNum
                if (count > 1) {
                    goodsCount = count - 1
                    positionGroup = position
                    positionChild = child_position
                    onShopCarListener?.changeCount(bean.shoppingCartId, 1, position, child_position, false)
                }
//                else
//                    onShopCarListener?.notice("最少购买一件哦！")
            }
        }
    }

    private fun updateMoney() {
        val calculateMoney = calculateMoney()
        onShopCarListener?.updatePrice(calculateMoney[0], calculateMoney[1])
    }

    private fun childNotify(position: Int, child_position: Int, tagSelect: Boolean = true) {
        val dataBean = list!![position]
        val bean = dataBean.goodsList!![child_position]
        if (tagSelect) {
            val tag = !bean.isSelect
            bean.isSelect = tag
            dataBean.isSelect = checkChildListSelect(dataBean.goodsList)
            checkAllSelect(tag)
        }
        notifyItemChanged(position)
    }

    private fun checkAllSelect(childTag: Boolean) {
        val tag = checkListSelect(list)
        if (tag != allTag) {
            allTag = tag
            onShopCarListener?.isAll(tag)
        }
    }

    private fun <T : SelectParent> changeSelectStatue(tag: Boolean, list: MutableList<T>?) {
        list?.forEach {
            it.isSelect = tag
        }
    }

    private fun checkChildListSelect(list: MutableList<ShopCarBean.GoodsListBean>?): Boolean {
        list?.forEach {
            if (!it.isSelect && it.goodsNumberCount > 0) {
                return false
            }
        }
        return true
    }

    private fun <T : SelectParent> checkListSelect(list: MutableList<T>?): Boolean {
        list?.forEach {
            if (!it.isSelect) {
                return false
            }
        }
        return true
    }

    private fun calculateMoney(): FloatArray {
        val array = FloatArray(2)
        list?.forEach {
            it.goodsList?.forEach { child ->
                if (child.isSelect) {
                    Log.e("TAG", "has select ${child.goodsName}-${child.goodsNum}")
                    array[0] += (child.goodsPrice * child.goodsNum)
                    array[1] += (child.postage)
                }
            }
        }
        return array
    }

    class ShopCarHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv_all_select = view.findViewById<ImageView>(R.id.iv_all_select)
        var ll_parent = view.findViewById<LinearLayout>(R.id.ll_parent)
    }

    private inner class ShopCarChildHolder {
        lateinit var iv_select: ImageView
        lateinit var iv_goods: ImageView
        lateinit var iv_no_goods: ImageView
        lateinit var ll_count: LinearLayout
        lateinit var tv_count: TextView
        lateinit var tv_goods_return_type: TextView
        lateinit var tv_price: TextView
        lateinit var tv_goods: TextView
        lateinit var tv_add: ImageView
        lateinit var tv_subtraction: ImageView
        lateinit var tv_delete: TextView
        fun updateView(view: View) {
            iv_select = view.findViewById(R.id.iv_select)
            iv_goods = view.findViewById(R.id.iv_goods)
            tv_goods_return_type = view.findViewById(R.id.tv_goods_return_type)
            iv_no_goods = view.findViewById(R.id.iv_no_goods)
            ll_count = view.findViewById(R.id.ll_count)
            tv_goods = view.findViewById(R.id.tv_goods)
            tv_count = view.findViewById(R.id.tv_count)
            tv_price = view.findViewById(R.id.tv_price)
            tv_add = view.findViewById(R.id.tv_add)
            tv_subtraction = view.findViewById(R.id.tv_subtraction)
            tv_delete = view.findViewById(R.id.tv_delete)
        }
    }

    var onShopCarListener: OnShopCarListener? = null

    interface OnShopCarListener {
        fun isAll(select: Boolean)
        fun updatePrice(price: Float, post: Float)
        fun changeCount(shoppingCartId: String, goodsNum: Int, groupPosition: Int, childPosition: Int, tag: Boolean)
        fun delete(shoppingCartId: String)
        fun notice(msg: String)
        fun noData()
    }
}