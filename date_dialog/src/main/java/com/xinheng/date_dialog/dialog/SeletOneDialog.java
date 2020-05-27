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
public class SeletOneDialog extends BaseDialog{
    private View dialogView;
    private WheelView leftWheel;
    private OnClickListener onClickListener;
    private List<String> leftList;
    private String leftS;
    private int leftIndex;
    boolean cancelable = true;
    public SeletOneDialog(Context mContext, OnClickListener listener,
                          List<String> left) {
        this.context = mContext;
        onClickListener = listener;
        this.leftList=left;
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
        WheelView rightWheel = (WheelView) dialogView.findViewById(R.id.select_time_wheel_right);
        rightWheel.setVisibility(View.GONE);
        if(leftList!=null) {
            leftWheel.setWheelItemList(leftList);
            leftWheel.setOnSelectListener(new WheelView.SelectListener() {
                @Override
                public void onSelect(int index, String text) {
                    leftS = text;
                    leftIndex = index;
                }
            });
        }else{
            leftList=new ArrayList<>();
            leftWheel.setWheelItemList(leftList);
            leftWheel.setVisibility(View.GONE);
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
                    if (!onClickListener.onSure(leftS,leftIndex)) {
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
        boolean onSure(String left, int leftIndex);

        boolean onCancel();
    }
}
