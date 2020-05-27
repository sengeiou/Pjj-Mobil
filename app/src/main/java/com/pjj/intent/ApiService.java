package com.pjj.intent;


import com.pjj.module.ScreenModelBean;
import com.pjj.module.listener.HomeCommon;
import com.pjj.module.listener.Result;
import com.pjj.module.listener.TextBean;
import com.pjj.module.parameters.Area;
import com.pjj.module.parameters.Elevator;
import com.pjj.module.parameters.ElevatorTime;
import com.pjj.module.parameters.IdentityInf;
import com.pjj.module.parameters.JiangKang;
import com.pjj.module.parameters.MakeOrder;
import com.pjj.module.parameters.NewMediaMakeOrder;
import com.pjj.module.parameters.OrderStatue;
import com.pjj.module.parameters.ReleaseFreeOrder;
import com.pjj.module.parameters.ReleaseScreenModel;
import com.pjj.module.parameters.SpeedTemplateUpload;
import com.pjj.module.parameters.Template;
import com.pjj.module.parameters.TemplateBianMin;
import com.pjj.module.parameters.TopPriceBean;
import com.pjj.module.parameters.UploadTemplate;
import com.pjj.module.parameters.UploadTemplateNew;
import com.pjj.module.parameters.ZhiDing;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Url;

/**
 * Create by xinheng on 2018/10/12 0012。
 * describe：retrofit网络接口
 */
public interface ApiService {
    /**
     * 获取城市列表
     *
     * @param requestBody
     * @return
     */
    @POST("getCityList.action ")
    Call<ResponseBody> getCityList(@Body RequestBody requestBody);   // 请求体味RequestBody 类型

    @POST("getAreaCodebyName.action")
    Call<ResponseBody> getAreaCodebyName(@Body RequestBody requestBody);

    /**
     * 查询区县
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("getcountyList.action")
    Call<ResponseBody> getCountyList(@Body RequestBody requestBody);

    @GET
    Call<ResponseBody> downloadFile(@Url String url);

    /**
     * 验证码
     *
     * @param phone     手机号
     * @param fromWhere 动机 ，哈哈哈
     * @return
     */
    @GET("sendMessage.action")
    Call<ResponseBody> sendMessage(@Query("phone") String phone, @Query("fromWhere") String fromWhere);

    /**
     * 用户注册 修改 密码
     * <p>
     * 用户信息
     *
     * @return
     */
    @GET("register.action")
    Call<ResponseBody> register(@Query("phone") String phone, @Query("password") String password, @Query("checkCode") String checkCode, @Query("reset") String reset);

    @GET("login.action")
    Call<ResponseBody> login(@Query("phone") String phone, @Query("password") String password);

    @GET("login.action")
    Call<ResponseBody> loginCheckCode(@Query("phone") String phone, @Query("checkCode") String checkCode);

    /**
     * 电梯列表
     *
     * @param elevator
     * @return
     */
    @POST("getElevatorList.action")
    Call<ResponseBody> getElevatorList(@Body Elevator elevator);

    /**
     * 广告屏不可用时间查询(时间段)
     *
     * @param elevator
     * @return
     */
    @POST("getElevatorTime.action")
    Call<ResponseBody> getElevatorTime(@Body ElevatorTime elevator);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("getTimeDiscount.action")
    Call<ResponseBody> getTimeDiscount(@Body RequestBody requestBody);

    /**
     * 个人模板
     *
     * @param template
     * @return
     */
    @POST("getUserTempletList.action")
    Call<ResponseBody> getUserTempletList(@Body Template template);

    /**
     * 查询新媒体用户模板
     *
     * @param template
     * @return
     */
    @POST("getNewMediaTempletList.action")
    Call<ResponseBody> getNewMediaTempletList(@Body Template template);

    @POST("getPeopleInfo.action")
    Call<ResponseBody> getPeopleInfo(@Body Template template);

    @POST("listCommunityByRang.action")
    Call<ResponseBody> listCommunityByRang(@Body Area area);

    /**
     * 根据区查询大厦
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("listCommunityByArea.action")
    Call<ResponseBody> listCommunityByArea(@Body RequestBody requestBody);

    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("getBasePrice.action")
    Call<ResponseBody> getBasePrice(@Body RequestBody requestBody);

    /**
     * 便民 diy
     *
     * @param requestBody
     * @return
     */
    @POST("generateOrder.action")
    Call<ResponseBody> generateOrder(@Body MakeOrder requestBody);

    /**
     * 填空 随机
     *
     * @param requestBody
     * @return
     */
    @POST("generateBlankOrder.action")
    Call<ResponseBody> generateBlankOrder(@Body MakeOrder requestBody);

