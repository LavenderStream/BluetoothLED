package com.bluetoothlamp.tiny.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluetoothlamp.tiny.bluetooth.Bluetooth;
import com.bluetoothlamp.tiny.data.BluetoothDevices;
import com.bluetoothlamp.tiny.view.material.MaterialButton;

import java.io.IOException;
import java.io.OutputStream;


/**
 * @author Tiny
 *         电视的遥控器控制，可以配合笔记本 + qq 来实现控制
 */
public class TvActivity extends Fragment implements View.OnClickListener
{
    // 控制音乐的暂停和播放
    private static final int S_PLAYANDPASUE = 1;
    // 上一首歌
    private static final int S_PLUS = 2;
    // 下一首歌
    private static final int S_SUB = 3;
    // 音量增加
    private static final int S_UP = 4;
    // 音量减少
    private static final int S_DOWN = 5;
    // 音乐的播放按钮
    private MaterialButton mPlayAndStop;
    // 音量增加
    private MaterialButton mVolumePlus;
    // 音量减少
    private MaterialButton mVolumeMinus;
    // 上一首歌
    private MaterialButton mNextButton;
    // 下一首歌
    private MaterialButton mPreviousButton;

    // 蓝牙socket
    private BluetoothSocket mBluetoothSocket = null;
    // 蓝牙设备的名称
    private String mBluetoothDeviceName = "TINY";
    // 当actiivty不可见的时候就停止连接蓝牙
    private boolean isContinue = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_tv, null);
        initView(view);
        isContinue = true;

        return view;
    }

    /**
     * 初始化View组件
     * @param view
     */
    private void initView(View view)
    {
        this.mPlayAndStop = (MaterialButton) view.findViewById(R.id.bt_tv_head);
        this.mVolumePlus = (MaterialButton) view.findViewById(R.id.bt_tv_plus);
        this.mVolumeMinus = (MaterialButton) view.findViewById(R.id.bt_tv_sub);
        this.mNextButton = (MaterialButton) view.findViewById(R.id.bt_tv_up);
        this.mPreviousButton = (MaterialButton) view.findViewById(R.id.bt_tv_down);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.bt_tv_head:
                sendMessageToBluetoothLamp(S_PLAYANDPASUE);
                break;
            case R.id.bt_tv_plus:
                sendMessageToBluetoothLamp(S_PLUS);
                break;
            case R.id.bt_tv_sub:
                sendMessageToBluetoothLamp(S_SUB);
                break;
            case R.id.bt_tv_up:
                sendMessageToBluetoothLamp(S_UP);
                break;
            case R.id.bt_tv_down:
                sendMessageToBluetoothLamp(S_DOWN);
                break;

            default:
                break;
        }
    }


    /**
     * @auther Tiny
     * 将类中的属性mBluetoothSocket 赋值，获得socket时可调用这个方法
     */
    private void getBlueToothConnect()
    {
        final BluetoothSocket socket = Bluetooth.getInstance().getBlueToothSocket(mBluetoothDeviceName);
        BluetoothAdapter mmBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mmBluetoothAdapter.cancelDiscovery();
        if (Bluetooth.getInstance().isBluetoothOpen() && socket != null)
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        TvActivity.this.mBluetoothSocket = socket;
                        // 如果socket获取失败，会直接catch下面的异常，并按钮保持刚才的灰色状态
                        // 一旦获取了socket，代码正常进行，更改按钮颜色为激活状态
                        TvActivity.this.mBluetoothSocket.connect();
                        // 取得连接之后将按钮中的文字颜色变成主题蓝色
                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                if (mPreviousButton != null)
                                    mPreviousButton.setTextColor(Color.parseColor("#ff33b5e5"));
                                setButtonListening();
                                // 当socket获得完成，才可以取得连接
                            }
                        });

                    } catch (IOException e)
                    {
                        e.printStackTrace();
                        if (isContinue) getBlueToothConnect();
                    }
                }
            }).start();
        }
        else if (!Bluetooth.getInstance().isBluetoothOpen())
        {
            Toast.makeText(getActivity(), "please open bluetooth", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 通过蓝牙发送数据
     * @param state
     */
    private void sendMessageToBluetoothLamp(int state)
    {
        OutputStream os = null;
        try
        {
            if (this.mBluetoothSocket != null)
            {
                os = this.mBluetoothSocket.getOutputStream();
                os.write(state);
                os.flush();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume()
    {
        mBluetoothDeviceName = BluetoothDevices.findById(BluetoothDevices.class, 1).
                getBluetoothTVName();
        getBlueToothConnect();
        // 获得蓝牙的连接并根据蓝牙的状态改变按钮的颜色ps:前提是点钱要得到蓝牙串口通讯并成功返回socket
        super.onResume();
    }

    @Override
    public void onDestroy()
    {
        try
        {
            destroyConnect();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    @Override
    public void onPause()
    {
        try
        {
            destroyConnect();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        super.onPause();
    }

    /**
     * 当此fragment消失之后的收尾工作
     */
    private void destroyConnect() throws IOException
    {
        if (mBluetoothSocket != null)
            this.mBluetoothSocket.close();

        this.isContinue = false;
        // 当连接断开之后把按钮颜色变成主题灰色
        getActivity().runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mPreviousButton.setTextColor(Color.parseColor("#a3a3a3"));
                // 当socket获得完成，才可以取得连接
            }
        });
    }

    /**
     * 设置五个按钮的监听
     */
    private void setButtonListening()
    {
        this.mPlayAndStop.setOnClickListener(this);
        this.mVolumePlus.setOnClickListener(this);
        this.mVolumeMinus.setOnClickListener(this);
        this.mNextButton.setOnClickListener(this);
        this.mPreviousButton.setOnClickListener(this);
    }
}
