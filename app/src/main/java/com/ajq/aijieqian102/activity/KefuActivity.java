package com.ajq.aijieqian102.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ajq.aijieqian102.R;
import com.ajq.aijieqian102.component.YYWebView;
import com.ajq.aijieqian102.util.ImageUtils;
import com.ajq.aijieqian102.util.Tools;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.foamtrace.photopicker.ImageCaptureManager;
import com.foamtrace.photopicker.PhotoPickerActivity;
import com.foamtrace.photopicker.SelectModel;
import com.foamtrace.photopicker.intent.PhotoPickerIntent;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by Administrator on 2017/6/1.
 */

public class KefuActivity extends Activity {
    private TextView mTv_title;
    private LinearLayout mLl_back;
    private WebSettings webSettings;
    private static ValueCallback<Uri> mUploadMessage;
    private static ValueCallback<Uri[]> mUploadCallbackAboveL;
    private ImageCaptureManager captureManager; // 相机拍照处理类
    private YYWebView webView;

    public static void start(Context activity) {
        Intent intent = new Intent();
        intent.setClass(activity, KefuActivity.class);
        activity.startActivity(intent);

    }

    private String url = "http://admin.appkefu.com/AppKeFu/float/wap/chat.php?wg=youyu111&robot=false&hidenav=true&history=true&nickname=";
    private String content = "用户：{%1$s}&postscript=设备型号：{%2$s}，系统版本号：{%2$s}，包名：{%4$s}，应用名：{%5$s}";
    private ProgressBar loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kufu);
        loadingView = (ProgressBar) findViewById(R.id.loading);
        mTv_title = (TextView) findViewById(R.id.tv_title);
        mLl_back = (LinearLayout) findViewById(R.id.ll_back);
        webView = (YYWebView) findViewById(R.id.wv_kufu);
        String uuid = Tools.getDeviceID();
        String device = Build.MODEL;
        String version = Build.VERSION.RELEASE;
        String PackageName = getApplicationContext().getPackageName();
        String AppName = Tools.getAppName(this);
        content = String.format(content, uuid, device, version, PackageName, AppName);
        if (url != null && url.length() > 0) {
            webView.init(findViewById(R.id.rootView), url + content, null);
        }
        webSettings = webView.getSettings();
        //启用数据库
        webSettings.setDatabaseEnabled(true);
        String dir = this.getApplicationContext().getDir("database", Context.MODE_PRIVATE).getPath();
        //启用地理定位
        webSettings.setGeolocationEnabled(true);
        //设置定位的数据库路径
        webSettings.setGeolocationDatabasePath(dir);
        //最重要的方法，一定要设置，这就是出不来的主要原因
        webSettings.setDomStorageEnabled(true);
        webView.setOnBackListener(new YYWebView.onBackListener() {
            @Override
            public void getUrl(String url) {
            }
        });
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return super.shouldOverrideUrlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                loadingView.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                super.onLoadResource(view, url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress > 90) {
                    // 网页加载完成
                    loadingView.setVisibility(View.INVISIBLE);
                } else {
                }
            }

            public boolean onJsConfirm(WebView view, String url,
                                       String message, final JsResult result) {
                // TODO Auto-generated method stub
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle(getString(R.string.dialog))
                        .setMessage(message)
                        .setNegativeButton(getString(R.string.cancel), null)
                        .setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finish();

                            }
                        });

                // 禁止响应按back键的事件
                builder.setCancelable(false);
                AlertDialog dialog = builder.create();
                dialog.show();
                result.confirm();

                return true;
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
        mTv_title.setText(getString(R.string.customerService));
        mLl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                            AndPermission.with(KefuActivity.this)
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
                            AndPermission.with(KefuActivity.this)
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

    private PermissionListener Plistener = new PermissionListener() {
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            switch (requestCode) {
                case 100:
                    break;
                case 200:
                    Toast.makeText(KefuActivity.this, getString(R.string.dialog_getPermissionSuccess), Toast.LENGTH_SHORT).show();
                    break;
                case 300:
                    try {
                        if (captureManager == null) {
                            captureManager = new ImageCaptureManager(KefuActivity.this);
                        }
                        Intent intent2 = captureManager.dispatchTakePictureIntent();
                        startActivityForResult(intent2, ImageCaptureManager.REQUEST_TAKE_PHOTO);
                    } catch (IOException e) {
                        Toast.makeText(KefuActivity.this, R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    break;
                case 400:
                    PhotoPickerIntent intent3 = new PhotoPickerIntent(KefuActivity.this);
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
                Toast.makeText(KefuActivity.this, getString(R.string.dialog_getPermissionFail), Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 拍照
                case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                    if (captureManager.getCurrentPhotoPath() != null) {
                        captureManager.galleryAddPic();
                        // 照片地址
                        String imagePaht = captureManager.getCurrentPhotoPath();
                        //压缩图片
                        Bitmap bitmap = ImageUtils.ratio(imagePaht, 950, 1280);
                        //获取图片URI
                        Uri uri = ImageUtils.getImageContentUri(KefuActivity.this, new File(imagePaht));
                        if (mUploadCallbackAboveL != null) {
                            Uri[] uris = new Uri[]{uri};
                            mUploadCallbackAboveL.onReceiveValue(uris);
                            mUploadCallbackAboveL = null;
                        } else if (mUploadMessage != null) {
                            mUploadMessage.onReceiveValue(uri);
                            mUploadMessage = null;
                        }
                    }
                    break;
                // 选择照片
                case 2:
                    refreshAdpater(data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT));
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
        Uri uri = ImageUtils.getImageContentUri(KefuActivity.this, new File(paths.get(0)));
        if (mUploadCallbackAboveL != null) {
            Uri[] uris = new Uri[]{uri};
            mUploadCallbackAboveL.onReceiveValue(uris);
            mUploadCallbackAboveL = null;
        } else if (mUploadMessage != null) {
            mUploadMessage.onReceiveValue(uri);
            mUploadMessage = null;
        }
    }
}
