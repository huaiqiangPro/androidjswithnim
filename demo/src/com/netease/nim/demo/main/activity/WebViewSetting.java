package com.netease.nim.demo.main.activity;
import android.os.Build;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewSetting {

    public static void setCommonWebView(WebView webView) {
        if (webView != null) {


            WebSettings webSettings = webView.getSettings();
            webSettings.setUserAgentString(webSettings.getUserAgentString() + " ddky");

            //设置WebView是否允许执行JavaScript脚本，默认false，不允许
            webSettings.setJavaScriptEnabled(true);
            // 重写缓存被使用到的方法，该方法基于Navigation Type，
            // 加载普通的页面，将会检查缓存同时重新验证是否需要加载，如果不需要重新加载，将直接从缓存读取数据，
            // 允许客户端通过指定LOAD_DEFAULT、LOAD_CACHE_ELSE_NETWORK、LOAD_NO_CACHE、LOAD_CACHE_ONLY其中之一重写该行为方法，
            // 默认值LOAD_DEFAULT
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            // 设置WebView是否使用预览模式加载界面。
            webSettings.setLoadWithOverviewMode(true);
            // 设置WebView是否使用viewport，当该属性被设置为false时，加载页面的宽度总是适应WebView控件宽度；
            //当被设置为true，当前页面包含viewport属性标签，在标签中指定宽度值生效，如果页面不包含viewport标签，无法提供一个宽度值，这个时候该方法将被使用。
            webSettings.setUseWideViewPort(true);
            // 设置WebView是否使用其内置的变焦机制，该机制结合屏幕缩放控件使用，默认是false，不使用内置变焦机制。
            webSettings.setBuiltInZoomControls(false);
            //在API18以上已废弃。未来将不支持插件，不要使用。
            // 告诉WebView启用、禁用或者有即用（on demand）的插件，
            // 即用模式是指如果存在一个可以处理嵌入内容的插件，会显示一个占位图标，点击时开启。默认值OFF。
            webSettings.setPluginState(WebSettings.PluginState.ON);
            // 设置是否开启DOM存储API权限，默认false，未开启，设置为true，WebView能够使用DOM storage API
            webSettings.setDomStorageEnabled(true);
            // 设置是否开启数据库存储API权限，默认false，未开启，可以参考setDatabasePath(String path)
            webSettings.setDatabaseEnabled(true);
            // 设置WebView是否支持使用屏幕控件或手势进行缩放，默认是true，支持缩放。
            webSettings.setSupportZoom(true);  //支持缩放

            // 设置在WebView内部是否允许访问文件，默认允许访问。
            webSettings.setAllowFileAccess(true);
            // 设置脚本是否允许自动打开弹窗，默认false，不允许
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            // 设置WebView是否加载图片资源，默认true，自动加载图片
            webSettings.setLoadsImagesAutomatically(true);

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                //WebView是否需要用户的手势进行媒体播放，默认值为true。
                webSettings.setMediaPlaybackRequiresUserGesture(false);
            }

            // 设置WebView使用内置缩放机制时，是否展现在屏幕缩放控件上，默认true，展现在控件上。
            webSettings.setDisplayZoomControls(false);
            // 是否允许在WebView中访问内容URL（Content Url），默认允许。
            // 内容Url访问允许WebView从安装在系统中的内容提供者载入内容。
            webSettings.setAllowContentAccess(true);

        }
    }

    public static void setInnerWebView(WebView webView) {
        if (webView != null) {


            WebSettings webSettings = webView.getSettings();

            //设置WebView是否允许执行JavaScript脚本，默认false，不允许
            webSettings.setJavaScriptEnabled(true);
            // 重写缓存被使用到的方法，该方法基于Navigation Type，
            // 加载普通的页面，将会检查缓存同时重新验证是否需要加载，如果不需要重新加载，将直接从缓存读取数据，
            // 允许客户端通过指定LOAD_DEFAULT、LOAD_CACHE_ELSE_NETWORK、LOAD_NO_CACHE、LOAD_CACHE_ONLY其中之一重写该行为方法，
            // 默认值LOAD_DEFAULT
            webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
            // 设置WebView是否使用预览模式加载界面。
            webSettings.setLoadWithOverviewMode(true);
            // 设置WebView是否使用viewport，当该属性被设置为false时，加载页面的宽度总是适应WebView控件宽度；
            //当被设置为true，当前页面包含viewport属性标签，在标签中指定宽度值生效，如果页面不包含viewport标签，无法提供一个宽度值，这个时候该方法将被使用。
            webSettings.setUseWideViewPort(true);
            // 设置WebView是否使用其内置的变焦机制，该机制结合屏幕缩放控件使用，默认是false，不使用内置变焦机制。
            webSettings.setBuiltInZoomControls(false);
            //在API18以上已废弃。未来将不支持插件，不要使用。
            // 告诉WebView启用、禁用或者有即用（on demand）的插件，
            // 即用模式是指如果存在一个可以处理嵌入内容的插件，会显示一个占位图标，点击时开启。默认值OFF。
            webSettings.setPluginState(WebSettings.PluginState.ON);
            // 设置是否开启DOM存储API权限，默认false，未开启，设置为true，WebView能够使用DOM storage API
            webSettings.setDomStorageEnabled(true);
            // 设置是否开启数据库存储API权限，默认false，未开启，可以参考setDatabasePath(String path)
            webSettings.setDatabaseEnabled(true);
            // 设置WebView是否支持使用屏幕控件或手势进行缩放，默认是true，支持缩放。
            webSettings.setSupportZoom(true);  //支持缩放

            // 设置在WebView内部是否允许访问文件，默认允许访问。
            webSettings.setAllowFileAccess(true);
            // 设置脚本是否允许自动打开弹窗，默认false，不允许
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            // 设置WebView是否加载图片资源，默认true，自动加载图片
            webSettings.setLoadsImagesAutomatically(true);

            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                //WebView是否需要用户的手势进行媒体播放，默认值为true。
                webSettings.setMediaPlaybackRequiresUserGesture(false);
            }

            // 设置WebView使用内置缩放机制时，是否展现在屏幕缩放控件上，默认true，展现在控件上。
            webSettings.setDisplayZoomControls(false);

            // 是否允许在WebView中访问内容URL（Content Url），默认允许。
            // 内容Url访问允许WebView从安装在系统中的内容提供者载入内容。
            webSettings.setAllowContentAccess(true);

            webView.setWebViewClient(new WebViewClient() {
                //覆盖shouldOverrideUrlLoading 方法
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return true;
                }
            });
        }
    }


}
