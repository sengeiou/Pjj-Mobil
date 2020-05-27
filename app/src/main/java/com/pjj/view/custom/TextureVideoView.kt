package com.pjj.view.custom

import android.content.Context
import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.net.Uri
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.Surface
import android.view.TextureView
import android.view.View

import java.io.File
import java.io.IOException

class TextureVideoView : TextureView, TextureView.SurfaceTextureListener {
    private val TAG = "TAG_TextureVideoView"
    private var surface: Surface? = null
    private var mMediaPlayer: MediaPlayer? = null
    private var onPreparedListener: MediaPlayer.OnPreparedListener? = null
    private var mVideoWidth: Int = 0
    private var mVideoHeight: Int = 0
    private var mVideoPath: String? = null
    //播放加载状态 0初始 1预加载完成 2有播放请求
    private var playStatue: Int = 0
    /**
     * 适应屏幕
     */
    private val tagScreen = true

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs, 0) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    private fun init() {
        mMediaPlayer = MediaPlayer()
        mMediaPlayer!!.setOnPreparedListener { mp ->
            //mp.isLooping = true
            val mVideoWidth = mp.videoWidth
            val mVideoHeight = mp.videoHeight
            setVideoSize(mVideoWidth, mVideoHeight)
            //Log.e(TAG, "init: start play " + playStatue);
            /*if (playStatue == 2) {
                mp.start()
            } else {
                playStatue = 1
            }*/
            mp.start()
            if (null != onPreparedListener) {
                onPreparedListener!!.onPrepared(mp)
            }
        }
        mMediaPlayer!!.setOnErrorListener { mp, what, extra ->
            Log.e(TAG, "what=$what, extra=$extra")
            mp.reset()
            if (!TextUtils.isEmpty(mVideoPath)) {
                //setVideoPath_(mVideoPath);
            }
            true
        }
        mMediaPlayer!!.setOnCompletionListener { mp ->
            playStatue = 1
            onCompletionListener?.onCompletion(mp)
        }
        surfaceTextureListener = this
        if (!TextUtils.isEmpty(mVideoPath)) {
            setVideoPath_(mVideoPath)
        }
    }

    var onCompletionListener: MediaPlayer.OnCompletionListener? = null
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var width = View.getDefaultSize(mVideoWidth, widthMeasureSpec)
        var height = View.getDefaultSize(mVideoHeight, heightMeasureSpec)
        if (mVideoWidth > 0 && mVideoHeight > 0) {
            val widthSpecMode = MeasureSpec.getMode(widthMeasureSpec)
            val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
            val heightSpecMode = MeasureSpec.getMode(heightMeasureSpec)
            val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)

            if (widthSpecMode == MeasureSpec.EXACTLY && heightSpecMode == View.MeasureSpec.EXACTLY) {
                // the size is fixed
                width = widthSpecSize
                height = heightSpecSize

                // for compatibility, we adjust size based on aspect ratio
                if (mVideoWidth * height < width * mVideoHeight) {
                    //Log.i("@@@", "image too wide, correcting");
                    width = height * mVideoWidth / mVideoHeight
                } else if (mVideoWidth * height > width * mVideoHeight) {
                    //Log.i("@@@", "image too tall, correcting");
                    height = width * mVideoHeight / mVideoWidth
                }
            } else if (widthSpecMode == View.MeasureSpec.EXACTLY) {
                // only the width is fixed, adjust the height to match aspect ratio if possible
                width = widthSpecSize
                height = width * mVideoHeight / mVideoWidth
                if (heightSpecMode == View.MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    height = heightSpecSize
                }
            } else if (heightSpecMode == View.MeasureSpec.EXACTLY) {
                // only the height is fixed, adjust the width to match aspect ratio if possible
                height = heightSpecSize
                width = height * mVideoWidth / mVideoHeight
                if (widthSpecMode == View.MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // couldn't match aspect ratio within the constraints
                    width = widthSpecSize
                }
            } else {
                // neither the width nor the height are fixed, try to use actual video size
                width = mVideoWidth
                height = mVideoHeight
                if (heightSpecMode == View.MeasureSpec.AT_MOST && height > heightSpecSize) {
                    // too tall, decrease both width and height
                    height = heightSpecSize
                    width = height * mVideoWidth / mVideoHeight
                }
                if (widthSpecMode == View.MeasureSpec.AT_MOST && width > widthSpecSize) {
                    // too wide, decrease both width and height
                    width = widthSpecSize
                    height = width * mVideoHeight / mVideoWidth
                }
            }
        } else {
            // no size yet, just adopt the given spec sizes
        }
        //Log.e("TAG", "onMeasure: video" + width + ", " + height);
        setMeasuredDimension(width, height)
    }

    override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
        //Log.e(TAG, "onSurfaceTextureAvailable: width=" + width + ", height=" + height);
        if (null == surface) {
            surface = Surface(surfaceTexture)
            if (null != mMediaPlayer)
                mMediaPlayer!!.setSurface(surface)
        }
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {
        //Log.e(TAG, "onSurfaceTextureSizeChanged:  width=" + width + ", height=" + height);
        //getBitmap()
    }

    override fun onSurfaceTextureDestroyed(surfaceTexture: SurfaceTexture): Boolean {
        //Log.e(TAG, "onSurfaceTextureDestroyed: ");
        surfaceTexture.release()
        if (null != surface) {
            surface!!.release()
            surface = null
        }
        stopPlayback()
        return true
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
        //Log.e(TAG, "onSurfaceTextureUpdated: ");
    }

    fun setOnPreparedListener(listener: MediaPlayer.OnPreparedListener) {
        this.onPreparedListener = listener
    }

    fun setVideoPath(path: String) {
        mVideoPath = path
        playStatue = 0
        if (null != mMediaPlayer) {
            setVideoPath_(path)
        }
    }

    private fun setVideoPath_(path: String?) {
        mMediaPlayer!!.reset()
        try {
            mMediaPlayer!!.setDataSource(path)
//            val parse = Uri.fromFile(File(path))
            //val parse = Uri.parse(path)
            //Log.e(TAG, "setVideoPath_: " + parse.scheme + ", path=" + path)
            //mMediaPlayer!!.setDataSource(context, parse, null)
            mMediaPlayer!!.prepareAsync()
            //            mMediaPlayer.prepare();
            //            mMediaPlayer.start();
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        }

    }

    fun start() {
        /*if (null != mMediaPlayer && playStatue == 1) {
            mMediaPlayer!!.start()
            Log.e("TAG","start")
        }
        playStatue = 2*/
    }

    fun pause() {
        if (null != mMediaPlayer && mMediaPlayer!!.isPlaying) {
            mMediaPlayer!!.pause()
        }
    }

    fun stopPlayback() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.release()
            mMediaPlayer = null
        }
    }

    fun reset() {
        if (mMediaPlayer != null) {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.reset()
        }
    }

    fun rePlay() {
        if (null != mMediaPlayer && mMediaPlayer!!.isPlaying) {
            mMediaPlayer!!.seekTo(0)
        }
    }

    fun setVideoSize(width: Int, height: Int) {
        // Log.e("TAG", "setVideoSize: width=" + width + ", height=" + height);
        if (width >= height || tagScreen) {
            mVideoWidth = width
            mVideoHeight = height
        } else {
            mVideoWidth = 0
            mVideoHeight = 0
        }
        requestLayout()
    }
}
