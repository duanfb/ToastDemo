package com.example.fuheng.toastdemo;

import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void switcher(View v) {
        ToastUtil.show(getApplicationContext(), isNotificationEnabled(this) ? "通知打开了" : "通知关闭了");
    }

    public void toast(View v) {
        ToastUtil.show(this, "我是吐司");
    }


    public void goSet(View v) {
        JumpPermissionManagement.GoToSetting(this);
    }

    /**
     * 用来判断是否开启通知权限
     */
    private boolean isNotificationEnabled(Context context) {
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }
}