    /**
     * 生成拼屏订单
     *
     * @param requestBody
     * @return
     */
    @POST("generateSplicingTemplet.action")
    Call<ResponseBody> generateSplicingTemplet(@Body MakeOrder requestBody);

    @POST("goAlipay.action")
    Call<ResponseBody> goAlipay(@Body RequestBody requestBody);

    @POST("goWxpay.action")
    Call<ResponseBody> goWxpay(@Body RequestBody requestBody);

    /**
     * 删除模板(DIY/随机)
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("delTemplet.action")
    Call<ResponseBody> delTemplet(@Body RequestBody requestBody);

    /**
     * 删除便民模板
     *
     * @param requestBody
     * @return
     */
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST("delPeopleInfo.action")
    Call<ResponseBody> delPeopleInfo(@Body RequestBody requestBody);

    /**
     * 文件上传
     *
     * @return 状态信息String
     */
//    @Multipart
    @POST("uploadTemplet.action")
    //Call<ResponseBody> uploadTemplateFile(@Part("body") RequestBody body, @Part("description") RequestBody description, @Part MultipartBody.Part file);
    Call<ResponseBody> uploadTemplateFile(@Body RequestBody body);

    /**
     * 实名认证
     * 图片上传
     *
     * @param body
     * @return
     */
    @POST("authentication.action")
    Call<ResponseBody> authentication(@Body RequestBody body);

    /**
     * 头像上传
     *
     * @param body
     * @return
     */
    @POST("uploadHeadNew.action")
    Call<ResponseBody> uploadHead(@Body RequestBody body);

    @POST("findOrderList.action")
    Call<ResponseBody> findOrderList(@Body OrderStatue orderStatue);

    /**
     * 订单详情
     *
     * @param body
     * @return
     */
    @POST("findOrderDetail.action")
    Call<ResponseBody> findOrderDetail(@Body RequestBody body);

    /**
     * 意见反馈
     *
     * @param body
     * @return
     */
    @POST("addUserMessage.action")
    Call<ResponseBody> addUserMessage(@Body RequestBody body);

    @POST("user/addUserMessage.action")
    Call<ResponseBody> addUserMessageType(@Body RequestBody body);

    /**
     * 随机订单详情
     *
     * @param body
     * @return
     */
    @POST("findBlankOrderDetail.action")
    Call<ResponseBody> findBlankOrderDetail(@Body RequestBody body);

    /**
     * 创建便民模板
     *
     * @param body
     * @return
     */
    @POST("addPeopleInfo.action")
    Call<ResponseBody> addPeopleInfo(@Body TemplateBianMin body);

    /**
     * 更新便民信息
     *
     * @param body
     * @return
     */
    @POST("updatePeopleInfo.action")
    Call<ResponseBody> updatePeopleInfo(@Body TemplateBianMin body);

    /**
     * 字典查询
     *
     * @param body
     * @return
     */
    @POST("getDiction.action")
    Call<ResponseBody> getDiction(@Body RequestBody body);

    /**
     * 验证用户是否认证
     * {userId:123}
     *
     * @param body
     * @return
     */
    @POST("verificaUser.action")
    Call<ResponseBody> verificaUser(@Body RequestBody body);

    /**
     * 查询是否可以监播
     *
     * @param body
     * @return
     */
    @POST("getIsPlay.action")
    Call<ResponseBody> getIsPlay(@Body RequestBody body);

    @POST("editUserInfo.action")
    Call<ResponseBody> editUserInfo(@Body RequestBody body);

    /**
     * 获取认证失败图片
     *
     * @param body
     * @return
     */
    @POST("getUserAuth.action")
    Call<ResponseBody> getUserAuthList(@Body RequestBody body);

    @POST("getAccessKey.action")
    Call<ResponseBody> getAccessKey(@Body RequestBody body);

    /**
     * 新模板上传
     * 2018年12月22日16:33:57
     *
     * @param body
     * @return
     */
    @POST("uploadTempletNew.action")
    Call<ResponseBody> uploadTempletNew(@Body UploadTemplateNew body);

    /**
     * 个人认证上传
     * 2019年1月9日18:15:14
     *
     * @param body
     * @return
     */
    @POST("authentication.action")
    Call<ResponseBody> authentication(@Body IdentityInf body);

    /**
     * 商家认证
     *
     * @param body
     * @return
     */
    @POST("authenticationBusiness.action")
    Call<ResponseBody> authenticationBusiness(@Body IdentityInf body);

    @POST("updateTemplet.action")
    Call<ResponseBody> updateTemplet(@Body RequestBody body);

