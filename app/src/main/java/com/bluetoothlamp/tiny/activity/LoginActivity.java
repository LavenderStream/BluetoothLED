package com.bluetoothlamp.tiny.activity;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import com.bluetoothlamp.tiny.data.BluetoothDevices;
import com.bluetoothlamp.tiny.utils.ApplicationUtils;
import com.bluetoothlamp.tiny.view.actionbar.SystemBarTintManager;
import com.bluetoothlamp.tiny.view.material.MaterialButton;

/**
 * 登入界面，当设备第一次运行的时候会运行这个activity
 */
public class LoginActivity extends Activity
{
    // 用户名字的输入框
    private EditText mUserNameEdit;
    // 用户密码的输入框
    private EditText mPassWordEdit;
    // 点击预览密码的button
    private Button mShowPassWord;
    // 登入按钮
    private MaterialButton mMaterialButton;

    private String mUserName;
    private String mPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // 初始化actionbar和状态栏
        initActionBarAndState();

        this.mUserNameEdit = (EditText) findViewById(R.id.login_username_edit);
        this.mPassWordEdit = (EditText) findViewById(R.id.login_passwd_edit);
        this.mMaterialButton = (MaterialButton) findViewById(R.id.login_button);
        this.mShowPassWord = (Button) findViewById(R.id.showpassword);

        this.mMaterialButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mUserName = mUserNameEdit.getText().toString();
                mPasswd = mPassWordEdit.getText().toString();
                // 存数要用户名和密码
                BluetoothDevices mBluetoothDevices = BluetoothDevices.findById(BluetoothDevices.class, 1);
                mBluetoothDevices.setUserName(mUserName);
                mBluetoothDevices.setPassWord(mPasswd);
                mBluetoothDevices.save();
                // 将用户名和密码存放在数据库中
                if(mUserName != null && mPasswd != null)
                {
                    Intent intent = new Intent(LoginActivity.this, MyActivity.class);
                    startActivity(intent);
                    LoginActivity.this.finish();
                }
            }
        });

        // 控制密码显示/隐藏
        this.mShowPassWord.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        //mPassWordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        mPassWordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        break;
                    case MotionEvent.ACTION_UP:
                        //mPassWordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        mPassWordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        Editable etext = mPassWordEdit.getText();
                        Selection.setSelection(etext, etext.length());
                        break;
                    default:
                        break;
                }
                return false;
            }
        });
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
        tintManager.setStatusBarTintResource(R.color.holo_red_dark);
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
