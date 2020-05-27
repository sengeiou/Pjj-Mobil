package com.pjj.view.activity

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import com.pjj.R
import com.pjj.present.BasePresent
import com.pjj.receiver.JPushReceiver

class EmptyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (intent.flags and Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT != 0) {
            finish()
            return
        }
        val stringExtra = intent.getStringExtra(JPushReceiver.KEY_TYPE)
        stringExtra?.let {
            startActivity(Intent(this, TestActivity::class.java).putExtra(JPushReceiver.KEY_TYPE, it))
            finish()
        }
        //startActivity(Intent(this, SplashActivity::class.java), ActivityOptionsCompat.makeCustomAnimation(this, R.anim.apha_0_1, 0).toBundle())
        //overridePendingTransition(0, R.anim.apha_0_1)
        startActivity(Intent(this, SplashActivity::class.java))
        finish()
        setContentView(R.layout.activity_empty)
    }

/*
    private fun openFullScreenModel() {
        if (Build.VERSION.SDK_INT >= 28) {
            //mActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);
            window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
            var lp = window.attributes
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
            window.attributes = lp
            var decorView = getWindow().getDecorView();
            var systemUiVisibility = decorView.getSystemUiVisibility();
            var flags = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
            systemUiVisibility = systemUiVisibility or flags
            window.decorView.systemUiVisibility = systemUiVisibility
        }
    }*/
}
