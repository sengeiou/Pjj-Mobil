package com.pjj.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pjj.PjjApplication

import com.pjj.R
import com.pjj.contract.TuiGuangContract
import com.pjj.module.TopPriceBean
import com.pjj.module.xspad.XspManage
import com.pjj.present.TuiGuangPresent
import com.pjj.utils.CalculateUtils
import com.pjj.utils.DateUtils
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.dialog.RemindNoticeDialog
import kotlinx.android.synthetic.main.activity_tuiguang.*
import java.util.*
import kotlin.collections.ArrayList

/**
 * Create by xinheng on 2019/07/15 11:14。
 * describe：推广
 */
class TuiGuangActivity : PayActivity<TuiGuangPresent<*>>(), TuiGuangContract.View {
    private var orderJson: String? = null

    override fun getMakeOrderJson(): String {
        return orderJson!!
    }

    override fun getFinalPayPrice(): Float {
        return priceFinal
    }

    override fun payFinishStartOrderView(index: Int) {
        if (index > 0 && from != null) {
            XspManage.getInstance().refreshTag.tagOrderListFragment = from
        }
        startActivity(Intent(this@TuiGuangActivity, MyTuiGuangActivity::class.java))
        finish()
    }


    private var datas: List<TopPriceBean.DataBean>? = null
    private var priceFinal = -1f
    private var orderId: String? = null
    private var from: String? = null
    private var createTime = 0L
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tuiguang)
        setTitle("置顶推广")
        (titleView.parent as ViewGroup).background = ColorDrawable(ViewUtils.getColor(R.color.color_f1f1f1))
        var statueHeight = PjjApplication.application.statueHeight
        val params = titleView.layoutParams as RelativeLayout.LayoutParams
        if (statueHeight > 0) {
            params.topMargin = statueHeight
        } else {
            statueHeight = statue
            PjjApplication.application.statueHeight = statueHeight
        }
        params.topMargin = statueHeight
        titleView.layoutParams = params
        mPresent = TuiGuangPresent(this, TuiGuangContract.View::class.java)
        selectImagArray.clear()
        mPresent!!.loadTopPriceTask()
        iv_read.buttonDrawable = ViewUtils.createSelectDrawable(R.mipmap.select_red, R.mipmap.unselect, android.R.attr.state_checked)!!.apply {
            val dp = ViewUtils.getDp(R.dimen.dp_16)
            setBounds(0, 0, dp, dp)
        }
        tv_read_rule.setOnClickListener(onClick)
        tv_submit_price.setOnClickListener(onClick)

        intent.getStringExtra("first")?.let {
            Glide.with(this).load(it).into(iv_template)
        }
        val second = intent.getStringExtra("second")
        if (!TextUtils.isEmpty(second)) {
            iv_template_1.visibility = View.VISIBLE
            Glide.with(this).load(second).into(iv_template_1)
        }
        tv_template_build.text = intent.getStringExtra("communityNum")
        //tv_release_date.text = "发布日期：" + intent.getLongExtra("releaseTime",-1L)
        createTime = intent.getLongExtra("createTime", System.currentTimeMillis())
        tv_create_date.text = "创建时间：" + getPointDate(System.currentTimeMillis())
        orderId = intent.getStringExtra("orderId")
        from = intent.getStringExtra("from")
    }

    private fun getPointDate(time: Long): String {
        return DateUtils.getSfPoint(Date(time))
    }

    private fun getReleaseDate(countDay: Int): String {
        val calendar = Calendar.getInstance(Locale.SIMPLIFIED_CHINESE)
        calendar.timeInMillis = createTime
        calendar.add(Calendar.DATE, countDay)
        return getPointDate(createTime) + " - " + getPointDate(calendar.timeInMillis)
    }

    private val remindNoticeDialog: RemindNoticeDialog by lazy {
        RemindNoticeDialog(this, 0)
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_read_rule -> WebActivity.newInstance(this, "置顶协议", "http://protal.test.pingjiajia.cn/guide/#/guideInfo?dicId=b13aa0ac4b074f44893b926e588c586e")
            R.id.tv_submit_price -> {
                if (priceFinal == -1f) {
                    showNotice("请选择价格")
                    return
                }
                if (!iv_read.isChecked) {
                    //showNotice("阅读")
                    remindNoticeDialog.show()
                    return
                }
                makeOrder()
            }
        }
    }

    override fun updatePrice(datas: MutableList<TopPriceBean.DataBean>?) {
        this.datas = datas
        val count = ll_time_long.childCount
        if (count > 1) {
            (1 until count).forEach {
                ll_time_long.removeViewAt(it)
            }
        }
        val size = datas?.size ?: 0

        if (size > 0) {
            val bean = datas!![0]
            bean.isSelectTag = true
            ll_time_long.addView(childView(bean, 0))
            if (size > 1) {
                (1 until size).forEach {
                    ll_time_long.addView(lineView())
                    val beanX = datas[it]
                    ll_time_long.addView(childView(beanX, it))
                }
            }
        }
    }

    private val dp48 = ViewUtils.getDp(R.dimen.dp_48)
    private val dp82 = ViewUtils.getDp(R.dimen.dp_82)
    private val dp90 = ViewUtils.getDp(R.dimen.dp_90)
    private val dp29 = ViewUtils.getDp(R.dimen.dp_29)
    private val dp19 = ViewUtils.getDp(R.dimen.dp_19)
    private val dp16 = ViewUtils.getDp(R.dimen.dp_16)
    private val dp11 = ViewUtils.getDp(R.dimen.dp_11)
    private val dp4 = ViewUtils.getDp(R.dimen.dp_4)
    private var selectImagArray = ArrayList<ImageView>()
    @SuppressLint("SetTextI18n")
    private fun childView(data: TopPriceBean.DataBean, index: Int): View {
        val day = data.topName
        val price = data.price
        val discount = data.discount
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp48))
            gravity = Gravity.CENTER_VERTICAL
            addView(TextView(this@TuiGuangActivity).apply {
                text = day
                layoutParams = ViewGroup.LayoutParams(dp82, dp48)
                gravity = Gravity.CENTER_VERTICAL
                setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_15))
                setTextColor(ViewUtils.getColor(R.color.color_333333))
            })
            addView(TextView(this@TuiGuangActivity).apply {
                text = "¥${CalculateUtils.m1(price)}"
                layoutParams = LinearLayout.LayoutParams(dp90, dp48)
                gravity = Gravity.CENTER_VERTICAL
                setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_15))
                setTextColor(ViewUtils.getColor(R.color.color_ff4c4c))
            })
            addView(TextView(this@TuiGuangActivity).apply {
                layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, dp16)
                gravity = Gravity.CENTER_VERTICAL
                setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_10))
                if (discount >= 0) {
                    val discountString = getDiscount(discount)
                    text = discountString
                    if (discountString.isEmpty()) {
                        background = ColorDrawable(Color.TRANSPARENT)
                    } else {
                        background = ViewUtils.getDrawable(R.drawable.shape_ff4c4c_bg_3)
                    }
                }
                setTextColor(ViewUtils.getColor(R.color.white))
                setPadding(dp4, 0, dp4, 0)
            })
            addView(View(this@TuiGuangActivity).apply {
                layoutParams = LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1f)
            })
            addView(ImageView(this@TuiGuangActivity).apply {
                setImageResource(if (data.isSelectTag) R.mipmap.select_red else R.mipmap.unselect)
                layoutParams = LinearLayout.LayoutParams(dp19, dp19)
                selectImagArray.add(this)
                if (data.isSelectTag) {
                    orderJson = "{\"orderId\":\"$orderId\",\"topId\":\"${data.topId}\",\"userId\":\"${PjjApplication.application.userId}\"}"
                    priceFinal = data.price * data.discount
                    updateSumPrice(priceFinal)
                    tv_release_date.text = "发布日期：" + getReleaseDate(data.dataTimeLong)
                }
                setOnClickListener {
                    data.isSelectTag = !data.isSelectTag
                    if (data.isSelectTag) {
                        priceFinal = data.price * data.discount
                        updateSumPrice(priceFinal)
                        tv_release_date.text = "发布日期：" + getReleaseDate(data.dataTimeLong)
                        orderJson = "{\"orderId\":\"$orderId\",\"topId\":\"${data.topId}\",\"userId\":\"${PjjApplication.application.userId}\"}"
                        updateChildSelectStatue(index)
                        (it as ImageView).setImageResource(R.mipmap.select_red)
                    } else {
                        priceFinal = -1f
                        updateSumPrice(0f)
                        (it as ImageView).setImageResource(R.mipmap.unselect)
                    }
                }
            })
            addView(View(this@TuiGuangActivity).apply {
                layoutParams = LinearLayout.LayoutParams(dp11, LinearLayout.LayoutParams.MATCH_PARENT)
            })
        }
    }

    private fun getDiscount(discount: Float): String {
        var fl = getString(discount * 100)
        return if (fl == "100") "" else fl + "折"
    }

    private fun getString(value: Float): String {
        val intValue = value.toInt()
        if (value - intValue > 0) {
            return value.toString()
        } else {
            return intValue.toString()
        }
    }

    private fun updateChildSelectStatue(indexSelf: Int) {
        selectImagArray.forEachIndexed { index, imageView ->
            if (index != indexSelf && datas!![index].isSelectTag) {
                datas!![index].isSelectTag = false
                imageView.setImageResource(R.mipmap.unselect)
            }
        }
    }

    private val dp1 = ViewUtils.getDp(R.dimen.dp_1)
    private fun lineView(): View {
        return View(this).apply {
            background = ColorDrawable(ViewUtils.getColor(R.color.color_e3e3e3))
            layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dp1)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateSumPrice(price: Float) {
        val m1 = CalculateUtils.m1(price)
        tv_price.text = "¥$m1"
        tv_submit_price.text = "立即支付（¥${m1}）"
    }

    override fun fitSystemWindow(): Boolean {
        return false
    }
}
