package com.pjj.view.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.pjj.R
import com.pjj.module.UserTempletBean
import com.pjj.module.xspad.XspManage
import com.pjj.utils.BitmapUtils
import com.pjj.utils.Log
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.custom.TowMediaView

/**
 * Created by XinHeng on 2018/12/06.
 * describe：
 * identityType 1个人 2商家
 */
class TemplateListAdapter(private var context: Context, private var releaseTag: Boolean = false, private var type: String? = null, private var identityType: String) : RecyclerView.Adapter<TemplateListAdapter.TemplateListHolder>() {
    private var selectPosition = -1
    private var selectId = ""
    private var preSelectTag = false
    fun setClick(templateId: String) {
        preSelectTag = true
        selectId = templateId
    }

    var hasAddOtherTemplate = false
    var list: MutableList<UserTempletBean.DataBean>? = null
        set(value) {
            field = value
            notifyDataSetChanged()
        }
    var addList: MutableList<UserTempletBean.DataBean>? = null
        set(value) {
            field = value
            hasAddOtherTemplate = false
            if (TextUtils.isNotEmptyList(list)) {
                if (TextUtils.isNotEmptyList(value)) {
                    list!!.addAll(value!!)
                }
                notifyDataSetChanged()
            } else {
                list = value
            }
        }
    private lateinit var createAddDrawable: Drawable
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemplateListHolder {
        var holder: TemplateListHolder
        if (viewType == 0) {
            holder = TemplateListHolderOne(LayoutInflater.from(parent.context).inflate(R.layout.layout_template_list_item, parent, false)).apply {
                if ("7" == type) {
                    var params = iv.layoutParams
                    params.height = params.width * 1800 / 1080
                    iv.layoutParams = params
                    params = iv_select.layoutParams
                    params.height = params.width * 1800 / 1080
                    iv_select.layoutParams = params
                    if (!releaseTag)
                        iv_pass.visibility = View.VISIBLE
                } else if (type == "9") {
                    var params = iv.layoutParams
                    params.height = params.width * 1800 / 1080
                    iv.layoutParams = params
                    params = iv_select.layoutParams
                    params.height = params.width * 1800 / 1080
                    iv_select.layoutParams = params
                    if (!releaseTag)
                        iv_pass.visibility = View.VISIBLE
                }
                var params = iv.layoutParams
                createAddDrawable = getCreateAdd(params.width, params.height)
                var layoutParams = iv_video.layoutParams as RelativeLayout.LayoutParams
                layoutParams.topMargin = (iv.layoutParams.height - layoutParams.height) / 2 + ViewUtils.getDp(R.dimen.dp_11)
                iv_video.layoutParams = layoutParams
                iv.setOnClickListener(onClick)
            }
        } else if (viewType == -1) {
            holder = TemplateListHolderOne(LayoutInflater.from(parent.context).inflate(R.layout.layout_template_list_item, parent, false)).apply {
                if ("7" == type) {
                    var params = iv.layoutParams
                    params.height = params.width * 1800 / 1080
                    iv.layoutParams = params
                } else if (type == "9") {
                    var params = iv.layoutParams
                    params.height = params.width * 1800 / 1080
                    iv.layoutParams = params
                }
                iv_select.visibility = View.GONE
                iv_video.visibility = View.GONE
                var params = iv.layoutParams
                val createAddDrawable = getCreateAdd(params.width, params.height, if (identityType == "1") R.mipmap.create_add_other_p else R.mipmap.create_add_other_m)
                iv.setImageDrawable(createAddDrawable)
                iv.setOnClickListener {
                    onItemClickListener?.loadOtherTemplate()
                }
            }
        } else {
            holder = TemplateListHolderTow(LayoutInflater.from(parent.context).inflate(R.layout.layout_template_list_tow_media_item, parent, false)).apply {
                if ("7" == type) {
                    var params = towMediaView.layoutParams
                    params.height = params.width * 1800 / 1080
                    towMediaView.layoutParams = params
                    params = iv_select.layoutParams
                    params.height = params.width * 1800 / 1080
                    iv_select.layoutParams = params
                    if (!releaseTag)
                        iv_pass.visibility = View.VISIBLE
                } else if (type == "9") {
                    var params = towMediaView.layoutParams
                    params.height = params.width * 1800 / 1080
                    towMediaView.layoutParams = params
                    params = iv_select.layoutParams
                    params.height = params.width * 1800 / 1080
                    iv_select.layoutParams = params
                    if (!releaseTag)
                        iv_pass.visibility = View.VISIBLE
                }
                towMediaView.setOnClickListener(onClick)
            }
        }
        return holder.apply {
            if (!releaseTag) {
                iv_delete.setOnClickListener(onClick)
            } else {
                iv_delete.visibility = View.GONE
                iv_select.setBackgroundColor(ViewUtils.getColor(R.color.color_000000_70))
                var drawable = ViewUtils.getDrawable(R.mipmap.select_white_1)
                var side = ViewUtils.getDp(R.dimen.dp_24)
                var measuredWidth = ViewUtils.getDp(R.dimen.dp_144)
                var measuredHeight = ViewUtils.getDp(R.dimen.dp_213)
                var left = (measuredWidth - side) / 2
                var top = (measuredHeight - side) / 2
                drawable.setBounds(left, top, left + side, top + side)
                iv_select.setImageDrawable(drawable)
            }
            //tv.setOnClickListener(onClick)
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (hasAddOtherTemplate && position == itemCount - 1) {
            return -1
        }
        if (position == 0 && !releaseTag) {
            return 0
        }
        if ("3" == list!![position - if (releaseTag) 0 else 1].templetType) {
            return 1
        }
        return 0
    }

    override fun getItemCount(): Int {
        return (list?.size ?: 0) + (if (releaseTag) 0 else 1) + (if (hasAddOtherTemplate) 1 else 0)
    }

    private var color = ViewUtils.getColor(R.color.color_666666)
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TemplateListHolder, position: Int) {
        if (hasAddOtherTemplate && releaseTag && position == itemCount - 1) {
            //(holder as TemplateListHolderOne).iv.setImageDrawable(createAddDrawable)
            return
        }
        if (position == 0 && !releaseTag) {
            var holder = holder as TemplateListHolderOne
            holder.iv.setImageDrawable(createAddDrawable)
            //holder.iv.scaleType = ImageView.ScaleType.FIT_XY
            holder.iv.background = ColorDrawable(Color.TRANSPARENT)
            holder.iv_delete.visibility = View.GONE
            holder.iv.setTag(R.id.position, position)
            holder.iv_video.visibility = View.GONE
            holder.iv_select.visibility = View.GONE
            holder.iv_pass.visibility = View.GONE
            if (itemCount == 1 && ("7" == type || "9" == type)) {
                holder.tv.gravity = Gravity.LEFT
                val height = if (type == "7") "1800" else "1800"
//                holder.tv.text = "您可上传图片或视频\n" +
//                        "建议尺寸：1080*${height}px\n" +
//                        ""
            } else {
                holder.tv.text = ""
            }
        } else {
            holder.tv.gravity = Gravity.CENTER
            var templet_id: String? = null
            if (!releaseTag) {
                holder.iv_delete.visibility = View.VISIBLE
            }
            if ((type == "7" || type == "9") && !releaseTag) {
                holder.iv_pass.visibility = View.VISIBLE
            }
            if (holder is TemplateListHolderOne) {
                holder.iv.background = ColorDrawable(color)
                //holder.iv.scaleType = ImageView.ScaleType.FIT_CENTER
                list?.get(position - if (releaseTag) 0 else 1)?.run {
                    holder.tv.text = templetName
                    var glide = Glide.with(context)
                    when (templetType) {
                        "1" -> {
                            holder.iv_video.visibility = View.GONE
                            glide.load(fileList[0].fileUrl).apply(requestOptions).into(holder.iv)
                        }
                        else -> {
                            holder.iv_video.visibility = View.VISIBLE
                            BitmapUtils.loadFirstImageForVideo(glide, fileList[0].fileUrl, holder.iv)
                        }
                    }
                    templet_id = this.templet_id
                    if (holder.iv_pass.visibility == View.VISIBLE)
                        when (status) {
                            "0" -> holder.iv_pass.setImageResource(R.mipmap.order_passing)
                            "1" -> holder.iv_pass.setImageResource(R.mipmap.order_pass)
                            else -> holder.iv_pass.setImageResource(R.mipmap.order_unpass)
                        }
                }
                holder.iv.setTag(R.id.position, position)
            } else if (holder is TemplateListHolderTow) {
                holder.towMediaView.setTag(R.id.position, position)
                list?.get(position - if (releaseTag) 0 else 1)?.run {
                    holder.tv.text = templetName
                    var glide = Glide.with(context)
                    var templetType = fileList[0].type
                    when (templetType) {
                        "1" -> {
                            glide.load(fileList[0].fileUrl).apply(requestOptions).into(holder.towMediaView.getSecond())
                            if (fileList.size > 1) {
                                BitmapUtils.loadFirstImageForVideo(glide, fileList[1].fileUrl, holder.towMediaView.getFirstView())
                            } else {
                                holder.towMediaView.getFirstView().setImageDrawable(null)
                            }
                        }
                        else -> {
                            BitmapUtils.loadFirstImageForVideo(glide, fileList[0].fileUrl, holder.towMediaView.getFirstView())
                            if (fileList.size > 1) {
                                glide.load(fileList[1].fileUrl).apply(requestOptions).into(holder.towMediaView.getSecond())
                            } else {
                                holder.towMediaView.getSecond().setImageDrawable(null)
                            }
                        }
                    }
                    if (holder.iv_pass.visibility == View.VISIBLE)
                        when (status) {
                            "0" -> holder.iv_pass.setImageResource(R.mipmap.order_passing)
                            "1" -> holder.iv_pass.setImageResource(R.mipmap.order_pass)
                            else -> holder.iv_pass.setImageResource(R.mipmap.order_unpass)
                        }
                    templet_id = this.templet_id
                }
            }
            if (releaseTag)
                if (templet_id == selectId) {
                    selectPosition = position
                    holder.iv_select.visibility = View.VISIBLE
                    if (preSelectTag) {
                        list?.get(position - if (releaseTag) 0 else 1)?.let { dataBean ->
                            XspManage.getInstance().newMediaData.templetIds = selectId
                            XspManage.getInstance().newMediaData.templetName = dataBean.templetName
                            XspManage.getInstance().newMediaData.selectedTemplate = dataBean.fileList[0]
                            if (dataBean.templetType == "3") {
                                XspManage.getInstance().newMediaData.preTowData = dataBean.speedDataBean
                                XspManage.getInstance().newMediaData.selectedTemplate = null
                            }
                        }
                        onItemClickListener?.itemClick("", "", "", "", false)
                    }
                } else {
                    if (selectPosition == position) {
                        selectPosition = -1
                    }
                    holder.iv_select.visibility = View.GONE
                }
        }
        holder.tv.setTag(R.id.position, position)
        holder.iv_delete.setTag(R.id.position, position)
        if (position == itemCount - 1) {
            if (preSelectTag)
                preSelectTag = false
        }
    }

