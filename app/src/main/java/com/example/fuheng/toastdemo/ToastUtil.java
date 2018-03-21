package com.example.fuheng.toastdemo;

import android.content.Context;



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
        toast.show();


//        Toast.makeText(context,text,duration);

        //see:https://github.com/johnkil/Android-AppMsg
//        可设置动画，自定义View，弹出优先级等等

//        AppMsg appMsg = AppMsg.makeText(context, text, AppMsg.STYLE_ALERT);
//        appMsg.setLayoutGravity(Gravity.CENTER);
//        appMsg.setParent(R.id.fl_msg_content);
//        appMsg.setAnimation(R.anim.slide_in_top, R.anim.slide_out_top);
//        switch (duration) {
//            case Toast.LENGTH_SHORT:
//                appMsg.setDuration(AppMsg.LENGTH_SHORT);
//                break;
//            case Toast.LENGTH_LONG:
//                appMsg.setDuration(AppMsg.LENGTH_LONG);
//                break;
//        }
//        appMsg.show();
    }

    public static void cancel() {
        if (toast != null) {
            toast.cancel();
        }
    }
}