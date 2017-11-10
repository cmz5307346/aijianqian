package com.ajq.aijieqian102.JPush;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ajq.aijieqian102.activity.MainActivity;
import com.ajq.aijieqian102.constant.PublicDef;

import cn.jpush.android.api.JPushInterface;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle bundle = intent.getExtras();

            // Log.e(TAG, "[MyReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));
            if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                //Logger.d(TAG, "[MyReceiver] 用户点击打开了通知");
                Intent newintent = new Intent(context, MainActivity.class);
                JsonObject json = new JsonParser().parse(bundle.getString(JPushInterface.EXTRA_EXTRA)).getAsJsonObject();
                if (json.get(PublicDef.PUSH_ASSIGN_TABID) != null && !json.get(PublicDef.PUSH_ASSIGN_TABID).isJsonNull()) {
                    String tabID = json.get(PublicDef.PUSH_ASSIGN_TABID).getAsString();
                    newintent.putExtra(PublicDef.PUSH_ASSIGN_TABID, tabID);
                } else if (json.get(PublicDef.PUSH_ASSIGN_URL) != null && !json.get(PublicDef.PUSH_ASSIGN_URL).isJsonNull()) {
                    String url = json.get(PublicDef.PUSH_ASSIGN_URL).getAsString();
                    newintent.putExtra(PublicDef.PUSH_ASSIGN_URL, url);
                }
                newintent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                context.startActivity(newintent);
            }
        } catch (Exception e) {

        }
    }

}
