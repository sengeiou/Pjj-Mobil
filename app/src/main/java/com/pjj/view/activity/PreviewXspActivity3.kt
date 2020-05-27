package com.pjj.view.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.pjj.R
import com.pjj.present.BasePresent
import com.pjj.utils.BitmapUtils
import com.pjj.utils.Log
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_preview1.*
import kotlinx.android.synthetic.main.layout_ijkplayer_item.*

/**
 * 广告屏预览
 */
class PreviewXspActivity3 : BaseActivity<BasePresent<*>>() {
    companion object {
        private val FILE_URL = "file_path"
        private val FILE_TYPE = "file_type"
        fun startActivity(context: Activity, fileUrl: String, fileType: String) {
            context.startActivity(Intent(context, PreviewXspActivity3::class.java)
                    .putExtra(FILE_URL, fileUrl)
                    .putExtra(FILE_TYPE, fileType))

        }
    }

    private val filePath: String?
        get() {
            return intent.getStringExtra(FILE_URL)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview1)
        setTitle("预览")

        var fileType = intent.getStringExtra(FILE_TYPE)
        if (fileType != "0") {
            Glide.with(this).let {
                when (fileType) {
                    "1" -> //图片
                        it.load(filePath).into(iv)
                    "2" -> {
                        stub_ijk.inflate()
                        showPlayButton()
                        BitmapUtils.loadFirstImageForVideo(it, filePath, iv)
                        //video.setMediaController(MediaController(this))
                        video.setOnPreparedListener { mediaPlayer ->
                            cancelWaiteStatue()
                            iv.visibility = View.GONE
                            iv_player.visibility = View.GONE
                            //var videoWidth = mediaPlayer.videoWidth
                            //var videoHeight = mediaPlayer.videoHeight
                            mediaPlayer.start()
                        }
                        video.setOnCompletionListener {
                            iv_player.visibility = View.VISIBLE
                        }
                        video.setOnErrorListener { _, _, _ ->
                            cancelWaiteStatue()
                            iv_player.visibility = View.GONE
                            //finish()
                            false
                        }
                    }
                    else -> {
                    }
                }
            }

        } else {
            tv_bm.text = filePath
            iv.scaleType = ImageView.ScaleType.FIT_XY
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
                Log.e("TAG", filePath)
                video.visibility = View.VISIBLE
                video.setVideoPath(filePath)
                showWaiteStatue()
            }
        }
    }

}
