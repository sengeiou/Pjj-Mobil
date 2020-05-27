package com.pjj.view.activity

import android.os.Bundle
import android.view.Gravity
import android.widget.Toast

import com.pjj.R
import com.pjj.contract.OpinionContract
import com.pjj.present.OpinionPresent
import com.pjj.utils.TextUtils
import kotlinx.android.synthetic.main.activity_opinion.*

/**
 * Create by xinheng on 2019/01/19 17:16。
 * describe：意见反馈
 */
class OpinionActivity : BaseActivity<OpinionPresent>(), OpinionContract.View {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opinion)
        setTitle("意见反馈")
        mPresent = OpinionPresent(this)
        TextUtils.banFaceForEditText(et_opinion)
        tv_submit.setOnClickListener {
            var msg = this@OpinionActivity.et_opinion.text.toString()
            if (TextUtils.isEmpty(msg)) {
                showNotice("留言内容不能为空")
                return@setOnClickListener
            }
            mPresent?.loadOpinionTask(msg)
        }
    }

    override fun loadSuccess() {
        /*showNotice("提交成功")
        Handler().postDelayed({
            cancelWaiteStatue()
            finish()
        }, 1000)*/
        var makeText = Toast.makeText(this, "提交成功", Toast.LENGTH_SHORT)
        makeText.setGravity(Gravity.CENTER, 0, 0)
        makeText.show()
        finish()
    }
}
