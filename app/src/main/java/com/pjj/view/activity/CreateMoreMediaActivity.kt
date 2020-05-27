package com.pjj.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.Toast
import com.bumptech.glide.Glide
import com.dmcbig.mediapicker.PickerActivity
import com.dmcbig.mediapicker.PickerConfig
import com.dmcbig.mediapicker.entity.Media
import com.pjj.R
import com.pjj.PjjApplication
import com.pjj.intent.AliFile
import com.pjj.intent.RetrofitService
import com.pjj.module.ResultBean
import com.pjj.module.parameters.Template
import com.pjj.module.parameters.UploadTemplate
import com.pjj.module.parameters.UploadTemplateNew
import com.pjj.present.BasePresent
import com.pjj.utils.BitmapUtils
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.custom.TowMediaView
import com.pjj.view.dialog.CallPhoneDialog
import com.pjj.view.dialog.SelectMediaDialog
import kotlinx.android.synthetic.main.activity_create_more_media.*
import java.io.File

class CreateMoreMediaActivity : BaseActivity<BasePresent<*>>() {
    private var template: Template? = null
    private var media: Media? = null
    private var mediaVideo: Media? = null
    private var handlerMain = Handler()
    //120 180 300 1920
    private var width = 1080
    private var height = 1886
    private var image_text: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_more_media)
        setTitle("创建模板")
        template = intent.getParcelableExtra("template")
        template?.let {
            height = when (it.purposeType) {
                "1", "9" -> 1800
                else -> 1800
            }
        }
        TextUtils.banFaceForEditText(et_name, 10)
        tv_save.setOnClickListener(onClick)
        iv.setOnClickListener(onClick)
        image_text = intent.getStringExtra("image_text")
        val explain_text = intent.getStringExtra("explain_text")
        //tv_explain.text = explain_text ?: ""
        var params = iv.layoutParams
        params.height = params.width * height / width
        iv.layoutParams = params
        iv.onTowMediaViewListener = object : TowMediaView.OnTowMediaViewListener {
            override fun firstClick() {
                selectMediaDialog.setType(2)
            }

            override fun secondClick() {
                selectMediaDialog.setType(1)
            }
        }
        et_name.setSelection(et_name.text.toString().length)
        line_dotted.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
    }

    private val selectMediaDialog: SelectMediaDialog by lazy {
        SelectMediaDialog(this).apply {
            onItemClickListener = object : SelectMediaDialog.OnItemClickListener {
                override fun takePhoto() {
                    startPhoto()
                }

                override fun selectPhoto() {
                    val intent = Intent(this@CreateMoreMediaActivity, PickerActivity::class.java)
                    intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE)
                    intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 1)
                    //intent.putExtra(PickerConfig.MAX_SELECT_SIZE, 20)
                    startActivityForResult(intent, 201)
                }

                override fun selectVideo() {
                    val intent = Intent(this@CreateMoreMediaActivity, PickerActivity::class.java)
                    intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_VIDEO)
                    intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 1)
                    startActivityForResult(intent, 202)
                }
            }
        }
    }

    private var mTempPhotoPath: String? = null
    private val titleDialog: CallPhoneDialog by lazy {
        CallPhoneDialog(this).apply {
            rightText = "确定"
            phone = "\n您未输入广告名称\n将默认为：我的广告\n"
            setCancelColor(ViewUtils.getColor(R.color.color_888888))
            onCallPhoneDialogListener = object : CallPhoneDialog.OnCallPhoneDialogListener {
                override fun callClick() {
                    this@CreateMoreMediaActivity.et_name.setText("我的广告")
                    uploadMediaForAli()
                }
            }
        }
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

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_save -> {
                if (null == media || mediaVideo == null) {
                    showNotice("您需要先上传图片或视频")
                    return
                }
                if (et_name.text.toString().isNotEmpty()) {
                    uploadMediaForAli()
                } else {
                    showNotice("为了更好的广告效果，请输入广告名称")
                    //titleDialog.show()
                    return
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if ((requestCode == 201 || requestCode == 202) && resultCode == PickerConfig.RESULT_CODE) {
            var select = data?.getParcelableArrayListExtra<Media>(PickerConfig.EXTRA_RESULT)
            select?.run {
                if (size > 0) {
                    var media = this[0]
                    media?.let {
                        var path = it.path
                        if (it.mediaType != 3) {//图片
                            //iv_video.visibility = View.GONE
                            //Glide.with(this@CreateDIYActivity).load(path).into(iv)
                            this@CreateMoreMediaActivity.media = it
                            CropActivity.newInstanceShape(this@CreateMoreMediaActivity, path, PjjApplication.App_Path + "image/", 203,
                                    width, height / 2)
                        } else {
                            if (File(path).length() > 20 * 1204 * 1024) {
                                mediaVideo = null
                                iv.getFirstView().setImageDrawable(null)
                                showNotice("视频大于20M，请重新选择")
                                return
                            }
                            mediaVideo = it
                            iv.getFirstView().textHidden = false
                            BitmapUtils.loadFirstImageForVideo(Glide.with(this@CreateMoreMediaActivity), path, iv.getFirstView())
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
                //iv_video.visibility = View.GONE
                //Glide.with(this@CreateDIYActivity).load(mTempPhotoPath).into(iv)
                CropActivity.newInstanceShape(this@CreateMoreMediaActivity, mTempPhotoPath!!, PjjApplication.App_Path + "image/", 203,
                        width, height / 2)
            }
        } else if (requestCode == 203 && resultCode == CropActivity.RESULT_CODE) {
            data?.getStringExtra(CropActivity.IMAGE_SAVE_PATH)?.let {
                Glide.with(this@CreateMoreMediaActivity).load(it).into(iv.getSecond())
                media?.run {
                    path = it
                    mediaType = 1
                }
            }
        }
    }

    /**
     * 上传文件
     * 阿里云
     */
    private fun uploadMediaForAli() {
        //progressDialog.show()
        showWaiteStatue()
        var array = arrayOf(media!!.path, mediaVideo!!.path)
        AliFile.getInstance().uploadFile(object : AliFile.UploadResult() {
            override fun fail(error: String?) {
                super.fail(error)
                showNotice(error)
            }

            override fun successMap(map: MutableMap<String, String>?) {
                super.successMap(map)
                newUploadTemplate(map!![media!!.path]!!, map!![mediaVideo!!.path]!!)
            }
        }, array)
    }

    private fun newUploadTemplate(imgName: String, videoName: String) {
        template?.run {
            var uploadTemplateFile = UploadTemplateNew().apply {
                templetType = "3"
                fileUpload = "$videoName&2&1,$imgName&1&1"
                fileRelName = et_name.text.toString()
                identityType = this@run.identityType
                purposeType = this@run.purposeType
                userId = PjjApplication.application.userId
            }
            RetrofitService.getInstance().loadNewUploadTemplateTask(uploadTemplateFile, object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
                override fun successResult(t: ResultBean) {
                    cancelWaiteStatue()
                    smillTag = true
                    showNotice("上传成功")
                    handlerMain.postDelayed({
                        //noticeDialog.dismiss()
                        if (noticeDialog.isShowing) {
                            noticeDialog.dismiss()
                        }
                        startActivity(Intent(this@CreateMoreMediaActivity, TemplateListActivity::class.java).putExtra("refresh", true))
                        finish()
                    }, 2100)
                }

                override fun fail(error: String?) {
                    super.fail(error)
                    cancelWaiteStatue()
                    //progressDialog.dismiss()
                    showNotice(error)
                }
            })
        }
    }
}
