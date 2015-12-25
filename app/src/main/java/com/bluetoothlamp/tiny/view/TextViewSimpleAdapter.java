package com.bluetoothlamp.tiny.view;

import android.content.Context;
import android.widget.SimpleAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by Tiny on 12/22/2014.
 */
public class TextViewSimpleAdapter extends SimpleAdapter
{
    public TextViewSimpleAdapter(Context context, List<? extends Map<String, ?>> data, int resource, String[] from, int[] to)
    {
        super(context, data, resource, from, to);
    }

    /**
     * 自定义textview的字体等属性
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
   /* @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View row = super.getView(position, convertView, parent);
        TextView titleText = (TextView) row.findViewById(R.id.about_title);
        TextView subTitleText = (TextView) row.findViewById(R.id.about_sub_title);
        Typeface typeFace = Typeface.createFromAsset(App.getContext().getAssets(), "fonts/opensans_bold.ttf");
        titleText.setTypeface(typeFace);
        subTitleText.setTypeface(typeFace);
        return row;
    }*/
}
