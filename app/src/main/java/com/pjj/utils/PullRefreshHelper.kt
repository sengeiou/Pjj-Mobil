package com.pjj.utils

import com.pjj.view.pulltorefresh.BaseRefreshListener
import com.pjj.view.pulltorefresh.PullToRefreshLayout

/**
 * Created by XinHeng on 2019/04/15.
 * describe：刷新工具
 */
class PullRefreshHelper<T>(private var pullToRefreshLayout: PullToRefreshLayout, private var firstIndex: Int = 1, loadMoreNum: Int = 10) {
    var loadMoreNum: Int
        private set
    var startIndex: Int
        private set

    init {
        this.startIndex = firstIndex
        this.loadMoreNum = loadMoreNum
        //先禁止加载更多
        //pullToRefreshLayout.setCanLoadMore(false)
        pullToRefreshLayout.setRefreshListener(object : BaseRefreshListener {
            override fun refresh() {
                this@PullRefreshHelper.refresh()
            }

            override fun loadMore() {
                this@PullRefreshHelper.loadMore()
            }

        })
    }

    fun loadMore() {
        onPullRefreshHelperListener?.loadData(startIndex, loadMoreNum)
    }

    fun refresh() {
        startIndex = firstIndex
        onPullRefreshHelperListener?.loadData(startIndex, loadMoreNum)
    }

    /**
     * @param list 新增的
     */
    fun updateResult(list: MutableList<T>?) {
        pullToRefreshLayout.finishRefresh()
        pullToRefreshLayout.finishLoadMore()
        val size = list?.size ?: 0
        if (startIndex == firstIndex) {
            onPullRefreshHelperListener?.iniList(list)
        } else {
            if (size > 0) {
                onPullRefreshHelperListener?.addList(list!!)
            }
        }
        if (size > 0) {
            startIndex++
        }
        //pullToRefreshLayout.setCanLoadMore(count >= loadMoreNum)
    }

    fun isRefresh(): Boolean {
        return startIndex == firstIndex
    }

    var onPullRefreshHelperListener: OnPullRefreshHelperListener<T>? = null

    interface OnPullRefreshHelperListener<T> {
        fun loadData(start: Int, num: Int)
        fun iniList(list: MutableList<T>?)
        fun addList(list: MutableList<T>)
    }

}