package com.pjj.view.adapter

import android.content.Context
import android.util.TypedValue
import android.view.View
import com.pjj.R
import com.pjj.utils.ViewUtils
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView

/**
 * Created by XinHeng on 2018/12/07.
 * describe：订单标题adapter
 */
class OrderCommonNavigatorAdapter(private var list: MutableList<String>? = null) : CommonNavigatorAdapter() {

    override fun getCount(): Int {
        return list?.size ?: 0
    }

    override fun getTitleView(context: Context, index: Int): IPagerTitleView {
        return ColorTransitionPagerTitleView(context).apply {
            tag = index
            if (null != list) {
                text = list!![index]
            }
            normalColor = ViewUtils.getColor(R.color.color_333333)
            selectedColor = ViewUtils.getColor(R.color.color_theme)
            setOnClickListener(onClick)
            setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_14))
        }
    }

    override fun getIndicator(context: Context): IPagerIndicator {
        return LinePagerIndicator(context).apply {
            setColors(ViewUtils.getColor(R.color.color_theme))
        }
    }

    private var onClick = View.OnClickListener {
        var i = it.tag as Int
        onCurrentItemListener?.currentItem(i)
    }
    var onCurrentItemListener: OnCurrentItemListener? = null

    interface OnCurrentItemListener {
        fun currentItem(index: Int)
    }
}