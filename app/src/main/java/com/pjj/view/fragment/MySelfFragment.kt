package com.pjj.view.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView

import com.pjj.R
import com.pjj.module.DiyDataBean
import com.pjj.present.BasePresent
import com.pjj.view.custom.DiyTemplateView
import kotlinx.android.synthetic.main.fragment_my_self.*

/**
 * A simple [Fragment] subclass.
 *
 */
class MySelfFragment : ABFragment<BasePresent<*>>() {
    override fun getLayoutRes(): Int {
        return R.layout.fragment_my_self
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        diyTemplateView.run {
            background = activity?.let { ContextCompat.getDrawable(it, R.mipmap.birthday_empty) }
            setOnViewClickListener(object : DiyTemplateView.OnViewClickListener {
                override fun onClick(dataBean: DiyDataBean.DataBean, view: View) {
                    when (view) {
                        is ImageView -> selectImage(dataBean)
                        is TextView -> selectText()
                    }
                }
            })
        }
        val diyDataBean = DiyDataBean()

        diyDataBean.data = ArrayList<DiyDataBean.DataBean>()

        diyDataBean.data.add(DiyDataBean.DataBean().apply {
            wide = "400"
            high = "200"
            x = "350"
            y = "900"
            isCircle = "0"
            templetElementId = "1"
        })
        diyDataBean.data.add(DiyDataBean.DataBean().apply {
            wide = "400"
            high = "62"
            x = "350"
            y = "1190"
            isCircle = "0"
            templetElementId = "2"
            wordColour = "#FEFEFE"
            wordSize = "10"
            wordAlign = "1"
        })
        diyDataBean.data.add(DiyDataBean.DataBean().apply {
            wide = "200"
            high = "200"
            x = "450"
            y = "1450"
            isCircle = "1"
            templetElementId = "1"
        })
        diyTemplateView.addViews(diyDataBean)
    }

    /**
     * 选择文本
     */
    private fun selectText() {
        diyTemplateView.updateClickView("啦啦啦啦aaa")
    }

    /**
     * 选择图片
     */
    private fun selectImage(dataBean: DiyDataBean.DataBean) {
        Log.e("TAg", "startSelectImage")
        //BitmapUtils.PATH?.let { diyTemplateView.updateClickView(it) }
    }

}
