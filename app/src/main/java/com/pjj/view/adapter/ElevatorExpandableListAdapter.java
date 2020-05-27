package com.pjj.view.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pjj.R;
import com.pjj.module.ElevatorBean;
import com.pjj.module.xspad.XspManage;
import com.pjj.utils.CalculateUtils;
import com.pjj.utils.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ElevatorExpandableListAdapter extends BaseExpandableListAdapter {
    private List<ElevatorBean.DataBean.ElevatorListBean> mGroupList;
    private List<List<ElevatorBean.DataBean.ElevatorListBean.ScreenListBean>> mChildList;
    private HashMap<Integer, List<Integer>> mSelectList = new HashMap<>();
    private OnSelectStatueListener onSelectStatueListener;
    //小区名称
    private String elevatorName;

    public void setList(String elevatorName, List<ElevatorBean.DataBean.ElevatorListBean> groupList, List<List<ElevatorBean.DataBean.ElevatorListBean.ScreenListBean>> childList) {
        this.mGroupList = groupList;
        this.mChildList = childList;
        this.elevatorName = elevatorName;
        int contains = XspManage.getInstance().getGroupLocation().indexOf(elevatorName);
        if (contains > -1) {
            List<ElevatorBean.DataBean.ElevatorListBean.ScreenListBean> screenListBeans = XspManage.getInstance().getChildXsp().get(contains);
            for (int i = 0; i < childList.size(); i++) {
                List<ElevatorBean.DataBean.ElevatorListBean.ScreenListBean> beans = childList.get(i);
                for (int j = 0; j < beans.size(); j++) {
                    int index = beans.get(j).beIndexOf(screenListBeans);
                    Log.e("TAG", "setList: 11 -" + index);
                    if (index > -1) {
                        handleSelectStatue(i, j);
                    }
                }
            }
        }
        onSelectStatueListener.selectStatue(isSelectAll() == 1);
    }

    /**
     * 更新数据
     * 根据暂存数据
     */
    public void updateForMangeXsp() {
        int contains = XspManage.getInstance().getGroupLocation().indexOf(elevatorName);
        if (contains > -1) {
            boolean change = false;
            List<ElevatorBean.DataBean.ElevatorListBean.ScreenListBean> screenListBeans = XspManage.getInstance().getChildXsp().get(contains);
            Set<Map.Entry<Integer, List<Integer>>> entries = mSelectList.entrySet();
            Iterator<Map.Entry<Integer, List<Integer>>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<Integer, List<Integer>> next = iterator.next();
                Integer key = next.getKey();
                List<Integer> value = next.getValue();
                List<ElevatorBean.DataBean.ElevatorListBean.ScreenListBean> screenChildList = mChildList.get(key);
                Iterator<Integer> it = value.iterator();
                while (it.hasNext()) {
                    Integer next1 = it.next();
                    if (screenChildList.get(next1).beIndexOf(screenListBeans) == -1) {
                        it.remove();
                        change = true;
                    }
                }
            }
            if (change) {
                notifyDataSetChanged();
                onSelectStatueListener.selectStatue(isSelectAll() == 1);
            }
        }else{
            mSelectList.clear();
            notifyDataSetChanged();
            onSelectStatueListener.selectStatue(false);
        }
    }

    @Override
    public int getGroupCount() {
        return mGroupList == null ? 0 : mGroupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildList == null ? 0 : mChildList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mChildList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupViewHolder groupViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_elevator_group_item, parent, false);
            groupViewHolder = new GroupViewHolder();
            groupViewHolder.iv = convertView.findViewById(R.id.iv);
            groupViewHolder.tv_elevator_name = convertView.findViewById(R.id.tv_elevator_name);
            groupViewHolder.tv_elevator_num = convertView.findViewById(R.id.tv_elevator_num);
            groupViewHolder.iv_show = convertView.findViewById(R.id.iv_show);
            convertView.setTag(groupViewHolder);
        } else {
            groupViewHolder = (GroupViewHolder) convertView.getTag();
        }
        groupViewHolder.iv.setTag(groupPosition);
        groupViewHolder.iv.setOnClickListener(groupClick);
        groupViewHolder.iv.setImageResource(getGroupIVResource(groupPosition));
        groupViewHolder.iv_show.setImageResource(!isExpanded ? R.mipmap.showxia : R.mipmap.unshowshang);
        groupViewHolder.tv_elevator_name.setText(mGroupList.get(groupPosition).getEleName());
        groupViewHolder.tv_elevator_num.setText(mGroupList.get(groupPosition).getSize() + "面屏幕");
        return convertView;
    }

    private int getGroupIVResource(int groupPosition) {
        List<Integer> integers = mSelectList.get(groupPosition);
        if (TextUtils.isNotEmptyList(integers)) {
            if (integers.size() == mGroupList.get(groupPosition).getSize()) {
                return R.mipmap.select;
            }
        }
        return R.mipmap.unselect;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder childViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_elevator_child_item, parent, false);
            childViewHolder = new ChildViewHolder();
            childViewHolder.iv_select = convertView.findViewById(R.id.iv_select);
            childViewHolder.iv = convertView.findViewById(R.id.iv);
            childViewHolder.tv_xsp_name = convertView.findViewById(R.id.tv_xsp_name);
            childViewHolder.tv_xsp_price = convertView.findViewById(R.id.tv_xsp_price);
            convertView.setTag(childViewHolder);
        } else {
            childViewHolder = (ChildViewHolder) convertView.getTag();
        }
        convertView.setTag(R.id.group_position, groupPosition);
        convertView.setTag(R.id.child_position, childPosition);
        convertView.setOnClickListener(childClick);
        childViewHolder.iv_select.setImageResource(getChildIVResource(groupPosition, childPosition));
        //Log.e("TAG", "getChildView: " + groupPosition + ", " + childPosition);
        ElevatorBean.DataBean.ElevatorListBean.ScreenListBean screenListBean = mChildList.get(groupPosition).get(childPosition);
        setChildIVResource(screenListBean.getScreenName(), childViewHolder.iv, childViewHolder.tv_xsp_name);
        //float discount = screenListBean.getDiscount();
        childViewHolder.tv_xsp_price.setText(CalculateUtils.m1(screenListBean.getDiscountPrice())+" 元/小时");
        return convertView;
    }

    private int getChildIVResource(int groupPosition, int childPosition) {
        List<Integer> integers = mSelectList.get(groupPosition);
        if (TextUtils.isNotEmptyList(integers)) {
            if (integers.contains(childPosition)) {
                return R.mipmap.select;
            }
        }
        return R.mipmap.unselect;
    }

    private void setChildIVResource(String s, ImageView iv, TextView tv) {
        int resource;
        String s1;
        if (s.contains("A")) {
            resource = R.mipmap.xsp_a;
            s1 = "（左侧）";
        } else if (s.contains("B")) {
            resource = R.mipmap.xsp_b;
            s1 = "（右侧）";
        } else {
            resource = R.mipmap.xsp_c;
            s1 = "（中间）";
        }
        iv.setImageResource(resource);
        tv.setText(s + s1);
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder {
        ImageView iv;
        TextView tv_elevator_name;
        TextView tv_elevator_num;
        ImageView iv_show;
    }

    class ChildViewHolder {
        ImageView iv_select;
        ImageView iv;
        TextView tv_xsp_name;
        TextView tv_xsp_price;
        //DeleteLineTextView tv_xsp_old_price;
    }

    private View.OnClickListener groupClick = v -> {
        int groupPosition = (int) v.getTag();
        List<Integer> integers = mSelectList.get(groupPosition);

        if (TextUtils.isNotEmptyList(integers) && mGroupList.get(groupPosition).getSize() == integers.size()) {
            mSelectList.remove(groupPosition);
        } else {
            integers = new ArrayList<>();
            for (int i = 0; i < mChildList.get(groupPosition).size(); i++) {
                integers.add(i);
            }
            mSelectList.put(groupPosition, integers);
        }
        notifyDataSetChanged();
        onSelectStatueListener.selectStatue(isSelectAll() == 1);
    };
    private View.OnClickListener childClick = v -> {
        int groupPosition = (int) v.getTag(R.id.group_position);
        int childPosition = (int) v.getTag(R.id.child_position);
        handleSelectStatue(groupPosition, childPosition);
        notifyDataSetChanged();
        onSelectStatueListener.selectStatue(isSelectAll() == 1);
    };

    private void handleSelectStatue(int groupPosition, int childPosition) {
        Log.e("TAG", "handleSelectStatue: " + groupPosition + ", " + childPosition);
        List<Integer> integers = mSelectList.get(groupPosition);
        if (null == integers) {
            integers = new ArrayList<>();
            integers.add(childPosition);
            mSelectList.put(groupPosition, integers);
        } else {
            Integer o = childPosition;
            if (integers.contains(o)) {
                integers.remove(o);
            } else {
                integers.add(o);
            }
        }
    }

    public void selectAll() {
        int selectAll = isSelectAll();
        //Log.e("TAG", "selectAll: " + selectAll);
        if (selectAll != 1) {
            //充满
            for (int i = 0; i < mGroupList.size(); i++) {
                int childSize = mGroupList.get(i).getSize();
                ArrayList<Integer> arrayList = new ArrayList<>(childSize);
                for (int j = 0; j < childSize; j++) {
                    arrayList.add(j);
                }
                mSelectList.put(i, arrayList);
            }
            onSelectStatueListener.selectStatue(true);
        } else {
            //清空
            mSelectList.clear();
            onSelectStatueListener.selectStatue(false);
        }
        notifyDataSetChanged();
    }

    private int isSelectAll() {
        boolean emptySize = true;
        boolean fullSize = true;
        for (int i = 0; i < mGroupList.size(); i++) {
            List<Integer> integers = mSelectList.get(i);
            if (!TextUtils.isNotEmptyList(integers)) {//空
                fullSize = false;
            } else {
                emptySize = false;
                if (integers.size() != mGroupList.get(i).getSize()) {
                    fullSize = false;
                }
            }
        }
        if (emptySize) {
            return 0;
        } else if (fullSize) {
            return 1;
        } else {
            return 2;
        }
    }

    public void setOnSelectStatueListener(OnSelectStatueListener onSelectStatueListener) {
        this.onSelectStatueListener = onSelectStatueListener;
    }

    public interface OnSelectStatueListener {
        void selectStatue(boolean isFull);
    }

    public void setXspManageData() {
        //Flatmap
        List<String> list = XspManage.getInstance().getGroupLocation();
        int index = list.indexOf(elevatorName);
        if (!mSelectList.isEmpty()) {
            Set<Map.Entry<Integer, List<Integer>>> entries = mSelectList.entrySet();
            Iterator<Map.Entry<Integer, List<Integer>>> iterator = entries.iterator();
            List<ElevatorBean.DataBean.ElevatorListBean.ScreenListBean> manageChildList = new ArrayList<>();
            while (iterator.hasNext()) {
                Map.Entry<Integer, List<Integer>> next = iterator.next();
                Integer key = next.getKey();
                List<Integer> value = next.getValue();
                List<ElevatorBean.DataBean.ElevatorListBean.ScreenListBean> screenListBeans = mChildList.get(key);
                for (int i = 0; i < screenListBeans.size(); i++) {
                    if (value.contains(i)) {
                        manageChildList.add(screenListBeans.get(i));
                    }
                }
            }
            if (manageChildList.size() > 0) {
                List<List<ElevatorBean.DataBean.ElevatorListBean.ScreenListBean>> childXsp = XspManage.getInstance().getChildXsp();
                if (index > -1) {
                    List<ElevatorBean.DataBean.ElevatorListBean.ScreenListBean> screensXsp = childXsp.get(index);//同样的小区
                    screensXsp.clear();
                    screensXsp.addAll(manageChildList);
                } else {
                    list.add(elevatorName);
                    childXsp.add(manageChildList);
                }
            }else{
                if (index > -1) {
                    XspManage.getInstance().getGroupLocation().remove(index);
                }
            }
        } else {
            if (index > -1) {
                XspManage.getInstance().getGroupLocation().remove(index);
            }
        }
    }
}
