package com.pjj.present;

import com.pjj.BuildConfig;
import com.pjj.contract.HomePageContract;
import com.pjj.db.DaoManager;
import com.pjj.db.WeatherBeanDao;
import com.pjj.intent.RetrofitService;
import com.pjj.module.PicBean;
import com.pjj.module.UserTempletBean;
import com.pjj.module.WeatherBean;
import com.pjj.module.parameters.ZhiDing;
import com.pjj.utils.DateUtils;
import com.pjj.utils.JsonUtils;
import com.pjj.utils.TextUtils;

import java.util.Calendar;
import java.util.Locale;

/**
 * Create by xinheng on 2018/11/07 16:38。
 * describe：P层
 */
public class HomePagePresent extends BasePresent<HomePageContract.View> implements HomePageContract.Present {

    public HomePagePresent(HomePageContract.View view) {
        super(view, HomePageContract.View.class);
    }

    @Override
    public void loadWeatherStatue(String cityName) {
        //if (BuildConfig.APP_TYPE) {
            Calendar calendar = Calendar.getInstance(Locale.SIMPLIFIED_CHINESE);
            String date = DateUtils.getSf(calendar);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            String weatherJson = com.pjj.db.WeatherBean.getWeatherJson(date, hour, cityName);
            if (!TextUtils.isEmpty(weatherJson)) {
                WeatherBean weatherBean = JsonUtils.parse(weatherJson, WeatherBean.class);
                dealwithWeatherData(weatherBean);
                return;
            }
            getRetrofitService().loadWeatherTask(cityName, new PresentCallBack<WeatherBean>(WeatherBean.class) {
                @Override
                protected void successResult(WeatherBean weatherBean) {
                    dealwithWeatherData(weatherBean);
                    com.pjj.db.WeatherBean.insert(weatherBean, date, hour, cityName);
                }

                @Override
                protected void fail(String error) {
                    //super.fail(error);
                    //mView.showNotice("该城市暂时无法查询天气");
                    mView.updateWeather("", "", "", "");
                }
            });
        //}
    }

    @Override
    public void loadZhiDingSearchTask(ZhiDing bean) {
        getRetrofitService().getOrderTempletList(bean, new PresentCallBack<UserTempletBean>(UserTempletBean.class) {
            @Override
            protected void successResult(UserTempletBean userTempletBean) {
                mView.updateZhiDingData(userTempletBean.getData());
            }
        });
    }

    private void dealwithWeatherData(WeatherBean weatherBean) {
        String temperature = "";
        String info = "";
        String level = "";
        String quality = "";
        try {
            WeatherBean.ResultBeanX result = weatherBean.getResult();
            WeatherBean.ResultBeanX.RealtimeBean realtime = result.getRealtime();
            WeatherBean.ResultBeanX.WeatherBeanX weatherX = realtime.getWeather();
            temperature = weatherX.getTemperature();//温度
            if (!TextUtils.isEmpty(temperature)) {
                temperature += "℃";
            }
            info = weatherX.getInfo();//天气
            WeatherBean.ResultBeanX.Pm25BeanX pm25 = result.getPm25();
            WeatherBean.ResultBeanX.Pm25BeanX.Pm25Bean pm251 = pm25.getPm25();
            level = pm251.getPm25();
            quality = pm251.getQuality();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mView.updateWeather(temperature, quality, level, info);
    }

    @Override
    public void loadHomePicTask(String pictrueType) {
        getRetrofitService().loadHomePicTask(pictrueType, new RetrofitService.CallbackClassResult<PicBean>(PicBean.class) {
            @Override
            protected void successResult(PicBean picBean) {
                mView.updateHomePic(picBean, pictrueType);
            }

            @Override
            protected void fail(String error) {
                super.fail(error);
                mView.updateHomePicFail(error);
            }
        });
    }
}
