package com.pjj.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import com.bumptech.glide.Glide
import com.dmcbig.mediapicker.PickerActivity
import com.dmcbig.mediapicker.PickerConfig
import com.dmcbig.mediapicker.entity.Media
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.contract.FreeReleaseContract
import com.pjj.intent.AliFile
import com.pjj.module.TopPriceBean
import com.pjj.module.parameters.ReleaseFreeOrder
import com.pjj.present.BasePresent
import com.pjj.present.FreeReleasePresent
import com.pjj.utils.CalculateUtils
import com.pjj.utils.SpaceItemDecoration
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.adapter.FreeZhiDingAdapter
import com.pjj.view.dialog.RemindNoticeDialog
import com.pjj.view.dialog.SelectMediaDialog
import kotlinx.android.synthetic.main.activity_free_release.*
import kotlinx.android.synthetic.main.layout_free_image_item.*
import kotlinx.android.synthetic.main.layout_free_text_item.*
import kotlinx.android.synthetic.main.layout_free_video_item.*
import kotlinx.android.synthetic.main.layout_free_zhi_ding_select_item.*
import java.io.File

class FreeReleaseActivity : PayActivity<FreeReleasePresent>(), FreeReleaseContract.View {

    override fun getMakeOrderJson(): String {
        return ""
    }

    override fun getFinalPayPrice(): Float {
        return mPrice
    }

    override fun makeOrder() {
        onClick(R.id.tv_sure)
    }

    override fun updateMakeOrderSuccess(orderId: String) {
        if (getIsZhiDing()) {
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

    private var type = 0
    private var freeType = 0
    private var pathImage: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free_release)
        setTitle("信息发布")
        if (null == savedInstanceState) {
            type = intent.getIntExtra("type", type)
            freeType = intent.getIntExtra("free_type", freeType)
        } else {
            type = savedInstanceState.getInt("type")
            freeType = savedInstanceState.getInt("freeType")
            pathImage = savedInstanceState.getString("pathImage")
        }
        mPresent = FreeReleasePresent(this)
        tv_read_rule.setOnClickListener(onClick)
        tv_sure.setOnClickListener(onClick)
        iv_read.buttonDrawable = ViewUtils.createSelectDrawable(R.mipmap.select_red, R.mipmap.unselect, android.R.attr.state_checked)!!.apply {
            val dp = ViewUtils.getDp(R.dimen.dp_16)
            setBounds(0, 0, dp, dp)
        }
        initView()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.let {
            it.putInt("type", type)
            it.putInt("freeType", freeType)
            it.putString("pathImage", pathImage)
        }
        super.onSaveInstanceState(outState)
    }

