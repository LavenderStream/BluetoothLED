package com.bluetoothlamp.tiny.view.foldingdrawer;




import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bluetoothlamp.tiny.activity.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 *
 */
public class DrawerFragment extends Fragment {
    private static final String TAG = "DrawerFragment";
    private ListView mListView;
    private DrawerAdapter mAdapter;
    private ISliderItmeClickListener mListener;

    public DrawerFragment() {
        // Required empty public constructor
    }

    public void setListener(ISliderItmeClickListener l)
    {
        this.mListener = l;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View contentView = inflater.inflate(R.layout.fragment_drawer, null);
        mListView = (ListView) contentView.findViewById(R.id.listView);
        // 这是自定义的一个适配器
        mAdapter = new DrawerAdapter(mListView);
        mListView.setAdapter(mAdapter);
        mListView.setItemChecked(0, true);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                mListView.setItemChecked(position, true);
                mListener.onItmeClick(position);
            }
        });
        return contentView;
    }
}
