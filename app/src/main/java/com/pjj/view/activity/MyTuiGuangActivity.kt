package com.pjj.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.pjj.PjjApplication

import com.pjj.R
import com.pjj.contract.MyTuiGuangContract
import com.pjj.contract.TuiGuangContract
import com.pjj.module.MyTuiGuangBean
import com.pjj.module.TopPriceBean
import com.pjj.module.UserTempletBean
import com.pjj.module.xspad.XspManage
import com.pjj.present.MyTuiGuangPresent
import com.pjj.present.TuiGuangPresent
import com.pjj.utils.*
import com.pjj.view.adapter.MyTuiGuangAdapter
import com.pjj.view.dialog.ErrorDialog
import com.pjj.view.dialog.MessageDialog
import com.pjj.view.dialog.TimeOutDialog
import com.pjj.view.pulltorefresh.BaseRefreshListener
import kotlinx.android.synthetic.main.activity_mytuiguang.*

/**
 * Create by xinheng on 2019/07/26 15:20。
 * describe：我的推广
 */
class MyTuiGuangActivity : PayActivity<MyTuiGuangPresent>(), MyTuiGuangContract.View {
    override fun updatePrice(datas: MutableList<TopPriceBean.DataBean>?) {
    }

    private var orderJson: String? = null
    private var priceFinal = -1f

    override fun getMakeOrderJson(): String {
        return orderJson!!
    }

    override fun getFinalPayPrice(): Float {
        return priceFinal
    }

    override fun payFinishStartOrderView(index: Int) {
        pullRefreshHelper.refresh()
    }

    override fun payCipherFai(msg: String) {
        timeOutDialog.show()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        pullRefreshHelper.refresh()
    }

    private lateinit var pullRefreshHelper: PullRefreshHelper<MyTuiGuangBean.DataBean>
    private lateinit var adaperTuiGuang: MyTuiGuangAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mytuiguang)
        setTitle("我的推广")
        mPresent = MyTuiGuangPresent(this)
        pullRefreshHelper = PullRefreshHelper(pullToRefresh)
        pullRefreshHelper.onPullRefreshHelperListener = object : PullRefreshHelper.OnPullRefreshHelperListener<MyTuiGuangBean.DataBean> {
            override fun loadData(start: Int, num: Int) {
                mPresent?.loadDataTask(start, num)
            }

            override fun iniList(list: MutableList<MyTuiGuangBean.DataBean>?) {
                adaperTuiGuang.list = list
            }

            override fun addList(list: MutableList<MyTuiGuangBean.DataBean>) {
                adaperTuiGuang.addMore(list)
                if (TextUtils.isNotEmptyList(list)) {
                    rv_mytuiguang.postDelayed({
                        rv_mytuiguang.smoothScrollBy(0, ViewUtils.getDp(R.dimen.dp_30))
                    }, 200)
                }
            }
        }
        pullToRefresh.setRefreshListener(object : BaseRefreshListener {
            override fun refresh() {
                pullRefreshHelper.refresh()
            }

            override fun loadMore() {
                pullRefreshHelper.loadMore()
            }
        })
        adaperTuiGuang = MyTuiGuangAdapter(this).apply {
            onMyTuiGuangAdapterListener = object : MyTuiGuangAdapter.OnMyTuiGuangAdapterListener {
                override fun pre(bean: MyTuiGuangBean.DataBean) {
                    val fileList = bean.templet.fileList
                    if (TextUtils.isNotEmptyList(fileList)) {
                        val two = fileList.size == 2
                        if (two) {
                            XspManage.getInstance().newMediaData.preTowData = UserTempletBean.DataBean.getSpeedDataBean(fileList)// bean.speedDataBean
                        }
                        val fileBean = fileList[0]
                        PreviewDialogActivity.startActivity(this@MyTuiGuangActivity, fileBean.fileUrl, fileBean.type, bean.templet.templetName, two)
                    }
                }

                override fun cancel(bean: MyTuiGuangBean.DataBean) {
                    topOrderId = bean.topOrderId
                    messageDialog.show()
                }

                override fun toPay(price: Float, orderId: String, topId: String, topOrderId: String?,
                                   first: String, second: String?, communityNum: String, releaseTime: Long, createTime: Long) {
                    priceFinal = price
                    this@MyTuiGuangActivity.first = first
                    this@MyTuiGuangActivity.second = second
                    this@MyTuiGuangActivity.communityNum = communityNum
                    this@MyTuiGuangActivity.releaseTime = releaseTime
                    this@MyTuiGuangActivity.createTime = createTime
                    this@MyTuiGuangActivity.orderId = orderId
                    if (TextUtils.isEmpty(topOrderId)) {
                        orderJson = "{\"orderId\":\"$orderId\",\"topId\":\"$topId\",\"userId\":\"${PjjApplication.application.userId}\"}"
                        makeOrder()
                    } else {
                        orderJson = null
                        payDialogHelp.orderId_ = topOrderId!!
                        payDialogHelp.showPayTypeDialog(CalculateUtils.m1(priceFinal), getZheKouTag())
                    }
                }

                override fun reLoadOrder(reNext: Boolean, first: String, second: String?, communityNum: String, releaseTime: Long, createTime: Long, orderId: String) {
                    reLoadMakeOrder(first, second, communityNum, releaseTime, createTime, orderId)
                }

                override fun showError(msg: String) {
                    errorDialog.showError(msg)
                }

            }
        }
        rv_mytuiguang.adapter = adaperTuiGuang
        rv_mytuiguang.layoutManager = LinearLayoutManager(this)
        rv_mytuiguang.addItemDecoration(SpaceItemDecoration(this, LinearLayoutManager.VERTICAL, ViewUtils.getDp(R.dimen.dp_10), ViewUtils.getColor(R.color.color_eeeeee)))
        pullRefreshHelper.refresh()
    }

    private lateinit var first: String
    private var second: String? = null
    private lateinit var communityNum: String
    private var releaseTime = 0L
    private var createTime = 0L
    private lateinit var orderId: String
    private fun reLoadMakeOrder(first: String, second: String?, communityNum: String, releaseTime: Long, createTime: Long, orderId: String) {
        startActivity(Intent(this, TuiGuangActivity::class.java)
                .putExtra("first", first)
                .putExtra("second", second)
                .putExtra("communityNum", communityNum)
                .putExtra("releaseTime", releaseTime)
                .putExtra("createTime", createTime)
                .putExtra("orderId", orderId)
        )
    }

    override fun updateData(list: MutableList<MyTuiGuangBean.DataBean>) {
        pullRefreshHelper.updateResult(list)
    }

    override fun cancelTopSuccess() {
        cancelWaiteStatue()
        pullRefreshHelper.refresh()
        showNotice("取消推广成功")
    }

    private val errorDialog: ErrorDialog by lazy {
        ErrorDialog(this)
    }
    private var topOrderId: String? = null
    private val messageDialog: MessageDialog by lazy {
        MessageDialog(this).apply {
            onMessageDialogListener = object : MessageDialog.OnMessageDialogListener {
                override fun callClick() {
                    topOrderId?.let {
                        mPresent?.loadCancelTopTask(it)
                    }
                }
            }
        }
    }
    private val timeOutDialog: TimeOutDialog by lazy {
        TimeOutDialog(this).apply {
            onTimeOutDialogListener = object : TimeOutDialog.OnTimeOutDialogListener {
                override fun callClick() {
                    pullRefreshHelper.refresh()
                    reLoadMakeOrder(first, second, communityNum, releaseTime, createTime!!, orderId)
                }
            }
            setOnDismissListener {
                pullRefreshHelper.refresh()
            }
        }
    }
}
