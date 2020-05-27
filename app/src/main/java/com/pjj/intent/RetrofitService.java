package com.pjj.intent;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.pjj.BuildConfig;
import com.pjj.module.CityBean;
import com.pjj.module.ResultBean;
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
import com.pjj.module.parameters.User;
import com.pjj.module.parameters.ZhiDing;
import com.pjj.utils.FileUtils;
import com.pjj.utils.JsonUtils;
import com.pjj.utils.Log;
import com.pjj.utils.TextUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Create by xinheng on 2018/10/12 0012。
 * describe：网络接口实现
 */
public class RetrofitService {
    private static RetrofitService RETROFIT_SERVICE;

    private RetrofitService() {
        initRetrofit();
    }

    private AppendUrlParamInterceptor appendUrlParamInterceptor;
    private Retrofit retrofit;
    private ApiService apiService;

    private static Gson gson;

    public void setToken(String token) {
        appendUrlParamInterceptor.setToken(token);
        setRetrofit();
        IntegralRetrofitService.Companion.getInstance().initRetrofit(appendUrlParamInterceptor);
    }

    public static RetrofitService getInstance() {
        if (null == RETROFIT_SERVICE) {
            synchronized (RetrofitService.class) {
                if (null == RETROFIT_SERVICE) {
                    RETROFIT_SERVICE = new RetrofitService();
                }
            }
        }
        return RETROFIT_SERVICE;
    }

    private void initRetrofit() {
        appendUrlParamInterceptor = new AppendUrlParamInterceptor();
        gson = JsonUtils.gson;
        IntegralRetrofitService.Companion.getInstance().initRetrofit(appendUrlParamInterceptor);
        setRetrofit();
    }

