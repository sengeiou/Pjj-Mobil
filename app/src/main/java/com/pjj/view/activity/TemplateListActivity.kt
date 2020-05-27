package com.pjj.view.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.view.View

import com.pjj.R
import com.pjj.PjjApplication
import com.pjj.contract.TemplateListContract
import com.pjj.module.BianMinBean
import com.pjj.module.UserTempletBean
import com.pjj.module.parameters.Template
import com.pjj.module.xspad.XspManage
import com.pjj.present.TemplateListPresent
import com.pjj.utils.Log
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.adapter.TemplateBianMinAdapter
import com.pjj.view.adapter.TemplateListAdapter
import com.pjj.view.dialog.AddTemplateDialog
import com.pjj.view.dialog.TemplateNameChangeDialog
import kotlinx.android.synthetic.main.activity_template_list.*

/**
 * Create by xinheng on 2018/12/06 13:41。
 * describe：模板列表
 */
class TemplateListActivity : BaseActivity<TemplateListPresent>(), TemplateListContract.View {
    private var adapterGrid: TemplateListAdapter? = null
    private var adapterList: TemplateBianMinAdapter? = null
    private var template = Template()
    /**
     * 是否是发布过来的标志
     */
    private var releaseTag = false
    private lateinit var data: List<BianMinBean.DataBean>
    private var deleteId: String? = null
    private var adType = 0
    /**
     * 修改名称，暂存
     */
    private var templateId: String? = null
    private var templateName: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_template_list)
        template.userId = PjjApplication.application.userId
        initParma()
        //template.purposeType = adType.toString()
        mPresent = TemplateListPresent(this).apply {
            loadTemplateListTask(template)
        }

    }

    private fun initParma() {
        releaseTag = intent.getBooleanExtra("releaseTag", releaseTag)
        //templateId = intent.getStringExtra("templateId")
        if (intent.getBooleanExtra("refresh_1", false)) {
            releaseTag = false
            XspManage.getInstance().newMediaData.releaseTag = false
        }
        var identityType = intent.getStringExtra("identity_type")// 身份类型:1个人 2商家
        var adType = intent.getIntExtra("ad_type", 1)
        if (releaseTag) {
            setTitle("选择发布模板")
        } else {
            setTitle(intent.getStringExtra("title_text"))
        }

        when (adType) {//用途类型:1 DIY类型1 2便民信息1 3随机div1 4随机便民2 1312  6新模式 7传统模式 8拼屏模式
            9 -> {
                //template.purposeType = "9"
                template.purposeType = "7"
                initListView(identityType, adType)
            }
            7 -> {
                template.purposeType = "7"
                initListView(identityType, adType)
            }
            2 -> {
                template.infoType = identityType.toString()
                template.purposeType = "1"
                initListViewBM(identityType, adType)
            }
            1 -> {
                template.purposeType = "1"
                initListView(identityType, adType)
            }
            4 -> {
                template.infoType = identityType.toString()
                template.purposeType = "2"
                initListViewBM(identityType, adType)
            }
            5 -> {
                template.infoType = "3"
                template.purposeType = "5"
                initListViewBM(identityType, adType)
            }
            else -> {
                template.purposeType = "3"
                initListView(identityType, adType)
            }
        }
        if (!releaseTag) {

            tv_create.setOnClickListener {
                if ((adapterList!!.list?.size ?: 0) >= 5) {
                    showNotice("模板最多为5个")
                    return@setOnClickListener
                }
                startActivityForResult(Intent(this@TemplateListActivity, CreateBianMinActivity::class.java)
                        .putExtra("template", template)
                        .putExtra("change", false), 303)
            }
        }
    }

    /**
     * 便民列表
     */
    private fun initListViewBM(identityType: String, adType: Int) {
        template.identityType = identityType
        //template.purposeType = adType.toString()//用途类型:1 DIY类型 2便民信息 3填空传媒
        this.adType = adType
        tv_create_bm.setOnClickListener {
            if ((adapterList!!.list?.size ?: 0) >= 5) {
                showNotice("模板最多为5个")
                return@setOnClickListener
            }
            startActivityForResult(Intent(this@TemplateListActivity, CreateBianMinActivity::class.java)
                    .putExtra("template", template)
                    .putExtra("change", false), 303)
        }
        ll_no_data.visibility = View.VISIBLE
        rv_template.background = ColorDrawable(ViewUtils.getColor(R.color.color_f1f1f1))
        rv_template.run {
            layoutManager = LinearLayoutManager(this@TemplateListActivity)
            //addItemDecoration(SpaceItemDecoration(this@TemplateListActivity, LinearLayoutManager.VERTICAL, ViewUtils.getDp(R.dimen.dp_1), ViewUtils.getColor(R.color.color_f1f1f1)))
            adapter = TemplateBianMinAdapter().apply {
                this@TemplateListActivity.adapterList = this
                onItemClickListener = object : TemplateBianMinAdapter.OnItemClickListener {
                    override fun createTemplate() {
                        if ((adapterList!!.list?.size ?: 0) >= 5) {
                            showNotice("模板最多为5个")
                            return
                        }
                        startActivityForResult(Intent(this@TemplateListActivity, CreateBianMinActivity::class.java)
                                .putExtra("template", template)
                                .putExtra("change", false), 303)
                    }

                    override fun delete(bean: BianMinBean.DataBean) {
                        deleteId = bean.peopleInfoId
                        deleteDialog.show()
                    }

                    override fun preview(bean: BianMinBean.DataBean) {
                        PreviewXspActivity.startActivity(this@TemplateListActivity, bean.info, "0", "屏加加科技有限公司", "4001251818")
                    }

                    override fun editor(bean: BianMinBean.DataBean) {
                        startActivityForResult(Intent(this@TemplateListActivity, CreateBianMinActivity::class.java)
                                .putExtra("BianMinBean", bean)
                                .putExtra("template", template)
                                .putExtra("change", true), 303)
                    }
                }
            }
        }
    }

    /**
     * 在此打开，刷新
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        var booleanExtra = intent.getBooleanExtra("refresh", false)
        if (booleanExtra) {
            mPresent?.loadTemplateListTask(template)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == 303 && resultCode == 303 -> {
                mPresent?.loadTemplateListTask(template)
            }
        }
    }

    /**
     * diy 列表
     */
    private fun initListView(identityType: String, adType: Int) {
        template.identityType = identityType
        this.adType = adType
        //template.purposeType = adType.toString()//用途类型:1 DIY类型 2便民信息 3填空传媒
        line_create.visibility = View.GONE
        if (releaseTag) {
            tv_create_bm.visibility = View.VISIBLE
            tv_create_bm.text = "选择模板"
            tv_create_bm.setTextColor(Color.WHITE)
            tv_create_bm.isEnabled = false
            tv_create_bm.background = ColorDrawable(ViewUtils.getColor(R.color.color_d2d2d2))
            tv_create_bm.setOnClickListener {
                startActivity(Intent(this@TemplateListActivity, SelectReleaseAreaAllActivity::class.java))
                /*if (adType == 7)
                    startActivity(Intent(this@TemplateListActivity, SelectReleaseAreaMediaActivity::class.java))
                else {//9
                    startActivity(Intent(this@TemplateListActivity, SelectReleaseAreaElevatorMediaActivity::class.java))
                }*/
            }
        } else {
            tv_create_bm.visibility = View.GONE
        }
        if (!releaseTag && (template.purposeType == "9" || template.purposeType == "7")) {
            tv_explain.visibility = View.VISIBLE
        } else {
            tv_explain.visibility = View.GONE
        }
        rv_template.run {
            var dp24 = ViewUtils.getDp(R.dimen.dp_24)
            var dp13 = ViewUtils.getDp(R.dimen.dp_13)
            setPadding(dp24, dp13, 0, 0)
            layoutManager = GridLayoutManager(this@TemplateListActivity, 2)
            adapter = TemplateListAdapter(this@TemplateListActivity, releaseTag, template.purposeType, identityType).apply {
                templateId?.let {
                    //setClick(it)
                }
                this@TemplateListActivity.adapterGrid = this
                onItemClickListener = object : TemplateListAdapter.OnItemClickListener {
                    override fun loadOtherTemplate() {
                        mPresent?.loadOtherTemplateListTask(template)
                    }

                    override fun createTemplate(count: Int) {
                        //startActivity(Intent(this@TemplateListActivity, CreateTemplateActivity::class.java).putExtra("template", template))
                        if (template.purposeType == "7" || template.purposeType == "9" || template.purposeType == "1") {
                            if (count >= 5) {
                                showNotice("模板最多为5个")
                                return
                            }
                            startActivity(Intent(this@TemplateListActivity, PersonMediaTemActivity::class.java).putExtra("template", template)
                                    .putExtra("title", titleView.textMiddle.substring(0, titleView.textMiddle.length - 2)))
                        } else {
                            startActivityForResult(Intent(this@TemplateListActivity, CreateDIYActivity::class.java).putExtra("template", template)
                                    .putExtra("image_text", "图片 / 视频"), 303)
                        }
                    }

                    override fun itemClick(path_content: String?, id: String, fileType: String, statue: String, towTag: Boolean) {
                        Log.e("TAG", "path=$path_content")
                        //PreviewXspActivity3.startActivity(this@TemplateListActivity, path_content, fileType);
                        if (releaseTag) {
                            if (null == path_content) {
                                if (tv_create_bm.isEnabled) {
                                    tv_create_bm.isEnabled = false
                                    tv_create_bm.background = ColorDrawable(ViewUtils.getColor(R.color.color_d2d2d2))
                                }
                                return
                            }
                            if (!tv_create_bm.isEnabled) {
                                tv_create_bm.isEnabled = true
                                tv_create_bm.background = ColorDrawable(ViewUtils.getColor(R.color.color_theme))
                            }
                            return
                        } else {
                            if (template.purposeType == "7") {
                                PreviewNewMediaActivity.newInstance(this@TemplateListActivity, path_content!!, fileType, towTag)
                                return
                            }
                            if (template.purposeType == "9") {
                                PreviewXspActivity.startActivity(this@TemplateListActivity, path_content!!, fileType, true, towTag)
                                return
                            }
                            if (towTag) {
                                PreviewNewMediaActivity.newInstance(this@TemplateListActivity, path_content!!, fileType, true)
                                return
                            }
                            PreviewXspActivity.startActivity(this@TemplateListActivity, path_content!!, fileType)
                        }
                    }

                    override fun delete(id: String) {
                        deleteId = id
                        deleteDialog.show()
                    }

                    override fun changeName(templateId: String, name: String) {
                        this@TemplateListActivity.templateId = templateId
                        changeNameDialog.name = name
                    }
                }
            }
        }
    }

    override fun updateOtherTemplate(list: MutableList<UserTempletBean.DataBean>?) {
        if (releaseTag) {
            adapterGrid?.addList = UserTempletBean.filter(list)
            if (adapterGrid?.itemCount == 0) {
                noticeDialog.setNotice("暂无审核通过模板\n请耐心等待审核", 4000)
            }
        }
    }

    override fun updateTemplate(list: MutableList<UserTempletBean.DataBean>) {
        if (releaseTag) {
            adapterGrid?.hasAddOtherTemplate = false
            adapterGrid?.list = UserTempletBean.filter(list)
        } else {
            adapterGrid?.list = list
        }
    }

    override fun updateBMTemplateListView(data: MutableList<BianMinBean.DataBean>) {
        adapterList?.list = data
        if (TextUtils.isNotEmptyList(data)) {
            ll_no_data.visibility = View.GONE
            rv_template.visibility = View.VISIBLE
            line_create.visibility = View.VISIBLE
            tv_create_bm.visibility = View.VISIBLE
        } else {
            //rv_template.closeMenu()
            rv_template.visibility = View.GONE
            ll_no_data.visibility = View.VISIBLE
            line_create.visibility = View.GONE
            tv_create_bm.visibility = View.GONE
        }
    }

    override fun deleteSuccess() {
        adapterGrid?.updateForDelete()
        adapterList?.let {
            it.upateForDelete()
            if (!TextUtils.isNotEmptyList(it.list)) {
                ll_no_data.visibility = View.VISIBLE
                line_create.visibility = View.GONE
                rv_template.visibility = View.GONE
                tv_create_bm.visibility = View.GONE
            }
        }
    }

    override fun updateNameSuccess() {
        templateName?.let {
            adapterGrid?.updateForChangeName(it)
        }
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
                        mPresent?.delete(it, adType == 2 || adType == 4)
                    }
                }
            }
        }
    }
}
