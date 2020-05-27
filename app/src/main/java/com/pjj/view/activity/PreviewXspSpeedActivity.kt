package com.pjj.view.activity

import android.os.Bundle
import com.pjj.R
import com.pjj.module.xspad.XspManage
import com.pjj.present.BasePresent
import kotlinx.android.synthetic.main.activity_preview_xsp_speed.*

class PreviewXspSpeedActivity : BaseActivity<BasePresent<*>>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_xsp_speed)
        setTitle("预览")
        svg.speedData = XspManage.getInstance().speedScreenData.clone
    }
}
