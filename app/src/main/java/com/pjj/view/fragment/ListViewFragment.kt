package com.pjj.view.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView
import com.pjj.R
import com.pjj.present.BasePresent
import com.pjj.utils.*
import com.pjj.view.adapter.ListViewAdapter
import com.pjj.view.pulltorefresh.BaseRefreshListener
import com.pjj.view.pulltorefresh.PullToRefreshLayout
import kotlinx.android.synthetic.main.fragment_list_view.*

/**
 * A simple [Fragment] subclass.
 *
 */
abstract class ListViewFragment<T, P : BasePresent<*>> : BaseFragment<P>() {
    protected var pullToRefresh: PullToRefreshLayout? = null
        private set
    private var noDataView: View? = null

    protected lateinit var listAdapter: ListViewAdapter<T, *>
        private set
    lateinit var pullRefreshHelper: PullRefreshHelper<T>

    override fun getLayoutRes(): Int {
        return R.layout.fragment_list_view
    }

    private var refreshAddLoadMoreStatue = true
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pullToRefresh = view.findViewById(R.id.pull_to_refresh)
        pullToRefresh!!.setCanLoadMore(refreshAddLoadMoreStatue)
        pullToRefresh!!.setCanRefresh(refreshAddLoadMoreStatue)
        pullRefreshHelper = PullRefreshHelper<T>(pullToRefresh!!).apply {
            onPullRefreshHelperListener = object : PullRefreshHelper.OnPullRefreshHelperListener<T> {
                override fun loadData(start: Int, num: Int) {
                    this@ListViewFragment.loadData(start, num)
                }

                override fun iniList(list: MutableList<T>?) {
                    listAdapter.list = list
                }

                override fun addList(list: MutableList<T>) {
                    listAdapter.addMore(list)
                    rv_list.postDelayed({
                        rv_list.smoothScrollBy(0, ViewUtils.getDp(R.dimen.dp_30))
                    }, 200)
                }

            }
        }
        //tvNoDate = view.findViewById(R.id.tv_no_data_explain)
        rv_list.layoutManager = getLayoutManager()
        listAdapter = getAdapter()
        rv_list.adapter = listAdapter
        if (isAddSpace()) {
            val dp5 = ViewUtils.getDp(R.dimen.dp_5)
            rv_list.setPadding(dp5, dp5, dp5, dp5)
            rv_list.addItemDecoration(SpaceItemDecoration(activity!!, LinearLayoutManager.VERTICAL, dp5, ViewUtils.getColor(R.color.color_f1f1f1)))
        }
        //pullToRefresh.setCanLoadMore(false)
        pullToRefresh?.setRefreshListener(object : BaseRefreshListener {
            override fun refresh() {
                startRefresh()
            }

            override fun loadMore() {
                pullRefreshHelper.loadMore()
            }

        })
        startRefresh()
    }

    fun startRefresh() {
        pullRefreshHelper.refresh()
    }

    fun changeRefreshAddLoadMoreStatue(tag: Boolean) {
        refreshAddLoadMoreStatue = tag
        pullToRefresh?.setCanRefresh(tag)
        pullToRefresh?.setCanLoadMore(tag)
    }

    protected open fun getLayoutManager(): RecyclerView.LayoutManager {
        return LinearLayoutManager(activity)
    }

    protected abstract fun getAdapter(): ListViewAdapter<T, *>
    protected open fun isAddSpace(): Boolean {
        return true
    }

    protected abstract fun loadData(start: Int, num: Int)

    protected fun setData(list: MutableList<T>?) {
        pullRefreshHelper.updateResult(list)
        val itemCount = listAdapter.itemCount
        if (itemCount == 0) {
            if (null == noDataView) {
                noDataView = id_no_data.inflate()
            } else {
                noDataView?.visibility = View.VISIBLE
            }
            val textView = noDataView!!.findViewById<TextView>(R.id.tv_text)
            textView.text = noDataText
        } else {
            noDataView?.visibility = View.GONE
        }
    }

    private var noDataText = "暂无数据"
    protected fun setNoDataText(text: String) {
        noDataText = text
    }

    override fun showNotice(msg: String?) {
        pullToRefresh?.finishRefresh()
        pullToRefresh?.finishLoadMore()
        super.showNotice(msg)
    }
}
