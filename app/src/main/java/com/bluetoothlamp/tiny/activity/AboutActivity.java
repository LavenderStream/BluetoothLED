package com.bluetoothlamp.tiny.activity;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.bluetoothlamp.tiny.bluetooth.Bluetooth;
import com.bluetoothlamp.tiny.data.BluetoothDevices;
import com.bluetoothlamp.tiny.data.CloudUpdate;
import com.bluetoothlamp.tiny.view.PullToZoomListView;
import com.bluetoothlamp.tiny.view.TextViewSimpleAdapter;
import com.bluetoothlamp.tiny.view.material.MaterialButton;
import com.bluetoothlamp.tiny.view.wheelview.WheelView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.bluetoothlamp.tiny.activity.R.id.about_sub_title;

/**
 * Created by Tiny on 12/21/2014.
 * 显示用户信息界面，可以更改用户的姓名，头像，蓝牙智能设备的名称
 */
public class AboutActivity extends Fragment
{
    // 每行listview 的主标题
    private TextView mTextTtile;
    // 每行listview 的副标题
    private TextView mTextSubTitle;
    // 下拉可放大界面最上面的图片
    private PullToZoomListView listView;
    // 决定什么栏目发生变化的变量
    private int mDecideShowListline;
    // 设置界面底下的两个button
    private MaterialButton mMaterialButtonLeft, mMaterialButtonRight;
    // activity_about_setting.xml
    private View mRelativeLayout;
    // 判断底部的view是否显示
    private boolean isShowing = false;

    private BluetoothDevices mBluetoothDevices = new BluetoothDevices();
    //设置车轮组件的数据
    private List<String> PLANETS = new ArrayList<String>();
    // 用于更新数据库表的数据
    private CloudUpdate mCloudUpdate = new CloudUpdate();

    private static int RESULT_LOAD_IMAGE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        Set<BluetoothDevice> set = Bluetooth.getInstance().getBluetoothDevicesName();
        if (set.size() > 0)
        {
            for (BluetoothDevice device : set)
            {
                PLANETS.add(device.getName().toString());
            }
        }


        // 初始化数据
        mBluetoothDevices.setBluetoothLampName(BluetoothDevices.findById(BluetoothDevices.class, 1).
                getBluetoothLampName());
        mCloudUpdate.setBluetoothLampName(mBluetoothDevices.getBluetoothLampName());
        mBluetoothDevices.setBluetoothTVName(BluetoothDevices.findById(BluetoothDevices.class, 1).
                getBluetoothTVName());
        mCloudUpdate.setMediaDeviceName(mBluetoothDevices.getBluetoothTVName());
        mBluetoothDevices.setUserName(BluetoothDevices.findById(BluetoothDevices.class, 1).
                getUserName());
        mCloudUpdate.setUsername(BluetoothDevices.findById(BluetoothDevices.class, 1).
                getUserName());

        //引入组件
        View view = inflater.inflate(R.layout.activity_about, null);
        View textView = inflater.inflate(R.layout.activity_about_itme, null);
        mTextTtile = (TextView) textView.findViewById(R.id.about_title);
        mTextSubTitle = (TextView) textView.findViewById(about_sub_title);

        this.listView = (PullToZoomListView) view.findViewById(R.id.listView);

        // 配置数据和对于栏目的监听
        final SimpleAdapter adapter = new TextViewSimpleAdapter(getActivity(), getData(), R.layout.activity_about_itme,
                new String[]{"title", "info"},
                new int[]{R.id.about_title, about_sub_title});

        listView.setAdapter(adapter);
        listView.getHeaderView().setImageResource(R.drawable.splash01); //about 之中上面的那张图片
        listView.getHeaderView().setScaleType(ImageView.ScaleType.CENTER_CROP);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                // 特效listView上面的图片
                if (position == 0)
                {
                    Intent i = new Intent(
                            Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                }
                if (position == 2 || position == 3)
                {
                    if (!isShowing)
                    {
                        Animation mShowAction = new TranslateAnimation(0.0f, 0.0f, 220.0f, 0.0f);
                        mShowAction.setInterpolator(new AccelerateInterpolator());
                        mShowAction.setDuration(200);
                        mRelativeLayout.setAnimation(mShowAction);
                        mRelativeLayout.startAnimation(mShowAction);
                        mRelativeLayout.setVisibility(View.VISIBLE);
                        isShowing = true;
                    }
                    mDecideShowListline = position;
                }
            }
        });

        // 更改字体样式
        Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/opensans_bold.ttf");
        mTextTtile.setTypeface(typeFace);
        mTextTtile.setTextColor(Color.RED);
        mTextSubTitle.setTypeface(typeFace);

        // 增加底边选择器view组件
        mRelativeLayout = (View) view.findViewById(R.id.activity_about_setting_menu);
        mRelativeLayout.setVisibility(View.GONE);
        WheelView wheelView = (WheelView) mRelativeLayout.findViewById(R.id.about_wheelview);
        this.mMaterialButtonLeft = (MaterialButton) mRelativeLayout.findViewById(R.id.about_materiabutton_left);
        this.mMaterialButtonRight = (MaterialButton) (mRelativeLayout).findViewById(R.id.about_materiabutton_right);

        wheelView.setOffset(1);
        wheelView.setItems(PLANETS);
        wheelView.setOnWheelViewListener(new WheelView.OnWheelViewListener()
        {
            @Override
            public void onSelected(int selectedIndex, String item)
            {
                Log.d("", "index: " + item + selectedIndex);
                switch (mDecideShowListline)
                {
                    case 2:
                        mBluetoothDevices.setBluetoothLampName(item);
                        mCloudUpdate.setBluetoothLampName(item);
                        break;
                    case 3:
                        mBluetoothDevices.setBluetoothTVName(item);
                        mCloudUpdate.setMediaDeviceName(item);
                        break;
                    default:
                        break;
                }

                SimpleAdapter adapter = new TextViewSimpleAdapter(getActivity(), getData(), R.layout.activity_about_itme,
                        new String[]{"title", "info"},
                        new int[]{R.id.about_title, about_sub_title});
                listView.setAdapter(adapter);
                adapter.notifyDataSetChanged();

            }
        });

        mMaterialButtonLeft.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mRelativeLayout.setVisibility(View.GONE);
                isShowing = false;
                saveDate2DateBase();
            }
        });
        mMaterialButtonRight.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mRelativeLayout.setVisibility(View.GONE);
                isShowing = false;
            }
        });

        return view;
    }


    private List<Map<String, Object>> getData()
    {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "name");
        map.put("info", mBluetoothDevices.getUserName());
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", getResources().getString(R.string.Lamp));
        map.put("info", mBluetoothDevices.getBluetoothLampName());
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", getResources().getString(R.string.TV));
        map.put("info", mBluetoothDevices.getBluetoothTVName());
        list.add(map);

        return list;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_LOAD_IMAGE && null != data)
        {
            Uri selectedImage = data.getData();
            Log.i("URL", "" + selectedImage);

            mCloudUpdate.setPhotoPathUil(selectedImage.toString());

            listView.getHeaderView().setImageURI(selectedImage); //about 之中上面的那张图片
        }
    }

    private void saveDate2DateBase()
    {
        BluetoothDevices bluetoothDevices = BluetoothDevices.findById(BluetoothDevices.class, 1);
        bluetoothDevices.setBluetoothLampName(mCloudUpdate.getBluetoothLampName());
        bluetoothDevices.setBluetoothTVName(mCloudUpdate.getMediaDeviceName());
        bluetoothDevices.save();
    }
}
