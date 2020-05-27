package com.pjj.view.fragment

import android.content.Context
import com.pjj.present.BasePresent

/**
 * Created by XinHeng on 2019/04/13.
 * describe：
 */
abstract class ABExchangeRecordFragment<P : BasePresent<*>> : BaseFragment<P>() {
    protected var onABExchangeRecordFragmentListener: OnABExchangeRecordFragmentListener? = null
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnABExchangeRecordFragmentListener) {
            onABExchangeRecordFragmentListener = context
        }
    }

    interface OnABExchangeRecordFragmentListener {
        fun update(index: Int)
    }
}