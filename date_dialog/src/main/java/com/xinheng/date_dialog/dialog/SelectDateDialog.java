package com.xinheng.date_dialog.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;


import com.xinheng.date_dialog.R;
import com.xinheng.date_dialog.view.WheelStyle;
import com.xinheng.date_dialog.view.WheelView;

import org.w3c.dom.ProcessingInstruction;

import java.util.Calendar;
import java.util.Date;

/**
 * 日期选择对话框
 * <p/>
 * Created by huangzj on 2015/10/25.
 */
public class SelectDateDialog extends BaseDialog {
    public static final byte YEAR_MONTH_DAY=0;
    public static final byte YEAR_MONTH=1;
    private int dateType;
    private View dialogView;
    private WheelView yearWheel;
    private WheelView monthWheel;
    private WheelView dayWheel;

    int selectYear;
    int selectMonth;

    private OnClickListener onClickListener;

    /**
     * 创建一个日期选择对话框
     *
     * @param mContext
     */
    public SelectDateDialog(Context mContext) {
        this.context = mContext;
        create();
    }

    /**
     * 创建一个日期选择对话框
     *
     * @param mContext
     */
    public SelectDateDialog(Context mContext, OnClickListener listener) {
        this.context = mContext;
        onClickListener = listener;
        create();
    }

    /**
     * 创建选择日期对话框
     */
    private void create() {

        if (dialog != null) {
            return;
        }

        LayoutInflater layoutInflater = LayoutInflater.from(context);
        dialogView = layoutInflater.inflate(R.layout.dialog_wheel_select_date, null);
        yearWheel = (WheelView) dialogView.findViewById(R.id.select_date_wheel_year_wheel);
        monthWheel = (WheelView) dialogView.findViewById(R.id.select_date_month_wheel);
        dayWheel = (WheelView) dialogView.findViewById(R.id.select_date_day_wheel);
        yearWheel.setWheelStyle(WheelStyle.STYLE_YEAR);//[min,max]
        yearWheel.setOnSelectListener(new WheelView.SelectListener() {
            @Override
            public void onSelect(int index, String text) {
                selectYear = index + WheelStyle.minYear;
                dayWheel.setWheelItemList(WheelStyle.createDayString(selectYear, selectMonth));
            }
        });

        monthWheel.setWheelStyle(WheelStyle.STYLE_MONTH);//[1,12]
        monthWheel.setOnSelectListener(new WheelView.SelectListener() {
            @Override
            public void onSelect(int index, String text) {
                selectMonth = index + 1;
                dayWheel.setWheelItemList(WheelStyle.createDayString(selectYear, selectMonth));
            }
        });

        Button cancelBt = (Button) dialogView.findViewById(R.id.select_date_cancel);
        cancelBt.setOnClickListener(new View.OnClickListener() {

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
        Button sureBt = (Button) dialogView.findViewById(R.id.select_date_sure);
        sureBt.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int year = yearWheel.getCurrentItem() + WheelStyle.minYear;
                int month = monthWheel.getCurrentItem()+1;
                int day = dayWheel.getCurrentItem() + 1;
                int daySize = dayWheel.getItemCount();
                if (day > daySize) {
                    day = day - daySize;
                }
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month-1);
                calendar.set(Calendar.DATE, day);
                //long setTime = calendar.getTimeInMillis();
//                int month1 = new Date(setTime).getMonth();
//                int month2 = calendar.get(Calendar.MONTH);
//                Log.e("TAG", "onClick: "+month1+", "+month2 );
                if (onClickListener != null) {
                    if (!onClickListener.onSure(year, month, day, 0)) {
                        dialog.dismiss();
                    }
                } else {
                    dialog.dismiss();
                }
            }
        });
        dialog = new AlertDialog.Builder(context).setView(dialogView).create();


    }

    public void setDateType(byte dateType) {
        if(dateType>YEAR_MONTH){
            dateType=YEAR_MONTH;
        }else if(dateType<YEAR_MONTH_DAY){
            dateType=YEAR_MONTH_DAY;
        }
        this.dateType = dateType;
        if(dateType==YEAR_MONTH)
            dayWheel.setVisibility(View.GONE);
    }

    /**
     * 显示选择日期对话框
     *
     * @param year  默认显示的年
     * @param month 默认月
     * @param day   默认日
     */
    public void show(int year, int month, int day) {
        if (dialog == null || dialog.isShowing()) {
            return;
        }
        dayWheel.setWheelItemList(WheelStyle.createDayString(year /*- WheelStyle.minYear*/, month));
        yearWheel.setCurrentItem(year - WheelStyle.minYear);
        monthWheel.setCurrentItem(month-1);
        //dayWheel.setCurrentItem(day - 1);
        dialog.show();
    }


    /**
     * 选择日期对话框回调
     *
     * @param listener
     */
    public void setOnClickListener(OnClickListener listener) {
        onClickListener = listener;
    }

    /**
     * 选择日期对话框回调接口，调用者实现
     *
     * @author huangzj
     */
    public interface OnClickListener {
        boolean onSure(int year, int month, int day, long time);

        boolean onCancel();
    }
}
