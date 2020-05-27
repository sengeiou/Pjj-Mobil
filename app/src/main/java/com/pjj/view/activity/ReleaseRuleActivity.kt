package com.pjj.view.activity

import android.content.Intent
import android.os.Bundle

import com.pjj.R
import com.pjj.contract.ReleaseRuleContract
import com.pjj.module.xspad.XspManage
import com.pjj.present.ReleaseRulePresent
import com.pjj.utils.Log
import com.pjj.view.dialog.AddTemplateDialog
import com.pjj.view.dialog.ImageNoticeDialog
import com.pjj.view.dialog.NoPassDialog
import kotlinx.android.synthetic.main.activity_releaserule.*

/**
 * Create by xinheng on 2019/04/27 10:05。
 * describe：发布规则
 */
class ReleaseRuleActivity : BaseActivity<ReleaseRulePresent>(), ReleaseRuleContract.View {
    private var oneTag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_releaserule)
        intent.getStringExtra("title")?.let {
            //setTitle("传统信息发布规则")
            setTitle(it)
        }
        oneTag = intent.getBooleanExtra("oneTag", oneTag)
        mPresent = ReleaseRulePresent(this)
        iv_person.setOnClickListener(onClick)
        iv_merchants.setOnClickListener(onClick)
        if(XspManage.getInstance().adType==7){
            iv_screen.setImageResource(R.mipmap.newmedia)
        }
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.iv_person -> {
                XspManage.getInstance().identityType = 1
                mPresent?.certificationUser(oneTag)
            }
            R.id.iv_merchants -> {
                XspManage.getInstance().identityType = 2
                mPresent?.certificationUser(oneTag)
            }
        }
    }

    override fun allowNext(tag: String, tag_b: String, msg: String) {
        var adType = XspManage.getInstance().adType
        var identityType = XspManage.getInstance().identityType
        when (tag) {
            "-77" -> {
                startActivity(Intent(this, JianKangActivity::class.java))
                return
            }
            "-14" -> {
                //adTypeDialog.dismiss()
                noticeDialog.setNotice("暂无审核通过模板\n请耐心等待审核", 4000)
                return
            }
            "-13" -> {
                startActivity(Intent(this, TemplateListActivity::class.java)
                        .putExtra("title_text", (if (adType == 9) "传统" else "新媒体") + (if (identityType == 1) "个人" else "商家") + "模板")
                        .putExtra("identity_type", identityType.toString())
                        .putExtra("ad_type", adType)
                        .putExtra("releaseTag", XspManage.getInstance().newMediaData.releaseTag))
                return
            }
            "-11" -> {
                if (identityType == 3) {
                    SpeedScreenActivity.newInstance(this, true)
                    return
                }
                if (identityType == 2) {
                    when (adType) {
                        1, 2 -> startActivity(Intent(this, SelectReleaseAreaActivity::class.java))
                        else -> startActivity(Intent(this, SelectRandomReleaseAreaActivity::class.java))
                    }
                } else {
                    when (adType) {
                        1, 2 -> startActivity(Intent(this, SelectReleaseAreaActivity::class.java))
                        else -> startActivity(Intent(this, SelectRandomReleaseAreaActivity::class.java))
                    }
                }
            }
            "-12", "-10" -> {
                templateDialog.setImageResource(when (adType) {
                    1 -> R.mipmap.main_diy
                    2 -> R.mipmap.main_bm
                    3 -> R.mipmap.main_suiji_diy
                    4 -> R.mipmap.main_suiji_bm
                    else -> R.mipmap.main_pp
                })
            }
            else -> {
                tagP = tag
                tagM = tag_b
                //0 未认证  1审核中  2认证失败  //3认证成功
                if (identityType == 2) {//1个人 2商家
                    when (tag_b) {
                        "1" -> {
                            shenHeDialog.notice = "您的认证资料正在审核中，\n请耐心等待！"
                            shenHeDialog.show()
                        }
                        "2" -> {
                            noPassDialog.showLeft()
                            noPassDialog.errorText = msg
                        }
                        else -> goRenZheng()
                    }
                } else {//1个人
                    when (tag) {
                        "1" -> {
                            shenHeDialog.notice = "您的认证资料正在审核中，\n请耐心等待！"
                            shenHeDialog.show()
                        }
                        "2" -> {//未通过
                            noPassDialog.showLeft()
                            noPassDialog.errorText = msg
                        }
                        else -> goRenZheng()
                    }
                }
            }
        }
    }

    private val templateDialog: AddTemplateDialog by lazy {
        AddTemplateDialog(this).apply {
            onItemClickListener = object : AddTemplateDialog.OnItemClickListener {
                override fun leftClick() {
                    goCreateTemplate()
                }
            }
        }
    }
    private val shenHeDialog: ImageNoticeDialog by lazy {
        ImageNoticeDialog(this)
    }
    private val noPassDialog: NoPassDialog by lazy {
        NoPassDialog(this).apply {
            onItemClickListener = object : NoPassDialog.OnItemClickListener {
                override fun go(msg: String) {
                    goRenZheng(msg)
                }
            }
        }
    }

    private fun goCreateTemplate() {
        var temType = XspManage.getInstance().adType
        var identity_type = XspManage.getInstance().identityType
        var head = when (identity_type) {
            1 -> "个人"
            else -> "商家"
        }
        var _title = when (temType) {
            1 -> "DIY信息模板"
            2 -> "便民信息模板"
            3 -> "随机发布模板"
            else -> "随机便民模板"
        }
        startActivity(Intent(this, TemplateListActivity::class.java)
                .putExtra("title_text", head + _title)
                .putExtra("identity_type", identity_type.toString())
                .putExtra("ad_type", temType))
    }

    private var tagP: String? = null
    private var tagM: String? = null
    /**
     * 去认证
     */
    private fun goRenZheng(msg: String? = null) {
        Log.e("TAG", "goRenZheng")
        var intent = Intent(this, CertificationActivity::class.java)
        intent.putExtra("default", XspManage.getInstance().identityType.toString())
        intent.putExtra("personStatue", tagP)
        intent.putExtra("merchantsStatue", tagM)
        startActivity(intent)
    }
}
