package com.example.fuheng.toastdemo;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                showDialog();
            }
        }, 1000);

        ToastUtil.show(getApplicationContext(),
                isNotificationEnabled(getApplicationContext()) ? "通知打开了" : "通知关闭了");

    }

    public void toast(View v) {
        ToastUtil.show(this, "我是吐司");
    }


    public void goSet(View v) {
        JumpPermissionManagement.GoToSetting(this);
//        TestActivity.start(this, 18);
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("标题");
        builder.setMessage("消息");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * 用来判断是否开启通知权限
     */
    private boolean isNotificationEnabled(Context context) {
        return NotificationManagerCompat.from(context).areNotificationsEnabled();
    }
}
