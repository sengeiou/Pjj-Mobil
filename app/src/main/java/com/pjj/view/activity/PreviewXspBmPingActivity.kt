package com.pjj.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView

import com.pjj.R
import com.pjj.module.xspad.XspManage
import com.pjj.present.BasePresent
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_preview_xsp_bm_ping.*
import java.nio.charset.Charset

class PreviewXspBmPingActivity : BaseActivity<BasePresent<*>>() {
    companion object {
        val XSP_PHONE = "xsp_phone"
        val XSP_NAME = "xsp_name"
        val XSP_TYPE = "xsp_type"
        val XSP_CONTENT = "xsp_content"
        val XSP_COLOR = "xsp_color"
        fun startActivity(context: Activity, content: String, type: Int, typeName: String, name: String? = null, phone: String? = null) {
            context.startActivity(Intent(context, PreviewXspBmPingActivity::class.java)
                    .putExtra(XSP_PHONE, phone)
                    .putExtra(XSP_TYPE, typeName)
                    .putExtra(XSP_NAME, name)
                    .putExtra(XSP_COLOR, type)
                    .putExtra(XSP_CONTENT, content)
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_xsp_bm_ping)
        setTitle("预览")
        var content = intent.getStringExtra(XSP_CONTENT)
        var phone = intent.getStringExtra(XSP_PHONE)
        var name = intent.getStringExtra(XSP_NAME)
        var titleType = intent.getStringExtra(XSP_TYPE)
        var tagInt = intent.getIntExtra(XSP_COLOR, 11)
        var color = XspManage.getBgColor(tagInt)
        var view = LayoutInflater.from(this).inflate(R.layout.layout_xsp_bm_item, fl_content, false)
/*        var buf = StringBuffer()
        (1..60).forEach {
            buf.append("爱")
        }
        content = buf.toString()*/
        var tvContent = view.findViewById<TextView>(R.id.tv_content)
        content?.let {
            if (it.toByteArray(Charset.forName("GBK")).size <= 37) {
                tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_15))
            }
        }
        tvContent.text = content
        view.findViewById<TextView>(R.id.tv_title_type).text = titleType
        view.findViewById<View>(R.id.view_space).background = ColorDrawable(color)

        view.findViewById<ImageView>(R.id.iv_left).setImageResource(XspManage.getLeftResource(tagInt.toString()))
        view.findViewById<ImageView>(R.id.iv_right).setImageResource(XspManage.getRightResource(tagInt.toString()))
        var showBottom = false

        phone?.let {
            var tv_phone_name = view.findViewById<TextView>(R.id.tv_phone_name)
        tv_phone_name.outlineProvider
            tv_phone_name.background = ViewUtils.getBgDrawable(color, 1, 0)
            var tv_phone = view.findViewById<TextView>(R.id.tv_phone)
            tv_phone.text = it
            tv_phone.setTextColor(color)
            tv_phone_name.visibility = View.VISIBLE
            tv_phone.visibility = View.VISIBLE
            showBottom = true
        }
        name?.let {
            var tv_name_name = view.findViewById<TextView>(R.id.tv_name_name)
            tv_name_name.background = ViewUtils.getBgDrawable(color, 1, 0)
            var tv_name = view.findViewById<TextView>(R.id.tv_name)
            tv_name.text = it
            tv_name.setTextColor(color)
            tv_name_name.visibility = View.VISIBLE
            tv_name.visibility = View.VISIBLE
            showBottom = true
        }
        if (!showBottom) {
            var add = ViewUtils.getFDp(R.dimen.dp_22)
            val top = ((add - tvContent.textSize) * 0.5f).toInt()
            tvContent.setLineSpacing(add, 0f)
            tvContent.setPadding(tvContent.paddingLeft, top, tvContent.paddingRight, -top)
        }
        fl_content.addView(view)
        var rate = ViewUtils.getFDp(R.dimen.dp_280) / ViewUtils.getFDp(R.dimen.dp_360)
        view.pivotX = 0f
        view.pivotY = 0f
        view.scaleX = rate
        view.scaleY = rate
        view_space.post {
            var dp11 = ViewUtils.getDp(R.dimen.dp_11)
            //var dp42 = ViewUtils.getDp(R.dimen.dp_42)
            var parentHeight = (scroll_view.parent as ViewGroup).measuredHeight
            //Log.e("TAG", ": " + (parentHeight - dp11 - dp42 - scroll_view.measuredHeight))
            var bottom = parentHeight - scroll_view.bottom
            if (bottom > dp11) {
                var half = (bottom + dp11) / 2
                var layoutParams = view_space.layoutParams
                layoutParams.height = half
                view_space.layoutParams = layoutParams
            }
        }
    }
}
