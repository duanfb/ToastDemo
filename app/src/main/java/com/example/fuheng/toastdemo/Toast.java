package com.example.fuheng.toastdemo;

import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;
import android.view.View;

/**
 * @author neo.duan
 * @date 2018/3/23
 * @desc 解决5.0+系統关闭吐司 thanks:https://github.com/Blincheng/EToast2
 */
public class Toast {
    private Object mToast;

    private Toast(Context context, String message, int duration) {
        boolean enabled = isNotificationEnabled(context);
        if (enabled) {
            mToast = android.widget.Toast.makeText(context, message, duration);
        } else {
            mToast = EToast2.makeText(context, message, duration);
        }
    }

    public static Toast makeText(Context context, String message, int duration) {
        return new Toast(context, message, duration);
    }

    public static Toast makeText(Context context, int resId, int duration) {
        return makeText(context, context.getResources().getString(resId), duration);
    }

    public void show() {
        if (mToast instanceof EToast2) {
            ((EToast2) mToast).show();
        } else if (mToast instanceof android.widget.Toast) {
            ((android.widget.Toast) mToast).show();
        }
    }

    public void cancel() {
        if (mToast instanceof EToast2) {
            ((EToast2) mToast).cancel();
        } else if (mToast instanceof android.widget.Toast) {
            ((android.widget.Toast) mToast).cancel();
        }
    }

    public void setText(CharSequence s) {
        if (mToast instanceof EToast2) {
            ((EToast2) mToast).setText(s);
        } else if (mToast instanceof android.widget.Toast) {
            ((android.widget.Toast) mToast).setText(s);
        }
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        if (mToast instanceof EToast2) {
            ((EToast2) mToast).setGravity(gravity, xOffset, yOffset);
        } else if (mToast instanceof android.widget.Toast) {
            ((android.widget.Toast) mToast).setGravity(gravity, xOffset, yOffset);
        }
    }

    public void setView(View view) {
        if (mToast instanceof EToast2) {
            ((EToast2) mToast).setView(view);
        } else if (mToast instanceof android.widget.Toast) {
            ((android.widget.Toast) mToast).setView(view);
        }
    }

    /**
     * 用来判断是否开启通知权限
     */
    private static boolean isNotificationEnabled(Context context) {
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }
}