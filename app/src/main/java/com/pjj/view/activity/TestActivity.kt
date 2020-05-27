package com.pjj.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ScrollView
import android.widget.TextView
import com.pjj.R
import com.pjj.present.BasePresent
import com.pjj.receiver.JPushReceiver
import com.pjj.utils.SharedUtils
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : BaseActivity<BasePresent<*>>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var type = intent.getStringExtra(JPushReceiver.KEY_TYPE)
        if (!TextUtils.isEmpty(type)) {
            if (SharedUtils.isEffiect()) {
                var intent = Intent(this, MainActivity::class.java)
                if (type == "0001") {
                    //"noticeType":"0001"  //订单时间到达
                    //"noticeType":"0002" //自定义群发通知
                    intent.putExtra("index", 2)
                }
                startActivity(intent)
                finish()
            } else {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
        }
        setContentView(R.layout.activity_test)
        setTitle("Test")
        //adjustResize
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        testEditText()
    }

    private fun testEditText() {//1794
        var height = resources.displayMetrics.heightPixels - ViewUtils.getDp(R.dimen.dp_42) * 3
        val lp = recycleView.layoutParams
        lp.height = height
        recycleView.layoutParams = lp
        recycleView.adapter = MyAdapter()
        recycleView.layoutManager = LinearLayoutManager(this)
        /*et.setOnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                this@TestActivity.et.postDelayed({
                    //this@TestActivity.scrollView.isEnabled=false
                    //scrollView.focusable
                    //this@TestActivity.scrollView.fullScroll(ScrollView.FOCUS_DOWN)
                },1200)
            } else {

            }
        }*/
    }

    private class MyAdapter : RecyclerView.Adapter<Myholder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Myholder {
            return Myholder(TextView(parent.context).apply {
                setPadding(20, 10, 20, 0)
            })
        }

        override fun getItemCount(): Int {
            return 200
        }

        @SuppressLint("SetTextI18n")
        override fun onBindViewHolder(holder: Myholder, position: Int) {
            holder.tv.text = "this is $position "
        }

    }

    private class Myholder(view: TextView) : RecyclerView.ViewHolder(view) {
        var tv = view
    }
}
