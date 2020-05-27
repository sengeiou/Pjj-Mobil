package com.pjj.view.fragment

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.*
import android.widget.TextView
import com.pjj.R
import com.pjj.PjjApplication
import com.pjj.contract.OrderStatueContract
import com.pjj.module.OrderResultBean
import com.pjj.module.parameters.OrderStatue
import com.pjj.module.xspad.PayManage
import com.pjj.module.xspad.XspManage
import com.pjj.present.OrderStatuePresent
import com.pjj.utils.*
import com.pjj.utils.Log.e
import com.pjj.view.activity.*
import com.pjj.view.adapter.*
import com.pjj.view.dialog.*
import com.pjj.view.pulltorefresh.BaseRefreshListener
import com.pjj.view.pulltorefresh.PullToRefreshLayout
import kotlinx.android.synthetic.main.layout_list_view.*

/**
 * Created by XinHeng on 2018/12/07.
 * describe：订单
 */
class OrderFragment : BaseFragment<OrderStatuePresent>(), OrderStatueContract.View {
    companion object {
        /**
         * 0 待审核/未处理  1 审核未通过  2 审核通过  3 已完成  4 审核过期  5 紧急撤销  6 未支付  7 支付过期 8主动取消(已付款,但未审核时用户主动取消)
         */
        val ORDER_TYPE = "order_type"
        val WAITE_PAY = 6.toString()
        val AUDIT = 0.toString()
        val RELEASE = 2.toString()
        //val COMPLETE = "1,3,4,5,7,8"
        val COMPLETE = "3"

        @JvmStatic
        fun newInstance(orderType: String): OrderFragment = OrderFragment().apply {
            arguments = Bundle().apply {
                putString(ORDER_TYPE, orderType)
            }
        }
    }

