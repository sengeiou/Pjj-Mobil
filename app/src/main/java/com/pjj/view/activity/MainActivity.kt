package com.pjj.view.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Point
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.View
import android.widget.RadioButton

import com.pjj.R
import com.pjj.module.xspad.XspManage
import com.pjj.view.custom.MainMenuView
import com.pjj.view.fragment.*
import kotlinx.android.synthetic.main.activity_main.*
import android.widget.Toast
import com.pjj.contract.MainContract
import com.pjj.present.MainPresent
import com.pjj.BuildConfig
import com.pjj.PjjApplication
import com.pjj.intent.RetrofitService
import com.pjj.module.AppUpdateBean
import com.pjj.receiver.TagAliasOperatorHelper
import com.pjj.utils.*
import com.pjj.view.dialog.*
import com.umeng.analytics.MobclickAgent
import java.io.File


/**
 * 首页
 * 借助上一个android写的布局（包括命名）
 */
class MainActivity : FragmentsActivity<BaseFragment<*>, MainPresent>(), ABFragment.OnFragmentInteractionListener, MainContract.View {
    private lateinit var handler: Handler
    private val adTypeDialog: AdTypeDialog by lazy {
        AdTypeDialog(this@MainActivity).apply {
            onTypeSelectListener = object : AdTypeDialog.OnTypeSelectListener {
                override fun selectMerchamts() {
                    XspManage.getInstance().identityType = 2
                    if (fragmentSelectType == 7 && !XspManage.getInstance().newMediaData.releaseTag) {
                        allowNext("-13", "", "")
                        return
                    }
                    mPresent?.certificationUser()
                    //mPresent?.getTemplateList()
                }

                override fun selectPerson() {
                    XspManage.getInstance().identityType = 1
                    if (fragmentSelectType == 7 && !XspManage.getInstance().newMediaData.releaseTag) {
                        allowNext("-13", "", "")
                        return
                    }
                    mPresent?.certificationUser()
                    //mPresent?.getTemplateList()
                }
            }
        }
    }
    private var parentHeight = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.e("FragmentsActivity_TAG", "savedInstanceState=${savedInstanceState == null}")
        var userId = SharedUtils.getXmlForKey(SharedUtils.USER_ID)
        var token = SharedUtils.getXmlForKey(SharedUtils.TOKEN)
        if (!TextUtils.isEmpty(userId) && !TextUtils.isEmpty(token)) {
            PjjApplication.application.userId = userId
            RetrofitService.getInstance().setToken(token)
        }
        PjjApplication.application.statueHeight = statue
        mPresent = MainPresent(this)
        handler = Handler()
        TagAliasOperatorHelper.getInstance().setOnAliasListener {
            if (it) {//成功
                Log.e("TAG", "setOnAlias:success")
            } else {
                handler.postDelayed({ setJPushAlias() }, 2000)
            }
        }
        setJPushAlias()

