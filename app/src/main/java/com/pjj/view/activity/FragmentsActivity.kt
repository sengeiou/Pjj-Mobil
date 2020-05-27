package com.pjj.view.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import com.pjj.present.BasePresent
import com.pjj.utils.Log
import com.pjj.utils.TextUtils
import com.pjj.view.fragment.BaseFragment

abstract class FragmentsActivity<F : BaseFragment<*>, P : BasePresent<*>> : BaseActivity<P>() {
    private val listTag = ArrayList<String>()
    protected open var nowFragment: F? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (null != savedInstanceState) {
            //val manager = supportFragmentManager
            //manager.popBackStackImmediate(null, 1)
            val list = savedInstanceState.getStringArrayList("fragment_tags")
            if (TextUtils.isNotEmptyList(list)) {
                listTag.clear()
                listTag.addAll(list!!)
                val fragmentTransaction = supportFragmentManager.beginTransaction()
                list.forEach {
                    val fragmentByTag = supportFragmentManager.findFragmentByTag(it)
                    if (null != fragmentByTag) {
                        fragmentTransaction.hide(fragmentByTag)
                    }
                }
                fragmentTransaction.commit()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.let {
            it.putStringArrayList("fragment_tags", listTag)
        }
        super.onSaveInstanceState(outState)
    }

    protected open fun showFragment(tag: String) {
        if (!listTag.contains(tag)) {
            listTag.add(tag)
        }
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        var fragmentByTag: Fragment? = supportFragmentManager.findFragmentByTag(tag)
        if (null == fragmentByTag) {
            Log.e("FragmentsActivity_TAG", "fragmentByTag==null")
            fragmentByTag = getFragment(tag)
            if (null == fragmentByTag) {
                return
            }
            fragmentTransaction.add(getFragmentContainerViewId(), fragmentByTag, tag)
        } else {
            if (fragmentByTag === nowFragment) {
                //更新即可
                return
            }
            fragmentTransaction.show(fragmentByTag)
        }
        if (null != nowFragment) {
            fragmentTransaction.hide(nowFragment!!)
        }
        nowFragment = fragmentByTag as F
        fragmentTransaction.commit()
    }

    //@IdRes int containerViewId
    abstract fun getFragmentContainerViewId(): Int

    abstract fun getFragment(tag: String): F?
}