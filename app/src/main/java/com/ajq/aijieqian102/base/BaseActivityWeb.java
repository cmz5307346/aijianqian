package com.ajq.aijieqian102.base;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ajq.aijieqian102.util.YYLog;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.google.gson.Gson;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.ajq.aijieqian102.R;
import com.ajq.aijieqian102.bean.ArrayBean;
import com.ajq.aijieqian102.bean.DataBean;
import com.ajq.aijieqian102.bean.WebSetJsonData;
import com.ajq.aijieqian102.component.YYWebView;
import com.ajq.aijieqian102.component.YYWebViewListener;
import com.ajq.aijieqian102.constant.PublicDef;
import com.ajq.aijieqian102.http.HttpUtil;
import com.ajq.aijieqian102.util.ImageUtils;
import com.ajq.aijieqian102.util.Tools;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/7/12.
 */

public class BaseActivityWeb extends BaseActivity {
    protected String nowUrl = "";
    private String firstUrl;
    private boolean isFirst = true;
    private String title = "";

    private UserData userData;
    private String loanProduct;
    private static ValueCallback<Uri> mUploadMessage;
    private static ValueCallback<Uri[]> mUploadCallbackAboveL;
    private List<ArrayBean> webArray = new ArrayList<>();
    private List<String> homeUrlList = new ArrayList<String>();
    YYWebView mWebView;
    private String TelURL;
    private WebSettings webSettings;
    private ImageCaptureManager captureManager; // 相机拍照处理类
    private boolean isDownload = false;
    private static String flag = "";

