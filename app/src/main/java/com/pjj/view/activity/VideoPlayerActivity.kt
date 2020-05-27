package com.pjj.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.KeyEvent
import android.view.View
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.intent.RetrofitService
import com.pjj.present.BasePresent
import com.pjj.utils.FileUtils
import com.pjj.utils.Log
import com.pjj.utils.TextUtils
import com.pjj.view.ijk.IjkVideoView
import kotlinx.android.synthetic.main.activity_video_player.*
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import tv.danmaku.ijk.media.player.misc.ITrackInfo

/**
 * Created by XinHeng on 2018/12/10.
 * describe：监播
 */
class VideoPlayerActivity : BaseActivity<BasePresent<*>>() {
    private lateinit var mVideoView: IjkVideoView
    private var mBackPressed: Boolean = false
    private var mVideoPath: String? = null
    private lateinit var handler: Handler

    companion object {
        fun intentTo(context: Context, videoPath: String) {
            context.startActivity(Intent(context, VideoPlayerActivity::class.java).putExtra("video_path", videoPath))
        }
    }

    private var screenshotsTag = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_player)
        setTitle("监播")
        handler = Handler()
        tv_screenshots.visibility = View.GONE
        waiteDialog.setCancelable(true)
        mVideoPath = intent.getStringExtra("video_path")
//        mVideoPath = "rtmp://47.92.254.65/live/123"
        //mVideoPath = "http://ivi.bupt.edu.cn/hls/cctv6hd.m3u8"
        //mVideoPath = "rtmp://live.hkstv.hk.lxdns.com/live/hks2"
        // init player
        IjkMediaPlayer.loadLibrariesOnce(null)
        IjkMediaPlayer.native_profileBegin("libijkplayer.so")
        mVideoView = findViewById(R.id.video_view)
        mVideoView.setOnPreparedListener {
            //it.setVolume(2f,2f)
            it.start()
            cancelWaiteStatue()
            var trackInfo = it.trackInfo
            for (trackInfo in trackInfo) {
                Log.e("TAG", ": ${trackInfo.infoInline}")
            }
            var selectedTrack = mVideoView.getSelectedTrack(ITrackInfo.MEDIA_TRACK_TYPE_AUDIO)
            Log.e("TAG", "setOnPreparedListener: selectedTrack=$selectedTrack")
            //mVideoView.selectTrack(0)
            //var selectedTrack1 = mVideoView.getSelectedTrack(ITrackInfo.MEDIA_TRACK_TYPE_AUDIO)
            //Log.e("TAG", "setOnPreparedListener: selectedTrack=$selectedTrack1")
        }
        mVideoView.setOnErrorListener { mp, what, extra ->
            Log.e("TAG", "OnErrorListener: what=$what extra=$extra")
            FileUtils.saveStringFile(PjjApplication.App_Path + "player.txt", "OnErrorListener: what=$what extra=$extra")
            showWaiteStatue()
            mVideoView.setVideoPath(mVideoPath)
            true
        }
        if (TextUtils.isEmpty(mVideoPath)) {
            showNotice("播放路径错误")
            return
        } else {
            showWaiteStatue()
            mVideoView.setVideoPath(mVideoPath)
            tv_screenshots.setOnClickListener(onClick)
        }
    }

    override fun onResume() {
        super.onResume()
        screenshotsTag = false
        mVideoView.start()
    }

    override fun onPause() {
        super.onPause()
        mVideoView.pause()
    }

    override fun onBackPressed() {
        mBackPressed = true
        super.onBackPressed()
    }

    override fun waiteInitiativeCancel() {
        cancelWaiteStatue()
        finish()
    }

    override fun onStop() {
        super.onStop()
        if (mBackPressed || !mVideoView.isBackgroundPlayEnabled) {
            mVideoView.stopPlayback()
            mVideoView.release(true)
            mVideoView.stopBackgroundPlay()
        } else {
            mVideoView.enterBackground()
        }
        IjkMediaPlayer.native_profileEnd()
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_screenshots -> {//启动截屏
                startScreenshots()
            }
        }
    }

    private fun startScreenshots() {
        showWaiteStatue()
        screenshotsTag = true
        //TODO 访问服务返回 图片名字
        //RetrofitService.getInstance().
    }

    private fun checkScreenshotsImage(name: String) {
        handler.postDelayed({
            RetrofitService.getInstance().downloadFile(PjjApplication.filePath + name, PjjApplication.App_Path + "xsp_screenshots/", object : FileUtils.OnDownloadListener {
                override fun success(filePath: String) {
                    if (!isFinishing) {
                        screenshotsTag = false
                        cancelWaiteStatue()
                        showScreenshots(filePath)
                    }
                }

                override fun fail() {
                    checkScreenshotsImage(name)
                }

            })
        }, 5000)
    }

    private fun showScreenshots(path: String) {
        PreviewScreenShotsActivity.newInstance(this, path)
        cancelWaiteStatue()
    }
}