    private val remindNoticeDialog: RemindNoticeDialog by lazy {
        RemindNoticeDialog(this, 0)
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.iv, R.id.ll_init -> {
                selectMediaDialog.show()
            }
            R.id.tv_read_rule -> WebActivity.newInstance(this, "置顶协议", "http://protal.test.pingjiajia.cn/guide/#/guideInfo?dicId=b13aa0ac4b074f44893b926e588c586e")
            R.id.tv_sure -> {
                val select = iv_read.isChecked
                if (!select && getIsZhiDing()) {
                    remindNoticeDialog.show()
                    return
                }
                val title = getFreeTitle()
                if (title.isEmpty()) {
                    showNotice("为了更好的广告效果，请输入标题")
                    return
                }
                val content = getContent()
                if (TextUtils.isEmpty(content)) {
                    showNotice(when (freeType) {
                        0 -> "请选择图片"
                        1 -> "请输入您发布的信息"
                        else -> "请您输入可播放的视频链接"
                    })
                    return
                }
                if (freeType == 0) {
                    loadImageView(content!!, title)
                } else {
                    release(title, content!!)
                }
            }
        }
    }

    private fun initView() {
        when (freeType) {
            0 -> {//图片
                findViewById<ViewStub>(R.id.stub_image).inflate()
                findViewById<ViewStub>(R.id.stub_zhiding).inflate()
                initZhiDing()
                ll_init.setOnClickListener(onClick)
                iv.setOnClickListener(onClick)
                setWindowBg(R.color.color_f1f1f1)
            }
            1 -> {//文本
                findViewById<ViewStub>(R.id.stub_text).inflate()
                val tvCount = tv_count
                et_text.addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    }

                    @SuppressLint("SetTextI18n")
                    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                        val length = s?.length ?: 0
                        tvCount.text = "$length/294"
                    }

                })
                findViewById<ViewStub>(R.id.stub_zhiding).inflate()
                initZhiDing()
            }
            2 -> {//视频链接
                findViewById<ViewStub>(R.id.stub_video).inflate()
                ll_read.visibility = View.INVISIBLE
                iv_read.isChecked = true
            }
        }
    }

    private fun setWindowBg(colorId: Int) {
        (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0).background = ColorDrawable(ViewUtils.getColor(colorId))
    }

    private fun getFreeTitle(): String {
        return when (freeType) {
            0 -> {//图片
                et_name.text.toString().trim()
            }
            1 -> {//文本
                et_text_title.text.toString().trim()
            }
            else -> {//视频链接
                et_video_title.text.toString().trim()
            }
        }
    }

    private fun getContent(): String? {
        return when (freeType) {
            0 -> {//图片
                pathImage
            }
            1 -> {//文本
                et_text.text.toString().trim()
            }
            else -> {//视频链接
                et_video_url.text.toString().trim()
            }
        }
    }

    private fun getIsZhiDing(): Boolean {
        return when (freeType) {
            2 -> false
            else -> cb_zhiding.isChecked
        }
    }

    private fun loadImageView(path: String, title: String) {
        showWaiteStatue()
        AliFile.getInstance().uploadFile(path, object : AliFile.UploadResult() {
            override fun success(result: String) {
                release(title, result)
            }

            override fun fail(error: String?) {
                showNotice(error)
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun initZhiDing() {
        cb_zhiding.buttonDrawable = ViewUtils.createSelectDrawable(R.mipmap.select_square, R.mipmap.unselect_square, android.R.attr.state_checked)
        rv_zhiding.adapter = freeZhiDingAdapter
        rv_zhiding.addItemDecoration(SpaceItemDecoration(this, LinearLayoutManager.HORIZONTAL, ViewUtils.getDp(R.dimen.dp_9), Color.WHITE))
        cb_zhiding.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                tv_sum_price.text = CalculateUtils.m1(mPrice) + " 元"
            } else {
                tv_sum_price.text = "0.00 元"
            }
        }
        mPresent?.loadTopPriceTask()
    }

    private fun release(title: String, content: String) {
        val isZhiDing = getIsZhiDing()
        if (!isZhiDing) {
            mTopId = null
        }
        val releaseFreeOrder = ReleaseFreeOrder().apply {
            userId = PjjApplication.application.userId
            this.title = title
            orderType = (freeType + 1).toString()
            if (freeType == 0) {
                fileName = content
            } else {
                this.content = content
            }
            identityType = type.toString()
            this.topId = mTopId
            price = mPrice
        }
        if (freeType != 1) {
            showWaiteStatue()
            mPresent?.loadReleaseFreeOrder(releaseFreeOrder)
        } else {//文字
            TextPreActivity.newInstance(this, title, content, releaseFreeOrder)
        }
    }

    private val selectMediaDialog: SelectMediaDialog by lazy {
        SelectMediaDialog(this).apply {
            //setType(1)
            onItemClickListener = object : SelectMediaDialog.OnItemClickListener {
                override fun takePhoto() {
                    startPhoto()
                }

                override fun selectPhoto() {
                    val intent = Intent(this@FreeReleaseActivity, PickerActivity::class.java)
                    intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE)
                    intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 1)
                    //intent.putExtra(PickerConfig.MAX_SELECT_SIZE, 20)
                    startActivityForResult(intent, 201)
                }

                override fun selectVideo() {
                    val intent = Intent(this@FreeReleaseActivity, PickerActivity::class.java)
                    intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_VIDEO)
                    intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 1)
                    startActivityForResult(intent, 202)
                }
            }
        }
    }

    private fun startPhoto() {
        // 跳转到系统的拍照界面
        pathImage = PjjApplication.App_Path + "photo/${System.currentTimeMillis()}-photo.png"
        //系统相机
        var intentCamera = Intent()
        var imageUri = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                FileProvider.getUriForFile(this, "com.pjj.fileprovider", File(pathImage))
            }
            else -> Uri.fromFile(File(pathImage))
        }
        intentCamera.action = MediaStore.ACTION_IMAGE_CAPTURE
        //将拍照结果保存至photo_file的Uri中，不保留在相册中
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intentCamera, 200)
    }

    private var width = 1080
    private var height = 1886
    private var media: Media? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == 201 || requestCode == 202) && resultCode == PickerConfig.RESULT_CODE) {
            var select = data?.getParcelableArrayListExtra<Media>(PickerConfig.EXTRA_RESULT)
            select?.run {
                if (size > 0) {
                    var media = this[0]
                    media?.let {
                        var path = it.path
                        this@FreeReleaseActivity.media = it
                        if (it.mediaType != 3) {//图片
                            iv_video.visibility = View.GONE
                            CropActivity.newInstanceShape(this@FreeReleaseActivity, path, PjjApplication.App_Path + "image/", 203,
                                    width, height)
                        } else {//视频
                            iv.visibility = View.VISIBLE
                            ll_init.visibility = View.GONE
                            Glide.with(this@FreeReleaseActivity).load(path).into(iv)
                            iv_video.visibility = View.VISIBLE
                            this@FreeReleaseActivity.pathImage = path
                        }
                    }
                }
            }
        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            var file = File(pathImage)
            if (file.exists()) {
                media = Media()
                media?.run {
                    path = pathImage
                    mediaType = 1
                }
                CropActivity.newInstanceShape(this@FreeReleaseActivity, pathImage!!, PjjApplication.App_Path + "image/", 203,
                        width, height)
            }
        } else if (requestCode == 203 && resultCode == CropActivity.RESULT_CODE) {
            data?.getStringExtra(CropActivity.IMAGE_SAVE_PATH)?.let {
                iv.visibility = View.VISIBLE
                ll_init.visibility = View.GONE
                Glide.with(this@FreeReleaseActivity).load(it).into(iv)
                pathImage = it
                media?.run {
                    path = it
                    mediaType = 1
                }
            }
        }
    }

    private var mPrice = 0f
    private var mTopId: String? = null
    private val freeZhiDingAdapter: FreeZhiDingAdapter by lazy {
        FreeZhiDingAdapter().apply {
            onFreeZhiDingAdapterListener = object : FreeZhiDingAdapter.OnFreeZhiDingAdapterListener {
                @SuppressLint("SetTextI18n")
                override fun itemClick(price: Float, topId: String) {
                    if (cb_zhiding.isChecked) {
                        tv_sum_price.text = CalculateUtils.m1(price) + " 元"
                    } else {
                        if (null != mTopId)
                            showNotice("请先勾选置顶推广\n再进行操作")
                    }
                    mPrice = price
                    mTopId = topId
                }
            }
        }
    }

    override fun updatePrice(datas: MutableList<TopPriceBean.DataBean>?) {
        freeZhiDingAdapter.datas = datas
    }
}
