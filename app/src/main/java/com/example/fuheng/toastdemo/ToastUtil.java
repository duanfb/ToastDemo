package com.example.fuheng.toastdemo;

import android.content.Context;
import android.view.Gravity;


/**
 * Author: neo.duan
 * Date: 2017/02/18
 * Desc: 吐司工具类
 */
public class ToastUtil {

    public static Toast toast;

    private ToastUtil() {
        throw new AssertionError();
    }
    
    public static void show(Context context, String text){
        show(context, text, EToast2.LENGTH_SHORT);
    }

    private static void show(Context context, String text, int duration) {
        if (toast == null) {
            toast = Toast.makeText(context, text,duration);
        } else {
            toast.setText(text);
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public static void cancel() {
        if (toast != null) {
            toast.cancel();
        }
    }
}