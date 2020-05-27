package com.pjj.view.help

import android.support.constraint.ConstraintLayout
import android.text.InputFilter
import android.text.Spanned
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.pjj.R
import com.pjj.utils.Log
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import java.util.regex.Pattern

/**
 * Created by XinHeng on 2019/01/09.
 * describe：认证
 */
open abstract class CertificationParentHelp {
    protected open var centerTag = false
    protected open val colorHint = ViewUtils.getColor(R.color.color_999999)
    protected open val colorText = ViewUtils.getColor(R.color.color_666666)
    protected open val centerMarLeft = ViewUtils.getDp(R.dimen.dp_101)
    protected open val noCenterMarLeft = ViewUtils.getDp(R.dimen.dp_14)
    protected open var nowImageView: ImageView? = null
    protected lateinit var view: View
    var visibility: Int = View.VISIBLE
        set(value) {
            field = value
            view.visibility = value
        }

    private fun getInputFilter(): InputFilter {
        return object : InputFilter {
            var pattern = Pattern.compile("[^\\u4E00-\\u9FA5]")
            override fun filter(charSequence: CharSequence, i: Int, i1: Int, spanned: Spanned, i2: Int, i3: Int): CharSequence? {
                val matcher = pattern.matcher(charSequence)
                if (!matcher.find()) {
                    return null
                }
                return ""
            }
        }
    }

    fun getFilters(index: Int = 0): Array<InputFilter> {
        return if (index > 0) {
            arrayOf(getInputFilter(), InputFilter.LengthFilter(index))
        } else {
            arrayOf(getInputFilter())
        }
    }

    protected open fun reSetMarginStartLayout(view: View, marginStart: Int = centerMarLeft) {
        var layoutParams = view.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.marginStart = marginStart
        view.layoutParams = layoutParams
    }

    protected open fun setText(tv: TextView, text: String?, hint: String) {
        var text = text
        var color = colorText
        if (TextUtils.isEmpty(text)) {
            text = hint
            color = colorHint
        }
        tv.setTextColor(color)
        tv.text = text
    }

    protected open var onClickListener = View.OnClickListener {
        onClick(it.id)
    }

    protected open fun onClick(viewId: Int) {

    }

    protected open fun startSelectImage(iv: ImageView) {
        nowImageView = iv
        Log.e("TAG", "startSelectImage: id=${nowImageView!!}")
        onCertificationHelpListenr?.startSelectImage()
    }

    protected open fun selectImageResult(path: String) {
        nowImageView?.let {
            it.isFocusable = true
            it.isFocusableInTouchMode = true
            it.requestFocus()
        }
    }

    fun setExplain(text: String?) {
        var textView = view.findViewById<TextView>(R.id.tv_explain)
        var line = view.findViewById<View>(R.id.line_explain)
        if (TextUtils.isEmpty(text)) {
            textView.visibility = View.GONE
            line.visibility = View.GONE
        } else {
            textView.visibility = View.VISIBLE
            line.visibility = View.VISIBLE
            textView.text = text
        }
    }

    var onCertificationHelpListenr: OnCertificationHelpListenr? = null

    interface OnCertificationHelpListenr {
        fun startSelectImage()
        /**
         * 隐藏输入法
         */
        fun hiddenInputMethod()
    }
}