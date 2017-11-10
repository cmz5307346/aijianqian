package com.ajq.aijieqian102.base;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ajq.aijieqian102.R;
import com.ajq.aijieqian102.bean.ArrayBean;
import com.ajq.aijieqian102.component.YYWebView;
import com.ajq.aijieqian102.component.YYWebViewListener;
import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class BaseFragmentWeb extends BaseFragment {
    private View rootView;//缓存Fragment view
    protected String url = "";
    private String firstUrl;
    private boolean isFirst = true;
    private String title = "";
    private String TelURL;
    private static ValueCallback<Uri> mUploadMessage;
    private static ValueCallback<Uri[]> mUploadCallbackAboveL;
    private WebSettings webSettings;
    private ImageCaptureManager captureManager; // 相机拍照处理类
    private List<ArrayBean> webArray = new ArrayList<>();
    private List<String> homeUrlList = new ArrayList<String>();
    private static String flag = "";

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_web;
    }

    @SuppressLint({"NewApi", "ValidFragment"})
    public BaseFragmentWeb() {
        super();
    }

    @SuppressLint({"NewApi", "ValidFragment"})
    public BaseFragmentWeb(String url) {
        this.url = url;
    }

    @SuppressLint({"NewApi", "ValidFragment"})
    public BaseFragmentWeb(String url, String title) {
        this.url = url;
        this.title = title;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (rootView == null) {
            rootView = inflater.inflate(provideContentViewId(), container, false);
            initView(rootView);
        }
        // 缓存的rootView需要判断是否已经被加过parent，如果有parent需要从parent删除，要不然会发生这个rootview已经有parent的错误。
        ViewGroup parent = (ViewGroup) rootView.getParent();
        if (parent != null) {
            parent.removeView(rootView);
        }
        return rootView;

    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        final ImageView iv_home = (ImageView) rootView.findViewById(R.id.iv_home);
        final ImageView iv_back = (ImageView) rootView.findViewById(R.id.iv_back);
        final YYWebView webView = (YYWebView) rootView.findViewById(R.id.webView);
        final TextView tv_title = (TextView) rootView.findViewById(R.id.tv_web_title);
        tv_title.setText(title);
        webSettings = webView.getSettings();
        //启用数据库
        webSettings.setDatabaseEnabled(true);
        String dir = getActivity().getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //启用地理定位
        webSettings.setGeolocationEnabled(true);
        //设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(dir);
        //最重要的方法，一定要设置，这就是出不来的主要原因
        webSettings.setDomStorageEnabled(true);
        webView.init(rootView, url, new YYWebViewListener() {
            @Override
            public void leaveHome() {
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                if (url.contains("tel:")) {
                    TelURL = url;
                    AndPermission.with(getActivity())
                            .requestCode(100)
                            .permission(Manifest.permission.CALL_PHONE)
                            .callback(Plistener)
                            .start();
                    return true;
                }
                String suffix = url.substring(url.length() - 4);
                if (suffix.equals(".apk")) {
                    String[] temp = url.split("\\.");
                    if (temp.length > 2) {
                        if (!flag.equals(temp[temp.length - 2])) {
                            flag = temp[temp.length - 2];
                            Intent intent = new Intent();
                            intent.setAction("android.intent.action.VIEW");
                            Uri content_url = Uri.parse(url);
                            intent.setData(content_url);
                            startActivity(intent);
                        }
                    } else {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(url);
                        intent.setData(content_url);
                        startActivity(intent);
                    }
                    return false;
                }

                String prefix = url.substring(0, 6);
                if (prefix.equals("tmast:")) {
                    try {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(url);
                        intent.setData(content_url);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
//                if (!bHome && listener != null) {
//                    listener.leaveHome();
//                }
                return true;
            }


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                if (!isFirst) {
                    if (url.equals(firstUrl)) {
                        iv_back.setVisibility(View.GONE);
                        iv_home.setVisibility(View.GONE);
                    } else {
                        iv_back.setVisibility(View.VISIBLE);
                        iv_home.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (isFirst) {
                    isFirst = false;
                    firstUrl = url;
                }
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (webView.canGoBack()) {
                    webView.goBack();
                }
            }
        });
        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                webView.loadUrl(firstUrl);
            }
        });
    }

    private PermissionListener Plistener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。

            // 这里的requestCode就是申请时设置的requestCode。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            switch (requestCode) {
                case 100:
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse(TelURL));
                    //android6.0后可以对权限判断
                    if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getActivity(), R.string.notice_msg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    startActivity(intent);
                    break;
                case 200:
                    Toast.makeText(getActivity(), "权限获取成功", Toast.LENGTH_SHORT).show();
                    break;
                case 300:
                    try {
                        if (captureManager == null) {
                            captureManager = new ImageCaptureManager(getActivity());
                        }
                        Intent intent2 = captureManager.dispatchTakePictureIntent();
                        startActivityForResult(intent2, ImageCaptureManager.REQUEST_TAKE_PHOTO);
                    } catch (IOException e) {
                        Toast.makeText(getActivity(), R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    break;
                case 400:
                    PhotoPickerIntent intent3 = new PhotoPickerIntent(getActivity());
                    intent3.setSelectModel(SelectModel.SINGLE);
                    intent3.setShowCarema(false); // 是否显示拍照， 默认false
                    // intent.setImageConfig(config);
                    startActivityForResult(intent3, 2);
                    break;
            }
        }

        @Override
        public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {

        }
    };

}
