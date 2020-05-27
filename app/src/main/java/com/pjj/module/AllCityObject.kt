package com.pjj.module

import com.google.gson.JsonArray
import com.pjj.utils.JsonUtils

/**
 * Created by XinHeng on 2019/04/02.
 * describe：
 */
class AllCityObject(json: String) : ResultBean() {
    var jsonArray: JsonArray? = null

    init {
        var jsonObject = JsonUtils.toJsonObject(json)
        flag = jsonObject.get("flag").asString
        msg = jsonObject.get("msg").asString
        if (isSuccess) {
            this.jsonArray = jsonObject.get("data").asJsonArray
        }
    }
}
