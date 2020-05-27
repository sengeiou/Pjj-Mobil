package com.pjj.view.fragment


import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View

import com.pjj.R
import com.pjj.contract.IntegralMallContract
import com.pjj.module.GoodsListBean
import com.pjj.module.MallClassificationBean
import com.pjj.present.IntegralMallPresent
import com.pjj.utils.Log
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.activity.GoodsDetailsActivity
import com.pjj.view.activity.MallGoodsDetailsActivity
import com.pjj.view.adapter.IntegralMallAdapter
import com.pjj.view.adapter.ListViewAdapter
import kotlinx.android.synthetic.main.fragment_list_view.*


/**
 * A simple [Fragment] subclass.
 * Use the [IntegralMallFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class IntegralMallFragment : ListViewFragment<GoodsListBean.GoodsListData, IntegralMallPresent>(), IntegralMallContract.View {

    private var mParam1: String = ""
    private var mallTag = false
    private var canScroll = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            mParam1 = arguments!!.getString(ARG_PARAM1)!!
            mallTag = arguments!!.getBoolean(ARG_PARAM2)
        }
    }

    companion object {
        private val ARG_PARAM1 = "param1"
        private val ARG_PARAM2 = "paramMallTag"
        fun newInstance(param1: String, mallTag: Boolean): IntegralMallFragment {
            val fragment = IntegralMallFragment()
            val args = Bundle()
            args.putString(ARG_PARAM1, param1)
            args.putBoolean(ARG_PARAM2, mallTag)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mPresent = IntegralMallPresent(this, mallTag)
        super.onViewCreated(view, savedInstanceState)
        val dp15 = ViewUtils.getDp(R.dimen.dp_13)
        val dp10 = ViewUtils.getDp(R.dimen.dp_10)
        pullToRefresh?.setCanLoadMore(false)
        pullToRefresh?.setCanRefresh(false)
        rv_list.setPadding(dp15, 0, 0, dp10)
        rv_list.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
                outRect.top = dp10
                if (parent.getChildLayoutPosition(view) % 2 == 0) {
                    outRect.right = dp10
                } else {
                    //outRect.set(dp10, dp15, 0, 0)
                }
            }
        })
        rv_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                Log.e("TAG", "onScrolled: dy=$dy")
                val canScrollVerticallyToUp = recyclerView.canScrollVertically(1)//false表示不能往上滑动
                val canScrollVerticallyToDown = recyclerView.canScrollVertically(-1)//false表示不能向下滑动
            }
        })
        //rv_list.canScroll = canScroll
        setNoDataText("暂无商品")
    }

    fun canScrollVerticallyToUp(): Boolean {
        return rv_list.canScrollVertically(1)//false表示不能往上滑动
    }

    fun canScrollVerticallyToDown(): Boolean {
        return rv_list.canScrollVertically(-1)//false表示不能向下滑动
    }

    fun setRecycleViewCanScrollStatue(tag: Boolean) {
//        canScroll = tag
//        if (null != pullToRefresh && null != rv_list)
//            rv_list.canScroll = tag
    }

    override fun isAddSpace(): Boolean {
        return false
    }

    override fun getAdapter(): ListViewAdapter<GoodsListBean.GoodsListData, *> {
        return IntegralMallAdapter(mallTag).apply {
            onIntegralMallAdapterListener = object : IntegralMallAdapter.OnIntegralMallAdapterListener {
                override fun itemClick(ivPath: String, goods: GoodsListBean.GoodsListData) {
                    if (mallTag) {
                        startActivity(Intent(activity!!, MallGoodsDetailsActivity::class.java).putExtra("goodsId", goods.integraGoodsId))
                    } else {
                        startActivity(Intent(activity!!, GoodsDetailsActivity::class.java).putExtra("goods_inf", goods))
                    }
                }
            }
        }
    }

    override fun getLayoutManager(): RecyclerView.LayoutManager {
        return object : GridLayoutManager(activity!!, 2) {
            override fun scrollVerticallyBy(dy: Int, recycler: RecyclerView.Recycler?, state: RecyclerView.State?): Int {
                val scrollVerticallyBy = super.scrollVerticallyBy(dy, recycler, state)
                val needIntercept = onIntegralMallFragmentListener?.parentIsNeedIntercept() ?: false
                Log.e("TAG", "scrollVerticallyBy: $scrollVerticallyBy , needIntercept=$needIntercept")
                if (!needIntercept && scrollVerticallyBy == 0) {
                    onIntegralMallFragmentListener?.setCanLoadMoreStatue(true)
                }
                return if (needIntercept) 0 else scrollVerticallyBy
            }

            override fun canScrollVertically(): Boolean {
                val needIntercept = onIntegralMallFragmentListener?.parentIsNeedIntercept() ?: false
                Log.e("TAG", "canScrollVertically: needIntercept=$needIntercept")
                return if (needIntercept) false else super.canScrollVertically()
            }
        }
    }

    override fun loadData(start: Int, num: Int) {
        mPresent?.loadMall(start, num, mParam1)
    }

    override fun updateClassificationData(data: MutableList<MallClassificationBean.DataBean>) {
    }

    override fun updateList(list: MutableList<GoodsListBean.GoodsListData>?) {
        setData(list)
        if (!pullRefreshHelper.isRefresh() && !TextUtils.isNotEmptyList(list)) {
            showNotice("暂无更多商品")
        }
        onIntegralMallFragmentListener?.finishLoad()
    }

    var onIntegralMallFragmentListener: OnIntegralMallFragmentListener? = null

    interface OnIntegralMallFragmentListener {
        //fun canScrollStatue(canToUp: Boolean, canToDown: Boolean)
        fun parentIsNeedIntercept(): Boolean

        fun setCanLoadMoreStatue(statue: Boolean)
        fun finishLoad()
    }
}
