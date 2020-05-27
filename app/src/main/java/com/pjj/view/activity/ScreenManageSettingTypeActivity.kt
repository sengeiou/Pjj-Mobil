package com.pjj.view.activity

import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager

import com.pjj.R
import com.pjj.contract.ScreenManageSettingTypeContract
import com.pjj.present.ScreenManageSettingTypePresent
import com.pjj.utils.SharedUtils
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.dialog.PartDialog
import com.pjj.view.dialog.SeekBarIntDialog
import kotlinx.android.synthetic.main.activity_screenmanagesettingtype.*
import java.util.regex.Pattern

/**
 * Create by xinheng on 2019/05/22 15:12。
 * describe：屏幕设置-运营方式、价格
 */
class ScreenManageSettingTypeActivity : BaseActivity<ScreenManageSettingTypePresent>(), ScreenManageSettingTypeContract.View {
    private var screenIds: String? = null
    private var mediaPrice: String? = null
    private var cooperationMode: String = "1"
    private var defaultPrice = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screenmanagesettingtype)
        setTitle("设置")
        screenIds = intent.getStringExtra("screenIds")
        defaultPrice = intent.getStringExtra("defaultPrice")
        cooperationMode = intent.getStringExtra("typeModel")
        tv_type.setOnClickListener(onClick)
        iv_type.setOnClickListener(onClick)
        tv_set.setOnClickListener(onClick)
        tv_price.setText(defaultPrice)
        tv_volume.setOnClickListener(onClick)
        mPresent = ScreenManageSettingTypePresent(this)
        initHiddenView()
    }

    private val typeDialog: PartDialog by lazy {
        PartDialog(this).apply {
            onPartDialogListener = object : PartDialog.OnPartDialogListener {
                override fun select(index: String, value: String) {
                    cooperationMode = value
                    //this@ScreenManageSettingTypeActivity.tv_type.text = index
                    initHiddenView()
                }
            }
        }
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_volume -> seekBarDialog.show()
            R.id.tv_type, R.id.iv_type -> {
                typeDialog.show(iv_type, iv_type.measuredWidth / 2)
            }
            R.id.tv_set -> {
                if (tv_price.visibility == View.GONE) {

                } else {
                    val priceText = tv_price.text.toString()
                    if (priceText.contains(".")) {
                        showNotice("投放价格不能为小数")
                        return
                    }
                    val price = try {
                        priceText.toInt()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        showNotice("请输入数字")
                        return
                    }
                    if (price < 1) {
                        showNotice("投放价格不能小于1元")
                        return
                    }
                    if (price > 10000) {
                        showNotice("投放价格不能大于10000元")
                        return
                    }
                    mediaPrice = price.toString()
                }
                mPresent?.loadSetType(screenIds!!, mediaPrice, cooperationMode, getVolume())
            }
        }
    }

    private fun getVolume(): String? {
        var string = tv_volume.text.toString()
        return when (TextUtils.isEmpty(string)) {
            true -> null
            else -> string
        }

    }

    override fun setSuccess() {
        smillTag = true
        showNotice("设置成功")
        smillTag = false
        Handler().postDelayed({
            setResult(302)
            finish()
        }, 2100)
    }

    private fun initHiddenView() {
        when (cooperationMode) {
            "1" -> {
                tv_type.text = "地方自营"
                tv_price_.visibility = View.VISIBLE
                tv_price.visibility = View.VISIBLE
                tv_price_after.visibility = View.VISIBLE
                line_price.visibility = View.VISIBLE
                tv_explain.visibility = View.VISIBLE
            }
            "4" -> {
                tv_type.text = "自用"
                tv_price_.visibility = View.GONE
                tv_price.visibility = View.GONE
                tv_price_after.visibility = View.GONE
                line_price.visibility = View.GONE
                tv_explain.visibility = View.GONE
            }
            else -> {
                tv_type.text = "请选择"
                tv_price_.visibility = View.GONE
                tv_price.visibility = View.GONE
                tv_price_after.visibility = View.GONE
                line_price.visibility = View.GONE
                tv_explain.visibility = View.GONE
            }
        }
    }

    private val seekBarDialog: SeekBarIntDialog by lazy {
        SeekBarIntDialog(this, 0).apply {
            onSeekBarIntDialogListener = object : SeekBarIntDialog.OnSeekBarIntDialogListener {
                override fun selectVolume(volume: Int) {
                    //mPresent?.loadSetType(screenIds!!, mediaPrice, cooperationMode, getVolume())
                    this@ScreenManageSettingTypeActivity.tv_volume.text = "$volume"
                }
            }
        }
    }
}
