package com.pjj.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.bumptech.glide.Glide
import com.pjj.R
import com.pjj.module.xspad.XspManage
import com.pjj.present.BasePresent
import com.pjj.utils.BitmapUtils
import com.pjj.utils.Log
import com.pjj.utils.ViewUtils
import com.pjj.view.custom.SpeedMediaViewGroup
import com.pjj.view.custom.SpeedViewGroup
import kotlinx.android.synthetic.main.activity_preview_new_media.*

class PreviewNewMediaActivity : BaseActivity<BasePresent<*>>() {
    companion object {
        private val FILE_URL = "file_path"
        private val FILE_TYPE = "file_type"
        private val TOW_TAG = "tow_tag"
        fun newInstance(activity: Activity, fileUrl: String, fileType: String, towTag: Boolean = false) {
            activity.startActivity(Intent(activity, PreviewNewMediaActivity::class.java)
                    .putExtra(FILE_URL, fileUrl)
                    .putExtra(TOW_TAG, towTag)
                    .putExtra(FILE_TYPE, fileType)

            )
        }
    }

    private var videoTag = false
    private lateinit var filePath: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_new_media)
        setTitle("预览")
        waiteDialog.setCancelable(true)
        var fileType = intent.getStringExtra(FILE_TYPE)
        filePath = intent.getStringExtra(FILE_URL)
        val tow_tag = intent.getBooleanExtra(TOW_TAG, false)
        if (tow_tag) {
            iv_screenshots.removeAllViews()
            iv_screenshots.addView(SpeedMediaViewGroup(this).apply {
                layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT)
                speedData = XspManage.getInstance().newMediaData.preTowData
                post { startVideo() }
            })
        } else {
            Glide.with(this).let {
                when (fileType) {
                    "1" -> //图片
                        it.load(filePath).into(iv)
                    "2" -> {
                        showPlayButton()
                        BitmapUtils.loadFirstImageForVideo(it, filePath, iv)
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
                    }
                    else -> {
                    }
                }
            }
            if (iv_player.visibility == View.VISIBLE) {
                iv_player.performClick()
            }
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
}