        /*if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0x55000000)
        }*/
        initMainDrawableTop()
        rb_home.setOnClickListener(onClick)
        rb_works.setOnClickListener(onClick)
        rb_order.setOnClickListener(onClick)
        rb_mine.setOnClickListener(onClick)
        rbPress.setOnClickListener(onClick)
        fl_main.setOnClickListener(onClick)
        view_empty.setOnClickListener(onClick)
        rl_bottom.setOnClickListener(onClick)
        val parentView = findViewById<View>(R.id.parent)
        parentView.setOnClickListener(onClick)
        parentView.post {
            parentHeight = parentView.height
            Log.e("TAG", "parentView: width=${parentView.width}, height=${parentView.height}")
        }
        onClick(R.id.rb_home)
        //mainMenu.addChildMenu("便民\n信息发布", "随机便民\n信息发布", "DIY\n信息发布", "随机\n信息发布", "拼屏\n信息发布")
        mainMenu.addChildMenu("免费\n线上发布", "广告\n传媒发布", "自营\n传媒发布")
        mainMenu.onItemClickListener = object : MainMenuView.OnItemClickListener {
            override fun itemClick(index: Int) {
                //Log.e("TAG", "点击位置=$index")
                if (checkLogin()) {
                    XspManage.getInstance().bianMinPing = 0
                    when (index) {
                        0 -> {
                            /*fragmentSelectType = 2
                            adTypeDialog.setType(2)*/
                            //startActivity(Intent(this@MainActivity, ElevatorNextActivity::class.java))
                            if (checkLogin()) {
                                startActivity(Intent(this@MainActivity, FreeReleaseSelectActivity::class.java))
                            }
//                            XspManage.getInstance().adType = 9
//                            XspManage.getInstance().newMediaData.releaseTag = true
//                            startActivity(Intent(this@MainActivity, ReleaseRuleActivity::class.java).putExtra("title", "电梯传媒发布规则"))
                        }
                        1 -> {
                            /*fragmentSelectType = 4
                            adTypeDialog.setType(4)*/
                            XspManage.getInstance().newMediaData.releaseTag = true
                            XspManage.getInstance().adType = 7
                            startActivity(Intent(this@MainActivity, ReleaseRuleActivity::class.java).putExtra("title", "广告传媒发布规则"))
                            //showADDialog(7)
                        }
                        2 -> {
                            /*fragmentSelectType = 1
                            adTypeDialog.setType(1)*/
                            showNotice(PjjApplication.Un_Show)
                        }
                        3 -> {
                            fragmentSelectType = 3
                            adTypeDialog.setType(3)
                        }
                        4 -> {
                            /* if (BuildConfig.APP_TYPE) {
                                 toast("暂未开放")
                             } else {
                                 XspManage.getInstance().bianMinPing = 1
                                 fragmentSelectType = 4
                                 adTypeDialog.setType(4)
                             }*/
                            showADDialog(5)
                        }
                    }
                }

//                mPresent?.certificationUser()
                if (index != 2) {
                    view_empty.visibility = View.GONE
                    fl_main.visibility = View.GONE
                }
            }
        }
        getScreenSizeOfDevice2()

        val index = intent.getIntExtra("index", -1)
        showOrderListActivityFragment(index)

