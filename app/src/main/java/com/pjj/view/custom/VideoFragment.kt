package com.pjj.view.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.pjj.R
import com.pjj.view.viewinterface.OnVideoLoadListener

/**
 * Created by XinHeng on 2019/04/16.
 * describe：
 */
class VideoFragment @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {
    private val videoView: MyVideoView
    private val startButton: ImageView
    private var videoLoadListener: OnVideoLoadListener? = null
    private var videoPath: String? = null

    init {
        addView(MyVideoView(context).apply {
            videoView = this
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER)
        })
        addView(ImageView(context).apply {
            layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT, Gravity.CENTER)
            setImageResource(R.drawable.video)
            startButton = this
            setOnClickListener {
                videoView.start()
                startButton.visibility = View.GONE
                videoLoadListener?.startLoad()
            }
        })
        videoView.setOnCompletionListener {
            startButton.visibility = View.VISIBLE
        }
        videoView.setOnPreparedListener {
            var videoWidth = it.videoWidth
            var videoHeight = it.videoHeight
            videoView.setVideoSize(videoWidth, videoHeight)
            videoLoadListener?.finishLoad()
        }
        videoView.setOnErrorListener { mp, _, _ ->
            videoLoadListener?.showNotice("播放失败")
            false
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val side = measuredWidth * 1 / 7
        val imageView = getChildAt(1) as ImageView
        var params = imageView.layoutParams
        params.width = side
        params.height = side
        imageView.layoutParams = params
    }

    fun setVideoPath(path: String) {
        Log.e("TAG", "path=$path")
        videoView.setVideoPath(path)
    }

    fun startVideo() {
        startButton.visibility = View.GONE
        videoView.start()
    }

}