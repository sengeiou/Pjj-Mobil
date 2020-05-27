package com.pjj.view.adapter;


import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pjj.R;
import com.pjj.module.AZItemEntity;
import com.pjj.module.BuildingBean;
import com.pjj.utils.TextUtils;
import com.pjj.utils.ViewUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class BuildingAdapter extends AZBaseAdapter<BuildingBean.CommunityListBean, RecyclerView.ViewHolder> {
    List<AZItemEntity<BuildingBean.CommunityListBean>> dataListOld;
    private boolean selectTag;
    private List<Integer> mSelectList;

    public BuildingAdapter(List<AZItemEntity<BuildingBean.CommunityListBean>> dataList, boolean selectTag) {
        super(dataList);
        dataListOld = dataList;
        this.selectTag = selectTag;
        mSelectList = new ArrayList<>();
    }

    public void filter(String filter) {
        mSelectList.clear();
        if (TextUtils.isEmpty(filter)) {
            if (null != dataListOld) {
                setDataList(dataListOld);
            }
            return;
        }
        if (TextUtils.isNotEmptyList(mDataList)) {
            List<AZItemEntity<BuildingBean.CommunityListBean>> listNew = new ArrayList<>();
            Iterator<AZItemEntity<BuildingBean.CommunityListBean>> iterator = mDataList.iterator();
            while (iterator.hasNext()) {
                AZItemEntity<BuildingBean.CommunityListBean> next = iterator.next();
                if (null != next && null != next.getValue()) {
                    String communityName = next.getValue().getCommunityName();
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
            //itemView.setOnClickListener(onClick);
            return new ItemHolderTitle(itemView);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_text_select_item, parent, false);
            view.setOnClickListener(onClickListener);
            return new ItemHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDataList.get(position).getValue() == null ? 0 : 1;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder) {
            BuildingBean.CommunityListBean communityListBean = mDataList.get(position).getValue();
            ((ItemHolder) holder).mTextName.setText(communityListBean.getCommunityName());
            ((ItemHolder) holder).mTextName.getRootView().setTag(position);
            String screenNum = communityListBean.getScreenNum();
            int parseInt = 0;
            try {
                parseInt = Integer.parseInt(screenNum);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (parseInt <= 0) {
                ((ItemHolder) holder).ivSelect.setVisibility(View.GONE);
            } else {
                ((ItemHolder) holder).ivSelect.setVisibility(View.VISIBLE);
                if (mSelectList.contains(position)) {
                    ((ItemHolder) holder).ivSelect.setImageResource(R.mipmap.select);
                } else {
                    ((ItemHolder) holder).ivSelect.setImageResource(R.mipmap.unselect);
                }
            }
        } else {
            ((ItemHolderTitle) holder).mTextName.setText(mDataList.get(position).getSortLetters());
        }
        holder.itemView.setTag(R.id.position, position);
    }

    private OnItemClickListener onItemClickListener;

    private View.OnClickListener onClickListener = v -> {
        Object tag = v.getTag();
        if (tag instanceof Integer) {
            Integer position = (Integer) tag;
            if (selectTag) {
                View viewById = v.findViewById(R.id.iv_select);
                if (null != viewById && viewById instanceof ImageView) {
                    if (mSelectList.contains(position)) {
                        ((ImageView) viewById).setImageResource(R.mipmap.unselect);
                        mSelectList.remove(position);
                    } else {
                        ((ImageView) viewById).setImageResource(R.mipmap.select);
                        mSelectList.add(position);
                    }
                }
            } else {
                if (null != onItemClickListener) {
                    BuildingBean.CommunityListBean communityListBean = mDataList.get(position).getValue();
                    if (null != communityListBean) {
                        onItemClickListener.itemClick(communityListBean);
                    }
                }
            }
        }
    };

    public List<BuildingBean.CommunityListBean> getSelectBuildings() {
        int size = mSelectList.size();
        if (size > 0) {
            TextUtils.compareSmallToMore(mSelectList);
            ArrayList<BuildingBean.CommunityListBean> listBeans = new ArrayList<>(size);
            for (int i = 0; i < size; i++) {
                BuildingBean.CommunityListBean value = getDataList().get(mSelectList.get(i)).getValue();
                listBeans.add(value);
            }
            return listBeans;
        } else {
            return null;
        }
    }

    class ItemHolder extends RecyclerView.ViewHolder {

        TextView mTextName;
        ImageView ivSelect;

        ItemHolder(View itemView) {
            super(itemView);
            mTextName = itemView.findViewById(R.id.tv_text);
            ivSelect = itemView.findViewById(R.id.iv_select);
            if (selectTag) {
                ivSelect.setVisibility(View.VISIBLE);
            }
        }
    }

    class ItemHolderTitle extends RecyclerView.ViewHolder {

        TextView mTextName;

        ItemHolderTitle(View itemView) {
            super(itemView);
            mTextName = (TextView) itemView;
        }
    }

    /*private View.OnClickListener onClick = v -> {
        if (null != onItemClickListener) {
            int tag = (int) v.getTag(R.id.position);
            AZItemEntity<BuildingBean.CommunityListBean> communityListBeanAZItemEntity = mDataList.get(tag);
            BuildingBean.CommunityListBean value = communityListBeanAZItemEntity.getValue();
            if (null != value) {
                onItemClickListener.itemClick(value);
            }
        }
    };*/

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void itemClick(BuildingBean.CommunityListBean data);
    }
}
