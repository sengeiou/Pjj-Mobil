package com.pjj.module.xspad

import com.pjj.module.SpeedDataBean

/**
 * Created by XinHeng on 2019/03/21.
 * describe：拼屏所需数据
 */
class SpeedScreenData {
    /**
     * 拼屏图片素材组合
     * 图片主键&randomFlag    0无  1固定坐标&图片宽&图片高度,",(图片信息已","分隔)
     */
    var file_id: String? = null
    /**
     * 模板唯一标识
     */
    var identificationId: String? = null
    /**
     * 拼屏数
     */
    var pieceNum = 0
    /**
     * 预览页
     */
    var prePic: String? = null
    /**
     * 素材组成的新模板
     * 或预览
     */
    var clone: SpeedDataBean? = null
    var templateData: SpeedDataBean? = null
    /**
     * 模板原始数据
     */
    var originalTemplateData: SpeedDataBean? = null
}