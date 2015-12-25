package com.bluetoothlamp.tiny.utils;

import android.content.Context;

import com.bluetoothlamp.tiny.activity.R;
import com.bluetoothlamp.tiny.data.BluetoothDevices;
import com.orm.SugarContext;
import com.orm.SugarRecord;

/**
 * Created by Tiny on 14-3-24.
 */
public class ApplicationUtils extends android.app.Application
{
    private static Context sContext;
    private String ActionbarTitleText;
    public static String[] SlideListItme;

    public String getActionbarTitleText()
    {
        return ActionbarTitleText;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        // 标题栏中显示程序的名字
        this.ActionbarTitleText = sContext.getString(R.string.app_name);
        //显示在滑动侧边栏的listview中的栏目, 可在这里增加栏目
        this.SlideListItme = new String[]{sContext.getString(R.string.Lamp)
                ,sContext.getString(R.string.TV)
                ,sContext.getString(R.string.Health)
                ,sContext.getString(R.string.Information)
                ,sContext.getString(R.string.Setting)};

        SugarContext.init(this);

        // 若数据库中没有数据，将预定义数据保存
        if (BluetoothDevices.findById(BluetoothDevices.class, 1) == null)
        {
            BluetoothDevices mBluetoothDevices = new BluetoothDevices();
            mBluetoothDevices.setBluetoothHeathName("iheath");
            mBluetoothDevices.setBluetoothLampName("arduino lamp");
            mBluetoothDevices.setBluetoothTVName("Tiny");
            SugarRecord.save(mBluetoothDevices);
        }
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        SugarContext.terminate();
    }

    public static Context getContext() {
        return sContext;
    }

    // 侧边栏的标记，myactivity通过这个来
    public static final int SLIDER_ITEM_LAMP = 0;
    public static final int SLIDER_ITEM_TV = 1;
    public static final int SLIDER_ITEM_HEALTH = 2;
    public static final int SLIDER_ITEM_ABOUT = 3;
    public static final int SLIDER_ITEM_Setting = 4;
}