    @POST("queryOrderPay.action")
    Call<ResponseBody> queryOrderPay(@Body RequestBody body);

    /**
     * 访问医学微视
     *
     * @param body
     * @return
     */
    @POST("goMedicineView.action")
    Call<ResponseBody> goMedicineView(@Body RequestBody body);

    /**
     * 访问招商网站
     *
     * @param body
     * @return
     */
    @POST("goZsView.action")
    Call<ResponseBody> goZsView(@Body RequestBody body);

    /**
     * 首页轮播图
     *
     * @param body
     * @return
     */
    @POST("getAppPictureList.action")
    Call<ResponseBody> getAppPictureList(@Body RequestBody body);

    /**
     * 取消订单
     *
     * @param body
     * @return
     */
    @POST("cancelOrder.action")
    Call<ResponseBody> cancelOrder(@Body RequestBody body);

    /**
     * app获取版本信息
     *
     * @param body
     * @return
     */
    @POST("getAppVersion.action")
    Call<ResponseBody> getAppVersion(@Body RequestBody body);

    /**
     * 发送截屏指令
     *
     * @param body
     * @return
     */
    @POST("captureImg.action")
    Call<ResponseBody> captureImg(@Body RequestBody body);

    /**
     * 拼屏订单生成
     *
     * @param body
     * @return
     */
    @POST("generateSplicingTemplate.action")
    Call<ResponseBody> generateSplicingTemplet(@Body SpeedTemplateUpload body);

    /**
     * 获取拼屏模板类型列表
     *
     * @param body
     * @return
     */
    @POST("getSpellIdentification.action")
    Call<ResponseBody> getSpellIdentification(@Body RequestBody body);

    /**
     * 拼屏模板上传
     *
     * @param body
     * @return
     */
    @POST("uploadSplicingTemplet.action")
    Call<ResponseBody> uploadSplicingTemplet(@Body SpeedTemplateUpload body);

    @POST("getSpellTempleByUserId.action")
    Call<ResponseBody> getSpellTempleByUserId(@Body RequestBody body);

    @POST("getUserSpellTempleByIdentificationId.action")
    Call<ResponseBody> getUserSpellTempleByIdentificationId(@Body RequestBody body);

    /**
     * 健康预约
     *
     * @param body
     * @return
     */
    @POST("insertHealthyRegister.action")
    Call<ResponseBody> insertHealthyRegister(@Body JiangKang body);

    /**
     * 通过行政区查询小区信息和新媒体屏幕数量
     *
     * @param body
     * @return
     */
    @POST("getCommunityNewMediaScreenNumByArea.action")
    Call<ResponseBody> getCommunityNewMediaScreenNumByArea(@Body RequestBody body);

    /**
     * 通过小区id查询相关新媒体屏幕信息
     *
     * @param body
     * @return
     */
    @POST("getNewMediaScreenInfoByCommunity.action")
    Call<ResponseBody> getNewMediaScreenInfoByCommunity(@Body RequestBody body);

    /**
     * 生成新媒体订单
     *
     * @param body
     * @return
     */
    @POST("generateNewMediaOrder.action")
    Call<ResponseBody> generateNewMediaOrder(@Body NewMediaMakeOrder body);

    /**
     * 广告屏不可用时间查询（时间段）
     *
     * @param body
     * @return
     */
    @POST("getElevatorTime.action")
    Call<ResponseBody> getElevatorTime(@Body RequestBody body);

    /**
     * 查询新传媒订单详情
     *
     * @param body
     * @return
     */
    @POST("findNewMediaOrderDetail.action")
    Call<ResponseBody> findNewMediaOrderDetail(@Body RequestBody body);

    /**
     * 获取订单截屏图片信息
     *
     * @param body
     * @return
     */
    @POST("getScreenImgList.action")
    Call<ResponseBody> getScreenImgList(@Body RequestBody body);

    /**
     * 发送订单截屏
     *
     * @param body
     * @return
     */
    @POST("screenshots.action")
    Call<ResponseBody> screenshots(@Body RequestBody body);

    /**
     * 获取 全国所有省市区架构
     *
     * @param body
     * @return
     */
    @POST("getAreaAll.action")
    Call<ResponseBody> getAreaAll(@Body RequestBody body);

    /**
     * 根据市编码获取大厦
     *
     * @param body
     * @return
     */
    @POST("getNewMediaCommNumByCityArea.action")
    Call<ResponseBody> getNewMediaCommNumByCityArea(@Body RequestBody body);

    @POST("getCommunityElevatorList.action")
    Call<ResponseBody> getCommunityElevatorList(@Body RequestBody body);

