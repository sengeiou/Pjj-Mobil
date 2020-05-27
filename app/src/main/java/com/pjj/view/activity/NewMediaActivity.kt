package com.pjj.view.activity

import android.os.Bundle
import com.pjj.R
import com.pjj.present.BasePresent

class NewMediaActivity : BaseActivity<BasePresent<*>>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_media)
    }
}
