package com.pjj.view.activity

import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import com.google.gson.JsonArray
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.contract.JianKangContract
import com.pjj.module.parameters.JiangKang
import com.pjj.present.JianKangPresent
import com.pjj.utils.TextUtils
import com.pjj.utils.UserUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.dialog.CityDialog
import com.pjj.view.dialog.DateDialog
import com.pjj.view.dialog.SexDialog
import kotlinx.android.synthetic.main.activity_jian_kang.*
import java.util.*


class JianKangActivity : BaseActivity<JianKangPresent>(), JianKangContract.View {
    private var areaId: String? = null
    private var sexTag: String? = null
    private lateinit var jianKang: JiangKang
    private val sexDialog: SexDialog by lazy {
        SexDialog(this).apply {
            onSexDialogListener = object : SexDialog.OnSexDialogListener {
                override fun selectSex(sex: String, sexTag: String) {
                    this@JianKangActivity.tv_sex.text = sex
                    this@JianKangActivity.sexTag = sexTag
                }

                override fun unSelectNotice() {
                    toast("请选择性别")
                }
            }
        }
    }
    private val dateDialog: DateDialog by lazy {
        DateDialog(this).apply {
            setSelectDay(Calendar.getInstance())
            onDateSelectListener = object : DateDialog.OnDateSelectListener {
                override fun dateSelect(calendar: Calendar) {
                    var date = calendar.let {
                        "${it[Calendar.YEAR]}-${TextUtils.format(it[Calendar.MONTH] + 1)}-${TextUtils.format(it[Calendar.DATE])}"
                    }
                    this@JianKangActivity.tv_time.text = date
                }

                override fun dateSelect(dates: String) {}

            }
        }
    }
    private var cityDialog: CityDialog? = null
    private var showTag = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_jian_kang)
        setTitle("健康预约")
        mPresent = JianKangPresent(this)
        jianKang = JiangKang()
        tv_submit.setOnClickListener(onClick)
        tv_sex.setOnClickListener(onClick)
        tv_time.setOnClickListener(onClick)
        tv_address_front.setOnClickListener(onClick)
        this@JianKangActivity.tv_submit.isEnabled = false
        this@JianKangActivity.tv_submit.background = ColorDrawable(ViewUtils.getColor(R.color.color_d2d2d2))
        var changeListener = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                showTag = false
                if (check()) {
                    if (!this@JianKangActivity.tv_submit.isEnabled) {
                        this@JianKangActivity.tv_submit.isEnabled = true
                        this@JianKangActivity.tv_submit.background = ColorDrawable(ViewUtils.getColor(R.color.color_theme))
                    }
                } else {
                    if (this@JianKangActivity.tv_submit.isEnabled) {
                        this@JianKangActivity.tv_submit.isEnabled = false
                        this@JianKangActivity.tv_submit.background = ColorDrawable(ViewUtils.getColor(R.color.color_d2d2d2))
                    }
                }
                showTag = true
            }

        }
        tv_name.addTextChangedListener(changeListener)
        tv_age.addTextChangedListener(changeListener)
        tv_phone_self.addTextChangedListener(changeListener)
        tv_phone.addTextChangedListener(changeListener)
        tv_address.addTextChangedListener(changeListener)
        et_note.addTextChangedListener(changeListener)


    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_submit -> {
                showTag = true
                if (check()) {
                    jianKang.userId = PjjApplication.application.userId
                    mPresent?.commit(jianKang)
                }
            }
            R.id.tv_sex -> sexDialog.show()
            R.id.tv_time -> dateDialog.show()
            R.id.tv_address_front -> {
                if (null == cityDialog) {
                    showWaiteStatue()
                    mPresent?.loadAreaData()
                } else {
                    cityDialog!!.show()
                }
            }
        }
    }

    private fun check(): Boolean {
        noticeDialog.updateImage(R.mipmap.cry_white)
        var tv_name = tv_name.text.toString()
        if (tv_name.isEmpty()) {
            showNotice("请输入预约人姓名")
            return false
        }
        if (showTag) {
            if (!checkHanZi(tv_name)) {
                showNotice("请输入正确的姓名")
                return false
            }
        }
        jianKang.personName = tv_name
        if (TextUtils.isEmpty(sexTag)) {
            showNotice("请选择性别")
            return false
        }
        jianKang.sex = sexTag
        var tv_age = tv_age.text.toString()
        if (tv_age.isEmpty()) {
            showNotice("请输入年龄")
            return false
        }
        if (showTag) {
            if (tv_age.toInt() < 1 || tv_age.toInt() > 150) {
                showNotice("请输入正确的年龄")
                return false
            }
        }
        jianKang.age = tv_age
        var tv_phone_self = tv_phone_self.text.toString()
        if (tv_phone_self.isEmpty()) {
            showNotice("请输入联系电话")
            return false
        }
        if (showTag) {
            var rightPhone = UserUtils.isRightPhone(tv_phone_self)
            if (null != rightPhone) {
                showNotice("请输入正确的联系电话")
                return false
            }
        }
        jianKang.phone = tv_phone_self
        if (TextUtils.isEmpty(areaId)) {
            showNotice("请选择所在地区")
            return false
        }
        jianKang.areaId = areaId
        var tv_address = tv_address.text.toString()
        if (tv_address.isEmpty()) {
            showNotice("请输入详细地址")
            return false
        }
        jianKang.address = tv_address
        var tv_doctor = tv_doctor.text.toString()
        if (tv_doctor.isEmpty()) {
            showNotice("请输入预约医院")
            return false
        }
        if (showTag) {
            if (!checkHanZi(tv_doctor)) {
                showNotice("请输入正确的医师姓名")
                return false
            }
        }
        jianKang.physicianName = tv_doctor
        var tv_content = tv_content.text.toString()
        if (tv_content.isEmpty()) {
            showNotice("请输入诊疗内容")
            return false
        }
        if (showTag) {
            if (!checkHanZi(tv_content)) {
                showNotice("请输入正确的诊疗内容")
                return false
            }
        }
        jianKang.content = tv_content
        var tv_time = tv_time.text.toString()
        if (tv_time.isEmpty()) {
            showNotice("请选择诊疗时间")
            return false
        }
        jianKang.appointmentTime = tv_time
        var tv_phone = tv_phone.text.toString()
        if (tv_phone.isEmpty()) {
            showNotice("请输入推荐人电话")
            return false
        }
        if (showTag) {
            var rightPhone = UserUtils.isRightPhone(tv_phone)
            if (null != rightPhone) {
                showNotice("请输入正确的推荐人电话")
                return false
            }
        }
        jianKang.recommendPhone = tv_phone
        var et_note = et_note.text.toString()
        if (et_note.isEmpty()) {
            showNotice("请输入备注信息")
            return false
        }
        if (showTag) {
            if (!checkHanZiAN(et_note)) {
                showNotice("请输入正确的备注信息")
                return false
            }
        }
        jianKang.remarks = et_note
        return true
    }

    override fun showNotice(error: String?) {
        if (showTag)
            super.showNotice(error)

    }

    override fun getCityData(array: JsonArray?) {
        cityDialog = CityDialog(this, array).apply {
            onCityDialogListener = object : CityDialog.OnCityDialogListener {
                override fun notice(msg: String) {
                    noticeDialog.updateImage(R.mipmap.cry_white)
                    showNotice(msg)
                }

                override fun selectAreaId(areaId: String, province: String, city: String, area: String) {
                    this@JianKangActivity.areaId = areaId
                    this@JianKangActivity.tv_address_front.text = "$province$city$area"
                }
            }
        }
    }

    private fun checkHanZiAN(s: String): Boolean {
        //var pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE)
        s.forEach {
            if (!isChinese1(it)) {
                return false
            }
        }
        return true
    }

    private fun checkHanZi(s: String): Boolean {
        //var pattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE or Pattern.CASE_INSENSITIVE)
        s.forEach {
            if (!isChinese(it)) {
                return false
            }
        }
        return true
    }

    private fun isChinese(c: Char): Boolean {
        if (c.toInt() in 0x4E00..0x9FA5) {
            return true
        } else if (((c in 'a'..'z') || (c in 'A'..'Z'))) {
            return true
        }
        return false
    }

    private fun isChinese1(c: Char): Boolean {
        if (c.toInt() in 0x4E00..0x9FA5) {
            return true
        } else if (((c in 'a'..'z') || (c in 'A'..'Z'))) {
            return true
        } else if ((c in '0'..'9')) {
            return true
        }

        return false
    }

    override fun commitSuccess() {
        showToast("提交成功")
        finish()
    }
}
