package com.pjj.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.pjj.R
import com.pjj.contract.OrderMediaInfContract
import com.pjj.module.MediaOrderInfBean
import com.pjj.module.SpeedDataBean
import com.pjj.module.xspad.XspManage
import com.pjj.present.OrderMediaInfPresent
import com.pjj.utils.*
import com.pjj.view.adapter.OrderElevatorAdapter
import com.pjj.view.adapter.OrderElevatorInfAdapter
import com.pjj.view.adapter.OrderMediaInfAdapter
import com.pjj.view.adapter.SelectReleaseAreaAllAdapter
import com.pjj.view.dialog.ImageNoticeDialog
import com.pjj.view.dialog.NoPassDialog
import kotlinx.android.synthetic.main.activity_order_media_inf.*
import java.util.*

class OrderMediaInfActivity : BaseActivity<OrderMediaInfPresent>(), OrderMediaInfContract.View {
    private lateinit var adapterOrder: RecyclerView.Adapter<*>
    private var completeTag = false
    private var isPlay: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_media_inf)
        setTitle("")
        titleView.background = ColorDrawable(Color.TRANSPARENT)
        completeTag = intent.getBooleanExtra("completeTag", completeTag)
        isPlay = intent.getStringExtra("isPlay")
        rv_media_order_inf.layoutManager = LinearLayoutManager(this)

        mPresent = OrderMediaInfPresent(this)
        intent.getStringExtra("orderId")?.let {
            mPresent?.loadMediaOrderInfTask(it)
        }
        //rl_bottom.visibility = View.INVISIBLE
    }

    private var speedDataBean: SpeedDataBean? = null
    private var path: String = ""
    private var type: String = "1"

    @SuppressLint("SetTextI18n")
    override fun updateListView(data: MediaOrderInfBean) {
        if (data.orderInfo.orderType == "9") {
            adapterOrder = OrderElevatorInfAdapter(true)
        } else {
            adapterOrder = OrderMediaInfAdapter(true)
        }

        val orderInfo = data.orderInfo
        if (TextUtils.isNotEmptyList(data.templetList)) {
            data.templetList[0]?.let {
                if (it.templetType == "3") {
                    speedDataBean = it.speedDataBean
                } else {
                    path = it.fileList[0].fileUrl
                    type = it.fileList[0].type
                }
            }
        }
        var sumPrice = ""
        orderInfo?.let {
            if (!completeTag) {
                tv_play_count.visibility = View.GONE
            } else {
                val playCount = try {
                    it.playCount.toInt()
                } catch (e: Exception) {
                    -1
                }
                if (playCount < 1) {
                    tv_play_count.visibility = View.GONE
                } else {
                    tv_play_count.visibility = View.VISIBLE
                    tv_play_count.text = "播放次数：${playCount}次"
                }
            }

            it.playDate?.let { date ->
                tv_play_date.text = "播放日期：$date"
            }
            tv_play_long_time.text = "播放总时长：${it.playTimeLong} 天"
            //tv_order_type.text = "发布类型："
            tv_look_inf.setOnClickListener {
                if (null != speedDataBean) {
                    XspManage.getInstance().newMediaData.preTowData = speedDataBean
                }
                if (data.orderInfo.orderType == "9") {
                    PreviewXspActivity.startActivity(this, path, type, true, speedDataBean != null)
                } else {
                    PreviewNewMediaActivity.newInstance(this, path, type, speedDataBean != null)
                }
            }
            tv_sum_screen.text = "屏幕总数量：${it.screenCount}面"
            sumPrice = CalculateUtils.m1(it.amount)
            tv_sum_price.text = "订单总金额：${sumPrice}元"
            tv_make_order_time.text = "下单时间：${DateUtils.getSf1(Date(it.createTime))}"
            tv_order_id.text = "订单编号：${it.orderId}"
            tv_play_long_time_one.text = "单次展示秒数：${it.showTime}秒"
            tv_copy_order.setOnClickListener {
                val clip = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                clip.text = tv_order_id.text.substring(5)// 复制
                noticeDialog.updateImage(R.mipmap.smile)
                noticeDialog.setNotice("复制成功")
            }
            if (data.orderInfo.orderType == "9") {
                tv_order_type.text = "发布类型：电梯传媒发布"
                tv_sum_elevator.text = "电梯总数量：${it.elevCount}部"
            } else {
                tv_sum_elevator.visibility = View.GONE
            }
            //it.i
            tv_order_statue.text = when (it.statusX) {
                "0" -> "待审核"
                "1" -> "审核未通过"
                "2" -> {
                    if (isPlay == "0") {
                        "待发布"
                    } else {
                        "发布中"
                    }
                }
                "4" -> "审核过期"
                "5" -> "被撤回"
                "6" -> "待支付"
                "7" -> "支付过期"
                "8" -> "主动取消"
                else -> "已结束" //3
            }
            setLeftDrawable(it.statusX)
            if (!TextUtils.isEmpty(it.revokeMsg)) {
                tv_error.visibility = View.VISIBLE
                tv_error.text = it.revokeMsg
            } else {
                tv_error.visibility = View.GONE
            }
            tv_again_release.setOnClickListener { _ ->
                XspManage.getInstance().identityType = it.authType.toInt()
                XspManage.getInstance().adType = it.orderType.toInt()
                mPresent?.loadOrderInfTask(it.orderId, it.orderType)
            }
        }
        tv_look_price_inf.setOnClickListener {
            startActivity(Intent(this@OrderMediaInfActivity, PriceInfActivity::class.java)
                    .putParcelableArrayListExtra("list_data", data.screenPriceDetail)
                    .putExtra("sum_price", sumPrice))
        }
        val viewBottom = rl_bottom
        var parent = viewBottom.parent as ViewGroup
        parent.removeView(viewBottom)
        val viewTop = rl_top
        val topHeight = viewTop.measuredHeight - titleView.measuredHeight
        parent.removeView(viewTop)
        if (adapterOrder is OrderMediaInfAdapter) {
            (adapterOrder as OrderMediaInfAdapter).list = data.orderScreenList as MutableList<SelectReleaseAreaAllAdapter.OrderAllInfParent>
            (adapterOrder as OrderMediaInfAdapter).addTopAddBottomView(viewTop, viewBottom)
        } else {
            (adapterOrder as OrderElevatorInfAdapter).list = data.orderScreenList as MutableList<OrderElevatorInfAdapter.OrderElevatorInfParent>
            (adapterOrder as OrderElevatorInfAdapter).addTopAddBottomView(viewTop, viewBottom)
        }
        rv_media_order_inf.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            var sumSlide = 0
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                sumSlide += dy
                if (sumSlide >= topHeight) {
                    this@OrderMediaInfActivity.titleView.setTextMiddle("订单详情", Color.WHITE)
                    this@OrderMediaInfActivity.titleView.background = bgColorDrawable
                } else {
                    this@OrderMediaInfActivity.titleView.setTextMiddle("", Color.WHITE)
                    this@OrderMediaInfActivity.titleView.background = ColorDrawable(Color.TRANSPARENT)
                }
            }
        })
        rv_media_order_inf.adapter = adapterOrder
    }

    private val dp16 = ViewUtils.getDp(R.dimen.dp_16)
    private fun setLeftDrawable(statue: String) {
        tv_order_statue.setCompoundDrawables(ViewUtils.getDrawable(if ("5" == statue) R.mipmap.order_inf else R.mipmap.select_white).apply {
            setBounds(0, 0, dp16, dp16)
        }, null, null, null)
    }

    override fun updateTemplateId(tag: Boolean) {
        cancelWaiteStatue()
        Log.e("TAG", "XspManage.getInstance().templateId : " + XspManage.getInstance().templateId)
        if (tag) {
            if (!TextUtils.isNotEmptyList(XspManage.getInstance().groupLocation)) {
                showNotice("无广告屏")
                return
            }
            startActivity(Intent(this, TimeActivity::class.java))
        } else {
            if (!TextUtils.isNotEmptyList(XspManage.getInstance().buildList)) {
                showNotice("无广告屏")
                return
            }
            if (XspManage.getInstance().bianMinPing == 1) {
                SelectBMPingTemplateActivity.start(this, "")
            } else {
                SelectRandomTemplateActivity.start(this, "")
            }

        }
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
                startActivity(Intent(this, TemplateListActivity::class.java)
                        .putExtra("title_text", (if (adType == 9) "传统" else "新媒体") + (if (identityType == 1) "个人" else "商家") + "模板")
                        .putExtra("identity_type", identityType.toString())
                        .putExtra("ad_type", adType)
                        .putExtra("templateId", templateId)
                        .putExtra("releaseTag", true))
                return
            }
        }
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

    //认证
    private val shenHeDialog: ImageNoticeDialog by lazy {
        ImageNoticeDialog(this)
    }
    private val noPassDialog: NoPassDialog by lazy {
        NoPassDialog(this).apply {
            onItemClickListener = object : NoPassDialog.OnItemClickListener {
                override fun go(msg: String) {
                    goRenZheng()
                }
            }
        }
    }

    private fun goRenZheng() {
        var intent = Intent(this, CertificationActivity::class.java)
        intent.putExtra("default", XspManage.getInstance().identityType.toString())
        startActivity(intent)
    }
}
