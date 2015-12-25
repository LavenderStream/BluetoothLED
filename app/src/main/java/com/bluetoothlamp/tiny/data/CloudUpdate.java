package com.bluetoothlamp.tiny.data;

import cn.bmob.v3.BmobObject;

/**
 * Created by Tiny on 4/16/2015.、
 * 云同步的bean
 */
public class CloudUpdate extends BmobObject
{
    //健康设备模块
    private int drinkWaterTarget;
    private int drinkWaterDone;

    // 个人信息资料
    private String photoPathUil;
    private String username;
    private String userPassWord;
    private String bluetoothLampName;
    private String mediaDeviceName;

    public int getDrinkWaterTarget()
    {
        return drinkWaterTarget;
    }

    public void setDrinkWaterTarget(int drinkWaterTarget)
    {
        this.drinkWaterTarget = drinkWaterTarget;
    }

    public int getDrinkWaterDone()
    {
        return drinkWaterDone;
    }

    public void setDrinkWaterDone(int drinkWaterDone)
    {
        this.drinkWaterDone = drinkWaterDone;
    }

    public String getPhotoPathUil()
    {
        return photoPathUil;
    }

    public void setPhotoPathUil(String photoPathUil)
    {
        this.photoPathUil = photoPathUil;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getBluetoothLampName()
    {
        return bluetoothLampName;
    }

    public void setBluetoothLampName(String bluetoothLampName)
    {
        this.bluetoothLampName = bluetoothLampName;
    }

    public String getMediaDeviceName()
    {
        return mediaDeviceName;
    }

    public void setMediaDeviceName(String mediaDeviceName)
    {
        this.mediaDeviceName = mediaDeviceName;
    }

    public String getUserPassWord()
    {
        return userPassWord;
    }

    public void setUserPassWord(String userPassWord)
    {
        this.userPassWord = userPassWord;
    }
}
