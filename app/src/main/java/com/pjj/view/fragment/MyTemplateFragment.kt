package com.pjj.view.fragment

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.amap.api.col.sl3.ob
import com.pjj.R
import com.pjj.module.xspad.XspManage
import com.pjj.present.BasePresent
import com.pjj.utils.SharedUtils
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.activity.*
import kotlinx.android.synthetic.main.activity_my_template.*

class MyTemplateFragment : ABFragment<BasePresent<*>>() {
    override fun getLayoutRes(): Int {
        return R.layout.activity_my_template
    }

    override fun isHeadPaddingStatue(): Boolean {
        return true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val list = ArrayList<TemplateBean>(7)
        list.add(TemplateBean("广告传媒模板管理", R.mipmap.new_media))
        list.add(TemplateBean("电梯传媒模板管理", R.mipmap.template_chuantong))
        list.add(TemplateBean("DIY信息模板管理", R.mipmap.template_diy))
//        list.add(TemplateBean("电梯随机信息发布", R.mipmap.template_suiji))
        list.add(TemplateBean("自用模板管理", R.mipmap.zi_yong))
//        list.add(TemplateBean("电梯随机便民信息发布", R.mipmap.template_suiji_bm))
//        list.add(TemplateBean("电梯拼屏信息发布", R.mipmap.template_pin_ping))
        rv_template.layoutManager = GridLayoutManager(activity, 2)
        rv_template.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect?, view: View?, parent: RecyclerView?, state: RecyclerView.State?) {
                outRect?.let {
                    it.right = ViewUtils.getDp(R.dimen.dp_24)
                }
            }
        })
        rv_template.adapter = MyAdapter().apply {
            this.list = list
        }
    }

    private val onAdapterClick = View.OnClickListener {
        when (it.tag as Int) {
            0 -> {//新传媒
                XspManage.getInstance().newMediaData.releaseTag = false
                XspManage.getInstance().adType = 7
                //startActivity(Intent(activity!!, ReleaseRuleActivity::class.java).putExtra("title", "创建新媒体模板"))
                startActivity(Intent(activity!!, CreateTemplateExplainActivity::class.java).putExtra("title", "创建广告传媒模板"))
            }
            1 -> {//传统信息
                XspManage.getInstance().newMediaData.releaseTag = false
                XspManage.getInstance().adType = 9
                startActivity(Intent(activity!!, CreateTemplateExplainActivity::class.java).putExtra("title", "创建电梯传媒模板"))
            }
            //2 -> CreateTemplateExplainActivity.start(activity as Context, CreateTemplateExplainActivity.DIY_TEM)
            //3 -> CreateTemplateExplainActivity.start(activity as Context, CreateTemplateExplainActivity.SUI_JI_TEM_DIY)
            3 -> {
                //请联系4001251818热线购买广告屏
                val useType = SharedUtils.getXmlForKey(SharedUtils.USER_TYPE)
                if (TextUtils.isEmpty(useType) || ("cooperativePartner" != useType && "companyLaolingwei" != useType && "subManagement" != useType)) {
                    showNotice("请联系4001251818热线购买广告屏")
                    return@OnClickListener
                }
                //showNotice("未知")
                startActivity(Intent(activity!!, SelfUseActivity::class.java))
                /*XspManage.getInstance().newMediaData.releaseTag = false
                startActivity(Intent(activity!!, TemplateListActivity::class.java)
                        .putExtra("title_text", "自用模板")
                        .putExtra("releaseTag", false)
                        .putExtra("identity_type", "1")
                        .putExtra("ad_type", "1"))*/
                //CreateTemplateExplainActivity.start(activity as Context, CreateTemplateExplainActivity.BIAN_MIN_TEM)
            }
            //5 -> CreateTemplateExplainActivity.start(activity as Context, CreateTemplateExplainActivity.SUI_JI_TEM_BM)
            else -> showNotice("该功能暂未开放\n敬请期待")
            //6 -> SpeedScreenActivity.newInstance(activity!!)
            //MyTemplateNextActivity

        }
    }

    private inner class MyAdapter : RecyclerView.Adapter<MyHolder>() {
        var list: MutableList<TemplateBean>? = null
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
            val context = parent.context
            return MyHolder(LinearLayout(context).apply {
                orientation = LinearLayout.VERTICAL
                addView(ImageView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(ViewUtils.getDp(R.dimen.dp_144), ViewUtils.getDp(R.dimen.dp_213))
                    scaleType = ImageView.ScaleType.FIT_XY
                })
                addView(TextView(context).apply {
                    layoutParams = LinearLayout.LayoutParams(ViewUtils.getDp(R.dimen.dp_144), ViewUtils.getDp(R.dimen.dp_38))
                    setPadding(0, ViewUtils.getDp(R.dimen.dp_9), 0, 0)
                    gravity = Gravity.CENTER_HORIZONTAL
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_14))
                    setTextColor(ViewUtils.getColor(R.color.color_666666))
                })
            }).apply {
                iv.setOnClickListener(onAdapterClick)
            }
        }

        override fun getItemCount(): Int {
            return list?.size ?: 0
        }

        override fun onBindViewHolder(holder: MyHolder, position: Int) {
            holder.tv.text = list!![position].text
            holder.iv.setImageResource(list!![position].resource)
            holder.iv.tag = position
        }

    }

    private class MyHolder(view: ViewGroup) : RecyclerView.ViewHolder(view) {
        var iv = view.getChildAt(0) as ImageView
        var tv = view.getChildAt(1) as TextView
    }

    class TemplateBean(text: String, resource: Int) {
        var text = ""
        var resource = -1

        init {
            this.text = text
            this.resource = resource
        }
    }
}
