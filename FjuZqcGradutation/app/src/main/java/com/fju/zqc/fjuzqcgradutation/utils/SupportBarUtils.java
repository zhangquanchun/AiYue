package com.fju.zqc.fjuzqcgradutation.utils;

import android.content.Context;
import android.graphics.Color;
import android.view.View;


import com.fju.zqc.fjuzqcgradutation.R;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.SupportBarChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.achartengine.renderer.support.SupportColorLevel;
import org.achartengine.renderer.support.SupportSelectedChartType;
import org.achartengine.renderer.support.SupportSeriesRender;
import org.achartengine.renderer.support.SupportXAlign;
import org.achartengine.renderer.support.SupportYAlign;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zqc on 08/11/15.
 */
public class SupportBarUtils extends BaseSupportUtils {
    private final static int COLOR_UP_TARGET = ColorTheme.getColor(R.color.blue);
    private final static int COLOR_LOW_TARGET = ColorTheme.getColor(R.color.red);
    private final static int FIRSTDAY=-1;

    private final static int COLOR_OTHER = Color.parseColor("#8FD85A");

    public SupportBarUtils(Context context) {
        super(context);
    }

    public View initBarChartView(float target,double[] data,int size,String YTitle) {
        mXYMultipleSeriesDataSet = new XYMultipleSeriesDataset();

        final SupportSeriesRender barSeriesRender = new SupportSeriesRender();
        //设置柱状图的背景阴影是否可见
        barSeriesRender.setShowBarChartShadow(false);
//      barSeriesRender.setShowBarChartShadow(Color.DKGRAY);

        barSeriesRender.setSelectedChartType(SupportSelectedChartType.BOTH);

        if(target<4){
            mXYRenderer.setTargetValue(target);
            mXYRenderer.setYAxisMax(target+target);
        }
        else{
            mXYRenderer.setTargetValue(target);
            mXYRenderer.setYAxisMax(target + target / 4);
        }
        mXYRenderer.setYTitle(YTitle);
        mXYRenderer.setShowLabels(true);
        mXYRenderer.setYLabels(0);

        //设置是否使用颜色分级功能
        barSeriesRender.setColorLevelValid(false);
        ArrayList<SupportColorLevel> list = new ArrayList<SupportColorLevel>();
        //如果仅仅以target作为颜色分级，可以使用这个用法
        SupportColorLevel supportColorLevel_a = new SupportColorLevel(0, mXYRenderer.getTargetValue(), COLOR_LOW_TARGET);
        SupportColorLevel supportColorLevel_b = new SupportColorLevel(mXYRenderer.getTargetValue(), mXYRenderer.getTargetValue() * 10, COLOR_UP_TARGET);

        // 若有多个颜色等级可以使用这个用法
//        SupportColorLevel supportColorLevel_a = new SupportColorLevel(0,10,COLOR_LOW_TARGET);
//        SupportColorLevel supportColorLevel_b = new SupportColorLevel(10,15,COLOR_UP_TARGET);
//        SupportColorLevel supportColorLevel_c = new SupportColorLevel(15,20,COLOR_OTHER);
        mXYRenderer.addXTextLabel(FIRSTDAY + 1, "星期一");
        mXYRenderer.addXTextLabel(FIRSTDAY + 2, "星期二");
        mXYRenderer.addXTextLabel(FIRSTDAY + 3, "星期三");
        mXYRenderer.addXTextLabel(FIRSTDAY + 4, "星期四");
        mXYRenderer.addXTextLabel(FIRSTDAY + 5, "星期五");
        mXYRenderer.addXTextLabel(FIRSTDAY + 6, "星期六");
        mXYRenderer.addXTextLabel(FIRSTDAY+7, "星期日");

        list.add(supportColorLevel_a);
        list.add(supportColorLevel_b);
        barSeriesRender.setColorLevelList(list);

        XYSeries sysSeries = new XYSeries("");
        for (int i = 0; i < data.length; i++) {
            sysSeries.add(i, data[i]);
        }
        mXYRenderer.addSupportRenderer(barSeriesRender);

        mXYMultipleSeriesDataSet.addSeries(sysSeries);
        View chartView = ChartFactory.getSupportBarChartView(mContext, mXYMultipleSeriesDataSet,
                mXYRenderer, SupportBarChart.Type.STACKED);
        chartView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GraphicalView graphicalView = (GraphicalView) v;
                graphicalView.handPointClickEvent(barSeriesRender,"good");
            }
        });
        return chartView;
    }

    @Override
    protected void setRespectiveRender(XYMultipleSeriesRenderer render) {
        mXYRenderer.setBarWidth(60);
        mXYRenderer.setBarSpacing(15);
        mXYRenderer.setXAxisMin(-1);
        mXYRenderer.setXAxisMax(7);
        mXYRenderer.setShowGrid(false);
        //设置XY轴Title的位置，默认是Center
        mXYRenderer.setSupportXAlign(SupportXAlign.LEFT);
        mXYRenderer.setSupportYAlign(SupportYAlign.TOP);
    }

    @Override
    protected XYSeriesRenderer getSimpleSeriesRender(int color) {
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setColor(ColorTheme.getColor(R.color.blue));
        renderer.setDisplayChartValues(true);  // 设置是否在点上显示数据
        renderer.setPointStrokeWidth(4f);
        renderer.setChartValuesTextSize(44f);

//        renderer.setGradientStart(0,Color.GRAY);  //可以设置柱状图颜色的渐变
//        renderer.setGradientStop(10,Color.GREEN);
//        renderer.setGradientEnabled(false);

        return renderer;
    }
}
