package com.pjj.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.pjj.R
import com.pjj.module.xspad.XspManage
import com.pjj.present.BasePresent
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_select_bg_color.*

class SelectBgColorActivity : BaseActivity<BasePresent<*>>() {
    companion object {
        val BG_COLOR_TAG = "bg_color_tag"
        fun instanceForResult(activity: Activity, requestCode: Int) {
            activity.startActivityForResult(Intent(activity, SelectBgColorActivity::class.java), requestCode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_select_bg_color)
        setTitle("选择背景颜色")
        gridView.adapter = MyAdapter()
        gridView.setOnItemClickListener { parent, view, position, id ->
            setFinishResult(position)
        }
    }

    private fun setFinishResult(position: Int) {
        setResult(Activity.RESULT_OK, Intent().putExtra(BG_COLOR_TAG, position + 1))
        finish()
    }

    private inner class MyAdapter : BaseAdapter() {
        private val dp58 = ViewUtils.getDp(R.dimen.dp_58)
        private val sp13 = ViewUtils.getFDp(R.dimen.sp_13)
        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            var convertView = convertView
            var holder: Myholder
            if (null == convertView) {
                holder = Myholder()
                convertView = TextView(parent.context).apply {
                    layoutParams = ViewGroup.LayoutParams(dp58, dp58)
                    text = "屏加加"
                    setTextColor(Color.WHITE)
                    gravity = Gravity.CENTER
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, sp13)
                    holder.tv = this
                }
                convertView.tag = holder
            } else {
                holder = convertView.tag as Myholder
            }
            holder.tv!!.background = ColorDrawable(XspManage.getBgColor(position + 1))
            return convertView
        }

        override fun getItem(position: Int): Any {
            return position
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return 12
        }

        private inner class Myholder {
            var tv: TextView? = null
        }


    }


}
