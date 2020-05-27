package com.pjj.view.activity

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import com.pjj.R
import com.pjj.present.BasePresent
import com.pjj.view.fragment.BaseFragment
import com.pjj.view.fragment.ImageAdFragment
import kotlinx.android.synthetic.main.activity_create_ad.*

/**
 * Created by XinHeng on 2018/11/30.
 * describe：
 */
class CreateAdActivity : FragmentsActivity<BaseFragment<*>,BasePresent<*>>() {
    override fun getFragmentContainerViewId(): Int {
        return R.id.fl_content
    }

    override fun getFragment(tag: String): BaseFragment<*>? {
        return when (tag) {
            "rb_bian_min" -> ImageAdFragment()
            "rb_diy_image" -> null
            else -> null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_ad)
        setTitle("创建广告", Color.WHITE, 0)
        rb_bian_min.setOnClickListener(onClick)
        rb_diy_image.setOnClickListener(onClick)
        rb_diy_video.setOnClickListener(onClick)
        onClick(R.id.rb_bian_min)
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.rb_bian_min -> showFragment("rb_bian_min")
            R.id.rb_diy_image -> showFragment("rb_diy_image")
            R.id.rb_diy_video -> showFragment("rb_diy_video")
        }
    }
}
