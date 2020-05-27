package com.pjj.view.activity

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.View
import com.pjj.PjjApplication

import com.pjj.R
import com.pjj.contract.ScreenshotsListContract
import com.pjj.module.ScreenshotsBean
import com.pjj.module.parameters.OrderStatue
import com.pjj.present.ScreenshotsListPresent
import com.pjj.utils.TextUtils
import com.pjj.view.adapter.ScreenshotsAdapter
import com.pjj.view.pulltorefresh.BaseRefreshListener
import kotlinx.android.synthetic.main.activity_screenshots_list.*

/**
 * Create by xinheng on 2019/03/18 17:52。
 * describe：截屏图片列表
 */
class ScreenshotsListActivity : BaseActivity<ScreenshotsListPresent>(), ScreenshotsListContract.View {
    private lateinit var screenAdapter: ScreenshotsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_screenshots_list)
        setTitle("截屏")
        var orderId = intent.getStringExtra("orderId")
        var screenId = intent.getStringExtra("screenId")
        mPresent = ScreenshotsListPresent(this)
        screenAdapter = ScreenshotsAdapter(this).apply {
            onScreenshotsAdapterListener = object : ScreenshotsAdapter.OnScreenshotsAdapterListener {
                override fun notice(msg: String) {
                    if (msg.contains("成功")) {
                        smillTag = true
                    }
                    showNotice(msg)
                }

                override fun showWaiteNotice() {
                    showWaiteStatue()
                }

                override fun dissmissNotice() {
                    cancelWaiteStatue()
                }

            }
        }
        pullToRefresh.setCanLoadMore(false)
        rv_screenshots.run {
            layoutManager = GridLayoutManager(this@ScreenshotsListActivity, 2)
            adapter = screenAdapter
        }
        if (null != orderId && null != screenId) {
            mPresent?.loadGetScreenshotsListTask(orderId, screenId)
            tv_sure.setOnClickListener {
                mPresent?.screenshots(orderId, screenId)
            }
            pullToRefresh.setRefreshListener(object : BaseRefreshListener {
                override fun loadMore() {
                }

                override fun refresh() {
                    mPresent?.loadGetScreenshotsListTask(orderId, screenId)
                }
            })
        }
    }

    override fun updateList(errInfo: String?, imgs: MutableList<ScreenshotsBean.ImgsBean>) {
        pullToRefresh.finishRefresh()
        if (!TextUtils.isEmpty(errInfo)) {
            showNotice(errInfo)
        }
        screenAdapter.imgs = imgs
        if (!TextUtils.isNotEmptyList(imgs)) {
            ll_no_data.visibility = View.VISIBLE
        } else {
            ll_no_data.visibility = View.GONE
        }
    }

    override fun sendScreenshotsCmdSuccess() {
        noticeDialog.updateImage(R.mipmap.smile)
        noticeDialog.setNotice("截屏获取中，请稍后")
    }

    override fun showNotice(error: String?) {
        pullToRefresh.finishRefresh()
        super.showNotice(error)
    }
}
