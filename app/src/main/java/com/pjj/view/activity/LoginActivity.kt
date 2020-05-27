package com.pjj.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View.*
import android.widget.Toast
import com.pjj.BuildConfig
import com.pjj.R
import com.pjj.PjjApplication
import com.pjj.contract.LoginContract
import com.pjj.intent.RetrofitService
import com.pjj.module.parameters.User
import com.pjj.present.LoginPresent
import com.pjj.utils.*
import com.umeng.socialize.UMAuthListener
import com.umeng.socialize.bean.SHARE_MEDIA
import kotlinx.android.synthetic.main.activity_login.*
import com.umeng.socialize.UMShareAPI

/**
 * Create by xinheng on 2018/11/07。
 * describe：登录
 */
class LoginActivity : BaseActivity<LoginPresent>(), LoginContract.View {
    private var time_out = false
    private var pasdTag = false//密文
    var authListener: UMAuthListener = object : UMAuthListener {
        /**
         * @desc 授权开始的回调
         * @param platform 平台名称
         */
        override fun onStart(platform: SHARE_MEDIA) {
            Log.e("TAG", "onStart: SHARE_MEDIA=${platform.getName()}")
        }

        /**
         * @desc 授权成功的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param data 用户资料返回
         */
        override fun onComplete(platform: SHARE_MEDIA, action: Int, data: Map<String, String>) {
            var jsonString = JsonUtils.toJsonString(data)
            Log.e("TAG", "onComplete: data=$jsonString")
            Toast.makeText(PjjApplication.application, "成功了", Toast.LENGTH_LONG).show()
        }

        /**
         * @desc 授权失败的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         * @param t 错误原因
         */
        override fun onError(platform: SHARE_MEDIA, action: Int, t: Throwable) {

            Toast.makeText(PjjApplication.application, "失败：" + t.message, Toast.LENGTH_LONG).show()
        }

        /**
         * @desc 授权取消的回调
         * @param platform 平台名称
         * @param action 行为序号，开发者用不上
         */
        override fun onCancel(platform: SHARE_MEDIA, action: Int) {

            Toast.makeText(PjjApplication.application, "取消了", Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mPresent = LoginPresent(this)
        //setTitle("登录", Color.WHITE, -1)
        setTitle("登录")
        time_out = intent.getBooleanExtra("time_out", time_out)
        Log.e("TAG", "onCreate: time_out=$time_out")
        detele_tv.setOnClickListener(onClick)
        forget_pwd_tv.setOnClickListener(onClick)
        register_tv.setOnClickListener(onClick)
        login_tv.setOnClickListener(onClick)
        tv_duanxin.setOnClickListener(onClick)
        PjjApplication.application.userId = null
        etPhone.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val string = s.toString()
                if (TextUtils.isEmpty(string)) {
                    detele_tv.visibility = INVISIBLE
                } else {
                    detele_tv.visibility = VISIBLE
                }
            }

        })
        var userId = SharedUtils.getXmlForKey(SharedUtils.USER_ID)
        var token = SharedUtils.getXmlForKey(SharedUtils.TOKEN)
        if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(token)) {
            PjjApplication.application.userId = userId
            RetrofitService.getInstance().setToken(token)
            loginSuccess()
        } else {
            SharedUtils.cleanLoginXml()
        }
        if (BuildConfig.DEBUG) {
            etPhone.setText("18201538182")
            etPwd.setText("123456")
        }
        UmengLoginUtils.initLogin()

        iv_qq.setOnClickListener(onClick)
        iv_sina.setOnClickListener(onClick)
        iv_weixin.setOnClickListener(onClick)
        iv_pwd.setOnClickListener(onClick)

    }

    override fun allowBackFinish(): Boolean {
        return false
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.detele_tv -> etPhone.setText("")
            R.id.login_tv -> mPresent?.loadLoginTask()
            R.id.forget_pwd_tv -> toForgetPasswordView()
            R.id.register_tv -> toRegisterView()
            R.id.tv_duanxin -> startActivity(Intent(this, RegisterActivity::class.java).putExtra("loginTag", true).putExtra("phone", etPhone.text.toString()))
            R.id.iv_qq -> UmengLoginUtils.login(this, SHARE_MEDIA.QQ, authListener)
            R.id.iv_sina -> UmengLoginUtils.login(this, SHARE_MEDIA.SINA, authListener)//新浪微博
            R.id.iv_weixin -> UmengLoginUtils.login(this, SHARE_MEDIA.WEIXIN, authListener)
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

    override fun getPhone(): String {
        return etPhone.text.toString()
    }

    override fun getPassword(): String {
        return etPwd.text.toString()
    }

    override fun login() {
        mPresent?.loadLoginTask()
    }

    private var registerClickTag = false
    override fun toRegisterView() {
        registerClickTag = true
        startActivityForResult(Intent(this, RegisterActivity::class.java).putExtra("opinion_register", true), 300)
    }

    override fun toForgetPasswordView() {
        startActivity(Intent(this, RegisterActivity::class.java))
        //overridePendingTransition(R.anim.start_translate_animation_exit, R.anim.finish_translate_animation_exit)
    }

    override fun loginSuccess() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun titleLeftClick() {
        if (time_out) {
            loginSuccess()
        } else {
            super.titleLeftClick()
        }
    }

    override fun onBackPressed() {
        if (time_out) {
            loginSuccess()
        } else {
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 300 && registerClickTag) {
            registerClickTag = false
            data?.let {
                mPresent?.login(User().apply {
                    phone = it.getStringExtra("phone")
                    password = it.getStringExtra("password")
                })
            }
        }
        //UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }
}