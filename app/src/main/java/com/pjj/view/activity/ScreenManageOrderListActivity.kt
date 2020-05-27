package com.pjj.view.activity

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager

import com.pjj.R
import com.pjj.contract.ScreenManageOrderListContract
import com.pjj.module.ScreenManageOrderListBean
import com.pjj.present.ScreenManageOrderListPresent
import com.pjj.utils.SpaceItemDecoration
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.adapter.ScreenManageOrderListAdapter
import com.pjj.view.dialog.CallPhoneDialog
import kotlinx.android.synthetic.main.activity_screenmanageorderlist.*
import kotlinx.android.synthetic.main.layout_list_view.rv_order

/**
 * Create by xinheng on 2019/06/05 13:50。
 * describe：屏幕管理订单列表
 */
class ScreenManageOrderListActivity : BaseActivity<ScreenManageOrderListPresent>(), ScreenManageOrderListContract.View {
    private var allDeleteTag = false
    private var ownOrderId: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screenmanageorderlist)
        setTitle("订单列表")
        rv_order.adapter = adapterOrderList
        rv_order.layoutManager = LinearLayoutManager(this)
        rv_order.addItemDecoration(SpaceItemDecoration(this, 1, ViewUtils.getDp(R.dimen.dp_15), ViewUtils.getColor(R.color.color_f1f1f1)))
        tv_delete_all_order.setOnClickListener(onClick)
        mPresent = ScreenManageOrderListPresent(this)
        mPresent!!.loadOrderListTask()
    }

    override fun onClick(viewId: Int) {
        super.onClick(viewId)
        when (viewId) {
            R.id.tv_delete_all_order -> {
                if (adapterOrderList.itemCount == 0) {
                    showNotice("暂无可撤播订单")
                    return
                }
                allDeleteTag = true
                ownOrderId = null
                showDeleteDialog()
            }
        }
    }

    private val adapterOrderList = ScreenManageOrderListAdapter().apply {
        onScreenManageOrderListAdapterListener = object : ScreenManageOrderListAdapter.OnScreenManageOrderListAdapterListener {
            override fun deleteOrder(orderId: String) {
                allDeleteTag = false
                ownOrderId = orderId
                showDeleteDialog()
            }

            override fun preview(mediaType: String, path: String, orderType: String) {
                PreviewNewMediaActivity.newInstance(this@ScreenManageOrderListActivity, path, mediaType)
            }
        }
    }
    private val deleteOrderDialog: CallPhoneDialog by lazy {
        CallPhoneDialog(this).apply {
            setTowText("确认", "取消")
            //phone = "确认撤播？"
            onCallPhoneDialogListener = object : CallPhoneDialog.OnCallPhoneDialogListener {
                override fun callClick() {
                    deleteOrder()
                }
            }
        }
    }

    private fun showDeleteDialog() {
        val text = if (allDeleteTag) "确认撤播？" else "确认撤播？"
        deleteOrderDialog.phone = text
        deleteOrderDialog.show()
    }

    private fun deleteOrder() {
        if (!allDeleteTag && TextUtils.isEmpty(ownOrderId)) {
            showNotice("订单id错误")
            return
        }
        mPresent?.loadDeleteOrderTask(ownOrderId)
    }

    override fun deleteSuccess() {
        smillTag = true
        showNotice("撤播成功")
        smillTag = false
        if (allDeleteTag) {
            adapterOrderList.deleteAll()
        } else {
            adapterOrderList.deleteOrder()
        }
    }

    override fun updateList(list: MutableList<ScreenManageOrderListBean.DataBean>?) {
        adapterOrderList.list = list
    }
}
