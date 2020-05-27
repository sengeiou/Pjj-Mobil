package com.pjj.module

/**
 * Created by XinHeng on 2019/03/08.
 * describe：拼接数据
 */
class SpeedDataBean {
    //1080*1800
    var viewSizeBeanList: ArrayList<ViewSizeBean>? = null
    var proportionX = 1
    var proportionY = 3
    var size = 1
    /**
     * 模板类型唯一标识
     */
    var IdentificationId: String? = null
    var templetId: String? = null
    /**
     * 缩略图
     */
    var thumbnail: String? = null

    override fun toString(): String {
        return "SpeedDataBean(viewSizeBeanList=$viewSizeBeanList, proportionX=$proportionX, proportionY=$proportionY, size=$size)"
    }

    fun clone(list: ArrayList<ViewSizeBean>): SpeedDataBean {
        return SpeedDataBean().apply {
            proportionX = this@SpeedDataBean.proportionX
            proportionY = this@SpeedDataBean.proportionY
            size = this@SpeedDataBean.size
            IdentificationId = this@SpeedDataBean.IdentificationId
            viewSizeBeanList = ArrayList()
            this@SpeedDataBean.viewSizeBeanList?.forEach {
                if (!list.contains(it)) {
                    this.viewSizeBeanList!!.add(it.clone().apply {
                        filePath = null
                    })
                } else {
                    this.viewSizeBeanList!!.add(it)
                }
            }
        }

    }

    fun clone(): SpeedDataBean {
        return SpeedDataBean().apply {
            proportionX = this@SpeedDataBean.proportionX
            proportionY = this@SpeedDataBean.proportionY
            size = this@SpeedDataBean.size
            IdentificationId = this@SpeedDataBean.IdentificationId
            viewSizeBeanList = ArrayList<ViewSizeBean>().apply {
                this@SpeedDataBean.viewSizeBeanList?.forEach {
                    this.add(it.clone())
                }
            }
        }
    }
}