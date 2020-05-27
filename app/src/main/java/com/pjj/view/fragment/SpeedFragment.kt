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
import android.widget.TextView

import com.pjj.R
import com.pjj.module.SpeedDataBean
import com.pjj.module.xspad.XspManage
import com.pjj.present.BasePresent
import com.pjj.view.activity.SelectRandomReleaseAreaActivity
import com.pjj.view.activity.ShowSpeedTemplateActivity
import com.pjj.view.activity.TemplateListActivity
import com.pjj.view.custom.SpeedViewGroup
import kotlinx.android.synthetic.main.activity_my_template_next.*
import kotlinx.android.synthetic.main.fragment_speed.*

/**
 * A simple [Fragment] subclass.
 *
 */
class SpeedFragment : BaseFragment<BasePresent<*>>() {
    private var index = 0
    var dataList: List<SpeedDataBean>? = null
    var showCreate = true

    companion object {
        val SPEED_INDEX = "speed_index"
        @JvmStatic
        fun newInstance(index: Int): SpeedFragment = SpeedFragment().apply {
            arguments = Bundle().apply {
                putInt(SPEED_INDEX, index)
            }
        }

        /*val datas2 = arrayOf(
                "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":1,\"height\":1,\"type\":2,\"randomFlag\":\"1\"},{\"x\":0,\"y\":1,\"width\":1,\"height\":1,\"randomFlag\":\"1\"}],\"proportionX\":1,\"proportionY\":2,\"size\":2}",
                "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":1,\"height\":1},{\"x\":0,\"y\":1,\"width\":1,\"height\":1,\"type\":2}],\"proportionX\":1,\"proportionY\":2,\"size\":2}",
                "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":1,\"height\":1},{\"x\":0,\"y\":1,\"width\":1,\"height\":1,\"type\":1}],\"proportionX\":1,\"proportionY\":2,\"size\":2}"
        )
        val datas3 = arrayOf(
                "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":1,\"height\":1},{\"x\":0,\"y\":1,\"width\":1,\"height\":1,\"type\":2},{\"x\":0,\"y\":2,\"width\":1,\"height\":1}],\"proportionX\":1,\"proportionY\":3,\"size\":3}",
                "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":1,\"height\":1},{\"x\":0,\"y\":1,\"width\":1,\"height\":1,\"type\":1},{\"x\":0,\"y\":2,\"width\":1,\"height\":1}],\"proportionX\":1,\"proportionY\":3,\"size\":3}",
                "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":1,\"height\":1,\"type\":2},{\"x\":0,\"y\":1,\"width\":1,\"height\":1},{\"x\":0,\"y\":2,\"width\":1,\"height\":1}],\"proportionX\":1,\"proportionY\":3,\"size\":3}",
                "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":1,\"height\":1},{\"x\":0,\"y\":1,\"width\":1,\"height\":1},{\"x\":0,\"y\":2,\"width\":1,\"height\":1,\"type\":2}],\"proportionX\":1,\"proportionY\":3,\"size\":3}"
        )
        val datas4 = arrayOf(
                "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":1,\"height\":1},{\"x\":0,\"y\":1,\"width\":1,\"height\":1},{\"x\":0,\"y\":2,\"width\":1,\"height\":1},{\"x\":0,\"y\":3,\"width\":1,\"height\":1}],\"proportionX\":1,\"proportionY\":4,\"size\":4}",
                "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":1,\"height\":1},{\"x\":0,\"y\":1,\"width\":1,\"height\":1},{\"x\":1,\"y\":0,\"width\":1,\"height\":1},{\"x\":1,\"y\":1,\"width\":1,\"height\":1}],\"proportionX\":2,\"proportionY\":2,\"size\":4}"
        )
        val datas5 = arrayOf(
                "{\"viewSizeBeanList\":[{\"type\":1,\"x\":0,\"y\":0,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":1,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":2,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":3,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":4,\"width\":1,\"height\":1}],\"proportionX\":1,\"proportionY\":5,\"size\":5}",
                "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":2,\"height\":1,\"type\":2},{\"x\":0,\"y\":1,\"width\":1,\"height\":1},{\"x\":0,\"y\":2,\"width\":1,\"height\":1},{\"x\":1,\"y\":1,\"width\":1,\"height\":1},{\"x\":1,\"y\":2,\"width\":1,\"height\":1}],\"proportionX\":2,\"proportionY\":3,\"size\":5}",
                "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":1,\"height\":1},{\"x\":0,\"y\":1,\"width\":1,\"height\":1},{\"x\":1,\"y\":0,\"width\":1,\"height\":1},{\"x\":1,\"y\":1,\"width\":1,\"height\":1},{\"x\":0,\"y\":2,\"width\":2,\"height\":1,\"type\":2}],\"proportionX\":2,\"proportionY\":3,\"size\":5}",
                "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":2,\"height\":1,\"type\":1},{\"x\":0,\"y\":1,\"width\":1,\"height\":1},{\"x\":0,\"y\":2,\"width\":1,\"height\":1},{\"x\":1,\"y\":1,\"width\":1,\"height\":1},{\"x\":1,\"y\":2,\"width\":1,\"height\":1}],\"proportionX\":2,\"proportionY\":3,\"size\":5}",
                "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":1,\"height\":1},{\"x\":0,\"y\":1,\"width\":1,\"height\":1},{\"x\":1,\"y\":0,\"width\":1,\"height\":1},{\"x\":1,\"y\":1,\"width\":1,\"height\":1},{\"x\":0,\"y\":2,\"width\":2,\"height\":1,\"type\":1}],\"proportionX\":2,\"proportionY\":3,\"size\":5}"
        )
        val datas6 = arrayOf(
                "{\"viewSizeBeanList\":[{\"type\":1,\"x\":0,\"y\":0,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":1,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":2,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":3,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":4,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":5,\"width\":1,\"height\":1}],\"proportionX\":1,\"proportionY\":6,\"size\":6}",
                "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":1,\"height\":1},{\"x\":0,\"y\":1,\"width\":1,\"height\":1},{\"x\":0,\"y\":2,\"width\":1,\"height\":1},{\"x\":1,\"y\":0,\"width\":1,\"height\":1},{\"x\":1,\"y\":1,\"width\":1,\"height\":1},{\"x\":1,\"y\":2,\"width\":1,\"height\":1}],\"proportionX\":2,\"proportionY\":3,\"size\":6}"
        )
        val datas7 = arrayOf(
                "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":2,\"height\":3,\"type\":2},{\"x\":0,\"y\":3,\"width\":1,\"height\":2},{\"x\":0,\"y\":5,\"width\":1,\"height\":2},{\"x\":0,\"y\":7,\"width\":1,\"height\":2},{\"x\":1,\"y\":3,\"width\":1,\"height\":2},{\"x\":1,\"y\":5,\"width\":1,\"height\":2},{\"x\":1,\"y\":7,\"width\":1,\"height\":2}],\"proportionX\":2,\"proportionY\":9,\"size\":7}",
                "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":2,\"height\":3,\"type\":1},{\"x\":0,\"y\":3,\"width\":1,\"height\":2},{\"x\":0,\"y\":5,\"width\":1,\"height\":2},{\"x\":0,\"y\":7,\"width\":1,\"height\":2},{\"x\":1,\"y\":3,\"width\":1,\"height\":2},{\"x\":1,\"y\":5,\"width\":1,\"height\":2},{\"x\":1,\"y\":7,\"width\":1,\"height\":2}],\"proportionX\":2,\"proportionY\":9,\"size\":7}",
                "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":1,\"height\":2},{\"x\":0,\"y\":2,\"width\":1,\"height\":2},{\"x\":0,\"y\":4,\"width\":1,\"height\":2},{\"x\":1,\"y\":0,\"width\":1,\"height\":2},{\"x\":1,\"y\":2,\"width\":1,\"height\":2},{\"x\":1,\"y\":4,\"width\":1,\"height\":2},{\"x\":0,\"y\":6,\"width\":2,\"height\":3,\"type\":2}],\"proportionX\":2,\"proportionY\":9,\"size\":7}",
                "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":1,\"height\":2},{\"x\":0,\"y\":2,\"width\":1,\"height\":2},{\"x\":0,\"y\":4,\"width\":1,\"height\":2},{\"x\":1,\"y\":0,\"width\":1,\"height\":2},{\"x\":1,\"y\":2,\"width\":1,\"height\":2},{\"x\":1,\"y\":4,\"width\":1,\"height\":2},{\"x\":0,\"y\":6,\"width\":2,\"height\":3,\"type\":1}],\"proportionX\":2,\"proportionY\":9,\"size\":7}"
        )
        val datas8 = arrayOf(
                "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":1,\"height\":1},{\"x\":0,\"y\":1,\"width\":1,\"height\":1},{\"x\":0,\"y\":2,\"width\":1,\"height\":1},{\"x\":0,\"y\":3,\"width\":1,\"height\":1},{\"x\":1,\"y\":0,\"width\":1,\"height\":1},{\"x\":1,\"y\":1,\"width\":1,\"height\":1},{\"x\":1,\"y\":2,\"width\":1,\"height\":1},{\"x\":1,\"y\":3,\"width\":1,\"height\":1}],\"proportionX\":2,\"proportionY\":4,\"size\":8}",
                "{\"viewSizeBeanList\":[{\"type\":1,\"x\":0,\"y\":0,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":1,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":2,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":3,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":4,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":5,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":6,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":7,\"width\":1,\"height\":1}],\"proportionX\":1,\"proportionY\":8,\"size\":8}\n"
        )
        val datas9 = arrayOf(
                "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":1,\"height\":1},{\"x\":0,\"y\":1,\"width\":1,\"height\":1},{\"x\":0,\"y\":2,\"width\":1,\"height\":1},{\"x\":1,\"y\":0,\"width\":1,\"height\":1},{\"x\":1,\"y\":1,\"width\":1,\"height\":1},{\"x\":1,\"y\":2,\"width\":1,\"height\":1},{\"x\":2,\"y\":0,\"width\":1,\"height\":1},{\"x\":2,\"y\":1,\"width\":1,\"height\":1},{\"x\":2,\"y\":2,\"width\":1,\"height\":1}],\"proportionX\":3,\"proportionY\":3,\"size\":9}",
                "{\"viewSizeBeanList\":[{\"type\":1,\"x\":0,\"y\":0,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":1,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":2,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":3,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":4,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":5,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":6,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":7,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":8,\"width\":1,\"height\":1}],\"proportionX\":1,\"proportionY\":9,\"size\":9}\n"
        )
        val datas10 = arrayOf(
                "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":3,\"height\":3,\"type\":1},{\"type\":1,\"x\":0,\"y\":3,\"width\":1,\"height\":2},{\"type\":1,\"x\":0,\"y\":5,\"width\":1,\"height\":2},{\"type\":1,\"x\":0,\"y\":7,\"width\":1,\"height\":2},{\"type\":1,\"x\":1,\"y\":3,\"width\":1,\"height\":2},{\"type\":1,\"x\":1,\"y\":5,\"width\":1,\"height\":2},{\"type\":1,\"x\":1,\"y\":7,\"width\":1,\"height\":2},{\"type\":1,\"x\":2,\"y\":3,\"width\":1,\"height\":2},{\"type\":1,\"x\":2,\"y\":5,\"width\":1,\"height\":2},{\"type\":1,\"x\":2,\"y\":7,\"width\":1,\"height\":2}],\"proportionX\":3,\"proportionY\":9,\"size\":9}",
                "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":3,\"height\":3,\"type\":2},{\"type\":1,\"x\":0,\"y\":3,\"width\":1,\"height\":2},{\"type\":1,\"x\":0,\"y\":5,\"width\":1,\"height\":2},{\"type\":1,\"x\":0,\"y\":7,\"width\":1,\"height\":2},{\"type\":1,\"x\":1,\"y\":3,\"width\":1,\"height\":2},{\"type\":1,\"x\":1,\"y\":5,\"width\":1,\"height\":2},{\"type\":1,\"x\":1,\"y\":7,\"width\":1,\"height\":2},{\"type\":1,\"x\":2,\"y\":3,\"width\":1,\"height\":2},{\"type\":1,\"x\":2,\"y\":5,\"width\":1,\"height\":2},{\"type\":1,\"x\":2,\"y\":7,\"width\":1,\"height\":2}],\"proportionX\":3,\"proportionY\":9,\"size\":9}",
                "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":1,\"height\":2},{\"x\":0,\"y\":2,\"width\":1,\"height\":2},{\"x\":0,\"y\":4,\"width\":1,\"height\":2},{\"x\":1,\"y\":0,\"width\":1,\"height\":2},{\"x\":1,\"y\":2,\"width\":1,\"height\":2},{\"x\":1,\"y\":4,\"width\":1,\"height\":2},{\"x\":2,\"y\":0,\"width\":1,\"height\":2},{\"x\":2,\"y\":2,\"width\":1,\"height\":2},{\"x\":2,\"y\":4,\"width\":1,\"height\":2},{\"x\":0,\"y\":6,\"width\":3,\"height\":3,\"type\":1}],\"proportionX\":3,\"proportionY\":9,\"size\":9}",
                "{\"viewSizeBeanList\":[{\"x\":0,\"y\":0,\"width\":1,\"height\":2},{\"x\":0,\"y\":2,\"width\":1,\"height\":2},{\"x\":0,\"y\":4,\"width\":1,\"height\":2},{\"x\":1,\"y\":0,\"width\":1,\"height\":2},{\"x\":1,\"y\":2,\"width\":1,\"height\":2},{\"x\":1,\"y\":4,\"width\":1,\"height\":2},{\"x\":2,\"y\":0,\"width\":1,\"height\":2},{\"x\":2,\"y\":2,\"width\":1,\"height\":2},{\"x\":2,\"y\":4,\"width\":1,\"height\":2},{\"x\":0,\"y\":6,\"width\":3,\"height\":3,\"type\":2}],\"proportionX\":3,\"proportionY\":9,\"size\":9}",
                "{\"viewSizeBeanList\":[{\"type\":3,\"x\":0,\"y\":0,\"width\":1,\"height\":1},{\"type\":3,\"x\":0,\"y\":1,\"width\":1,\"height\":1},{\"type\":3,\"x\":0,\"y\":2,\"width\":1,\"height\":1},{\"type\":3,\"x\":0,\"y\":3,\"width\":1,\"height\":1},{\"type\":3,\"x\":0,\"y\":4,\"width\":1,\"height\":1},{\"type\":3,\"x\":0,\"y\":5,\"width\":1,\"height\":1},{\"type\":3,\"x\":0,\"y\":6,\"width\":1,\"height\":1},{\"type\":3,\"x\":0,\"y\":7,\"width\":1,\"height\":1},{\"type\":3,\"x\":0,\"y\":8,\"width\":1,\"height\":1},{\"type\":3,\"x\":0,\"y\":9,\"width\":1,\"height\":1}],\"proportionX\":1,\"proportionY\":10,\"size\":10}"
        )
        val datas11 = arrayOf(
                "{\"viewSizeBeanList\":[{\"type\":1,\"x\":0,\"y\":0,\"width\":3,\"height\":1},{\"type\":1,\"x\":3,\"y\":0,\"width\":3,\"height\":1},{\"type\":1,\"x\":0,\"y\":1,\"width\":2,\"height\":1},{\"type\":1,\"x\":0,\"y\":2,\"width\":2,\"height\":1},{\"type\":1,\"x\":0,\"y\":3,\"width\":2,\"height\":1},{\"type\":1,\"x\":2,\"y\":1,\"width\":2,\"height\":1},{\"type\":1,\"x\":2,\"y\":2,\"width\":2,\"height\":1},{\"type\":1,\"x\":2,\"y\":3,\"width\":2,\"height\":1},{\"type\":1,\"x\":4,\"y\":1,\"width\":2,\"height\":1},{\"type\":1,\"x\":4,\"y\":2,\"width\":2,\"height\":1},{\"type\":1,\"x\":4,\"y\":3,\"width\":2,\"height\":1}],\"proportionX\":6,\"proportionY\":4,\"size\":11}",
                "{\"viewSizeBeanList\":[{\"type\":1,\"x\":0,\"y\":0,\"width\":2,\"height\":1},{\"type\":1,\"x\":0,\"y\":1,\"width\":2,\"height\":1},{\"type\":1,\"x\":0,\"y\":2,\"width\":2,\"height\":1},{\"type\":1,\"x\":2,\"y\":0,\"width\":2,\"height\":1},{\"type\":1,\"x\":2,\"y\":1,\"width\":2,\"height\":1},{\"type\":1,\"x\":2,\"y\":2,\"width\":2,\"height\":1},{\"type\":1,\"x\":4,\"y\":0,\"width\":2,\"height\":1},{\"type\":1,\"x\":4,\"y\":1,\"width\":2,\"height\":1},{\"type\":1,\"x\":4,\"y\":2,\"width\":2,\"height\":1},{\"type\":1,\"x\":0,\"y\":3,\"width\":3,\"height\":1},{\"type\":1,\"x\":3,\"y\":3,\"width\":3,\"height\":1}],\"proportionX\":6,\"proportionY\":4,\"size\":11}\n"
        )
        val datas12 = arrayOf(
                "{\"viewSizeBeanList\":[{\"type\":1,\"x\":0,\"y\":0,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":1,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":2,\"width\":1,\"height\":1},{\"type\":1,\"x\":0,\"y\":3,\"width\":1,\"height\":1},{\"type\":1,\"x\":1,\"y\":0,\"width\":1,\"height\":1},{\"type\":1,\"x\":1,\"y\":1,\"width\":1,\"height\":1},{\"type\":1,\"x\":1,\"y\":2,\"width\":1,\"height\":1},{\"type\":1,\"x\":1,\"y\":3,\"width\":1,\"height\":1},{\"type\":1,\"x\":2,\"y\":0,\"width\":1,\"height\":1},{\"type\":1,\"x\":2,\"y\":1,\"width\":1,\"height\":1},{\"type\":1,\"x\":2,\"y\":2,\"width\":1,\"height\":1},{\"type\":1,\"x\":2,\"y\":3,\"width\":1,\"height\":1}],\"proportionX\":3,\"proportionY\":4,\"size\":12}"
        )
        val datas = arrayOf(datas2, datas3, datas4, datas5, datas6, datas7, datas8, datas9, datas10, datas11, datas12)*/
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        index = arguments!!.getInt(SPEED_INDEX)
    }

