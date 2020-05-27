package com.pjj.view.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.widget.LinearLayoutManager

import com.pjj.R
import com.pjj.contract.MyReleaseContract
import com.pjj.module.MyReleaseBean
import com.pjj.present.MyReleasePresent
import com.pjj.utils.DateUtils
import com.pjj.utils.PullRefreshHelper
import com.pjj.utils.SpaceItemDecoration
import com.pjj.utils.ViewUtils
import com.pjj.view.adapter.MyReleaseAdapter
import com.pjj.view.dialog.ErrorDialog
import com.pjj.view.dialog.MessageDialog
import kotlinx.android.synthetic.main.activity_myrelease.*
import java.util.*

/**
 * Create by xinheng on 2019/08/09 14:49。
 * describe：我的发布
 */
class MyReleaseActivity : BaseActivity<MyReleasePresent>(), MyReleaseContract.View {
    private lateinit var pullRefreshHelper: PullRefreshHelper<MyReleaseBean.DataBean>
    private val myReleaseAdapter = MyReleaseAdapter(this)
    private var isTop = false
    private var freeOrderId: String? = null
    private var status: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_myrelease)
        savedInstanceState?.run {
            freeOrderId = getString("freeOrderId")
            status = getString("status")
            isTop = getBoolean("isTop")
        }
        setTitle("我的发布")
        mPresent = MyReleasePresent(this)
        pullRefreshHelper = PullRefreshHelper(pullToRefresh)
        pullRefreshHelper.onPullRefreshHelperListener = object : PullRefreshHelper.OnPullRefreshHelperListener<MyReleaseBean.DataBean> {
            override fun loadData(start: Int, num: Int) {
                mPresent?.loadMyReleaseTask(start, num)
            }

            override fun iniList(list: MutableList<MyReleaseBean.DataBean>?) {
                myReleaseAdapter.list = list
            }

            override fun addList(list: MutableList<MyReleaseBean.DataBean>) {
                myReleaseAdapter.addMore(list)
            }
        }
        rv_my_release.layoutManager = LinearLayoutManager(this)
        rv_my_release.addItemDecoration(SpaceItemDecoration(this, 1, ViewUtils.getDp(R.dimen.dp_10), ViewUtils.getColor(R.color.color_f4f4f4)))
        myReleaseAdapter.onMyReleaseAdapterListener = object : MyReleaseAdapter.OnMyReleaseAdapterListener {
            override fun pre(bean: MyReleaseBean.DataBean) {
                if (bean.orderType == "3") {
                    WebActivity.newInstance(this@MyReleaseActivity, "外部视频链接", bean.content)
                } else {
                    bean.filePath?.let {
                        PreviewDialogActivity.startActivity(this@MyReleaseActivity, it, bean.fileType, bean.title, false)
                    }
                }
            }

            override fun tuiGuang(bean: MyReleaseBean.DataBean) {
                reLoadMakeOrder(bean.filePath, null, bean.title, -1L, System.currentTimeMillis(), bean.freeOrderId)
            }

            override fun delete(bean: MyReleaseBean.DataBean) {
                messageDialog.message("确认删除该订单吗？")
                isTop = bean.isTop == "1"
                freeOrderId = bean.freeOrderId
                status = "3"
            }

            override fun error(bean: MyReleaseBean.DataBean) {
                val error = bean.revokeMsg
                errorDialog.showError(error)
            }

            override fun recover(bean: MyReleaseBean.DataBean) {
                status = "1"
                freeOrderId = bean.freeOrderId
                messageDialog.message("确认恢复该订单吗？")
            }

        }
        rv_my_release.adapter = myReleaseAdapter
    }

    override fun onResume() {
        pullRefreshHelper.refresh()
        super.onResume()
    }

    private fun reLoadMakeOrder(first: String, second: String?, communityNum: String, releaseTime: Long, createTime: Long, orderId: String) {
        startActivity(Intent(this, TuiGuangActivity::class.java)
                .putExtra("first", first)
                .putExtra("second", second)
                .putExtra("communityNum", communityNum)
                //.putExtra("releaseTime", releaseTime)
                .putExtra("createTime", createTime)
                .putExtra("orderId", orderId)
        )
    }

    override fun titleLeftClick() {
        startActivity(Intent(this@MyReleaseActivity, MainActivity::class.java))
        finish()
    }

    override fun onBackPressed() {
        titleLeftClick()
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.run {
            putString("freeOrderId", freeOrderId)
            putString("status", status)
            putBoolean("isTop", isTop)
        }
        super.onSaveInstanceState(outState)
    }

    override fun updateData(list: MutableList<MyReleaseBean.DataBean>?) {
        pullRefreshHelper.updateResult(list)
    }

    override fun deleteOrRecoverSuccess(tagDelete: Boolean) {
        if (!messageDialog.message().contains("正在推广中")) {
            cancelWaiteStatue()
            pullRefreshHelper.refresh()
        } else {
            showNotice("您可以在我的推广\n中查看详情")
        }
        //smillTag = true
        //showNotice(if (tagDelete) "您的推广订单\n删除成功" else "您的推广订单恢复成功")
    }

    override fun canacelFreeTopOrderSuccess() {
        pullRefreshHelper.refresh()
        cancelWaiteStatue()
        smillTag = true
        showNotice("您的推广订单\n删除成功")
    }

    private var onlyDelete = false
    private val messageDialog: MessageDialog by lazy {
        MessageDialog(this).apply {
            setOnDismissListener {
                if (onlyDelete) {
                    onlyDelete = false
                    mPresent?.loadDeleteOrRecoverTask(freeOrderId!!, "1")
                }
            }
            onMessageDialogListener = object : MessageDialog.OnMessageDialogListener {
                override fun callClick() {
                    if ("1" == status) {
                        mPresent?.loadDeleteOrRecoverTask(freeOrderId!!, status!!)
                    } else {
                        when {
                            !isTop -> mPresent?.loadDeleteOrRecoverTask(freeOrderId!!, status!!)
                            isTop && message().contains("确认删除") -> {
                                onlyDelete = true
                                message("您的信息 正在推广中，\n取消推广暂不退款,是否继续~", false)
                            }
                            else -> {
                                onlyDelete = false
                                mPresent?.loadDeleteOrRecoverTask(freeOrderId!!, status!!)
                                Handler().postDelayed({
                                    mPresent?.loadCancelFreeTopOrderTask(freeOrderId!!)
                                }, 500)
                            }
                        }
                    }
                }
            }
        }
    }
    private val errorDialog: ErrorDialog by lazy {
        ErrorDialog(this)
    }
}
