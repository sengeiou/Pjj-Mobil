package com.pjj.view.dialog

import android.content.Context
import android.graphics.Color
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.pjj.R
import com.pjj.module.xspad.XspManage
import com.pjj.utils.BitmapUtils
import com.pjj.utils.Log
import com.pjj.view.custom.ClipImageView
import com.pjj.view.custom.SpeedMediaViewGroup
import com.pjj.view.custom.TextureVideoView
import kotlinx.android.synthetic.main.layout_zhi_ding_pre_item.*

class ZhiDingPreDialog(context: Context) : FullWithNoTitleDialog(context, 0) {
    override fun getHeightRate(): Float {
        return 1f
    }

    private var filePath: String = ""

    init {
        setContentView(R.layout.layout_zhi_ding_pre_item)
        iv_close.setOnClickListener(onClick)
        view_close.setOnClickListener(onClick)
    }

    fun setContent(name: String, fileUrl: String, fileType: String, two: Boolean = false) {
        tv_name.text = name
        var params = fl_content.layoutParams
        params.height = params.width * 1800 / 1080
        if (two) {
            fl_content.removeAllViews()
            fl_content.addView(SpeedMediaViewGroup(context).apply {
                params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                speedData = XspManage.getInstance().newMediaData.preTowData
                post { startVideo() }
            })
        } else {
            filePath = fileUrl
            fillVideoContent(fileType)
        }
        show()
    }

    private fun fillVideoContent(fileType: String?) {
        Glide.with(context).let {
            when (fileType) {
                "1" -> { //图片
                    iv.visibility = View.VISIBLE
                    iv_player.visibility = View.GONE
                    video.visibility = View.GONE
                    it.load(filePath).into(iv)
                }
                "2" -> {
                    showPlayButton()
                    iv.visibility = View.VISIBLE
                    BitmapUtils.loadFirstImageForVideo(it, filePath, iv)
                    //video.setMediaController(MediaController(this))
                    //iv.visibility = View.GONE
                    //video.setVideoPath(filePath)
                    //video.start()
                    video.onCompletionListener = MediaPlayer.OnCompletionListener { iv_player.visibility = View.VISIBLE }
                    onClick(iv_player)
                }
                else -> {
                }
            }
        }
    }

    private fun showPlayButton() {
        //iv_player.visibility = View.VISIBLE
        iv_player.setOnClickListener(onClick)
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.iv_player -> {
                Log.e("TAG", "onClick: $filePath")
                video.visibility = View.VISIBLE
                iv.visibility = View.GONE
                video.setVideoPath(filePath)
                video.start()
                //showWaiteStatue()
            }
            R.id.view_close, R.id.iv_close -> dismiss()
        }
    }

    override fun dismiss() {
        video.stopPlayback()
        super.dismiss()
    }
}