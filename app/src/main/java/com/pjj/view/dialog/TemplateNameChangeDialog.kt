package com.pjj.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.utils.TextUtils
import kotlinx.android.synthetic.main.layout_change_template_name_dialog.*

/**
 * Created by XinHeng on 2018/12/20.
 * describe：
 */
class TemplateNameChangeDialog(context: Context, themeResId: Int = 0) : FullWithNoTitleDialog(context, themeResId) {
    override fun getDialogGravity(): Int {
        return Gravity.CENTER
    }

    override fun isFullScreen(): Boolean {
        return false
    }

    override fun getWindowBgDrawable(): Drawable {
        return ColorDrawable(Color.TRANSPARENT)
    }

    init {
        setContentView(R.layout.layout_change_template_name_dialog)
        //TextUtils.banFaceForEditText(et_name, 10)
        tv_cancel.setOnClickListener(onClick)
        tv_create.setOnClickListener(onClick)
    }

    var hint: String = "请输入模板名称"
        set(value) {
            field = value
            et_name.hint = value
        }
    var maxLength: Int = -1
        set(value) {
            field = value
            TextUtils.banFaceForEditText(et_name, value)
        }
    var gravity: Int = Gravity.CENTER
        set(value) {
            field = value
            et_name.gravity = gravity
        }
    var name: String = ""
        set(value) {
            et_name.setText(value)
            et_name.setSelection(et_name.text.length)
            if (!isShowing) {
                show()
                et_name.postDelayed({
                    et_name.isFocusableInTouchMode = true
                    et_name.requestFocus()
                    val inputMethodManager = et_name.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputMethodManager.showSoftInput(et_name, 0)
                }, 200)
            }
        }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.tv_cancel -> dismiss()
            R.id.tv_create -> {
                val name = et_name.text.toString()
                if (TextUtils.isEmpty(name)) {
                    onItemClickListener?.notice(hint)
                    return
                }
                if (name.length > maxLength && maxLength > 0) {
                    onItemClickListener?.notice("不能超过${maxLength}个文字")
                    return
                }
                onItemClickListener?.leftClick(name)
                dismiss()
            }
        }
    }

    var onItemClickListener: OnItemClickListener? = null
    fun show(msg: String?) {
        val text = TextUtils.clean(msg)
        name = text
        //super.show()
    }

    interface OnItemClickListener {
        /**
         * 左侧按钮点击
         */
        fun leftClick(name: String)

        fun notice(msg: String)
    }
}