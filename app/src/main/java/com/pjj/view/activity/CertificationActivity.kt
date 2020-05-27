package com.pjj.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.*
import com.dmcbig.mediapicker.PickerActivity
import com.dmcbig.mediapicker.PickerConfig
import com.dmcbig.mediapicker.entity.Media

import com.pjj.R
import com.pjj.PjjApplication
import com.pjj.contract.CertificationContract
import com.pjj.module.CertificationResultBean
import com.pjj.module.parameters.IdentityInf
import com.pjj.present.CertificationPresent
import com.pjj.utils.TextUtils
import com.pjj.utils.UserUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.help.CertificationMerchantsHelp
import com.pjj.view.help.CertificationParentHelp
import com.pjj.view.help.CertificationPersonHelp
import kotlinx.android.synthetic.main.activity_certification1.*
import kotlinx.android.synthetic.main.layout_ren_zheng_notice.*
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.support.v4.content.FileProvider
import com.pjj.view.dialog.AddTemplateDialog
import com.pjj.view.dialog.TakePhotoDialog
import java.io.File


/**
 * Create by xinheng on 2018/12/10 18:31。
 * describe：身份认证
 */
class CertificationActivity : BaseActivity<CertificationPresent>(), CertificationContract.View {

    private lateinit var identityInf: IdentityInf
    private val merchantsText = "您的商家身份信息已经通过系统审核，您可以以商家名义发布商业广告，包含（但不限于）招商合作，商品交易，店面推广等内容。商家用户不可发布个人信息，如寻租/招租信息，失物招领，寻物启事，亲友祝福，模板展示，个人宣传等；如有需要，请提交认证信息开通个人用户权限。"
    private val personText = "您的个人身份信息已经通过系统审核，您可以以个人名义发布信息，包含但不限于寻租/招租信息，失物招领，寻物启事，亲友祝福，模板展示，个人宣传等。个人用户不可发布带有商业宣传性质的信息，如有需要，请提交认证信息开通商家用户权限。"
    //private val allText = "您的个人/商家用户身份认证已通过审核，您可以发布个人/商家类型的所有消息。请妥善保管好您的账户和密码，合法合规地使用本平台发布信息。"
    private var inflate: View? = null
    private lateinit var merchantsHelp: CertificationMerchantsHelp
    private lateinit var personHelp: CertificationPersonHelp
    private var personData: CertificationResultBean.UserAuthDetailsBean? = null
    private var merchantsData: CertificationResultBean.UserBusinessAuthDetailsBean? = null
    private var reToShenHeP = false
    private var reToShenHeM = false
    /**
     * 可编辑状态
     */
    private var editPersonTag = false
    private var editMerchantTag = false

