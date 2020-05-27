package com.pjj.view.activity

import android.app.Activity
import android.content.Intent
import android.graphics.PointF
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.DrawableImageViewTarget
import com.pjj.R
import com.pjj.present.BasePresent
import com.pjj.utils.Log
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_preview_screen_shots.*

class PreviewScreenShotsActivity : BaseActivity<BasePresent<*>>() {
    companion object {
        private val PREVIEW_PATH = "preview_path"
        fun newInstance(activity: Activity, path: String) {
            activity.startActivity(Intent(activity, PreviewScreenShotsActivity::class.java).putExtra(PREVIEW_PATH, path))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preview_screen_shots)
        setTitle("预览")
        var path = intent.getStringExtra(PREVIEW_PATH)
        if (!TextUtils.isEmpty(path)) {
            Log.e("TAG", "path=$path")
            //path = "/storage/emulated/0/pingjiajia/image/1555653654089.png"
            iv_screenshots.post {
                val widthIv = iv_screenshots.measuredWidth
                val lp = iv_screenshots.layoutParams
                Glide.with(this).asDrawable().load(path).into(object : DrawableImageViewTarget(iv_screenshots) {
                    override fun setResource(resource: Drawable?) {
                        resource?.let {
                            val height = widthIv * it.intrinsicHeight / it.intrinsicWidth
                            it.intrinsicWidth * 1f / it.intrinsicHeight
                            lp.width = widthIv
                            lp.height = height
                            iv_screenshots.layoutParams = lp
                            iv_screenshots.clipBorder = RectF(0f, 0f, widthIv.toFloat(), height.toFloat())
                            val rlLp = rl_xsp.layoutParams
                            rlLp.height = height + ViewUtils.getDp(R.dimen.dp_55)
                            rl_xsp.layoutParams = rlLp
                        }
                        super.setResource(resource)
                        //reScrollLayout()
                    }
                })
            }
        }
    }

    private fun reScrollLayout() {
        view_space.post {
            var dp11 = ViewUtils.getDp(R.dimen.dp_11)
            //var dp42 = ViewUtils.getDp(R.dimen.dp_42)
            var parentHeight = (view_space.parent as ViewGroup).measuredHeight
            var bottom = parentHeight - scroll_view.bottom
            Log.e("TAG", "reScrollLayout: $bottom  $dp11")
            if (bottom > dp11) {
                var half = (bottom + dp11) / 2
                var layoutParams = view_space.layoutParams
                layoutParams.height = half
                view_space.layoutParams = layoutParams
            }
        }
    }
}
