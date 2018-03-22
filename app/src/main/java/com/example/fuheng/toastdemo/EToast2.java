package com.example.fuheng.toastdemo;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * Author: Blincheng.
 * Date: 2017/6/30.
 * Description:EToast2.0
 */

public class EToast2 {

    private final BlockingQueue<Runnable> sPoolWorkQueue =
            new LinkedBlockingQueue<>(128);

    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, "EToast2 #" + mCount.getAndIncrement());
        }
    };

    private WindowManager windowManager;
    private Long time = 2000L;
    private View contentView;
    private WindowManager.LayoutParams params;
    private Toast oldToast;
    public static final int LENGTH_SHORT = 0;
    public static final int LENGTH_LONG = 1;
    private Handler handler;
    private Timer timer;
    private CharSequence text;
    private int duration;

    private EToast2(Context context, CharSequence text, int duration) {
        this.text = text;
        this.duration = duration;
        init(context);
    }

    private void init(Context context) {
        if (windowManager == null) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }

        if (duration == EToast2.LENGTH_SHORT) {
            this.time = 2000L;
        } else if (duration == EToast2.LENGTH_LONG) {
            this.time = 3500L;
        }

        if (oldToast == null) {
            Toast toast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
            oldToast = toast;
            contentView = toast.getView();
            params = createParams();
        }

        if (handler == null) {
            handler = new ToastHandler(this);
        }
    }

    public static EToast2 makeText(Context context, String text, int duration) {
        return new EToast2(context, text, duration);
    }

    public static EToast2 makeText(Context context, int resId, int duration) {
        return makeText(context, context.getText(resId).toString(), duration);
    }

    private WindowManager.LayoutParams createParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.windowAnimations = R.style.MyToast;
        params.setTitle("EToast2");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER;
        params.y = 200;

        return params;
    }

    public void show() {
        oldToast.setText(text);
        if (!ViewCompat.isAttachedToWindow(contentView)) {
            windowManager.addView(contentView, params);
        }
        if (timer != null) {
            timer.cancel();
        }
        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                handler.sendEmptyMessage(1);
            }
        }, time);
    }

    private void hide() {
        if (contentView != null && ViewCompat.isAttachedToWindow(contentView)) {
            windowManager.removeView(contentView);
        }
    }

    public void cancel() {
        try {
            windowManager.removeView(contentView);
        } catch (IllegalArgumentException e) {
            //这边由于上下文被销毁后removeView可能会抛出IllegalArgumentException
            //暂时这么处理，因为EToast2是轻量级的，不想和Context上下文的生命周期绑定在一块儿
            //其实如果真的想这么做，可以参考博文2的第一种实现方式，添加一个空的fragment来做生命周期绑定
        }
        oldToast.cancel();
        oldToast = null;
        contentView = null;
        handler = null;
    }

    public void setText(CharSequence s) {
        this.text = s;
    }

    /**
     * 处理吐司隐藏Handler
     */
    private static class ToastHandler extends Handler {
        WeakReference<EToast2 > mToastReference;

        private ToastHandler(EToast2 toast) {
            super(Looper.getMainLooper());
            mToastReference = new WeakReference<>(toast);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mToastReference != null) {
                EToast2 toast = mToastReference.get();
                if (toast != null) {
                    toast.hide();
                }
            }
        }
    }
}