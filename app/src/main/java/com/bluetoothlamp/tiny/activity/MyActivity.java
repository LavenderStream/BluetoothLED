/**@auther Tiny
 * 这是一个智能家电的客户端w+（我家）
 * characteristic：
 * 1、statue与action色彩融合
 * 2、折纸效果的侧边栏
 * 3、监控身体的图表分析
 * 4、蓝牙台灯的控制
 * 5、多端操控的体验
 * 6、操控tv的遥控器面板
 *
 * 侧边栏的数组放在ResourceInfo文件中，在DrawerAdapter里添加对侧边栏的点击事件*/
package com.bluetoothlamp.tiny.activity;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.bluetoothlamp.tiny.data.BluetoothDevices;
import com.bluetoothlamp.tiny.utils.ApplicationUtils;
import com.bluetoothlamp.tiny.view.actionbar.BlurFoldingActionBarToggle;
import com.bluetoothlamp.tiny.view.foldingdrawer.DrawerFragment;
import com.bluetoothlamp.tiny.view.foldingdrawer.FoldingDrawerLayout;
import com.bluetoothlamp.tiny.view.actionbar.SystemBarTintManager;
import com.bluetoothlamp.tiny.view.foldingdrawer.ISliderItmeClickListener;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import cn.bmob.v3.Bmob;


/**
 * 程序运行的主界面
 */
