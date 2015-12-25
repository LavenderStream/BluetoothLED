package com.bluetoothlamp.tiny.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bluetoothlamp.tiny.data.CloudUpdate;
import com.bluetoothlamp.tiny.tool.OptionDate2Cloud;
import com.bluetoothlamp.tiny.view.material.Switch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;


/**
 * @author tiny
 * 程序的设置用途，目前只可以完成云同步功能的开启
 */
public class SettingActivity extends Fragment
{
    private ListView mListView;
    private ListAdapter mAdapter;
    // 开启云同步的开关
    private Switch mSwitch;
    private TextView mTexView;
    private TextView mSettingTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view;
        view = inflater.inflate(R.layout.activity_setting, null);
        final View switchView = view.findViewById(R.id.setting_sheck_view);

        this.mSettingTitle = (TextView) view.findViewById(R.id.setting_textview_title);
        this.mSettingTitle.setOnClickListener(new TitleClick());

        this.mSwitch = (Switch) switchView.findViewById(R.id.setting_switch);
        this.mSwitch.setOncheckListener(new Switch.OnCheckListener()
        {
            @Override
            public void onCheck(boolean check)
            {
                if (check)
                {
                    OptionDate2Cloud.getInstance().saveDate2Cloud(getActivity());

                    new Thread(new Runnable()
                    {
                        @Override
                        public void run()
                        {
                            BmobQuery<CloudUpdate> b = new BmobQuery<CloudUpdate>();
                            b.getObject(getActivity(), "f0c4584e3a", new GetListener<CloudUpdate>()
                            {
                                @Override
                                public void onSuccess(CloudUpdate cloudUpdate)
                                {
                                    HeathActivity.mDrinkWaterNumber = cloudUpdate.getDrinkWaterDone();
                                    mSwitch.setChecked(false);
                                    Log.i("", "aa" + HeathActivity.mDrinkWaterNumber);
                                }

                                @Override
                                public void onFailure(int i, String s)
                                {
                                }
                            });
                        }
                    }).start();
                }
            }
        });

        // 更改字体样式
        this.mTexView = (TextView) switchView.findViewById(R.id.setting_textview);
        Typeface typeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/opensans_bold.ttf");
        mTexView.setTypeface(typeFace);

/*        mAdapter = new SimpleAdapter(getActivity(), getDate(),R.layout.activity_setting_item,
                new String[]{"title"},
                new int[]{R.id.setting_textview});

        mListView = (ListView) view.findViewById(R.id.setting_activity_list);
        mListView.setAdapter(mAdapter);*/

        return view;
    }

    public List<? extends Map<String,?>> getDate()
    {
      List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map = new HashMap<String, Object>();
        map.put("title", "G3");
        list.add(map);

        return list;
    }

    /**
     * 点击title是出发， 作为程序的‘彩蛋’加入
     */
    private class TitleClick implements View.OnClickListener
    {
        @Override
        public void onClick(View v)
        {
            Animation operatingAnim = AnimationUtils.loadAnimation(SettingActivity.this.getActivity(), R.anim.rock);
            LinearInterpolator lin = new LinearInterpolator();
            operatingAnim.setInterpolator(lin);
            if (operatingAnim != null) {
                mSettingTitle.startAnimation(operatingAnim);
            }
        }
    }
}
