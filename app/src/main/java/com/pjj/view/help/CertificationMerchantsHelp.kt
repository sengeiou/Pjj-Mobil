package com.pjj.view.help

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.pjj.PjjApplication
import com.pjj.R
import com.pjj.module.CertificationResultBean
import com.pjj.module.parameters.IdentityInf
import com.pjj.utils.Log
import com.pjj.utils.TextUtils
import com.pjj.utils.ViewUtils
import com.pjj.view.dialog.CertificationPopuWindow

/**
 * Created by XinHeng on 2019/01/09.
 * describe：商家认证
 */
class CertificationMerchantsHelp(private var activity: Activity, parent: ViewGroup, var identityInf: IdentityInf = IdentityInf()) : CertificationParentHelp() {
    private var tv_person_type: TextView
    private var tv_company: EditText
    private var tv_job: TextView
    private var tv_name: EditText
    private var tv_id: EditText
    private var tv_company_no: EditText

    private var tvAgentName: EditText
    private var tv_agent_id: EditText
    private var front: ImageView
    private var back: ImageView
//    private var holdIdCard: ImageView
    private var license: ImageView
    //private var holdLicense: ImageView
    private var agentFront: ImageView
    private var agentBack: ImageView
    private var iv_person_type: ImageView
    private var iv_job_select: ImageView

    private var front_iv: ImageView
    private var back_iv: ImageView
    private var agentFront_iv: ImageView
    private var agentBack_iv: ImageView
    //private var holdIdCard_iv: ImageView
    private var license_iv: ImageView
    //private var holdLicense_iv: ImageView

    private val personTypePopupWindow: CertificationPopuWindow by lazy {
        CertificationPopuWindow(activity as Context).apply {
            updateTextContent("法人", "代理人")
            setDefault(1)
            onCertificationListener = object : CertificationPopuWindow.OnCertificationListener {
                override fun textClick(index: Int) {
                    //if(index==1){//法人
                    selectType(index == 1)
                }
            }
        }
    }

    init {
        view = LayoutInflater.from(parent.context).inflate(R.layout.layout_certification_merchants, parent, false)
        parent.addView(view)

        front = view.findViewById(R.id.iv_id_front_example)
        back = view.findViewById(R.id.iv_id_back_example)
        //holdIdCard = view.findViewById(R.id.iv_agent_hold_id_card_example)
        license = view.findViewById(R.id.iv_business_license_example)
        //holdLicense = view.findViewById(R.id.iv_person_business_license_example)
        agentFront = view.findViewById(R.id.iv_agent_id_front_example)
        agentBack = view.findViewById(R.id.iv_agent_id_back_example)
        iv_person_type = view.findViewById(R.id.iv_person_type)
        iv_job_select = view.findViewById(R.id.iv_job_select)

        front_iv = view.findViewById(R.id.iv_id_front)
        back_iv = view.findViewById(R.id.iv_id_back)
        agentFront_iv = view.findViewById(R.id.iv_agent_id_front)
        agentBack_iv = view.findViewById(R.id.iv_agent_id_back)
        //holdIdCard_iv = view.findViewById(R.id.iv_agent_hold_id_card)
        license_iv = view.findViewById(R.id.iv_business_license)
        //holdLicense_iv = view.findViewById(R.id.iv_person_business_license)

        tv_person_type = view.findViewById(R.id.tv_person_type)
        tv_company = view.findViewById(R.id.tv_company)
        tv_job = view.findViewById(R.id.tv_job)
        tv_name = view.findViewById(R.id.tv_name)
        tv_id = view.findViewById(R.id.tv_id)
        tv_company_no = view.findViewById(R.id.tv_company_no)
        tvAgentName = view.findViewById(R.id.tv_agent_name)
        tv_agent_id = view.findViewById(R.id.tv_agent_id)

        front_iv.setOnClickListener(onClickListener)
        back_iv.setOnClickListener(onClickListener)
        //holdIdCard_iv.setOnClickListener(onClickListener)
        view.findViewById<View>(R.id.rl_id_front).setOnClickListener(onClickListener)
        view.findViewById<View>(R.id.rl_id_back).setOnClickListener(onClickListener)
        //view.findViewById<View>(R.id.rl_agent_hold_id_card).setOnClickListener(onClickListener)

        agentFront_iv.setOnClickListener(onClickListener)
        agentBack_iv.setOnClickListener(onClickListener)
        view.findViewById<View>(R.id.rl_agent_id_front).setOnClickListener(onClickListener)
        view.findViewById<View>(R.id.rl_agent_id_back).setOnClickListener(onClickListener)

        license_iv.setOnClickListener(onClickListener)
        //holdLicense_iv.setOnClickListener(onClickListener)
        view.findViewById<View>(R.id.rl_business_license).setOnClickListener(onClickListener)
        //view.findViewById<View>(R.id.rl_person_business_license).setOnClickListener(onClickListener)

        iv_person_type.setOnClickListener(onClickListener)
        tv_job.setOnClickListener(onClickListener)
        selectType(true)
        iv_person_type.setColorFilter(ViewUtils.getColor(R.color.color_d5d5d5))
        iv_job_select.setColorFilter(ViewUtils.getColor(R.color.color_d5d5d5))

        tv_name.filters = getFilters()
        tvAgentName.filters = getFilters()
    }

