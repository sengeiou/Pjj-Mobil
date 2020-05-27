package com.pjj.module

/**
 * Created by XinHeng on 2019/03/08.
 * describe：view的起始坐标 与宽高比
 */
class ViewSizeBean(var x: Int, var y: Int, var width: Int, var height: Int) {
    //1 image 2 video 3便民
    var type = 1
        get() {
            if (field == 0) {
                field = 1
            }
            return field
        }
    var filePath: String? = null
        get() {
            if (field == null) {
                return fileName
            }
            return field
        }
    var fileName: String? = null
    var randomFlag: String? = null
        get() {
            if (field == null) {
                field = "0"
            }
            return field
        }
    var file_id: String? = null
    override fun toString(): String {
        return "SpellDataBean(x=$x, y=$y, width=$width, height=$height)"
    }
    fun clone():ViewSizeBean{
        return ViewSizeBean(x,y,width,height).apply {
            randomFlag=this@ViewSizeBean.randomFlag
            filePath=this@ViewSizeBean.filePath
            type=this@ViewSizeBean.type
        }
    }
}