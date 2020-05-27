package com.pjj.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.pjj.PjjApplication;
import com.pjj.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.security.MessageDigest;

import static com.bumptech.glide.load.resource.bitmap.VideoDecoder.FRAME_OPTION;

/**
 * Create by xinheng on 2018/11/12。
 * describe：
 */
public class BitmapUtils {
    public static Bitmap bitmap;

    public static void saveBitmap(Bitmap bitmap, String path) {
        BitmapUtils.bitmap = bitmap;
        File file = new File(path);
        FileUtils.createFolder(file.getParentFile());
        if (file.exists()) {
            file.getAbsoluteFile().delete();
        }
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (null != out)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);//peg支持透明
        if (file.length() > 0) {
            Toast makeText = Toast.makeText(PjjApplication.application, "保存成功", Toast.LENGTH_SHORT);
            makeText.setGravity(Gravity.CENTER, 0, 0);
            makeText.show();
        }
    }

    public static void saveBitmapThread(Bitmap bitmap, Bitmap.CompressFormat format, String path, int quality, OnSaveListener onSaveListener) {
        new Thread() {
            @Override
            public void run() {
                BitmapUtils.bitmap = bitmap;
                File file = new File(path);
                FileUtils.createFolder(file.getParentFile());
                FileOutputStream out = null;
                try {
                    out = new FileOutputStream(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (null != out)
                    bitmap.compress(format, quality, out);//peg支持透明
                if (file.length() > 1) {
                    onSaveListener.saveResult(true);
                } else {
                    onSaveListener.saveResult(false);
                }
            }
        }.start();
    }

    public static void saveBitmapThread(Bitmap bitmap, String path, OnSaveListener onSaveListener) {
        saveBitmapThread(bitmap, Bitmap.CompressFormat.PNG, path, 100, onSaveListener);
    }

    public static void loadImage(RequestManager requestManager, String path, ImageView imageView) {
        RequestOptions requestOptions = new RequestOptions()
                .error(R.mipmap.ic_launcher);
        requestManager.load(path).apply(requestOptions).into(new DrawableImageViewTarget(imageView) {

        });
    }

    /**
     * 获取视频其中一帧的图片
     *
     * @param frameTimeMicros 获取某一时间帧
     */
    @SuppressLint("CheckResult")
    public static RequestOptions getGlideRequestOptionsForVideo(long frameTimeMicros) {
        RequestOptions requestOptions = RequestOptions.frameOf(frameTimeMicros);
        requestOptions.set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST);
        requestOptions.diskCacheStrategy(DiskCacheStrategy.ALL);
        requestOptions.transform(new BitmapTransformation() {
            @Override
            protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
                return toTransform;
            }

            @Override
            public void updateDiskCacheKey(MessageDigest messageDigest) {
                try {
                    messageDigest.update((PjjApplication.application.getPackageName() + "RotateTransform").getBytes("utf-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        return requestOptions;
    }

    /**
     * 加载视频第一帧图片
     *
     * @param requestManager
     * @param path           路径
     * @param imageView
     */
    public static void loadFirstImageForVideo(RequestManager requestManager, String path, ImageView imageView) {
        requestManager.load(path).apply(getGlideRequestOptionsForVideo(1)).into(imageView);
    }

    public interface OnSaveListener {
        void saveResult(boolean tag);
    }
}