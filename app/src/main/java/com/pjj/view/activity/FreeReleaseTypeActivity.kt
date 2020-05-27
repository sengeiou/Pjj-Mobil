package com.pjj.view.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.pjj.R
import com.pjj.present.BasePresent
import com.pjj.utils.SpaceItemDecoration
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_free_release_type.*

class FreeReleaseTypeActivity : BaseActivity<BasePresent<*>>() {
    private var type: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_free_release_type)
        setTitle("选择操作类型")
        type = savedInstanceState?.getInt("type") ?: intent.getIntExtra("type", type)
        rv_type.layoutManager = LinearLayoutManager(this)
        rv_type.adapter = FreeAdapter()
        rv_type.addItemDecoration(SpaceItemDecoration(this, LinearLayoutManager.VERTICAL, ViewUtils.getDp(R.dimen.dp_24), ViewUtils.getColor(R.color.color_f4f4f4)))
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.let {
            it.putInt("type", type)
        }
        super.onSaveInstanceState(outState)
    }

    private inner class FreeAdapter : RecyclerView.Adapter<FreeHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FreeHolder {
            return FreeHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_free_release_type_item, parent, false)).apply {
                itemView.setOnClickListener {
                    val position = it.getTag(R.id.position) as Int
                    startActivity(Intent(this@FreeReleaseTypeActivity, FreeReleaseActivity::class.java)
                            .putExtra("free_type", position)
                            .putExtra("type", type))
                }
            }
        }

        override fun getItemCount(): Int {
            return 3
        }

        override fun onBindViewHolder(holder: FreeHolder, position: Int) {
            holder.itemView.setTag(R.id.position, position)
            when (position) {
                0 -> {
                    holder.iv_type.setImageResource(R.mipmap.free_image)
                    holder.tv_title.text = "发布图片 / 视频"
                    holder.tv_content.text = "可发布自定义图片或视频"
                }
                1 -> {
                    holder.iv_type.setImageResource(R.mipmap.free_text)
                    holder.tv_title.text = "发布文字信息"
                    holder.tv_content.text = "通知标题不可超过23个字"
                }
                else -> {
                    holder.iv_type.setImageResource(R.mipmap.free_video)
                    holder.tv_title.text = "发布视频链接"
                    holder.tv_content.text = "仅可发布外部视频链接"
                }
            }
        }

    }

    private class FreeHolder(view: View) : RecyclerView.ViewHolder(view) {
        var iv_type = itemView.findViewById<ImageView>(R.id.iv_type)
        var tv_title = itemView.findViewById<TextView>(R.id.tv_title)
        var tv_content = itemView.findViewById<TextView>(R.id.tv_content)
    }
}
