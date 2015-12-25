package com.bluetoothlamp.tiny.data;


import com.orm.SugarRecord;
import com.orm.dsl.Table;

/**
 * Created by Tiny on 1/2/2015.
 * 映射到数据库的bean
 */
@Table(name = "BluetoothDevices")
public class BluetoothDevices extends SugarRecord
{
    private String mBluetoothLampName;
    private String mBluetoothTVName;
    private String mBluetoothHeathName;
    private String passWord;
    private String userName;

    public String getUserName()
    {
        return userName;
    }

    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public void setBluetoothLampName(String mBluetoothLampName)
    {
        this.mBluetoothLampName = mBluetoothLampName;
    }

    public void setBluetoothTVName(String mBluetoothTVName)
    {
        this.mBluetoothTVName = mBluetoothTVName;
    }

    public void setBluetoothHeathName(String getmBluetoothHeathName)
    {
        this.mBluetoothHeathName = getmBluetoothHeathName;
    }

    public String getBluetoothLampName()
    {
        return mBluetoothLampName;
    }

    public String getBluetoothTVName()
    {
        return mBluetoothTVName;
    }

    public String getBluetoothHeathName()
    {
        return mBluetoothHeathName;
    }

    public String getPassWord()
    {
        return passWord;
    }

    public void setPassWord(String passWord)
    {
        this.passWord = passWord;
    }
}
