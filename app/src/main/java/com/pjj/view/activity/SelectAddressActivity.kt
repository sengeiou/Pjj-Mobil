package com.pjj.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager

import com.pjj.R
import com.pjj.contract.SelectAddressContract
import com.pjj.module.AddressBean
import com.pjj.present.SelectAddressPresent
import com.pjj.view.adapter.AddressAdapter
import kotlinx.android.synthetic.main.activity_select_address.*

/**
 * Create by xinheng on 2019/04/12 13:55。
 * describe：选择地址
 */
class SelectAddressActivity : BaseActivity<SelectAddressPresent>(), SelectAddressContract.View {
    private val addressAdapter = AddressAdapter().apply {
        onAddressAdapterListener = object : AddressAdapter.OnAddressAdapterListener {
            override fun itemClick(data: AddressBean.AddressData) {
                if (!notBack) {
                    setResult(RESULT_OK, Intent().apply {
                        putExtra("name", data.name)
                        putExtra("phone", data.phone)
                        putExtra("position", data.position)
                        putExtra("address", data.describe)
                    })
                    finish()
                }
            }

            override fun updateAddress(data: AddressBean.AddressData) {
                CreateAddressActivity.startActivityForResult(this@SelectAddressActivity, 303, "update", data)
            }
        }
    }
    private var notBack = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_address)
        setBlackTitle("选择地址")
        notBack = intent.getBooleanExtra("notBack", notBack)
        rv_address.layoutManager = LinearLayoutManager(this)
        rv_address.adapter = addressAdapter
        mPresent = SelectAddressPresent(this)
        mPresent?.loadAddressListTask()
        tv_add.setOnClickListener(onClick)
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_add -> CreateAddressActivity.startActivityForResult(this, 303, CreateAddressActivity.TYPE_CREATE)
        }
    }

    override fun updateAddressList(dataList: MutableList<AddressBean.AddressData>?) {
        addressAdapter.list = dataList
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == 303 && resultCode == RESULT_OK -> {
                if (!notBack) {
                    setResult(RESULT_OK, Intent().apply {
                        putExtra("name", data?.getStringExtra("name"))
                        putExtra("phone", data?.getStringExtra("phone"))
                        putExtra("position", data?.getStringExtra("position"))
                        putExtra("address", data?.getStringExtra("address"))
                    })
                    finish()
                } else {
                    mPresent?.loadAddressListTask()
                }
            }
            requestCode == 303 && resultCode == 404 -> mPresent?.loadAddressListTask()
        }
    }
}
