package com.pjj.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.transition.Transition
import com.dmcbig.mediapicker.PickerActivity
import com.dmcbig.mediapicker.PickerConfig
import com.dmcbig.mediapicker.entity.Media
import com.pjj.PjjApplication

import com.pjj.R
import com.pjj.contract.BirthdayWishesContract
import com.pjj.intent.AliFile
import com.pjj.intent.RetrofitService
import com.pjj.module.DiyDataBean
import com.pjj.module.ResultBean
import com.pjj.module.parameters.Template
import com.pjj.module.parameters.UploadTemplateNew
import com.pjj.present.BirthdayWishesPresent
import com.pjj.utils.BitmapUtils
import com.pjj.utils.SharedUtils
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.custom.DiyTemplateView
import com.pjj.view.custom.MyTextView
import com.pjj.view.dialog.CallPhoneDialog
import com.pjj.view.dialog.TakePhotoDialog
import com.pjj.view.dialog.TemplateNameChangeDialog
import kotlinx.android.synthetic.main.activity_birthdaywishes.*
import java.io.File

/**
 * Create by xinheng on 2019/05/28 17:58。
 * describe：生日祝福
 */
class BirthdayWishesActivity : BaseActivity<BirthdayWishesPresent>(), BirthdayWishesContract.View {
    private var width = 0
    private var height = 0
    private var shape = true
    private var theme: String? = null
    private var template: Template? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_birthdaywishes)
        setTitle("创建模板")
        if (null != savedInstanceState) {
            mTempPhotoPath = savedInstanceState.getString("mTempPhotoPath")
            template = savedInstanceState.getParcelable("template")
            width = try {
                savedInstanceState.getString("width_template").toInt()
            } catch (e: Exception) {
                270
            }
            val type = template?.purposeType
            height = try {
                savedInstanceState.getString("height_template").toInt()
            } catch (e: Exception) {
                if ("9" == type) 450 else 472
            }
            theme = savedInstanceState.getString("theme")
        } else {
            template = intent.getParcelableExtra<Template>("template")
            val type = template?.purposeType
            width = try {
                intent.getStringExtra("width_template").toInt()
            } catch (e: Exception) {
                270
            }
            height = try {
                intent.getStringExtra("height_template").toInt()
            } catch (e: Exception) {
                if ("9" == type) 450 else 472
            }
            theme = intent.getStringExtra("theme")
        }
        /*theme?.let {
            et_name.setText(it)
        }*/
        changeNameDialog.maxLength = when (theme) {
            "生日祝福" -> 10
            "失物认领" -> 39
            "寻物启事" -> 44
            else -> 13
        }
        Log.e("TAG", "初始比例： width=$width ,height=$height")
        val lp = diyTemplateView.layoutParams
        lp.height = (lp.width * 1f * height / width).toInt()
        diyTemplateView.layoutParams = lp
        val id = intent.getStringExtra("adTempletId")
        mPresent = BirthdayWishesPresent(this)
        val path = intent.getStringExtra("bg_path")
        diyTemplateView.setRateWidth(width)
        Glide.with(this@BirthdayWishesActivity).asDrawable().load(PjjApplication.filePath + path).into(object : CustomViewTarget<DiyTemplateView, Drawable>(diyTemplateView) {
            override fun onResourceCleared(placeholder: Drawable?) {
                Log.e("TAG", "onResourceCleared")
            }

            override fun onLoadFailed(errorDrawable: Drawable?) {
                showNotice("加载背景图失败")
            }

            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                view.background = resource
                mPresent?.loadTemplateInfTask(id)
            }
        })
        diyTemplateView.setOnViewClickListener(object : DiyTemplateView.OnViewClickListener {
            override fun onClick(dataBean: DiyDataBean.DataBean, view: View) {
                val max = dataBean.wordNumber
                when (view) {
                    is ImageView -> selectImage(dataBean)
                    //is TextView -> selectText()
                    is MyTextView -> {
                        val text = view.text
                        if (max > 0)
                            changeNameDialog.maxLength = max
                        selectText(text)
                    }

                }
            }
        })
        tv_save.setOnClickListener(onClick)
        et_name.setSelection(et_name.text.length)
    }

    private val titleDialog: CallPhoneDialog by lazy {
        CallPhoneDialog(this).apply {
            rightText = "确定"
            phone = "\n您未输入广告名称\n将默认为：我的广告\n"
            setCancelColor(ViewUtils.getColor(R.color.color_888888))
            onCallPhoneDialogListener = object : CallPhoneDialog.OnCallPhoneDialogListener {
                override fun callClick() {
                    this@BirthdayWishesActivity.et_name.setText("我的广告")
                    saveBitmap()
                }
            }
        }
    }

    //private val module=PjjApplication.App_Path+"module/"
    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_save -> {
                val hasAllSelect = diyTemplateView.hasAllSelect()
                if (null == hasAllSelect) {
                    val title = et_name.text.toString()
                    if (TextUtils.isEmpty(title)) {
                        //titleDialog.show()
                        showNotice("为了更好的广告效果，请输入广告名称")
                        return
                    }
                    saveBitmap()
                } else {
                    //val s = if (hasAllSelect == 1) "您还未上传图片" else "您还未输入相关内容"
                    showNotice(hasAllSelect)
                }
            }
        }
    }

    private fun saveBitmap() {
        showWaiteStatue()
        val bitmap = diyTemplateView.getBitmap()
        val path = PjjApplication.App_Path + "module/" + System.currentTimeMillis() + "-module.jpg"
        BitmapUtils.saveBitmapThread(bitmap, Bitmap.CompressFormat.JPEG, path, 100) {
            runOnUiThread {
                if (it) {
                    uploadMediaForAli(path, true)
                } else {
                    cancelWaiteStatue()
                    showNotice("保存预览图失败，请稍后重试")
                }
            }
        }
    }

    override fun update(bean: DiyDataBean) {
        val data = bean.data
        if (TextUtils.isNotEmptyList(data)) {
            diyTemplateView.addViews(bean)
        } else {
            showNotice("模板信息错误")
        }
    }

    /**
     * 选择文本
     */
    private fun selectText(text: String?) {
        changeNameDialog.show(text)
    }

    /**
     * 选择图片
     */
    private fun selectImage(dataBean: DiyDataBean.DataBean) {
        Log.e("TAg", "startSelectImage")
        shape = dataBean.isCircle != "1"
        width = dataBean.wide.toInt()
        height = dataBean.high.toInt()
        //BitmapUtils.PATH?.let { diyTemplateView.updateClickView(it) }
        takePhotoDialog.show()
    }

    private var mTempPhotoPath: String? = null

    private fun startPhoto() {
        // 跳转到系统的拍照界面
        mTempPhotoPath = PjjApplication.App_Path + "photo/${System.currentTimeMillis()}-photo.png"
        SharedUtils.saveForXml("mTempPhotoPath", mTempPhotoPath)
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

    private val takePhotoDialog: TakePhotoDialog by lazy {
        TakePhotoDialog(this).apply {
            onItemClickListener = object : TakePhotoDialog.OnItemClickListener {
                override fun takePhoto() {
                    //checkPermission()
                    startPhoto()
                    dismiss()
                }

                override fun selectPhoto() {
                    val intent = Intent(this@BirthdayWishesActivity, PickerActivity::class.java)
                    intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE)
                    intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 1)
                    startActivityForResult(intent, 201)
                    dismiss()
                }

                override fun surePhoto(path: String) {
                    //this@BirthdayWishesActivity.diyTemplateView.updateClickView(path)
                }
            }
        }
    }
    private val changeNameDialog: TemplateNameChangeDialog by lazy {
        TemplateNameChangeDialog(this).apply {
            hint = "请输入相关内容"
            //maxLength = 13
            gravity = Gravity.LEFT or Gravity.CENTER_VERTICAL
            onItemClickListener = object : TemplateNameChangeDialog.OnItemClickListener {
                override fun leftClick(name: String) {
                    this@BirthdayWishesActivity.diyTemplateView.updateClickView(name)
                }

                override fun notice(msg: String) {
                    showNotice(msg)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 201 && resultCode == PickerConfig.RESULT_CODE) {
            data?.getParcelableArrayListExtra<Media>(PickerConfig.EXTRA_RESULT)?.let {
                if (it.size > 0) {
                    it[0].path?.let { item ->
                        //takePhotoDialog.setPhotoResult(item)
                        startActivityForResult(Intent(this, CropActivity::class.java)
                                .putExtra(CropActivity.IMAGE_PATH, item)
                                .putExtra(CropActivity.WIDTH, width)
                                .putExtra(CropActivity.HEIGHT, height)
                                .putExtra(CropActivity.SHAPE, shape)
                                .putExtra(CropActivity.IMAGE_SAVE_PATH, PjjApplication.App_Path + "image/"), 202)
                    }
                }
            }
        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            Log.e("TAG", "mTempPhotoPath=$mTempPhotoPath")
            if (null == mTempPhotoPath) {
                mTempPhotoPath = SharedUtils.getXmlForKey("mTempPhotoPath")
            }
            Log.e("TAG", "mTempPhotoPath=$mTempPhotoPath")
            mTempPhotoPath?.let {
                val file = File(it)
                if (file.exists()) {
                    startActivityForResult(Intent(this, CropActivity::class.java)
                            .putExtra(CropActivity.IMAGE_PATH, mTempPhotoPath)
                            .putExtra(CropActivity.WIDTH, width)
                            .putExtra(CropActivity.HEIGHT, height)
                            .putExtra(CropActivity.SHAPE, shape)
                            .putExtra(CropActivity.IMAGE_SAVE_PATH, PjjApplication.App_Path + "image/"), 202)
                }
            }
        } else if (requestCode == 202 && resultCode == CropActivity.RESULT_CODE) {
            data?.getStringExtra(CropActivity.IMAGE_SAVE_PATH)?.let {
                //takePhotoDialog.setPhotoResult(it)
                this@BirthdayWishesActivity.diyTemplateView.updateClickView(it)
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
                this@BirthdayWishesActivity.diyTemplateView.postDelayed({
                    if (noticeDialog.isShowing) {
                        noticeDialog.dismiss()
                    }
                    val cls = if ("1" == template?.comeFromTemplate) SelfUseActivity::class.java else ScreenManageReleaseActivity::class.java
                    startActivity(Intent(this@BirthdayWishesActivity, cls).putExtra("refresh", true))
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
                    this@BirthdayWishesActivity.diyTemplateView.postDelayed({
                        //noticeDialog.dismiss()
                        if (noticeDialog.isShowing) {
                            noticeDialog.dismiss()
                        }
                        startActivity(Intent(this@BirthdayWishesActivity, TemplateListActivity::class.java).putExtra("refresh", true))
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

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.let {
            it.putString("mTempPhotoPath", mTempPhotoPath)
            it.putString("width_template", width.toString())
            it.putString("height_template", height.toString())
            it.putString("theme", theme)
            it.putParcelable("template", template)
        }
        super.onSaveInstanceState(outState)
    }
}
