package com.bluetoothlamp.tiny.view.foldingdrawer;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.bluetoothlamp.tiny.activity.R;
import com.bluetoothlamp.tiny.utils.ApplicationUtils;


/**
 * Created by storm on 14-3-25.
 */
public class DrawerAdapter extends BaseAdapter {
    private ListView mListView;

    public DrawerAdapter(ListView listView) {
        mListView = listView;
    }

    @Override
    public int getCount() {
        return ApplicationUtils.SlideListItme.length;
    }

    @Override
    public String getItem(int position) {
        return ApplicationUtils.SlideListItme[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(ApplicationUtils.getContext()).inflate(R.layout.listitem_drawer, null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.textView);
        textView.setText(getItem(position) + "");
        textView.setSelected(mListView.isItemChecked(position));
        return convertView;
    }
}
