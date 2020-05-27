package com.pjj.view.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.pjj.R
import com.pjj.contract.MallOrderContract
import com.pjj.module.CancelWhyBean
import com.pjj.module.MallOrderListBean
import com.pjj.present.MallOrderPresent
import com.pjj.present.MallPayPresent
import com.pjj.view.activity.MallHomepageActivity
import com.pjj.view.activity.MallOrderInfActivity
import com.pjj.view.adapter.ListViewAdapter
import com.pjj.view.adapter.MallOrderAdapter
import com.pjj.view.dialog.CallPhoneDialog
import com.pjj.view.dialog.MallCancelOrderDialog
import com.pjj.view.dialog.PayDialogHelp

/**
 * Create by xinheng on 2019/05/18 17:13。
 * describe：订单列表
 */
class MallOrderFragment : ListViewFragment<MallOrderListBean.DataBean, MallOrderPresent>(), MallOrderContract.View {
    private var tag = 0
    private var mallPayPresent: MallPayPresent? = null

    companion object {
        private val STATUE_TAG = "statue_tag"
        @JvmStatic
        fun newInstance(statue: Int) = MallOrderFragment().apply {
            arguments = Bundle().apply {
                putInt(STATUE_TAG, statue)
            }
        }
    }

    override fun startOrderView(index: Int) {
        if (index > 0) { //0 等待支付
            startRefresh()
        }
    }

    override fun preMakeOrder(typePay: Int): Boolean {
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tag = arguments?.getInt(STATUE_TAG) ?: tag
    }

    override fun onResume() {
        if (null != pullToRefresh) {
            pullRefreshHelper?.refresh()
        }
        super.onResume()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mPresent = MallOrderPresent(this)
        super.onViewCreated(view, savedInstanceState)
        setNoDataText(when (tag) {
            0 -> "当前无待支付订单~"
            1 -> "当前无待收货订单~"
            else -> "当前无已完成订单~"
        })
        mallPayPresent = MallPayPresent(activity!!, this)
    }

    private var cancelDialog: MallCancelOrderDialog? = null

    override fun updateCancelWhy(datas: MutableList<CancelWhyBean.DataBean>) {
        if (null == cancelDialog) {
            cancelDialog = MallCancelOrderDialog(activity!!).apply {
                onMallCancelOrderListener = object : MallCancelOrderDialog.OnMallCancelOrderListener {
                    override fun cancel(msg: String) {
                        mPresent?.loadCancelTask(goodOrderId, storeId, msg)
                    }

                    override fun notice(msg: String) {
                        showNotice(msg)
                    }

                }
            }
        }
        cancelDialog!!.data = datas
        cancelDialog!!.show()
    }

    private val deleteDialog: CallPhoneDialog by lazy {
        CallPhoneDialog(activity!!).apply {
            phone = "确认删除订单？"
            setTowText("确认", "取消", false)
            onCallPhoneDialogListener = object : CallPhoneDialog.OnCallPhoneDialogListener {
                override fun callClick() {//123171565848297699
                    mPresent?.loadDeleteOrder(goodOrderId, storeId)
                }
            }
        }
    }

    override fun updateData(list: MutableList<MallOrderListBean.DataBean>?) {
        setData(list)
    }

    override fun cancelSuccess() {
        if (null != cancelDialog && cancelDialog!!.isShowing)
            cancelDialog?.dismiss()
        startRefresh()
    }

    override fun paySuccess() {
        startRefresh()
    }

    private lateinit var goodOrderId: String
    private lateinit var storeId: String
    override fun getAdapter(): ListViewAdapter<MallOrderListBean.DataBean, *> {
        return MallOrderAdapter(tag).apply {
            onMallOrderAdapterListener = object : MallOrderAdapter.OnMallOrderAdapterListener {
                override fun cancelOrder(goodOrderId: String, storeId: String) {
                    this@MallOrderFragment.goodOrderId = goodOrderId
                    this@MallOrderFragment.storeId = storeId
                    mPresent?.loadCancelWhy()
                }

                override fun deleteOrder(goodOrderId: String, storeId: String) {
                    this@MallOrderFragment.goodOrderId = goodOrderId
                    this@MallOrderFragment.storeId = storeId
                    deleteDialog.show()
                }

                override fun payNow(goodOrderId: String, storeId: String, sumPrice: String) {
                    if (tag == 1) {//确认收货
                        mPresent?.loadSureOrder(goodOrderId, storeId)
                    } else {
                        mallPayPresent?.goodOrderId = goodOrderId
                        mallPayPresent?.storeId = storeId
                        mallPayPresent?.showDialog(sumPrice, true)
                    }
                }

                override fun itemClick(goodOrderId: String, storeId: String) {
                    startActivityForResult(Intent(activity, MallOrderInfActivity::class.java)
                            .putExtra("type", tag.toString())
                            .putExtra("goodOrderId", goodOrderId)
                            .putExtra("storeId", storeId)
                            , 202)
                }

                override fun notice(msg: String) {
                    showNotice(msg)
                }
            }
        }
    }

    override fun sureOrderSuccess() {
        startRefresh()
    }

    override fun deleteSuccess() {
        pullRefreshHelper.refresh()
    }

    override fun loadData(start: Int, num: Int) {
        mPresent?.loadData(tag, start, num)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            requestCode == 202 && resultCode == 303 -> {
                if (tag == 0) {
                    onMallOrderFragmentListener?.startOtherFragment(2)
                }
                startRefresh()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    var onMallOrderFragmentListener: OnMallOrderFragmentListener? = null

    interface OnMallOrderFragmentListener {
        fun startOtherFragment(tag: Int)
    }
}
