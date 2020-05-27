package com.pjj.view.adapter;


import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pjj.R;
import com.pjj.module.AZItemEntity;
import com.pjj.module.BuildingBean;
import com.pjj.module.CityBean;
import com.pjj.utils.TextUtils;
import com.pjj.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CitiesAdapter extends AZBaseAdapter<CityBean.CityListBean, RecyclerView.ViewHolder> {
    List<AZItemEntity<CityBean.CityListBean>> dataListOld;

    public CitiesAdapter(List<AZItemEntity<CityBean.CityListBean>> dataList) {
        super(dataList);
        dataListOld = dataList;
    }

    public CityBean.CityListBean getSelectCity(String cityName) {
        if (TextUtils.isEmpty(cityName)) {
            return null;
        }
        if (TextUtils.isNotEmptyList(dataListOld)) {
            for (int i = 0; i < dataListOld.size(); i++) {
                AZItemEntity<CityBean.CityListBean> cityListBeanAZItemEntity = dataListOld.get(i);
                CityBean.CityListBean value = cityListBeanAZItemEntity.getValue();
                if (null != value && cityName.equals(value.getAreaName())) {
                    return value;
                }
            }
        }
        return null;
    }

    public void filter(String filter) {
        if (TextUtils.isEmpty(filter)) {
            if (null != dataListOld) {
                setDataList(dataListOld);
            }
            return;
        }
        if (TextUtils.isNotEmptyList(dataListOld)) {
            List<AZItemEntity<CityBean.CityListBean>> listNew = new ArrayList<>();
            Iterator<AZItemEntity<CityBean.CityListBean>> iterator = dataListOld.iterator();
            while (iterator.hasNext()) {
                AZItemEntity<CityBean.CityListBean> next = iterator.next();
                if (null != next && null != next.getValue()) {
                    String communityName = next.getValue().getAreaName();
                    if (null != communityName && communityName.contains(filter)) {
                        listNew.add(next);
                    }
                }
            }
            setDataList(TextUtils.addFirstLetter(listNew));
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView itemView;
        if (viewType == 0) {
            itemView = new TextView(parent.getContext());
            itemView.setPadding(ViewUtils.getDp(R.dimen.dp_11), 0, 0, 0);
            itemView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getDp(R.dimen.sp_13));
            itemView.setTextColor(ViewUtils.getColor(R.color.color_444444));
            itemView.setGravity(Gravity.CENTER_VERTICAL);
            Drawable drawable = ContextCompat.getDrawable(parent.getContext(), R.mipmap.zimu);
            drawable.setBounds(0, 0, ViewUtils.getDp(R.dimen.dp_16), ViewUtils.getDp(R.dimen.dp_15));
            itemView.setCompoundDrawablePadding(ViewUtils.getDp(R.dimen.dp_10));
            itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_38)));
            itemView.setCompoundDrawables(drawable, null, null, null);
            itemView.setBackgroundColor(ViewUtils.getColor(R.color.color_f1f1f1));
            itemView.setOnClickListener(onClick);
            return new ItemHolderTitle(itemView);
        } else {
            itemView = new TextView(parent.getContext());
            itemView.setPadding(ViewUtils.getDp(R.dimen.dp_37), 0, 0, 0);
            itemView.setTextSize(TypedValue.COMPLEX_UNIT_PX, ViewUtils.getFDp(R.dimen.sp_12));
            itemView.setTextColor(ViewUtils.getColor(R.color.color_444444));
            itemView.setGravity(Gravity.CENTER_VERTICAL);
            itemView.setOnClickListener(onClickListener);
            itemView.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, ViewUtils.getDp(R.dimen.dp_38)));
            return new ItemHolder(itemView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).getValue() == null ? 0 : 1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder) {
            ((ItemHolder) holder).mTextName.setText(mDataList.get(position).getValue().getAreaName());
            ((ItemHolder) holder).mTextName.setTag(position);
        } else {
            ((ItemHolderTitle) holder).mTextName.setText(mDataList.get(position).getSortLetters());
        }
        holder.itemView.setTag(R.id.position, position);
    }

    private OnItemClickListener onItemClickListener;

    private View.OnClickListener onClickListener = v -> {
        Object tag = v.getTag();
        if (null != onItemClickListener && null != tag && tag instanceof Integer) {
            CityBean.CityListBean communityListBean = mDataList.get((Integer) tag).getValue();
            if (null != communityListBean) {
                onItemClickListener.itemClick(communityListBean);
            }
        }
    };

    class ItemHolder extends RecyclerView.ViewHolder {

        TextView mTextName;

        ItemHolder(View itemView) {
            super(itemView);
            mTextName = (TextView) itemView;
        }
    }

    class ItemHolderTitle extends RecyclerView.ViewHolder {

        TextView mTextName;

        ItemHolderTitle(View itemView) {
            super(itemView);
            mTextName = (TextView) itemView;
        }
    }

    private View.OnClickListener onClick = v -> {
        if (null != onItemClickListener) {
            int tag = (int) v.getTag(R.id.position);
            AZItemEntity<CityBean.CityListBean> communityListBeanAZItemEntity = mDataList.get(tag);
            CityBean.CityListBean value = communityListBeanAZItemEntity.getValue();
            if (null != value) {
                onItemClickListener.itemClick(value);
            }
        }
    };

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void itemClick(CityBean.CityListBean data);
    }
}
