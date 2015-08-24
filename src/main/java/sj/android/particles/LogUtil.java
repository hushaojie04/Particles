package sj.android.particles;

import android.util.Log;

/**
 * Created by Administrator on 2015/8/9.
 */
public class LogUtil {
    public static final boolean ON = true;

    public static void L(String msg) {
        Log.i("log", msg);
    }

    public static void L(String tag, String msg) {
        Log.i(tag, msg);
    }
}
