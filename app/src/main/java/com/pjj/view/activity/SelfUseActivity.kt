package com.pjj.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.contract.SelfUseContract
import com.pjj.module.ScreenModelBean
import com.pjj.module.parameters.Template
import com.pjj.present.SelfUsePresent
import com.pjj.utils.ViewUtils
import com.pjj.view.adapter.TemplateSelfUseListAdapter
import com.pjj.view.dialog.AddTemplateDialog
import com.pjj.view.dialog.TemplateNameChangeDialog
import kotlinx.android.synthetic.main.activity_self_use.*

class SelfUseActivity : BaseActivity<SelfUsePresent>(), SelfUseContract.View {
    private lateinit var rvAdapter: TemplateSelfUseListAdapter
    private var partnerFileId: String? = null
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        var booleanExtra = intent.getBooleanExtra("refresh", false)
        if (booleanExtra) {
            mPresent?.loadSelfUseTemplate()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_self_use)
        setTitle("自用模板")
        mPresent = SelfUsePresent(this)
        rv_self_use.layoutManager = LinearLayoutManager(this)
        rvAdapter = TemplateSelfUseListAdapter(this, false, "7", "1")
        rvAdapter.onItemClickListener = object : TemplateSelfUseListAdapter.OnItemClickListener {
            override fun itemClick(path_content: String?, id: String, fileType: String, statue: String, towTag: Boolean) {
                PreviewNewMediaActivity.newInstance(this@SelfUseActivity, PjjApplication.filePath + path_content!!, fileType, towTag)
            }

            override fun createTemplate(count: Int) {
                if (count >= 10) {
                    showNotice("模板最多为10个")
                    return
                }
                startActivity(Intent(this@SelfUseActivity, PersonMediaTemActivity::class.java).putExtra("template", Template().apply {
                    userId = PjjApplication.application.userId
                    purposeType = "7"
                    selfUseTag = "1"
                    comeFromTemplate = "1"
                }).putExtra("title", "创建模板"))
            }

            override fun changeName(templateId: String, name: String) {
                this@SelfUseActivity.templateId = templateId
                changeNameDialog.name = name
            }

            override fun loadOtherTemplate() {
            }

            override fun delete(id: String) {
                this@SelfUseActivity.deleteId = id
                deleteDialog.show()
            }

        }
        rv_self_use.adapter = rvAdapter
        rv_self_use.layoutManager = GridLayoutManager(this, 2)
        mPresent?.loadSelfUseTemplate()
    }

    /**
     * 修改名称，暂存
     */
    private var templateId: String? = null
    private var templateName: String? = null
    private var deleteId: String? = null

    override fun updateNameSuccess() {
        rvAdapter.updateForChangeName(templateName!!)
    }

    override fun deleteSuccess(tagDelete: Boolean) {
        val text = if (tagDelete) "删除成功" else "上传成功"
        smillTag = true
        showNotice(text)
        smillTag = false
        mPresent?.loadSelfUseTemplate()
    }

    override fun updateData(list: MutableList<ScreenModelBean.DataBean>?) {
        rvAdapter.list = list
    }

    private val changeNameDialog: TemplateNameChangeDialog by lazy {
        TemplateNameChangeDialog(this).apply {
            onItemClickListener = object : TemplateNameChangeDialog.OnItemClickListener {
                override fun leftClick(name: String) {
                    templateId?.let {
                        templateName = name
                        mPresent?.changeTemplateName(it, name)
                    }
                }

                override fun notice(msg: String) {
                    showNotice(msg)
                }
            }
        }
    }
    private val deleteDialog: AddTemplateDialog by lazy {
        AddTemplateDialog(this).apply {
            notice = "是否删除模板"
            leftText = "删除"
            var dp48 = ViewUtils.getDp(R.dimen.dp_48)
            setImageResource(R.mipmap.delete_, false, dp48, dp48)
            onItemClickListener = object : AddTemplateDialog.OnItemClickListener {
                override fun leftClick() {
                    deleteId?.let {
                        mPresent?.deleteTemplate(it)
                    }
                }
            }
        }
    }
}
