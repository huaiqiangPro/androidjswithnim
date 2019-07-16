package com.netease.nim.demo.main.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {
    /**
     * ���ر���ͼƬ
     *
     * @param
     * @return
     */
    public static Bitmap getBitmapFromSDCard(String path) {
        try {
            if (path != null) {
                return BitmapFactory.decodeFile(path);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // �����ڲ�ͬ����ֱ�Ӵ���ͼƬ
    public static Bitmap temp;

    // ͼƬ����
    public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) width / w);
        float scaleHeight = ((float) height / h);
        matrix.postScale(scaleWidth, scaleHeight);


        Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
        return newbmp;
    }


    //使用Bitmap加Matrix来缩放
    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width <= w && height <= h)
            return (bitmap);
        // 计算图片缩放比例
        float scale;
        final float sx = w / (float) width;
        final float sy = h / (float) height;
        if (sx > sy) {
            scale = sy;
        } else {
            scale = sx;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap bit = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return bit;
    }

    /**
     * 图片缩放
     *
     * @param bitmap 对象
     * @param w      最大宽
     * @param h      最大高
     * @return 新Bitmap对象
     */
    public static Bitmap approximateZoomBitmap(Bitmap bitmap, int w, int h) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        if (width <= w && height <= h)
            return (bitmap);
        // 计算图片缩放比例
        float scale;
        final float sx = w / (float) width;
        final float sy = h / (float) height;
        if (sx <= sy) {
            scale = sy;
        } else {
            scale = sx;
        }
        Matrix matrix = new Matrix();
        matrix.postScale(scale, scale);
        Bitmap bit = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        try {
            if (null != bitmap) {
                bitmap.recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bitmap = null;
        }
        return bit;
    }

    /**  android中保存Bitmap图片到指定文件夹中的方法 */
    public static void saveBitmapToSDCard(Bitmap bitmap, String path) {
        File file = new File(path);
        saveBitmapToSDCard(bitmap, file);
    }

    public static void saveBitmapToSDCard(Bitmap bitmap, File file) {
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("----------save success-------------------");
    }

        /**
         * 按照高度的百分比压缩
         *
         * @param srcBitmap
         * @param newHeight
         * @return
         */
    public static Bitmap bitmapZoomByHeight(Bitmap srcBitmap, float newHeight) {
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();

        float scaleHeight = newHeight / srcHeight;
        float scaleWidth = scaleHeight;

        return bitmapZoomByScale(srcBitmap, scaleWidth, scaleHeight);

    }

    /**
     * 按照高度的百分比压缩
     *
     * @param drawable
     * @param newHeight
     * @return
     */
    public static Bitmap bitmapZoomByHeight(Drawable drawable, float newHeight) {
        Bitmap srcBitmap = drawableToBitmap(drawable);
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();

        float scaleHeight = newHeight / srcHeight;
        float scaleWidth = scaleHeight;

        return bitmapZoomByScale(srcBitmap, scaleWidth, scaleHeight);

    }

    /**
     * 使用长宽缩放比缩放
     *
     * @param srcBitmap
     * @param scaleWidth
     * @param scaleHeight
     * @return
     */
    public static Bitmap bitmapZoomByScale(Bitmap srcBitmap, float scaleWidth, float scaleHeight) {
        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        Matrix matrix = new Matrix();

        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcWidth,
                srcHeight, matrix, true);
        if (resizedBitmap != null) {
            srcBitmap = null;
            return resizedBitmap;
        } else {
            return srcBitmap;
        }
    }

    /**
     * Drawable转化为Bitmap
     */
    public static Bitmap drawableToBitmap(Drawable drawable) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(width, height, drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        drawable.draw(canvas);
        return bitmap;

    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {

        return bmpToByteArray(bmp, needRecycle, 100);
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle, int size) {

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, size, output);

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result.length > 1024 * 32) {
            size -= 10;
            return bmpToByteArray(bmp, needRecycle, (size < 0) ? 0 : size);
        }
        if (needRecycle) {
            bmp.recycle();
        }
        return result;
    }

    /**
     * 读取图像的旋转度
     */
    public static int readBitmapDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    public static Bitmap create(byte[] bytes, int maxWidth, int maxHeight) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        Bitmap bitmap;

        if (maxWidth <= 0 && maxHeight <= 0) {
            bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, option);
        } else {
            option.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bytes, 0, bytes.length, option);
            int actualWidth = option.outWidth;
            int actualHeight = option.outHeight;

            // 计算出图片应该显示的宽高
            int desiredWidth = getResizedDimension(maxWidth, maxHeight, actualWidth, actualHeight);
            int desiredHeight = getResizedDimension(maxHeight, maxWidth, actualHeight, actualWidth);

            option.inJustDecodeBounds = false;
            option.inSampleSize = findBestSampleSize(actualWidth, actualHeight,
                    desiredWidth, desiredHeight);
            Bitmap tempBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, option);

            // 做缩放
            if (tempBitmap != null
                    && (tempBitmap.getWidth() > desiredWidth || tempBitmap
                    .getHeight() > desiredHeight)) {
                bitmap = Bitmap.createScaledBitmap(tempBitmap, desiredWidth,
                        desiredHeight, true);
                tempBitmap.recycle();
            } else {
                bitmap = tempBitmap;
            }
        }

        return bitmap;
    }

    static int findBestSampleSize(int actualWidth, int actualHeight,
                                  int desiredWidth, int desiredHeight) {
        double wr = (double) actualWidth / desiredWidth;
        double hr = (double) actualHeight / desiredHeight;
        double ratio = Math.min(wr, hr);
        float n = 1.0f;
        while ((n * 2) <= ratio) {
            n *= 2;
        }
        return (int) n;
    }

    private static int getResizedDimension(int maxPrimary, int maxSecondary,
                                           int actualPrimary, int actualSecondary) {
        if (maxPrimary == 0 && maxSecondary == 0) {
            return actualPrimary;
        }
        if (maxPrimary == 0) {
            double ratio = (double) maxSecondary / (double) actualSecondary;
            return (int) (actualPrimary * ratio);
        }

        if (maxSecondary == 0) {
            return maxPrimary;
        }

        double ratio = (double) actualSecondary / (double) actualPrimary;
        int resized = maxPrimary;
        if (resized * ratio > maxSecondary) {
            resized = (int) (maxSecondary / ratio);
        }
        return resized;
    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param bitmap
     * @return
     */
    public static Bitmap rotateBitmap(int angle, Bitmap bitmap) {
        // 旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        return resizedBitmap;
    }

    /**
     * 照片转byte二进制
     * @param imagepath 需要转byte的照片路径
     * @return 已经转成的byte
     * @throws Exception
     */
    public static byte[] readStream(String imagepath) throws Exception {
        FileInputStream fs = new FileInputStream(imagepath);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while (-1 != (len = fs.read(buffer))) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        fs.close();
        return outStream.toByteArray();
    }

    /**
     * 在二维码中间添加Logo图案
     */
    public static Bitmap addLogo(Bitmap src, Bitmap logo) {
        if (src == null) {
            return null;
        }

        if (logo == null) {
            return src;
        }

        //获取图片的宽高
        int srcWidth = src.getWidth();
        int srcHeight = src.getHeight();
        int logoWidth = logo.getWidth();
        int logoHeight = logo.getHeight();

        if (srcWidth == 0 || srcHeight == 0) {
            return null;
        }

        if (logoWidth == 0 || logoHeight == 0) {
            return src;
        }

        //logo大小为二维码整体大小的1/5
        float scaleFactor = srcWidth * 1.0f / 5 / logoWidth;
        Bitmap bitmap = Bitmap.createBitmap(srcWidth, srcHeight, Bitmap.Config.ARGB_8888);
        try {
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(src, 0, 0, null);
            canvas.scale(scaleFactor, scaleFactor, srcWidth / 2, srcHeight / 2);
            canvas.drawBitmap(logo, (srcWidth - logoWidth) / 2, (srcHeight - logoHeight) / 2, null);

            canvas.save(Canvas.ALL_SAVE_FLAG);
            canvas.restore();
        } catch (Exception e) {
            bitmap = null;
            e.getStackTrace();
        }

        return bitmap;
    }

}
