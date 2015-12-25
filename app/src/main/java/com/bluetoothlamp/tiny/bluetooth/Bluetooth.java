/**
 * Created by Tiny on 2014/11/2.
 * 封装蓝牙的连接类
 * @deprecated
 * final BluetoothSocket socket = Bluetooth.getInstance().getBlueToothSocket(mBluetoothDeviceName);
 * BluetoothAdapter mmBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
 * mmBluetoothAdapter.cancelDiscovery();
 * if (Bluetooth.getInstance().isBluetoothOpen() && socket != null)
 * {
 *new Thread(new Runnable()
 * @Override public void run()
 * {
 * try
 * {
 * {
 * TvActivity.this.mBluetoothSocket = socket;
 * TvActivity.this.mBluetoothSocket.connect();
 *
 * android 5.0 + 之后的系统，蓝牙连接发现不稳定，所以采用递归额方式不断申请连接
 * 直到成功获得链接的时候才跳出连接
 */
package com.bluetoothlamp.tiny.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;

public class Bluetooth
{
    private static final String TAG = "Bluetooth";
    /*单例模式*/
    private static Bluetooth instance;

    private Bluetooth()
    {
    }

    public static Bluetooth getInstance()
    {
        if (instance == null)
        {
            instance = new Bluetooth();
        }
        return instance;
    }

    /**
     * 得到蓝牙socket
     */
    public BluetoothSocket getBlueToothSocket(String deviceCode)
    {
        BluetoothSocket socket = null;
        try
        {
            BluetoothDevice device = initBlueToothDevice(deviceCode);
            if (device != null)
            {
                socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            }
        }
        catch (IOException e)
        {
            Log.i(TAG, "create bluetooth socket have a missing");
            e.printStackTrace();
        }

        return socket;
    }

    /**扫描本机中的蓝牙设备，并且得到已配对的蓝牙设备*/
    private BluetoothDevice initBlueToothDevice(String deviceCode)
    {
        BluetoothDevice bluetoothDevice = null;
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> devices = adapter.getBondedDevices();

        if (devices.size() > 0)
        {
            // Loop through paired devices
            for (BluetoothDevice device : devices)
            {
                Log.i("", "devices name : " + device.getName().toString());
                if (device.getName().toString().equals(deviceCode)) // Bluetooth Lamp : 38:59:F9:F7:DB:49
                {
                    bluetoothDevice = adapter.getRemoteDevice(device.getAddress());
                }
            }
        }

        return bluetoothDevice;
    }

    /**判断手机中的蓝牙是否打开，
     * @return 是否打开*/
    public boolean isBluetoothOpen()
    {
        boolean flag = false;

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter.isEnabled())
        {
            flag = true;
        }
        else
        {
            flag = false;
        }

        return flag;
    }

    public Set<BluetoothDevice> getBluetoothDevicesName()
    {
        BluetoothDevice bluetoothDevice = null;
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> devices = adapter.getBondedDevices();

        return devices;
    }
}