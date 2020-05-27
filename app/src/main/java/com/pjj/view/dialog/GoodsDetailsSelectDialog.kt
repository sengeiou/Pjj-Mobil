package com.pjj.view.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pjj.R
import com.pjj.module.GoodsDetailBean
import com.pjj.module.ShopCarBean
import com.pjj.module.xspad.XspManage
import com.pjj.utils.Log
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.custom.WordWrapViewGroup
import kotlinx.android.synthetic.main.layout_goods_dateil_item.*
import kotlinx.android.synthetic.main.layout_goods_dateil_item.iv_close
import kotlin.collections.ArrayList

class GoodsDetailsSelectDialog(context: Context) : FullWithNoTitleDialog(context, 0) {
    private var sumCount = 0
    private var count = 1
    private var listAll: MutableList<GoodsDetailBean.DataBean.GoodSpecificInfoAllBean>? = null
    private lateinit var listView: MutableList<WordWrapViewGroup<TextView, *>>
    private lateinit var typeArray: Array<GoodsBean?>
    private var storeId: String? = null
    private var oldPrice = ""
    private var oldCount = ""

    companion object {
        val SHOP_CAR = "shop_car"
        val BUY_GOODS = "buy_goods"
    }

    private var type: String = SHOP_CAR

    init {
        setContentView(R.layout.layout_goods_dateil_item)
        iv_close.setOnClickListener(onClick)
        tv_add.setOnClickListener(onClick)
        tv_subtraction.setOnClickListener(onClick)
        tv_sure.setOnClickListener(onClick)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_close -> if (isShowing) dismiss()
            R.id.tv_add -> {
                if (count < sumCount) {
                    ++count
                    tv_count.text = count.toString()
                }
            }
            R.id.tv_subtraction -> {
                if (count >= 1) {
                    --count
                    tv_count.text = count.toString()
                }
            }
            R.id.tv_sure -> {
                if (allTypeHasSelected()) {
                    val specificInfoAllBean = findSameSelect(changeTypeString())
                    specificInfoAllBean?.let {
                        when (type) {
                            SHOP_CAR -> onGoodsDetailsSelectDialogListener?.addShopCar(it.goodsId, it.specificId, count)
                            else -> {
                                val integralGoods = XspManage.getInstance().integralGoods
                                integralGoods.count = count
                                integralGoods.integraGoodsId = "{\\\"$storeId\\\":\\\"${it.goodsId}&${it.specificId}&$count\\\"}"
                                integralGoods.integral = it.goodsPrice.toString()
                                integralGoods.goods = ArrayList<ShopCarBean.DataBean>().apply {
                                    add(ShopCarBean.DataBean().apply {
                                        goodsList = ArrayList<ShopCarBean.GoodsListBean>().apply {
                                            add(it.cloneShopCar())
                                        }
                                    })
                                }
                                onGoodsDetailsSelectDialogListener?.nowBuy()
                            }
                        }
                        dismiss()
                    }
                } else {
                    onGoodsDetailsSelectDialogListener?.showNotice(getExplainString())
                }
            }
        }
    }

    fun updateType(type: String) {
        this.type = type
    }

    @SuppressLint("SetTextI18n")
    fun updateData(storeId: String, list: MutableList<GoodsDetailBean.DataBean.GoodSpecificTypeBean>?,
                   listAll: MutableList<GoodsDetailBean.DataBean.GoodSpecificInfoAllBean>?,
                   goodsNumberCount: Int, goodsPic: String, goodsName: String, goodsPrice: String) {
        val stringBuffer = StringBuffer()
        this.listAll = listAll
        this.storeId = storeId
        stringBuffer.append("请选择 ")
        val dp14 = ViewUtils.getDp(R.dimen.dp_13)
        val rlCount = rl_count
        typeArray = arrayOfNulls(list?.size ?: 0)
        listView = ArrayList(list?.size ?: 0)
        ll_parent.removeAllViews()
        list?.forEachIndexed { index, it ->
            stringBuffer.append(it.typeName)
            stringBuffer.append(" ")
            typeArray[index] = GoodsBean().apply {
                title = it.typeName
            }
            ll_parent.addView(WordWrapViewGroup<TextView, GoodsDetailBean.DataBean.GoodSpecificTypeBean.SpecificInfoListBean>(context).apply {
                setPadding(dp14, dp10, dp14, dp14)
                listView.add(this)
                onWordWrapViewGroupListener = this@GoodsDetailsSelectDialog.onWordWrapViewGroupListener
                setContext(index, it.typeName, it.specificInfoList)
            })
            ll_parent.addView(getLineView())
        }
        ll_parent.addView(rlCount)
        tv_explain.text = stringBuffer.toString()
        sumCount = goodsNumberCount
        if (sumCount < 1) {
            count = 0
            tv_count.text = count.toString()
        }
        oldCount = "库存${goodsNumberCount}件"
        tv_all_count.text = oldCount
        oldPrice = "¥$goodsPrice"
        tv_price.text = oldPrice
        tv_goods.text = goodsName
        Glide.with(context).load(goodsPic).into(iv_goods)
    }

    fun restore() {
        listView.forEach {
            it.cancelSelectUpdateStatue()
        }
        tv_all_count.text = oldCount
        tv_price.text = oldPrice
    }

    private fun getLineView(): View {
        return View(context).apply {
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_1))
            background = ColorDrawable(ViewUtils.getColor(R.color.color_f1f1f1))
        }
    }

    private val dp24 = ViewUtils.getDp(R.dimen.dp_24)
    private val dp10 = ViewUtils.getDp(R.dimen.dp_10)
    private val onWordWrapViewGroupListener = object : WordWrapViewGroup.OnWordWrapViewGroupListener<TextView, GoodsDetailBean.DataBean.GoodSpecificTypeBean.SpecificInfoListBean> {
        override fun getChildView(text: GoodsDetailBean.DataBean.GoodSpecificTypeBean.SpecificInfoListBean): TextView {
            return TextView(context).apply {
                layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp24)
                gravity = Gravity.CENTER_VERTICAL
                setPadding(dp10, 0, dp10, 0)
                this.text = text.specificName
                setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_12))
                restoreView(this)
            }
        }

        override fun getTitleView(text: String): View {
            return TextView(context).apply {
                this.text = text
                setTextColor(Color.BLACK)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_14))
            }
        }

        override fun restoreView(view: TextView) {
            Log.e("TAG", "restoreView title=${view.text}")
            view.setTextColor(Color.BLACK)
            view.background = ColorDrawable(ViewUtils.getColor(R.color.color_f1f1f1))
        }

        override fun updateSelectView(viewSelect: TextView, data: GoodsDetailBean.DataBean.GoodSpecificTypeBean.SpecificInfoListBean, index: Int) {
            viewSelect.setTextColor(ViewUtils.getColor(R.color.color_018ed3))
            viewSelect.background = ViewUtils.getDrawable(R.drawable.shape_theme_side_1_fill_cdedfd)
            typeArray[index]!!.content = data.specificName
            updateSelectExplain()

        }

        override fun cancelSelect(index: Int) {
            typeArray[index]!!.content = null
            updateSelectExplain()
        }

        override fun setUnSelect(view: TextView) {
            Log.e("TAG", "setUnSelect title=${view.text}")
            view.setTextColor(ViewUtils.getColor(R.color.color_999999))
            view.background = ColorDrawable(ViewUtils.getColor(R.color.color_f1f1f1))
        }

        override fun initCheckCanSelect(data: GoodsDetailBean.DataBean.GoodSpecificTypeBean.SpecificInfoListBean): Boolean {
            val name = data.specificName
            return initCheckCanSelect(name, listAll)
        }

        override fun checkCanSelect(data: GoodsDetailBean.DataBean.GoodSpecificTypeBean.SpecificInfoListBean, index: Int): Boolean {
            val name = data.specificName
            return checkCanSelect(name, -1)
        }

        override fun checkCanSelectForSelfArray(data: GoodsDetailBean.DataBean.GoodSpecificTypeBean.SpecificInfoListBean, index: Int): Boolean {
            val name = data.specificName
            return checkCanSelect(name, index)
        }

        override fun checkDefaultSelect(data: GoodsDetailBean.DataBean.GoodSpecificTypeBean.SpecificInfoListBean): Boolean {
            return false
        }
    }

    /**
     * 初始化检测
     */
    private fun initCheckCanSelect(name: String, listAll: MutableList<GoodsDetailBean.DataBean.GoodSpecificInfoAllBean>?): Boolean {
        val tag = false
        listAll?.forEach {
            val split = it.specificPath.split(",")
            Log.e("TAG", "${it.specificPath} name=$name ")
            if (split.size == typeArray.size && split.contains(name)) {
                Log.e("TAG", "initCheckCanSelect true $name")
                return true
            }
        }
        return tag
    }

    /**
     * 检测是否可选
     */
    private fun checkCanSelect(name: String, index: Int): Boolean {
        val tag = false
        listAll?.forEach {
            val split = it.specificPath.split(",")
            val list = changeTypeArray(index)
            Log.e("TAG", "${it.specificPath} name=$name  ${list.size}")
            if (split.size == typeArray.size && split.contains(name) && checkArray(split, list)) {
                Log.e("TAG", "checkCanSelect true $name")
                return true
            }
        }
        return tag
    }

    /**
     * 更新提示
     */
    private fun updateSelectExplain() {
        tv_explain.text = getExplainString()
        listView.forEachIndexed { it, wordWrapViewGroup ->
            wordWrapViewGroup.updateCanSelectStatue(it)
        }
    }

    /**
     * 获取提示信息
     */
    private fun getExplainString(): String {
        val stringBuffer = StringBuffer()
        val allSelect = allTypeHasSelected()
        if (allSelect) {
            stringBuffer.append("已选择 ")
            typeArray.forEach {
                stringBuffer.append(it!!.content)
                stringBuffer.append(" ")
            }
            updateGoodsInf()
        } else {
            stringBuffer.append("请选择 ")
            typeArray.forEach {
                if (TextUtils.isEmpty(it!!.content)) {
                    stringBuffer.append(it!!.title)
                    stringBuffer.append(" ")
                }
            }
        }
        return stringBuffer.toString()
    }

    @SuppressLint("SetTextI18n")
    private fun updateGoodsInf() {
        //var changeTypeString = changeTypeString()
        val findSameSelect = findSameSelect(changeTypeString())
        if (null != findSameSelect) {
            tv_all_count.text = "库存${findSameSelect!!.goodsNumber}件"
            tv_price.text = "¥${findSameSelect!!.goodsPrice}"
        } else {
            tv_all_count.text = oldCount
            tv_price.text = oldPrice
        }
    }

    /**
     * 判断规格是否全部选中
     */
    private fun allTypeHasSelected(): Boolean {
        typeArray.forEach {
            it?.let { bean ->
                if (TextUtils.isEmpty(bean.content)) {
                    return false
                }
            }
        }
        return true
    }

    /**
     * 规格转成集合
     */
    private fun changeTypeArray(index: Int): MutableList<String> {
        val list = ArrayList<String>()
        typeArray.forEachIndexed { it, goodsBean ->
            if (it != index && !TextUtils.isEmpty(goodsBean!!.content)) {
                list.add(goodsBean.content!!)
            }
        }
        return list
    }

    /**
     * 规格转字符串
     */
    private fun changeTypeString(): String {
        val buffer = StringBuffer()
        typeArray.forEach {
            if (!TextUtils.isEmpty(it!!.content)) {
                buffer.append(it.content)
                buffer.append(",")
            }
        }
        if (buffer.isNotEmpty()) {
            buffer.deleteCharAt(buffer.length - 1)
        }
        return buffer.toString()
    }

    /**
     * 查找选中的
     */
    private fun findSameSelect(selectArray: String): GoodsDetailBean.DataBean.GoodSpecificInfoAllBean? {
        listAll?.forEach {
            if (selectArray == it.specificPath) {
                return it
            }
        }
        return null
    }

    /**
     * 判断集合里是否包含数组全部子元素
     * @param parentString 集合、数组
     * @param list 集合
     */
    private fun checkArray(parentString: List<String>, list: MutableList<String>): Boolean {
        list.forEach {
            if (!parentString.contains(it)) {
                return false
            }
        }
        return true
    }

    private class GoodsBean {
        var title: String? = null
        var content: String? = null
    }

    var onGoodsDetailsSelectDialogListener: OnGoodsDetailsSelectDialogListener? = null

    interface OnGoodsDetailsSelectDialogListener {
        fun nowBuy()

        fun addShopCar(goodsId: String, specificId: String?, goodsNum: Int)

        fun showNotice(msg: String)
    }
}