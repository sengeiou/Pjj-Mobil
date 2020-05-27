package com.pjj.view.activity

import android.content.Intent
import android.os.Bundle
import com.pjj.R
import com.pjj.contract.FreeReleaseSelectContract
import com.pjj.module.VerificaBean
import com.pjj.module.xspad.XspManage
import com.pjj.present.BasePresent
import com.pjj.present.FreeReleaseSelectPresent
import com.pjj.utils.Log
import com.pjj.view.dialog.ImageNoticeDialog
import com.pjj.view.dialog.NoPassDialog
import kotlinx.android.synthetic.main.activity_free_release_select.*

class FreeReleaseSelectActivity : BaseActivity<FreeReleaseSelectPresent>(), FreeReleaseSelectContract.View {
    private var verificaBean: VerificaBean? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free_release_select)
        setTitle("免费线上发布")
        iv_person.setOnClickListener(onClick)
        iv_merchants.setOnClickListener(onClick)
        mPresent = FreeReleaseSelectPresent(this)
        verificaBean = savedInstanceState?.getParcelable("verificaBean")
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putParcelable("verificaBean", verificaBean)
        super.onSaveInstanceState(outState)
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.iv_person -> startActivity(Intent(this, FreeReleaseTypeActivity::class.java).putExtra("type", 1))
            R.id.iv_merchants -> mPresent?.loadRenZhengTask()
        }
    }

    override fun result(result: VerificaBean) {
        verificaBean = result
        when (result.userBusinessAuth) {//0 未认证  1审核中  2认证失败  3认证成功
            "3" -> startActivity(Intent(this, FreeReleaseTypeActivity::class.java).putExtra("type", 2))
            "1" -> {
                shenHeDialog.notice = "您的认证资料正在审核中，\n请耐心等待！"
                shenHeDialog.show()
            }
            "2" -> {//认证失败
                noPassDialog.showLeft()
                noPassDialog.errorText = result.userBusinessAuthOpinion
            }
            else -> {//未认证
                goRenZheng()
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

    /**
     * 去认证
     */
    private fun goRenZheng(msg: String? = null) {
        Log.e("TAG", "goRenZheng")
        var intent = Intent(this, CertificationActivity::class.java)
        intent.putExtra("default", "2")
        intent.putExtra("personStatue", verificaBean!!.userAuth)
        intent.putExtra("merchantsStatue", verificaBean!!.userBusinessAuth)
        startActivity(intent)
    }
}
