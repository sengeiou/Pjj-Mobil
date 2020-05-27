package com.pjj.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import com.pjj.R
import com.pjj.contract.SelectBMTypeContract
import com.pjj.module.BmTypeBean
import com.pjj.present.SelectBMTypePresent
import com.pjj.utils.SpaceItemDecoration
import com.pjj.utils.ViewUtils
import com.pjj.view.adapter.SelectBMAdapter
import kotlinx.android.synthetic.main.activity_select_bmtype.*

class SelectBMTypeActivity : BaseActivity<SelectBMTypePresent>(), SelectBMTypeContract.View {
    companion object {
        val BM_TYPE_TAG = "bm_type_tag"
        val BM_TYPE_NAME = "bm_type_name"
        fun instanceForResult(activity: Activity, requestCode: Int, name: String? = null) {
            activity.startActivityForResult(Intent(activity, SelectBMTypeActivity::class.java).putExtra(BM_TYPE_NAME, name), requestCode)
        }
    }

    private lateinit var adapter: SelectBMAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_bmtype)
        setTitle("选择分类")
        var name = intent.getStringExtra(BM_TYPE_NAME)
        if (null == name) {
            tv_now_select_statue.text = "未选择"
            tv_now_select.text = ""
        } else {
            tv_now_select_statue.text = "当前选择"
            tv_now_select.text = name
        }
        mPresent = SelectBMTypePresent(this)
        rv.adapter = SelectBMAdapter().apply {
            adapter = this
            onBMSelectListener = object : SelectBMAdapter.OnBMSelectListener {
                override fun itemClick(name: String) {
                    setFinishResult(name)
                }
            }
        }
        rv.addItemDecoration(SpaceItemDecoration(this, RecyclerView.VERTICAL, ViewUtils.getDp(R.dimen.dp_1), ViewUtils.getColor(R.color.color_f1f1f1)))
        mPresent?.loadBmTypeTask()
    }

    private fun setFinishResult(name: String) {
        setResult(Activity.RESULT_OK, Intent().putExtra(BM_TYPE_TAG, name))
        finish()
    }

    override fun updateList(data: MutableList<BmTypeBean.DataBean>) {
        adapter.list = data
    }
}