    private var deletePosition = -1
    private var onClick = View.OnClickListener {
        var position = it.getTag(R.id.position) as Int
        if (position == 0 && !releaseTag) {
            onItemClickListener?.createTemplate(itemCount - 1)
            return@OnClickListener
        }
        val oldPosition = position
        position = if (releaseTag) (position + 1) else (position)
        var dataBean = list!![position - 1]
        if (it is TextView) {
            deletePosition = position - 1
            onItemClickListener?.changeName(dataBean.templet_id, dataBean.templetName)
            return@OnClickListener
        }
        when (it.id) {
            R.id.iv_delete -> {
                deletePosition = position - 1
                onItemClickListener?.delete(dataBean.templet_id)
            }
            else -> {
                if (releaseTag) {
                    if (selectId == dataBean.templet_id) {
                        selectId = ""
                        val position_ = selectPosition
                        selectPosition = -1
                        if (position_ > -1)
                            notifyItemChanged(position_)
                        onItemClickListener?.itemClick(null, dataBean.templet_id, dataBean.fileList[0].type, dataBean.status, dataBean.templetType == "3")
                        return@OnClickListener
                    }
                    selectId = dataBean.templet_id
                    var select_ = selectPosition
                    selectPosition = oldPosition
                    if (select_ >= 0)
                        notifyItemChanged(select_)
                    notifyItemChanged(selectPosition)
                    XspManage.getInstance().newMediaData.templetIds = selectId
                    XspManage.getInstance().newMediaData.templetName = dataBean.templetName
                    XspManage.getInstance().newMediaData.selectedTemplate = dataBean.fileList[0]
                }
                if (dataBean.templetType == "3") {
                    XspManage.getInstance().newMediaData.preTowData = dataBean.speedDataBean
                    XspManage.getInstance().newMediaData.selectedTemplate = null
                }
                onItemClickListener?.itemClick(dataBean.fileList[0].fileUrl, dataBean.templet_id, dataBean.fileList[0].type, dataBean.status, dataBean.templetType == "3")
            }
        }
    }
    var requestOptions = RequestOptions().error(R.mipmap.ic_launcher)
    fun updateForDelete() {
        if (deletePosition > -1 && list != null) {
            list?.removeAt(deletePosition)
            notifyItemRemoved(deletePosition + 1)
            notifyItemRangeChanged(deletePosition, itemCount - deletePosition)
        }
    }