    public static void start(Context activity, String url, String title) {
        Intent intent = new Intent();
        intent.setClass(activity, BaseActivityWeb.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        activity.startActivity(intent);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_web;
    }

    @Override
    public void init() {
        nowUrl = getIntent().getStringExtra("url");
//        nowUrl = "https://test.doraemoney.com/newCube/SourceTestPage.html";
        title = getIntent().getStringExtra("title");
        String name = Tools.getConf("name", "");
        String phone = Tools.getConf("phone", "");
        userData = new UserData(name, phone);
        if (webArray.size() == 0) {
            DataBean dataBean = Tools.getDataBean(this);
            if (dataBean != null) {
                webArray = dataBean.getArray();
            } else {
                if (PublicDef.READ_LOCAL_JSON) {
                    Tools.readLocalJson(this);
                } else {
                    getWebProperty();
                }

            }
        }
    }

    @Override
    public void initView() {
        super.initView();
        final ImageView iv_home = (ImageView) findViewById(R.id.iv_home);
        final ImageView iv_back = (ImageView) findViewById(R.id.iv_back);
        mWebView = (YYWebView) findViewById(R.id.webView);
        webSettings = mWebView.getSettings();
        //启用数据库
        webSettings.setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //启用地理定位
        webSettings.setGeolocationEnabled(true);
        //设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(dir);
        //最重要的方法，一定要设置，这就是出不来的主要原因
        webSettings.setDomStorageEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        final TextView tv_title = (TextView) findViewById(R.id.tv_web_title);
        tv_title.setText(title);
        mWebView.init(getWindow().getDecorView(), nowUrl, new YYWebViewListener() {
            @Override
            public void leaveHome() {
            }
        });
        mWebView.addJavascriptInterface(new JavaScriptInterface(), "web");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, final String url) {
                if (url.contains("tel:")) {
                    TelURL = url;
                    AndPermission.with(BaseActivityWeb.this)
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
            public void onLoadResource(WebView view, String url) {
                YYLog.ez("onLoadResource", url);
                for (int i = 0; i < webArray.size(); i++) {
                    List<String> getUrls = webArray.get(i).getWebUrl().getGetUrl();
                    if (getUrls.size() > 0) {
                        String temp = getUrls.get(0);
                        if (url.contains(temp)) {
                            if (url.contains("https://loan.rongba.com/h5tuiguang/kff")) {
                                if (!url.equals("https://loan.rongba.com/h5tuiguang/kff")) {
                                    continue;
                                }
                            }

                            String script = webArray.get(i).getScript_android().getGet();
                            YYLog.ez("script", script);
                            if (!TextUtils.isEmpty(script)) {
                                mWebView.loadUrl1(script);
                            }
                            break;
                        }
                    }
                }
            }

            @Override
            public void onPageStarted(WebView view, final String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                YYLog.ez("onPageStarted", url);
                if (!isFirst) {
                    if (url.equals(firstUrl)) {
                        iv_home.setVisibility(View.GONE);
                    } else {
                        iv_home.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (isFirst) {
                    isFirst = false;
                    firstUrl = url;
                }
                nowUrl = url;
//                onBackListener.getUrl(url);
                if (userData != null) {
                    for (int i = 0; i < webArray.size(); i++) {
                        List<String> setUrls = webArray.get(i).getWebUrl().getSetUrl();
                        if (setUrls.size() > 0) {
                            if (url.contains(setUrls.get(0))) {
                                loanProduct = webArray.get(i).getWebName();
                                fillUserData(url, i);
                                break;
                            }
                        }
                    }
                }
                hideProgress();
            }
        });
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress > 90) {
                    // 网页加载完成
                    hideProgress();
                } else {
                    // 加载中
//                    showProgress();
                }
            }

            @Override
            public void onGeolocationPermissionsShowPrompt(final String origin, final GeolocationPermissions.Callback callback) {
                final boolean remember = false;
                AndPermission.with(BaseActivityWeb.this)
                        .requestCode(200)
                        .permission(Manifest.permission.ACCESS_COARSE_LOCATION)
                        .permission(Manifest.permission.ACCESS_FINE_LOCATION)
                        .callback(new PermissionListener() {

                            @Override
                            public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                                callback.invoke(origin, true, remember);
                            }

                            @Override
                            public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
//                                callback.invoke(origin, false, remember);
                            }
                        })
                        .start();
            }

            // For Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                mUploadMessage = uploadMsg;
                showOptions();
            }

            // For Android  > 4.1.1
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
                mUploadMessage = uploadMsg;
                showOptions();
            }

            // For Android > 5.0支持多张上传
            @Override
            public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> uploadMsg, FileChooserParams
                    fileChooserParams) {
                mUploadCallbackAboveL = uploadMsg;
                boolean isEm = mUploadCallbackAboveL == null ? true : false;
                showOptions();
                return true;
            }
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nowUrl.equals(firstUrl)) {
                    finish();
                } else {
                    if (mWebView.canGoBack()) {
                        mWebView.goBack();
                    }
                }
            }
        });
        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void showOptions() {
        String[] stringItems = {getString(R.string.camera), getString(R.string.photo)};
        final ActionSheetDialog dialog = new ActionSheetDialog(this, stringItems, null);
        dialog.isTitleShow(false).titleTextSize_SP(14.5f).show();//
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                cancelCallback();
            }
        });
        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                //position
                switch (position) {
                    case 0:
                        dialog.dismiss();
                        //如果大于6.0
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            AndPermission.with(BaseActivityWeb.this)
                                    .requestCode(300)
                                    .permission(Manifest.permission.CAMERA)
                                    .callback(Plistener)
                                    .start();
                        }
                        break;
                    case 1:
                        dialog.dismiss();
                        //如果大于6.0
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            AndPermission.with(BaseActivityWeb.this)
                                    .requestCode(400)
                                    .permission(Manifest.permission.READ_EXTERNAL_STORAGE)
                                    .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                    .callback(Plistener)
                                    .start();
                        }
                }
            }
        });


    }

    public class JavaScriptInterface {

        public JavaScriptInterface() {
        }

        @JavascriptInterface
        public void saveUserData(final String name, String phone) {
            phone=phone.replaceAll(" ","");
            if (Tools.isMobile(phone)) {
                userData = new UserData(name, phone);
                if (!TextUtils.isEmpty(name) || !TextUtils.isEmpty(phone)) {
                    if (!name.equals(Tools.getConf("name", ""))) {
                        Tools.writeConf("name", name);
                        Tools.writeConf("phone", phone);
                        saveUserDataToWeb(name, phone);
                    }
                    if (!phone.equals(Tools.getConf("phone", ""))) {
                        Tools.writeConf("phone", phone);
                        saveUserDataToWeb(name, phone);
                    }
                    YYLog.ez("name", name);
                    YYLog.ez("phone", phone);
                }
                if (Tools.getSubmitFlag(2) == 2) {
                    Tools.writeSubmitFlag(1);
                }
            }
        }
    }

    private void saveUserDataToWeb(String name, String phone) {
        String version = Tools.getVersionName();
        String packageName = Tools.getPackageName();
        String loanProduct = getLoanProduct();
        HttpUtil.reportNewUser(name,phone,packageName,version,this.loanProduct);
        HttpUtil.SaveMethod(name, phone, packageName, version, loanProduct, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
            }
        });

    }

    private String getLoanProduct() {
        String strUTF8 = "";
        try {
            strUTF8 = URLEncoder.encode(loanProduct, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return strUTF8;
    }

    class UserData {
        private String userName;
        private String userPhone;

        public UserData(String userName, String userPhone) {
            this.userName = userName;
            this.userPhone = userPhone;
        }

        public String getUserName() {
            return userName;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

    }

    private void getWebProperty() {
        HttpUtil.LoadUserInfoConfigMethod(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String jsonStr = response.body().string();
                Gson gson = new Gson();
                WebSetJsonData data = gson.fromJson(jsonStr, WebSetJsonData.class);
                DataBean dataBean = data.getData();
                if (dataBean != null) {
                    webArray = dataBean.getArray();
                    int localVersion = Tools.getWebSetVersion(0);
                    int currentVersion = dataBean.getVersion();
                    if (localVersion < currentVersion) {
                        Tools.writeWebSetVersion(currentVersion);
                        Tools.writeFile(getApplicationContext(), PublicDef.WEB_PROPERTY_FILE, jsonStr);
                    }
                }
            }
        });
    }

    private void showProgress() {
        ProgressBar loadingView = (ProgressBar) findViewById(R.id.loading);
        loadingView.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        ProgressBar loadingView = (ProgressBar) findViewById(R.id.loading);
        loadingView.setVisibility(View.INVISIBLE);
    }

    //自动填充
    private void fillUserData(String url, int i) {
        String script = webArray.get(i).getScript_android().getSet();
        if (!TextUtils.isEmpty(script)) {
            int count = 0;
            String str = script;
            while (str.indexOf("%s") != -1) {
                count++;
                str = str.substring(str.indexOf("%s") + 2);
            }
            if (count == 2) {
                String name = userData.getUserName();
                String phone = userData.getUserPhone();
                script = String.format(script, name, phone);
                mWebView.loadUrl(script);
            } else {
                String phone = userData.getUserPhone();
                script = String.format(script, phone);
                mWebView.loadUrl(script);
            }
            return;
        }
    }

    public void setOnBackListener(YYWebView.onBackListener onBackListener) {
        this.onBackListener = onBackListener;
    }

    public YYWebView.onBackListener onBackListener;

    public interface onBackListener {
        public void getUrl(String url);
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
                    if (ActivityCompat.checkSelfPermission(BaseActivityWeb.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(BaseActivityWeb.this, R.string.notice_msg, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    startActivity(intent);
                    break;
                case 200:
                    Toast.makeText(BaseActivityWeb.this, "权限获取成功", Toast.LENGTH_SHORT).show();
                    break;
                case 300:
                    try {
                        if (captureManager == null) {
                            captureManager = new ImageCaptureManager(BaseActivityWeb.this);
                        }
                        Intent intent2 = captureManager.dispatchTakePictureIntent();
                        startActivityForResult(intent2, ImageCaptureManager.REQUEST_TAKE_PHOTO);
                    } catch (IOException e) {
                        Toast.makeText(BaseActivityWeb.this, R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    break;
                case 400:
                    PhotoPickerIntent intent3 = new PhotoPickerIntent(BaseActivityWeb.this);
                    intent3.setSelectModel(SelectModel.SINGLE);
                    intent3.setShowCarema(false); // 是否显示拍照， 默认false
                    // intent.setImageConfig(config);
                    startActivityForResult(intent3, 2);
                    break;
            }
        }

        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == 100) {
                // TODO ...
                Toast.makeText(BaseActivityWeb.this, "电话权限申请失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case 2:
                    refreshAdpater(data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));
                    break;
                // 拍照
                case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                    if (captureManager.getCurrentPhotoPath() != null) {
                        captureManager.galleryAddPic();
                        // 照片地址
                        String imagePaht = captureManager.getCurrentPhotoPath();
                        //压缩图片
                        Bitmap bitmap = ImageUtils.ratio(imagePaht, 950, 1280);
                        //得到图片新的路径
                        String newfilepath = ImageUtils.saveMyBitmap(bitmap);
                        //获取图片URI
                        Uri uri = ImageUtils.getImageContentUri(BaseActivityWeb.this, new File(imagePaht));
                        if (mUploadCallbackAboveL != null) {
                            Uri[] uris = new Uri[]{uri};
                            mUploadCallbackAboveL.onReceiveValue(uris);
                            mUploadCallbackAboveL = null;
                        } else {
                            mUploadMessage.onReceiveValue(uri);
                            mUploadMessage = null;
                        }
                    }
                    break;


            }
        } else {
            cancelCallback();
        }
    }

    public static void cancelCallback() {
        if (mUploadCallbackAboveL != null) {
            mUploadCallbackAboveL.onReceiveValue(new Uri[]{Uri.EMPTY});
            mUploadCallbackAboveL = null;
        } else if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(Uri.EMPTY);
            mUploadMessage = null;
        }
    }

    private void refreshAdpater(ArrayList<String> paths) {
        // 处理返回照片地址
        //压缩图片
        Bitmap bitmap = ImageUtils.ratio(paths.get(0), 950, 1280);
        //得到图片新的路径
        String newfilepath = ImageUtils.saveMyBitmap(bitmap);
        //获取图片URI
        Uri uri = ImageUtils.getImageContentUri(BaseActivityWeb.this, new File(paths.get(0)));
        if (mUploadCallbackAboveL != null) {
            Uri[] uris = new Uri[]{uri};
            mUploadCallbackAboveL.onReceiveValue(uris);
            mUploadCallbackAboveL = null;
        } else if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(uri);
            mUploadMessage = null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
