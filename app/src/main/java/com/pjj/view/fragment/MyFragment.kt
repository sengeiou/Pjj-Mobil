package com.pjj.view.fragment


import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.FileProvider
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.dmcbig.mediapicker.PickerActivity
import com.dmcbig.mediapicker.PickerConfig
import com.dmcbig.mediapicker.entity.Media
import com.pjj.BuildConfig

import com.pjj.R
import com.pjj.PjjApplication
import com.pjj.contract.MyContract
import com.pjj.present.MyPresent
import com.pjj.utils.Log
import com.pjj.utils.SharedUtils
import com.pjj.utils.SharedUtils.USER_TYPE
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.activity.*
import com.pjj.view.dialog.TakePhotoDialog
import kotlinx.android.synthetic.main.fragment_my.*
import java.io.File


/**
 * A simple [Fragment] subclass.
 * 我的
 */
class MyFragment : ABFragment<MyPresent>(), MyContract.View {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_my
    }

    private var loginStatue = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let {
            mTempPhotoPath = it.getString("mTempPhotoPath")
        }
        mPresent = MyPresent(this)
        ll_ren_zheng.setOnClickListener(onClickListener)
        edit_tv.setOnClickListener(onClickListener)
        ll_my_order.setOnClickListener(onClickListener)
        ll_zuo_piin.setOnClickListener(onClickListener)
        ll_mo_ban.setOnClickListener(onClickListener)
        ll_my_account.setOnClickListener(onClickListener)
        ll_fa_piao.setOnClickListener(onClickListener)
        ll_zhi_dao_shou_ce.setOnClickListener(onClickListener)
        ll_guan_yu_wo_men.setOnClickListener(onClickListener)
        ll_yi_jian.setOnClickListener(onClickListener)
        bt_join.setOnClickListener(onClickListener)
        ll_tuiguang.setOnClickListener(onClickListener)
        loginStatue = SharedUtils.checkLogin()
        tv_name.setOnClickListener(onClickListener)
        iv_head.setOnClickListener(onClickListener)
        ll_jifen.setOnClickListener(onClickListener)
        ll_screen_manage.setOnClickListener(onClickListener)
        ll_fabu.setOnClickListener(onClickListener)
        val dp16 = ViewUtils.getDp(R.dimen.dp_16)
        val useType = SharedUtils.getXmlForKey(USER_TYPE)
        if (TextUtils.isEmpty(useType) || ("estateManagement" != useType && "cooperativePartner" != useType && "companyLaolingwei" != useType && "subManagement" != useType)) {
            ll_screen_manage.visibility = View.GONE
        }
        var drawable: Drawable = ViewUtils.getDrawable(R.mipmap.write_name).apply {
            setBounds(0, 0, dp16, dp16)
        }
        if (!BuildConfig.APP_TYPE) {
            var drawableLeft = ViewUtils.getDrawable(R.mipmap.heart).apply {
                setBounds(0, 0, dp16, ViewUtils.getDp(R.dimen.dp_14))
            }
            //tv_name.setCompoundDrawables(drawable, null, null, null)
            val drawableArray = arrayOf(drawable, drawableLeft)
            drawable = LayerDrawable(drawableArray).apply {
                setLayerInset(0, 0, 0, dp16 + 10, 0)
                setLayerInset(1, dp16 + 10, 0, 0, 6)
                setBounds(0, 0, dp16 * 2 + 10, dp16)
            }
        }
        tv_name.compoundDrawablePadding = ViewUtils.getDp(R.dimen.dp_13)
        tv_name.setCompoundDrawables(null, null, drawable, null)
        aboutCheckLogin()
        mPresent?.loadMyIntegral()
    }

    @SuppressLint("SetTextI18n")
    override fun updateMyIntegral(s: String) {
        val s1 = if (s.isEmpty()) "" else "$s 金币"
        tv_jifen.text = s1
    }

    private fun aboutCheckLogin() {
        if (loginStatue) {
            mPresent?.loadMyIntegral()
            mPresent.loadVerificaTask()
            var xmlForKey = SharedUtils.getXmlForKey(SharedUtils.HEAD_IMG)
            var name = SharedUtils.getXmlForKey(SharedUtils.USER_NICKNAME)
            tv_name.text = name
            edit_tv.text = "退出登录"
            updateHeadImg(xmlForKey)
            user_rank.visibility = View.VISIBLE
            val useType = SharedUtils.getXmlForKey(USER_TYPE)
            if (TextUtils.isEmpty(useType) || ("estateManagement" != useType && "cooperativePartner" != useType && "companyLaolingwei" != useType && "subManagement" != useType)) {
                ll_screen_manage.visibility = View.GONE
            } else {
                ll_screen_manage.visibility = View.VISIBLE
            }
        } else {
            edit_tv.text = "登录"
            tv_name.text = "未登录"
            var drawable = if (BuildConfig.APP_TYPE) null else ViewUtils.getDrawable(R.mipmap.heart).apply {
                setBounds(0, 0, ViewUtils.getDp(R.dimen.dp_14), ViewUtils.getDp(R.dimen.dp_14))
            }
            tv_name.setCompoundDrawables(null, null, drawable, null)
            user_rank.visibility = View.GONE
            iv_head.setImageDrawable(ViewUtils.getDrawable(R.mipmap.un_login))
            ll_screen_manage.visibility = View.GONE
        }
    }

    private var requestOptions = RequestOptions().error(ViewUtils.getDrawable(R.mipmap.head_default)).diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop()

    override fun updateHeadImg(path: String) {
        if (!TextUtils.isEmpty(path)) {
            SharedUtils.saveForXml(SharedUtils.HEAD_IMG, path)
        }
        var s = PjjApplication.filePath + path
        Log.e("TAG", "filePath=$s")
        Glide.with(this).load(s).apply(requestOptions).into(iv_head)
        if (takePhotoDialog.isShowing) {
            takePhotoDialog.dismiss()
        }
    }

    //http://pjj-liftapp.oss-cn-beijing.aliyuncs.com/
    override fun onResume() {
        super.onResume()
        if (!loginStatue) {//再次检测登录
            var statue = SharedUtils.checkLogin()
            if (statue != loginStatue) {
                loginStatue = statue
                aboutCheckLogin()
            }
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            mPresent?.loadMyIntegral()
            if (loginStatue && PjjApplication.application.renZheng != "3") {
                mPresent?.loadVerificaTask()
            }
        }

    }

    private var personStatue: String? = null
    private var merchantsStatue: String? = null
    override fun updateVerifiResult(tag: String, tag_b: String, msg: String?) {
        //0 未认证  1审核中  2认证失败  3认证成功
        personStatue = tag
        merchantsStatue = tag_b
        tv_ren_zheng.text = when {
            tag == "3" && tag_b == "3" -> {
                PjjApplication.application.renZheng = "3"
                "个人/商家认证"
            }
            tag == "3" && tag_b != "3" -> {
                "个人认证"
            }
            tag != "3" && tag_b == "3" -> {
                "商家认证"
            }
            /* tag == "1" && tag_b != "3" -> {
                 "个人认证中"
             }
             tag != "3" && tag_b == "1" -> {
                 "商家认证中"
             }
             tag == "0" && tag_b == "0" -> {
                 "未认证"
             }*/
            else -> {
                "未认证"
            }
        }
    }

    private var onClickListener = View.OnClickListener {
        when (it.id) {
            R.id.ll_tuiguang -> startActivity(Intent(activity, MyTuiGuangActivity::class.java))
            R.id.ll_fabu -> startActivity(Intent(activity, MyReleaseActivity::class.java))
            R.id.ll_screen_manage -> {
                val useType = SharedUtils.getXmlForKey(USER_TYPE)
                if (TextUtils.isEmpty(useType)) {
                    showNotice("权限不足")
                    return@OnClickListener
                }
                if (checkLogin()) {
                    if (useType == "companyLaolingwei") {
                        startActivity(Intent(activity!!, ManageBothActivity::class.java))
                    } else {
                        startActivity(Intent(activity!!, ScreenManageActivity::class.java))
                    }
                }
            }
            R.id.ll_jifen -> startActivity(Intent(activity!!, MyIntegralActivity::class.java))
            R.id.ll_mo_ban, R.id.ll_my_account, R.id.ll_fa_piao -> if (checkLogin()) toast("暂未开放")
            R.id.iv_head -> if (checkLogin()) takePhotoDialog.setPhotoResult(null)
            R.id.ll_zhi_dao_shou_ce -> WebActivity.newInstance(activity!!, "指导手册", "http://protal.test.pingjiajia.cn/guide", "1")
            R.id.ll_guan_yu_wo_men -> startActivity(Intent(activity!!, AboutSelfActivity::class.java))
            R.id.ll_yi_jian -> if (checkLogin()) startActivity(Intent(activity!!, OpinionActivity::class.java))
            R.id.ll_my_order -> {
                if (checkLogin()) onFragmentInteractionListener?.showOtherFragment("my_order")
            }
            R.id.ll_zuo_piin/*, R.id.ll_mo_ban */ -> {
                if (checkLogin()) onFragmentInteractionListener?.showOtherFragment("my_template")
            }
            R.id.ll_ren_zheng -> {
                if (checkLogin()) {
                    var intent = Intent(activity, CertificationActivity::class.java)
                    intent.putExtra("personStatue", personStatue)
                    intent.putExtra("merchantsStatue", merchantsStatue)
                    intent.putExtra("comeFrom", true)
                    startActivity(intent)
                }
            }

            R.id.edit_tv -> {
                SharedUtils.cleanLoginXml()
                if (loginStatue) {
                    startActivity(Intent(activity, LoginActivity::class.java).putExtra("time_out", true))
                    activity?.finish()
                } else {
                    startActivity(Intent(activity, LoginActivity::class.java))
                }
            }
            R.id.bt_join -> {
                WebActivity.newInstance(activity!!, "全国区域独家代理", PjjApplication.AD_JOIN, "5")
            }
            R.id.tv_name -> {
                if (checkLogin()) startActivityForResult(Intent(activity, ChangeNameActivity2::class.java), 203)
            }
        }
    }
    private val takePhotoDialog: TakePhotoDialog by lazy {
        TakePhotoDialog(activity!!).apply {
            onItemClickListener = object : TakePhotoDialog.OnItemClickListener {
                override fun takePhoto() {
                    //checkPermission()
                    startPhoto()
                }

                override fun selectPhoto() {
                    val intent = Intent(activity, PickerActivity::class.java)
                    intent.putExtra(PickerConfig.SELECT_MODE, PickerConfig.PICKER_IMAGE)
                    intent.putExtra(PickerConfig.MAX_SELECT_COUNT, 1)
                    startActivityForResult(intent, 201)
                }

                override fun surePhoto(path: String) {
                    mPresent?.uploadHeadImage(path)
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
                        startActivityForResult(Intent(activity, CropActivity::class.java)
                                .putExtra(CropActivity.IMAGE_PATH, item)
                                .putExtra(CropActivity.IMAGE_SAVE_PATH, PjjApplication.App_Path + "image/"), 202)
                    }
                }
            }
        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            var file = File(mTempPhotoPath)
            if (file.exists()) {
                //CropActivity.newInstance(this, mTempPhotoPath, PjjApplication.App_Path + "image/", 202)
                startActivityForResult(Intent(activity, CropActivity::class.java)
                        .putExtra(CropActivity.IMAGE_PATH, mTempPhotoPath)
                        .putExtra(CropActivity.IMAGE_SAVE_PATH, PjjApplication.App_Path + "image/"), 202)
            }
        } else if (requestCode == 202 && resultCode == CropActivity.RESULT_CODE) {
            data?.getStringExtra(CropActivity.IMAGE_SAVE_PATH)?.let {
                takePhotoDialog.setPhotoResult(it)
            }
        } else if (requestCode == 203 && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra("user_name")?.let {
                tv_name.text = it
                SharedUtils.saveForXml(SharedUtils.USER_NICKNAME, it)
            }
        }
    }

    private var mTempPhotoPath: String? = null
    private fun startPhoto() {
        // 跳转到系统的拍照界面
        mTempPhotoPath = PjjApplication.App_Path + "photo/${System.currentTimeMillis()}-photo.png"
        /*  var intentToTakePhoto = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
          var imageUri = FileProvider.getUriForFile(activity!!, "com.pjj.fileprovider", File(mTempPhotoPath))
          //下面这句指定调用相机拍照后的照片存储的路径
          intentToTakePhoto.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
          startActivityForResult(intentToTakePhoto, 200)*/
        //系统相机
        var intentCamera = Intent()
        var imageUri = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                //添加这一句表示对目标应用临时授权该Uri所代表的文件
                intentCamera.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                FileProvider.getUriForFile(activity!!, "com.pjj.fileprovider", File(mTempPhotoPath))
            }
            else -> Uri.fromFile(File(mTempPhotoPath))
        }
        intentCamera.action = MediaStore.ACTION_IMAGE_CAPTURE
        //将拍照结果保存至photo_file的Uri中，不保留在相册中
        intentCamera.putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
        startActivityForResult(intentCamera, 200)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        mTempPhotoPath?.run {
            outState.putString("mTempPhotoPath", this)
        }
        super.onSaveInstanceState(outState)
    }
}
