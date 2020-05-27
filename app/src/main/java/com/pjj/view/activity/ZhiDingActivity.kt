package com.pjj.view.activity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager

import com.pjj.R
import com.pjj.contract.ZhiDingContract
import com.pjj.module.UserTempletBean
import com.pjj.module.parameters.ZhiDing
import com.pjj.module.xspad.XspManage
import com.pjj.present.ZhiDingPresent
import com.pjj.utils.PullRefreshHelper
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.adapter.ZhiDingAdapter
import com.pjj.view.pulltorefresh.BaseRefreshListener
import com.pjj.view.pulltorefresh.PullToRefreshLayout
import kotlinx.android.synthetic.main.activity_zhiding.*

/**
 * Create by xinheng on 2019/07/25 14:25。
 * describe：置顶 哈哈哈
 */
class ZhiDingActivity : BaseActivity<ZhiDingPresent>(), ZhiDingContract.View {
    private val zhiDing = ZhiDing()
    private var emptyIndex = 0
    private var noFilterInterface = false
    private lateinit var zhiDingAdapter: ZhiDingAdapter
    private lateinit var pullRefreshHelper: PullRefreshHelper<UserTempletBean.DataBean>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_zhiding)
        setStatueWhite()
        mPresent = ZhiDingPresent(this)
        zhiDingAdapter = ZhiDingAdapter(this, true).apply {
            onZhiDingItemClickListener = object : ZhiDingAdapter.OnZhiDingItemClickListener {
                override fun itemClick(bean: UserTempletBean.DataBean) {
                    if (bean.templetType == "6") {
                        WebActivity.newInstance(this@ZhiDingActivity, "外部视频链接", bean.urlContent)
                        return
                    }
                    if (TextUtils.isNotEmptyList(bean.fileList)) {
                        val two = bean.fileList.size == 2
                        if (two) {
                            XspManage.getInstance().newMediaData.preTowData = bean.speedDataBean
                        }
                        val fileBean = bean.fileList[0]
                        //preDialog.setContent(bean.templetName, fileBean.fileUrl, fileBean.type, two)
                        PreviewDialogActivity.startActivity(this@ZhiDingActivity, fileBean.fileUrl, fileBean.type, bean.templetName, two)
                    }
                }
            }
        }
        rv_zhiding.adapter = zhiDingAdapter
        val gridLayoutManager = GridLayoutManager(this, 2)
        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return when (this@ZhiDingActivity.rv_zhiding.adapter.getItemViewType(position)) {
                    0, 1 -> 1
                    else -> 2
                }
            }
            /*override fun getSpanIndex(position: Int, spanCount: Int): Int {
                return position % spanCount
            }*/
        }
        rv_zhiding.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
                //super.getItemOffsets(outRect, view, parent, state)
                val position = parent.getChildLayoutPosition(view) - emptyIndex
                if (position % 2 == 0) {
                    outRect.left = ViewUtils.getDp(R.dimen.dp_17)
                } else {
                    outRect.left = ViewUtils.getDp(R.dimen.dp_8)
                }
            }
        })
        rv_zhiding.layoutManager = gridLayoutManager
        pullRefreshHelper = PullRefreshHelper(pullToRefresh)
        pullRefreshHelper.onPullRefreshHelperListener = object : PullRefreshHelper.OnPullRefreshHelperListener<UserTempletBean.DataBean> {
            override fun loadData(start: Int, num: Int) {
                zhiDing.pageNo = start
                zhiDing.pageNum = num
                if (noFilterInterface) {
                    mPresent!!.loadTuiJianZhiDingTask(zhiDing)
                } else {
                    mPresent!!.loadZhiDingSearchTask(zhiDing)
                }
            }

            override fun iniList(list: MutableList<UserTempletBean.DataBean>?) {
                if (TextUtils.isNotEmptyList(list) && null != list!![0].templetType) {
                    list.add(0, UserTempletBean.DataBean().apply {
                        templetName = "recommended"
                        templet_id = "recommended"
                    })
                    emptyIndex = 1
                }
                zhiDingAdapter.list = list
            }

            override fun addList(list: MutableList<UserTempletBean.DataBean>) {
                zhiDingAdapter.addMore(list)
                rv_zhiding.postDelayed({
                    rv_zhiding.smoothScrollBy(0, ViewUtils.getDp(R.dimen.dp_30))
                }, 200)
            }
        }
        pullToRefresh.setRefreshListener(object : BaseRefreshListener {
            override fun refresh() {
                pullRefreshHelper.refresh()
            }

            override fun loadMore() {
                pullRefreshHelper.loadMore()
            }
        })
        et.setOnEditorActionListener { v, actionId, event ->
            when (actionId) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    val text = v.text.toString()
                    dealWithText(text)
                    true
                }
                else -> false
            }
        }
        pullRefreshHelper.refresh()
        tv_back.setOnClickListener(onClick)
        et.setSelection(et.text.toString().length)
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_back -> finish()
        }
    }

    private fun dealWithText(text: String) {
        val inputMethodManager = applicationContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.showSoftInput(et, 0)
        when (text) {
            //"指导手册"->WebActivity.newInstance(this, "指导手册", "http://protal.test.pingjiajia.cn/guide", "1")
            "" -> {
                WebActivity.newInstance(this, "投放指南", "http://protal.test.pingjiajia.cn/guide/#/guideInfo?dicId=cddb5ce0766e4d3ba8adb2e89940d83b")
                /*noFilterInterface = false
                zhiDing.templetName = null
                pullRefreshHelper.refresh()*/
            }
            else -> {
                noFilterInterface = false
                zhiDing.templetName = text
                pullRefreshHelper.refresh()
            }
        }
    }

    override fun updateDatas(list: MutableList<UserTempletBean.DataBean>?, indexEmpty: Int) {
        emptyIndex = indexEmpty
        //zhiDingAdapter.list = list
        pullRefreshHelper.updateResult(list)
    }

    override fun resetPage() {
        noFilterInterface = true
        pullRefreshHelper.refresh()
    }

}