public class MyActivity extends FragmentActivity
{
    private Effectstype mEffectstype;
    // 这个类是绘制折叠侧边栏的类，但是实现效果是FoldingDrawerLayout和ActionBarDrawerToggle共同的作用
    private FoldingDrawerLayout mDrawerLayout = null;
    private ActionBarDrawerToggle mDrawerToggle;
    // 这是一个模糊的图片， 是客户端的背景，实现的就是透明白色毛玻璃效果的客户端主界面
    private ImageView mBlurImage;
    private FrameLayout mContentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        Bmob.initialize(this, "fc979187c0d1c8b9d5ccfe22051554d8");
        // 核对用户信息，当第一次登入的时候跳转到登入界面
        if (!checkUserInfomation())
        {
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            this.finish();
        }
        else
        {
            setContentView(R.layout.activity_main);
            // 初始化状态栏和actionbar
            initActionBarAndState();
            // 客户端侧边栏的初始化
            initSlideWindow();
        }
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
        tintManager.setStatusBarTintResource(R.color.actionbar_bg);
        // 取得actionbar这是返回事件，actionbar的标题
        ActionBar actionBar = getActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ff33b5e5")));
        actionBar.setTitle(((ApplicationUtils) getApplication()).getActionbarTitleText());
    }


    /**
     * @author Tiny
     * 初始化侧边栏，侧边栏的点击事件，向右滑动带有折纸效果动画展开侧边栏，侧边栏上放有listview，
     * 每行listview上是一个itme
     */
    private void initSlideWindow()
    {
        this.mDrawerLayout = (FoldingDrawerLayout) findViewById(R.id.drawer_layout);
        // 加载中间的内容
        this.mContentLayout = (FrameLayout) findViewById(R.id.content_frame);
        // 毛玻璃效果
        this.mBlurImage = (ImageView) findViewById(R.id.blur_image);
        // 这是客户端界面的颜色
        this.mDrawerLayout.setScrimColor(Color.argb(100, 255, 255, 255));
        // 继承了ActionBarToggle的工具类，监听侧边栏的开启和关闭的效果
        this.mDrawerToggle = new BlurFoldingActionBarToggle(this, mDrawerLayout, R.drawable.ic_drawer, R.string.drawer_open, R.string.drawer_close)
        {
            @Override
            public void onDrawerOpened(View view)
            {
                super.onDrawerOpened(view);
            }

            @Override
            public void onDrawerClosed(View view)
            {
                super.onDrawerClosed(view);
                // 关闭之后恢复原来的背景
                mBlurImage.setImageBitmap(null);
            }
        };
        ((BlurFoldingActionBarToggle) mDrawerToggle).setBlurImageAndView(mBlurImage, mContentLayout);
        this.mDrawerLayout.setDrawerListener(mDrawerToggle);

        // 设置侧边栏中按钮的监听，以碎片的形式添加到现实窗口中
        DrawerFragment drawerFragment = new DrawerFragment();
        LampActivity lamp = new LampActivity();
        replaceFragment(R.id.content_frame, lamp);
        drawerFragment.setListener(new ISliderItmeClickListener()
        {
            @Override
            public void onItmeClick(int position)
            {
                switch (position)
                {
                    case ApplicationUtils.SLIDER_ITEM_LAMP:
                        LampActivity lamp = new LampActivity();
                        replaceFragment(R.id.content_frame, lamp);
                        break;
                    case ApplicationUtils.SLIDER_ITEM_TV:
                        TvActivity tvActivity = new TvActivity();
                        replaceFragment(R.id.content_frame, tvActivity);
                        break;
                    case ApplicationUtils.SLIDER_ITEM_HEALTH:
                        HeathActivity heathActivity = new HeathActivity();
                        replaceFragment(R.id.content_frame, heathActivity);
                        break;
                    case ApplicationUtils.SLIDER_ITEM_ABOUT:
                        AboutActivity abActivity = new AboutActivity();
                        replaceFragment(R.id.content_frame, abActivity);
                        break;
                    case ApplicationUtils.SLIDER_ITEM_Setting:
                        SettingActivity stActivity = new SettingActivity();
                        replaceFragment(R.id.content_frame, stActivity);
                    default:
                        break;
                }
                // 20mm后延迟执行，不然转换为TV界面的时候会卡
                new Thread(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            Thread.sleep(30);
                            runOnUiThread(new Runnable()
                            {
                                @Override
                                public void run()
                                {
                                    mDrawerLayout.closeDrawers();
                                }
                            });
                        }
                        catch (InterruptedException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });
        // DrawerFragment继承了Fragment，把这个碎片加载到侧边栏上，中间包含一个listview
        replaceFragment(R.id.left_drawer, drawerFragment);
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


    /**
     * @author Tiny
     * 把这个碎片加载到侧边栏上
     */
    private void replaceFragment(int viewId, Fragment fragment)
    {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(viewId, fragment).commit();
    }


    /**
     * @author Tiny
     * 重写了返回键，当按下返回键时，弹出确认退出的窗口
     */
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
        {
            final NiftyDialogBuilder dialogBuilder = NiftyDialogBuilder.getInstance(this);
            mEffectstype = Effectstype.Fall;

            dialogBuilder.withTitle(this.getResources().getString(R.string.dialog_title))
                    .withTitleColor("#FFFFFF")
                    .withDividerColor("#11000000")
                    .withIcon(getResources().getDrawable(R.drawable.icon)).withMessage(
                    this.getResources().getString(R.string.dialog_hint))
                    .withMessageColor("#FFFFFFFF")
                    .withDialogColor("#ff33b5e5")
                    .isCancelableOnTouchOutside(true)
                    .withDuration(0)
                    .withButton1Text(this.getResources().getString(R.string.OK))
                    .withButton2Text(this.getResources().getString(R.string.Cancel))
                    .setButton1Click(new View.OnClickListener()
                    {
                        @Override
                        public void onClick(View v)
                        {
                            System.exit(0);
                        }
                    }).setButton2Click(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    dialogBuilder.dismiss();
                }
            }).show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * @author Tiny
     * 当menu命令发生请求是调用此方法
     * R.id.右上角home是客户端角的退出按键，根据客户端侧边栏所在的状态来判断展开与否
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case android.R.id.home:
                if (!this.mDrawerLayout.isDrawerVisible(Gravity.LEFT))
                {
                    this.mDrawerLayout.openDrawer(Gravity.LEFT);
                }
                else
                {
                    this.mDrawerLayout.closeDrawer(Gravity.LEFT);
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }

    /**
     * 核对用户是否是第一次登入 false为第一次登入
     * @return boolean
     */
    private boolean checkUserInfomation()
    {
        boolean flag = false;
        if (BluetoothDevices.findById(BluetoothDevices.class, 1).getUserName() == null)
        {
            flag = false;
        }
        else
        {
            flag = true;
        }
        return flag;
    }
}
