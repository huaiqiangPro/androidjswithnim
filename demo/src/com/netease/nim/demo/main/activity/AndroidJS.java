package com.netease.nim.demo.main.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import com.jrmf360.normallib.base.utils.ToastUtil;
import com.netease.nim.avchatkit.AVChatKit;
import com.netease.nim.avchatkit.activity.AVChatActivity;
import com.netease.nim.demo.DemoCache;
import com.netease.nim.demo.R;
import com.netease.nim.demo.config.preference.Preferences;
import com.netease.nim.demo.config.preference.UserPreferences;
import com.netease.nim.demo.contact.constant.UserConstant;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.ToastHelper;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.util.string.MD5;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusBarNotificationConfig;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.avchat.constant.AVChatType;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

public class AndroidJS {
    Context mContext;
    WebView mWebView;
    private AbortableFuture<LoginInfo> loginRequest;


    public AndroidJS(Context c) {
        mContext = c;
    }
    public AndroidJS(Context c,WebView webView) {
        mContext = c;
        mWebView=webView;
    }

    private static String readAppKey(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                return appInfo.metaData.getString("com.netease.nim.appKey");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 与js交互时用到的方法，在js里直接调用的
     */
    @JavascriptInterface
    public void outgoingCall(String account) {


        if (!TextUtils.isEmpty(account)) {

            AVChatKit.outgoingCall(mContext, account, UserInfoHelper.getUserDisplayName(account), AVChatType.VIDEO.getValue(), AVChatActivity.FROM_INTERNAL);
        } else {
            ToastUtil.showLongToast(mContext, "请输入需要呼叫的account");
        }

    }

    @JavascriptInterface
    public void toast(String account) {


        ToastUtil.showLongToast(mContext, "传过来的参数  ------" + account);

    }

    @JavascriptInterface
    public void setVideoFlag(boolean videoFlag) {


        UserConstant.videoFlag = videoFlag;
        //ToastUtil.showLongToast(mContext, "open video  ------" + videoFlag);

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @JavascriptInterface
    public void snapShot() {

        RxPermissions rxPermission = new RxPermissions((Activity) mContext);
        rxPermission
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            try {

//                                Bitmap bitmap = captureWebView(mWebView);
//                                ImageHelper.saveBitmapToSDCard(mContext, bitmap);
                                ((Activity)mContext).runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Bitmap bitmap = captureWebView(mWebView);
                                        ImageHelper.saveBitmapToSDCard(mContext, bitmap);
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e("ghq",  " Exception:"+e.getMessage());
                            }
                            Log.d("ghq", permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时。还会提示请求权限的对话框
                            Log.d("ghq", permission.name + " is denied. More info should be provided.");
                        } else {
                            ToastUtil.showLongToast(mContext, "权限未开启，请到设置中开启");
                            // 用户拒绝了该权限，而且选中『不再询问』
                            Log.d("ghq", permission.name + " is denied.");
                        }
                    }
                });
    }

    @JavascriptInterface
    public void login(final String account, final String tokenn) {

        DialogMaker.showProgressDialog(mContext, null, "登录中", true, new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (loginRequest != null) {
                    loginRequest.abort();
                    onLoginDone();
                }
            }
        }).setCanceledOnTouchOutside(false);

        // 云信只提供消息通道，并不包含用户资料逻辑。开发者需要在管理后台或通过服务器接口将用户帐号和token同步到云信服务器。
        // 在这里直接使用同步到云信服务器的帐号和token登录。
        // 这里为了简便起见，demo就直接使用了密码的md5作为token。
        // 如果开发者直接使用这个demo，只更改appkey，然后就登入自己的账户体系的话，需要传入同步到云信服务器的token，而不是用户密码。
        // 登录
        final String token = tokenFromPassword(tokenn);

        loginRequest = NimUIKit.login(new LoginInfo(account, token), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                //ToastUtil.showLongToast(mContext, " 登录成功 : account = " + account + "; token=" + token);
                Log.e("login", " 登录成功 : account = " + account + "; token=" + token);
                onLoginDone();
                DemoCache.setAccount(account);
                saveLoginInfo(account, token);
                // 初始化消息提醒配置
                initNotificationConfig();
                // 进入主界面
                //HomeActivity.start(mContext, null);
                //WebViewActivity.start(mContext, "http://www.qq.com");

                //finish();
            }

            @Override
            public void onFailed(int code) {
                onLoginDone();
                if (code == 302 || code == 404) {
                    Log.e("login", "帐号或密码错误");

                    //ToastHelper.showToast(mContext, "帐号或密码错误");
                } else {
                    Log.e("login", "登录失败: " + code);

                    //ToastHelper.showToast(mContext, "登录失败: " + code);
                }
            }

            @Override
            public void onException(Throwable exception) {
                ToastHelper.showToast(mContext, R.string.login_exception);
                onLoginDone();
            }
        });
    }

    private void onLoginDone() {
        loginRequest = null;
        DialogMaker.dismissProgressDialog();
    }

    private void saveLoginInfo(final String account, final String token) {
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
    }

    private void initNotificationConfig() {
        // 初始化消息提醒
        NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
        // 加载状态栏配置
        StatusBarNotificationConfig statusBarNotificationConfig = UserPreferences.getStatusConfig();
        if (statusBarNotificationConfig == null) {
            statusBarNotificationConfig = DemoCache.getNotificationConfig();
            UserPreferences.setStatusConfig(statusBarNotificationConfig);
        }
        // 更新配置
        NIMClient.updateStatusBarNotificationConfig(statusBarNotificationConfig);
    }

    //DEMO中使用 username 作为 NIM 的account ，md5(password) 作为 token
    //开发者需要根据自己的实际情况配置自身用户系统和 NIM 用户系统的关系
    private String tokenFromPassword(String password) {
        String appKey = readAppKey(mContext);
        boolean isDemo = "45c6af3c98409b18a84451215d0bdd6e".equals(appKey) ||
                "fe416640c8e8a72734219e1847ad2547".equals(appKey);

        return isDemo ? MD5.getStringMD5(password) : password;
    }


    private  Bitmap captureWebView(WebView webView) {

        float scale = webView.getScale();
        int width = webView.getWidth();
        int height = (int) (webView.getContentHeight() * scale + 0.5);
        Log.e("ghq","页面的高度："+height+"");
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        webView.draw(canvas);
        return bitmap;
    }

    public Bitmap captureWebView0(final View view) {
        if (view == null)
            return null;
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(measureSpec, measureSpec);
        if (view.getMeasuredWidth() <= 0 || view.getMeasuredHeight() <= 0) {
            return null;
        }
        Bitmap bm;
        try {
            bm = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.RGB_565);
        } catch (OutOfMemoryError e) {
            System.gc();
            try {
                bm = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(), Bitmap.Config.RGB_565);
            } catch (OutOfMemoryError ee) {
                return null;
            }
        }
        Canvas bigCanvas = new Canvas(bm);
        Paint paint = new Paint();
        int iHeight = bm.getHeight();
        bigCanvas.drawBitmap(bm, 0, iHeight, paint);
        view.draw(bigCanvas);
        //showToast(getString(R.string.already_share_save_img));
        return bm;
    }



}