package com.bluetoothlamp.tiny.activity;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.bluetoothlamp.tiny.utils.ApplicationUtils;
import com.bluetoothlamp.tiny.view.actionbar.SystemBarTintManager;

/**
 * 之前想开发的一个注册功能，但是后来也没用就放在这了
 */
public class RegisterActivity extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initActionBarAndState();
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
        tintManager.setStatusBarTintResource(R.color.holo_green_dark);
        // 取得actionbar这是返回事件，actionbar的标题
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#cc0000")));
        actionBar.setTitle(((ApplicationUtils) getApplication()).getActionbarTitleText());
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