    fun visibility(tag: Int) {
        view.visibility = tag
    }

    private fun selectType(faren: Boolean = true) {
        var tag: Int
        if (faren) {
            identityInf.authType = "2"
            tag = View.GONE
            tv_person_type.text = "法人"
        } else {//代理人
            identityInf.authType = "3"
            tag = View.VISIBLE
            tv_person_type.text = "代理人"
        }
        updatePersonType(tag)
    }

    @SuppressLint("SetTextI18n")
    private fun updatePersonType(showOrHidden: Int) {
        //代理人姓名
        view.findViewById<View>(R.id.tv_agent_name_text).visibility = showOrHidden
        view.findViewById<View>(R.id.tv_agent_name).visibility = showOrHidden
        view.findViewById<View>(R.id.line_agent_name).visibility = showOrHidden
        //代理人身份证
        view.findViewById<View>(R.id.tv_agent_id_text).visibility = showOrHidden
        view.findViewById<View>(R.id.tv_agent_id).visibility = showOrHidden
        view.findViewById<View>(R.id.line_agent_id).visibility = showOrHidden
        //代理人 图
        view.findViewById<View>(R.id.tv_agent).visibility = showOrHidden
        view.findViewById<View>(R.id.line_id_back).visibility = showOrHidden
        //代理人 身份证正面
        view.findViewById<View>(R.id.iv_agent_id_front_example).visibility = showOrHidden
        view.findViewById<View>(R.id.tv_agent_id_front_text).visibility = showOrHidden
        agentFront_iv.visibility = showOrHidden
        view.findViewById<View>(R.id.rl_agent_id_front).visibility = showOrHidden
        //代理人 身份证反面
        view.findViewById<View>(R.id.iv_agent_id_back_example).visibility = showOrHidden
        view.findViewById<View>(R.id.tv_agent_id_back_text).visibility = showOrHidden
        view.findViewById<View>(R.id.iv_agent_id_back).visibility = showOrHidden
        view.findViewById<View>(R.id.rl_agent_id_back).visibility = showOrHidden
        //手持身份证
       // view.findViewById<TextView>(R.id.tv_agent_hold_id_card_text).text = "手持身份证照片示例(" + tv_person_type.text.toString() + ")"
       // view.findViewById<TextView>(R.id.tv_person_business_license_text).text = "手持营业执照照片示例(" + tv_person_type.text.toString() + ")"
    }

