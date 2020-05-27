package com.pjj.view.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.Toast
import com.dmcbig.mediapicker.PickerActivity
import com.dmcbig.mediapicker.PickerConfig
import com.dmcbig.mediapicker.entity.Media

import com.pjj.R
import com.pjj.PjjApplication
import com.pjj.contract.CreateTemplateContract
import com.pjj.intent.RetrofitService
import com.pjj.module.ResultBean
import com.pjj.module.parameters.Template
import com.pjj.module.parameters.UploadTemplate
import com.pjj.present.CreateTemplatePresent
import kotlinx.android.synthetic.main.activity_createtemplate.*
import java.io.File
import java.util.ArrayList

/**
 * Create by xinheng on 2018/12/06 13:40。
 * describe：创建模板
 */
class CreateTemplateActivity : BaseActivity<CreateTemplatePresent>(), CreateTemplateContract.View {
    private var template: Template? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_createtemplate)
        setTitle("创建模板")
        template = intent.getParcelableExtra("template")
        iv_image_all.setOnClickListener(onClick)
        iv_image_half.setOnClickListener(onClick)
        iv_image_four.setOnClickListener(onClick)
    }

    internal var select: ArrayList<Media>? = null
    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.iv_image_all -> {
                startActivity(Intent(this, CreateDIYActivity::class.java).putExtra("template", template))
            }
            R.id.iv_image_half -> toast("待开发中")
            R.id.iv_image_four -> toast("待开发中")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 200 && resultCode == PickerConfig.RESULT_CODE) {
            select = data?.getParcelableArrayListExtra(PickerConfig.EXTRA_RESULT)
            select?.run {
                if (size > 0) {
                    //println(this[0].path)
                    var media = this[0]
                    Log.e("select", "select.size=" + media.path)
                    uploadTemplate(media.path, media.mediaType != 3)
                }
            }
        }
    }

    private fun uploadTemplate(path: String, isImg: Boolean) {
        template?.run {
            showWaiteStatue()
            var uploadTemplateFile = UploadTemplate().apply {
                filePath = path
                when (isImg) {
                    true -> {
                        templetType = "1"
                        fileName = "1&1."
                    }
                    else -> {
                        templetType = "2"
                        fileName = "2&1."
                    }
                }
                identityType = this@run.identityType
                purposeType = this@run.purposeType
                isImage = isImg
                fileRelName = ""
                userId = PjjApplication.application.userId
            }
            var file = File(uploadTemplateFile.filePath)
            if (!file.exists()) {
                return
            } else {
                var name = uploadTemplateFile.filePath
                uploadTemplateFile.fileRelName = file.name
                val prefix = name.substring(name.lastIndexOf(".") + 1)
                uploadTemplateFile.fileName = uploadTemplateFile.fileName + prefix
                RetrofitService.getInstance().uploadTemplateFile(uploadTemplateFile, object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
                    override fun successResult(t: ResultBean?) {
                        cancelWaiteStatue()
                        smillTag = true
                        showNotice("上传成功")
                        this@CreateTemplateActivity.iv_image_all.postDelayed({
                            startActivity(Intent(this@CreateTemplateActivity, TemplateListActivity::class.java).putExtra("refresh", true))
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

    }
}
