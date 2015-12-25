package com.bluetoothlamp.tiny.tool;

import android.content.Context;
import android.util.Log;

import com.bluetoothlamp.tiny.data.BluetoothDevices;
import com.bluetoothlamp.tiny.data.CloudUpdate;

import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Tiny on 4/16/2015.
 * 同步用户数据的类
 */
public class OptionDate2Cloud
{
    private boolean flag;
    private static OptionDate2Cloud ourInstance = new OptionDate2Cloud();

    public static OptionDate2Cloud getInstance()
    {
        return ourInstance;
    }

    private OptionDate2Cloud()
    {
    }

    public void saveDate2Cloud(Context context)
    {
        CloudUpdate cp = new CloudUpdate();
        BluetoothDevices bluetoothDevices = BluetoothDevices.findById(BluetoothDevices.class, 1);
        cp.setBluetoothLampName(bluetoothDevices.getBluetoothLampName());
        cp.setMediaDeviceName(bluetoothDevices.getBluetoothTVName());
        cp.save(context, new SaveListener()
        {
            @Override
            public void onSuccess()
            {
                Log.d("------","gggggggg");
            }

            @Override
            public void onFailure(int i, String s)
            {
                Log.d("------","xxxxxxxx");
            }
        });
    }

    public boolean checkUserByCloud(String username, String passWord)
    {
        return this.flag;
    }
}
