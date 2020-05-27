package com.pjj.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import com.bumptech.glide.Glide
import com.dmcbig.mediapicker.PickerActivity
import com.dmcbig.mediapicker.PickerConfig
import com.dmcbig.mediapicker.entity.Media
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.intent.AliFile
import com.pjj.intent.RetrofitService
import com.pjj.module.ResultBean
import com.pjj.module.SpeedDataBean
import com.pjj.module.ViewSizeBean
import com.pjj.module.parameters.SpeedTemplateUpload
import com.pjj.module.xspad.XspManage
import com.pjj.present.BasePresent
import com.pjj.utils.BitmapUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.custom.SpeedViewGroup
import com.pjj.view.custom.TextImageView
import com.pjj.view.dialog.MediaSelectDialog
import kotlinx.android.synthetic.main.activity_upload_speed.*
import java.io.File

class UploadSpeedActivity : BaseActivity<BasePresent<*>>() {

    private var view: TextImageView? = null
    private var index = -1
    private var fileType = -1
    private var speedSize = -1
    private var mTempPhotoPath: String? = null
    private var media: Media? = null
    private var viewSizeBeanList = ArrayList<ViewSizeBean>()
    private var templateTag: String? = ""
    private var selectReleaseTag = false

    companion object {
        @JvmStatic
        fun newInstance(activity: Activity, selectReleaseTag: Boolean = false) {
            activity.startActivity(Intent(activity, UploadSpeedActivity::class.java).putExtra("selectReleaseTag", selectReleaseTag))
        }

        @JvmStatic
        fun newInstanceForResult(activity: Activity, requestCode: Int) {
            activity.startActivityForResult(Intent(activity, UploadSpeedActivity::class.java), requestCode)
        }
    }