    fun updateForChangeName(name: String) {
        if (deletePosition > -1 && list != null) {
            list!![deletePosition].templetName = name
            notifyItemChanged(deletePosition + 1)
        }
    }

    open class TemplateListHolder(view: View) : RecyclerView.ViewHolder(view) {
        var tv = view.findViewById<TextView>(R.id.tv)
        var iv_delete = view.findViewById<ImageView>(R.id.iv_delete)
        var iv_select = view.findViewById<ImageView>(R.id.iv_select)
        var iv_pass = view.findViewById<ImageView>(R.id.iv_pass)
    }

    class TemplateListHolderOne(view: View) : TemplateListHolder(view) {
        var iv_video = view.findViewById<ImageView>(R.id.iv_video)
        var iv = view.findViewById<ImageView>(R.id.iv)
    }

    class TemplateListHolderTow(view: View) : TemplateListHolder(view) {
        var towMediaView = view.findViewById<TowMediaView>(R.id.towMediaView)
    }

    var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener : OnDeleteListener {
        fun itemClick(path_content: String?, id: String, fileType: String, statue: String, towTag: Boolean = false)
        fun createTemplate(count: Int)
        fun changeName(templateId: String, name: String)
        fun loadOtherTemplate()
    }

    private fun getCreateAdd(width: Int, height: Int, res: Int = R.mipmap.create_add): Drawable {
        val dp96 = ViewUtils.getDp(R.dimen.dp_95)
        val dp130 = ViewUtils.getDp(R.dimen.dp_130)
        return object : Drawable() {
            private var paint = Paint().apply {
                isAntiAlias = true
                isDither = true
                style = Paint.Style.STROKE
                val array = FloatArray(2)
                array[0] = 4f
                array[1] = 4f
                pathEffect = DashPathEffect(array, 0f)
            }

            override fun draw(canvas: Canvas) {
                canvas.drawColor(Color.WHITE)
                ViewUtils.getDrawable(res).apply {
                    //Log.e("TAG", "getCreateAdd: width=$intrinsicWidth  height=$intrinsicHeight")
                    //var dp130=intrinsicWidth
                    val left = (width - dp130) / 2
                    val top = (height - dp96) / 2
                    setBounds(left, top, left + dp130, top + dp96)
                }.draw(canvas)
                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), paint)
            }

            override fun setAlpha(alpha: Int) {
                paint.alpha = alpha
            }

            override fun getOpacity(): Int {
                return PixelFormat.TRANSLUCENT
            }

            override fun setColorFilter(colorFilter: ColorFilter?) {
                paint.colorFilter = colorFilter
            }
        }.apply {
            setBounds(0, 0, width, height)
        }
    }
}