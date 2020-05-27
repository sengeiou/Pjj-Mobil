package com.pjj.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.widget.Toast;

import java.io.File;

/**
 * Created by xinheng on 2018/6/25.<br/>
 * describe：app升级安装
 */
public class AppUpdate {
    public static int errorCode = 0;

    public static Boolean installApk(Context appContext, String downloadApkPath) {
        if (TextUtils.isEmpty(downloadApkPath)) {
            Toast.makeText(appContext, "APP安装文件不存在或已损坏", Toast.LENGTH_LONG).show();
            return null;
        }
        File apkFile = new File(Uri.parse(downloadApkPath).getPath());
        if (!apkFile.exists()) {
            Toast.makeText(appContext, "APP安装文件不存在或已损坏", Toast.LENGTH_LONG).show();
            return null;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {//8.0
            boolean haveInstallPermission = appContext.getPackageManager().canRequestPackageInstalls();
            if (haveInstallPermission) {//先获取是否有安装未知来源应用的权限
                install26(appContext, apkFile);
                return true;
            } else {
                Toast.makeText(appContext, "请允许安装未知来源", Toast.LENGTH_SHORT).show();
                errorCode = 1;
                //请求安装未知应用来源的权限  
                //install26(appContext, apkFile);
                //ActivityCompat.requestPermissions((Activity) appContext,new String[]{Manifest.permission.REQUEST_INSTALL_PACKAGES},203);
                return false;
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {//7.0
            Uri contentUri = FileProvider.getUriForFile(appContext, "com.pjj.fileprovider", apkFile);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        appContext.startActivity(intent);
        return true;
    }

    public static void install26(Context appContext, File apkFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri contentUri = FileProvider.getUriForFile(appContext, "com.pjj.fileprovider", apkFile);
        intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        appContext.startActivity(intent);
    }
}
