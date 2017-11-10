package com.ajq.aijieqian102.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;


import com.ajq.aijieqian102.constant.PublicDef;
import com.ajq.aijieqian102.http.HttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/5/8.
 */

public class PamramUtils {
    private static Map<String, String> mParamsMap;
    public static boolean isRefresh = false;
    public static final String APPTYPE_LOAN = "loan";//贷款类型
    private static final String APPTYPE_INSURANCE = "insurance";//保险类型
    private static final String APPTYPE_LOTTERY = "lottery";//彩票类型

    public static boolean getIsRefresh(final Context context, String packageName) {

        HttpUtil.GetOnlineParam(APPTYPE_LOAN, packageName, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String jsonStr = response.body().string();
                String paramsjosn;
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr).getJSONObject("data");
                    paramsjosn = jsonObject.getString("params");
                    int currentVersion = jsonObject.getInt("version");
                    int localVersion = Tools.getOnlineParamVersion(0);

                    if (localVersion < currentVersion) {
                        setParamsMap(jsonToObject(paramsjosn));
                        Tools.writeOnlineParamVersion(currentVersion);
                        Tools.writeFile(context, PublicDef.WEB_ONLINEPARAM_FILE, paramsjosn);
                        Intent intent = new Intent();
                        intent.setAction(PublicDef.ACTION_REFRESH);
                        context.sendBroadcast(intent);
                        //本地数据版本号小于服务器版本才进行刷新
                    } else {
                        paramsjosn = Tools.readFile(context, PublicDef.WEB_ONLINEPARAM_FILE);
                        setParamsMap(jsonToObject(paramsjosn));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        return isRefresh;
    }


    public static void getOnlineParam(final Context context, String packageName) {

        HttpUtil.GetOnlineParam(APPTYPE_LOAN, packageName, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String jsonStr = response.body().string();
                String paramsjosn;
                try {
                    JSONObject jsonObject = new JSONObject(jsonStr).getJSONObject("data");
                    paramsjosn = jsonObject.getString("params");
                    int currentVersion = jsonObject.getInt("version");
                    int localVersion = Tools.getOnlineParamVersion(0);
                    Log.e("test", jsonObject.toString());
                    if (localVersion < currentVersion) {
                        Tools.writeOnlineParamVersion(currentVersion);
                        Tools.writeFile(context, PublicDef.WEB_ONLINEPARAM_FILE, paramsjosn);

                    } else {
                        paramsjosn = Tools.readFile(context, PublicDef.WEB_ONLINEPARAM_FILE);
                    }
                    setParamsMap(jsonToObject(paramsjosn));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public static Map jsonToObject(String jsonStr) throws JSONException {
        JSONObject jsonObj = new JSONObject(jsonStr);
        Iterator<String> nameItr = jsonObj.keys();
        String name;
        Map<String, String> outMap = new HashMap<String, String>();
        while (nameItr.hasNext()) {
            name = nameItr.next();
            outMap.put(name, jsonObj.getString(name));
        }
        return outMap;
    }

    public static void setParamsMap(Map<String, String> paramsMap) {
        mParamsMap = paramsMap;
    }

    private static String getValve(String key, String defval) {
        if (mParamsMap != null) {
            if (mParamsMap.containsKey(key)) {
                return mParamsMap.get(key);
            }
        }
        return defval;
    }

    //是否正在审核
    public static boolean isInAudit() {
        String flag = getValve("isInAudit", "YES");
        return flag.equals("YES");
    }

    //是否打开客服
    public static boolean isService() {
        return getValve("ShowService", "").equals("YES");
    }

    public static String getCardURL() {
        return getValve("CardURL", "https://at.umeng.com/0rm0zy");
    }

    public static String getaccessUrl() {
        return getValve("accessUrl", "");
    }

    public static String getappName() {
        return getValve("appName", "");
    }

    public static String gettitle() {
        return getValve("title", "");
    }

    public static String geticonUrl() {
        return getValve("iconUrl", "");
    }

    public static String getamountUrl() {
        return getValve("amountUrl", "");
    }

    public static String getamount() {
        return getValve("amount", "");
    }

    public static String gettagLeft() {
        return getValve("tagLeft", "");
    }

    public static String gettagRight() {
        return getValve("tagRight", "");
    }

    public static String getbtnTitle() {
        return getValve("btnTitle", "");
    }

    public static String gethomeTitle() {
        return getValve("homeTitle", "");
    }
}
