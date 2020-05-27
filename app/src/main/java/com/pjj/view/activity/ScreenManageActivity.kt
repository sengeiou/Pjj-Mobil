package com.pjj.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View

import com.pjj.R
import com.pjj.contract.ScreenManageContract
import com.pjj.module.ScreenBean
import com.pjj.module.xspad.XspManage
import com.pjj.present.ScreenManagePresent
import com.pjj.utils.*
import kotlinx.android.synthetic.main.activity_screenmanage.*

/**
 * Create by xinheng on 2019/05/20 17:55。
 * describe：屏幕管理
 */
class ScreenManageActivity : BaseActivity<ScreenManagePresent>(), ScreenManageContract.View {
    private lateinit var useType: String
    private lateinit var queryType: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screenmanage)
        setTitle("屏幕管理")
        useType = SharedUtils.getXmlForKey(SharedUtils.USER_TYPE)
        Log.e("TAG", "useType=$useType")
        if (useType == "companyLaolingwei") {
            useType = intent.getStringExtra("useType")
        }
        queryType = when (useType) {//"5 estateManagement物业 6 cooperativePartner合作伙伴"
            "estateManagement" -> {
                tv_title_order.visibility = View.GONE
                tv_release.visibility = View.GONE
                tv_set.text = "确定"
                tv_set.background = ViewUtils.getDrawable(R.drawable.shape_theme_bg_19)
                "2"
            }
            else -> {
                tv_title_order.visibility = View.VISIBLE
                tv_title_order.setOnClickListener(onClick)
                "1"
            }
        }
        if ("subManagement" == useType) {
            tv_set.visibility = View.GONE
            tv_release.background = ViewUtils.getDrawable(R.drawable.shape_fe8024_bg_19)
        }
        tv_set.setOnClickListener(onClick)
        tv_release.setOnClickListener(onClick)
        iv_select_all.setOnClickListener(onClick)
        //合作伙伴 1 4
        //物业单位
        rv_screen.addItemDecoration(SpaceItemDecoration(this, LinearLayoutManager.VERTICAL,
                ViewUtils.getDp(R.dimen.dp_5), ViewUtils.getColor(R.color.color_f1f1f1)))
        rv_screen.layoutManager = LinearLayoutManager(this)
        rv_screen.adapter = adapterScreen
        mPresent = ScreenManagePresent(this)
        mPresent?.loadScreenDataTask(queryType)
        XspManage.getInstance().refreshTag.tagScreenManageActivity = false
    }

    override fun onResume() {
        super.onResume()
        if (XspManage.getInstance().refreshTag.tagScreenManageActivity) {
            XspManage.getInstance().refreshTag.tagScreenManageActivity = false
            mPresent?.loadScreenDataTask(queryType)
        }
    }

    override fun updateData(list: MutableList<ScreenBean.DataBean>?) {
        adapterScreen.list = list
    }

    private val adapterScreen: ScreenManageAdapter by lazy {
        ScreenManageAdapter(true).apply {
            onScreenManageAdapterListener = object : ScreenManageAdapter.OnScreenManageAdapterListener {
                override fun isAllSelect(isAll: Boolean) {
                    iv_select_all.setImageResource(if (isAll) R.mipmap.select else R.mipmap.unselect)
                }

                override fun notice(msg: String) {
                    //showNotice(msg)
                }
            }
        }
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_title_order -> startActivity(Intent(this, ScreenManageOrderListActivity::class.java))
            R.id.iv_select_all -> adapterScreen.changeAllSelect()
            R.id.tv_set -> {
                val list = adapterScreen.getSelectData()
                if (!TextUtils.isNotEmptyList(list)) {
                    showNotice("您还没有选择屏幕")
                    return
                }
                if (useType == "estateManagement") {
                    startActivityForResult(Intent(this, ScreenManageSettingBmActivity::class.java).putExtra("screenIds", getString(list)), 303)
                } else {
                    var typeModel = "-1"
                    var priceS = ""
                    if (list.size == 1) {
                        typeModel = list[0].cooperationMode
                        val realPrice = list[0].realPrice
                        if (realPrice - realPrice.toInt() > 0) {
                            priceS = realPrice.toString()
                        } else {
                            priceS = realPrice.toInt().toString()
                        }
                    }
                    startActivityForResult(Intent(this, ScreenManageSettingTypeActivity::class.java)
                            .putExtra("screenIds", getString(list, true))
                            .putExtra("typeModel", typeModel)
                            .putExtra("defaultPrice", priceS)
                            , 303)
                }
            }
            R.id.tv_release -> {
                val list = adapterScreen.getSelectData()
                if (!TextUtils.isNotEmptyList(list)) {
                    showNotice("您还没有选择屏幕")
                    return
                }
                startActivity(Intent(this, ScreenManageReleaseActivity::class.java)
                        .putExtra("screenList", getStringList(list))
                        .putExtra("allSelfTag", allSelfTag)
                )
            }
        }
    }

    private var allSelfTag = false
    private fun getString(list: MutableList<ScreenBean.DataBean>?, tag: Boolean = false): String {
        val buffer = StringBuffer()
        allSelfTag = true
        list?.forEach {
            if (it.cooperationMode == "1") {
                allSelfTag = false
            }
            buffer.append(it.screenId)
            if (tag) {
                buffer.append("&")
                buffer.append(it.screenLocation)
            }
            buffer.append(",")
        }
        if (buffer.isNotEmpty()) {
            buffer.deleteCharAt(buffer.length - 1)
        }
        return buffer.toString()
    }

    private fun getStringList(list: MutableList<ScreenBean.DataBean>?): ArrayList<String> {
        var list1 = ArrayList<String>()
        allSelfTag = true
        list?.forEach {
            list1.add(it.screenId)
            if (it.cooperationMode == "1") {
                allSelfTag = false
            }
        }
        return list1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when {
            requestCode == 303 && resultCode == 302 -> {
                adapterScreen.cleanSelect()
                mPresent?.loadScreenDataTask(queryType)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
