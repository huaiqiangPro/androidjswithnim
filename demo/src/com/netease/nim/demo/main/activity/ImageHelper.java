package com.netease.nim.demo.main.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import com.jrmf360.normallib.base.utils.ToastUtil;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class ImageHelper {

    private static final String prefix = "ddky";

    private static String generateMediaName(MediaType type) {
        return prefix + "_" + getPictureTime() + "." + type.getName();
    }

    private static String getMediaPath() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                + File.separator;
    }

    private static String getPictureTime() {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
        return format.format(date);
    }

    /**
     * 创建bitmap
     *
     * @param v
     * @return
     */
    public static Bitmap createBitmapFromView(View v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        v.draw(canvas);
        return bitmap;
    }

    public static Bitmap createBitmapFromView(Picture v) {
        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);

        v.draw(canvas);
        return bitmap;
    }

    /**
     * 保存图片到相册
     *
     * @param context
     * @param bitmap
     */
    public static void saveBitmapToSDCard(Context context, Bitmap bitmap) {

        if (bitmap != null) {

            File file = new File(getMediaPath(), generateMediaName(MediaType.JPG));

            BitmapUtil.saveBitmapToSDCard(bitmap, file);

            context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(file)));

            ToastUtil.showLongToast(context,"已保存到手机");

        }
    }


}