        if (Build.VERSION.SDK_INT >= 23) {
            mPermissionList.clear()
            for (i in 0 until permissions.size) {
                if (ContextCompat.checkSelfPermission(this@MainActivity, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i])
                }
            }
            if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
                mPresent?.loadAppVersionTask()
            }
        } else {
            mPresent?.loadAppVersionTask()
        }
    }

    override fun fitSystemWindow(): Boolean {
        return false
    }

    override fun allowBackFinish(): Boolean {
        return false
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        var index = intent.getIntExtra("index", -1)
        showOrderListActivityFragment(index)
    }

    private fun showOrderListActivityFragment(index: Int) {
        cancelWaiteStatue()
        if (index > -1) {
            //onClick(R.id.rbMessage)
            rb_order.performClick()
            if (nowFragment is OrderListFragment) {
                Log.e("TAG", "showOrderListActivityFragment index=$index")
                (nowFragment as OrderListFragment).index = index
            }
        }
    }

    private fun getScreenSizeOfDevice2() {
        var point = Point()
        windowManager.defaultDisplay.getRealSize(point)

        PjjApplication.application.screenHeight = point.y
        var file = File(PjjApplication.App_Path + "phone.txt")
        var dm = resources.displayMetrics
        //手机宽度dp值 = 手机实际宽度像素px / 手机屏幕密度比
        val widthDP = dm.widthPixels / dm.density//375dp
        Log.e("TAG", "onCreate: " + widthDP + " " + 750 / 320f)
        Log.e("TAG", "onCreate: " + dm.widthPixels + " * " + dm.heightPixels + " \n " + dm.toString())


        var x = Math.pow(point.x.toDouble() / dm.xdpi, 2.toDouble())
        var y = Math.pow(point.y.toDouble() / dm.ydpi, 2.toDouble())
        var screenInches = Math.sqrt(x + y)
        Log.e("TAG", "Screen inches : $screenInches")

        var writeString = dm.toString() + "\n尺寸" + screenInches
        if (!file.exists()) {
            FileUtils.saveStringFile(file.absolutePath, writeString)
            FileUtils.saveStringFile(file.absolutePath, "x=${point.x}, y=${point.y}")
        }
    }

    override fun getFragmentContainerViewId(): Int {
        return R.id.content1
    }

    override fun onResume() {
        super.onResume()
        checkSelfPermission()
        if (update) {
            Log.e("TAG", "onResume :finish ")
            finish()
        }
    }

    private var nowRadioButton: RadioButton? = null
    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.rb_home -> {
                nowRadioButton = rb_home
                showFragment("rbHome")
            }//首页
            R.id.rb_works -> {
                if (checkLogin()) showFragment("rbShop")
                else {
                    nowRadioButton?.isChecked = true
                    rb_works.isChecked = false
                }
            }//模板/认证
            R.id.rb_order -> {
                if (checkLogin()) showFragment("rbMessage")
                else {
                    nowRadioButton?.isChecked = true
                    rb_order.isChecked = false
                }
            }//订单
            R.id.rb_mine -> {
                nowRadioButton = rb_mine
                showFragment("rbMine")
            }//我的
            R.id.fl_main -> {
                fl_main.visibility = View.GONE
                view_empty.visibility = View.GONE
            }
            R.id.view_empty, R.id.rl_bottom -> {
                hideMainMenu()
            }
            R.id.rbPress -> {
                //showMainMenu()
                if (fl_main.visibility == View.VISIBLE) {
                    view_empty.visibility = View.GONE
                    fl_main.visibility = View.GONE
                } else {
                    fl_main.visibility = View.VISIBLE
                    view_empty.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun hideMainMenu(): Boolean {
        if (fl_main.visibility == View.VISIBLE) {
            view_empty.visibility = View.GONE
            fl_main.visibility = View.GONE
            return true
        }
        return false
    }

    override fun getFragment(tag: String): BaseFragment<*>? {
        when (tag) {
            "rbHome" -> return HomePageFragment()
            "rbShop" -> return MyTemplateFragment()
            "rbMessage" -> return OrderListFragment()
            "rbMine" -> return MyFragment()
        }
        return null
    }

    private var fragmentSelectType = -1
    override fun showADDialog(type: Int) {
        if (type == 77) {//健康预约
            mPresent?.certificationUser(true)
            return
        }
        if (type < 0) {
            startActivity(Intent(this, ElevatorNextActivity::class.java))
            return
        }
        if (type < 1000) {
            showNotice("该功能暂未开放\n敬请期待")
            return
        }

        if (type != 4) {
            XspManage.getInstance().bianMinPing = 0
        }
        fragmentSelectType = type
        if (type == 5) {//拼屏
            XspManage.getInstance().identityType = 3
            XspManage.getInstance().adType = 5
            mPresent?.certificationUser()
            return
        }
        //mPresent?.certificationUser()
        adTypeDialog.setType(type)
    }

    override fun allowAdDialog() {
        adTypeDialog.setType(fragmentSelectType)
    }

    private val shenHeDialog: ImageNoticeDialog by lazy {
        ImageNoticeDialog(this)
    }
    private val noPassDialog: NoPassDialog by lazy {
        NoPassDialog(this).apply {
            onItemClickListener = object : NoPassDialog.OnItemClickListener {
                override fun go(msg: String) {
                    goRenZheng(msg)
                }
            }
        }
    }
    private val templateDialog: AddTemplateDialog by lazy {
        AddTemplateDialog(this).apply {
            onItemClickListener = object : AddTemplateDialog.OnItemClickListener {
                override fun leftClick() {
                    goCreateTemplate()
                }
            }
        }
    }

    /**
     * 去认证
     */
    private fun goRenZheng(msg: String? = null) {
        Log.e("TAG", "goRenZheng")
        var intent = Intent(this, CertificationActivity::class.java)
        intent.putExtra("default", XspManage.getInstance().identityType.toString())
        intent.putExtra("personStatue", tagP)
        intent.putExtra("merchantsStatue", tagM)
        startActivity(intent)
    }

    private fun goCreateTemplate() {
        var temType = XspManage.getInstance().adType
        var identity_type = XspManage.getInstance().identityType
        var head = when (identity_type) {
            1 -> "个人"
            else -> "商家"
        }
        var _title = when (temType) {
            1 -> "DIY信息模板"
            2 -> "便民信息模板"
            3 -> "随机发布模板"
            else -> "随机便民模板"
        }
        startActivity(Intent(this, TemplateListActivity::class.java)
                .putExtra("title_text", head + _title)
                .putExtra("identity_type", identity_type.toString())
                .putExtra("ad_type", temType))
    }

    private var tagP: String? = null
    private var tagM: String? = null

    /**
     * 0 未认证  1审核中  2认证失败  3认证成功
     */
    override fun allowNext(tag: String, tag_b: String, msg: String?) {
        var adType = XspManage.getInstance().adType
        var identityType = XspManage.getInstance().identityType
        when (tag) {
            "-14" -> {
                adTypeDialog.dismiss()
                noticeDialog.setNotice("暂无审核通过模板\n" +
                        "请耐心等待审核\n", 4000)
                return
            }
            "-77" -> {
                startActivity(Intent(this, JianKangActivity::class.java))
                return
            }
            "-13" -> {
                startActivity(Intent(this, TemplateListActivity::class.java)
                        .putExtra("title_text", "个人传媒发布模板")
                        .putExtra("identity_type", identityType.toString())
                        .putExtra("ad_type", adType)
                        .putExtra("releaseTag", XspManage.getInstance().newMediaData.releaseTag))
                return
            }
            "-11" -> {
                if (identityType == 3) {
                    SpeedScreenActivity.newInstance(this, true)
                    return
                }
                if (identityType == 2) {
                    when (adType) {
                        1, 2 -> startActivity(Intent(this@MainActivity, SelectReleaseAreaActivity::class.java))
                        else -> startActivity(Intent(this@MainActivity, SelectRandomReleaseAreaActivity::class.java))
                    }
                } else {
                    when (adType) {
                        1, 2 -> startActivity(Intent(this@MainActivity, SelectReleaseAreaActivity::class.java))
                        else -> startActivity(Intent(this@MainActivity, SelectRandomReleaseAreaActivity::class.java))
                    }
                }
            }
            "-12", "-10" -> {
                templateDialog.setImageResource(when (adType) {
                    1 -> R.mipmap.main_diy
                    2 -> R.mipmap.main_bm
                    3 -> R.mipmap.main_suiji_diy
                    4 -> R.mipmap.main_suiji_bm
                    else -> R.mipmap.main_pp
                })
            }
            else -> {
                tagP = tag
                tagM = tag_b
                //0 未认证  1审核中  2认证失败  //3认证成功
                if (identityType == 2) {//1个人 2商家
                    when (tag_b) {
                        "1" -> {
                            shenHeDialog.notice = "您的认证资料正在审核中，\n请耐心等待！"
                            shenHeDialog.show()
                        }
                        "2" -> {
                            noPassDialog.showLeft()
                            noPassDialog.errorText = msg
                        }
                        else -> goRenZheng()
                    }
                } else {//1个人
                    when (tag) {
                        "1" -> {
                            shenHeDialog.notice = "您的认证资料正在审核中，\n请耐心等待！"
                            shenHeDialog.show()
                        }
                        "2" -> {//未通过
                            noPassDialog.showLeft()
                            noPassDialog.errorText = msg
                        }
                        else -> goRenZheng()
                    }
                }
            }
        }
    }

    override fun showOtherFragment(tag: String) {
        when (tag) {
            "my_order" -> rb_order.performClick()
            else -> rb_works.performClick()
        }
    }

    override fun showGuidStub(index: Int) {
        Log.e("TAG", "turnOffMainMenu : index=$index")
    }

    private var finishTag = false

    override fun onBackPressed() {
        if (!finishTag) {
            finishTag = true
            var makeText = Toast.makeText(this, "再次点击退出程序", Toast.LENGTH_LONG)
            makeText.setGravity(Gravity.CENTER, 0, 200)
            makeText.show()
            handler.postDelayed({
                finishTag = false
            }, 3500)
            return
        } else {
            if (BuildConfig.APP_TYPE)
                MobclickAgent.onKillProcess(this)
            android.os.Process.killProcess(android.os.Process.myPid())
            return
        }
        super.onBackPressed()
    }

    private var permissions = arrayOf(Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.READ_PHONE_STATE)
    private var mPermissionList = ArrayList<String>()
    private val MY_PERMISSIONS_REQUEST_CALL_CAMERA = 2
    private val GET_UNKNOWN_APP_SOURCES = 203
    private val INSTALL_PACKAGES_REQUESTCODE = 201
    private var update = false
    private fun checkSelfPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            mPermissionList.clear()
            for (i in 0 until permissions.size) {
                if (ContextCompat.checkSelfPermission(this@MainActivity, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    mPermissionList.add(permissions[i])
                }
            }
            if (mPermissionList.isEmpty()) {//未授予的权限为空，表示都授予了
                //Toast.makeText(this@MainActivity, "已经授权", Toast.LENGTH_LONG).show()
            } else {//请求权限方法
                val permissions = mPermissionList.toArray(arrayOfNulls<String>(mPermissionList.size))
                ActivityCompat.requestPermissions(this@MainActivity, permissions, MY_PERMISSIONS_REQUEST_CALL_CAMERA)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_CAMERA) {
            grantResults.forEachIndexed { i, it ->
                if (it !== PackageManager.PERMISSION_GRANTED) {
                    //判断是否勾选禁止后不再询问
                    if (permissions[i] != Manifest.permission.REQUEST_INSTALL_PACKAGES) {
                        val showRequestPermission = ActivityCompat.shouldShowRequestPermissionRationale(this@MainActivity, permissions[i])
                        if (showRequestPermission) {
                            finish()
                        }
                    }
                }
            }
            mPresent?.loadAppVersionTask()
        } else if (requestCode == INSTALL_PACKAGES_REQUESTCODE) {
            AppUpdate.install26(this, File(Uri.parse(updateAppFilePath).path))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GET_UNKNOWN_APP_SOURCES) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val haveInstallPermission = packageManager.canRequestPackageInstalls()
                if (!haveInstallPermission) {
                    finish()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private val updateAppDialog: UpdateAppDialog by lazy {
        UpdateAppDialog(this).apply {
            onUpdateAppListener = object : UpdateAppDialog.OnUpdateAppListener {
                override fun downLoadApp(updateUrl: String) {
                    mPresent?.loadDownloadAppTask(updateUrl)
                }
            }
        }
    }

    override fun appVersionResult(data: AppUpdateBean) {
        data.data.add(AppUpdateBean.DataBean().apply {
            downName = "9ac3aa60573219c88193ee1dc771a2b7.apk"
            versionInt = 4
            lowestVersionInt = 4
            info = "测试安装"
        })
        var data1 = data.data
        if (TextUtils.isNotEmptyList(data1)) {
            var dataBean = data1[0]
            var lastVersion = dataBean.lowestVersionInt
            var versionInt = dataBean.versionInt
            var downName = dataBean.downName
            //downName = "9ac3aa60573219c88193ee1dc771a2b7.apk"
            if (!TextUtils.isEmpty(downName)) {
                var nowVersion = BuildConfig.VERSION_CODE
                when {
                    //强制升级
                    nowVersion < lastVersion -> {
                        update = true
                        updateAppDialog.setUpdateText(PjjApplication.apkPath + downName, dataBean.info, true)
                    }
                    //提示升级
                    nowVersion < versionInt -> updateAppDialog.setUpdateText(PjjApplication.apkPath + downName, dataBean.info)
                }

            }
        }
    }

    private var updateAppFilePath: String? = null

    override fun installApk(filePath: String) {
        runOnUiThread {
            if (updateAppDialog.isShowing) {
                updateAppDialog.dismiss()
            }
            updateAppFilePath = filePath
            var installApk = AppUpdate.installApk(this, filePath)
            if (!installApk) {
                if (AppUpdate.errorCode == 1) {
                    //请求安装未知应用来源的权限
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.REQUEST_INSTALL_PACKAGES), INSTALL_PACKAGES_REQUESTCODE)
                    }
                    Log.e("TAG", "请求安装未知应用来源的权限: ")
                }
            }
        }
    }

    private fun setJPushAlias() {
        var token = SharedUtils.getXmlForKey(SharedUtils.TOKEN)
        var alias = TextUtils.md5(token)
        if (alias.isNotEmpty() && alias != SharedUtils.getXmlForKey(SharedUtils.JPUSH_ALIAS)) {
            val action = TagAliasOperatorHelper.ACTION_SET
            val tagAliasBeanAlias = TagAliasOperatorHelper.TagAliasBean()
            tagAliasBeanAlias.action = action
            TagAliasOperatorHelper.sequence++
            tagAliasBeanAlias.alias = alias
            tagAliasBeanAlias.isAliasAction = true
            TagAliasOperatorHelper.getInstance().handleAction(applicationContext, TagAliasOperatorHelper.sequence, tagAliasBeanAlias)
        }
    }

    override fun onDestroy() {
        if (updateAppDialog.isShowing) {
            updateAppDialog.dismiss()
        }
        super.onDestroy()
    }

    private fun initMainDrawableTop() {
        setMainDrawableTop(rb_home, ViewUtils.getDrawable(R.drawable.selector_main_home_btn_home).apply {
            setBounds(0, 0, ViewUtils.getDp(R.dimen.dp_23), ViewUtils.getDp(R.dimen.dp_20))
        }, R.dimen.dp_3)
        setMainDrawableTop(rb_works, ViewUtils.getDrawable(R.drawable.selector_etemplate_btn_home).apply {
            setBounds(0, 0, ViewUtils.getDp(R.dimen.dp_18), ViewUtils.getDp(R.dimen.dp_19))
        }, R.dimen.dp_4)
        setMainDrawableTop(rb_order, ViewUtils.getDrawable(R.drawable.selector_main_order_btn_home).apply {
            setBounds(0, 0, ViewUtils.getDp(R.dimen.dp_20), ViewUtils.getDp(R.dimen.dp_17))
        }, R.dimen.dp_4)
        setMainDrawableTop(rb_mine, ViewUtils.getDrawable(R.drawable.selector_mine_btn).apply {
            setBounds(0, 0, ViewUtils.getDp(R.dimen.dp_16), ViewUtils.getDp(R.dimen.dp_20))
        }, R.dimen.dp_3)
    }

    private fun setMainDrawableTop(rb: RadioButton, drawable: Drawable, id: Int) {
        rb.setCompoundDrawables(null, drawable, null, null)
        rb.compoundDrawablePadding = ViewUtils.getDp(id)
    }

    private fun checkLogin(): Boolean {
        if (SharedUtils.checkLogin()) {
            return true
        }
        startActivity(Intent(this, LoginActivity::class.java))
        return false
    }

    override fun getParentHeight(): Int {
        return parentHeight
    }
}
