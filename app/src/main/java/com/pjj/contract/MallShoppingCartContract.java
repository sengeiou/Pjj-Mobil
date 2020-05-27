package com.pjj.contract;

import com.pjj.module.ShopCarBean;

import java.util.List;

/**
 * Create by xinheng on 2019/05/08 16:16。
 * describe：
 */
public interface MallShoppingCartContract {
    interface View extends BaseView {
        void updateShopCar(List<ShopCarBean.DataBean> list);

        void addSuccess();

        void refresh();

        void deleteSuccess();

        void checkPass(String json);
    }

    interface Present {
        void loadShopCarList();

        void loadAddShopCar(String goodsId, boolean tag, int goodsNum);

        void loadDeleteShopCarGoods(String shoppingCartId);

        void loadCheckGoodsStockTask(String json);
    }
}