    private val speedDialog: MediaSelectDialog by lazy {
        MediaSelectDialog(this).apply {
            onMediaSelectDialog = object : MediaSelectDialog.OnMediaSelectListener {
                override fun selectMedia() {
                    if (fileType == 2) {
                        selectVideo()
                    } else {
                        selectPhoto()
                    }
                }

                override fun takePhoto() {
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upload_speed)
        setTitle("拼屏信息模板")
        tv_submit.setOnClickListener(onClick)
        selectReleaseTag = intent.getBooleanExtra("selectReleaseTag", false)
        var speedDataBean: SpeedDataBean?
        try {
            speedDataBean = when {
                selectReleaseTag -> XspManage.getInstance().speedScreenData.templateData!!
                else -> XspManage.getInstance().speedScreenData.originalTemplateData!!.clone()
            }
            speedSize = speedDataBean.size
            templateTag = speedDataBean.IdentificationId
            sg_speed.childClickTag = true
            sg_speed.speedData = speedDataBean
            sg_speed.textSize= ViewUtils.getFDp(R.dimen.sp_8)
            sg_speed.onSpeedViewChildClickListener = object : SpeedViewGroup.OnSpeedViewChildClickListener {
                override fun childClick(view: TextImageView, index: Int, fileType: Int) {
                    this@UploadSpeedActivity.view = view
                    this@UploadSpeedActivity.index = index
                    this@UploadSpeedActivity.fileType = fileType
                    if (selectReleaseTag) {
                        if (view.drawable != null) {
                            view.select = !view.select
                            selectTemplateMedia(speedDataBean.viewSizeBeanList!![index], view.select)
                        }
                    } else {
                        showSpeedDialog()
                    }
                }
            }
            var noHead = "658448e350b646c7a45edca8a1dc6910" == speedDataBean.IdentificationId || "39d6cb23a5764decafd1b09ecdf0780e" == speedDataBean.IdentificationId
            var head = ""
            var textSize = if (null != sg_speed.videoSizeText && null != sg_speed.imageSizeText) {
                "建议尺寸：${sg_speed.videoSizeText}\n${sg_speed.imageSizeText}"
            } else {
                "建议尺寸：" + (if (null != sg_speed.videoSizeText) sg_speed.videoSizeText else sg_speed.imageSizeText)?.let {
                    it.substring(3, it.length)
                }
            }
            if (!noHead) {
                head = if (textSize.contains("大图")) {
                    "小图位置随机\n"
                } else {
                    "图片位置随机\n"
                }
            }
            tv_size.text = head + textSize
        } catch (e: Exception) {
            e.printStackTrace()
            tv_submit.isEnabled = false
            showNotice("数据错误")
        }
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_submit -> {
                if (selectReleaseTag) {
                    if (selectMediaList.size == 0) {
                        showNotice("请选择素材")
                    } else {
                        dealSelectMediaData()
                    }
                } else {
                    if (null == media) {
                        showNotice("请选择媒体")
                    } else {
                        showWaiteStatue()
                        makeTemplatePic()
                    }
                }
            }
            R.id.position -> {

            }
        }
    }

    private fun dealSelectMediaData() {
        var identificationId = sg_speed.speedData!!.IdentificationId
        var buffer = StringBuffer()
        var clone = sg_speed.speedData!!.clone(selectMediaList)
        selectMediaList.forEach {
            //clone.viewSizeBeanList!!.add(it)
            buffer.append(it.file_id)
            buffer.append("&")
            buffer.append(it.randomFlag)
            buffer.append("&")
            buffer.append(it.x)
            buffer.append("&")
            buffer.append(it.y)
            buffer.append("&")
            buffer.append(it.width)
            buffer.append("&")
            buffer.append(it.height)
            buffer.append(",")
        }
        if (!buffer.isEmpty()) {
            buffer.deleteCharAt(buffer.length - 1)
        }
        var file_id = buffer.toString()
        var speedScreenData = XspManage.getInstance().speedScreenData
        speedScreenData.file_id = file_id
        speedScreenData.identificationId = identificationId
        speedScreenData.pieceNum = sg_speed.speedData!!.size
        speedScreenData.clone = clone
        startActivity(Intent(this, SelectRandomReleaseAreaActivity::class.java))
    }

    private var startIndex: Int = 0
    private var templatePrePic: String? = null
    /**
     * 上传媒体信息
     */
    private fun uploadMedia(it: Int) {
        if (it == viewSizeBeanList.size && !templatePrePic!!.contains("/speed")) {
            makeUpload(templateTag.toString(), speedSize)
            return
        }
        var path = when (it < viewSizeBeanList.size) {
            true -> viewSizeBeanList[it].filePath
            else -> templatePrePic
        }
        AliFile.getInstance().uploadFile(path, object : AliFile.UploadResult() {
            override fun success(result: String) {
                super.success(result)
                if (it == viewSizeBeanList.size) {
                    templatePrePic = result
                    makeUpload(templateTag.toString(), speedSize)
                } else {
                    viewSizeBeanList[it].filePath = result
                    uploadMedia(it + 1)
                }
            }

            override fun fail(error: String?) {
                super.fail(error)
                startIndex = it
                cancelWaiteStatue()
                showNotice("第${it + 1}张图片上传失败，请稍后重试")
            }
        })
    }

    private fun makeTemplatePic() {
        if (null != templatePrePic) {
            uploadMedia(startIndex)
            return
        }
        var bitmap = Bitmap.createBitmap(sg_speed.measuredWidth, sg_speed.measuredHeight, Bitmap.Config.RGB_565)
        val canvas = Canvas(bitmap)
        sg_speed.draw(canvas)
        var path = PjjApplication.App_Path + "speed/" + System.currentTimeMillis() + "-speed.jpg"
        BitmapUtils.saveBitmapThread(bitmap, Bitmap.CompressFormat.JPEG, path, 30) {
            runOnUiThread {
                if (it) {
                    templatePrePic = path
                    uploadMedia(startIndex)
                } else {
                    cancelWaiteStatue()
                    showNotice("保存预览图失败，请稍后重试")
                }
            }
        }
    }

    private fun makeUpload(iden: String, size: Int) {
        var speedTemplateUpload = SpeedTemplateUpload().apply {
            fileRelName = "拼屏$size-$iden"
            userId = PjjApplication.application.userId
            identityType = "3"
            purposeType = "5"
            spellType = speedSize.toString()
            identificationId = iden
            //templetFormat = "1"
            //TODO 预览图添加 templatePrePic
            fileUpload = makeUploadParameter()
        }
        RetrofitService.getInstance().uploadSplicingTemplet(speedTemplateUpload, object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                cancelWaiteStatue()
                showToast("上传成功")
                setResult(RESULT_OK)
                finish()
            }

            override fun fail(error: String?) {
                super.fail(error)
                cancelWaiteStatue()
                showNotice("上传模板失败，请稍后重试")
            }
        })
    }

    private fun makeUploadParameter(): String {
        var s = ""
        viewSizeBeanList.forEachIndexed { index, it ->
            if (index != 0) {
                s += ","
            }
            s += makeUploadParameterFileUpload(it)
        }
        s += ",$templatePrePic&1&1&&&0&0&1"
        return s
    }

    /**
     * 构造参数信息
     */
    private fun makeUploadParameterFileUpload(it: ViewSizeBean): String {
        var buffer = StringBuffer()
        buffer.append(it.filePath)
        buffer.append("&")
        buffer.append(it.type)
        buffer.append("&")
        buffer.append("1")
        buffer.append("&")
        buffer.append(it.x)
        buffer.append("&")
        buffer.append(it.y)
        buffer.append("&")
        buffer.append(it.width)
        buffer.append("&")
        buffer.append(it.height)
        buffer.append("&")
        buffer.append(0)
        //TODO 添加大图标识
        return buffer.toString()
    }

    private fun getReal(tag: String?, value: Int): String {
        return if ("1" == tag) value.toString() else ""
    }

    private fun showSpeedDialog() {
        speedDialog.show()
    }

    private fun startPhoto() {
        // 跳转到系统的拍照界面
        mTempPhotoPath = PjjApplication.App_Path + "photo/${System.currentTimeMillis()}-photo.png"
        //系统相机
        var intentCamera = Intent()
        var imageUri = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                FileProvider.getUriForFile(this, "com.pjj.fileprovider", File(mTempPhotoPath))
            }
            else -> Uri.fromFile(File(mTempPhotoPath))
        }
        intentCamera.action = MediaStore.ACTION_IMAGE_CAPTURE
        //将拍照结果保存至photo_file的Uri中，不保留在相册中
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intentCamera, 200)
    }

    private fun selectPhoto() {
        val intent = Intent(this, PickerActivity::class.java)
        intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE)
        intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 1)
        //intent.putExtra(PickerConfig.MAX_SELECT_SIZE, 20)
        startActivityForResult(intent, 201)
    }

    private fun selectVideo() {
        val intent = Intent(this, PickerActivity::class.java)
        intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_VIDEO)
        intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 1)
        startActivityForResult(intent, 202)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 203 && resultCode == CropActivity.RESULT_CODE) {
            data?.getStringExtra(CropActivity.IMAGE_SAVE_PATH)?.let {
                view!!.fileType = 1
                Glide.with(this@UploadSpeedActivity).load(it).into(view!!)
                var viewSizeBean = sg_speed.speedData!!.viewSizeBeanList!![index]
                viewSizeBean.filePath = it
                if (!viewSizeBeanList.contains(viewSizeBean))
                    viewSizeBeanList.add(viewSizeBean)
            }
        } else if ((requestCode == 201 || requestCode == 202) && resultCode == PickerConfig.RESULT_CODE) {
            var select = data?.getParcelableArrayListExtra<Media>(PickerConfig.EXTRA_RESULT)
            select?.run {
                if (size > 0) {
                    templatePrePic = null
                    media = this[0]
                    media?.let {
                        var path = it.path
                        if (it.mediaType != 3) {//图片
                            //Glide.with(this@UploadSpeedActivity).load(path).into(view!!)
                            CropActivity.newInstanceShape(this@UploadSpeedActivity, path, PjjApplication.App_Path + "image/", 203,
                                    view!!.measuredWidth, view!!.measuredHeight)
                            return
                        } else {
                            if (File(path).length() > 20 * 1204 * 1024) {
                                media = null
                                //view!!.setImageDrawable(null)
                                showNotice("视频大于20M，请重新选择")
                                return
                            }
                            view!!.fileType = 2
                            BitmapUtils.loadFirstImageForVideo(Glide.with(this@UploadSpeedActivity), path, view!!)
                        }
                        var viewSizeBean = sg_speed.speedData!!.viewSizeBeanList!![index]
                        viewSizeBean.filePath = path
                        if (!viewSizeBeanList.contains(viewSizeBean))
                            viewSizeBeanList.add(viewSizeBean)
                    }
                }
            }
        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            var file = File(mTempPhotoPath)
            if (file.exists()) {
                templatePrePic = null
                media = Media()
                media?.run {
                    path = mTempPhotoPath
                    mediaType = 1
                }
                view!!.fileType = 1
                Glide.with(this).load(mTempPhotoPath).into(view!!)
                var viewSizeBean = sg_speed.speedData!!.viewSizeBeanList!![index]
                viewSizeBean.filePath = mTempPhotoPath
                if (!viewSizeBeanList.contains(viewSizeBean))
                    viewSizeBeanList.add(viewSizeBean)
            }
        }
    }

    private val selectMediaList: ArrayList<ViewSizeBean> by lazy {
        ArrayList<ViewSizeBean>()
    }

    private fun selectTemplateMedia(bean: ViewSizeBean, add: Boolean = false) {
        if (add) {
            selectMediaList.add(bean)
        } else {
            selectMediaList.remove(bean)
        }
    }

    override fun onDestroy() {
        if (speedDialog.isShowing) {
            speedDialog.dismiss()
        }
        super.onDestroy()
    }
}
