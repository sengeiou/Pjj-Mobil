package com.pjj.view.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.google.gson.JsonArray

import com.pjj.R
import com.pjj.contract.CreateAddressContract
import com.pjj.module.AddressBean
import com.pjj.present.CreateAddressPresent
import com.pjj.utils.TextUtils
import com.pjj.utils.UserUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.dialog.AddTemplateDialog
import com.pjj.view.dialog.CityDialog
import kotlinx.android.synthetic.main.activity_create_address.*
import kotlinx.android.synthetic.main.activity_exchange_record_inf.*

/**
 * Create by xinheng on 2019/04/11 16:57。
 * describe：新建地址
 */
class CreateAddressActivity : BaseActivity<CreateAddressPresent>(), CreateAddressContract.View {
    private var cityDialog: CityDialog? = null
    private var areaCode: String? = null
    private var addressData: AddressBean.AddressData? = null
    private var type: String = TYPE_CREATE

    companion object {
        private val POSITION_TYPE = "position_type"
        private val ADDRESS_ID = "address_id"
        val TYPE_CREATE = "create"
        @JvmStatic
        fun startActivityForResult(activity: BaseActivity<*>, requestCode: Int, type: String, addressData: AddressBean.AddressData? = null) {
            activity.startActivityForResult(Intent(activity, CreateAddressActivity::class.java).putExtra(POSITION_TYPE, type).putExtra(ADDRESS_ID, addressData), requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_address)
        tv_person_local.setOnClickListener(onClick)
        tv_save.setOnClickListener(onClick)
        mPresent = CreateAddressPresent(this)
        type = intent.getStringExtra(POSITION_TYPE)
        if (type != TYPE_CREATE) {
            setBlackTitle("编辑地址")
            tv_delete.visibility = View.VISIBLE
            tv_delete.setOnClickListener(onClick)
            addressData = intent.getParcelableExtra(ADDRESS_ID)
            addressData?.let {
                areaCode = it.areaCode
                et_name.setText(it.name)
                et_phone.setText(it.phone)
                tv_person_local.text = it.position
                et_person_address.setText(it.describe)
                when (it.isDefault) {
                    "1" -> default_switch.isChecked = true
                    else -> default_switch.isChecked = false
                }
            }
        } else {
            setBlackTitle("新建地址")
            tv_delete.visibility = View.GONE
        }
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_person_local -> if (null == cityDialog) {
                showWaiteStatue()
                mPresent?.loadAreaData()
            } else {
                cityDialog!!.show()
            }
            R.id.tv_save -> check()
            R.id.tv_delete -> deleteDialog.show()
        }
    }

    private val deleteDialog: AddTemplateDialog by lazy {
        AddTemplateDialog(this).apply {
            setImageResource(R.mipmap.local_big, false, ViewUtils.getDp(R.dimen.dp_82), ViewUtils.getDp(R.dimen.dp_64))
            leftText = "删除"
            notice = "确定要删除该地址吗？"
            onItemClickListener = object : AddTemplateDialog.OnItemClickListener {
                override fun leftClick() {
                    mPresent?.loadDeleteAddressTask(addressData!!.addressId!!)
                }
            }
        }
    }

    override fun deleteSuccess() {
        setResult(404)
        finish()
    }

    private fun check() {
        val name = et_name.text.toString()
        if (TextUtils.isEmpty(name)) {
            showNotice("请输入收货人姓名")
            return
        }
        if (!checkHanZi(name)) {
            showNotice("请输入正确的收货人姓名")
            return
        }
        val phone = et_phone.text.toString()
        if (TextUtils.isEmpty(phone)) {
            showNotice("请输入收货人手机号")
            return
        }
        if (UserUtils.isRightPhone(phone) != null) {
            showNotice("请输入正确的收货人手机号")
            return
        }

        if (TextUtils.isEmpty(areaCode)) {
            showNotice("请选择所在地区")
            return
        }
        val position = tv_person_local.text.toString()
        val describe = et_person_address.text.toString()
        if (TextUtils.isEmpty(describe)) {
            showNotice("请输入详细地址")
            return
        }
        val isDefault = if (default_switch.isChecked) "1" else "2"
        if (TYPE_CREATE == type) {
            mPresent?.insertReceivingAddress(areaCode!!, position, describe, "", phone, name, isDefault)
        } else {
            if (null == addressData!!.addressId) {
                showNotice("错误：地址id为空")
                return
            }
            mPresent?.updateReceivingAddress(addressData!!.addressId!!, areaCode!!, position, describe, "", phone, name, isDefault)
        }
    }

    override fun saveSuccess() {
        setResult(RESULT_OK, Intent().apply {
            putExtra("name", et_name.text.toString())
            putExtra("phone", et_phone.text.toString())
            putExtra("position", tv_person_local.text.toString())
            putExtra("address", et_person_address.text.toString())
        })
        finish()
    }

    override fun getCityData(array: JsonArray?) {
        cityDialog = CityDialog(this, array).apply {
            onCityDialogListener = object : CityDialog.OnCityDialogListener {
                override fun notice(msg: String) {
                    noticeDialog.updateImage(R.mipmap.cry_white)
                    showNotice(msg)
                }

                override fun selectAreaId(areaId: String, province: String, city: String, area: String) {
                    this@CreateAddressActivity.tv_person_local.text = province + city + area
                    this@CreateAddressActivity.areaCode = areaId
                }
            }
        }
        cityDialog!!.show()
    }

    override fun getStatusBarColor(): Int {
        return Color.WHITE
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
}
