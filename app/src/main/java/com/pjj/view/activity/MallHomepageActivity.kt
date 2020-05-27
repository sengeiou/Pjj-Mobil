package com.pjj.view.activity

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.widget.TextView
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.present.BasePresent
import com.pjj.utils.StatusBarUtil.setTranslucentStatus
import com.pjj.utils.ViewUtils
import com.pjj.view.fragment.*
import com.pjj.view.viewinterface.OnFragmentListener
import kotlinx.android.synthetic.main.activity_mall_homepage.*

class MallHomepageActivity : FragmentsActivity<BaseFragment<*>, BasePresent<*>>(), OnFragmentListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mall_homepage)
        setStatueWhite()
        iniTopView()
        //fl_fragment.setPadding(0, statue, 0, 0)
/*        val params = fl_fragment.layoutParams as ConstraintLayout.LayoutParams
        params.topMargin = PjjApplication.application.statueHeight
        fl_fragment.layoutParams = params*/
        rb_home.setOnClickListener(onClick)
        rb_shop_car.setOnClickListener(onClick)
        rb_order.setOnClickListener(onClick)
        rb_mine.setOnClickListener(onClick)
//        onClick(R.id.rb_home)
        reShowFragment(intent)
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        reShowFragment(intent)
    }

    private fun reShowFragment(intent: Intent?) {
        when (intent?.getIntExtra("index", 0) ?: 0) {
            0 -> rb_home.performClick()
            1 -> rb_shop_car.performClick()
            3 -> rb_mine.performClick()
            2 -> rb_order.performClick()
        }
    }

    override fun getFragmentContainerViewId(): Int {
        return R.id.fl_fragment
    }

    override fun onBackPressed() {
        if (nowFragment is MallHomepageFragment) {
            val fragment = nowFragment as MallHomepageFragment
            if (!fragment.scrollTag) {
                fragment.backFinish()
                return
            }
        }
        super.onBackPressed()
    }

    override fun getFragment(tag: String): BaseFragment<*>? {
        return when (tag) {
            "rb_home" -> MallHomepageFragment()
            "rb_shop_car" -> MallShoppingCartFragment()
            "rb_mine" -> MallMySelfFragment()
            else -> MallOrderListFragment()
        }
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.rb_home -> showFragment("rb_home")
            R.id.rb_shop_car -> showFragment("rb_shop_car")
            R.id.rb_order -> showFragment("rb_order")
            R.id.rb_mine -> showFragment("rb_mine")
        }
    }

    override fun startOtherFragment(tag: String) {
        when (tag) {
            "rb_home" -> rb_home.performClick()
            "rb_shop_car" -> rb_shop_car.performClick()
            "rb_mine" -> rb_mine.performClick()
            else -> rb_order.performClick()
        }
    }

    private fun iniTopView() {
        rb_home.setCompoundDrawables(null, getSelectDrawable(R.mipmap.mall_home, R.mipmap.mall_un_home).apply {
            setBounds(0, 0, getDp(R.dimen.dp_24), getDp(R.dimen.dp_20))
        }, null, null)
        rb_shop_car.setCompoundDrawables(null, getSelectDrawable(R.mipmap.mall_shop_car, R.mipmap.mall_un_shop_car).apply {
            setBounds(0, 0, getDp(R.dimen.dp_22), getDp(R.dimen.dp_19))
        }, null, null)
        rb_order.setCompoundDrawables(null, getSelectDrawable(R.mipmap.mall_order, R.mipmap.mall_un_order).apply {
            setBounds(0, 0, getDp(R.dimen.dp_16), getDp(R.dimen.dp_18))
        }, null, null)
        rb_mine.setCompoundDrawables(null, getSelectDrawable(R.mipmap.mall_mine, R.mipmap.mall_un_mine).apply {
            setBounds(0, 0, getDp(R.dimen.dp_17), getDp(R.dimen.dp_20))
        }, null, null)
    }

    private fun getDp(resource: Int): Int {
        return ViewUtils.getDp(resource)
    }

    private fun getSelectDrawable(resourceId: Int, normalId: Int): Drawable {
        return ViewUtils.createSelectDrawable(resourceId, normalId, android.R.attr.state_checked)
    }

    override fun getStatusBarColor(): Int {
        return Color.WHITE
    }
}
