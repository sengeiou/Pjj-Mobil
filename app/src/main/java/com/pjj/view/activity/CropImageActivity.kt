package com.pjj.view.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Bundle
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget

import com.pjj.R
import com.pjj.module.DiyDataBean
import com.pjj.present.BasePresent
import kotlinx.android.synthetic.main.activity_crop_image.*

/**
 * Create by xinheng on 2018-11-10 14:44:06。
 * describe：
 */
class CropImageActivity : BaseActivity<BasePresent<*>>() {
    companion object {
        const val DATA_BEAN = "data_bean"
        const val IMAGE_SRC = "image_src"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crop_image)
        setTitle("裁剪", Color.BLACK)
        val parcelableExtra = intent.getParcelableExtra<DiyDataBean.DataBean>(DATA_BEAN)
        parcelableExtra?.let {
            customShapeView.setShape(it)
        }
        var src = intent.getStringExtra(IMAGE_SRC)

        Glide.with(this).load(src!!).into(object : DrawableImageViewTarget(iv) {
            override fun onLoadFailed(errorDrawable: Drawable?) {
                super.onLoadFailed(errorDrawable)
                toast("设置图片失败")
            }

            override fun setResource(resource: Drawable?) {
                super.setResource(resource)
                bt_sure.setOnClickListener(onClick)
            }
        })
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.bt_sure -> crop()
        }
    }

    private fun crop() {
        var cropImage = customShapeView.cropImage(iv)
        var intent = Intent().apply {
            putExtra("bitmap",cropImage)
        }
        setResult(1,intent)
        finish()
    }
}
