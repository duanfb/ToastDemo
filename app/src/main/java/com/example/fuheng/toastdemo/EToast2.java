package com.example.fuheng.toastdemo;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.ViewCompat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Author: Blincheng.
 * Date: 2017/6/30.
 * Description:EToast2.0
 */

public class EToast2 {

    public static final int LENGTH_SHORT = 0;
    public static final int LENGTH_LONG = 1;

    private static final int SHOW = 0;
    private static final int HIDE = 1;
    private static final int CANCEL = 2;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams mParams;

    private Long mDelay = 2000L;
    private View mView;
    private View mNextView;

    private int mGravity;
    int mX, mY;

    private CharSequence mText;
    private int mDuration;

    private Handler mHandler;
    private Timer mTimer;

    private EToast2(Context context, CharSequence text, int duration) {
        this.mDuration = duration;
        this.mText = text;
        init(context);
    }

    private void init(Context context) {
        if (mWindowManager == null) {
            mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }

        if (mHandler == null) {
            mHandler = new ToastHandler(this);
        }

        if (mDuration == EToast2.LENGTH_SHORT) {
            this.mDelay = 2000L;
        } else if (mDuration == EToast2.LENGTH_LONG) {
            this.mDelay = 3500L;
        }

        LayoutInflater inflate = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.transient_notification, null);
        TextView tv = v.findViewById(R.id.message);
        tv.setText(mText);

        this.mNextView = v;
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
        params.type = WindowManager.LayoutParams.TYPE_TOAST;
        params.setTitle("EToast2");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        final Configuration config = mView.getContext().getResources().getConfiguration();
        final int gravity = Gravity.getAbsoluteGravity(mGravity, config.getLayoutDirection());
        params.gravity = gravity;
        if ((gravity & Gravity.HORIZONTAL_GRAVITY_MASK) == Gravity.FILL_HORIZONTAL) {
            params.horizontalWeight = 1.0f;
        }
        if ((gravity & Gravity.VERTICAL_GRAVITY_MASK) == Gravity.FILL_VERTICAL) {
            params.verticalWeight = 1.0f;
        }

        params.x = mX;
        params.y = mY;

        return params;
    }

    public void setText(CharSequence s) {
        if (mNextView == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        TextView tv = mNextView.findViewById(R.id.message);
        if (tv == null) {
            throw new RuntimeException("This Toast was not created with Toast.makeText()");
        }
        tv.setText(s);
    }

    public void setGravity(int gravity, int xOffset, int yOffset) {
        this.mGravity = gravity;
        this.mX = xOffset;
        this.mY = yOffset;
    }

    public void setView(View view) {
        mNextView = view;
    }

    /**
     * 发送显示消息Toast
     */
    public void show() {
        mHandler.obtainMessage(SHOW).sendToTarget();
    }

    public void cancel() {
        mHandler.obtainMessage(CANCEL).sendToTarget();
    }


    /**
     * 处理显示逻辑
     */
    private void handleShow() {
        // the window token is already invalid and no need to do any work.
        if (mHandler.hasMessages(CANCEL) || mHandler.hasMessages(HIDE)) {
            return;
        }

        if (mView != mNextView) {
            // remove the old view if necessary
            handleHide();
            mView = mNextView;

            if (mParams == null) {
                mParams = createParams();
            }

            if (!ViewCompat.isAttachedToWindow(mView)) {
                mWindowManager.addView(mView, mParams);
            }
            if (mTimer != null) {
                mTimer.cancel();
            }
            mTimer = new Timer();
            mTimer.schedule(new TimerTask() {

                @Override
                public void run() {
                    mHandler.sendEmptyMessage(HIDE);
                }
            }, mDelay);
        }
    }

    /**
     * 隐藏Toast
     */
    private void handleHide() {
        if (mView != null) {
            if (mView.getParent() != null) {
                if (mView != null && ViewCompat.isAttachedToWindow(mView)) {
                    mWindowManager.removeView(mView);
                }
            }
            mView = null;
        }
    }

    private void handleCancel() {
        handleHide();
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }


    /**
     * 处理吐司隐藏Handler
     */
    private static class ToastHandler extends Handler {
        WeakReference<EToast2> mToastReference;

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
                    switch (msg.what) {
                        case SHOW:
                            toast.handleShow();
                            break;
                        case HIDE:
                            toast.handleHide();
                            break;
                        case CANCEL:
                            toast.handleCancel();
                            break;
                        default:
                            break;
                    }

                }
            }
        }
    }
}