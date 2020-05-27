package com.pjj.view.fragment

import android.content.Intent
import com.pjj.present.BasePresent
import com.pjj.utils.SharedUtils
import com.pjj.view.activity.LoginActivity

/**
 * Created by XinHeng on 2019/02/12.
 * describe：
 */
abstract class ABLoginCheckFragment<P : BasePresent<*>> : BaseFragment<P>() {
    protected open fun checkLogin(isCheck: Boolean = true): Boolean {
        return if (isCheck) {
            var checkLogin = SharedUtils.checkLogin()
            if (checkLogin) {
                true
            } else {
                jumpLoginActivity()
                false
            }
        } else {
            true
        }
    }

    private fun jumpLoginActivity() {
        startActivity(Intent(activity!!, LoginActivity::class.java))
    }
}