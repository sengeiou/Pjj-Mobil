package com.pjj.intent

import com.pjj.BuildConfig
import com.pjj.utils.JsonUtils
import com.pjj.utils.TextUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

/**
 * Created by XinHeng on 2019/04/09.
 * describe：
 */
class IntegralRetrofitService private constructor() {
    /*private var INSTANCE: IntegralRetrofitService? = null

    val instance: IntegralRetrofitService
        get() {
            if (null == INSTANCE) {
                synchronized(IntegralRetrofitService::class.java) {
                    if (null == INSTANCE) {
                        INSTANCE = IntegralRetrofitService()
                    }
                }
            }
            return INSTANCE!!
        }
    */
    private lateinit var integralApiService: IntegralApiService

    companion object {
        val instance: IntegralRetrofitService by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            IntegralRetrofitService()
        }
    }

    fun initRetrofit(appendUrlParamInterceptor: Interceptor) {
        val retrofit = Retrofit.Builder()
                .baseUrl(if (BuildConfig.APP_TYPE) "http://39.98.75.37:8080/" else "http://47.92.254.65:8088/") //测试
                .client(OkHttpClient.Builder()
                        .addInterceptor(appendUrlParamInterceptor)
                        .connectTimeout(180, TimeUnit.SECONDS)
                        .readTimeout(120, TimeUnit.SECONDS)
                        .writeTimeout(120, TimeUnit.SECONDS)
                        .build())
                .build()
        integralApiService = retrofit.create(IntegralApiService::class.java)
    }

    fun getAppIntergralGoodsList(pageNo: Int, pageNum: Int, callback: Callback<ResponseBody>, integraGoodsTypeId: String? = null): Call<ResponseBody> {
        var s =
                if (null != integraGoodsTypeId && "-1" != integraGoodsTypeId)
                    "{\"pageNo\":\"$pageNo\",\"pageNum\":\"$pageNum\",\"integraGoodsTypeId\":\"$integraGoodsTypeId\"}"
                else
                    "{\"pageNo\":\"$pageNo\",\"pageNum\":\"$pageNum\"}"
        return integralApiService.getAppIntergralGoodsList(RetrofitService.getRequestBody(s)).apply {
            enqueue(callback)
        }
    }

    fun isUserIntegralBalance(IntegralQuantity: String, userId: String, callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.isUserIntegralBalance(RetrofitService.getRequestBody("{\"userId\":\"$userId\",\"IntegralQuantity\":\"$IntegralQuantity\"}")).apply {
            enqueue(callback)
        }
    }

    fun getReceivingAddressList(userId: String, callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.getReceivingAddressList(RetrofitService.getRequestBody("{\"userId\":\"$userId\"}")).apply {
            enqueue(callback)
        }
    }

    fun insertReceivingAddress(userId: String, areaCode: String, position: String, describe: String, postId: String, phone: String, name: String, isDefault: String, callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.insertReceivingAddress(RetrofitService.getRequestBody("{\"userId\":\"$userId\",\"areaCode\":\"$areaCode\",\"position\":\"$position\",\"describe\":\"$describe\",\"postId\":\"$postId\",\"phone\":\"$phone\",\"name\":\"$name\",\"isDefault\":\"$isDefault\"}")).apply {
            enqueue(callback)
        }
    }

    fun updateReceivingAddress(userId: String, addressId: String, areaCode: String, position: String, describe: String, postId: String, phone: String, name: String, isDefault: String, callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.updateReceivingAddress(RetrofitService.getRequestBody("{\"userId\":\"$userId\",\"addressId\":\"$addressId\",\"areaCode\":\"$areaCode\",\"position\":\"$position\",\"describe\":\"$describe\",\"postId\":\"$postId\",\"phone\":\"$phone\",\"name\":\"$name\",\"isDefault\":\"$isDefault\"}")).apply {
            enqueue(callback)
        }
    }

    fun deleteReceivingAddress(addressId: String, callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.deleteReceivingAddress(RetrofitService.getRequestBody("{\"addressId\":\"$addressId\"}")).apply {
            enqueue(callback)
        }
    }

    fun generateIntegralOrder(userId: String, IntegralQuantity: String, describe: String, integraGoodsId: String, position: String, address: String, postCost: String, phone: String, name: String, goodsPicture: String, callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.generateIntegralOrder(RetrofitService.getRequestBody("{\"userId\":\"$userId\",\"IntegralQuantity\":\"$IntegralQuantity\",\"integraGoodsId\":\"$integraGoodsId\",\"position\":\"$position\",\"describe\":\"$describe\",\"postCost\":\"$postCost\",\"address\":\"$address\",\"phone\":\"$phone\",\"name\":\"$name\",\"goodsPicture\":\"$goodsPicture\"}")).apply {
            enqueue(callback)
        }
    }

    fun getIntegralOrderList(userId: String, pageNo: Int, pageNum: Int, callback: Callback<ResponseBody>, status: String? = null): Call<ResponseBody> {
        var json = if (TextUtils.isEmpty(status)) "{\"userId\":\"$userId\",\"pageNo\":\"$pageNo\",\"pageNum\":\"$pageNum\"}" else "{\"userId\":\"$userId\",\"pageNo\":\"$pageNo\",\"pageNum\":\"$pageNum\",\"status\":\"$status\"}"
        return integralApiService.getIntegralOrderList(RetrofitService.getRequestBody(json)).apply {
            enqueue(callback)
        }
    }

    fun getIntegralRecordList(userId: String, pageNo: Int, pageNum: Int, callback: Callback<ResponseBody>, type: String? = null): Call<ResponseBody> {
        var json = if (TextUtils.isEmpty(type)) "{\"userId\":\"$userId\",\"pageNo\":\"$pageNo\",\"pageNum\":\"$pageNum\"}" else "{\"userId\":\"$userId\",\"pageNo\":\"$pageNo\",\"pageNum\":\"$pageNum\",\"type\":\"$type\"}"
        return integralApiService.getIntegralRecordList(RetrofitService.getRequestBody(json)).apply {
            enqueue(callback)
        }
    }

    fun getUserIntegralByUserId(userId: String, callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.getUserIntegralByUserId(RetrofitService.getRequestBody("{\"userId\":\"$userId\"}")).apply {
            enqueue(callback)
        }
    }

    /**
     * @param ruleType  金币规则类型  1兑换说明   2金币规则
     */
    fun getIntegralRule(ruleType: String, callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.getIntegralRule(RetrofitService.getRequestBody("{\"ruleType\":\"$ruleType\"}")).apply {
            enqueue(callback)
        }
    }

    fun getIntegralOrderByOrderId(orderId: String, callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.getIntegralOrderByOrderId(RetrofitService.getRequestBody("{\"orderId\":\"$orderId\"}")).apply {
            enqueue(callback)
        }
    }

    fun confirmReceivingGoods(orderId: String, callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.confirmReceivingGoods(RetrofitService.getRequestBody("{\"orderId\":\"$orderId\"}")).apply {
            enqueue(callback)
        }
    }

    fun goIntegralAlipay(orderId: String, callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.goIntegralAlipay(RetrofitService.getRequestBody("{\"orderId\":\"$orderId\"}")).apply {
            enqueue(callback)
        }
    }

    fun goIntegralWxpay(orderId: String, callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.goIntegralWxpay(RetrofitService.getRequestBody("{\"orderId\":\"$orderId\"}")).apply {
            enqueue(callback)
        }
    }

    fun getIntegralGoodsType(callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.getIntegralGoodsType(RetrofitService.getRequestBody("{}")).apply {
            enqueue(callback)
        }
    }

    fun getGoodsDetails(goodsId: String, callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.getGoodsDetails(RetrofitService.getRequestBody("{\"goodsId\":\"$goodsId\"}")).apply {
            enqueue(callback)
        }
    }

    fun getShoppingCart(userId: String, callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.getShoppingCart(RetrofitService.getRequestBody("{\"userId\":\"$userId\"}")).apply {
            enqueue(callback)
        }
    }

    fun addShoppingCart(userId: String, goodsId: String, specificId: String?, goodsNum: Int, callback: Callback<ResponseBody>): Call<ResponseBody> {
        //,"specificId":"$specificId"
        return integralApiService.addShoppingCart(RetrofitService.getRequestBody("{\"userId\":\"$userId\",\"goodsId\":\"$goodsId\",\"goodsNum\":\"$goodsNum\"}")).apply {
            enqueue(callback)
        }
    }

    fun changeShoppingCar(userId: String, shoppingCartId: String, goodsNum: Int, addTag: Boolean, callback: Callback<ResponseBody>): Call<ResponseBody> {
        //,"specificId":"$specificId"
        val key = if (addTag) "addNum" else "subNum"
        return integralApiService.updateShoppingCartGoodsNum(RetrofitService.getRequestBody("{\"userId\":\"$userId\",\"shoppingCartId\":\"$shoppingCartId\",\"$key\":\"$goodsNum\"}")).apply {
            enqueue(callback)
        }
    }

    fun updateShoppingCartGoodsNum(shoppingCartId: String, goodsNum: Int, callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.updateShoppingCartGoodsNum(RetrofitService.getRequestBody("{\"shoppingCartId\":\"$shoppingCartId\",\"goodsNum\":\"$goodsNum\"}")).apply {
            enqueue(callback)
        }
    }

    fun generateShoppingOrder(userId: String, goodsList: String, position: String, address: String, name: String, phone: String, callback: Callback<ResponseBody>, shoppingCar: String = "0"): Call<ResponseBody> {
        return integralApiService.generateShoppingOrder(RetrofitService.getRequestBody("{\"userId\":\"$userId\",\"goodsList\":\"$goodsList\",\"position\":\"$position\",\"address\":\"$address\",\"name\":\"$name\",\"phone\":\"$phone\",\"shoppingCar\":\"$shoppingCar\"}")).apply {
            enqueue(callback)
        }
    }

    fun getGoodsOrderList(status: String, userId: String, pageNo: Int, pageNum: Int, callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.getGoodsOrderList(RetrofitService.getRequestBody("{\"status\":\"$status\",\"userId\":\"$userId\",\"pageNo\":\"$pageNo\",\"pageNum\":\"$pageNum\"}")).apply {
            enqueue(callback)
        }
    }

    fun delShoppingCart(shoppingCartIds: String, callback: Callback<ResponseBody>): Call<ResponseBody> {
        /*val stringBuffer = StringBuffer()
        stringBuffer.append("[")
        shoppingCartIds.forEach {
            stringBuffer.append("{")
            stringBuffer.append("\"shoppingCartId\":\"")
            stringBuffer.append(it)
            stringBuffer.append("\"}")
            stringBuffer.append(",")
        }
        stringBuffer.deleteCharAt(stringBuffer.length - 1)
        stringBuffer.append("]")*/
        return integralApiService.delShoppingCart(RetrofitService.getRequestBody("{\"shoppingCartId\":\"$shoppingCartIds\"}")).apply {
            enqueue(callback)
        }
    }

    fun getGoodsByQuery(goods_category_id: String, pageNo: Int, pageNum: Int, isRecommend: String? = null, callback: Callback<ResponseBody>): Call<ResponseBody> {
        val json = when {
            goods_category_id == "-1" && isRecommend == null -> "{\"pageNo\":\"$pageNo\",\"pageNum\":\"$pageNum\"}"
            goods_category_id != "-1" && isRecommend == null -> "{\"goods_category_id\":\"$goods_category_id\",\"pageNo\":\"$pageNo\",\"pageNum\":\"$pageNum\"}"
            goods_category_id == "-1" && isRecommend != null -> "{\"pageNo\":\"$pageNo\",\"pageNum\":\"$pageNum\",\"isRecommend\":\"$isRecommend\"}"
            else -> "{\"goods_category_id\":\"$goods_category_id\",\"pageNo\":\"$pageNo\",\"pageNum\":\"$pageNum\",\"isRecommend\":\"$isRecommend\"}"
        }
        return integralApiService.getGoodsByQuery(RetrofitService.getRequestBody(json)).apply {
            enqueue(callback)
        }
    }

    fun getGoodsCategory(callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.getGoodsCategory(RetrofitService.getRequestBody("{}")).apply {
            enqueue(callback)
        }
    }

    fun getAdverBanner(callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.getAdverBanner(RetrofitService.getRequestBody("{}")).apply {
            enqueue(callback)
        }
    }

    fun getCancelOrderInfo(callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.getCancelOrderInfo(RetrofitService.getRequestBody("{}")).apply {
            enqueue(callback)
        }
    }

    fun confirmGoodsStock(json: String, callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.confirmGoodsStock(RetrofitService.getRequestBody("{\"goodsList\":\"$json\"}")).apply {
            enqueue(callback)
        }
    }

    fun goShoppingPay(goodOrderId: String, storeId: String?, payType: String, callback: Callback<ResponseBody>): Call<ResponseBody> {
        val json = JsonUtils.toJsonString(HashMap<String, String?>().apply {
            put("goodOrderId", goodOrderId)
            put("storeId", storeId)
            put("payType", payType)
        })
        return integralApiService.goShoppingPay(RetrofitService.getRequestBody(json)).apply {
            enqueue(callback)
        }
    }

    fun cancelShoppingOrder(goodOrderId: String, storeId: String, notes: String, callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.cancelShoppingOrder(RetrofitService.getRequestBody("{\"goodOrderId\":\"$goodOrderId\",\"storeId\":\"$storeId\",\"notes\":\"$notes\"}")).apply {
            enqueue(callback)
        }
    }

    fun confirmReceipt(goodOrderId: String, storeId: String, callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.confirmReceipt(RetrofitService.getRequestBody("{\"goodOrderId\":\"$goodOrderId\",\"storeId\":\"$storeId\"}")).apply {
            enqueue(callback)
        }
    }

    fun deleteGoodsOrder(goodOrderId: String, storeId: String, callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.deleteGoodsOrder(RetrofitService.getRequestBody("{\"goodOrderId\":\"$goodOrderId\",\"storeId\":\"$storeId\"}")).apply {
            enqueue(callback)
        }
    }

    fun getGoodsOrderDetail(goodOrderId: String, storeId: String, callback: Callback<ResponseBody>): Call<ResponseBody> {
        return integralApiService.getGoodsOrderDetail(RetrofitService.getRequestBody("{\"goodOrderId\":\"$goodOrderId\",\"storeId\":\"$storeId\"}")).apply {
            enqueue(callback)
        }
    }

}
