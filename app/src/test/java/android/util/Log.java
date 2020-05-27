package android.util;

/**
 * Created by XinHeng on 2018/12/08.
 * describe：
 */
public class Log {
    public static int e(String tag, String msg) {
        System.out.println(tag + ", msg=" + msg);
        return 0;
    }
    public static int i(String tag, String msg) {
        System.out.println(tag + ", msg=" + msg);
        return 0;
    }
}
