package com.pjj.view.help

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.module.CertificationResultBean
import com.pjj.module.parameters.IdentityInf

/**
 * Created by XinHeng on 2019/01/09.
 * describe：
 */
class CertificationPersonHelp(private var activity: Activity, parent: ViewGroup, var identityInf: IdentityInf) : CertificationParentHelp() {
    private var tv_name: EditText
    private var tv_id: EditText

    private var front: ImageView
    private var back: ImageView
    //private var holdIdCard: ImageView

    private var front_iv: ImageView
    private var back_iv: ImageView
    //private var holdIdCard_iv: ImageView
    init {
        view = LayoutInflater.from(parent.context).inflate(R.layout.layout_certification_person, parent, false)
        tv_name = view.findViewById(R.id.tv_name)
        tv_id = view.findViewById(R.id.tv_id)

        front = view.findViewById(R.id.iv_id_front_example)
        back = view.findViewById(R.id.iv_id_back_example)
        //oldIdCard = view.findViewById(R.id.iv_person_business_license_example)

        front_iv = view.findViewById(R.id.iv_id_front)
        back_iv = view.findViewById(R.id.iv_id_back)
        //holdIdCard_iv = view.findViewById(R.id.iv_person_business_license)

        parent.addView(view)
        front_iv.setOnClickListener(onClickListener)
        back_iv.setOnClickListener(onClickListener)
        //holdIdCard_iv.setOnClickListener(onClickListener)
        view.findViewById<View>(R.id.rl_id_front).setOnClickListener(onClickListener)
        view.findViewById<View>(R.id.rl_id_back).setOnClickListener(onClickListener)
        //view.findViewById<View>(R.id.rl_person_business_license).setOnClickListener(onClickListener)

        tv_name.filters = getFilters()
    }

    fun centerShow(bean: CertificationResultBean.UserAuthDetailsBean) {
        centerTag = true
        tv_name.setText(bean.name)
        tv_id.setText(bean.idcart)
        tv_name.isEnabled = false
        tv_id.isEnabled = false
        Glide.with(activity).load(PjjApplication.filePath + bean.idFaceFile).into(front)
        Glide.with(activity).load(PjjApplication.filePath + bean.idBackFile).into(back)
        //Glide.with(activity).load(PjjApplication.filePath + bean.idHoldFile).into(holdIdCard)
        view.findViewById<View>(R.id.iv_id_front).visibility = View.GONE
        view.findViewById<View>(R.id.rl_id_front).visibility = View.GONE
        reSetMarginStartLayout(front)
        view.findViewById<View>(R.id.iv_id_back).visibility = View.GONE
        view.findViewById<View>(R.id.rl_id_back).visibility = View.GONE
        reSetMarginStartLayout(back)
        //view.findViewById<View>(R.id.iv_person_business_license).visibility = View.GONE
        //view.findViewById<View>(R.id.rl_person_business_license).visibility = View.GONE
//        reSetMarginStartLayout(holdIdCard)
    }

    fun clearContent() {
        if (!centerTag) {
            return
        }
        tv_name.isEnabled = true
        tv_id.isEnabled = true
        centerTag = false
        tv_name.setText("")
        tv_id.setText("")
        view.findViewById<View>(R.id.iv_id_front).visibility = View.VISIBLE
        view.findViewById<View>(R.id.rl_id_front).visibility = View.VISIBLE
        view.findViewById<View>(R.id.iv_id_back).visibility = View.VISIBLE
        view.findViewById<View>(R.id.rl_id_back).visibility = View.VISIBLE
        //view.findViewById<View>(R.id.iv_person_business_license).visibility = View.VISIBLE
        //view.findViewById<View>(R.id.rl_person_business_license).visibility = View.VISIBLE
        reSetMarginStartLayout(front, noCenterMarLeft)
        reSetMarginStartLayout(back, noCenterMarLeft)
        //reSetMarginStartLayout(holdIdCard, noCenterMarLeft)
        front.setImageResource(R.mipmap.id_card_01)
        back.setImageResource(R.mipmap.id_card_02)
        //holdIdCard.setImageResource(R.mipmap.id_card_03)

        tv_name.isFocusable = true
        tv_name.isFocusableInTouchMode = true
        tv_name.requestFocus()
    }

    fun fillInf() {
        identityInf.name = tv_name.text.toString()
        identityInf.idcart = tv_id.text.toString()
    }

    public override fun selectImageResult(path: String) {
        super.selectImageResult(path)
        when (nowImageView) {
            front_iv -> {
                identityInf.idFace1 = path
                view.findViewById<View>(R.id.rl_id_front).visibility = View.GONE
            }
            back_iv -> {
                identityInf.idBack1 = path
                view.findViewById<View>(R.id.rl_id_back).visibility = View.GONE
            }
           /* holdIdCard_iv -> {
                identityInf.idHold1 = path
                view.findViewById<View>(R.id.rl_person_business_license).visibility = View.GONE
            }*/
        }
        Glide.with(activity).load(path).into(nowImageView!!)
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.rl_id_front, R.id.iv_id_front -> startSelectImage(front_iv)
            R.id.iv_id_back, R.id.rl_id_back -> startSelectImage(back_iv)
            //R.id.iv_person_business_license, R.id.rl_person_business_license -> startSelectImage(holdIdCard_iv)
        }
    }
}