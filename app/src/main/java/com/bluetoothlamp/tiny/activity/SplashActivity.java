package com.bluetoothlamp.tiny.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.bluetoothlamp.tiny.view.actionbar.SystemBarTintManager;

/**
 *程序的加载时的画面
 */
public class SplashActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        // 初始化actionbar和状态栏
        initActionBarAndState();
        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                Intent mainIntent = new Intent(SplashActivity.this, MyActivity.class);
                //跳转到MainActivity
                SplashActivity.this.startActivity(mainIntent);
                //结束SplashActivity
                SplashActivity.this.finish();
            }
            //给postDelayed()方法传递延迟参数
        }, 1600);
    }

    /**
     * @author Tiny
     * 初始化状态栏和actonbar
     */
    private void initActionBarAndState()
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
        {
            setTranslucentStatus(true);
        }
        SystemBarTintManager tintManager = new SystemBarTintManager(this);
        tintManager.setStatusBarTintEnabled(true);
        tintManager.setStatusBarTintResource(R.color.white);
    }
    /**
     * @author Tiny
     * 设置半透明状态栏
     */
    @TargetApi(19)
    private void setTranslucentStatus(boolean on)
    {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on)
        {
            winParams.flags |= bits;
        }
        else
        {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

}
