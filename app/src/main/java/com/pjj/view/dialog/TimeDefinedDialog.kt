package com.pjj.view.dialog

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import com.pjj.R
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.layout_time_definnd_item.*
import kotlin.math.max
import kotlin.math.min

class TimeDefinedDialog(context: Context, themeResId: Int = 0) : FullWithNoTitleDialog(context, themeResId) {
    private val colorRed = ViewUtils.getColor(R.color.color_ff4c4c)
    private val colorGray = ViewUtils.getColor(R.color.color_888888)

    init {
        setContentView(R.layout.layout_time_definnd_item)
        tv_cancel.setOnClickListener(onClick)
        tv_sure.setOnClickListener(onClick)
        /*et.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val num = try {
                    Integer.parseInt(s.toString())
                } catch (e: Exception) {
                    0
                }
                if (num > 10 || num < 1) {
                    updateHint("只能输入1-10分钟", colorRed)
                } else {
                    updateHint("请输入1-10分钟（单次展示秒数）", colorGray)
                }
            }

        })*/
    }

    @SuppressLint("SetTextI18n")
    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_cancel -> dismiss()
            R.id.tv_sure -> {
                var num = try {
                    Integer.parseInt(et.text.toString())
                } catch (e: Exception) {
                    0
                }
                var i = num % 5
                if (num in 1..600 && i == 0) {
                    onTimeDefinedListener?.sure(num)
                    updateHint("请输入5的倍数，最高600秒", colorGray)
                    dismiss()
                } else {
//                    onTimeDefinedListener?.notice("只能输入1-10分钟")
                    updateHint("只能输入5的倍数，最高600秒", colorRed)
                    if (num < 5) {
                        i = 0
                        num = 5
                    }
                    val min = min(600, num)
                    et.setText((if (min == 600) 600 else (min - i)).toString())
                    et.setSelection(et.text.length)
                }
            }
        }
    }

    private fun updateHint(hint: String, color: Int) {
        this@TimeDefinedDialog.tv_hint.text = hint
        this@TimeDefinedDialog.tv_hint.setTextColor(color)
    }

    override fun getWindowBgDrawable(): Drawable {
        return GradientDrawable().apply {
            cornerRadius = ViewUtils.getFDp(R.dimen.dp_5)
            setColor(Color.WHITE)
        }
    }

    override fun getDialogGravity(): Int {
        return Gravity.CENTER
    }

    override fun isFullScreen(): Boolean {
        return false
    }

    var onTimeDefinedListener: OnTimeDefinedListener? = null

    interface OnTimeDefinedListener {
        fun notice(msg: String)
        fun sure(num: Int)
    }
}