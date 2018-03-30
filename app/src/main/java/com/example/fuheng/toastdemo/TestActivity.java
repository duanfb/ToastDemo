package com.example.fuheng.toastdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    private static final String AGE = "age";
    private int age;

    public static void start(Context context, int age) {
        Intent intent = new Intent(context, TestActivity.class);
        intent.putExtra(AGE, age);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        age = getIntent().getIntExtra(AGE, 0);
    }

}