    fun centerShow(bean: CertificationResultBean.UserBusinessAuthDetailsBean) {
        centerTag = true
        view.findViewById<View>(R.id.iv_person_type).isEnabled = false
        tv_job.isEnabled = false
        tv_name.isEnabled = false
        tv_id.isEnabled = false
        tv_company.isEnabled = false
        tv_company_no.isEnabled = false

        tv_agent_id.isEnabled = false
        tvAgentName.isEnabled = false


        //1个人 2法人 3代理人
        var authType = bean.authType
        var faren = authType == "2"
        selectType(faren)
        setText(tv_job, bean.professionName, "")

        tv_company.setText(bean.companyName)
        tv_name.setText(bean.name)
        tv_company_no.setText(bean.companyNo)
        tv_id.setText(bean.idcart)


        Glide.with(activity).load(PjjApplication.filePath + bean.idFaceFile).into(front)
        Glide.with(activity).load(PjjApplication.filePath + bean.idBackFile).into(back)
        view.findViewById<View>(R.id.iv_id_front).visibility = View.GONE
        view.findViewById<View>(R.id.rl_id_front).visibility = View.GONE
        reSetMarginStartLayout(front)

        view.findViewById<View>(R.id.iv_id_back).visibility = View.GONE
        view.findViewById<View>(R.id.rl_id_back).visibility = View.GONE
        reSetMarginStartLayout(back)
        if (!faren) {
            tvAgentName.setText(bean.proxName)
            tv_agent_id.setText(bean.proxidcart)
            Log.e("TAG", "proxName=${bean.proxName} proxidcart=${bean.proxidcart}")
            Glide.with(activity).load(PjjApplication.filePath + bean.proxidFaceFile).into(agentFront)
            Glide.with(activity).load(PjjApplication.filePath + bean.proxidBackFile).into(agentBack)
           // Glide.with(activity).load(PjjApplication.filePath + bean.proxidHoldFile).into(holdIdCard)
            view.findViewById<View>(R.id.iv_agent_id_front).visibility = View.GONE
            view.findViewById<View>(R.id.rl_agent_id_front).visibility = View.GONE
            view.findViewById<View>(R.id.iv_agent_id_back).visibility = View.GONE
            view.findViewById<View>(R.id.rl_agent_id_back).visibility = View.GONE
            reSetMarginStartLayout(agentFront)
            reSetMarginStartLayout(agentBack)
        } else {
            //Glide.with(activity).load(PjjApplication.filePath + bean.idHoldFile).into(holdIdCard)
        }
        view.findViewById<View>(R.id.iv_agent_hold_id_card).visibility = View.GONE
        view.findViewById<View>(R.id.rl_agent_hold_id_card).visibility = View.GONE
        //reSetMarginStartLayout(holdIdCard)

        Glide.with(activity).load(PjjApplication.filePath + bean.businessLicence).into(license)
        //Glide.with(activity).load(PjjApplication.filePath + bean.businessLicenceHold).into(holdLicense)

        view.findViewById<View>(R.id.iv_business_license).visibility = View.GONE
        view.findViewById<View>(R.id.rl_business_license).visibility = View.GONE
        reSetMarginStartLayout(license)

        //view.findViewById<View>(R.id.iv_person_business_license).visibility = View.GONE
        //view.findViewById<View>(R.id.rl_person_business_license).visibility = View.GONE
        //reSetMarginStartLayout(holdLicense)
    }

    fun clearContent() {
        if (!centerTag) {
            return
        }
        centerTag = false
        view.findViewById<View>(R.id.iv_person_type).isEnabled = true
        tv_job.isEnabled = true
        tv_name.isEnabled = true
        tv_id.isEnabled = true
        tv_company.isEnabled = true
        tv_company_no.isEnabled = true

        tv_agent_id.isEnabled = true
        tvAgentName.isEnabled = true

        //默认法人
        selectType(true)
        tv_company.setText("")
        tv_name.setText("")
        tv_id.setText("")
        tv_company_no.setText("")
        //tvAgentName.setText("")
        tv_agent_id.setText("")
        setText(tv_job, null, "请选择行业类型")
        identityInf.professionType = null
        reSetMarginStartLayout(front, noCenterMarLeft)
        reSetMarginStartLayout(back, noCenterMarLeft)
        front.setImageResource(R.mipmap.id_card_01)
        back.setImageResource(R.mipmap.id_card_02)
       // reSetMarginStartLayout(holdIdCard, noCenterMarLeft)
       // holdIdCard.setImageResource(R.mipmap.id_card_03)

        reSetMarginStartLayout(license, noCenterMarLeft)
        license.setImageResource(R.mipmap.business_license)
       // reSetMarginStartLayout(holdLicense, noCenterMarLeft)
        //holdLicense.setImageResource(R.mipmap.person_business_license)

        reSetMarginStartLayout(agentFront, noCenterMarLeft)
        reSetMarginStartLayout(agentBack, noCenterMarLeft)
        agentFront.setImageResource(R.mipmap.id_card_01)
        agentBack.setImageResource(R.mipmap.id_card_02)
        //view.findViewById<View>(R.id.iv_person_business_license).visibility = View.VISIBLE
        //view.findViewById<View>(R.id.rl_person_business_license).visibility = View.VISIBLE
        view.findViewById<View>(R.id.iv_business_license).visibility = View.VISIBLE
        view.findViewById<View>(R.id.rl_business_license).visibility = View.VISIBLE
        //view.findViewById<View>(R.id.iv_agent_hold_id_card).visibility = View.VISIBLE
        //view.findViewById<View>(R.id.rl_agent_hold_id_card).visibility = View.VISIBLE
        //view.findViewById<View>(R.id.iv_agent_id_front).visibility = View.VISIBLE
        //view.findViewById<View>(R.id.rl_agent_id_front).visibility = View.VISIBLE
        view.findViewById<View>(R.id.iv_id_back).visibility = View.VISIBLE
        view.findViewById<View>(R.id.rl_id_back).visibility = View.VISIBLE
        view.findViewById<View>(R.id.iv_id_front).visibility = View.VISIBLE
        view.findViewById<View>(R.id.rl_id_front).visibility = View.VISIBLE

        tv_company.isFocusable = true
        tv_company.isFocusableInTouchMode = true
        tv_company.requestFocus()
    }

