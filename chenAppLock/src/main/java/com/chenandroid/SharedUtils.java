package com.chenandroid;

import android.content.Context;
import android.content.SharedPreferences;

/****
 * 该类是操作SP的类
 *
 * @author michaelwei
 */
public class SharedUtils {

    private static SharedPreferences sp;
    public static String SP_NAME = "config";
    /**
     * 从SP中获取int类型的值
     */
    public static int getInt(String key, int defValue) {
        if (sp == null) {
            sp = MyApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return sp.getInt(key, defValue);
    }

    /**
     * 往SP中存入int类型的值
     */
    public static void putInt(String key, int value) {
        if (sp == null) {
            sp =MyApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        sp.edit().putInt(key, value).commit();
    }


    /**
     * 从SP中获取String字符串
     */
    public static String getString(String key, String defValue) {
        if (sp == null) {
            sp =MyApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }

    /**
     * 从SP中获取String字符串
     */
    public static String getString(String key) {
        if (sp == null) {
            sp = MyApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return sp.getString(key, "");
    }

    /**
     * 往SP中存入String字符串
     */
    public static void putString(String key, String value) {
        if (sp == null) {
            sp = MyApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }

    /**
     * 从SP中获取boolean值
     **/
    public static boolean getBoolean(String key, boolean defValue) {
        if (sp == null) {
            sp = MyApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }

    /**
     * 往SP中存入boolean值
     */
    public static void putBoolean(String key, boolean value) {
        if (sp == null) {
            sp =MyApplication.getInstance().getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }


}
