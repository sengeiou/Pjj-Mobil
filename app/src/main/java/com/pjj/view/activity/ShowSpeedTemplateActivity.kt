package com.pjj.view.activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.AbstractThreadedSyncAdapter
import android.content.Intent
import android.os.Bundle
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
import com.pjj.contract.ShowSpeedTemplateContract
import com.pjj.module.SpeedDataBean
import com.pjj.module.xspad.XspManage
import com.pjj.present.ShowSpeedTemplatePresent
import com.pjj.utils.Log
import com.pjj.utils.ViewUtils
import com.pjj.view.dialog.AddTemplateDialog
import kotlinx.android.synthetic.main.activity_showspeedtemplate.*

/**
 * Create by xinheng on 2019/03/20 11:47。
 * describe：展示拼屏模板列表
 */
class ShowSpeedTemplateActivity : BaseActivity<ShowSpeedTemplatePresent>(), ShowSpeedTemplateContract.View {
    private var dataList: MutableList<SpeedDataBean>? = null
    private var showCreate = false
    private var deleteId: String? = null
    private var deletePosition: Int = -1

    private lateinit var showSpeedAdapter: ShowSpeedAdapter

    companion object {
        @JvmStatic
        fun newInstance(activity: Activity, showCreate: Boolean = false) {
            activity.startActivity(Intent(activity, ShowSpeedTemplateActivity::class.java).putExtra("showCreate", showCreate))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_showspeedtemplate)
        setTitle("拼屏信息模板")
        mPresent = ShowSpeedTemplatePresent(this)
        refreshData()
        showCreate = intent.getBooleanExtra("showCreate", showCreate)
        rv_speed.layoutManager = GridLayoutManager(this, 2)
        showSpeedAdapter = ShowSpeedAdapter()
        rv_speed.adapter = showSpeedAdapter
    }

    override fun updateData(list: MutableList<SpeedDataBean>) {
        this.dataList = list
        /*if (list.isEmpty()) {
            ll_no_data.visibility = View.VISIBLE
        } else {
            ll_no_data.visibility = View.GONE
        }*/
        showSpeedAdapter.notifyDataSetChanged()
    }

    private var onClick1 = View.OnClickListener {
        when (it.id) {
            R.id.iv -> {
                val position = it.getTag(R.id.position) as Int
                if (showCreate) {
                    XspManage.getInstance().speedScreenData.clone = dataList!![position]
                    startActivity(Intent(this@ShowSpeedTemplateActivity, PreviewXspSpeedActivity::class.java))
                } else {
                    XspManage.getInstance().speedScreenData.templateData = dataList!![position]
                    UploadSpeedActivity.newInstance(this@ShowSpeedTemplateActivity, true)
                }
            }
            R.id.iv_delete -> {
                val position = it.getTag(R.id.position) as Int
                deletePosition = position
                dataList?.let { item ->
                    deleteId = item[position].templetId
                    deleteDialog.show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when {
            requestCode == 303 && resultCode == RESULT_OK -> {
                refreshData()
            }
        }
    }

    private fun refreshData() {
        mPresent?.loadSpeedSimpleTask(XspManage.getInstance().speedScreenData.originalTemplateData!!.IdentificationId!!)
    }

    override fun deleteSuccess() {
        showSpeedAdapter.delete()
    }

    private val deleteDialog: AddTemplateDialog by lazy {
        AddTemplateDialog(this).apply {
            notice = "是否删除模板"
            leftText = "删除"
            var dp48 = ViewUtils.getDp(R.dimen.dp_48)
            setImageResource(R.mipmap.delete_, false, dp48, dp48)
            onItemClickListener = object : AddTemplateDialog.OnItemClickListener {
                override fun leftClick() {
                    deleteId?.let {
                        mPresent?.delete(it, false)
                    }
                }
            }
        }
    }

    inner class ShowSpeedAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                0 -> CreateHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_template_list_item, parent, false))
                else -> ShowSpeedHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_template_list_item, parent, false)).apply {
                    iv.setOnClickListener(onClick1)
                    iv_delete.setOnClickListener(onClick1)
                }
            }
        }

        fun delete() {
            dataList?.let {
                if (it.size > deletePosition) {
                    it.removeAt(deletePosition)
                    notifyDataSetChanged()
                }
            }
        }

        override fun getItemCount(): Int {
            var head = 1
            var i = (dataList?.size ?: 0) + head
            return i
        }

        override fun getItemViewType(position: Int): Int {
            return if (position == 0) 0 else 1
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val tag = -1
            when (holder) {
                is ShowSpeedHolder -> {
                    Glide.with(this@ShowSpeedTemplateActivity).load(PjjApplication.filePath + dataList!![position + tag].thumbnail).into(holder.iv)
                    holder.tv.text = "模板${position + tag + 1}"
                    holder.iv.setTag(R.id.position, position + tag)
                    holder.iv_delete.setTag(R.id.position, position + tag)
                }
                is CreateHolder -> {
                    holder.iv.setOnClickListener {
                        UploadSpeedActivity.newInstanceForResult(this@ShowSpeedTemplateActivity, 303)
                    }
                }
            }
        }

        inner class CreateHolder(view: View) : RecyclerView.ViewHolder(view) {
            var iv = view.findViewById<ImageView>(R.id.iv)!!
            var tv = view.findViewById<TextView>(R.id.tv)!!

            init {
                iv.setImageResource(R.mipmap.create_tem111)
                tv.visibility = View.INVISIBLE
                view.findViewById<ImageView>(R.id.iv_video).visibility = View.GONE
                view.findViewById<ImageView>(R.id.iv_delete)!!.visibility = View.INVISIBLE
            }
        }

        inner class ShowSpeedHolder(view: View) : RecyclerView.ViewHolder(view) {
            var iv = view.findViewById<ImageView>(R.id.iv)!!
            var tv = view.findViewById<TextView>(R.id.tv)!!
            var iv_delete = view.findViewById<ImageView>(R.id.iv_delete)!!

            init {
                view.findViewById<ImageView>(R.id.iv_video).visibility = View.GONE
                if (!showCreate) {
                    iv_delete.visibility = View.INVISIBLE
                }
            }
        }
    }
}