    private var personStatue: String? = "0"
    private var merchantsStatue: String? = "0"
    private var defaultShow = false
    private var comeFrom = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_certification1)
        setTitle("实名认证")
        noticeDialog.updateImage(R.mipmap.cry_white)

        personStatue = intent.getStringExtra("personStatue")
        merchantsStatue = intent.getStringExtra("merchantsStatue")

        updateCertification(tv_geren_statue, personStatue)
        updateCertification(tv_shangjia_statue, merchantsStatue)

        mPresent = CertificationPresent(this)
        identityInf = IdentityInf().apply {
            //默认
            userId = PjjApplication.application.userId
        }
        tv_submit.setOnClickListener(onClick)
        ll_geren.setOnClickListener(onClick)
        ll_shangjia.setOnClickListener(onClick)
        personHelp = CertificationPersonHelp(this, fl_parent, identityInf).apply {
            onCertificationHelpListenr = object : CertificationParentHelp.OnCertificationHelpListenr {
                override fun startSelectImage() {
                    uploadImage()
                }

                override fun hiddenInputMethod() {
                    this@CertificationActivity.hiddenInputMethod()
                }
            }
        }
        merchantsHelp = CertificationMerchantsHelp(this, fl_parent, identityInf).apply {
            onCertificationHelpListenr = object : CertificationMerchantsHelp.OnMerchantsListener {
                override fun startSelectProfessionType() {
                    startActivityForResult(Intent(this@CertificationActivity, IndustryActivity::class.java), 300)
                }

                override fun startSelectImage() {
                    uploadImage()
                }

                override fun hiddenInputMethod() {
                    this@CertificationActivity.hiddenInputMethod()
                }
            }
        }

        var defaultType = intent.getStringExtra("default")
        identityInf.authType = defaultType?.let {
            defaultShow = true
            it
        } ?: "1"
        comeFrom = savedInstanceState?.getBoolean("comeFrom")
                ?: intent.getBooleanExtra("comeFrom", false)
        val tag = when {
            comeFrom && merchantsStatue != "3" && personStatue != "3" -> true
            merchantsStatue != "3" -> true
            else -> false
        }
        if (tag) {
            inflate = include_ren_zheng.inflate()
            scroll_view.visibility = View.GONE
            tv_shen_fen_ren_zheng.setOnClickListener(onClick)
            personHelp.setExplain(null)
            merchantsHelp.setExplain(null)
            editPersonTag = true
            editMerchantTag = true
        } else {
            if (personStatue == "3") {
                personHelp.setExplain(personText)
            } else {
                personHelp.setExplain(null)
            }
            if (merchantsStatue == "3") {
                merchantsHelp.setExplain(merchantsText)
            } else {
                merchantsHelp.setExplain(null)
            }
            mPresent?.loadCertificationFailTask(PjjApplication.application.userId)
        }
        if (identityInf.authType == "1") {
            ll_geren.performClick()
        } else {
            ll_shangjia.performClick()
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putBoolean("comeFrom", comeFrom)
        super.onSaveInstanceState(outState)
    }

    /**
     * 认证状态
     */
    private fun updateCertification(tv: TextView, statue: String?) {
        var color: Int = -1
        var text: String? = null
        when (statue) {//0未认证  1审核中  2认证失败  3认证成功
            "1" -> {
                color = ViewUtils.getColor(R.color.color_ea4a4a)
                text = "审核中"
            }
            "3" -> {
                color = ViewUtils.getColor(R.color.color_82e16f)
                text = "已认证"
            }
        }
        setCertificationResult(tv, text, color)
    }

    private val finishDialog: AddTemplateDialog by lazy {
        AddTemplateDialog(this).apply {
            var dp55 = ViewUtils.getDp(R.dimen.dp_55)
            setImageResource(R.mipmap.edit, false, dp55, dp55)
            notice = "是否放弃本次编辑？"
            leftText = "确定"
            onItemClickListener = object : AddTemplateDialog.OnItemClickListener {
                override fun leftClick() {
                    finish()
                }
            }
        }
    }

    override fun titleLeftClick() {
        if (personHelp.visibility == View.VISIBLE) {
            if (!checkPersonIsEmpty()) {
                finishDialog.show()
                return
            }
        } else {
            if (!checkMerchantIsEmpty()) {
                finishDialog.show()
                return
            }
        }
        super.titleLeftClick()
    }

    override fun onBackPressed() {
        if (personHelp.visibility == View.VISIBLE) {
            if (!checkPersonIsEmpty()) {
                finishDialog.show()
                return
            }
        } else {
            if (!checkMerchantIsEmpty()) {
                finishDialog.show()
                return
            }
        }
        super.onBackPressed()
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_shen_fen_ren_zheng -> {
                scroll_view.visibility = View.VISIBLE
                inflate?.visibility = View.GONE
            }
            R.id.ll_geren -> {
                geren()
                view_geren.background = ColorDrawable(ViewUtils.getColor(R.color.color_theme))
                view_shangjia.background = ColorDrawable(Color.TRANSPARENT)

            }
            R.id.ll_shangjia -> {
                shangjia()
                view_geren.background = ColorDrawable(Color.TRANSPARENT)
                view_shangjia.background = ColorDrawable(ViewUtils.getColor(R.color.color_theme))
            }
            R.id.tv_submit -> {
                if (personHelp.visibility == View.VISIBLE) {//个人
                    when (personStatue) {
                        "1" -> {
                            if (reToShenHeP && tv_submit.text.toString() == "确认提交") {
                                submit()
                            } else {
                                editPersonTag = true
                                reToShenHeP = true
                                changeNoticeText()
                                changeSubmitButton("确认提交")
                                personHelp.clearContent()
                                //scroll_view.fullScroll(ScrollView.FOCUS_UP)
                            }
                        }
                        else -> submit()
                    }
                } else {//商家
                    when (merchantsStatue) {
                        "1" -> {
                            if (reToShenHeM && tv_submit.text.toString() == "确认提交") {
                                submit()
                            } else {
                                editMerchantTag = true
                                reToShenHeM = true
                                changeNoticeText()
                                changeSubmitButton("确认提交")
                                merchantsHelp.clearContent()
                                //scroll_view.fullScroll(ScrollView.FOCUS_UP)
                            }
                        }
                        else -> submit()
                    }
                }
            }
        }

    }

    private fun checkPersonIsEmpty(): Boolean {
        if (editPersonTag) {
            personHelp.fillInf()
            return TextUtils.isEmpty(identityInf.name) && TextUtils.isEmpty(identityInf.idcart)
                    && TextUtils.isEmpty(identityInf.idFace1) && TextUtils.isEmpty(identityInf.idBack1) && TextUtils.isEmpty(identityInf.idHold1)
        }
        return true
    }

    private fun checkMerchantIsEmpty(): Boolean {
        if (editMerchantTag) {
            merchantsHelp.fillInf()
            var b = TextUtils.isEmpty(identityInf.companyName) && TextUtils.isEmpty(identityInf.companyNo) && TextUtils.isEmpty(identityInf.professionType)
                    && TextUtils.isEmpty(identityInf.name) && TextUtils.isEmpty(identityInf.idcart)
                    && TextUtils.isEmpty(identityInf.idFace1) && TextUtils.isEmpty(identityInf.idBack1)
                    && TextUtils.isEmpty(identityInf.businessLicence1) && TextUtils.isEmpty(identityInf.businessLicenceHold1)
            var bb: Boolean = if (identityInf.authType == "3") {
                (TextUtils.isEmpty(identityInf.proxName) && TextUtils.isEmpty(identityInf.proxidcart)
                        && TextUtils.isEmpty(identityInf.proxidFaceFile1) && TextUtils.isEmpty(identityInf.proxidBackFile1) && TextUtils.isEmpty(identityInf.proxidHoldFile1))
            } else {
                TextUtils.isEmpty(identityInf.idHold1)
            }
            return b && bb
        }
        return true
    }

    /**
     * 提交
     */
    private fun submit() {
        if (personHelp.visibility == View.VISIBLE) {//校验
            identityInf.authType = "1"
        }
        var auth_type = identityInf.authType
        if (TextUtils.isEmpty(auth_type)) {
            showNotice("请选择认证类型")
            return
        }
        var name: String
        if (auth_type == "1") {
            personHelp.fillInf()
            name = identityInf.name

            if (TextUtils.isEmpty(name)) {
                showNotice(when (auth_type) {
                    "1" -> "请填写姓名"
                    else -> "请填写法人姓名"
                })
                return
            }
            var idCardNo = identityInf.idcart
            if (TextUtils.isEmpty(idCardNo)) {
                showNotice(when (auth_type) {
                    "1" -> "请填写身份证号"
                    else -> "请填写法人身份证号"
                })
                return
            } else if (!UserUtils.verificaID(idCardNo)) {
                showNotice(when (auth_type) {
                    "1" -> "请填写正确的身份证号"
                    else -> "请填写正确的法人身份证号"
                })
                return
            }
            if (TextUtils.isEmpty(identityInf.idFace1)) {
                showNotice("请选择身份证正面照")
                return
            }
            if (TextUtils.isEmpty(identityInf.idBack1)) {
                showNotice("请选择身份证反面照")
                return
            }
            /*if (TextUtils.isEmpty(identityInf.idHold1)) {
                showNotice("请选择手持身份证照片")
                return
            }*/
        } else {
            merchantsHelp.fillInf()

            name = identityInf.name
            if (TextUtils.isEmpty(identityInf.companyName)) {
                showNotice("请填写公司名称")
                return
            }
            if (TextUtils.isEmpty(identityInf.companyNo)) {
                showNotice("请填写工商注册号")
                return
            }
            /*if (identityInf.companyNo.length < 18) {
                showNotice("请填写正确的工商注册号")
                return
            }*/
            if (TextUtils.isEmpty(identityInf.professionType)) {
                showNotice("请选择行业类型")
                return
            }
            if (TextUtils.isEmpty(name)) {
                showNotice(when (auth_type) {
                    "1" -> "请填写姓名"
                    else -> "请填写法人姓名"
                })
                return
            }
            var idCardNo = identityInf.idcart
            if (TextUtils.isEmpty(idCardNo)) {
                showNotice(when (auth_type) {
                    "1" -> "请填写身份证号"
                    else -> "请填写法人身份证号"
                })
                return
            } else if (!UserUtils.verificaID(idCardNo)) {
                showNotice(when (auth_type) {
                    "1" -> "请填写正确的身份证号"
                    else -> "请填写正确的法人身份证号"
                })
                return
            }
            if (auth_type == "2") {
                identityInf.proxidcart = null
                identityInf.proxName = null
                identityInf.proxidHoldFile = null
            } else {
                if (TextUtils.isEmpty(identityInf.proxName)) {
                    showNotice("请填写代理人姓名")
                    return
                }
                if (TextUtils.isEmpty(identityInf.proxidcart)) {
                    showNotice("请填写代理人身份证号码")
                    return
                } else if (!UserUtils.verificaID(identityInf.proxidcart)) {
                    showNotice("请填正确的代理人身份证号码")
                    return
                }
            }
            if (TextUtils.isEmpty(identityInf.idFace1)) {
                showNotice("请选择法人身份证正面照")
                return
            }
            if (TextUtils.isEmpty(identityInf.idBack1)) {
                showNotice("请选择法人身份证反面照")
                return
            }
            if (auth_type == "3") {
                if (TextUtils.isEmpty(identityInf.proxidFaceFile1)) {
                    showNotice("请选择代理人身份证正面照")
                    return
                }
                if (TextUtils.isEmpty(identityInf.proxidBackFile1)) {
                    showNotice("请选择代理人身份证反面照")
                    return
                }
                /*if (TextUtils.isEmpty(identityInf.proxidHoldFile1)) {
                    showNotice("请选择代理人手持身份证照片")
                    return
                }*/
            } else {
                /*if (TextUtils.isEmpty(identityInf.idHold1)) {
                    showNotice("请选择法人手持身份证照片")
                    return
                }*/
            }
            if (TextUtils.isEmpty(identityInf.businessLicence1)) {
                showNotice("请选择营业执照")
                return
            }
            /*if (TextUtils.isEmpty(identityInf.businessLicenceHold1)) {
                showNotice(when (auth_type) {
                    "3" -> "请选择法人手持营业执照"
                    else -> "请选择代理人手持营业执照"
                })
                return
            }*/
        }
        if ("1" != auth_type) {
            //identityInf.idHold = null
        } else {
            identityInf.position = null
            identityInf.companyName = null
            identityInf.businessLicenceHold1 = null
            identityInf.businessLicenceHold = null
            identityInf.professionType = null
        }
        uploadInf(when (auth_type) {
            "1" -> 1
            "2" -> 2
            else -> 3
        })
    }

    override fun uploadResult(tag: Boolean, msg: String?) {
        if (tag) {
            PjjApplication.application.renZheng = "1"
            var makeText = Toast.makeText(PjjApplication.application, "上传成功", Toast.LENGTH_LONG)
            makeText.setGravity(Gravity.CENTER, 0, 0)
            makeText.show()
            finish()
        } else {
            showNotice(msg)
        }
    }

    private val takePhotoDialog: TakePhotoDialog by lazy {
        TakePhotoDialog(this).apply {
            onItemClickListener = object : TakePhotoDialog.OnItemClickListener {
                override fun takePhoto() {
                    startPhoto()
                    dismiss()
                }

                override fun selectPhoto() {
                    uploadImage_()
                    dismiss()
                }

                override fun surePhoto(path: String) {
                    dismiss()
                }
            }
        }
    }

    /**
     * 启动选图片前弹窗
     */
    private fun uploadImage() {
        takePhotoDialog.show()
    }

    private var mTempPhotoPath: String? = null
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
        startActivityForResult(intentCamera, 201)
    }

    /**
     * 正式选择图片
     */
    private fun uploadImage_() {
        val intent = Intent(this, PickerActivity::class.java)
        intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE)
        intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 1)
        startActivityForResult(intent, 200)
    }

    private fun changeNoticeText(text: String? = "") {
        if (text == null) {
            tv_sure_notice.visibility = View.GONE
            return
        }
        tv_sure_notice.visibility = View.VISIBLE
    }

    private fun changeSubmitButton(text: String?, whiteBg: Boolean = false) {
        if (text == null) {
            tv_submit.visibility = View.GONE
            return
        }
        tv_submit.visibility = View.VISIBLE
        tv_submit.text = text
        if (whiteBg) {
            tv_submit.background = ViewUtils.getDrawable(R.drawable.shape_theme_side_3)
            tv_submit.setTextColor(ViewUtils.getColor(R.color.color_theme))
        } else {
            tv_submit.background = ViewUtils.getDrawable(R.drawable.shape_theme_bg_3)
            tv_submit.setTextColor(Color.WHITE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == 200 && resultCode == PickerConfig.RESULT_CODE -> {
                var select = data?.getParcelableArrayListExtra<Media>(PickerConfig.EXTRA_RESULT)
                select?.run {
                    if (size > 0) {
                        //println(this[0].path)
                        var media = this[0]
                        var path = media.path
                        Log.e("select", "select.size=$path")
                        if (identityInf.authType != "1") {
                            merchantsHelp.selectImageResult(path)
                        } else {
                            personHelp.selectImageResult(path)
                        }
                    }
                }
            }
            requestCode == 201 && resultCode == Activity.RESULT_OK -> {
                var file = File(mTempPhotoPath)
                if (file.exists()) {
                    if (identityInf.authType != "1") {
                        merchantsHelp.selectImageResult(mTempPhotoPath!!)
                    } else {
                        personHelp.selectImageResult(mTempPhotoPath!!)
                    }
                }
            }
            requestCode == 300 && resultCode == 200 -> {
                data?.let {
                    identityInf.professionType = it.getStringExtra("indestry_type_id")
                    merchantsHelp.selectProfessionResult(it.getStringExtra("industry"))
                }
            }
        }
    }

    private fun uploadInf(tag: Int) {
        mPresent?.loadUploadInformation(identityInf, tag)
    }

    private fun geren() {
        identityInf.authType = "1"
        personHelp.visibility = View.VISIBLE
        merchantsHelp.visibility(View.GONE)
        changeSubmit(personStatue)
        if (reToShenHeP) {
            personData?.let {
                personHelp.centerShow(it)
            }
        }
        //Log.e("TAG", "geren autype: ${identityInf.authType}")
    }

    private fun shangjia() {
        identityInf.authType = "2"
        personHelp.visibility = View.GONE
        merchantsHelp.visibility(View.VISIBLE)
        changeSubmit(merchantsStatue)
        if (reToShenHeM) {
            merchantsData?.let {
                merchantsHelp.centerShow(it)
            }
        }
        //Log.e("TAG", "shangjia autype: ${identityInf.authType}")
    }

    private fun changeSubmit(statue: String?) {
        when (statue) {
            "1" -> {
                changeNoticeText(null)
                changeSubmitButton("编辑认证信息", true)
            }
            "3" -> {
                changeNoticeText(null)
                changeSubmitButton(null)
            }
            else -> {
                changeNoticeText()
                changeSubmitButton("确认提交")
            }
        }
    }

    override fun updateHasSelectedImage(bean: CertificationResultBean) {
        personStatue = bean.userAuthDetails.isAuth
        merchantsStatue = bean.userBusinessAuthDetails.isAuth
        personData = bean.userAuthDetails
        merchantsData = bean.userBusinessAuthDetails

        editPersonTag = if (personStatue == "1" || personStatue == "3") {
            personData?.let {
                personHelp.centerShow(it)
            }
            false
        } else {
            true
        }
        editMerchantTag = if (merchantsStatue == "1" || merchantsStatue == "3") {
            merchantsData?.let {
                merchantsHelp.centerShow(it)
            }
            false
        } else {
            true
        }
        updateCertification(tv_geren_statue, personStatue)
        updateCertification(tv_shangjia_statue, merchantsStatue)
        if (defaultShow) {
            //Log.e("TAG", "autype: ${identityInf.authType}")
            if (personHelp.visibility == View.VISIBLE) {
                ll_geren.performClick()
            } else {
                ll_shangjia.performClick()
            }
            return
        }
        when {
            merchantsStatue == "3" || (merchantsStatue == "2" && personStatue != "3") -> {
                identityInf.authType = merchantsData?.let {
                    it.authType
                }
                ll_shangjia.performClick()
            }
            else -> {
                identityInf.authType = "1"
                ll_geren.performClick()
            }
        }
    }

    private fun setCertificationResult(tv: TextView, result: String?, color: Int = -1) {
        if (TextUtils.isEmpty(result)) {
            tv.visibility = View.GONE
        } else {
            tv.visibility = View.VISIBLE
            tv.text = result
            tv.setTextColor(color)
        }
    }

    /**
     * 隐藏输入法
     */
    private fun hiddenInputMethod() {
        /*currentFocus?.let {
            (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(it.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }*/
    }
}