    fun selectProfessionResult(text: String) {
        setText(tv_job, text, "请选择行业类型")
    }

    fun fillInf() {
        identityInf.name = tv_name.text.toString()
        identityInf.idcart = tv_id.text.toString()
        if (identityInf.authType == "3") {
            identityInf.proxName = tvAgentName.text.toString()
            identityInf.proxidcart = tv_agent_id.text.toString()
        }
        identityInf.companyNo = tv_company_no.text.toString()
        identityInf.companyName = tv_company.text.toString()
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
            agentFront_iv -> {
                identityInf.proxidFaceFile1 = path
                view.findViewById<View>(R.id.rl_agent_id_front).visibility = View.GONE
            }
            agentBack_iv -> {
                identityInf.proxidBackFile1 = path
                view.findViewById<View>(R.id.rl_agent_id_back).visibility = View.GONE
            }
           /* holdIdCard_iv -> {
                if (identityInf.authType == "2") {
                    identityInf.idHold1 = path
                } else {
                    identityInf.proxidHoldFile1 = path
                }
                view.findViewById<View>(R.id.rl_agent_hold_id_card).visibility = View.GONE
            }*/
            license_iv -> {
                identityInf.businessLicence1 = path
                view.findViewById<View>(R.id.rl_business_license).visibility = View.GONE
            }
            /*holdLicense_iv -> {
                identityInf.businessLicenceHold1 = path
                view.findViewById<View>(R.id.rl_person_business_license).visibility = View.GONE
            }*/
        }
        Glide.with(activity).load(path).into(nowImageView!!)
    }

    override fun onClick(viewId: Int) {
        when (viewId) {
            R.id.tv_job -> {
                if (onCertificationHelpListenr is OnMerchantsListener) {
                    (onCertificationHelpListenr as OnMerchantsListener).startSelectProfessionType()
                }
            }
            R.id.iv_person_type -> {
                onCertificationHelpListenr?.hiddenInputMethod()
                personTypePopupWindow.showAsDropDown(iv_person_type, 0, -iv_person_type.measuredHeight * 2 + iv_person_type.measuredHeight / 2)
            }
            R.id.rl_id_front, R.id.iv_id_front -> startSelectImage(front_iv)
            R.id.iv_id_back, R.id.rl_id_back -> startSelectImage(back_iv)
            R.id.iv_agent_id_front, R.id.rl_agent_id_front -> startSelectImage(agentFront_iv)
            R.id.iv_agent_id_back, R.id.rl_agent_id_back -> startSelectImage(agentBack_iv)
            //R.id.iv_agent_hold_id_card, R.id.rl_agent_hold_id_card -> startSelectImage(holdIdCard_iv)
            R.id.iv_business_license, R.id.rl_business_license -> startSelectImage(license_iv)
           // R.id.iv_person_business_license, R.id.rl_person_business_license -> startSelectImage(holdLicense_iv)
        }
    }

    interface OnMerchantsListener : OnCertificationHelpListenr {
        fun startSelectProfessionType()
    }
}
