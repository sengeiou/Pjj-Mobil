package com.pjj.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.View.*
import android.widget.Toast

import com.pjj.R
import com.pjj.contract.RegisterContract
import com.pjj.present.RegisterPresent
import com.pjj.utils.TextUtils

import kotlinx.android.synthetic.main.activity_register.*

/**
 * Create by xinheng on 2018/11/06 14:45。
 * describe：注册
 */
class RegisterActivity : BaseActivity<RegisterPresent>(), RegisterContract.View {
    private var opinionRegister: Boolean = false
    private var loginTag: Boolean = false
    private val link = "http://protal.test.pingjiajia.cn/guide/#/useAgreement"
    private val yinsi = "http://protal.test.pingjiajia.cn/guide/main.html"//隐私政策协议
    private var linkTag = true
    private var pasdTag = false//密文
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        opinionRegister = intent.getBooleanExtra("opinion_register", false)
        loginTag = intent.getBooleanExtra("loginTag", false)
        mPresent = RegisterPresent(this)
        var title = when {
            loginTag -> "登录"
            opinionRegister -> {
                tv_submit.text = "注册"
                "注册"
            }
            else -> "重置密码"
        }
        setTitle(title)
        tv_submit.setOnClickListener(onClick)
        verification_code_tv.setOnClickListener(onClick)
        detele_tv.setOnClickListener(onClick)
        tv_xieyi.setOnClickListener(onClick)
        tv_xieyi1.setOnClickListener(onClick)
        iv_xieyi.setOnClickListener(onClick)
        iv_pwd.setOnClickListener(onClick)
        tv_yinsi.setOnClickListener(onClick)
        if (loginTag) {
            var phone = intent.getStringExtra("phone")
            phone?.let {
                etPhone.setText(it)
            }
            ll_psd.visibility = View.GONE
            tv_line.visibility = View.GONE
            agreeLin.visibility = View.GONE
            tv_submit.text = "登录"
        } else {
            if (!opinionRegister) {
                agreeLin.visibility = View.INVISIBLE
            } else {
                agreeLin.visibility = View.VISIBLE
            }
        }
        etPhone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            @Suppress("UNREACHABLE_CODE")
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val string = s.toString()
                if (TextUtils.isEmpty(string)) {
                    detele_tv.visibility = INVISIBLE
                } else {
                    detele_tv.visibility = VISIBLE
                }
            }

        })
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_submit -> {
                if (loginTag) {
                    mPresent?.loginCode()
                    return
                }
                if (opinionRegister) {
                    if (!linkTag) {
                        showNotice("请勾选《屏加加用户协议》")
                        return
                    }
                }
                mPresent?.loadRegisterTask(when (opinionRegister) {
                    true -> ""
                    else -> "true"
                })
            }
            R.id.detele_tv -> etPhone.setText("")
            R.id.tv_xieyi1 -> linkActivity(link)
            R.id.tv_yinsi -> linkActivity(yinsi)
            R.id.iv_xieyi -> {
                if (linkTag) {
                    iv_xieyi.setImageResource(R.mipmap.select_icon)
                } else {
                    iv_xieyi.setImageResource(R.mipmap.selected_icon)
                }
                linkTag = !linkTag
            }
            R.id.verification_code_tv -> {
                etCode.setText("")
                mPresent?.loadGetCodeTask(when (opinionRegister) {
                    true -> "register"
                    else -> "other"
                })
            }
            R.id.iv_pwd -> {
                if (pasdTag) {
                    iv_pwd.setImageResource(R.mipmap.zhegnyan)
                    //显示明文--设置为可见的密码
                    etPwd.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                } else {
                    iv_pwd.setImageResource(R.mipmap.biyan)
                    etPwd.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                }
                pasdTag = !pasdTag
                etPwd.setSelection(etPwd.text.length)
            }
        }
    }

    private fun linkActivity(url: String) {
        WebActivity.newInstance(this, "屏加加用户协议", url)
    }

    override fun registerSuccess(result: String) {
        var s = when (opinionRegister) {
            true -> "注册成功"
            else -> "修改成功，请重新登录"
        }
        var makeText = Toast.makeText(application, s, Toast.LENGTH_SHORT)
        makeText.setGravity(Gravity.CENTER, 0, 0)
        makeText.show()
        if (opinionRegister) {
            setResult(300, Intent().putExtra("phone", phone).putExtra("password", password))
        }
        finish()
    }

    override fun updateCodeText(text: String, effiect: Boolean) {
        verification_code_tv.text = text
        verification_code_tv.isEnabled = effiect
        if (effiect) {
            verification_code_tv.setTextColor(ContextCompat.getColor(this, R.color.color_3e3e3e))
        } else {
            verification_code_tv.setTextColor(ContextCompat.getColor(this, R.color.color_7b7b7b))
        }

    }

    override fun getPhone(): String {
        return etPhone.text.toString()
    }

    override fun getCode(): String {
        return etCode.text.toString()
    }

    override fun getPassword(): String {
        return etPwd.text.toString()
    }

    override fun loginSuccess() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}
