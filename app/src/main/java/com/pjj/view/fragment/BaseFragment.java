package com.pjj.view.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pjj.R;
import com.pjj.PjjApplication;
import com.pjj.present.BasePresent;
import com.pjj.utils.SharedUtils;
import com.pjj.view.activity.LoginActivity;
import com.pjj.view.dialog.NoticeDialog;
import com.pjj.view.dialog.WaiteDialog;

import java.util.Objects;

/**
 * Create by xinheng on 2018/11/07。
 * describe：
 */
public abstract class BaseFragment<P extends BasePresent> extends Fragment {
    protected P mPresent;
    private Toast mToast;
    protected WaiteDialog waiteDialog;
    protected NoticeDialog noticeDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = createView();
        if (null == view) {
            view = inflater.inflate(getLayoutRes(), container, false);
        }
        waiteDialog = new WaiteDialog(getActivity(), 0);
        noticeDialog = new NoticeDialog(getActivity(), R.mipmap.cry_white, 0);
        if (isHeadPaddingStatue()) {
            View view_top = view.findViewById(R.id.view_top);
            if (null != view_top) {
                Resources resources = getActivity().getResources();
                int identifier = resources.getIdentifier("status_bar_height", "dimen", "android");
                int topStatueHeight = resources.getDimensionPixelSize(identifier);
                ViewGroup.LayoutParams layoutParams = view_top.getLayoutParams();
                if (null != layoutParams && topStatueHeight > 0) {
                    layoutParams.height = topStatueHeight;
                    view_top.setLayoutParams(layoutParams);
                }
            }
        }

        return view;
    }

    abstract protected int getLayoutRes();

    /**
     * 顶部标题添加顶部状态栏高度的paddingTop
     *
     * @return
     */
    protected boolean isHeadPaddingStatue() {
        return false;
    }

    protected View createView() {
        return null;
    }

    protected void toast(String msg) {
        if (null == msg) {
            msg = " msg = null";
        }
        if (msg.contains("用户验证不通过")) {
            cancelWaiteStatue();
            SharedUtils.saveForXml(SharedUtils.USER_ID, "");
            SharedUtils.saveForXml(SharedUtils.TOKEN, "");
            Toast mToast = Toast.makeText(PjjApplication.application, "登录已过期，请重新登录", Toast.LENGTH_LONG);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.show();
            Intent intent = new Intent(getActivity(), LoginActivity.class).putExtra("time_out", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
            return;
        }
        if (mToast == null) {
            mToast = Toast.makeText(PjjApplication.application, msg, Toast.LENGTH_SHORT);
            mToast.setGravity(Gravity.CENTER, 0, 0);
        } else {
            mToast.setText(msg);
        }
        mToast.show();
    }

    public void showWaiteStatue() {
        waiteDialog.show();
    }

    public void cancelWaiteStatue() {
        FragmentActivity activity = getActivity();
        if (null != activity && !activity.isFinishing() && waiteDialog.isShowing()) {
            waiteDialog.dismiss();
        }
    }

    public void showNotice(String msg) {
        cancelWaiteStatue();
        if (null != msg && msg.contains("用户验证不通过")) {
            cancelWaiteStatue();
            SharedUtils.saveForXml(SharedUtils.USER_ID, "");
            SharedUtils.saveForXml(SharedUtils.USER_PHONE, "");
            SharedUtils.saveForXml(SharedUtils.TOKEN, "");
            Toast mToast = Toast.makeText(PjjApplication.application, "登录已过期，请重新登录", Toast.LENGTH_LONG);
            mToast.setGravity(Gravity.CENTER, 0, 0);
            mToast.show();
            Intent intent = new Intent(getActivity(), LoginActivity.class).putExtra("time_out", true);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            getActivity().finish();
            return;
        }

        if (null == msg) {
            //msg = " msg = null";
            msg = "失败-000";
        } else if (msg.contains("<html>")) {
            msg = "服务器开小差了";
        }
        noticeDialog.setNotice(msg, 2000);
    }

    @Override
    public void onPause() {
        if (null != mToast) {
            mToast.cancel();
        }
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {//隐藏
            if (null != mToast) {
                mToast.cancel();
            }
        }
        super.onHiddenChanged(hidden);
    }

    @Override
    public void onDetach() {
        if (null != mPresent) {
            mPresent.recycle();
            mPresent = null;
        }
        cancelWaiteStatue();
        if (noticeDialog.isShowing())
            noticeDialog.dismiss();
        mToast = null;
        super.onDetach();
    }
}
