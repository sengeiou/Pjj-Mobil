package com.pjj.view.fragment


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pjj.PjjApplication

import com.pjj.R
import com.pjj.module.SpeedDataBean
import com.pjj.module.xspad.XspManage
import com.pjj.utils.JsonUtils
import com.pjj.utils.Log
import com.pjj.view.activity.PreviewXspSpeedActivity
import com.pjj.view.activity.UploadSpeedActivity
import kotlinx.android.synthetic.main.fragment_show_speed_template.*

/**
 * A simple [Fragment] subclass.
 *
 */
class ShowSpeedTemplateFragment : Fragment() {
    var dataList: List<SpeedDataBean>? = null
    var hiddenDelete = false
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_show_speed_template, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //rv_speed.layoutManager = GridLayoutManager(context, 2)

    }

    private var onClick = View.OnClickListener {

    }


}
