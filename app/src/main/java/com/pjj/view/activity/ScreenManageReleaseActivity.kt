package com.pjj.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.dmcbig.mediapicker.PickerActivity
import com.dmcbig.mediapicker.PickerConfig
import com.dmcbig.mediapicker.entity.Media
import com.pjj.PjjApplication

import com.pjj.R
import com.pjj.contract.ScreenManageReleaseContract
import com.pjj.intent.AliFile
import com.pjj.module.ScreenModelBean
import com.pjj.module.parameters.ReleaseScreenModel
import com.pjj.module.parameters.Template
import com.pjj.module.xspad.XspManage
import com.pjj.present.ScreenManageReleasePresent
import com.pjj.utils.*
import com.pjj.view.adapter.ScreenManageReleaseModelAdapter
import com.pjj.view.dialog.DatePopuWindow
import com.pjj.view.dialog.SelectMediaDialog
import com.pjj.view.dialog.TimeDefinedDialog
import kotlinx.android.synthetic.main.activity_screenmanagerelease.*
import java.io.File
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * Create by xinheng on 2019/05/22 15:12。
 * describe：屏幕发布
 */
class ScreenManageReleaseActivity : BaseActivity<ScreenManageReleasePresent>(), ScreenManageReleaseContract.View {
    private var dates: MutableList<String>? = null
    private var screenList: ArrayList<String>? = null
    private var media: Media? = null
    private var partnerFileId: String? = null
    private var allSelfTag = false
    private var adapter = ScreenManageReleaseModelAdapter().apply {
        onScreenManageReleaseModelListener = object : ScreenManageReleaseModelAdapter.OnScreenManageReleaseModelListener {
            override fun delete(bean: ScreenModelBean.DataBean) {
                mPresent?.loadDeleteModel(bean.partnerFileId)
            }

            override fun select(partnerFileId: String?) {
                this@ScreenManageReleaseActivity.partnerFileId = partnerFileId
            }
        }
    }
    private val dateDialog: DatePopuWindow by lazy {
        DatePopuWindow(this).apply {
            setHasSelectDate(Calendar.getInstance())
            onDateSelectListener = object : DatePopuWindow.OnDateSelectListener {
                override fun dateSelect(dates: MutableList<String>) {
                    this@ScreenManageReleaseActivity.dates = dates
                    val text = if (dates!!.size > 1) (dates!![0].replace("-", ".") + "-" + dates!![dates!!.size - 1].replace("-", ".")) else dates!![0].replace("-", ".")
                    tv_date.text = text
                }

                override fun dismiss() {
                    titleView.setTextMiddle("发布", Color.WHITE)
                }
            }
        }
    }

