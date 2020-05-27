package com.pjj.view.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.contract.FreeReleaseContract
import com.pjj.contract.MyTuiGuangContract
import com.pjj.intent.AliFile
import com.pjj.module.TopPriceBean
import com.pjj.module.parameters.ReleaseFreeOrder
import com.pjj.present.BasePresent
import com.pjj.present.FreeReleasePresent
import com.pjj.present.MyTuiGuangPresent
import com.pjj.utils.BitmapUtils
import kotlinx.android.synthetic.main.activity_text_pre.*

class TextPreActivity : PayActivity<FreeReleasePresent>(), FreeReleaseContract.View {
    override fun getMakeOrderJson(): String {
        return ""
    }

    override fun getFinalPayPrice(): Float {
        return zhiDing?.price ?: 0f
    }

    override fun updateMakeOrderSuccess(orderId: String) {
        if (zhiDing?.topId != null) {
            super.updateMakeOrderSuccess(orderId)
        } else {
            payFinishStartOrderView(1)
        }
    }

    override fun payFinishStartOrderView(index: Int) {
        cancelWaiteStatue()
        startActivity(Intent(this, MyReleaseActivity::class.java))
        finish()
    }

    override fun updatePrice(datas: MutableList<TopPriceBean.DataBean>?) {
    }

    companion object {

        @JvmStatic
        fun newInstance(activity: BaseActivity<*>, title: String, text: String, zhiDing: ReleaseFreeOrder? = null) {
            activity.startActivity(Intent(activity, TextPreActivity::class.java)
                    .putExtra("title", title)
                    .putExtra("text", text)
                    //.putExtra("type", type)
                    .putExtra("zhiDing", zhiDing))
        }
    }

    //private var type = 0
    private var title: String? = null
    private var text: String? = null
    private var zhiDing: ReleaseFreeOrder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_pre)
        setTitle("预览")
        tv_release.setOnClickListener(onClick)
        if (null == savedInstanceState) {
            //type = intent.getIntExtra("type", type)
            title = intent.getStringExtra("title")
            text = intent.getStringExtra("text")
            zhiDing = intent.getParcelableExtra("zhiDing")
        } else {
            //type = savedInstanceState.getInt("type")
            title = savedInstanceState.getString("title")
            text = savedInstanceState.getString("text")
            zhiDing = savedInstanceState.getParcelable("zhiDing")
        }
        mPresent = FreeReleasePresent(this)
        initView()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.let {
            //it.putInt("type", type)
            it.putString("title", title)
            it.putString("text", text)
            it.putParcelable("zhiDing", zhiDing)
        }
        super.onSaveInstanceState(outState)
    }

    private fun initView() {
        tv_title.text = title
        tv_text.text = text
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_release -> {
                zhiDing?.let { bean ->
                    showWaiteStatue()
                    val bitmap = Bitmap.createBitmap(ll_text.measuredWidth, ll_text.measuredHeight, Bitmap.Config.ARGB_8888)
                    val canvas = Canvas(bitmap)
                    ll_text.draw(canvas)
                    val path = PjjApplication.App_Path + "module/" + System.currentTimeMillis() + "-text.jpg"
                    BitmapUtils.saveBitmapThread(bitmap, Bitmap.CompressFormat.JPEG, path, 100) {
                        runOnUiThread {
                            if (it) {
                                AliFile.getInstance().uploadFile(path, object : AliFile.UploadResult() {
                                    override fun success(result: String) {
                                        bean.fileName = result
                                        mPresent?.loadReleaseFreeOrder(bean)
                                    }

                                    override fun fail(error: String?) {
                                        showNotice("上传文字图片失败,请稍后重试")
                                    }
                                })
                            } else {
                                cancelWaiteStatue()
                                showNotice("保存预览图失败，请稍后重试")
                            }
                        }
                    }
                }
            }
        }
    }
}
