package com.bluetoothlamp.tiny.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bluetoothlamp.tiny.view.verticalview.VerticalAdapter;
import com.bluetoothlamp.tiny.view.verticalview.VerticalViewPager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.LimitLine;

import java.util.ArrayList;


/**
 * Created by Tiny on 12/21/2014.
 * 显示健康设备并且根据数据可以得出一些图标
 */
public class HeathActivity extends Fragment
{
    // 条状图-显示跑步情况
    private BarChart mBarChart;
    // 饼形图-显示每日饮水情况
    private PieChart mPieChart;
    // 折线图-显示心跳
    private LineChart mLineChart;
    // 竖直翻页的view 继承自viewGroup
    private VerticalViewPager viewPager;
    // 图标的列表view集合
    private ArrayList<View> viewList;
    // 喝水的数量
    public static int mDrinkWaterNumber = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.activity_health, null);
        viewPager = (VerticalViewPager) view.findViewById(R.id.health_viewpaper);

        init();

        VerticalAdapter vAdapter1 = new VerticalAdapter(viewList);

        viewPager.setAdapter(vAdapter1);
        viewPager.setOnPageChangeListener(new VerticalViewPager.OnPageChangeListener()
        {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
            {
            }

            @Override
            public void onPageSelected(int position)
            {
                switch (position)
                {
                    case 0:
                        mPieChart.animateXY(750, 750);
                        mPieChart.setDrawCenterText(true);
                        mPieChart.setCenterText(getResources().getString(R.string.piechart_center_text));
                        mPieChart.setDrawHoleEnabled(true);
                        break;
                    case 1:
                        mBarChart.animateXY(350, 350);
                        break;
                    case 2:
                        mLineChart.animateXY(350, 350);
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state)
            {
            }
        });

        return view;
    }


    /**
     * 初始化UI组件
     */
    private void init()
    {
        this.mPieChart = new PieChart(getActivity());
        this.mBarChart = new BarChart(getActivity());
        this.mLineChart = new LineChart(getActivity());
        setBarChartData(7, 20);
        setDataPiechart(mDrinkWaterNumber, 8);
        setLineChartData(15, 100);

        viewList = new ArrayList<View>();// 将要分页显示的View装入数组中
        viewList.add(mPieChart);
        viewList.add(mBarChart);
        viewList.add(mLineChart);
    }


    private void setBarChartData(int count, float range)
    {

        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++)
        {
            xVals.add("星期" + i);
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

        for (int i = 0; i < count; i++)
        {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult);
            yVals1.add(new BarEntry(val, i));
        }

        BarDataSet set1 = new BarDataSet(yVals1, "DataSet");
        set1.setBarSpacePercent(35f);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);

        mBarChart.setData(data);
    }

    private void setDataPiechart(int count, float range)
    {
        mPieChart.animateXY(900, 900);
        mPieChart.setDrawCenterText(true);
        mPieChart.setCenterText(getResources().getString(R.string.piechart_center_text));
        mPieChart.setDrawHoleEnabled(true);

        float mult = range;

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        yVals1.add(new Entry((float) mDrinkWaterNumber, 100));
        yVals1.add(new Entry((float) 8 - mDrinkWaterNumber, 100));

        ArrayList<String> xVals = new ArrayList<String>();

        xVals.add("完成");
        xVals.add("未完成");

        PieDataSet set1 = new PieDataSet(yVals1, "Election Results");
        set1.setColor(Color.BLUE);
        set1.setSliceSpace(3f);


        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        set1.setColors(colors);

        PieData data = new PieData(xVals, set1);
        mPieChart.setData(data);

        mPieChart.highlightValues(null);

        mPieChart.invalidate();
    }

    private void setLineChartData(int count, float range)
    {
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++)
        {
            xVals.add((i) + "");
        }

        ArrayList<Entry> yVals = new ArrayList<Entry>();

        for (int i = 0; i < count; i++)
        {
            float mult = (range + 1);
            float val = (float) (Math.random() * mult) + 3;// + (float)
            // ((mult *
            // 0.1) / 10);
            yVals.add(new Entry(val, i));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals, "DataSet 1");
        set1.setFillAlpha(110);
        set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        set1.enableDashedLine(10f, 5f, 0f);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setCircleColor(Color.WHITE);
        set1.setLineWidth(1f);
        set1.setCircleSize(4f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.BLACK);
        // set1.setShader(new LinearGradient(0, 0, 0, mChart.getHeight(),
        // Color.BLACK, Color.WHITE, Shader.TileMode.MIRROR));

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        LimitLine ll1 = new LimitLine(130f);
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setDrawValue(true);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT);

        LimitLine ll2 = new LimitLine(-30f);
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setDrawValue(true);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT);

        data.addLimitLine(ll1);
        data.addLimitLine(ll2);

        // set data
        mLineChart.setData(data);
    }


}