    /**
     * 在此打开，刷新
     */
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        var booleanExtra = intent.getBooleanExtra("refresh", false)
        if (booleanExtra) {
            mPresent?.loadManageModelTask("1")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screenmanagerelease)
        setTitle("发布")
        screenList = intent.getStringArrayListExtra("screenList")
        allSelfTag = intent.getBooleanExtra("allSelfTag", allSelfTag)
        tv_date.setOnClickListener(onClick)
        tv_update_text.setOnClickListener(onClick)
        tv_release.setOnClickListener(onClick)
        iv_left.setOnClickListener(onClick)
        iv_right.setOnClickListener(onClick)
        rv.adapter = this@ScreenManageReleaseActivity.adapter
        rv.addItemDecoration(SpaceItemDecoration(this, LinearLayoutManager.HORIZONTAL, ViewUtils.getDp(R.dimen.dp_19), Color.WHITE))
        mPresent = ScreenManageReleasePresent(this)
        mPresent?.loadManageModelTask("1")
        if (allSelfTag) {
            ll_date.visibility = View.GONE
        }
        addPlayTime()
    }

    private val dp96 = ViewUtils.getDp(R.dimen.dp_96)
    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.iv_left -> rv.smoothScrollBy(-dp96, 0)
            R.id.iv_right -> rv.smoothScrollBy(dp96, 0)
            R.id.tv_date -> {
                titleView.setTextMiddle("选择投放日期", Color.WHITE)
                dateDialog.showAsDropDown(titleView)
            }
            R.id.tv_update_text -> {
                val count = adapter.itemCount
                if (count >= 10) {
                    tv_update_text.text = ""
                    showNotice("素材最多为十个")
                    return
                }
                startActivity(Intent(this@ScreenManageReleaseActivity, PersonMediaTemActivity::class.java).putExtra("template", Template().apply {
                    userId = PjjApplication.application.userId
                    purposeType = "7"
                    selfUseTag = "1"
                }).putExtra("title", "创建模板"))
            }

            R.id.tv_release -> {
                if (!TextUtils.isNotEmptyList(screenList)) {
                    showNotice("请选择屏幕")
                    return
                }
                if (TextUtils.isNotEmptyList(dates) || allSelfTag) {
                    val id = partnerFileId
                    if (TextUtils.isEmpty(id)) {
                        showNotice("您还没有选择素材")
                        return
                    }
                    mPresent?.loadPushManageModelTask(ReleaseScreenModel().apply {
                        if (TextUtils.isNotEmptyList(dates))
                            playDate = if (dates!!.size > 1) (dates!![0].replace("-", ".") + "-" + dates!![dates!!.size - 1].replace("-", ".")) else dates!![0].replace("-", ".")
                        playTime = timeHours()
                        userId = PjjApplication.application.userId
                        screen_time = createScreenTime()
                        partnerFileId = id
                        orderType = "7"
                        showTime = videoPlayTimes.toString()
                    })
                } else {
                    showNotice("您还没有选择日期")
                }
            }
        }
    }

    override fun update(list: MutableList<ScreenModelBean.DataBean>?) {
        if (TextUtils.isNotEmptyList(list)) {
            line_ll.visibility = View.VISIBLE
            ll_rv.visibility = View.VISIBLE
            adapter.list = list
            if (list!!.size >= 10) {
                tv_update_text.text = ""
                tv_update_text.isEnabled = true
            } else {
                tv_update_text.text = "上传素材"
                tv_update_text.isEnabled = true
            }
        } else {
            line_ll.visibility = View.GONE
            ll_rv.visibility = View.GONE
            tv_update_text.text = "上传素材"
        }
    }

    override fun pushSuccess() {
        smillTag = true
        showNotice("发布成功")
        smillTag = false
        Handler().postDelayed({
            startActivity(Intent(this, ScreenManageOrderListActivity::class.java))
            finish()
        }, 2100)
    }

    override fun deleteOrAddSuccess(tagDelete: Boolean) {
        val text = if (tagDelete) "删除成功" else "上传成功"
        smillTag = true
        showNotice(text)
        smillTag = false
        mPresent?.loadManageModelTask("1")
    }

    private fun createScreenTime(): String {
        val value = createTime()
        return JsonUtils.toJsonString(HashMap<String, String>().apply {
            screenList?.forEach {
                this[it] = value
            }
        })
    }

    private fun createTime(): String {
        val build = StringBuffer()
        dates?.forEach {
            build.append(it)
            build.append("#")
            build.append(timeHours())
            build.append("&")
        }
        if (build.isNotEmpty())
            build.deleteCharAt(build.length - 1)
        return build.toString()
    }

    private fun timeHours(): String {
        return "0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23"
    }

    private var mTempPhotoPath: String? = null


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == 201 || requestCode == 202) && resultCode == PickerConfig.RESULT_CODE) {
            var select = data?.getParcelableArrayListExtra<Media>(PickerConfig.EXTRA_RESULT)
            select?.run {
                if (size > 0) {
                    media = this[0]
                    media?.let {
                        var path = it.path
                        //iv.background = ColorDrawable(ViewUtils.getColor(R.color.color_666666))
                        if (it.mediaType != 3) {//图片
                            CropActivity.newInstanceShape(this@ScreenManageReleaseActivity, path, PjjApplication.App_Path + "image/", 203,
                                    1080, 1886)
                        } else {
                            if (File(path).length() > 20 * 1204 * 1024) {
                                media = null
                                //iv_video.visibility = View.GONE
                                //Glide.with(this@ScreenManageReleaseActivity).load(R.mipmap.create_tem111).into(iv)
                                showNotice("视频大于20M，请重新选择")
                                return
                            }
                            //BitmapUtils.loadFirstImageForVideo(Glide.with(this@ScreenManageReleaseActivity), path, iv)
                            uploadMediaForAli(path, false)
                        }
                    }
                }
            }
        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            var file = File(mTempPhotoPath)
            if (file.exists()) {
                media = Media()
                media?.run {
                    path = mTempPhotoPath
                    mediaType = 1
                }
                CropActivity.newInstanceShape(this@ScreenManageReleaseActivity, mTempPhotoPath!!, PjjApplication.App_Path + "image/", 203,
                        1080, 1886)
            }
        } else if (requestCode == 203 && resultCode == CropActivity.RESULT_CODE) {
            data?.getStringExtra(CropActivity.IMAGE_SAVE_PATH)?.let {
                uploadMediaForAli(it, true)
            }
        }
    }

    /**
     * 上传文件
     * 阿里云
     */
    private fun uploadMediaForAli(path: String, isImg: Boolean) {
        //progressDialog.show()
        showWaiteStatue()
        AliFile.getInstance().uploadFile(path, object : AliFile.UploadResult() {
            override fun fail(error: String?) {
                super.fail(error)
                showNotice(error)
            }

            override fun uploadProgress(currentSize: Long, totalSize: Long) {
                /*runOnUiThread {
                    var progressReal = currentSize * 1f / totalSize
                    if (progressReal == 1f) {
                        progressReal = .99f
                    }
                    progressDialog.setProgress(progressReal)
                }*/
            }

            override fun success(result: String) {
                super.success(result)
                Log.e("TAG", ": result=$result")
                mPresent?.loadAddManageModelTask(PjjApplication.application.userId, "321", result, if (isImg) "1" else "2", "1")
            }
        })
    }

    private fun addPlayTime() {
        val array = arrayOf(5, 10, 15, 30, 60)
        array.forEach { value ->
            ll_play_time.addView(createChildView(value))
            ll_play_time.addView(getSpaceView())
        }
        ll_play_time.addView(createChildView(-1))
    }

    private lateinit var tv_defined: TextView
    private var videoPlayTimes: Int = 15
    @SuppressLint("SetTextI18n")
    private fun createChildView(value: Int): TextView {
        return TextView(this).apply {
            gravity = Gravity.CENTER
            setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_12))
            tag = value
            if (value > 0) {
                text = "${value}秒"
            } else {
                tv_defined = this
                text = "自定义"
            }
            background = if (value != videoPlayTimes) getWhiteBg() else ViewUtils.getDrawable(R.drawable.shape_theme_bg_3)
            setTextColor(if (value != videoPlayTimes) ViewUtils.getColor(R.color.color_333333) else Color.WHITE)
            layoutParams = LinearLayout.LayoutParams(ViewUtils.getDp(R.dimen.dp_48), LinearLayout.LayoutParams.MATCH_PARENT)
            setOnClickListener(onChildClick)
        }
    }

    private fun getSpaceView(): View {
        return View(this).apply {
            layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
        }
    }

    private val onChildClick = View.OnClickListener {
        val timeLength = it.tag as Int
        if (videoPlayTimes == timeLength) {
            return@OnClickListener
        }
        if (timeLength == -1) {
            showTimeDialog()
        } else {
            it.background = ViewUtils.getDrawable(R.drawable.shape_theme_bg_3)
            (it as TextView).setTextColor(Color.WHITE)
            videoPlayTimes = timeLength
        }
        recoverOtherChildStatue(it)
    }

    private fun recoverOtherChildStatue(tvClick: View) {
        (0 until ll_play_time.childCount).forEach {
            val view = ll_play_time.getChildAt(it)
            if (view != tvClick && view is TextView) {
                view.background = getWhiteBg()
                view.setTextColor(ViewUtils.getColor(R.color.color_333333))
            }
        }
    }

    private fun getWhiteBg(): Drawable {
        return ViewUtils.getDrawable(R.drawable.shape_333_side_3)
    }

    private fun showTimeDialog() {
        timeDefinedDialog.show()
    }

    private val timeDefinedDialog: TimeDefinedDialog by lazy {
        TimeDefinedDialog(this).apply {
            onTimeDefinedListener = object : TimeDefinedDialog.OnTimeDefinedListener {
                override fun notice(msg: String) {
                    showNotice(msg)
                }

                @SuppressLint("SetTextI18n")
                override fun sure(num: Int) {
                    tv_defined.text = num.toString() + "秒"
                    tv_defined.background = ViewUtils.getDrawable(R.drawable.shape_theme_bg_3)
                    tv_defined.setTextColor(Color.WHITE)
                    videoPlayTimes = num
                }
            }
        }
    }

    override fun showNotice(error: String?) {
        super.showNotice(error)
        if (null != error && error.contains("更换为屏加加自营模式")) {
            XspManage.getInstance().refreshTag.tagScreenManageActivity = true
            noticeDialog.setOnDismissListener {
                finish()
            }
        }
    }
}
