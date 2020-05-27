package com.pjj.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pjj.R;
import com.pjj.module.ElevatorBean;
import com.pjj.module.xspad.XspManage;
import com.pjj.view.custom.SwipeMenu;

import java.util.List;

/**
 * 选择模板中广告屏时间列表
 */
public class TemplateElevatorExpandableListAdapter extends BaseExpandableListAdapter {
    private List<String> mGroupLocation;
    private List<List<ElevatorBean.DataBean.ElevatorListBean.ScreenListBean>> mChildXsp;
    private boolean deleteTag;

    public void setDeleteTag(boolean deleteTag) {
        this.deleteTag = deleteTag;
    }

    public TemplateElevatorExpandableListAdapter() {
        mGroupLocation = XspManage.getInstance().getGroupLocation();
        mChildXsp = XspManage.getInstance().getChildXsp();
    }

    @Override
    public int getGroupCount() {
        return mGroupLocation == null ? 0 : mGroupLocation.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mChildXsp.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mGroupLocation.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mChildXsp.get(groupPosition).get(childPosition);
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
        GroupViewHolder holder;
        if (null == convertView) {
            holder = new GroupViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.time_select_group_item, parent, false);
            holder.tv_location = convertView.findViewById(R.id.tv_local);
            holder.tv_xsp_num = convertView.findViewById(R.id.tv_xsp_num);
            holder.tv_delete = convertView.findViewById(R.id.tv_delete);
            holder.iv_direction = convertView.findViewById(R.id.iv_direction);
            holder.rl_content = convertView.findViewById(R.id.rl_content);
            convertView.setTag(holder);
        } else {
            holder = (GroupViewHolder) convertView.getTag();
        }
        holder.tv_location.setText(mGroupLocation.get(groupPosition));
        holder.tv_xsp_num.setText("屏幕x" + getChildrenCount(groupPosition));
        holder.iv_direction.setImageResource(isExpanded ? R.mipmap.unshowshang : R.mipmap.showxia);
        holder.tv_delete.setTag(R.id.group_position, groupPosition);
        holder.rl_content.setTag(R.id.group_position, groupPosition);
        holder.tv_delete.setOnClickListener(groupDeleteClick);
        holder.rl_content.setOnClickListener(groupDeleteClick);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildViewHolder holder;
        if (null == convertView) {
            holder = new ChildViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_template_child_item, parent, false);
            holder.tv_xsp_name = convertView.findViewById(R.id.tv_xsp_name);
            holder.tv_time_inf = convertView.findViewById(R.id.tv_time_inf);
            convertView.setTag(holder);
        } else {
            holder = (ChildViewHolder) convertView.getTag();
        }
        holder.tv_xsp_name.setText(addScreenLocation(mChildXsp.get(groupPosition).get(childPosition).getScreenName()));
        holder.tv_time_inf.setTag(R.id.group_position, groupPosition);
        holder.tv_time_inf.setTag(R.id.child_position, childPosition);
        holder.tv_time_inf.setOnClickListener(childClick);
        return convertView;
    }

    private String addScreenLocation(String s) {
        if (s.contains("A")) {
            return s + "（左侧）";
        } else if (s.contains("B")) {
            return s + "（右侧）";
        } else {
            return s + "（中间）";
        }
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupViewHolder {
        TextView tv_location;
        TextView tv_xsp_num;
        ImageView iv_direction;
        TextView tv_delete;
        View rl_content;
    }

    class ChildViewHolder {
        TextView tv_xsp_name;
        TextView tv_time_inf;
    }

    private OnDeleteListener onDeleteListener;
    private View.OnClickListener groupDeleteClick = v -> {
        int id = v.getId();
        int group_position = (int) v.getTag(R.id.group_position);
        if (id == R.id.rl_content) {
            if (null != onDeleteListener) {
                onDeleteListener.expanded(group_position);
            }
        } else {
            if (group_position < mGroupLocation.size()) {
                mGroupLocation.remove(group_position);
                mChildXsp.remove(group_position);
            }
            SwipeMenu.closeMenu();
            notifyDataSetChanged();
            if (null != onDeleteListener) {
                onDeleteListener.onDelete();
            }
        }
    };
    private View.OnClickListener childClick = v -> {
        int group_position = (int) v.getTag(R.id.group_position);
        int child_position = (int) v.getTag(R.id.child_position);
        List<ElevatorBean.DataBean.ElevatorListBean.ScreenListBean> screenListBeans = mChildXsp.get(group_position);
        if (null != onDeleteListener) {
            ElevatorBean.DataBean.ElevatorListBean.ScreenListBean screenListBean = screenListBeans.get(child_position);
            String screenId = screenListBean.getScreenId();
            String screenName = screenListBean.getScreenName();
            onDeleteListener.showHoursDialog(screenName,screenId);
        }
    };

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public interface OnDeleteListener {
        void onDelete();

        void showHoursDialog(String xspName,String screenId);

        void expanded(int groupPosition);
    }
}
