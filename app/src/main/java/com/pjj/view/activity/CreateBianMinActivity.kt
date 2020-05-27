package com.pjj.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.Gravity
import android.widget.Toast
import com.pjj.R
import com.pjj.PjjApplication
import com.pjj.intent.RetrofitService
import com.pjj.module.BianMinBean
import com.pjj.module.ResultBean
import com.pjj.module.parameters.Template
import com.pjj.module.parameters.TemplateBianMin
import com.pjj.present.BasePresent
import com.pjj.utils.TextUtils
import kotlinx.android.synthetic.main.activity_create_bian_min.*

class CreateBianMinActivity : BaseActivity<BasePresent<*>>() {
    private val templateBianMin: TemplateBianMin = TemplateBianMin()
    private var template: Template? = null
    private var change: Boolean = false
    private var dataBean: BianMinBean.DataBean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_bian_min)
        setTitle("创建模板")
        template = intent.getParcelableExtra("template")
        change = intent.getBooleanExtra("change", change)
        if (change) {
            dataBean = intent.getParcelableExtra("BianMinBean")
        }
        TextUtils.banFaceForEditText(et_name, 10)
        TextUtils.banFaceForEditText(et_write, 60)
        et_write.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //tv_count.text = "${s.length}/60"
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                tv_count.text = "${s.length}/60"
            }

        })
        tv_preview.setOnClickListener(onClick)
        tv_save.setOnClickListener(onClick)
        dataBean?.let {
            et_name.setText(it.title)
            et_write.setText(it.info)
            et_name.setSelection(et_name.text.toString().length)
        }
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_preview -> {
                var content = et_write.text.toString()
                if (content.isNotEmpty()) {
                    PreviewXspActivity.startActivity(this, content, "0", "屏加加科技有限公司", "4001251818")
                } else {
                    toast("请输入内容")
                    return
                }
            }
            R.id.tv_save -> {
                var title1 = et_name.text.toString()
                if (title1.isNotEmpty()) {
                    templateBianMin.title = title1
                } else {
                    toast("请输入模板名称")
                    return
                }
                var content = et_write.text.toString()
                if (content.isNotEmpty()) {
                    templateBianMin.info = content
                } else {
                    toast("请输入内容")
                    return
                }
                templateBianMin.infoType = template?.infoType
                templateBianMin.userId = PjjApplication.application.userId
                templateBianMin.purposeType = template?.purposeType
                showWaiteStatue()
                if (change) {
                    templateBianMin.peopleInfoId = dataBean?.peopleInfoId
                    update()
                } else {
                    upload()
                }
            }
        }
    }

    private fun upload() {
        RetrofitService.getInstance().loadUploadBianMinInfTask(templateBianMin, object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean) {
                cancelWaiteStatue()
                smillTag = true
                showNotice("创建成功")
                et_write.postDelayed({
                    setResult(303)
                    finish()
                }, 2100)
            }

            override fun fail(error: String?) {
                super.fail(error)
                cancelWaiteStatue()
                showNotice(error)
            }
        })
    }

    private fun update() {
        RetrofitService.getInstance().loadUpdateBianMinInfTask(templateBianMin, object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean) {
                cancelWaiteStatue()
                smillTag = true
                showNotice("创建成功")
                et_write.postDelayed({
                    setResult(303)
                    finish()
                }, 2100)
            }

            override fun fail(error: String?) {
                super.fail(error)
                cancelWaiteStatue()
                showNotice(error)
            }
        })
    }
}
