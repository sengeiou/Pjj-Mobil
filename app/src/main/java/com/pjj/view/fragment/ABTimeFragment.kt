package com.pjj.view.fragment

import android.content.Context
import com.pjj.module.TimeDiscountBean
import com.pjj.present.BasePresent
import com.pjj.utils.CalculateUtils
import java.lang.RuntimeException

/**
 * Created by XinHeng on 2018/11/23.
 * describe：选择时间
 */
abstract class ABTimeFragment<P : BasePresent<*>> : BaseFragment<P>(), OnXspCountChangeListener {
    protected open lateinit var onFragmentInteractionListener: OnFragmentInteractionListener
    protected open var allXspPrice = 0f
    protected open var allHours = 0
    protected open var listHour: List<Int>? = null
    protected open var needUpdatePrice = false
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            onFragmentInteractionListener = context
        } else {
            throw RuntimeException("context must be implementation ABTimeFragment.OnFragmentInteractionListener")
        }
        //onFragmentInteractionListener.updateAllXspPrices("${CalculateUtils.m1(allXspPrice)}")
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            if (needUpdatePrice) {
                needUpdatePrice = false
                updateXspPriceAndTime()
            } else {
                onFragmentInteractionListener.updateAllXspPrices("${CalculateUtils.m1(allXspPrice)}", allHours)
            }
        }
        super.onHiddenChanged(hidden)
    }

    abstract fun updateXspPriceAndTime()
    override fun countChange() {
        if (userVisibleHint) {
            needUpdatePrice = false
            updateXspPriceAndTime()
        } else {
            needUpdatePrice = true
        }
    }

    interface OnFragmentInteractionListener {
        /**
         * 获取一组广告屏id
         * @return 逗号隔开
         */
        fun getXspIds(): String

        /**
         * 广告类型
         * @return 便民/DIY
         */
        fun getAdType(): Int

        /**
         * 更新总额
         * @param money 总额
         * @param hours 总时长
         */
        fun updateAllXspPrices(money: String, hours: Int)

        fun getTimeDiscountBean(): TimeDiscountBean?
    }
}