package com.pjj.view.dialog

import android.content.Intent
import android.os.Bundle
import com.pjj.R
import com.pjj.contract.MainContract
import com.pjj.module.AppUpdateBean
import com.pjj.module.xspad.XspManage
import com.pjj.present.MainPresent
import com.pjj.utils.Log
import com.pjj.view.activity.*
import kotlinx.android.synthetic.main.activity_elevator_next.*

class ElevatorNextActivity : BaseActivity<MainPresent>(), MainContract.View {
    private val adTypeDialog: AdTypeDialog by lazy {
        AdTypeDialog(this@ElevatorNextActivity).apply {
            onTypeSelectListener = object : AdTypeDialog.OnTypeSelectListener {
                override fun selectMerchamts() {
                    XspManage.getInstance().identityType = 2
                    mPresent?.certificationUser()
                    //mPresent?.getTemplateList()
                }

                override fun selectPerson() {
                    XspManage.getInstance().identityType = 1
                    mPresent?.certificationUser()
                    //mPresent?.getTemplateList()
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
    private val templateDialog: AddTemplateDialog by lazy {
        AddTemplateDialog(this).apply {
            onItemClickListener = object : AddTemplateDialog.OnItemClickListener {
                override fun leftClick() {
                    goCreateTemplate()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_elevator_next)
        setTitle("电梯安全传媒发布")
        mPresent = MainPresent(this)
        iv_elevator_media.setOnClickListener(onClick)
        iv_div_media.setOnClickListener(onClick)
        iv_difangziying.setOnClickListener(onClick)
        iv_pjj_lingshou.setOnClickListener(onClick)
        iv_yixue.setOnClickListener(onClick)
        iv_chuantong.setOnClickListener(onClick)
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.iv_elevator_media -> showADDialog(2)
            R.id.iv_chuantong -> showADDialog(9)
            R.id.iv_div_media -> showADDialog(4)
            R.id.iv_difangziying -> showADDialog(1)
            R.id.iv_pjj_lingshou -> showADDialog(3)
            R.id.iv_yixue -> showADDialog(5 + 1)
        }
    }

    private fun showADDialog(type: Int) {
        if (type == 77) {
            mPresent?.certificationUser(true)
            return
        }
        if (type != 4) {
            XspManage.getInstance().bianMinPing = 0
        }
        if (type == 5) {//拼屏
            XspManage.getInstance().identityType = 3
            XspManage.getInstance().adType = 5
            mPresent?.certificationUser()
            return
        }
        //mPresent?.certificationUser()
        if (type == 9) {
            XspManage.getInstance().adType = type
            XspManage.getInstance().newMediaData.releaseTag = true
            startActivity(Intent(this, ReleaseRuleActivity::class.java).putExtra("title", "传统发布规则"))
        } else {
            //showNotice("该功能暂未开放\n敬请期待")
        }
        adTypeDialog.setType(type)
    }

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
    override fun allowNext(tag: String, tag_b: String, msg: String?) {
        var adType = XspManage.getInstance().adType
        var identityType = XspManage.getInstance().identityType
        when (tag) {
            "-77" -> {
                startActivity(Intent(this, JianKangActivity::class.java))
                return
            }
            "-13" -> {
                startActivity(Intent(this, TemplateListActivity::class.java)
                        .putExtra("title_text", "个人传媒发布模板")
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

    override fun allowAdDialog() {
    }

    override fun appVersionResult(data: AppUpdateBean) {
    }

    override fun installApk(filePath: String) {
    }
}
