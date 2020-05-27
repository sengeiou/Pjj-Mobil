package com.pjj.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pjj.R
import com.pjj.module.xspad.XspManage
import com.pjj.present.BasePresent
import com.pjj.utils.BitmapUtils
import com.pjj.utils.Log
import com.pjj.utils.ViewUtils
import com.pjj.view.custom.SpeedMediaViewGroup
import kotlinx.android.synthetic.main.activity_preview.*
import java.nio.charset.Charset

/**
 * 广告屏预览
 */
class PreviewXspActivity : BaseActivity<BasePresent<*>>() {
    companion object {
        private val FILE_URL = "file_path"
        private val FILE_TYPE = "file_type"
        private val AD_NAME = "ad_name"
        private val AD_PHONE = "ad_phone"
        fun startActivity(context: Activity, fileUrl: String, fileType: String, name: String? = null, phone: String? = null) {
            context.startActivity(Intent(context, PreviewXspActivity::class.java)
                    .putExtra(FILE_URL, fileUrl)
                    .putExtra(FILE_TYPE, fileType)
                    .putExtra(AD_NAME, name)
                    .putExtra(AD_PHONE, phone)
            )
        }

        fun startActivity(context: Activity, fileUrl: String, fileType: String, mbHidden: Boolean, two: Boolean = false) {
            context.startActivity(Intent(context, PreviewXspActivity::class.java)
                    .putExtra(FILE_URL, fileUrl)
                    .putExtra(FILE_TYPE, fileType)
                    .putExtra("mbHidden", mbHidden)
                    .putExtra("towTag", two)
            )
        }
    }

    private val filePath: String?
        get() {
            return intent.getStringExtra(FILE_URL)
        }
    private var videoTag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview)
        setTitle("预览")

        waiteDialog.setCancelable(true)
        var fileType = intent.getStringExtra(FILE_TYPE)

        if (intent.getBooleanExtra("mbHidden", false)) {
            rl_xsp.removeView(fl_content)
            var params = fl_diy.layoutParams
            params.height = params.width * 1800 / 1080
            fl_diy.layoutParams = params
            if (intent.getBooleanExtra("towTag", false)) {
                fl_diy.removeAllViews()
                fl_diy.addView(SpeedMediaViewGroup(this).apply {
                    params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                    speedData = XspManage.getInstance().newMediaData.preTowData
                    post { startVideo() }
                })
            } else {
                fillVideoContent(fileType)

            }
            return
        }
        if (fileType != "0") {
            addBM("热烈庆祝屏加加app以及电梯物联网系统本月正式上线；屏联你我，加载未来，开启电梯安全新纪元！！！", "4001251818", "屏加加科技有限公司")
            fillVideoContent(fileType)
        } else {
            iv.scaleType = ImageView.ScaleType.FIT_XY
            addBM(filePath, intent.getStringExtra(AD_PHONE), intent.getStringExtra(AD_NAME))
        }
        view_space.post {
            var dp11 = ViewUtils.getDp(R.dimen.dp_11)
            //var dp42 = ViewUtils.getDp(R.dimen.dp_42)
            var parentHeight = (view_space.parent as ViewGroup).measuredHeight
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

    private fun fillVideoContent(fileType: String?) {
        Glide.with(this).let {
            when (fileType) {
                "1" -> //图片
                    it.load(filePath).into(iv)
                "2" -> {
                    showPlayButton()
                    BitmapUtils.loadFirstImageForVideo(it, filePath, iv)
                    //video.setMediaController(MediaController(this))
                    video.setOnPreparedListener { mediaPlayer ->
                        //mediaPlayer.setVolume(0f, 0f)
                        cancelWaiteStatue()
                        iv.visibility = View.GONE
                        iv_player.visibility = View.GONE
                        var videoWidth = mediaPlayer.videoWidth
                        var videoHeight = mediaPlayer.videoHeight
                        video.setVideoSize(videoWidth, videoHeight)
                        mediaPlayer.start()
                    }
                    video.setOnCompletionListener {
                        iv_player.visibility = View.VISIBLE
                    }
                    video.setOnErrorListener { _, _, _ ->
                        cancelWaiteStatue()
                        iv_player.visibility = View.GONE
                        //finish()
                        true
                    }
                    iv_player.performClick()
                }
                else -> {
                }
            }
        }
    }

    private fun addBM(content: String?, phone: String?, name: String?) {
        var color = XspManage.getBgColor(11)
        var view = LayoutInflater.from(this).inflate(R.layout.layout_xsp_bm_item, fl_content, false)
        var tvContent = view.findViewById<TextView>(R.id.tv_content)
        content?.let {
            if (it.toByteArray(Charset.forName("GBK")).size <= 37) {
                tvContent.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_15))
            }
        }

        tvContent.text = content
        view.findViewById<TextView>(R.id.tv_title_type).text = "便民信息"
        view.findViewById<View>(R.id.view_space).background = ColorDrawable(color)

        view.findViewById<ImageView>(R.id.iv_left).setImageResource(XspManage.getLeftResource("11"))
        view.findViewById<ImageView>(R.id.iv_right).setImageResource(XspManage.getRightResource("11"))
        var showBottom = false
        phone?.let {
            var tv_phone_name = view.findViewById<TextView>(R.id.tv_phone_name)
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
        if (iv_player.visibility == View.VISIBLE) {
            iv_player.performClick()
        }
    }

    private fun showPlayButton() {
        iv_player.visibility = View.VISIBLE
        iv_player.setOnClickListener(onClick)
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.iv_player -> {
                videoTag = true
                Log.e("TAG", filePath)
                video.visibility = View.VISIBLE
                video.setVideoPath(filePath)
                showWaiteStatue()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (videoTag && !video.isPlaying) {

            video.start()
        }
    }

    override fun onPause() {
        super.onPause()
        if (videoTag && video.canPause()) {
            video.pause()
        }
    }

    override fun waiteInitiativeCancel() {
        cancelWaiteStatue()
        finish()
    }
}
