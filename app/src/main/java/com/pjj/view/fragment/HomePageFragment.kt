package com.pjj.view.fragment

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.provider.Settings
import android.support.constraint.ConstraintLayout
import android.support.v4.view.ViewPager
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.bumptech.glide.Glide
import com.pjj.BuildConfig
import com.pjj.PjjApplication

import com.pjj.R
import com.pjj.contract.HomePageContract
import com.pjj.module.PicBean
import com.pjj.module.UserTempletBean
import com.pjj.module.parameters.ZhiDing
import com.pjj.module.xspad.XspManage
import com.pjj.present.HomePagePresent
import com.pjj.utils.*
import com.pjj.view.activity.*
import com.pjj.view.adapter.HomeMidAdapter
import com.pjj.view.adapter.ZhiDingAdapter
import com.pjj.view.custom.GuideViewDialog
import com.pjj.view.dialog.CallPhoneDialog
import com.pjj.view.dialog.ZhiDingPreDialog
import com.pjj.view.pulltorefresh.BaseRefreshListener
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import com.youth.banner.loader.ImageLoader
import kotlinx.android.synthetic.main.fragment_homepage.*

/**
 * Create by xinheng on 2018/11/07 16:38。
 * describe：首页
 */
class HomePageFragment : ABFragment<HomePagePresent>(), HomePageContract.View {
    private lateinit var iv_mall_goods: ImageView
    private lateinit var tv_local_city: TextView
    //private lateinit var iv_mall_goods:ImageView
    override fun getLayoutRes(): Int {
        return R.layout.fragment_homepage
    }

    private var tvWeather: TextView? = null
    private val gpsDialog: CallPhoneDialog by lazy {
        CallPhoneDialog(activity!!).apply {
            rightText = "去设置"
            phone = "请在设置中打开定位\n以确定您的位置"
            onCallPhoneDialogListener = object : CallPhoneDialog.OnCallPhoneDialogListener {
                override fun callClick() {
                    startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 3000)
                }
            }
        }
    }
    private val preDialog: ZhiDingPreDialog by lazy {
        ZhiDingPreDialog(activity!!)
    }
    private var height: Int = 0
    private var localCity: String = ""
    //private lateinit var parentScroll: MyScrollView
    private lateinit var viewRv: RecyclerView
    private lateinit var adapterRv: HomeMidAdapter
    private lateinit var parentConstraintLayout: ConstraintLayout
    private val guideViewDialog: GuideViewDialog by lazy {
        GuideViewDialog(activity!!).apply {
            onGuideListener = object : GuideViewDialog.OnGuideListener {
                override fun guide(index: Int) {
                    guideChildViewLocation(index)
                }
            }
        }
    }
    private var statueBarTag = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val statueHeight = PjjApplication.application.statueHeight
        iv_mall_goods = view.findViewById(R.id.iv_mall_goods)
        tv_local_city = view.findViewById(R.id.tv_local_city)
        if (statueHeight > 0) {
            val layoutParams = tv_search_scroll.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.topMargin = statueHeight + ViewUtils.getDp(R.dimen.dp_44)
            tv_search_scroll.layoutParams = layoutParams
            fl_top.setPadding(fl_top.paddingLeft, statueHeight + ViewUtils.getDp(R.dimen.dp_6), fl_top.paddingRight, fl_top.paddingBottom)
        }
        //FileUtils.saveStringFile(File(PjjApplication.App_Path + "phone_statue.txt").absolutePath, "statueHeight=$statueHeight")
        mPresent = HomePagePresent(this).apply {
            loadHomePicTask("1,2,3")
        }
        tvWeather = tv_weather
        initBanner()
        view_rv.run {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            adapter = HomeMidAdapter(activity as Context).apply {
                adapterRv = this
                onHomeMidListener = object : HomeMidAdapter.OnHomeMidListener {
                    override fun itemClick(title: String, linkUrl: String) {
                        toWebViewActivity(linkUrl, title, "3")
                    }
                }
                list = ArrayList<PicBean.DataBean.`_$1Bean`>(2).apply {
                    add(PicBean.DataBean.`_$1Bean`().apply {
                        fileName = "file:///android_asset/main1.png"
                        pictrueSort = "1"
                        pictureName = "1"
                        linkUrl = "#"
                    })
                    add(PicBean.DataBean.`_$1Bean`().apply {
                        fileName = "file:///android_asset/main2.png"
                        pictrueSort = "2"
                        pictureName = "2"
                        linkUrl = "#"
                    })
                }
            }
        }
        iv_difangziying.setOnClickListener(onClick)
        iv_div_media.setOnClickListener(onClick)
        view_mall.setOnClickListener(onClick)
        //iv_elevator_media.setOnClickListener(onClick)
        iv_free_release_media.setOnClickListener(onClick)
        xxtf.setOnClickListener(onClick)
        tv_local_city.setOnClickListener(onClick)
        about_me.setOnClickListener(onClick)
        view_yixue.setOnClickListener(onClick)
        tv_search_scroll.setOnClickListener(onClick)
        view_search.setOnClickListener(onClick)
