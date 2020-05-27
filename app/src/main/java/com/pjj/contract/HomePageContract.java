package com.pjj.contract;

import com.pjj.module.PicBean;
import com.pjj.module.UserTempletBean;
import com.pjj.module.parameters.ZhiDing;

import java.util.List;

/**
 * Create by xinheng on 2018/11/07 16:38。
 * describe：
 */
public interface HomePageContract {
    interface View extends BaseView {
        void toSelectLocationView();

        void toAboutSelfView();

        void toInformation();

        void updateHomePic(PicBean list, String pictrueType);

        void updateHomePicFail(String error);

        void updateWeather(String temperature, String quality, String level, String info);

        void updateZhiDingData(List<UserTempletBean.DataBean> list);
    }

    interface Present {
        void loadHomePicTask(String pictrueType);

        void loadWeatherStatue(String cityName);

        void loadZhiDingSearchTask(ZhiDing bean);
    }
}