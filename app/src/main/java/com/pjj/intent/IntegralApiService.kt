package com.pjj.intent

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Created by XinHeng on 2019/04/09.
 * describe：
 */

interface IntegralApiService {
    /**
     * 查询金币商品
     * @param requestBody
     * @return
     */
    @POST("integralGoods/getAppIntergralGoodsList.action")
    fun getAppIntergralGoodsList(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 查询金币是否充足
     * @param requestBody
     * @return
     */
    @POST("integralUser/isUserIntegralBalance.action")
    fun isUserIntegralBalance(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 获取用户收货地址
     * @param requestBody
     * @return
     */
    @POST("integralUser/getReceivingAddressList.action")
    fun getReceivingAddressList(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 添加用户收货地址
     * @param requestBody
     * @return
     */
    @POST("integralUser/insertReceivingAddress.action")
    fun insertReceivingAddress(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 修改用户收货地址
     * @param requestBody
     * @return
     */
    @POST("integralUser/updateReceivingAddress.action")
    fun updateReceivingAddress(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 删除收货地址
     * @param requestBody
     * @return
     */
    @POST("integralUser/deleteReceivingAddress.action")
    fun deleteReceivingAddress(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 生成金币商品订单
     * @param requestBody
     * @return
     */
    @POST("integralOrder/generateIntegralOrder.action")
    fun generateIntegralOrder(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 获取金币商品订单
     * @param requestBody
     * @return
     */
    @POST("integralOrder/getIntegralOrderList.action")
    fun getIntegralOrderList(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 获取用户金币记录
     */
    @POST("getIntegralRecordList.action")
    fun getIntegralRecordList(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 获取用户金币余额
     */
    @POST("getUserIntegralByUserId.action")
    fun getUserIntegralByUserId(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 获取金币规则
     */
    @POST("getIntegralRule.action")
    fun getIntegralRule(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 查询金币订单详情
     */
    @POST("integralOrder/getIntegralOrderByOrderId.action")
    fun getIntegralOrderByOrderId(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 确认金币商品订单
     */
    @POST("integralOrder/confirmReceivingGoods.action")
    fun confirmReceivingGoods(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 去支付宝支付金币商品邮费,获取加密信息
     */
    @POST("integralOrder/goIntegralAlipay.action")
    fun goIntegralAlipay(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 去微信支付金币商品邮费,获取加密信息
     */
    @POST("integralOrder/goIntegralWxpay.action")
    fun goIntegralWxpay(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 获取金币商品分类列表
     */
    @POST("integralGoods/getIntegralGoodsType.action")
    fun getIntegralGoodsType(@Body requestBody: RequestBody): Call<ResponseBody>

    //购物商城
    /**
     * 获取广告位轮播图和商品种类
     */
    @POST("/business/getAdverBanner.action")
    fun getAdverBanner(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 获取商品种类
     */
    @POST("/business/getGoodsCategory.action")
    fun getGoodsCategory(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 获取商品列表
     */
    @POST("/business/getGoodsByQuery.action")
    fun getGoodsByQuery(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 获取商品详情
     */
    @POST("/business/getGoodsDetails.action")
    fun getGoodsDetails(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 添加购物车
     */
    @POST("/shoppingCar/addShoppingCart.action")
    fun addShoppingCart(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 删除购物车
     */
    @POST("/shoppingCar/delShoppingCart.action")
    fun delShoppingCart(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 查询购物车列表
     */
    @POST("/shoppingCar/getShoppingCart.action")
    fun getShoppingCart(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 修改购物车商品数量
     */
    @POST("/shoppingCar/updateShoppingCartGoodsNum.action")
    fun updateShoppingCartGoodsNum(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 提交商品订单
     */
    @POST("/shoppingOrder/generateShoppingOrder.action")
    fun generateShoppingOrder(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 查询订单列表
     */
    @POST("/shoppingOrder/getGoodsOrderList.action")
    fun getGoodsOrderList(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 查询订单详情
     */
    @POST("/shoppingOrder/getGoodsOrderDetail.action")
    fun getGoodsOrderDetail(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 删除订单
     */
    @POST("/shoppingOrder/deleteGoodsOrder.action")
    fun deleteGoodsOrder(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 取消订单
     */
    @POST("/shoppingOrder/cancelShoppingOrder.action")
    fun cancelShoppingOrder(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 确认
     */
    @POST("/shoppingOrder/confirmReceipt.action")
    fun confirmReceipt(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 购物商城订单支付
     */
    @POST("/shoppingOrder/goShoppingPay.action")
    fun goShoppingPay(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 获取取消订单原因
     */
    @POST("/shoppingOrder/getCancelOrderInfo.action")
    fun getCancelOrderInfo(@Body requestBody: RequestBody): Call<ResponseBody>

    /**
     * 确认购物车商品数量库存
     */
    @POST("/business/confirmGoodsStock.action")
    fun confirmGoodsStock(@Body requestBody: RequestBody): Call<ResponseBody>


}