    private void setRetrofit() {
        retrofit = new Retrofit.Builder()
                //.baseUrl("http://47.92.50.83:8082/")
                .baseUrl(BuildConfig.APP_TYPE ? "http://39.98.75.37:8080/" : "http://47.92.254.65:8088/") //测试
                //.baseUrl("http://39.98.75.37:8080/")//正式
                .client(new OkHttpClient.Builder()
                        .addInterceptor(appendUrlParamInterceptor)
                        .connectTimeout(180, TimeUnit.SECONDS)
                        .readTimeout(120, TimeUnit.SECONDS)
                        .writeTimeout(120, TimeUnit.SECONDS)
                        .build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiService = retrofit.create(ApiService.class);
    }

    public static RequestBody getRequestBody(String string) {
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), string);
        return body;
    }


    public Call<ResponseBody> getCities(CallbackClassResult<CityBean> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getCityList(getRequestBody("{}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> getAreaCodeByName(String area, Callback callback) {
        Call<ResponseBody> responseBodyCall = apiService.getAreaCodebyName(getRequestBody("{\"areaName\":\"" + area + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> getCountyList(String areaCode, Callback callback) {
        Call<ResponseBody> responseBodyCall = apiService.getCountyList(getRequestBody("{\"areaCode\":\"" + areaCode + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadGetCodeTask(String phone, String fromWhere, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.sendMessage(phone, fromWhere);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadRegisterTask(User user, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.register(user.getPhone(), user.getPassword(), user.getCheckCode(), user.getReset());
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadLoginTask(User user, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall;
        if (TextUtils.isEmpty(user.getPassword()) && !TextUtils.isEmpty(user.getCheckCode())) {
            responseBodyCall = apiService.loginCheckCode(user.getPhone(), user.getCheckCode());
        } else {
            responseBodyCall = apiService.login(user.getPhone(), user.getPassword());
        }
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadElevatorListTask(Elevator elevator, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getElevatorList(elevator);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadElevatorTimeTask(ElevatorTime elevator, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getElevatorTime(elevator);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadTimeDiscountTask(Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getTimeDiscount(getRequestBody("{}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadUserTempletListTask(Template template, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getUserTempletList(template);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> getNewMediaTempletList(Template template, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getNewMediaTempletList(template);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadBianMinListTask(Template template, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getPeopleInfo(template);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    /**
     * 猜你所想投放的区域
     *
     * @param area
     * @param callback
     * @return
     */
    public Call<ResponseBody> loadGuessListTask(Area area, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.listCommunityByRang(area);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadAreaBuildingTask(String areaCode, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.listCommunityByArea(getRequestBody("{\"areaCode\":\"" + areaCode + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    /**
     * 全国基准价
     *
     * @param callback
     * @return
     */
    public Call<ResponseBody> getBasePrice(Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getBasePrice(getRequestBody("{}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadMakeOrderTaskDiyAndBm(MakeOrder makeOrder, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.generateOrder(makeOrder);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadMakeOrderTaskTianKong(MakeOrder makeOrder, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.generateBlankOrder(makeOrder);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> generateSplicingTemplate(MakeOrder makeOrder, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.generateSplicingTemplet(makeOrder);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    private Retrofit getTestRetrofit(String url) {
        return new Retrofit.Builder()
                .baseUrl(url) //本地
                .client(new OkHttpClient.Builder()
                        //.addInterceptor(appendUrlParamInterceptor)
                        .connectTimeout(180, TimeUnit.SECONDS)
                        .readTimeout(120, TimeUnit.SECONDS)
                        .writeTimeout(120, TimeUnit.SECONDS)
                        .build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    public Call<ResponseBody> loadAliPayTask(String orderId, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.goAlipay(getRequestBody("{\"orderId\":\"" + orderId + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadWeiXinPayTask(String orderId, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.goWxpay(getRequestBody("{\"orderId\":\"" + orderId + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> deleteBianMin(String id, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.delPeopleInfo(getRequestBody("{\"peopleInfoId\":\"" + id + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> deleteDIY(String id, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.delTemplet(getRequestBody("{\"templetId\":\"" + id + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadOrderStatueTask(OrderStatue orderStatue, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.findOrderList(orderStatue);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadWeatherTask(String cityname, Callback<ResponseBody> callback) {
        Retrofit testRetrofit = getTestRetrofit("http://api.avatardata.cn/");
        Call<ResponseBody> weather = testRetrofit.create(ApiService.class).getWeather(cityname);
        weather.enqueue(callback);
        return weather;
    }

    public Call<ResponseBody> loadOrderInfTask(String orderId, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.findOrderDetail(getRequestBody("{\"orderId\":\"" + orderId + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> addUserMessage(String userId, String message, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall;
        HashMap<String, String> map = new HashMap<>(2);
        map.put("userId", userId);
        map.put("message", message);
        //String s="{\"message\":\"嘟嘟嘟\\nui翻翻看家\\n服服服\",\"userId\":\"dd12d2967f1948cea62627c418c99237\"}";
        String json = JsonUtils.toJsonString(map);
        if (BuildConfig.APP_TYPE) {
            Retrofit retrofit1 = new Retrofit.Builder()
                    .baseUrl("http://39.98.75.37:8087") //正式
                    .client(new OkHttpClient.Builder()
                            .addInterceptor(appendUrlParamInterceptor)
                            .connectTimeout(180, TimeUnit.SECONDS)
                            .readTimeout(120, TimeUnit.SECONDS)
                            .writeTimeout(120, TimeUnit.SECONDS)
                            .build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            responseBodyCall = retrofit1.create(ApiService.class).addUserMessageType(getRequestBody(json));
        } else {
            responseBodyCall = apiService.addUserMessage(getRequestBody(json));
        }
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadOrderRandomInfTask(String orderId, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.findBlankOrderDetail(getRequestBody("{\"orderId\":\"" + orderId + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadUploadBianMinInfTask(TemplateBianMin templateBianMin, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.addPeopleInfo(templateBianMin);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadUpdateBianMinInfTask(TemplateBianMin templateBianMin, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.updatePeopleInfo(templateBianMin);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadUpdateHead(String head, String userId, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.uploadHead(getRequestBody("{\"head\":\"" + head + "\",\"userId\":\"" + userId + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    /**
     * 字典查询
     * 目前行业类型 1
     * 便民信息分类 3
     *
     * @param type
     * @param callback
     * @return
     */
    public Call<ResponseBody> loadInfTask(String type, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getDiction(getRequestBody("{\"type\":\"" + type + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadCertificationTask(String userId, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.verificaUser(getRequestBody("{\"userId\":\"" + userId + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    /**
     * @param orderId
     * @param register
     * @return
     */
    public Call<ResponseBody> loadIsPlayTask(String orderId, String register, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getIsPlay(getRequestBody("{\"orderId\":\"" + orderId + "\",\"screenId\":\"" + register + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadEditUserNameTask(String userId, String nickname, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.editUserInfo(getRequestBody("{\"userId\":\"" + userId + "\",\"nickname\":\"" + nickname + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadCertificationFailTask(String userId, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getUserAuthList(getRequestBody("{\"userId\":\"" + userId + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadAccessKeyTask(String userId, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getAccessKey(getRequestBody("{\"userId\":\"" + userId + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadNewUploadTemplateTask(UploadTemplateNew uploadTemplateNew, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.uploadTempletNew(uploadTemplateNew);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> uploadNewIdentityImage(IdentityInf identityInf, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall;
        if ("1".equals(identityInf.getAuthType())) {
            responseBodyCall = apiService.authentication(identityInf);
        } else {
            responseBodyCall = apiService.authenticationBusiness(identityInf);
        }
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadUploadTemplateNameTask(String templetId, String templetName, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.updateTemplet(getRequestBody("{\"templetId\":\"" + templetId + "\",\"templetName\":\"" + templetName + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadPayResultTask(String orderId, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.queryOrderPay(getRequestBody("{\"orderId\":\"" + orderId + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadYiXueWeiShiTask(String udid, String phone, String from, Callback<ResponseBody> callback) {
        Map<String, String> map = new HashMap<>();
        map.put("udid", udid);
        map.put("phone", phone);
        map.put("from", from);//入口类型
        Call<ResponseBody> responseBodyCall = apiService.goMedicineView(getRequestBody(JsonUtils.toJsonString(map)));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadZhaoShangTask(String udid, String phone, String from, Callback<ResponseBody> callback) {
        Map<String, String> map = new HashMap<>();
        map.put("udid", udid);
        map.put("phone", phone);
        map.put("from", from);//入口类型
        Call<ResponseBody> responseBodyCall = apiService.goZsView(getRequestBody(JsonUtils.toJsonString(map)));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadHomePicTask(String pictrueType, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getAppPictureList(getRequestBody("{\"pictrueType\":\"" + pictrueType + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadCancelOrderTask(String userId, String orderId, String cancelReasons, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.cancelOrder(
                getRequestBody("{\"userId\":\"" + userId + "\",\"orderId\":\"" + orderId + "\",\"cancelReasons\":\"" + cancelReasons + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> loadAppVersionTask(Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getAppVersion(getRequestBody("{\"type\":\"2\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> getSpellIdentification(Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getSpellIdentification(getRequestBody("{}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> uploadSplicingTemplet(SpeedTemplateUpload bean, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.uploadSplicingTemplet(bean);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> getSpellTempleByUserId(String userId, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getSpellTempleByUserId(getRequestBody("{\"userId\":\"" + userId + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> getUserSpellTempleByIdentificationId(String userId, String IdentificationId, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getUserSpellTempleByIdentificationId(getRequestBody("{\"userId\":\"" + userId + "\",\"IdentificationId\":\"" + IdentificationId + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> insertHealthyRegister(JiangKang jiangKang, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.insertHealthyRegister(jiangKang);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> getCommunityNewMediaScreenNumByAreaBuilding(String areaCode, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getCommunityNewMediaScreenNumByArea(getRequestBody("{\"areaCode\":\"" + areaCode + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> getNewMediaScreenInfoByCommunity(String communityId, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getNewMediaScreenInfoByCommunity(getRequestBody("{\"communityId\":\"" + communityId + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> generateNewMediaOrder(NewMediaMakeOrder newMediaMakeOrder, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.generateNewMediaOrder(newMediaMakeOrder);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> getElevatorTime(String screenIds, String selectDate, String orderType, Callback<ResponseBody> callback) {//"screenIds":""
        Call<ResponseBody> responseBodyCall = apiService.getElevatorTime(getRequestBody("{\"screentIds\":\"" + screenIds + "\",\"selectDate\":\"" + selectDate + "\",\"orderType\":\"" + orderType + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> findNewMediaOrderDetail(String orderId, Callback<ResponseBody> callback) {//"screenIds":""
        Call<ResponseBody> responseBodyCall = apiService.findNewMediaOrderDetail(getRequestBody("{\"orderId\":\"" + orderId + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> getScreenImgList(String orderId, String screenId, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getScreenImgList(getRequestBody("{\"orderId\":\"" + orderId + "\",\"screenId\":\"" + screenId + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> screenshots(String orderId, String screenId, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.screenshots(getRequestBody("{\"orderId\":\"" + orderId + "\",\"screenId\":\"" + screenId + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> getAreaAll(Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getAreaAll(getRequestBody("{}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> getNewMediaCommNumByCityArea(String json, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getNewMediaCommNumByCityArea(getRequestBody(json));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> getCommunityElevatorList(String json, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getCommunityElevatorList(getRequestBody(json));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> getOwnScreen(String userId, String queryType, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getOwnScreen(getRequestBody("{\"userId\":\"" + userId + "\",\"queryType\":\"" + queryType + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> getOwnFile(String userId, String sourceType, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getOwnFile(getRequestBody("{\"userId\":\"" + userId + "\",\"sourceType\":\"" + sourceType + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    /**
     * @param purposeType 用途类型 7新媒体传媒模式  9传统模式
     * @param callback
     * @return
     */
    public Call<ResponseBody> getAdTemplet(String userId, String purposeType, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getAdTemplet(getRequestBody("{\"userId\":\"" + userId + "\",\"purposeType\":\"" + purposeType + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    /**
     * @param adTempletId 自定义模板id
     * @param callback
     * @return
     */
    public Call<ResponseBody> getAdTempletDetails(String adTempletId, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getAdTempletDetails(getRequestBody("{\"adTempletId\":\"" + adTempletId + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> getCommunityRangeType(Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getCommunityRangeType(getRequestBody("{}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> setPropertyInfo(String userId, String propertyTitle, String propertyInfo, String screenId, String isShow, Callback<ResponseBody> callback) {
        HashMap<String, String> map = new HashMap<>(5);
        map.put("userId", userId);
        map.put("propertyTitle", propertyTitle);
        map.put("propertyInfo", propertyInfo);
        map.put("screenId", screenId);
        map.put("isShow", isShow);
        Call<ResponseBody> responseBodyCall = apiService.setPropertyInfo(getRequestBody(JsonUtils.toJsonString(map)));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> deleteOwnFile(String partnerFileId, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.deleteOwnFile(getRequestBody("{\"partnerFileId\":\"" + partnerFileId + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> setOwnScreen(String userId, String screenId, String mediaPrice, String cooperationMode, String setVoice, Callback<ResponseBody> callback) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("screenId", screenId);
        map.put("mediaPrice", mediaPrice);
        map.put("cooperationMode", cooperationMode);
        map.put("setVoice", setVoice);
        String json = JsonUtils.toJsonString(map);
        Call<ResponseBody> responseBodyCall = apiService.setOwnScreen(getRequestBody(json));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> inserOwnFile(String userId, String materialName, String fileName, String fileType, String sourceType, Callback<ResponseBody> callback) {
        Map<String, String> map = new HashMap<>();
        map.put("userId", userId);
        map.put("materialName", materialName);
        map.put("fileName", fileName);
        map.put("fileType", fileType);
        map.put("sourceType", sourceType);
        String json = JsonUtils.toJsonString(map);
        Call<ResponseBody> responseBodyCall = apiService.inserOwnFile(getRequestBody(json));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> generateOwnManageOrder(ReleaseScreenModel screenModelBean, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.generateOwnManageOrder(screenModelBean);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> getAppOwnOrderList(String userId, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getAppOwnOrderList(getRequestBody("{\"userId\":\"" + userId + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> revokeOwnOrder(String json, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.revokeOwnOrder(getRequestBody(json));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> getOwnAdTemplet(String userId, String purposeType, Callback<ResponseBody> callback) {
        Map<String, String> map = new HashMap<>(2);
        map.put("userId", userId);
        map.put("purposeType", purposeType);
        Call<ResponseBody> responseBodyCall = apiService.getOwnAdTemplet(getRequestBody(JsonUtils.toJsonString(map)));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> getTopPriceList(Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getTopPriceList(getRequestBody("{}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> getOrderTempletList(ZhiDing bean, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getOrderTempletList(bean);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> getRecommendedOrderTempletList(ZhiDing bean, Callback<ResponseBody> callback) {
        bean.setTempletName(null);
        Call<ResponseBody> responseBodyCall = apiService.getRecommendedOrderTempletList(bean);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> generateTopOrder(String bean, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.generateTopOrder(getRequestBody(bean));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> getTopOrderList(String userId, int pageNo, int pageNum, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getTopOrderList(getRequestBody("{\"userId\":\"" + userId + "\",\"pageNo\":" + pageNo + ",\"pageNum\":" + pageNum + "}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> goTopOrderAlipay(String topOrderId, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.goTopOrderAlipay(getRequestBody("{\"topOrderId\":\"" + topOrderId + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> goTopOrderWxpay(String topOrderId, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.goTopOrderWxpay(getRequestBody("{\"topOrderId\":\"" + topOrderId + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> releaseFreeOrder(ReleaseFreeOrder bean, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.releaseFreeOrder(bean);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> updateFreeOrderStatus(String freeOrderId, String status, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.updateFreeOrderStatus(getRequestBody("{\"freeOrderId\":\"" + freeOrderId + "\",\"status\":\"" + status + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }
    public Call<ResponseBody> canacelFreeTopOrder(String freeOrderId, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.canacelFreeTopOrder(getRequestBody("{\"freeOrderId\":\"" + freeOrderId + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }
    public Call<ResponseBody> cancelTopOrder(String topOrderId, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.cancelTopOrder(getRequestBody("{\"topOrderId\":\"" + topOrderId + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> getFreeOrderList(String userId, int pageNo, int pageNum, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = apiService.getFreeOrderList(getRequestBody("{\"userId\":\"" + userId + "\",\"pageNo\":" + pageNo + ",\"pageNum\":" + pageNum + "}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    private class IntegerDefault0Adapter implements JsonSerializer<Integer>, JsonDeserializer<Integer> {
        @Override
        public Integer deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            try {
                if (json.getAsString().equals("") || json.getAsString().equals("null")) {//定义为int类型,如果后台返回""或者null,则返回0
                    return 0;
                }
            } catch (Exception ignore) {
            }
            try {
                return json.getAsInt();
            } catch (NumberFormatException e) {
                throw new JsonSyntaxException(e);
            }
        }

        @Override
        public JsonElement serialize(Integer src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src);
        }
    }

    public Call<ResponseBody> captureImg(String screenId, Callback<ResponseBody> callback) {
        Call<ResponseBody> responseBodyCall = new Retrofit.Builder()
                .baseUrl("http://47.92.50.83:8083/") //临时
                .client(new OkHttpClient.Builder()
                        .addInterceptor(appendUrlParamInterceptor)
                        .connectTimeout(180, TimeUnit.SECONDS)
                        .readTimeout(120, TimeUnit.SECONDS)
                        .writeTimeout(120, TimeUnit.SECONDS)
                        .build())
                .addConverterFactory(GsonConverterFactory.create())
                .build().create(ApiService.class).captureImg(getRequestBody("{\"screenId\":\"" + screenId + "\"}"));
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    /**
     * 文件下载
     *
     * @param url      下载链接
     * @param savePath 保存所在的文件夹
     */
    public void downloadFile(String url, String savePath, FileUtils.OnDownloadListener onDownloadListener) {
        if (TextUtils.isEmpty(url)) {
            onDownloadListener.fail();
            return;
        }
        final String fileName = getFileName(url);
        Log.e("TAG", "downloadFile: " + url + ", " + fileName);
        File fileComplete = new File(savePath + fileName);
        if (fileComplete.exists()) {
            Log.e("TAG", "downloadFile: 已存在");
            onDownloadListener.success(fileComplete.getAbsolutePath());
            return;
        }
        Call<ResponseBody> responseBodyCall = apiService.downloadFile(url);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    ResponseBody body = response.body();
                    if (null != body) {
                        InputStream inputStream = body.byteStream();
                        saveFile(savePath + "/" + fileName, inputStream, onDownloadListener);
                    } else {
                        onDownloadListener.fail();
                    }
                } else {
                    ResponseBody responseBody = response.errorBody();
                    if (null != responseBody) {
                        String string = null;
                        try {
                            string = responseBody.string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Log.e("TAG", "onResponse: error=" + string);
                    }
                    onDownloadListener.fail();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (null != t) {
                    t.printStackTrace();
                }
                onDownloadListener.fail();
            }
        });
    }

    /**
     * 文件保存
     *
     * @param url                保存地址
     * @param stream             文件流
     * @param onDownloadListener 回调
     */
    private void saveFile(final String url, final InputStream stream, FileUtils.OnDownloadListener onDownloadListener) {
        new Thread() {
            @Override
            public void run() {
                FileUtils.saveFile(url, stream, onDownloadListener);
            }
        }.start();
    }

    public String getFileName(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        // 从路径中获取
        return url.substring(url.lastIndexOf("/") + 1);
    }

    public static abstract class MyCallback implements Callback<ResponseBody> {

        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            Request request = call.request();
            Log.e("TAG", "onResponse: " + request.url().toString());
            if (response.isSuccessful()) {
                ResponseBody body = response.body();
                if (null == body) {
                    fail("service is wrong");
                    return;
                }
                try {
                    String string = body.string();
                    success(string);
                } catch (Exception e) {
                    e.printStackTrace();
                    fail("错误");
                }
            } else {
                try {
                    String string = response.errorBody().string();
                    fail(string);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onFailure(Call call, Throwable t) {
            String error = "网络错误";
            if (null != t) {
                t.printStackTrace();
                String message = t.getMessage();
                if (null != message && message.contains("ailed to connect to")) {
                    error = "网络连接异常";
                }
            }
            fail(error);
        }

        /**
         * 网络接口访问成功
         *
         * @param s 返回的数据
         */
        protected abstract void success(String s);

        /**
         * 结果失败
         *
         * @param error 错误原因
         */
        protected void fail(String error) {
            System.out.println("TAG - fail: " + error);
        }
    }

    public static abstract class CallbackClass<T> extends MyCallback {
        private Class<T> tClass;

        public CallbackClass(Class<T> tClass) {
            this.tClass = tClass;
        }

        @Override
        protected void success(String s) {
            if (null == s) {
                fail("暂无数据");
            } else {
                try {
                    Log.e("TAG", "success: " + s);
                    T t = gson.fromJson(s, tClass);
                    if (null == t) {
                        fail("暂无数据");
                        return;
                    }
                    result(t);
                } catch (JsonSyntaxException e) {
                    e.printStackTrace();
                    fail("pars-error");
                }
            }
        }

        protected abstract void result(T t);
    }

    public static abstract class CallbackClassResult<T extends ResultBean> extends CallbackClass<T> {

        public CallbackClassResult(Class<T> aClass) {
            super(aClass);
        }

        @Override
        protected void result(T t) {
            if (t.isSuccess()) {
                successResult(t);
            } else {
                String msg = t.getMsg();//错误原因
                fail(msg);
            }
        }

        protected abstract void successResult(T t);
    }


    /**
     * 上传
     *
     * @param uploadTemplate 地址
     * @param callback       回调
     * @return 接口响应体
     */
    public Call<ResponseBody> uploadTemplateFile(UploadTemplate uploadTemplate, Callback<ResponseBody> callback) {
        //1&1 1img 2video
        //2&1
        //TODO 多文件上传 待支持
        String s;
        if (uploadTemplate.isImage()) {
            s = "image/png";
        } else {
            s = "multipart/form-data";
        }
        RequestBody requestFile = RequestBody.create(MediaType.parse(s), new File(uploadTemplate.getFilePath()));
        RequestBody requestBody = new MultipartBody.Builder()
                .addFormDataPart("templetType", uploadTemplate.getTempletType())
                .addFormDataPart("userId", uploadTemplate.getUserId())
                .addFormDataPart("fileRelName", uploadTemplate.getFileRelName())
                .addFormDataPart("identityType", uploadTemplate.getIdentityType())
                .addFormDataPart("purposeType", uploadTemplate.getPurposeType())
                .addFormDataPart("fileUpload", uploadTemplate.getFileName(), requestFile)
                .setType(MultipartBody.FORM)
                .build();
        Call<ResponseBody> responseBodyCall = apiService.uploadTemplateFile(requestBody);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

    public Call<ResponseBody> uploadIdentityImage(IdentityInf identityInf, Callback<ResponseBody> callback) {
        String s;
        s = "image/png";
        File file_idFace = new File(identityInf.getIdFace());
        File file_idBack = new File(identityInf.getIdBack());
        File file_idHold = new File(identityInf.getIdHold());
        RequestBody idFace = RequestBody.create(MediaType.parse(s), file_idFace);
        RequestBody idBack = RequestBody.create(MediaType.parse(s), file_idBack);
        RequestBody idHold = RequestBody.create(MediaType.parse(s), file_idHold);

        String authType = identityInf.getAuthType();
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .addFormDataPart("authType", authType)
                .addFormDataPart("userId", identityInf.getUserId())
                .addFormDataPart("name", identityInf.getName())
                .addFormDataPart("id", identityInf.getIdcart())
                .addFormDataPart("idFace", file_idFace.getName(), idFace)
                .addFormDataPart("idBack", file_idBack.getName(), idBack)
                .addFormDataPart("idHold", file_idHold.getName(), idHold)
                .setType(MultipartBody.FORM);
        if ("2".equals(authType)) {
            File file_ = new File(identityInf.getBusinessLicence());
            builder.addFormDataPart("businessLicence", file_.getName(), RequestBody.create(MediaType.parse(s), file_))//营业执照
                    .addFormDataPart("professionType", identityInf.getProfessionType());//行业类型
        }
        RequestBody requestBody = builder.build();
        Call<ResponseBody> responseBodyCall = apiService.authentication(requestBody);
        responseBodyCall.enqueue(callback);
        return responseBodyCall;
    }

}