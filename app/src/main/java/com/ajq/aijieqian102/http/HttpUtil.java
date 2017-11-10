package com.ajq.aijieqian102.http;

import android.util.Log;

import com.ajq.aijieqian102.application.YYApplication;
import com.ajq.aijieqian102.util.Tools;

import java.io.IOException;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/6/20.
 */

public class HttpUtil {
    private final static String Url = "http://p2p.elongpay.com/LoanAPI/";
//    private final static String Url = "https://www.youyunet.com/LoanAPI/";
    private final static String reporturl="http://p2p.elongpay.com/TgChannel/v2/reportNewUser";
//    private final static String reporturl="https://www.youyunet.com/TgChannel/v2/reportNewUser";
    //private final static String Url = "http://120.27.131.97/LoanAPI/";
//    private final static String reporturl="http://172.23.3.200/TgChannel/v2/reportNewUser";
//    private final static String Url = "http://172.23.3.200/LoanAPI/";
    private final static String md5key = "d277c389a1ad257e7888f8a9b971c1c6";
    public final static String BANNERAD_TOP = "market-top";
    public final static String BANNERAD_BOTTOM = "personal-bottom";
    private static final String PATH = "Filter/v1/";
    private static final String SAVE_USER_INFO = "Save";
    private static final String LOAD_USER_INFO = "LoadUserInfoConfig";

    // 获取首页贷款产品列表
    public static void getHomeProductList(Callback callback) {
        String sign = "deviceType=" + android.os.Build.MODEL + "&pageSize=100" + "&sysVersion=" + android.os.Build.VERSION.RELEASE + "&md5key=" + md5key;
        String url = Url + "v2/getHomeProductList?" + "deviceType=" + android.os.Build.MODEL + "&pageSize=100" + "&sysVersion=" + android.os.Build.VERSION.RELEASE + "&sign=" + Tools.MD5(sign);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        final Request request = new Request.Builder().
                get().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);

    }

    //获取在线参数
    public static void GetOnlineParam(String appType, String packageName, Callback callback) {
        int version = Tools.getOnlineParamVersion(0);
        OkHttpClient okHttpClient = new OkHttpClient();
        final Request request = new Request.Builder().url(Url + "getOnlineParam" + "?version=" + version + "&packageName=" + packageName + "&platform=android&appType=" + appType).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(callback);
    }


    // 获取贷款超市
    public static void getProductList(String loanCycle, String loanRange, int pageIndex, Callback callback) {
        String sign = "deviceType=" + android.os.Build.MODEL + "&loanCycle=" + loanCycle + "&loanRange=" + loanRange + "&pageIndex=" + pageIndex + "&sysVersion=" + android.os.Build.VERSION.RELEASE + "&md5key=" + md5key;
        loanRange = loanRange.replace("+", "%2b");
        String url = Url + "v2/getProductList?" + "deviceType=" + android.os.Build.MODEL + "&loanCycle=" + loanCycle + "&loanRange=" + loanRange + "&pageIndex=" + pageIndex + "&sysVersion=" + android.os.Build.VERSION.RELEASE + "&sign=" + Tools.MD5(sign);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        final Request request = new Request.Builder().
                get().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    // 获取轮播图
    public static void getBannerAd(String position, String version, Callback callback) {
        String sign = "position=" + position + "&md5key=" + md5key;
        String url = Url + "v2/getBannerAd?" + "position=" + position + "&version=" + version + "&sign=" + Tools.MD5(sign);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        final Request request = new Request.Builder().
                get().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    // 获取弹窗
    public static void getgetPopMsg(String version, Callback callback) {
        String sign = "md5key=" + md5key;
        String url = Url + "v2/getPopMsg?" + "&version=" + version + "&sign=" + Tools.MD5(sign);
        OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
        final Request request = new Request.Builder().
                get().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    //获取上报Json文件
    public static void LoadUserInfoConfigMethod(Callback callback) {
        String deviceId = Tools.getDeviceID();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < deviceId.length(); i++) {
            char x = deviceId.charAt(i);
            if (x != '-') {
                sb.append(x);
            }
        }
        deviceId = sb.toString();
        String packageName = YYApplication.getInstance().getPackageName();
        int config_version = Tools.getWebSetVersion(0);
        String md5 = Tools.MD5("device-id=" + deviceId + "&package-name=" + packageName + "&apple-id=0&config-version=" + config_version + "&platform=android&md5key=d277c389a1ad257e7888f8a9b971c1c6");
        md5 = md5.toLowerCase();

        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(Url + PATH + LOAD_USER_INFO + "?device-id=" + deviceId + "&package-name=" + packageName + "&apple-id=0&config-version=" + config_version + "&platform=android&sign=" + md5)
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
    }

    public static void SaveMethod(String name, String phone, String packageName, String version, String loanproduct, Callback callback) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        final Request request = new Request.Builder()
                .url(Url + PATH + SAVE_USER_INFO + "?loan-amount=-1&age=-1&has-house=-1&has-car=-1&has-life-insurance=-1&name=" + name + "&phone=" + phone + "&appleid=&package-name=" + packageName + "&version=" + version + "&loanproduct=" + loanproduct + "&platform=android")
                .build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
    }

    public static  void reportNewUser(String name, final String phone, String packageName, String version, String loanproduct){


        if (Tools.getIsSavePhone(phone)){
            return;
        }

        Map<String, String> map = new TreeMap<String, String>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        return obj1.compareTo(obj2);
                    }
                });
        map.put("name",name);
        map.put("phone",phone);
        map.put("packageName",packageName);
        map.put("version",version);
        map.put("loanProduct",loanproduct);
        map.put("platform","android");
        String sign=Tools.getSign(map,md5key);
        OkHttpClient mOkHttpClient = new OkHttpClient();
        FormBody.Builder params=new FormBody.Builder();
        params.add("name",name);
        params.add("phone",phone);
        params.add("packageName",packageName);
        params.add("version",version);
        params.add("loanProduct",loanproduct);
        params.add("platform","android");
        params.add("sign",sign);
        final Request request = new Request.Builder()
                .url(reporturl)
                .post(params.build())
                .build();

        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("test","213213e");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Tools.writeIsSavePhone(phone);
                Log.e("test",response.body().string());
            }
        });

    }




    //获取产品详情页面信息
    public static void GetProductInfo(String productId, Callback callback) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        String sign = "productId=" + productId + "&md5key=" + md5key;
        String url = Url + "v2/getProductDetail/" + productId + "?sign=" + Tools.MD5(sign);
//        String url = Url + "v2/getProductDetail/1?sign=" + Tools.MD5(sign);
        Log.i("url", url);
        final Request request = new Request.Builder()
                .url(url).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
    }

    //获取广告轮播数据
    public static void GetNotice(String packageName, String num, Callback callback) {
        OkHttpClient mOkHttpClient = new OkHttpClient();
        String sign = "num=" + num + "&packageName=" + packageName + "&md5key=" + md5key;
        String url = Url + "v2/getNotice?num=" + num + "&packageName=" + packageName + "&sign=" + Tools.MD5(sign);
        final Request request = new Request.Builder()
                .url(url).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(callback);
    }
}