//        iv_click.setOnClickListener(onClick)
        view_old_people.setOnClickListener(onClick)
        height = resources.displayMetrics.heightPixels
        viewRv = view.findViewById<RecyclerView>(R.id.view_rv)
        parentConstraintLayout = view.findViewById<ConstraintLayout>(R.id.parent_constraintLayout)
        var guide = SharedUtils.getXmlForKey(SharedUtils.NEW_GUIDE)
        //Log.e("TAG", "111 ${parentConstraintLayout.measuredHeight} ${parentConstraintLayout.height}")
        /*parentConstraintLayout.post {
            Log.e("TAG", "111 ${parentScroll.measuredHeight} ${containerTitle.height} ${(containerTitle.parent as LinearLayout).measuredHeight} ${ViewUtils.getDp(R.dimen.dp_48)}")
        }*/
        /*if (BuildConfig.VERSION_NAME != guide) {
            parent_scroll.post {
                guideViewDialog.showDialog((parent_scroll.parent as LinearLayout).measuredHeight + ViewUtils.getDp(R.dimen.dp_48))
                guideViewDialog.setOnCancelListener {
                    preStartLocation()
                }
            }
        } else {
            preStartLocation()
        }*/
        initRecycleView()
        rv_main.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var scrollY = 0
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                scrollY += dy
                val slide = ViewUtils.getDp(R.dimen.dp_39)
                if (scrollY >= slide) {
                    this@HomePageFragment.fl_top.visibility = View.VISIBLE
                    setStatueBar(true)
                    statueBarTag = true
                } else {
                    this@HomePageFragment.fl_top.visibility = View.GONE
                    setStatueBar(false)
                    statueBarTag = false
                }
            }
        })
        preStartLocation()
    }

    private fun toWebViewActivity(linkUrl: String, title: String, from: String) {
        if (!TextUtils.isEmpty(linkUrl)) {
            if ("#" != linkUrl)
                WebActivity.newInstance(activity!!, title, linkUrl, from)
        } else {
            //showNotice("链接无效")
        }
    }

    private val zhiDing = ZhiDing()
    private lateinit var zhiDingAdapter: ZhiDingAdapter
    private lateinit var pullRefreshHelper: PullRefreshHelper<UserTempletBean.DataBean>
    private fun initRecycleView() {
        val gridLayoutManager = GridLayoutManager(activity, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (this@HomePageFragment.rv_main.adapter.getItemViewType(position)) {
                    0, 1 -> 1
                    else -> 2
                }
            }
            /*override fun getSpanIndex(position: Int, spanCount: Int): Int {
                return position % spanCount
            }*/
        }
        rv_main.layoutManager = gridLayoutManager
        zhiDingAdapter = ZhiDingAdapter(activity!!, true).apply {
            onZhiDingItemClickListener = object : ZhiDingAdapter.OnZhiDingItemClickListener {
                override fun itemClick(bean: UserTempletBean.DataBean) {
                    if (bean.templetType == "6") {
                        WebActivity.newInstance(activity!!, "外部视频链接", bean.urlContent)
                        return
                    }
                    if (TextUtils.isNotEmptyList(bean.fileList)) {
                        val two = bean.fileList.size == 2
                        if (two) {
                            XspManage.getInstance().newMediaData.preTowData = bean.speedDataBean
                        }
                        val fileBean = bean.fileList[0]
                        PreviewDialogActivity.startActivity(activity!!, fileBean.fileUrl, fileBean.type, bean.templetName, two)
                    }
                }
            }
        }
        rv_main.adapter = zhiDingAdapter
        rv_main.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
                //super.getItemOffsets(outRect, view, parent, state)
                val position = parent.getChildLayoutPosition(view) - 1
                when {
                    position == -1 -> {
                        outRect.left = 0
                    }
                    position % 2 == 0 -> outRect.left = ViewUtils.getDp(R.dimen.dp_17)
                    else -> outRect.left = ViewUtils.getDp(R.dimen.dp_8)
                }
            }
        })
        pullRefreshHelper = PullRefreshHelper(pullToRefresh)
        //pullToRefresh.setCanRefresh(false)
        pullRefreshHelper.onPullRefreshHelperListener = object : PullRefreshHelper.OnPullRefreshHelperListener<UserTempletBean.DataBean> {
            override fun loadData(start: Int, num: Int) {
                zhiDing.pageNo = start
                zhiDing.pageNum = num
                mPresent!!.loadZhiDingSearchTask(zhiDing)
            }

            override fun iniList(list: MutableList<UserTempletBean.DataBean>?) {
                zhiDingAdapter.list = list
            }

            override fun addList(list: MutableList<UserTempletBean.DataBean>) {
                zhiDingAdapter.addMore(list)
                rv_main.postDelayed({
                    rv_main.smoothScrollBy(0, ViewUtils.getDp(R.dimen.dp_30))
                }, 200)
            }
        }
        val topView = parent_constraintLayout
        val viewGroup = topView.parent as ViewGroup
        viewGroup.removeView(topView)
        zhiDingAdapter.topView = topView
        zhiDingAdapter.notifyDataSetChanged()
        pullRefreshHelper.refresh()
    }

    /**
     * 顶部轮播图
     */
    private fun initBanner() {
        val list_path = ArrayList<String>()
        val list_title = ArrayList<String>()
        list_path.add("file:///android_asset/top_head1.png")
        list_path.add("file:///android_asset/top_head2.png")
        list_path.add("file:///android_asset/top_head3.png")

        list_title.add("")
        list_title.add("")
        list_title.add("")
        banner.setBannerStyle(BannerConfig.NOT_INDICATOR)
        banner.setImageLoader(object : ImageLoader() {
            override fun displayImage(context: Context, path: Any, imageView: ImageView) {
                Glide.with(context).load(path as String).into(imageView)
            }
        })
        banner.setOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(p0: Int) {
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
                activity?.let {
                    if (!it.isFinishing) {
                        it.runOnUiThread {
                            this@HomePageFragment.indicator?.selectIndex(p0)
                        }
                    }
                }
            }
        })
        //设置指示器的位置，小点点，左中右。
        banner.setIndicatorGravity(BannerConfig.CENTER)
        //设置图片网址或地址的集合
        banner.setImages(list_path)
        //设置轮播的动画效果，内含多种特效，可点入方法内查找后内逐一体验
        banner.setBannerAnimation(Transformer.Default)
        //设置轮播图的标题集合
        banner.setBannerTitles(list_title)
        //设置轮播间隔时间
        banner.setDelayTime(4000)
        //设置是否为自动轮播，默认是“是”。
        banner.isAutoPlay(true)

        //以上内容都可写成链式布局，这是轮播图的监听。比较重要。方法在下面。
        banner.setOnBannerListener { }
        //必须最后调用的方法，启动轮播图。
        banner.start()
    }

    private var onClick = View.OnClickListener {
        when (it.id) {
            R.id.view_search, R.id.tv_search_scroll -> {
                startActivity(Intent(activity!!, ZhiDingActivity::class.java))
            }
            R.id.tv_local_city -> {
                if (!GpsUtils.isOPen(activity)) {
                    gpsAddTag = false
                    if (!gpsDialog.isShowing)
                        gpsDialog.show()
                    return@OnClickListener
                }
                startActivityForResult(Intent(activity!!, CitiesActivity::class.java).putExtra("now_city", localCity), 200)
            }
            R.id.iv_difangziying -> onFragmentInteractionListener?.showADDialog(2)
            R.id.iv_div_media -> {
                if (checkLogin()) {
                    XspManage.getInstance().newMediaData.releaseTag = true
                    XspManage.getInstance().adType = 7
                    startActivity(Intent(activity!!, ReleaseRuleActivity::class.java).putExtra("title", "广告传媒发布规则"))
                    //onFragmentInteractionListener?.showADDialog(7)
                }
            }//presenter.onTKClicked()//填空信息发布
            R.id.view_mall -> startActivity(Intent(activity, MallHomepageActivity::class.java))
            //R.id.view_mall -> startActivity(Intent(activity, IntegralMallActivity::class.java))
            R.id.iv_free_release_media -> {
                if (checkLogin()) startActivity(Intent(activity, FreeReleaseSelectActivity::class.java))
            }
            R.id.iv_elevator_media -> {
                if (checkLogin()) toElevator()
            }//presenter.toElevator()//便民信息发布
            //无需登录
            R.id.xxtf -> toInformation()//指导手册
            R.id.about_me -> toAboutSelfView()//关于我们
            R.id.view_yixue -> startActivity(Intent(activity, WebActivity::class.java))
            //R.id.tv_notice, R.id.tv_notice1, R.id.iv_click -> WebActivity.newInstance(activity!!, "全国区域独家代理", PjjApplication.AD_JOIN, "4")
            R.id.view_old_people -> {
                //onFragmentInteractionListener?.showADDialog(77)
                //startActivity(Intent(activity, JianKangActivity::class.java))
                val useType = SharedUtils.getXmlForKey(SharedUtils.USER_TYPE)
                if (TextUtils.isEmpty(useType) || ("companyLaolingwei" != useType && "subManagement" != useType)) {
                    showNotice("您不是国家老龄委单位\n请移驾到广告传媒发布")
                    return@OnClickListener
                }
                if (checkLogin()) {
                    if ("subManagement" == useType) {
                        startActivity(Intent(activity!!, ScreenManageActivity::class.java))
                        //startActivity(Intent(activity!!, ManageBothActivity::class.java))
                    } else {
                        startActivity(Intent(activity!!, ManageBothActivity::class.java))
                    }
                }
            }
        }
    }

    private fun onDIYClicked() {
        onFragmentInteractionListener?.showADDialog(1)
    }

    private fun toElevator() {
        //onFragmentInteractionListener?.showADDialog(2)
//        onFragmentInteractionListener?.showADDialog(-2)
        XspManage.getInstance().adType = 9
        XspManage.getInstance().newMediaData.releaseTag = true
        startActivity(Intent(activity!!, ReleaseRuleActivity::class.java).putExtra("title", "电梯传媒发布规则"))
    }

    override fun toSelectLocationView() {
        startActivity(Intent(activity, SelectReleaseAreaActivity::class.java))
    }

    override fun toAboutSelfView() {
        startActivity(Intent(activity!!, AboutSelfActivity::class.java))
    }

    override fun toInformation() {
        WebActivity.newInstance(activity!!, "指导手册", "http://protal.test.pingjiajia.cn/guide", "1")
    }

    override fun updateHomePic(bean: PicBean, pictrueType: String) {
        var data = bean.data
        data?.let {
            var list1 = it.`_$1`
            var list2 = it.`_$2`
            updateTopImages(list1)
            updateMiddle(list2)
            if (TextUtils.isNotEmptyList(it.`_$3`)) {
                it.`_$3`[0]?.let { item ->
                    Glide.with(this@HomePageFragment).load(PjjApplication.filePath + item.fileName).into(this@HomePageFragment.iv_mall_goods)
                }
            }
        }
    }

    override fun updateHomePicFail(error: String?) {
        showNotice(error)
    }

    private fun updateTopImages(list: MutableList<PicBean.DataBean.`_$1Bean`>?) {
        if (TextUtils.isNotEmptyList(list)) {
            list?.let { it ->
                val list_path = ArrayList<String>(list.size)
                val list_title = ArrayList<String>(list.size)
                it.forEach { index ->
                    list_path.add(PjjApplication.filePath + index.fileName)
                    list_title.add("")
                }
                indicator.setChildCounts(list_path.size)
                banner.update(list_path, list_title)
                banner.setOnBannerListener { position ->
                    var dataBean = it[position]
                    var linkUrl = dataBean.linkUrl
                    toWebViewActivity(linkUrl, dataBean.pictureName, "2")
                }
            }
        }
    }

    private fun updateMiddle(list: MutableList<PicBean.DataBean.`_$1Bean`>?) {
        if (TextUtils.isNotEmptyList(list)) {
            adapterRv.list = list
        }
    }

    private var dp2 = ViewUtils.getDp(R.dimen.dp_5)
    private fun guideChildViewLocation(index: Int) {
        var emptyViewTop = -1
        var emptyViewHeight = 0
        var intArray = IntArray(2)
        when (index) {
            0 -> {
            }
            1 -> {
                emptyViewHeight = banner.measuredHeight + dp2
                emptyViewTop = dp2 / 2
            }
//            2 -> {
//                ll_notice.getLocationOnScreen(intArray)
//                emptyViewTop = intArray[1]
//                emptyViewHeight = ll_notice.measuredHeight
//            }
            3 -> {
                line_ad.getLocationOnScreen(intArray)
                var bottom = intArray[1]
                //iv_elevator_media.getLocationOnScreen(intArray)
                // emptyViewTop = intArray[1] + iv_elevator_media.measuredHeight
                emptyViewHeight = bottom - emptyViewTop
                // var top = iv_elevator_media.top
                //Log.e("TAG", "iv_bm:top=$top ")
            }
            4 -> {
                line_ad.getLocationOnScreen(intArray)
                var scrollFront = intArray[1]
                var scrllLength = 0
                if (parent_constraintLayout.measuredHeight > height) {
                    scrllLength = parent_constraintLayout.measuredHeight //- parent_scroll.measuredHeight
                    viewRv.getLocationOnScreen(intArray)
                    //parentScroll.fullScroll(ScrollView.FOCUS_DOWN)//滚动到底部
                }
                line_ad.getLocationOnScreen(intArray)
                emptyViewTop = intArray[1]
                view_rv.getLocationOnScreen(intArray)
                var bottom = intArray[1]
                emptyViewHeight = bottom - emptyViewTop
                emptyViewTop = scrollFront - scrllLength
            }
            5 -> {
                emptyViewHeight = ViewUtils.getDp(R.dimen.dp_80)
            }
        }
        //Log.e("TAG", "guideChildViewLocation: ${onFragmentInteractionListener == null}")
        onFragmentInteractionListener?.showGuidStub(index)
        guideViewDialog.setStub(index, emptyViewHeight, emptyViewTop)
    }

    override fun onDetach() {
        if (guideViewDialog.isShowing) {
            guideViewDialog.dismiss()
        }
        super.onDetach()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 3000 && (resultCode == Activity.RESULT_CANCELED || resultCode == Activity.RESULT_OK)) {
            //com.pjj.utils.Log.e("TAG", "GPS RESULT  resultCode=$resultCode")
            preStartLocation()
        } else if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            //var areaCode = data?.getStringExtra("areaCode")
            XspManage.getInstance().cityNameWeather = data?.getStringExtra("cityName")
            data?.getStringExtra("areaName")?.let { areaName ->
                XspManage.getInstance().cityName = areaName
/*                localCity = areaName
                this@HomePageFragment.tv_local_city.text = areaName*/
                checkCityChange()
            }
        }
    }

    private var gpsAddTag = false
    private fun preStartLocation() {
        if (GpsUtils.isOPen(activity)) {
            gpsAddTag = true
            startLocation()
        } else {
            gpsAddTag = false
            if (!gpsDialog.isShowing)
                gpsDialog.show()
        }
    }

    private lateinit var mLocationClient: AMapLocationClient
    @SuppressLint("SetTextI18n")
    override fun updateWeather(temperature: String, quality: String, level: String, info: String) {
        if (tvWeather == null) {
            tvWeather = zhiDingAdapter.topView?.findViewById<TextView>(R.id.tv_weather)
        }
        if (null != this@HomePageFragment.tvWeather)
            if (TextUtils.isEmpty(quality)) {
                this@HomePageFragment.tvWeather!!.text = temperature
            } else {
                this@HomePageFragment.tvWeather!!.text = "空气$quality：$level | $info  $temperature"
            }
    }

    override fun onResume() {
        super.onResume()
        checkCityChange()
    }

    private fun checkCityChange() {
        val cityName = XspManage.getInstance().cityName
        if (nowTextCity != cityName && null != cityName) {
            updateCity(cityName)
            var cityNameWeather = XspManage.getInstance().cityNameWeather
            if (TextUtils.isEmpty(cityNameWeather)) {
                cityNameWeather = cityName
            }
            mPresent?.loadWeatherStatue(cityNameWeather)
        }
    }

    private var nowTextCity: String = ""
    private fun updateCity(cityName: String) {
        nowTextCity = cityName
        TextUtils.setMaxEms(tv_local_city, cityName, 4)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            //checkCityChange()
            if (statueBarTag)
                setStatueBar(true)
        } else {
            if (statueBarTag)
                setStatueBar(false)
        }
        super.onHiddenChanged(hidden)
    }

    private fun setStatueBar(tag: Boolean) {
        if (!StatusBarUtil.setStatusBarDarkTheme(activity, tag)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(activity, 0x55000000)
        }
    }

    override fun updateZhiDingData(list: MutableList<UserTempletBean.DataBean>?) {
        pullRefreshHelper.updateResult(list)
    }

    private fun startLocation() {
        //初始化定位
        mLocationClient = AMapLocationClient(PjjApplication.application)
        //初始化定位参数
        var mLocationOption = AMapLocationClientOption()
        //设置定位回调监听
        mLocationClient.setLocationListener {
            mLocationClient.stopLocation()
            mLocationClient.onDestroy()
            com.pjj.utils.Log.e("TAG", "amapLocation=$it")
            // mView.updateAreaName(it.city)
            //loadGuessListTask(it.adCode)
            localCity = it.city
            XspManage.getInstance().lng = it.longitude.toString()//经度
            XspManage.getInstance().lat = it.latitude.toString()//纬度
            XspManage.getInstance().cityName = it.city
            XspManage.getInstance().aotuLocalCity = it.city
            TextUtils.setMaxEms(this@HomePageFragment.tv_local_city, it.city, 4)
            XspManage.getInstance().cityNameWeather = it.city
            mPresent?.loadWeatherStatue(it.city)
        }
        //设置为高精度定位模式
        mLocationOption.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //设置定位参数
        mLocationClient.setLocationOption(mLocationOption)
        mLocationOption.interval = 1000
        // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        // 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用stopLocation()方法来取消定位请求
        // 在定位结束后，在合适的生命周期调用onDestroy()方法
        // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        mLocationClient!!.startLocation()//启动定位
    }
}
