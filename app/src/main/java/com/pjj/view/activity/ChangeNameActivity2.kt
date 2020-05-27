package com.pjj.view.activity

import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
import android.text.Spanned
import com.pjj.R
import com.pjj.PjjApplication
import com.pjj.intent.RetrofitService
import com.pjj.module.ResultBean
import com.pjj.present.BasePresent
import com.pjj.utils.TextUtils
import kotlinx.android.synthetic.main.activity_change_name.*

class ChangeNameActivity2 : BaseActivity<BasePresent<*>>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_name)
        setTitle("修改昵称")
        tv_save.setOnClickListener(onClick)
        TextUtils.banFaceForEditText(et_write, 12)
    }
    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_save -> {
                var toString = et_write.text.toString()
                if (TextUtils.isEmpty(toString)) {
                    showNotice("请输入昵称")
                    return
                }
                changeNameForNetWork(toString)
            }
        }
    }

    private fun changeNameForNetWork(name: String) {
        showWaiteStatue()
        RetrofitService.getInstance().loadEditUserNameTask(PjjApplication.application.userId, name, object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean) {
                cancelWaiteStatue()
                setResult(RESULT_OK, Intent().putExtra("user_name", name))
                finish()
            }

            override fun fail(error: String?) {
                super.fail(error)
                cancelWaiteStatue()
                showNotice(error)
            }
        })
    }
}
