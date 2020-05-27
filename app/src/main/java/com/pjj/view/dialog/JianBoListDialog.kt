package com.pjj.view.dialog

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.pjj.R
import com.pjj.module.OrderResultBean
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.custom.MaxHeightRecyclerView
import kotlinx.android.synthetic.main.layout_jian_bo_dialog.*

/**
 * Created by XinHeng on 2018/12/18.
 * describe：
 */
class JianBoListDialog(context: Context, themeResId: Int = 0) : FullWithNoTitleDialog(context, themeResId) {
    private var adapterJianBo: JianBoAdapter
    override fun getDialogGravity(): Int {
        return Gravity.CENTER
    }

    private var viewJianboHidden = false
    override fun isFullScreen(): Boolean {
        return false
    }

    init {
        setContentView(R.layout.layout_jian_bo_dialog)
        var rvXsp = findViewById<MaxHeightRecyclerView>(R.id.rv_xsp)
        rvXsp.layoutManager = LinearLayoutManager(context)
        //rvXsp.mMaxHeight = ViewUtils.getDp(R.dimen.dp_260)
        adapterJianBo = JianBoAdapter()
        rvXsp.adapter = adapterJianBo
        iv_close.setOnClickListener { dismiss() }
    }

    var onItemClickListener: OnItemClickListener? = null
    fun setDataList(list: MutableList<OrderResultBean.DataBean.CanPlayElevatorBean>?, jianbo: Boolean) {
        viewJianboHidden = jianbo
        adapterJianBo.list = list
        show()
    }

    inner class JianBoAdapter : RecyclerView.Adapter<JianBoAdapter.JianBoHolder>() {
        var list: MutableList<OrderResultBean.DataBean.CanPlayElevatorBean>? = null
            set(value) {
                field = value
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JianBoHolder {
            return JianBoHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_jiao_bo_item, parent, false)).apply {
                tvCamera.setOnClickListener(onClickListener)
                tvScreenshots.setOnClickListener(onClickListener)
            }
        }

        override fun getItemCount(): Int {
            return list?.size ?: 0
        }

        override fun onBindViewHolder(holder: JianBoHolder, position: Int) {
            val bean = list!![position]
            holder.tv.text = list!![position].run {
                eleName
            } ?: "无效数据"
            holder.tvCamera.tag = position
            holder.tvScreenshots.tag = position
            if (TextUtils.isEmpty(bean.zhiboUrl)) {
                holder.tvCamera.visibility = View.VISIBLE
            } else {
                holder.tvCamera.visibility = View.GONE
            }
        }

        inner class JianBoHolder(view: View) : RecyclerView.ViewHolder(view) {
            var tv = view.findViewById<TextView>(R.id.tv_screen_name)
            var tvCamera = view.findViewById<TextView>(R.id.tv_camera)
            var tvScreenshots = view.findViewById<TextView>(R.id.tv_screenshots)
        }

        private var onClickListener = View.OnClickListener {
            when (it.id) {
                R.id.tv_camera -> {
                    var position = it.tag as Int
                    onItemClickListener?.itemClick(list!![position].register, list!![position].zhiboUrl)
                }
                R.id.tv_screenshots -> {
                    var position = it.tag as Int
                    list!![position]?.let {
                        onItemClickListener?.screenshots(it.screenId)
                    }
                }
            }
            dismiss()
        }
    }


    interface OnItemClickListener {
        /**
         * 监播
         * 摄像头
         */
        fun itemClick(register: String, zhiBoUrl: String?)

        /**
         * 截屏
         */
        fun screenshots(screenId: String)
    }
}