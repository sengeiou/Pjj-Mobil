package com.pjj.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
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
import kotlinx.android.synthetic.main.activity_create_diy.*
import java.io.File

class CreateDIYActivity : BaseActivity<BasePresent<*>>() {
    private var template: Template? = null
    private var media: Media? = null
    private var handlerMain = Handler()
    //120 180 300 1920
    private var width = 1080
    private var height = 1620
    private var image_text: String? = null
    private lateinit var drawableBg: Drawable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_diy)
        setTitle("创建模板")
        template = intent.getParcelableExtra("template")
        TextUtils.banFaceForEditText(et_name, 10)
        tv_save.setOnClickListener(onClick)
        iv.setOnClickListener(onClick)
        ll_init.setOnClickListener(onClick)
        image_text = intent.getStringExtra("image_text")
        val explain_text = intent.getStringExtra("explain_text")
        val text_iv = image_text ?: "图片 / 视频"
        iv.text = text_iv
        //tv_explain.text = explain_text ?: ""
        template?.let {
            height = when (it.purposeType) {
                "7" -> 1800
                "1", "9" -> 1800
                else -> 1620
            }
        }
        var params = iv.layoutParams
        params.height = params.width * height / width
        iv.layoutParams = params
        drawableBg = TowMediaView.getDrawableBg(R.mipmap.tem_photo, text_iv, ViewUtils.getColor(R.color.color_fffdc1), ViewUtils.getColor(R.color.color_ffebc1))
        iv.setImageDrawable(drawableBg)
        et_name.setSelection(et_name.text.toString().length)
    }


    private val selectMediaDialog: SelectMediaDialog by lazy {
        SelectMediaDialog(this).apply {
            onItemClickListener = object : SelectMediaDialog.OnItemClickListener {
                override fun takePhoto() {
                    startPhoto()
                }

                override fun selectPhoto() {
                    val intent = Intent(this@CreateDIYActivity, PickerActivity::class.java)
                    intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE)
                    intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 1)
                    //intent.putExtra(PickerConfig.MAX_SELECT_SIZE, 20)
                    startActivityForResult(intent, 201)
                }

                override fun selectVideo() {
                    val intent = Intent(this@CreateDIYActivity, PickerActivity::class.java)
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
                    this@CreateDIYActivity.et_name.setText("我的广告")
                    uploadMediaForAli(media!!.path, media!!.mediaType != 3)
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
            R.id.ll_init, R.id.iv -> {
                selectMediaDialog.show()
            }
            R.id.tv_save -> {
                if (null == media) {
                    showNotice("您需要先上传图片\n或视频")
                    return
                }
                if (et_name.text.toString().isNotEmpty()) {
                    uploadMediaForAli(media!!.path, media!!.mediaType != 3)
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
                    media = this[0]
                    media?.let {
                        var path = it.path
                        //iv.background = ColorDrawable(ViewUtils.getColor(R.color.color_666666))
                        if (it.mediaType != 3) {//图片
                            CropActivity.newInstanceShape(this@CreateDIYActivity, path, PjjApplication.App_Path + "image/", 203,
                                    width, height)
                        } else {
                            if (File(path).length() > 20 * 1204 * 1024) {
                                media = null
                                iv_video.visibility = View.GONE
                                iv.setImageDrawable(drawableBg)
                                //Glide.with(this@CreateDIYActivity).load(R.mipmap.create_tem111).into(iv)
                                showNotice("视频大于20M，请重新选择")
                                this@CreateDIYActivity.ll_init.visibility = View.VISIBLE
                                this@CreateDIYActivity.iv.visibility = View.GONE
                                return
                            }
                            //iv_video.visibility = View.VISIBLE
                            this@CreateDIYActivity.ll_init.visibility = View.GONE
                            this@CreateDIYActivity.iv.visibility = View.VISIBLE
                            BitmapUtils.loadFirstImageForVideo(Glide.with(this@CreateDIYActivity), path, iv)
                        }
                    }
                }
            }
        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            mTempPhotoPath?.let {
                var file = File(it)
                if (file.exists()) {
                    media = Media()
                    media?.run {
                        path = it
                        mediaType = 1
                    }
                    //iv_video.visibility = View.GONE
                    //Glide.with(this@CreateDIYActivity).load(mTempPhotoPath).into(iv)
                    CropActivity.newInstanceShape(this@CreateDIYActivity, it, PjjApplication.App_Path + "image/", 203,
                            width, height)
                }
            }

        } else if (requestCode == 203 && resultCode == CropActivity.RESULT_CODE) {
            data?.getStringExtra(CropActivity.IMAGE_SAVE_PATH)?.let {
                iv_video.visibility = View.GONE
                this@CreateDIYActivity.ll_init.visibility = View.GONE
                this@CreateDIYActivity.iv.visibility = View.VISIBLE
                Glide.with(this@CreateDIYActivity).load(it).into(iv)
                media?.run {
                    path = it
                    mediaType = 1
                }
            }
        }
    }

    /* private val progressDialog: LineProgressDialog by lazy {
         LineProgressDialog(this)
     }*/

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
                //showWaiteStatue()
                newUploadTemplate(result, isImg)
            }
        })
    }

    private fun selfUseTemplateUpload(path: String, isImg: Boolean) {
        RetrofitService.getInstance().inserOwnFile(PjjApplication.application.userId, et_name.text.toString(), path, if (isImg) "1" else "2", "1", object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean?) {
                cancelWaiteStatue()
                smillTag = true
                showNotice("上传成功")
                handlerMain.postDelayed({
                    //noticeDialog.dismiss()
                    if (noticeDialog.isShowing) {
                        noticeDialog.dismiss()
                    }
                    //setResult(303)
                    val cls = if ("1" == template?.comeFromTemplate) SelfUseActivity::class.java else ScreenManageReleaseActivity::class.java
                    startActivity(Intent(this@CreateDIYActivity, cls).putExtra("refresh", true))
                    finish()
                }, 2100)
            }
        })
    }

    private fun newUploadTemplate(path: String, isImg: Boolean) {
        if ("1" == template?.selfUseTag) {
            selfUseTemplateUpload(path, isImg)
            return
        }
        template?.run {
            var uploadTemplateFile = UploadTemplateNew().apply {
                when (isImg) {
                    true -> {
                        templetType = "1"
                        fileUpload = "$path&1&1"
                    }
                    else -> {
                        templetType = "2"
                        fileUpload = "$path&2&1"
                    }
                }
                fileRelName = et_name.text.toString()
                identityType = this@run.identityType
                purposeType = this@run.purposeType
                userId = PjjApplication.application.userId
            }
            RetrofitService.getInstance().loadNewUploadTemplateTask(uploadTemplateFile, object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
                override fun successResult(t: ResultBean) {
                    //progressDialog.setProgress(1f)
                    cancelWaiteStatue()
                    smillTag = true
                    showNotice("上传成功")
                    handlerMain.postDelayed({
                        //noticeDialog.dismiss()
                        if (noticeDialog.isShowing) {
                            noticeDialog.dismiss()
                        }
                        //setResult(303)
                        startActivity(Intent(this@CreateDIYActivity, TemplateListActivity::class.java).putExtra("refresh", true))
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

    private fun uploadTemplate(path: String, isImg: Boolean) {
        template?.run {
            showWaiteStatue()
            var uploadTemplateFile = UploadTemplate().apply {
                filePath = path
                when (isImg) {
                    true -> {
                        templetType = "1"
                        fileName = "1&1."
                    }
                    else -> {
                        templetType = "2"
                        fileName = "2&1."
                    }
                }
                identityType = this@run.identityType
                purposeType = this@run.purposeType
                isImage = isImg
                fileRelName = ""
                userId = PjjApplication.application.userId
            }
            var file = File(uploadTemplateFile.filePath)
            if (!file.exists()) {
                return
            } else {
                var name = uploadTemplateFile.filePath
                uploadTemplateFile.fileRelName = et_name.text.toString()
                val prefix = name.substring(name.lastIndexOf(".") + 1)
                uploadTemplateFile.fileName = uploadTemplateFile.fileName + prefix
                RetrofitService.getInstance().uploadTemplateFile(uploadTemplateFile, object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
                    override fun successResult(t: ResultBean?) {
                        cancelWaiteStatue()
                        var makeText = Toast.makeText(PjjApplication.application, "上传成功", Toast.LENGTH_SHORT)
                        makeText.setGravity(Gravity.CENTER, 0, 0)
                        makeText.show()
                        startActivity(Intent(this@CreateDIYActivity, TemplateListActivity::class.java).putExtra("refresh", true))
                        //finish()
                    }

                    override fun fail(error: String?) {
                        super.fail(error)
                        cancelWaiteStatue()
                        showNotice(error)
                    }
                })
            }
        }

    }
}
