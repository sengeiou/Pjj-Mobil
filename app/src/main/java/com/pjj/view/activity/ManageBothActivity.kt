package com.pjj.view.activity

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import com.pjj.R
import com.pjj.present.BasePresent
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_manage_both.*

class ManageBothActivity : BaseActivity<BasePresent<*>>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_both)
        setTitle("屏幕管理")
        view_space_media.setOnClickListener(onClick)
        view_space_note.setOnClickListener(onClick)
        (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0).background = ColorDrawable(ViewUtils.getColor(R.color.color_f4f4f4))
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.view_space_media -> startActivity(Intent(this, ScreenManageActivity::class.java).putExtra("useType", "cooperativePartner"))
            R.id.view_space_note -> startActivity(Intent(this, ScreenManageActivity::class.java).putExtra("useType", "estateManagement"))
        }
    }
}
