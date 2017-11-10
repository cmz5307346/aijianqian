package com.ajq.aijieqian102.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ajq.aijieqian102.application.YYApplication;
import com.ajq.aijieqian102.bean.DataBean;
import com.ajq.aijieqian102.bean.ProductInfo;
import com.ajq.aijieqian102.bean.WebSetJsonData;
import com.ajq.aijieqian102.constant.PublicDef;

import org.apache.http.util.EncodingUtils;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import static android.content.Context.MODE_PRIVATE;
import static android.text.TextUtils.isEmpty;

/**
 * Created by Administrator on 2017/6/20.
 */
public class Tools {
    /**
     * 获取手机唯一标识
     */
    public static String getDeviceID() {
        StringBuilder deviceId = new StringBuilder();
        Context context = YYApplication.getInstance();
        try {
            //wifi mac地址
            WifiManager wifi = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            String wifiMac = info.getMacAddress();
            if (!isEmpty(wifiMac)) {
                deviceId.append(wifiMac);
                return deviceId.toString().replace(':', '-');
            }
            //IMEI（imei）,需要电话权限,先屏蔽
//            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
//            String imei = tm.getDeviceId();
//            if(!isEmpty(imei)){
//                deviceId.append("imei");
//                deviceId.append(imei);
//                return deviceId.toString();
//            }
            //序列号（sn）
//            String sn = tm.getSimSerialNumber();
//            if(!isEmpty(sn)){
//                deviceId.append("sn");
//                deviceId.append(sn);
//                return deviceId.toString();
//            }
            //如果上面都没有， 则生成一个id：随机码
            String uuid = UUID.randomUUID().toString();
            if (!isEmpty(uuid)) {
                deviceId.append("id");
                deviceId.append(uuid);
                return deviceId.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
            deviceId.append("id").append(UUID.randomUUID().toString());
        }
        return deviceId.toString();
    }

    public static final String MD5(String pwd) {
        //用于加密的字符
        char md5String[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F'};
        try {
            //使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
            byte[] btInput = pwd.getBytes();
            //信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            //MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
            mdInst.update(btInput);
            // 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
            byte[] md = mdInst.digest();
            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {   //  i = 0
                byte byte0 = md[i];  //95
                str[k++] = md5String[byte0 >>> 4 & 0xf];    //    5
                str[k++] = md5String[byte0 & 0xf];   //   F
            }
            //返回经过加密后的字符串
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean writeOnlineParamVersion(int val) {
        SharedPreferences sharedPreferences = YYApplication.getInstance().getSharedPreferences("youyunet", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PublicDef.WEB_ONLINEPARAM_VERSION, val);
        return editor.commit();
    }

    public static int getOnlineParamVersion(int defVal) {
        SharedPreferences sharedPreferences = YYApplication.getInstance().getSharedPreferences("youyunet", 0);
        return sharedPreferences.getInt(PublicDef.WEB_ONLINEPARAM_VERSION, defVal);
    }

    public static boolean writeBannerAdtopVersion(int val) {
        SharedPreferences sharedPreferences = YYApplication.getInstance().getSharedPreferences("youyunet", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PublicDef.WEB_BANNERADTOP_VERSION, val);
        return editor.commit();
    }

    public static int getBannerAdtopVersio(int defVal) {
        SharedPreferences sharedPreferences = YYApplication.getInstance().getSharedPreferences("youyunet", 0);
        return sharedPreferences.getInt(PublicDef.WEB_BANNERADTOP_VERSION, defVal);
    }

    public static boolean writePopMsgVersion(int val) {
        SharedPreferences sharedPreferences = YYApplication.getInstance().getSharedPreferences("youyunet", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PublicDef.WEB_POPMSG_VERSION, val);
        return editor.commit();
    }

    public static int getPopMsgVersio(int defVal) {
        SharedPreferences sharedPreferences = YYApplication.getInstance().getSharedPreferences("youyunet", 0);
        return sharedPreferences.getInt(PublicDef.WEB_POPMSG_VERSION, defVal);
    }

    public static boolean writePopMsgData(long val) {
        SharedPreferences sharedPreferences = YYApplication.getInstance().getSharedPreferences("youyunet", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(PublicDef.WEB_POPMSG_DATA, val);
        return editor.commit();
    }

    public static long getPopMsgData(long defVal) {
        SharedPreferences sharedPreferences = YYApplication.getInstance().getSharedPreferences("youyunet", 0);
        return sharedPreferences.getLong(PublicDef.WEB_POPMSG_DATA, defVal);
    }


    public static void writeFile(Context context, String fileName, String writestr) throws IOException {
        try {
            FileOutputStream fout = context.openFileOutput(fileName, MODE_PRIVATE);
            byte[] bytes = writestr.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readFile(Context context, String fileName) throws IOException {
        String res = "";
        try {
            FileInputStream fin = context.openFileInput(fileName);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static String getPackageName() {
        return YYApplication.getInstance().getPackageName();
    }

    /**
     * 获取应用程序名称
     */
    public static String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    //存储是否首次进入
    public static boolean writFirst(boolean val) {
        SharedPreferences sharedPreferences = YYApplication.getInstance().getSharedPreferences("youyunet", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("First", val);
        return editor.commit();
    }

    //获取是否首次进入
    public static boolean getFirst(boolean defVal) {
        SharedPreferences sharedPreferences = YYApplication.getInstance().getSharedPreferences("youyunet", 0);
        return sharedPreferences.getBoolean("First", defVal);
    }

    //获取当前日期用户保存记录
    public static String getData() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
    }

    //保存记录
    public static void saveRecord(ProductInfo productInfo) {
        Map<String, List<ProductInfo>> map = Tools.getDataMap();
        if (map.size() > 0) {
            Iterator<Map.Entry<String, List<ProductInfo>>> entries = map.entrySet().iterator();
            while (entries.hasNext()) {
                Map.Entry<String, List<ProductInfo>> entry = entries.next();
                List<ProductInfo> list = entry.getValue();
                if (list.contains(productInfo)) {
                    list.remove(productInfo);
                }
                if (list.size() == 0) {
                    entries.remove();
                    map.remove(entry.getKey());
                }
            }
            if (map.containsKey(Tools.getData())) {
                List<ProductInfo> productInfos = map.get(Tools.getData());
                productInfos.add(0, productInfo);
                map.put(Tools.getData(), productInfos);
            } else {
                List<ProductInfo> productInfos = new ArrayList<>();
                productInfos.add(productInfo);
                map.put(Tools.getData(), productInfos);
            }
            Tools.setDataMap(map);
        } else {
            List<ProductInfo> productInfos = new ArrayList<>();
            productInfos.add(productInfo);
            map.put(Tools.getData(), productInfos);
            Tools.setDataMap(map);
        }
    }

    /**
     * 保存MAP
     *
     * @param datalist
     */
    public static void setDataMap(Map<String, List<ProductInfo>> datalist) {
        if (null == datalist || datalist.size() <= 0)
            return;
        SharedPreferences sharedPreferences = YYApplication.getInstance().getSharedPreferences("youyunetlist", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        //转换成json数据，再保存
        String strJson = gson.toJson(datalist);
        editor.clear();
        editor.putString(PublicDef.SAVE_RECORD, strJson);
        editor.commit();
    }

    /**
     * 获取MAP
     *
     * @return
     */
    public static Map<String, List<ProductInfo>> getDataMap() {
        Map<String, List<ProductInfo>> map = new HashMap<>();
        SharedPreferences sharedPreferences = YYApplication.getInstance().getSharedPreferences("youyunetlist", 0);
        String strJson = sharedPreferences.getString(PublicDef.SAVE_RECORD, null);
        if (null == strJson) {
            return map;
        }
        Gson gson = new Gson();
        map = gson.fromJson(strJson, new TypeToken<Map<String, List<ProductInfo>>>() {
        }.getType());
        return map;
    }

    public static boolean writeBannerAdButtomVersion(int val) {
        SharedPreferences sharedPreferences = YYApplication.getInstance().getSharedPreferences("youyunet", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PublicDef.WEB_BANNERADBUTTOM_VERSION, val);
        return editor.commit();
    }

    public static int getBannerAdButtomVersion(int defVal) {
        SharedPreferences sharedPreferences = YYApplication.getInstance().getSharedPreferences("youyunet", 0);
        return sharedPreferences.getInt(PublicDef.WEB_BANNERADBUTTOM_VERSION, defVal);
    }
    //存储手机号为保存状态
    public static boolean writeIsSavePhone(String key) {
        SharedPreferences sharedPreferences = YYApplication.getInstance().getSharedPreferences("youyunet", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, true);
        return editor.commit();
    }
    //获取手机号的是否保存
    public static boolean getIsSavePhone(String key) {
        SharedPreferences sharedPreferences = YYApplication.getInstance().getSharedPreferences("youyunet", 0);
        return sharedPreferences.getBoolean(key, false);
    }



    /**
     * APP是否处于前台唤醒状态
     *
     * @return
     */
    public static boolean isAppOnForeground(Activity activity) {
        ActivityManager activityManager = (ActivityManager) activity.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = activity.getApplicationContext().getPackageName();
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }
        return false;
    }

    public static String getRunningActivityName() {
        ActivityManager manager = (ActivityManager) YYApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
        String activityName = manager.getRunningTasks(1).get(0).topActivity.getClassName();
        String name = activityName.substring(activityName.lastIndexOf(".") + 1, activityName.length());
        return name;
    }


    public static boolean writeConf(String name, String val) {
        SharedPreferences sharedPreferences = YYApplication.getInstance().getSharedPreferences("youyunet", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(name, val);
        return editor.commit();
    }

    public static boolean clenConf(String name, String val) {
        SharedPreferences sharedPreferences = YYApplication.getInstance().getSharedPreferences("youyunet", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        return editor.commit();
    }


    public static String getConf(String name, String defVal) {
        SharedPreferences sharedPreferences = YYApplication.getInstance().getSharedPreferences("youyunet", 0);
        return sharedPreferences.getString(name, defVal);

    }


    public static DataBean getDataBean(Context context) {
        try {
            String jsonStr = Tools.readFile(context, PublicDef.WEB_PROPERTY_FILE);
            Gson gson = new Gson();
            WebSetJsonData data = gson.fromJson(jsonStr, WebSetJsonData.class);
            if (data != null) {
                DataBean dataBean = data.getData();
                return dataBean;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void readLocalJson(Context context) {
        try {
            InputStreamReader inputReader = new InputStreamReader(YYApplication.getInstance().getResources().getAssets().open(PublicDef.LOCAL_JSON_FILENAME));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line = "";
            String result = "";
            while ((line = bufReader.readLine()) != null) {
                result += line;
            }
            String jsonStr = result.toString();
            Gson gson = new Gson();
            WebSetJsonData data = gson.fromJson(jsonStr, WebSetJsonData.class);
            DataBean dataBean = data.getData();
            int localVersion = Tools.getWebSetVersion(0);
            if (dataBean != null) {
                int currentVersion = dataBean.getVersion();
                if (localVersion < currentVersion) {
                    Tools.writeWebSetVersion(currentVersion);
                    try {
                        Tools.writeFile(context, PublicDef.WEB_PROPERTY_FILE, jsonStr);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            bufReader.close();
            inputReader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getWebSetVersion(int defVal) {
        SharedPreferences sharedPreferences = YYApplication.getInstance().getSharedPreferences("youyunet", 0);
        return sharedPreferences.getInt(PublicDef.WEB_PROPERTY_VERSION, defVal);
    }

    public static boolean writeWebSetVersion(int val) {
        SharedPreferences sharedPreferences = YYApplication.getInstance().getSharedPreferences("youyunet", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PublicDef.WEB_PROPERTY_VERSION, val);
        return editor.commit();
    }

    public static String getVersionName() {
        PackageManager packageManager = YYApplication.getInstance().getPackageManager();
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(YYApplication.getInstance().getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }

    public static boolean writeSubmitFlag(int val) {
        SharedPreferences sharedPreferences = YYApplication.getInstance().getSharedPreferences("youyunet", 0);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(PublicDef.CONF_SCORESUBMITFLAG, val);
        return editor.commit();
    }

    public static int getSubmitFlag(int defVal) {
        SharedPreferences sharedPreferences = YYApplication.getInstance().getSharedPreferences("youyunet", 0);
        return sharedPreferences.getInt(PublicDef.CONF_SCORESUBMITFLAG, defVal);
    }

    public static double getLoanImgscale(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        double imgscale = 0.5;
        int width = dm.widthPixels;
        if (width >= 1440) {
            imgscale = 1.0;
        } else if (width >= 1080 && width < 1440) {
            imgscale = 0.7;
        } else if (width >= 720 && width < 1080) {
            imgscale = 0.5;
        } else if (width >= 480 && width < 720) {
            imgscale = 0.3;
        }
        return imgscale;
    }

    /**
     * 验证手机格式
     */
    public static boolean isMobile(String number) {
        String num = "((1[3,5,8][0-9])|(14[5,7])|(17[0,6,7,8]))\\d{8}$";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        number=number.replaceAll(" ","");
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }

    /**
     * 所有参数按首字母排序，返回签名
     */
    public static String getSign(Map<String, String> map,String md5key ) {
        String sign = "";
        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        while (iter.hasNext()) {
            String key = iter.next();
            sign += key + "=" + map.get(key) + "&";
        }
        sign=sign+"md5key=" + md5key;
        return MD5(sign);
    }
}