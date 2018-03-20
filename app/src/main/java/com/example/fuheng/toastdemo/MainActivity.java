package com.example.fuheng.toastdemo;

import android.content.Context;
import android.support.v4.app.NotificationManagerCompat;
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
        Toast.makeText(this, isNotificationEnabled(this) ? "通知打开了" : "通知关闭了", 0).show();
    }

    public void toast(View v) {
        Toast.makeText(this, "我是吐司", 0).show();
    }

    /**
     * 用来判断是否开启通知权限
     */
    private boolean isNotificationEnabled(Context context) {
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }
}
