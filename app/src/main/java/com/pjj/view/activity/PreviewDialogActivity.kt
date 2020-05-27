package com.pjj.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.pjj.R
import com.pjj.module.xspad.XspManage
import com.pjj.present.BasePresent
import com.pjj.utils.BitmapUtils
import com.pjj.utils.Log
import com.pjj.view.custom.SpeedMediaViewGroup
import kotlinx.android.synthetic.main.activity_preview_dialog.*

/**
 * 广告屏预览
 */
class PreviewDialogActivity : BaseActivity<BasePresent<*>>() {
    companion object {
        private val FILE_URL = "file_path"
        private val FILE_TYPE = "file_type"

        fun startActivity(context: Activity, fileUrl: String, fileType: String, templetName: String, two: Boolean = false) {
            context.startActivity(Intent(context, PreviewDialogActivity::class.java)
                    .putExtra(FILE_URL, fileUrl)
                    .putExtra(FILE_TYPE, fileType)
                    .putExtra("templetName", templetName)
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
        setContentView(R.layout.activity_preview_dialog)
        window.navigationBarColor = getStatusBarColor()
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
        val fileType = intent.getStringExtra(FILE_TYPE)

        intent.getStringExtra("templetName")?.let {
            tv_name.text = it
        }
        waiteDialog.setCancelable(true)
        /*fl_diy.post {
            val params = fl_diy.layoutParams
            val heightOld = params.height
            val newHeight = params.width * 1800 / 1080
            if (heightOld > newHeight) {
                params.height = newHeight
            } else {
                params.width = heightOld * 1080 / 1800
            }
            fl_diy.layoutParams = params
        }*/
        if (intent.getBooleanExtra("towTag", false)) {
            fl_diy.removeAllViews()
            fl_diy.addView(SpeedMediaViewGroup(this).apply {
                //params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                speedData = XspManage.getInstance().newMediaData.preTowData
                post { startVideo() }
            })
        } else {
            fillVideoContent(fileType)
        }
        iv_close.setOnClickListener(onClick)
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
                        Log.e("TAG", "setOnPreparedListener")
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
            R.id.iv_close -> {
                finish()
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

    override fun getStatusBarColor(): Int {
        return Color.BLACK
    }

}
