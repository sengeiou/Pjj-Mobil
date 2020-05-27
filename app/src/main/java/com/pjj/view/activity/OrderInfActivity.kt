package com.pjj.view.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.ClipboardManager
import android.view.View
import android.view.ViewGroup
import android.view.ViewStub
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide

import com.pjj.R
import com.pjj.contract.OrderInfContract
import com.pjj.module.OrderElevatorBean
import com.pjj.module.SpeedDataBean
import com.pjj.module.xspad.XspManage
import com.pjj.present.OrderInfPresent
import com.pjj.utils.BitmapUtils
import com.pjj.utils.Log
import com.pjj.utils.ViewUtils
import com.pjj.view.adapter.OrderElevatorAdapter
import com.pjj.view.custom.SpeedViewGroup
import kotlinx.android.synthetic.main.activity_orderinf.*
import kotlinx.android.synthetic.main.layout_order_bianmin_item.*
import java.util.*
import kotlin.math.log

/**
 * Create by xinheng on 2018/12/08 11:43。
 * describe：订单详情
 */
class OrderInfActivity : BaseActivity<OrderInfPresent>(), OrderInfContract.View {
    private lateinit var orderAdapter: OrderElevatorAdapter
    private var bmPingTag = false
    private var bmPinColorTag = 11
    private var bmPingName = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orderinf)
        setTitle("订单详情")
        orderAdapter = OrderElevatorAdapter(this)
        rv_elevator.run {
            layoutManager = LinearLayoutManager(this@OrderInfActivity)
            adapter = orderAdapter
        }
        mPresent = OrderInfPresent(this).apply {
            loadOrderInfTask(intent.getStringExtra("orderId"), intent.getStringExtra("type"))
        }
    }

    @SuppressLint("SetTextI18n")
    override fun updateOrderInf(type: String, path_content: String, title: String, showPhone: Boolean, showPerson: Boolean, time: String, no: String, timeLength: String, price: String, mediaType: String?, speedDataBean: SpeedDataBean?) {
        var clickView: View
        tv_order_type.text = "发布类型：" + when (type) {
            "1" -> {
                findViewById<ViewStub>(R.id.order_diy).inflate().apply {
                    var imageView = findViewById<ImageView>(R.id.iv_order)
                    if (mediaType == "1") {
                        Glide.with(this@OrderInfActivity).load(path_content).into(imageView)
                        findViewById<ImageView>(R.id.iv_play).visibility = View.GONE
                    } else {
                        BitmapUtils.loadFirstImageForVideo(Glide.with(this@OrderInfActivity), path_content as String, imageView)
                        findViewById<ImageView>(R.id.iv_play).visibility = View.VISIBLE
                    }
                    clickView = imageView
                }
                findViewById<TextView>(R.id.tv_order_name).apply {
                    visibility = View.VISIBLE
                }.text = "$title"
                "DIY整版"
            }
            "7" -> {
                if (speedDataBean == null) {
                    findViewById<ViewStub>(R.id.order_diy).inflate().apply {
                        var imageView = findViewById<ImageView>(R.id.iv_order)
                        if (mediaType == "1") {
                            Glide.with(this@OrderInfActivity).load(path_content).into(imageView)
                            findViewById<ImageView>(R.id.iv_play).visibility = View.GONE
                        } else {
                            BitmapUtils.loadFirstImageForVideo(Glide.with(this@OrderInfActivity), path_content, imageView)
                            findViewById<ImageView>(R.id.iv_play).visibility = View.VISIBLE
                        }
                        clickView = imageView
                    }
                } else {
                    findViewById<ViewStub>(R.id.order_speed).inflate().apply {
                        clickView = this
                        var speedViewGroup = findViewById<SpeedViewGroup>(R.id.svg)
                        speedViewGroup.textSize = ViewUtils.getFDp(R.dimen.sp_8)
                        speedViewGroup.speedData = speedDataBean
                    }
                }
                findViewById<TextView>(R.id.tv_order_name).apply {
                    visibility = View.VISIBLE
                }.text = "$title"
                "新媒体发布"
            }
            "2" -> {
                findViewById<ViewStub>(R.id.order_bianmin).inflate().apply {
                    clickView = this
                    findViewById<TextView>(R.id.tv_order_name).text = title
                    tv_order_context.text = path_content as String
                }
                "便民整版"
            }
            "4" -> {
                findViewById<ViewStub>(R.id.order_bianmin).inflate().apply {
                    clickView = this
                    findViewById<TextView>(R.id.tv_order_name).text = title
                    tv_order_context.text = path_content as String
                }
                "随机便民整版"
            }
            "5" -> {
                findViewById<ViewStub>(R.id.order_speed).inflate().apply {
                    clickView = this
                    var speedViewGroup = findViewById<SpeedViewGroup>(R.id.svg)
                    speedViewGroup.textSize = ViewUtils.getFDp(R.dimen.sp_8)
                    speedViewGroup.speedData = XspManage.getInstance().speedScreenData.clone
                }
                "拼屏发布"
            }
            else -> {
                findViewById<ViewStub>(R.id.order_diy).inflate().apply {
                    var imageView = findViewById<ImageView>(R.id.iv_order)
                    if (mediaType == "1") {
                        Glide.with(this@OrderInfActivity).load(path_content).into(imageView)
                        findViewById<ImageView>(R.id.iv_play).visibility = View.GONE
                    } else {
                        BitmapUtils.loadFirstImageForVideo(Glide.with(this@OrderInfActivity), path_content, imageView)
                        findViewById<ImageView>(R.id.iv_play).visibility = View.VISIBLE
                    }
                    clickView = imageView
                }
                findViewById<TextView>(R.id.tv_order_name).apply {
                    visibility = View.VISIBLE
                }.text = "$title"
                "随机DIY整版"
            }
        }
        clickView.setOnClickListener {
            if (type == "5") {
                startActivity(Intent(this@OrderInfActivity, PreviewXspSpeedActivity::class.java))
                return@setOnClickListener
            }
            if (type == "7") {
                if (speedDataBean != null) {
                    XspManage.getInstance().newMediaData.preTowData = speedDataBean
                }
                PreviewNewMediaActivity.newInstance(this@OrderInfActivity, path_content, mediaType!!, speedDataBean != null)
                return@setOnClickListener
            }
            mediaType?.let { item ->
                if (bmPingTag) {
                    PreviewXspBmPingActivity.startActivity(this@OrderInfActivity, path_content, bmPinColorTag, bmPingName,
                            when (showPerson) {
                                true -> "屏加加科技有限公司"
                                else -> null
                            },
                            when (showPhone) {
                                true -> "4001251818"
                                false -> null
                            })
                } else {
                    PreviewXspActivity.startActivity(this@OrderInfActivity, path_content, item,
                            when (showPerson) {
                                true -> "屏加加科技有限公司"
                                else -> null
                            },
                            when (showPhone) {
                                true -> "4001251818"
                                false -> null
                            })
                }
            }

        }
        tv_time.text = "时间：$time"
        tv_order.text = "订单编号：$no"
        tv_order_copy.setOnClickListener {
            var clip = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            clip.text = tv_order.text.substring(5)// 复制
            noticeDialog.updateImage(R.mipmap.smile)
            noticeDialog.setNotice("复制成功")
        }
        if (type != "5" && type != "7") {
            tv_right_text.text = when (showPerson) {
                true -> "显示发布者"
                else -> "隐藏发布者"
            } + "   " + when (showPhone) {
                true -> "显示手机号"
                else -> "隐藏手机号"
            }
        } else {
            tv_right_text.text = ""
        }
        tv_order_length.text = "发布总时长："
        tv_order_price.text = "订单总金额：$price 元"
    }

    override fun updateOrderElevatorList(list: MutableList<OrderElevatorBean>) {
        if (orderAdapter.head == null) {
            val head = cl_head
            (head.parent as ViewGroup).removeView(head)
            orderAdapter.head = head
        }
        orderAdapter.list = list
    }

    override fun updateHours(hours: Int) {
        if (orderAdapter.head != null) {
            Log.e("TAG", "head:!=null ")
            var txt: String = if (hours < 0) {
                "发布总天数：${-hours} 天"
            } else {
                "发布总时长：$hours 小时"
            }
            orderAdapter.head!!.findViewById<TextView>(R.id.tv_order_length).text = txt
        } else {
            tv_order_length.text = "发布总时长：$hours 小时"
        }
    }

    override fun updatePingInf(name: String?, colorTag: String) {
        if (null == name) {
            gonePing()
        } else {
            tv_order_type.text = "发布类型：拼屏信息"
            findViewById<TextView>(R.id.tv_bian_min_type).text = "分类：$name"
            var colorTagInt: Int = -1
            try {
                colorTagInt = colorTag.toInt()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            if (colorTagInt > -1) {
                bmPinColorTag = colorTagInt
                bmPingName = name
                bmPingTag = true
                findViewById<View>(R.id.view_color).background = ColorDrawable(XspManage.getBgColor(colorTagInt))
            }
        }
    }

    private fun gonePing() {
        findViewById<View>(R.id.tv_bian_min_type).visibility = View.GONE
        findViewById<View>(R.id.line_bm_type).visibility = View.GONE
        findViewById<View>(R.id.tv_bg_color).visibility = View.GONE
        findViewById<View>(R.id.view_color).visibility = View.GONE
        findViewById<View>(R.id.line_bg_color).visibility = View.GONE
    }
}