    /**
     * 查询归属屏幕
     *
     * @param body
     * @return
     */
    @POST("getOwnScreen.action")
    Call<ResponseBody> getOwnScreen(@Body RequestBody body);

    /**
     * 设置物业便民信息
     *
     * @param body
     * @return
     */
    @POST("setPropertyInfo.action")
    Call<ResponseBody> setPropertyInfo(@Body RequestBody body);

    /**
     * 设置物业便民信息
     *
     * @param body
     * @return
     */
    @POST("setOwnScreen.action")
    Call<ResponseBody> setOwnScreen(@Body RequestBody body);

    /**
     * 发布自用和地方自营投屏素材
     *
     * @param body
     * @return
     */
    @POST("generateOwnManageOrder.action")
    Call<ResponseBody> generateOwnManageOrder(@Body ReleaseScreenModel body);

    @POST("deleteOwnFile.action")
    Call<ResponseBody> deleteOwnFile(@Body RequestBody body);

    @POST("inserOwnFile.action")
    Call<ResponseBody> inserOwnFile(@Body RequestBody body);

    @POST("getOwnFile.action")
    Call<ResponseBody> getOwnFile(@Body RequestBody body);

    /**
     * 查询自定义模板
     *
     * @param body
     * @return
     */
    @POST("getAdTemplet.action")
    Call<ResponseBody> getAdTemplet(@Body RequestBody body);

    /**
     * 自定义模板元素
     *
     * @param body
     * @return
     */
    @POST("getAdTempletDetails.action")
    Call<ResponseBody> getAdTempletDetails(@Body RequestBody body);

    /**
     * 天气查询
     *
     * @param cityname
     * @return
     */
    @GET("Weather/Query?key=5694547aab284f3ea129cc4b2295accf")
    Call<ResponseBody> getWeather(@Query("cityname") String cityname);

    /**
     * 获取投放区域距离和小区类型
     *
     * @param body
     * @return
     */
    @POST("getCommunityRangeType.action")
    Call<ResponseBody> getCommunityRangeType(@Body RequestBody body);

    /**
     * 屏幕管理订单查询
     *
     * @param body
     * @return
     */
    @POST("getAppOwnOrderList.action")
    Call<ResponseBody> getAppOwnOrderList(@Body RequestBody body);

    /**
     * 屏幕管理撤单
     *
     * @param body
     * @return
     */
    @POST("revokeOwnOrder.action")
    Call<ResponseBody> revokeOwnOrder(@Body RequestBody body);

    @POST("getOwnAdTemplet.action")
    Call<ResponseBody> getOwnAdTemplet(@Body RequestBody body);

    /**
     * 获取推广置顶价格
     *
     * @param body
     * @return
     */
    @POST("getTopPriceList.action")
    Call<ResponseBody> getTopPriceList(@Body RequestBody body);

    /**
     * 置顶查询列表
     * 可附加条件
     *
     * @param body
     * @return
     */
    @POST("getOrderTempletList.action")
    Call<ResponseBody> getOrderTempletList(@Body ZhiDing body);

    /**
     * 置顶推荐列表
     * 无附加条件
     *
     * @param body
     * @return
     */
    @POST("getRecommendedOrderTempletList.action")
    Call<ResponseBody> getRecommendedOrderTempletList(@Body ZhiDing body);

    /**
     * 我的推广
     *
     * @param body
     * @return
     */
    @POST("getTopOrderList.action")
    Call<ResponseBody> getTopOrderList(@Body RequestBody body);

    /**
     * 提交推广置顶订单，生成支付订单
     *
     * @param body
     * @return
     */
    @POST("generateTopOrder.action")
    Call<ResponseBody> generateTopOrder(@Body RequestBody body);

    @POST("goTopOrderAlipay.action")
    Call<ResponseBody> goTopOrderAlipay(@Body RequestBody body);

    @POST("goTopOrderWxpay.action")
    Call<ResponseBody> goTopOrderWxpay(@Body RequestBody body);
    @POST("releaseFreeOrder.action")
    Call<ResponseBody> releaseFreeOrder(@Body ReleaseFreeOrder body);
    @POST("updateFreeOrderStatus.action")
    Call<ResponseBody> updateFreeOrderStatus(@Body RequestBody body);
    @POST("getFreeOrderList.action")
    Call<ResponseBody> getFreeOrderList(@Body RequestBody body);
    @POST("canacelFreeTopOrder.action")
    Call<ResponseBody> canacelFreeTopOrder(@Body RequestBody body);

    /**
     * 置顶订单取消推广
     * @param body
     * @return
     */
    @POST("cancelTopOrder.action")
    Call<ResponseBody> cancelTopOrder(@Body RequestBody body);

}
