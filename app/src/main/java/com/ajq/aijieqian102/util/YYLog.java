package com.ajq.aijieqian102.util;

import android.util.Log;

/**
 * Created by huangchunxin on 16/10/11.
 */

public class YYLog {
    public static final String TAG = "youyunet";
    public static final boolean SHOW_LOG = true;
    public static final boolean SHOW_Z_LOG = true;

    public static int v(String msg) {
        if (!SHOW_LOG) {
            return 0;
        }
        return Log.v(TAG, msg);
    }

    public static int i(String msg) {
        if (!SHOW_LOG) {
            return 0;
        }
        return Log.i(TAG, msg);
    }

    public static int d(String msg) {
        if (!SHOW_LOG) {
            return 0;
        }
        return Log.d(TAG, msg);
    }

    public static int w(String msg) {
        if (!SHOW_LOG) {
            return 0;
        }
        return Log.w(TAG, msg);
    }

    public static int e(String msg) {
        if (!SHOW_LOG) {
            return 0;
        }
        return Log.e(TAG, msg);
    }

    public static int ez(String msg) {
        if (!SHOW_Z_LOG) {
            return 0;
        }
        return Log.e("zzz", msg);
    }

    public static int ez(String tag, String msg) {
        if (!SHOW_Z_LOG) {
            return 0;
        }
        return Log.e(tag, msg);
    }
}
