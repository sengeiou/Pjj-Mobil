package com.pjj.present

import com.pjj.BuildConfig
import com.pjj.contract.CertificationContract
import com.pjj.intent.AliFile
import com.pjj.intent.RetrofitService
import com.pjj.module.CertificationResultBean
import com.pjj.module.ResultBean
import com.pjj.module.parameters.IdentityInf
import com.pjj.utils.Log

/**
 * Create by xinheng on 2018/12/10 18:31。
 * describe：P层
 */
class CertificationPresent(view: CertificationContract.View) : BasePresent<CertificationContract.View>(view, CertificationContract.View::class.java), CertificationContract.Present {
    /**
     * 上传
     * @param identityInf
     * @param tag 0 个人 1个人增强 2 法人 3 法人增强
     */
    override fun loadUploadInformation(identityInf: IdentityInf, tag: Int) {
        mView.showWaiteStatue()
        /*var array: Array<String> = when (tag) {
            3 -> arrayOf(identityInf.idFace1, identityInf.idBack1, identityInf.businessLicenceHold1, identityInf.businessLicence1)
            2 -> arrayOf(identityInf.idFace1, identityInf.idBack1, identityInf.businessLicence1)
            1 -> arrayOf(identityInf.idFace1, identityInf.idBack1, identityInf.idHold1)
            else -> arrayOf(identityInf.idFace1, identityInf.idBack1)
        }*/
        val array: Array<String> = when (tag) {
            3 -> arrayOf(identityInf.idFace1, identityInf.idBack1, identityInf.proxidFaceFile1, identityInf.proxidBackFile1,  identityInf.businessLicence1)
            2 -> arrayOf(identityInf.idFace1, identityInf.idBack1, identityInf.businessLicence1)
            else -> arrayOf(identityInf.idFace1, identityInf.idBack1)
        }
        if (BuildConfig.DEBUG) {
            array.forEach {
                Log.e("TAG", "图片array: $it")
            }
        }
        var sameImage = false
        array.forEachIndexed { index, item ->
            for (i in 0 until array.size) {
                if (index != i && item == array[i]) {
                    sameImage = true
                    return@forEachIndexed
                }
            }
        }
        if (sameImage) {
            mView.cancelWaiteStatue()
            mView.uploadResult(false, "存在相同的图片,\n请检查更换")
            return
        }
        AliFile.getInstance().uploadFile(object : AliFile.UploadResult() {
            override fun successMap(map: MutableMap<String, String>) {
                identityInf.idFace = map[identityInf.idFace1]
                identityInf.idBack = map[identityInf.idBack1]
                when (tag) {
                    1 -> identityInf.idHold = map[identityInf.idHold1]
                    2 -> {
                        identityInf.idHold = map[identityInf.idHold1]
                        identityInf.businessLicenceHold = map[identityInf.businessLicenceHold1]
                        identityInf.businessLicence = map[identityInf.businessLicence1]
                    }
                    3 -> {
                        identityInf.proxidFaceFile = map[identityInf.proxidFaceFile1]
                        identityInf.proxidBackFile = map[identityInf.proxidBackFile1]
                        identityInf.proxidHoldFile = map[identityInf.proxidHoldFile1]

                        identityInf.businessLicence = map[identityInf.businessLicence1]
                        identityInf.businessLicenceHold = map[identityInf.businessLicenceHold1]
                    }
                }
                uploadNew_(identityInf)
            }

            override fun fail(error: String) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.uploadResult(false, error)
            }
        }, array)
    }

    private fun uploadNew_(identityInf: IdentityInf) {
        Log.e("TAG", "uploadNew_ 上传成功，访问服务器")
        retrofitService.uploadNewIdentityImage(identityInf, object : RetrofitService.CallbackClassResult<ResultBean>(ResultBean::class.java) {
            override fun successResult(t: ResultBean) {
                mView.cancelWaiteStatue()
                mView.uploadResult(true, null)
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.uploadResult(false, error)
            }
        })
    }

    override fun loadCertificationFailTask(userId: String) {
        mView.showWaiteStatue()
        retrofitService.loadCertificationFailTask(userId, object : RetrofitService.CallbackClassResult<CertificationResultBean>(CertificationResultBean::class.java) {
            override fun successResult(t: CertificationResultBean) {
                mView.updateHasSelectedImage(t)
                mView.cancelWaiteStatue()
            }

            override fun fail(error: String?) {
                super.fail(error)
                mView.cancelWaiteStatue()
                mView.showNotice(error)
            }
        })
    }
}
