package com.pjj.view.fragment

import android.content.Context
import com.pjj.present.BasePresent

/**
 * Create by xinheng on 2018/10/18。
 * describe：Fragment 父类
 */
abstract class ABFragment<P : BasePresent<*>> : ABLoginCheckFragment<P>() {
    protected open var onFragmentInteractionListener: OnFragmentInteractionListener? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            onFragmentInteractionListener = context
        }
    }

    interface OnFragmentInteractionListener {
        //fun onStartActivity(intent: Intent, cls: Class<*>)
        fun showADDialog(type: Int)

        fun showOtherFragment(tag: String)
        /**
         * 展示
         */
        fun showGuidStub(index: Int)

        fun getParentHeight(): Int
    }
}
