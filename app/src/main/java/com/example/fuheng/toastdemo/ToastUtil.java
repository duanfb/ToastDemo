package com.example.fuheng.toastdemo;

import android.content.Context;
import android.view.Gravity;
import android.widget.Toast;


/**
 * Toast工具类
 * Created by 田慧楠 on 2017/03/23
 */

public class ToastUtil {

    private static com.example.fuheng.toastdemo.Toast  toast;

    /**
     * 气泡展示
     */
    public static void show(Context context, String msg) {
        if (toast == null) {
            toast = com.example.fuheng.toastdemo.Toast.makeText(context, msg,
                    Toast.LENGTH_SHORT);
        } else {
            toast.setText(String.valueOf(msg));
        }
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }
}
