package com.pjj.view.dialog

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.widget.TextView
import com.pjj.R
import com.pjj.module.xspad.XspManage
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.layout_release_type.*

/**
 * Created by XinHeng on 2018/12/14.
 * describe：
 */
class AdTypeDialog(context: Context) : Dialog(context) {

    private var onClick = View.OnClickListener {
        when (it.id) {
            R.id.tv_cancel -> dismiss()
            R.id.tv_merchants -> {
                dismiss()
                onTypeSelectListener?.selectMerchamts()
            }
            R.id.tv_person -> {
                dismiss()
                onTypeSelectListener?.selectPerson()
            }
        }
    }
    private var dp = ViewUtils.getDp(R.dimen.dp_58)
    private fun setTopDrawable(resource: Int, tv: TextView) {
        tv.setCompoundDrawables(null, ViewUtils.getDrawable(resource).apply {
            setBounds(0, 0, dp, dp)
        }, null, null)
    }

    init {
        window.requestFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.layout_release_type)
        window.decorView.setPadding(0, 0, 0, 0)
        window.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        setTopDrawable(R.mipmap.ad_person, tv_person)
        setTopDrawable(R.mipmap.ad_merchants, tv_merchants)
        tv_cancel.setOnClickListener(onClick)
        tv_person.setOnClickListener(onClick)
        tv_merchants.setOnClickListener(onClick)
    }

    @SuppressLint("SetTextI18n")
    fun setType(type: Int) {
        var name: String
        XspManage.getInstance().adType = type
        tv_text4.visibility = View.GONE
        when (type) {
            1 -> {
                name = "DIY信息发布规则"
                tv_type_name.text = name
                setDIYText()
            }
            2 -> {
                name = "便民信息发布规则"
                tv_type_name.text = name
                setBMText()
            }
            3 -> {
                name = "随机信息发布规则"
                tv_type_name.text = name
                setDIYRText()
            }
            7->{
                name = "新媒体发布规则"
                tv_type_name.text = name
                setNewMediaText()
            }
            else -> {
                if (XspManage.getInstance().bianMinPing == 1) {
                    name = "拼屏信息发布规则"
                    setBMPingText()
                } else {
                    name = "随机便民信息发布规则"
                    setBMRText()
                }
                tv_type_name.text = name
            }
        }
        show()
    }
    private fun setNewMediaText(){
        tv_text1.text = "支持图文 / 视频"
        tv_text2.text = "可自由选择发布时间 支持多天"
        tv_text3.text = "可自由选择发布城市 / 商场 / 屏幕"
        tv_text4.visibility = View.GONE
        tv_text5.visibility = View.GONE
    }
    private fun setBMText() {
        tv_text1.text = "支持文字信息内容发布，输入上限60字"
        tv_text2.text = "可自由选择发布时间 支持多天/整周/整月"
        tv_text3.text = "可自由选择发布城市 / 小区 / 电梯 / 屏幕"
       // tv_text4.visibility = View.VISIBLE
    }

    private fun setBMRText() {
        tv_text1.text = "支持文字信息内容发布，输入上限60字"
        tv_text2.text = "可自由选择发布时长，不可选择时间"
        tv_text3.text = "可自由选择发布城市 / 小区，不可选择屏幕"
        //tv_text4.visibility = View.VISIBLE
    }

    private fun setBMPingText() {
        tv_text1.text = "屏幕位置随机排列"
        tv_text2.text = "支持文字信息内容发布，输入上限60字"
        tv_text3.text = "可自由选择发布时长，不可选择时间"
        tv_text4.text = "可自由选择发布城市 / 小区，不可选择屏幕"
        //tv_text4.visibility = View.VISIBLE
        //tv_text5.text = "可选择显示或隐藏落款姓氏 / 全名 / 电话"
    }

    private fun setDIYText() {
        tv_text1.text = "支持图文 / 视频等内容"
        tv_text2.text = "自由选择发布时间 支持多天 / 整周 / 整月"
        tv_text3.text = "可自由选择发布城市 / 小区 / 电梯 / 屏幕"
        //tv_text4.visibility = View.VISIBLE
    }

    private fun setDIYRText() {
        tv_text1.text = "支持图文 / 视频等内容"
        tv_text2.text = "可自由选择发布时长，不可选择时间"
        tv_text3.text = "可自由选择发布城市/小区，不可选择屏幕"
        //tv_text4.visibility = View.VISIBLE
    }


    var onTypeSelectListener: OnTypeSelectListener? = null

    interface OnTypeSelectListener {
        fun selectPerson()
        fun selectMerchamts()
    }
}