package com.ajq.aijieqian102.component;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.net.http.SslError;
import android.support.v4.app.ActivityCompat;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.ajq.aijieqian102.R;
import com.ajq.aijieqian102.bean.ArrayBean;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by huangchunxin on 16/10/10.
 */

public class YYWebView extends WebView {
    private View rootView;
    private YYWebViewListener listener;
    private Context mContext;
    private String loanProduct;
    private static ValueCallback<Uri> mUploadMessage;
    private static ValueCallback<Uri[]> mUploadCallbackAboveL;
    private List<ArrayBean> webArray = new ArrayList<>();
    private List<String> homeUrlList = new ArrayList<String>();
    private String TelURL;

    public YYWebView(Context context) {
        super(context);
        mContext = context;
    }

    public YYWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public YYWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

//    public YYWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }

//    public YYWebView(Context context, AttributeSet attrs, int defStyleAttr, boolean privateBrowsing) {
//        super(context, attrs, defStyleAttr, privateBrowsing);
//    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        super.setWebViewClient(client);
    }

    public void init(View rootView, String homeUrl, YYWebViewListener l) {
        this.rootView = rootView;
        this.listener = l;


        homeUrlList.add(homeUrl);
//        homeUrlList.add("http://www.youyunet.com");
//        homeUrlList.add("http://120.27.159.66");

        this.getSettings().setJavaScriptEnabled(true);
        this.getSettings().setDomStorageEnabled(true);
        this.getSettings().setGeolocationEnabled(true);
        this.getSettings().setUseWideViewPort(true);

        this.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                if (url.contains("tel:")) {
                    TelURL = url;
                    AndPermission.with(getContext())
                            .requestCode(100)
                            .permission(Manifest.permission.CALL_PHONE)
                            .callback(Plistener)
                            .start();
                    return true;
                }
                String suffix = url.substring(url.length() - 4);
                if (suffix.equals(".apk")) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(url);
                    intent.setData(content_url);
                    mContext.startActivity(intent);
                    return false;
                }

                String prefix = url.substring(0, 6);
                if (prefix.equals("tmast:")) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(url);
                    intent.setData(content_url);
                    mContext.startActivity(intent);
                    return false;
                }


                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                boolean bHome = false;
                for (String s : homeUrlList) {
                    if (url.contains(s)) {
                        bHome = true;
                        break;
                    }
                }
                if (!bHome && listener != null) {
                    listener.leaveHome();
                }
                return true;
            }

            @Override
            public void onLoadResource(WebView view, String url) {
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                // 网页加载完成
                hideProgress();
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
                //super.onReceivedSslError(view, handler, error);
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                if (listener != null) {
                    listener.leaveHome();
                }
            }
        });

        this.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress > 90) {
                    // 网页加载完成
                    hideProgress();
                } else {
                    // 加载中
                    showProgress();
                }
            }

            @Override
            public boolean onJsAlert(WebView view, String url, String message, JsResult result) {

                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());

                builder.setTitle("对话框")
                        .setMessage(message)
                        .setPositiveButton("确定", null);

                // 禁止响应按back键的事件
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
                result.confirm();// 因为没有绑定事件，需要强行confirm,否则页面会变黑显示不了内容。
                return true;

            }

            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
                super.onGeolocationPermissionsShowPrompt(origin, callback);
            }

            @Override
            public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
                onBackListener.getUrl(url);
                return true;
            }
        });

        this.loadUrl(homeUrl);
    }

    @Override
    public void loadUrl(String url) {
        super.loadUrl(url);
        showProgress();
    }

    public void loadUrl1(String url) {
        super.loadUrl(url);
    }

    private void showProgress() {
        ProgressBar loadingView = (ProgressBar) rootView.findViewById(R.id.loading);
        loadingView.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        ProgressBar loadingView = (ProgressBar) rootView.findViewById(R.id.loading);
        loadingView.setVisibility(View.INVISIBLE);
    }

    public void setOnBackListener(onBackListener onBackListener) {
        this.onBackListener = onBackListener;
    }

    public onBackListener onBackListener;

    public interface onBackListener {
        public void getUrl(String url);
    }

    private PermissionListener Plistener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。

            // 这里的requestCode就是申请时设置的requestCode。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if (requestCode == 100) {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(TelURL));
                //android6.0后可以对权限判断
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(mContext, R.string.notice_msg, Toast.LENGTH_SHORT).show();
//                                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, 1);//权限申请
                    return;
                }
                mContext.startActivity(intent);
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == 100) {
                // TODO ...
                Toast.makeText(mContext, "电话权限申请失败", Toast.LENGTH_SHORT).show();
            }
        }
    };
}

