package com.pjj.view.fragment

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.contract.MallMySelfContract
import com.pjj.present.MallMySelfPresent
import com.pjj.utils.Log
import com.pjj.utils.SharedUtils
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.activity.MyIntegralActivity
import com.pjj.view.activity.OpinionActivity
import com.pjj.view.activity.SelectAddressActivity
import com.pjj.view.viewinterface.OnFragmentListener
import kotlinx.android.synthetic.main.fragment_mallmyself.*

/**
 * Create by xinheng on 2019/05/08 16:16。
 * describe：商城我的
 */
class MallMySelfFragment : BaseFragment<MallMySelfPresent>(), MallMySelfContract.View {

    private var onFragmentListener: OnFragmentListener? = null
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentListener) {
            onFragmentListener = context
        }
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_mallmyself
    }

    private var requestOptions = RequestOptions().error(ViewUtils.getDrawable(R.mipmap.head_default)).diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ll_address.setOnClickListener(onClick)
        ll_integral.setOnClickListener(onClick)
        ll_my_order.setOnClickListener(onClick)
        ll_mall_question.setOnClickListener(onClick)
        updateHeadInf()
        mPresent = MallMySelfPresent(this)
        mPresent?.loadMyIntegral()
    }

    private fun updateHeadInf() {
        if (SharedUtils.isEffiect()) {
            var path = SharedUtils.getXmlForKey(SharedUtils.HEAD_IMG)
            var name = SharedUtils.getXmlForKey(SharedUtils.USER_NICKNAME)
            tv_name.text = name
            if (!TextUtils.isEmpty(path)) {
                //SharedUtils.saveForXml(SharedUtils.HEAD_IMG, path)
                val s = PjjApplication.filePath + path
                Log.e("TAG", "filePath=$s")
                Glide.with(this).load(s).apply(requestOptions).into(iv_head)
            }
        } else {
            tv_name.text = ""
            iv_head.setImageResource(R.mipmap.head_default)

        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (!hidden) {
            mPresent?.loadMyIntegral()
            updateHeadInf()
        }
        super.onHiddenChanged(hidden)
    }

    private val onClick = View.OnClickListener {
        when (it.id) {
            R.id.ll_address -> startActivity(Intent(activity!!, SelectAddressActivity::class.java).putExtra("notBack", true))
            R.id.ll_integral -> startActivity(Intent(activity!!, MyIntegralActivity::class.java))
            R.id.ll_my_order -> onFragmentListener?.startOtherFragment("rb_order")
            R.id.ll_mall_question -> startActivity(Intent(activity!!, OpinionActivity::class.java))
        }
    }

    @SuppressLint("SetTextI18n")
    override fun updateMyIntegral(count: String?) {
        tv_integral.text = "$count 金币"
    }
}