    override fun getLayoutRes(): Int {
        return R.layout.fragment_speed
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        rv_speed.run {
            layoutManager = GridLayoutManager(context, 2)
            adapter = SpeedAdapter()
        }
    }


    inner class SpeedAdapter : RecyclerView.Adapter<SpeedAdapter.SpeedHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeedHolder {
            return SpeedHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_speed_item, parent, false))
        }

        override fun getItemCount(): Int {
            return dataList!!.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: SpeedHolder, position: Int) {
            holder.sgSpeed.speedData = dataList!![position]
            holder.tvSize.text = "建议尺寸：" + holder.sgSpeed.run {
                if (null == imageSizeText) {
                    if (null != videoSizeText)
                        videoSizeText!!.substring(3, videoSizeText!!.length)
                    else
                        ""
                } else {
                    if (null != videoSizeText) {
                        videoSizeText + "\n" + imageSizeText
                    } else {
                        imageSizeText!!.substring(3, imageSizeText!!.length)
                    }
                }
            }
            holder.itemView.setOnClickListener {
                if (index == 10 && "248a1400e13e4b30ac1adf3b7abababc" == dataList!![position].IdentificationId) {
                    if (showCreate) {
                        startActivity(Intent(activity!!, TemplateListActivity::class.java)
                                .putExtra("title_text", "拼屏便民模板")
                                .putExtra("identity_type", "3")
                                .putExtra("ad_type", 5))
                        return@setOnClickListener
                    } else {
                        XspManage.getInstance().bianMinPing = 1
                        startActivity(Intent(activity!!, SelectRandomReleaseAreaActivity::class.java))
                    }
                }
                //UploadSpeedActivity.newInstance(activity!!, JsonUtils.toJsonString(dataList!![position]))
                XspManage.getInstance().speedScreenData.originalTemplateData = dataList!![position]
                ShowSpeedTemplateActivity.newInstance(activity!!, showCreate)
            }
        }

        inner class SpeedHolder(view: View) : RecyclerView.ViewHolder(view) {
            var sgSpeed = view.findViewById<SpeedViewGroup>(R.id.sg_speed)!!
            var tvSize = view.findViewById<TextView>(R.id.tv_size)!!
        }
    }
}
