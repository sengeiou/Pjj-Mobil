package com.pjj.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
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
import com.pjj.contract.PersonMediaContract
import com.pjj.module.ModuleMoreBean
import com.pjj.module.parameters.Template
import com.pjj.present.PersonMediaPresent
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_person_media_tem.*

class PersonMediaTemActivity : BaseActivity<PersonMediaPresent>(), PersonMediaContract.View {
    private var template: Template? = null
    private var useSelfTag: Boolean = false
    private var height = 1886
    private lateinit var personAdapter: PersonMediaAdapter
    private var resArray = IntArray(2)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_media_tem)
        //var title="传媒"
        val title = intent.getStringExtra("title")?.let {
            it + "模板"
        } ?: "模板"
        setTitle(title)
        template = intent.getParcelableExtra("template")
        mPresent = PersonMediaPresent(this)
        template?.let {
            val purposeType = it.purposeType
            useSelfTag = "1" == it.selfUseTag
            if (useSelfTag) {
                mPresent?.loadSelfUseTask()
            } else {
                mPresent?.loadModuleTask(purposeType)
            }
            height = when (purposeType) {
                /*"9", "1" -> {
                    resArray[0] = R.mipmap.photo_video_216
                    resArray[1] = R.mipmap.video_image_tow_216
                    1800
                }*/
                else -> {
                    resArray[0] = R.mipmap.photo_video
                    resArray[1] = R.mipmap.video_image_tow
                    1800
                }
            }
            personAdapter = PersonMediaAdapter()
            rv_person.adapter = personAdapter
        }
        rv_person.layoutManager = GridLayoutManager(this, 2)
        rv_person.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View?, parent: RecyclerView, state: RecyclerView.State?) {
                if (parent.getChildLayoutPosition(view) % 2 == 0) {
                    outRect.right = ViewUtils.getDp(R.dimen.dp_38)
                }
                //super.getItemOffsets(outRect, view, parent, state)
            }
        })

    }

    private var text = ""
    override fun onClick(view: View) {
        super.onClick(view)
        val viewId = view.id
        when (viewId) {
            R.id.iv -> {
                when (val position = view.getTag(R.id.position) as Int) {
                    0 -> {
                        text = "您可上传图片或视频，建议尺寸：1080*${height}px\n视频大小不能超过20M"
                        startActivity(Intent(this, CreateDIYActivity::class.java).putExtra("template", template)
                                .putExtra("image_text", "图片 / 视频")
                                .putExtra("explain_text", text))
                    }
                    1 -> {
                        if ("1" == template?.selfUseTag) {
                            startBirthdayActivity(1)
                            return
                        }
                        text = "您可上传图片或视频，建议尺寸：1080*${height / 2}px\n视频大小不能超过20M"
                        startActivity(Intent(this, CreateMoreMediaActivity::class.java).putExtra("template", template)
                                .putExtra("explain_text", text))
                    }
                    else -> {
                        startBirthdayActivity(position)
                    }
                }
            }
        }
    }

    private fun startBirthdayActivity(position: Int) {
        val bean = personAdapter.list[position]
        startActivity(Intent(this, BirthdayWishesActivity::class.java)
                .putExtra("template", template)
                .putExtra("width_template", bean.wide)
                .putExtra("height_template", bean.high)
                .putExtra("adTempletId", bean.adTempletId)
                .putExtra("bg_path", bean.bgPic)
                .putExtra("theme", bean.theme)
        )
    }

    override fun update(list: MutableList<ModuleMoreBean.DataBean>) {
        personAdapter.list = list
    }

    private inner class PersonMediaAdapter : RecyclerView.Adapter<PersonMediaHolder>() {
        var list: MutableList<ModuleMoreBean.DataBean> = ArrayList<ModuleMoreBean.DataBean>().apply {
            add(ModuleMoreBean.DataBean().apply {
                res = resArray[0]
            })
            if (!useSelfTag) {
                add(ModuleMoreBean.DataBean().apply {
                    res = resArray[1]
                })
            }
        }
            set(value) {
                field.addAll(value)
                notifyDataSetChanged()
            }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonMediaHolder {
            return PersonMediaHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_person_media_item, parent, false)).apply {
                iv.setOnClickListener(onClick)
                val lp = iv.layoutParams
                lp.height = lp.width * height / 1080
                iv.layoutParams = lp
            }
        }

        override fun getItemCount(): Int {
            return list.size
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: PersonMediaHolder, position: Int) {
            holder.iv.setTag(R.id.position, position)
            val bean = list[position]
            when (position) {
                0 -> {
                    Glide.with(this@PersonMediaTemActivity).load(bean.res).into(holder.iv)
                    holder.tv.text = "模板${position + 1}"
                }
                1 -> {
                    if (useSelfTag) {
                        Glide.with(this@PersonMediaTemActivity).load(PjjApplication.filePath + bean.examplePic).into(holder.iv)
                        holder.tv.text = bean.theme
                    } else {
                        Glide.with(this@PersonMediaTemActivity).load(bean.res).into(holder.iv)
                        holder.tv.text = "模板${position + 1}"
                    }
                }
                else -> {
                    Glide.with(this@PersonMediaTemActivity).load(PjjApplication.filePath + bean.examplePic).into(holder.iv)
                    holder.tv.text = bean.theme
                }
            }
        }

    }

    private class PersonMediaHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv = view.findViewById<TextView>(R.id.tv)
        var iv = view.findViewById<ImageView>(R.id.iv)
    }
}
