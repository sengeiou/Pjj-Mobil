package com.pjj.view.activity

import android.content.Intent
import android.os.Bundle
import com.pjj.R
import com.pjj.present.BasePresent
import kotlinx.android.synthetic.main.activity_jian_bo_new_media.*

class JianBoNewMediaActivity : BaseActivity<BasePresent<*>>() {
    private var orderId: String? = null
    private var screenId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jian_bo_new_media)
        orderId = intent.getStringExtra("orderId")
        screenId = intent.getStringExtra("screenId")
        setTitle("监播")
        iv_look.setOnClickListener(onClick)
        tv_look.setOnClickListener(onClick)
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.iv_look, R.id.tv_look -> startActivity(Intent(this, ScreenshotsListActivity::class.java).putExtra("orderId",orderId).putExtra("screenId",screenId))
        }
    }
}
