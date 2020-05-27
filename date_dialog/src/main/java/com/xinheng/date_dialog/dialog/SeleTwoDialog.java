package com.xinheng.date_dialog.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.xinheng.date_dialog.R;
import com.xinheng.date_dialog.view.WheelView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xinheng on 2018/6/3.<br/>
 * describe：两条选项弹窗
 */
public class SeleTwoDialog extends BaseDialog{
    private View dialogView;
    private WheelView leftWheel;
    private WheelView rightWheel;
    private OnClickListener onClickListener;
    private List<String> leftList;
    private List<String> rightList;
    private String leftS,rightS;
    private int leftIndex,rightIndex;
    boolean cancelable = true;
    public SeleTwoDialog(Context mContext, OnClickListener listener,
                         List<String> left,List<String> right) {
        this.context = mContext;
        onClickListener = listener;
        this.leftList=left;
        this.rightList=right;
        create();
    }
    /**
     * 创建选择时间对话框
     */
    private void create() {

        if (dialog != null) {
            return;
        }

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        dialogView = layoutInflater.inflate(R.layout.dialog_wheel_select_two, null);
        leftWheel = (WheelView) dialogView.findViewById(R.id.select_time_wheel_left);
        rightWheel = (WheelView) dialogView.findViewById(R.id.select_time_wheel_right);
        if(leftList!=null&&leftList.size()>0) {
            leftWheel.setWheelItemList(leftList);
            leftWheel.setOnSelectListener(new WheelView.SelectListener() {
                @Override
                public void onSelect(int index, String text) {
                    leftS = text;
                    leftIndex = index;
                }
            });
            leftS=leftList.get(0);
        }else{
            leftList=new ArrayList<>();
            leftWheel.setWheelItemList(leftList);
            leftWheel.setVisibility(View.GONE);
        }
        if(rightList!=null&&rightList.size()>0) {
            rightWheel.setWheelItemList(rightList);
            rightWheel.setOnSelectListener(new WheelView.SelectListener() {
                @Override
                public void onSelect(int index, String text) {
                    rightS = text;
                    rightIndex = index;
                }
            });
            rightS=rightList.get(0);
        }else{
            rightList=new ArrayList<>();
            rightWheel.setWheelItemList(rightList);
            rightWheel.setVisibility(View.GONE);
        }
        dialog = new AlertDialog.Builder(context).setView(dialogView).create();

        dialog.setCancelable(cancelable);

        Button cancelBtn = (Button) dialogView.findViewById(R.id.select_time_cancel_btn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    if (!onClickListener.onCancel()) {
                        dialog.dismiss();
                    }
                } else {
                    dialog.dismiss();
                }
            }
        });
        Button sureBtn = (Button) dialogView.findViewById(R.id.select_time_sure_btn);
        sureBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    if (!onClickListener.onSure(leftS,leftIndex,rightS,rightIndex)) {
                        dialog.dismiss();
                    }
                } else {
                    dialog.dismiss();
                }

            }
        });

    }
    /**
     * 显示对话框
     * @param leftIndex  默认显示
     * @param rightIndex 默认显示
     */
    public void show(int leftIndex, int rightIndex) {
        if (dialog == null || dialog.isShowing()) {
            return;
        }
        leftWheel.setCurrentItem(leftIndex);
        rightWheel.setCurrentItem(rightIndex);
        dialog.show();
    }
    public void setOnUpdateTimeListener(OnClickListener listener) {
        onClickListener = listener;
    }

    /**
     * 选择时间对话框回调接口，调用者实现
     *
     * @author huangzj
     */
    public interface OnClickListener {
        boolean onSure(String left,int leftIndex, String right,int rightIndex);

        boolean onCancel();
    }
}
