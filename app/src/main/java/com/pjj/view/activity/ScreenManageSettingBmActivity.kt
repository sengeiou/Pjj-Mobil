package com.pjj.view.activity

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher

import com.pjj.R
import com.pjj.contract.ScreenManageSettingBmContract
import com.pjj.present.ScreenManageSettingBmPresent
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_screenmanagesettingbm.*

/**
 * Create by xinheng on 2019/05/22 15:12。
 * describe：屏幕设置-物业便民
 */
class ScreenManageSettingBmActivity : BaseActivity<ScreenManageSettingBmPresent>(), ScreenManageSettingBmContract.View {
    private var screenIds: String? = null
    private var isShow = "0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screenmanagesettingbm)
        setTitle("屏幕管理")
        screenIds = intent.getStringExtra("screenIds")
        tv_hidden.setOnClickListener(onClick)
        tv_show.setOnClickListener(onClick)
        tv_set.setOnClickListener(onClick)
        et_note.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                tv_text_count.text = "${s.length}/60"
            }
        })
        tv_hidden.setCompoundDrawables(getLeftDrawable(R.mipmap.select_point), null, null, null)
        tv_show.setCompoundDrawables(getLeftDrawable(R.mipmap.unselect), null, null, null)
        mPresent = ScreenManageSettingBmPresent(this)
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_show -> toggleShow()
            R.id.tv_hidden -> toggleShow()
            R.id.tv_set -> {
                val title = et_title.text.toString()
                if (title.isEmpty() && "1" == isShow) {
                    showNotice("请输入标题")
                    return
                }

                val s = et_note.text.toString()
                if (s.isEmpty() && "1" == isShow) {
                    showNotice("您还没有填写信息")
                    return
                }
                if (s.trim().isEmpty() && "1" == isShow) {
                    showNotice("请输入有效信息")
                    return
                }
                screenIds?.let {
                    mPresent?.setBm(title, s, it, isShow)
                }
            }
        }
    }

    private fun toggleShow() {
        if ("0" == isShow) {
            isShow = "1"
            tv_hidden.setCompoundDrawables(getLeftDrawable(R.mipmap.unselect), null, null, null)
            tv_show.setCompoundDrawables(getLeftDrawable(R.mipmap.select_point), null, null, null)
        } else {
            isShow = "0"
            tv_hidden.setCompoundDrawables(getLeftDrawable(R.mipmap.select_point), null, null, null)
            tv_show.setCompoundDrawables(getLeftDrawable(R.mipmap.unselect), null, null, null)
        }
    }

    private val dp19 = ViewUtils.getDp(R.dimen.dp_19)
    private fun getLeftDrawable(res: Int): Drawable {
        return ViewUtils.getDrawable(res).apply {
            setBounds(0, 0, dp19, dp19)
        }
    }

    override fun setSuccess() {
        smillTag = true
        showNotice("设置成功")
        smillTag = false
        Handler().postDelayed({
            setResult(302)
            finish()
        }, 2100)
    }
}
