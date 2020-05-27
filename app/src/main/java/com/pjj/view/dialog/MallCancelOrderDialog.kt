package com.pjj.view.dialog

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.pjj.R
import com.pjj.module.CancelWhyBean
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.layout_mall_cancel_order_dialog_item.*

class MallCancelOrderDialog(context: Context) : FullWithNoTitleDialog(context, 0) {
    init {
        setContentView(R.layout.layout_mall_cancel_order_dialog_item)
        tv_not_cancel.setOnClickListener(onClick)
        tv_sure_cancel.setOnClickListener(onClick)
        iv_close.setOnClickListener(onClick)
    }

    var data: List<CancelWhyBean.DataBean>? = null
        set(value) {
            field = value
            if (ll_parent.childCount > 0) {
                ll_parent.removeAllViews()
            }
            value?.forEachIndexed { index, dataBean ->
                ll_parent.addView(createChildView(index, dataBean))
                ll_parent.addView(createLineView())
            }
        }
    private var hasSelectView: ImageView? = null

    override fun onClick(view: View) {
        val tag = view.tag
        if (tag is Int && view is ImageView) {
            if (hasSelectView == view) {
                view.setImageResource(R.mipmap.unselect)
                hasSelectView = null
            } else {
                view.setImageResource(R.mipmap.select)
                hasSelectView?.setImageResource(R.mipmap.unselect)
                hasSelectView = view
            }
            return
        }
        when (view.id) {
            R.id.iv_close, R.id.tv_not_cancel -> if (isShowing) dismiss()
            R.id.tv_sure_cancel -> {
                if (null == hasSelectView) {
                    onMallCancelOrderListener?.notice("请选择取消原因")
                    return
                }
                onMallCancelOrderListener?.cancel(hasSelectView!!.let { data!![it.tag as Int].content })
            }
        }
    }

    private val dp43 = ViewUtils.getDp(R.dimen.dp_43)
    private val dp14 = ViewUtils.getDp(R.dimen.dp_14)
    private val dp12 = ViewUtils.getDp(R.dimen.dp_12)
    private fun createChildView(position: Int, data: CancelWhyBean.DataBean): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            addView(TextView(context).apply {
                layoutParams = LinearLayout.LayoutParams(0, dp43, 1f)
                text = data.content
                gravity = Gravity.CENTER_VERTICAL
                setTextColor(ViewUtils.getColor(R.color.color_333333))
                setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_12))
                setPadding(dp14, 0, 0, 0)
            })
            //19 28 47 19 24 43
            addView(ImageView(context).apply {
                layoutParams = LinearLayout.LayoutParams(dp43, dp43)
                setPadding(dp12, dp12, dp12, dp12)
                scaleType = ImageView.ScaleType.FIT_XY
                setImageResource(R.mipmap.unselect)
                tag = position
                setOnClickListener(onClick)
            })
        }

    }

    private fun createLineView(): View {
        return ViewUtils.createLineHView(context)
    }

    var onMallCancelOrderListener: OnMallCancelOrderListener? = null

    interface OnMallCancelOrderListener {
        fun cancel(msg: String)
        fun notice(msg: String)
    }
}