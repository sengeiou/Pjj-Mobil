package com.pjj.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.bumptech.glide.request.target.ImageViewTarget

import com.pjj.R
import com.pjj.module.DiyDataBean
import com.pjj.present.BasePresent
import com.pjj.utils.BitmapUtils
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.fragment_test.*


class CropActivity : BaseActivity<BasePresent<*>>() {

    private var savePath: String? = null
    private lateinit var path: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_test)
        setTitle("图片裁剪")
        bt.setOnClickListener { startCrop() }
        customShapeView.isEnabled = false
        var shape = intent.getBooleanExtra(SHAPE, false)
        var dataBean = DiyDataBean.DataBean()
        var width: Int
        var height: Int
        if (shape) {
            dataBean.isCircle = "0"
            width = ViewUtils.getDp(R.dimen.dp_300)
            dataBean.wide = width.toString()
            height = width * intent.getIntExtra(HEIGHT, 0) / intent.getIntExtra(WIDTH, 0)
            dataBean.high = height.toString()
        } else {
            dataBean.isCircle = "1"
            width = ViewUtils.getDp(R.dimen.dp_192)
            dataBean.wide = width.toString()
            dataBean.high = dataBean.wide
        }
        customShapeView.setShape(dataBean)
        //iv.setImageResource(R.mipmap.id_card_01)
        customShapeView.post {
            iv.clipBorder = customShapeView.rectF
            var stringExtra = intent.getStringExtra(IMAGE_PATH)!!
            Glide.with(this).load(stringExtra).into(iv)
        }
        savePath = intent.getStringExtra(IMAGE_SAVE_PATH)
    }

    private fun startCrop() {
        var bit = customShapeView.cropImage(iv)
        /*var dialog = Dialog(activity)
        var imageView = ImageView(activity)
        imageView.setImageBitmap(bit)
        dialog.setContentView(imageView)
        dialog.show()*/
        showWaiteStatue()
        Log.e("TAG", "bit = null")
        path = savePath + "${System.currentTimeMillis()}.png"
        BitmapUtils.saveBitmapThread(bit, path) {
            stopSelf(it)
        }
    }

    private fun stopSelf(tag: Boolean) {
        runOnUiThread {
            cancelWaiteStatue()
            if (tag) {
                setResult(RESULT_CODE, Intent().putExtra(IMAGE_SAVE_PATH, path))
                finish()
            } else {
                showNotice("保存失败")
            }
        }

    }

    companion object {
        val IMAGE_PATH = "image_path"
        val IMAGE_SAVE_PATH = "image_save_path"
        val RESULT_CODE = 200
        val SHAPE = "shape"
        val WIDTH = "width"
        val HEIGHT = "height"
        @JvmStatic
        fun newInstance(c: Activity, path: String, savePath: String, requestCode: Int) {
            c.startActivityForResult(Intent(c, CropActivity::class.java)
                    .putExtra(IMAGE_PATH, path)
                    .putExtra(IMAGE_SAVE_PATH, savePath), requestCode)
        }

        @JvmStatic
        fun newInstanceShape(c: Activity, path: String, savePath: String, requestCode: Int, width: Int, height: Int) {
            c.startActivityForResult(Intent(c, CropActivity::class.java)
                    .putExtra(IMAGE_PATH, path)
                    .putExtra(SHAPE, true)
                    .putExtra(WIDTH, width)
                    .putExtra(HEIGHT, height)
                    .putExtra(IMAGE_SAVE_PATH, savePath), requestCode)
        }
    }
}
