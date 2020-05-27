package com.pjj.view.fragment

import android.os.Bundle
import android.view.View

import com.pjj.R
import com.pjj.present.BasePresent
import kotlinx.android.synthetic.main.fragment_image_ad.*

/**
 * Created by XinHeng on 2018/11/30.
 * describe：diy图片样式选择
 */
class ImageAdFragment : BaseFragment<BasePresent<*>>() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_image_ad
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iv_image_all.setOnClickListener(onClick)
        iv_image_half.setOnClickListener(onClick)
        iv_image_four.setOnClickListener(onClick)
        iv_image_more.setOnClickListener(onClick)
    }
    private var onClick=View.OnClickListener {
//        when(it.id){
//            R.id.iv_image_all->
//            R.id.iv_image_half->
//            R.id.iv_image_four->
//            R.id.iv_image_more->
//        }
    }
}