    private lateinit var pullRefreshHelper: PullRefreshHelper<OrderResultBean.DataBean>
    private lateinit var noData: String
    private lateinit var orderType: String
    private var updateData = false
    private var adapter: OrderAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderType = arguments!!.getString(ORDER_TYPE)
    }

    private var noDataView: View? = null
    override fun uploadList(list: MutableList<OrderResultBean.DataBean>?) {
        pullRefreshHelper.updateResult(list)
        //pullToRefreshLayout?.finishRefresh()
        //adapter?.list = list
        val itemCount = adapter?.itemCount
        if (itemCount == 0) {
            if (null == noDataView) {
                noDataView = id_no_data.inflate()
            } else {
                noDataView?.visibility = View.VISIBLE
            }
            var textView = noDataView!!.findViewById<TextView>(R.id.tv_text)
            textView.text = noData
        } else {
            noDataView?.visibility = View.GONE
        }
    }

    override fun showNotice(notice: String?) {
        super.showNotice(notice)
        pullToRefreshLayout?.finishRefresh()
    }

    private lateinit var recyclerView: RecyclerView
    private var pullToRefreshLayout: PullToRefreshLayout? = null
    override fun getLayoutRes(): Int {
        return R.layout.layout_list_view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pullToRefreshLayout = view.findViewById(R.id.pullToRefresh)
        pullToRefreshLayout!!.setCanLoadMore(true)
        recyclerView = pullToRefreshLayout!!.findViewById(R.id.rv_order)
        val dp10 = ViewUtils.getDp(R.dimen.dp_10)
        recyclerView.setPadding(dp10 / 2, dp10 / 2, dp10 / 2, 0)
        pullRefreshHelper = PullRefreshHelper(pullToRefreshLayout!!)
        pullRefreshHelper.onPullRefreshHelperListener = object : PullRefreshHelper.OnPullRefreshHelperListener<OrderResultBean.DataBean> {
            override fun loadData(start: Int, num: Int) {
                val orderStatue = OrderStatue(PjjApplication.application.userId, orderType)
                orderStatue.pageNo = start
                orderStatue.pageNum = num
                mPresent?.loadOrderStatueTask(orderStatue)
            }

            override fun iniList(list: MutableList<OrderResultBean.DataBean>?) {
                adapter?.list = list
            }

            override fun addList(list: MutableList<OrderResultBean.DataBean>) {
                adapter?.addMore(list)
                recyclerView.postDelayed({
                    recyclerView.smoothScrollBy(0, ViewUtils.getDp(R.dimen.dp_60))
                }, 200)
            }

        }
        pullToRefreshLayout!!.setRefreshListener(object : BaseRefreshListener {
            override fun loadMore() {
                pullRefreshHelper.loadMore()
            }

            override fun refresh() {
                pullRefreshHelper.refresh()
            }
        })
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = getAdapter()?.apply {
            adapter = this
            onItemClickListener = object : OrderAdapter.OnItemClickListener {
                override fun rightClick(orderId: String, type: String, isPlay: String) {
                    startActivity(orderId, type, isPlay)
                }

                override fun leftClick() {

                }
            }
        }
        recyclerView.addItemDecoration(SpaceItemDecoration(activity, LinearLayoutManager.VERTICAL, ViewUtils.getDp(R.dimen.dp_10), ViewUtils.getColor(R.color.color_f1f1f1)))
        mPresent = OrderStatuePresent(this).apply {
            updateData = false
        }
        pullRefreshHelper.refresh()
    }

    private var payType = -1
    private val payDialogHelp: PayDialogHelp by lazy {
        PayDialogHelp(activity!!, object : PayDialogHelp.OnPayListener {
            override fun showNotice(msg: String) {
                this@OrderFragment.showNotice(msg)
            }

            override fun makeOrder(payType: Int) {
                PayManage.getInstance().payDialogHelp = payDialogHelp
                this@OrderFragment.payType = payType
                selectPayType(payDialogHelp.orderId_!!, payType)
            }

            override fun reLoadPay(payType: Int, orderId: String) {
                PayManage.getInstance().payDialogHelp = payDialogHelp
                this@OrderFragment.payType = payType
                selectPayType(orderId, payType)
            }

            override fun startOrderView(index: Int) {
                cancelWaiteStatue()
                if (index > 0)
                    startActivity(Intent(activity!!, MainActivity::class.java).putExtra("index", index))
            }

            override fun loadAliPayResult(orderId: String) {
                mPresent?.loadPayResultTask(orderId)
            }

            override fun loadWeiXinPayResult(orderId: String) {
                mPresent?.loadPayResultTask(orderId)
            }

            override fun loadYinHangPayResult(orderId: String) {
            }

            override fun paySuccess(payType: Int) {
                cancelWaiteStatue()
//                mPresent?.loadOrderStatueTask(OrderStatue(PjjApplication.application.userId, orderType))
                pullRefreshHelper.refresh()
            }

        })
    }
    private val cancelOrderDialog: CancelOrderDialog by lazy {
        CancelOrderDialog(activity!!).apply {
            onCancelOrderListener = object : CancelOrderDialog.OnCancelOrderListener {
                override fun showNotice(msg: String) {
                    this@OrderFragment.showNotice(msg)
                }

                override fun sureCancel(msg: String, orderId: String) {
                    mPresent?.cancelOrder(orderId, msg)
                }

                override fun selectCancelReason() {
                    cancelReasonDialog.show()
                }
            }
        }
    }
    private val cancelReasonDialog: BottomListTextDialog by lazy {
        BottomListTextDialog(activity!!).apply {
            onSelectListener = object : BottomListTextDialog.OnSelectListener {
                override fun itemSelect(msg: String) {
                    cancelOrderDialog.updateCancelReason(msg)
                }
            }
        }
    }
    private var orderOrderType: String? = null
    private var completeTag = false
    private fun getAdapter(): OrderAdapter? {
        return activity?.let {
            when (orderType) {
                WAITE_PAY -> {
                    completeTag = false
                    noData = "当前无待支付订单~"
                    OrderWaitePayAdapter(it).apply {
                        onWaitePayListener = object : OrderWaitePayAdapter.OnWaitePayListener {
                            override fun pay(orderId: String, price: String?, orderType: String?) {
                                payDialogHelp.orderId_ = orderId
                                orderOrderType = orderType
                                payDialogHelp.showPayTypeDialog(price, false)
                            }
                        }
                    }
                }
                AUDIT -> {
                    noData = "当前无审核中订单~"
                    completeTag = false
                    OrderAuditAdapter(it).apply {
                        onAdapterAudiListener = object : OrderAuditAdapter.OnAdapterAudiListener {
                            override fun cancelOrder(orderId: String, orderPrice: String) {
                                cancelOrderDialog.setPrice(orderId, orderPrice)
                            }
                        }
                    }
                }
                RELEASE -> {
                    noData = "当前无发布中订单~"
                    completeTag = false
                    OrderReleaseAdapter(it).apply {
                        adapterAskNetWorkListener = object : OrderReleaseAdapter.AdapterAskNetWorkListener {
                            override fun noPlay() {
                                this@OrderFragment.noPlay()
                            }

                            override fun loadIsPlayTask(orderId: String, screenId: String, zhiBoUrl: String?) {
                                mPresent?.loadIsPlayTask(orderId, screenId, zhiBoUrl)
                            }

                            override fun toNewMediaPager(orderId: String, screenId: String) {
                                startActivity(Intent(activity!!, ScreenshotsListActivity::class.java).putExtra("orderId", orderId).putExtra("screenId", screenId))
                            }

                            override fun screenshots(orderId: String, screenId: String) {
                                startActivity(Intent(activity!!, ScreenshotsListActivity::class.java).putExtra("orderId", orderId).putExtra("screenId", screenId))
                            }

                            override fun tuiGuang(first: String, second: String?, communityNum: String, releaseTime: String, createTime: String, orderId: String) {
                                startActivity(Intent(activity, TuiGuangActivity::class.java)
                                        .putExtra("from", orderType)
                                        .putExtra("first", first)
                                        .putExtra("second", second)
                                        .putExtra("communityNum", communityNum)
                                        .putExtra("releaseTime", releaseTime)
                                        .putExtra("createTime", createTime)
                                        .putExtra("orderId", orderId)
                                )
                            }
                        }
                    }
                }
                else -> {
                    completeTag = true
                    noData = "当前无已结束订单~"
                    OrderCompleteAdapter(it).apply {
                        onOrderCompleteListener = object : OrderCompleteAdapter.OnOrderCompleteListener {
                            override fun notice(msg: String) {
                                showNotice(msg)
                            }

                            override fun reRelease(orderId: String, adType: String) {
                                mPresent?.loadOrderInfTask(orderId, adType)
                            }

                            override fun tuiGuang(first: String, second: String?, communityNum: String, releaseTime: String, createTime: String, orderId: String) {
                                startActivity(Intent(activity, TuiGuangActivity::class.java)
                                        .putExtra("from", orderType)
                                        .putExtra("first", first)
                                        .putExtra("second", second)
                                        .putExtra("communityNum", communityNum)
                                        .putExtra("releaseTime", releaseTime)
                                        .putExtra("createTime", createTime)
                                        .putExtra("orderId", orderId)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun startActivity(orderId: String, type: String, isPlay: String) {
        if (type == "7" || type == "9") {
            startActivity(Intent(activity, OrderMediaInfActivity::class.java)
                    .putExtra("isPlay", isPlay)
                    .putExtra("orderId", orderId)
                    .putExtra("completeTag", completeTag))
            return
        }
        startActivity(Intent(activity, OrderInfActivity::class.java).putExtra("orderId", orderId).putExtra("type", type))
    }

    override fun allowNext(tag: String, templateId: String?, msg: String) {
        var adType = XspManage.getInstance().adType
        var identityType = XspManage.getInstance().identityType
        when (tag) {
            "-14" -> {
                noticeDialog.setNotice("暂无审核通过模板\n请耐心等待审核", 4000)
                return
            }
            "-13" -> {
                XspManage.getInstance().newMediaData.releaseTag = true
                startActivity(Intent(activity!!, TemplateListActivity::class.java)
                        .putExtra("title_text", (if (adType == 9) "传统" else "新媒体") + (if (identityType == 1) "个人" else "商家") + "模板")
                        .putExtra("identity_type", identityType.toString())
                        .putExtra("ad_type", adType)
                        .putExtra("templateId", templateId)
                        .putExtra("releaseTag", true))
                return
            }
        }
    }

    override fun noPlay() {
        noPlayElevatorDialog.show(2000)
    }

    override fun play(path: String?) {
        if (path == null || path == "#") {
            showNotice("该功能暂未开放\n敬请期待！")
            return
        }
        //VideoPlayerActivity.intentTo(activity!!, PjjApplication.VIDOE_PATH)
        VideoPlayerActivity.intentTo(activity!!, path)
    }

    /**
     * 无监播弹窗
     */
    private val noPlayElevatorDialog: NoticeDialog by lazy {
        NoticeDialog(activity!!, R.mipmap.cry_white)
    }

    override fun payCipherSuccess(orderInfo: String, orderId: String) {
        payDialogHelp.payForOrderInfo(orderInfo, payType)
    }

    override fun payResult(result: Boolean, msg: String?) {
        payDialogHelp.setPayResult(result, msg)
    }


    override fun updateTemplateId(tag: Boolean) {
        cancelWaiteStatue()
        e("TAG", "XspManage.getInstance().templateId : " + XspManage.getInstance().templateId)
        if (tag) {
            if (!TextUtils.isNotEmptyList(XspManage.getInstance().groupLocation)) {
                showNotice("无广告屏")
                return
            }
            startActivity(Intent(activity!!, TimeActivity::class.java))
        } else {
            if (!TextUtils.isNotEmptyList(XspManage.getInstance().buildList)) {
                showNotice("无广告屏")
                return
            }
            if (XspManage.getInstance().bianMinPing == 1) {
                SelectBMPingTemplateActivity.start(activity!!, "")
            } else {
                SelectRandomTemplateActivity.start(activity!!, "")
            }

        }
    }

    private fun selectPayType(oriderId: String, type: Int) {
        when (type) {
            PayOrderDialog.PAY_ZHIFUBAO -> mPresent?.loadAliPay(oriderId)
            PayOrderDialog.PAY_WEIXIN -> mPresent?.loadWeiXinPay(oriderId)
            else -> {

            }
        }
    }

    override fun cancelOrder(tag: Boolean, msg: String?) {
        if (tag) {
            //刷新
            //mPresent?.loadOrderStatueTask(OrderStatue(PjjApplication.application.userId, orderType))
            pullRefreshHelper.refresh()
            cancelOrderDialog.dismiss()
            noticeDialog.updateImage(R.mipmap.date_select, true)
            showNotice("取消订单成功")
        } else {
            noticeDialog.updateImage(R.mipmap.close_white)
            showNotice("取消订单失败\n$msg")
        }
    }

    fun needUpdate() {
        e("TAG", "needUpdate:userVisibleHint=$userVisibleHint ")
        if (null == pullToRefreshLayout) {
            updateData = true
        } else {
            update()
        }
    }

    private fun update() {
        //Log.e("TAG", "update: autoRefresh")
        updateData = false
        pullToRefreshLayout?.autoRefresh()
    }

    override fun onDetach() {
        PayManage.getInstance().clear()
        super.onDetach()
    }

    //认证
    private val shenHeDialog: ImageNoticeDialog by lazy {
        ImageNoticeDialog(activity!!)
    }
    private val noPassDialog: NoPassDialog by lazy {
        NoPassDialog(activity!!).apply {
            onItemClickListener = object : NoPassDialog.OnItemClickListener {
                override fun go(msg: String) {
                    goRenZheng()
                }
            }
        }
    }

    private fun goRenZheng() {
        var intent = Intent(activity!!, CertificationActivity::class.java)
        intent.putExtra("default", XspManage.getInstance().identityType.toString())
        startActivity(intent)
    }

    override fun certificationResult(tag: String, msg: String?) {
        if (tag == "1") {
            shenHeDialog.notice = "您的认证资料正在审核中，\n请耐心等待！"
            shenHeDialog.show()
        } else if (tag == "2") {
            noPassDialog.showLeft()
            noPassDialog.errorText = msg
        } else {
            goRenZheng()
        }
    }

    override fun onResume() {
        super.onResume()
        if (XspManage.getInstance().refreshTag.tagOrderListFragment == orderType) {
            XspManage.getInstance().refreshTag.tagOrderListFragment = null
            pullRefreshHelper.refresh()
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden && XspManage.getInstance().refreshTag.tagOrderListFragment == orderType) {
            XspManage.getInstance().refreshTag.tagOrderListFragment = null
            pullRefreshHelper.refresh()
        }
    }
}