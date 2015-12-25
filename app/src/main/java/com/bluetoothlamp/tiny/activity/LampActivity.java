package com.bluetoothlamp.tiny.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluetoothlamp.tiny.bluetooth.Bluetooth;
import com.bluetoothlamp.tiny.view.CircleButton;
import java.io.IOException;
import java.io.OutputStream;
import java.text.DecimalFormat;

/**
 * 蓝牙台灯设备，智能台灯分为2段，没有连接的时候按钮是灰色，取得连接是蓝色，中等灯光是黄色，亮光是红色
 * 灰色：Color.GRAY
 * 蓝色：#ff33b5e5
 * 黄色：Color.YELLOW
 * 红色：Color.RED
 */
public class LampActivity extends Fragment
{
    // Lamp 的几种亮度模式
    // 亮
    private static final int LIGHT_MODE = 1;
    // 灭
    private static final int CLOSE_MODE = 0;
    // 高亮
    private static final int HEIGLIGHT_MODE = 2;

    // 蓝牙设备的名称
    private String mBluetoothDeviceName = "Bluetooth Lamp";
    // 蓝牙socket
    private BluetoothSocket mBluetoothSocket = null;
    // lamp fragment中的按钮
    private CircleButton mCircleButton;
    // 记录当前灯的状态
    private static int mStateNow = CLOSE_MODE;

    //获取温度传感器
    private SensorManager mSensorManager=null;
    private Sensor mSensor = null;
    private float mTemperatures = 0;
    // 控制lcd屏幕背光的标记
    private boolean mBackGroud = false;
    private boolean isCheach = true;

    /**
     * @auther Tiny
     * 创建Fragment时会调用这个方法
     */
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState)
    {
        mSensorManager=(SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_TEMPERATURE);

        mSensorManager.registerListener(mSensorEventListener, mSensor
                , SensorManager.SENSOR_DELAY_NORMAL);

        View view = inflater.inflate(R.layout.activity_lamp, null);
        this.mCircleButton = (CircleButton) view.findViewById(R.id.circlebtn);

        return view;
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
        if (Bluetooth.getInstance().isBluetoothOpen() &&
                socket != null)
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                        LampActivity.this.mBluetoothSocket = socket;
                        // 如果socket获取失败，会直接catch下面的异常，并按钮保持刚才的灰色状态
                        // 一旦获取了socket，代码正常进行，更改按钮颜色为激活状态
                        LampActivity.this.mBluetoothSocket.connect();

                        // 格式化温度传感器的数据，若连接成功后可以发送到arduino端
                        String strTemperatures = new DecimalFormat("###,###,###.##").format(LampActivity.this.mTemperatures);
                        sendMessageToBluetoothLamp("       " + strTemperatures + " c");

                        getActivity().runOnUiThread(new Runnable()
                        {
                            @Override
                            public void run()
                            {
                                if (mCircleButton != null)
                                {
                                    LampActivity.this.mCircleButton.setColor(Color.parseColor("#ff33b5e5"));
                                    // 当socket获得完成，才可以取得连接
                                    setCircleButtonListening();
                                }
                            }
                        });
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                        if (isCheach) getBlueToothConnect();
                    }
                }
            }).start();
        }
        else if (!Bluetooth.getInstance().isBluetoothOpen())
        {
            Toast.makeText(getActivity(), "please open bluetooth", Toast.LENGTH_SHORT).show();
            LampActivity.this.mCircleButton.setColor(Color.GRAY);
        }
    }

    /**
     * @param state
     * 通过Socket发送数据
     */
    private void sendMessageToBluetoothLamp(String state)
    {
        OutputStream os = null;
        try
        {
            if (this.mBluetoothSocket != null)
            {
                os = this.mBluetoothSocket.getOutputStream();
                String massage = state;
                os.write(massage.getBytes());
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume()
    {
        // 获得蓝牙的连接并根据蓝牙的状态改变按钮的颜色ps:前提是点钱要得到蓝牙串口通讯并成功返回socket
        getBlueToothConnect();

        super.onResume();
    }

    @Override
    public void onPause()
    {
        mSensorManager.unregisterListener(mSensorEventListener, mSensor);
        try
        {
            destroyConnect();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        super.onPause();
    }

    @Override
    public void onDestroy()
    {
        mSensorManager.unregisterListener(mSensorEventListener, mSensor);
        try
        {
            destroyConnect();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        super.onDestroy();
    }

    /**
     * 当此fragment消失之后的收尾工作
     */
    private void destroyConnect() throws IOException
    {
        if (mBluetoothSocket != null)
            this.mBluetoothSocket.close();
        this.mCircleButton.setColor(Color.GRAY);
        this.isCheach = false;
    }

    /**
     * 设置按钮监听的封装方法
     */
    private void setCircleButtonListening()
    {
        LampActivity.this.mCircleButton.setOnLongClickListener(new View.OnLongClickListener()
        {
            @Override
            public boolean onLongClick(View v)
            {
                // 当前状态是不亮的时候变亮
                if (mBackGroud == false)
                {
                    sendMessageToBluetoothLamp("4");
                    mBackGroud = true;
                }
                else
                {
                    sendMessageToBluetoothLamp("5");
                    mBackGroud = false;
                }

                return true;
            }
        });

        LampActivity.this.mCircleButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mStateNow++;
                if (mStateNow >= HEIGLIGHT_MODE + 1)
                {
                    mStateNow = CLOSE_MODE;
                }
                switch (mStateNow)
                {
                    case CLOSE_MODE:
                        mCircleButton.setColor(Color.parseColor("#ff33b5e5"));
                        break;
                    case LIGHT_MODE:
                        mCircleButton.setColor(Color.YELLOW);
                        break;
                    case HEIGLIGHT_MODE:
                        mCircleButton.setColor(Color.RED);
                        break;
                    default:
                        break;
                }
                sendMessageToBluetoothLamp("" + mStateNow);
            }
        });
    }

    /**
     * 增加android的传感器接口，用来传递手机感受到的温度信息
     */
    private final SensorEventListener mSensorEventListener = new SensorEventListener()
    {
        @Override
        public void onSensorChanged(SensorEvent event)
        {
            if (event.sensor.getType() == Sensor.TYPE_TEMPERATURE)
            {
                /*温度传感器返回当前的温度，单位是摄氏度（°C）。*/
                float temperature = event.values[0];
                LampActivity.this.mTemperatures = temperature;
            }
        }
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy)
        {
        }
    };


}