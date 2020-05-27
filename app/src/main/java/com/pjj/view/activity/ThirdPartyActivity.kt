package com.pjj.view.activity

import android.os.Bundle
import com.pjj.R
import com.pjj.contract.ThirdPartyContract
import com.pjj.present.ThirdParthPresent
import com.pjj.view.dialog.TwoTextDialog

class ThirdPartyActivity : BaseActivity<ThirdParthPresent>(), ThirdPartyContract.View {
    private val unBindDialog: TwoTextDialog by lazy {
        TwoTextDialog(this).apply {
            onTwoTextListener = object : TwoTextDialog.OnTwoTextListener {
                override fun sureClick() {
                    mPresent?.loadUnbind()
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_third_party)
        setTitle("账号与安全")
    }

}
