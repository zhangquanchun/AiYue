package com.fju.zqc.fjuzqcgradutation.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.cy.widgetlibrary.base.BaseFragmentActivity;
import com.cy.widgetlibrary.base.BindView;
import com.cy.widgetlibrary.content.CustomTitleView;
import com.fju.zqc.fjuzqcgradutation.R;
import com.fju.zqc.fjuzqcgradutation.bean.ArticleList;
import com.fju.zqc.fjuzqcgradutation.bean.UserInfo;
import com.fju.zqc.fjuzqcgradutation.utils.DataStorageUtils;
import com.fju.zqc.fjuzqcgradutation.utils.SupportBarUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by ejianshen on 15/11/12.
 */
public class AtyAWeekData extends BaseFragmentActivity {
    private SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private String currentTime;
    private SupportBarUtils mBarChartUtils;
    private static long timeBetween,day,hour,min,s;
    @BindView
    private CustomTitleView vTitle;
    @BindView
    private LinearLayout llChartWeek;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected boolean isBindViewByAnnotation() {
        return true;
    }

    @Override
    protected int getContentViewId() {
        return R.layout.aty_m_week_data;
    }

    @Override
    protected void initView() {
        vTitle.setTitle("本周写作统计");
        vTitle.setTxtLeftText("       ");
        vTitle.setTxtLeftIcon(R.drawable.header_back);
        vTitle.setTxtLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        currentTime=format.format(new Date(System.currentTimeMillis()));
        mBarChartUtils = new SupportBarUtils(mActivity);
    }

    @Override
    protected void initData() {
        getData();
    }
    private void getData(){
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
        String mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
        final String mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
        final int mWay = c.get(Calendar.DAY_OF_WEEK);
        BmobQuery<ArticleList> query=new BmobQuery<>();
        UserInfo userInfo= BmobUser.getCurrentUser(mContext,UserInfo.class);
        query.addWhereEqualTo("user", userInfo);
        query.findObjects(mContext, new FindListener<ArticleList>() {
            @Override
            public void onSuccess(List<ArticleList> list) {
               int monDay=0,tDay=0,wDay=0,tuDay=0,fDay=0,staDay=0,sunDay=0;
                int[] weekDays=new int[]{monDay,tDay,wDay,tuDay,fDay,staDay,sunDay};
                for(int i=0;i<list.size();i++){
                    Integer weekDay=list.get(i).getWeekDay();
                    String createTime=list.get(i).getCreatedAt();
                    try {
                        Date timeCreate=format.parse(createTime);
                        Date timeCurrent=format.parse(currentTime);
                        long timeBetween=timeCurrent.getTime()-timeCreate.getTime();
                        SimpleDateFormat formatHour=new SimpleDateFormat("HH");
                        int createHour=Integer.parseInt(formatHour.format(timeCreate));
                        int currentHour=Integer.parseInt(formatHour.format(timeCurrent));
                        if (createHour-currentHour>0){
                            day=timeBetween/(24*60*60*1000)+1;
                        }else if(createHour-currentHour==0){
                            SimpleDateFormat formatMin=new SimpleDateFormat("mm");
                            int createMin=Integer.parseInt(formatMin.format(timeCreate));
                            int currentMin=Integer.parseInt(formatMin.format(timeCurrent));
                            if(createMin-currentMin>0){
                                day=timeBetween/(24*60*60*1000)+1;
                            }else{
                                day=timeBetween/(24*60*60*1000);
                            }
                        }else{
                            day=timeBetween/(24*60*60*1000);
                        }

                        // day=timeBetween/(24*60*60*1000);
                        hour = (timeBetween/(60*60*1000)-day*24);
                        min=((timeBetween/(60*1000))-day*24*60-hour*60);
                        s=(timeBetween/1000-day*24*60*60-hour*60*60-min*60);
                        if(mWay==1){
                            if(day>7){
                                return;
                            }
                            else {
                                for(int j=0;j<7;j++){
                                    if(weekDay==(j+1)){
                                        weekDays[j]=weekDays[j]+1;
                                    }
                                }
                            }
                        }else{
                            if ((int)day<mWay-1){
                                for(int j=0;j<7;j++){
                                    if(weekDay==(j+1)){
                                        weekDays[j]=weekDays[j]+1;
                                    }
                                }
                            }
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                        Log.d("tttt", "error" + e.getMessage());
                    }
                }
                double[] week=new double[]{
                  weekDays[0],weekDays[1],weekDays[2],weekDays[3],weekDays[4],weekDays[5],weekDays[6]
                };
                float target=0;
                for(int i=0;i<7;i++){
                    if(week[i]>target){
                        target=(float)week[i];
                    }
                }
                llChartWeek.addView(mBarChartUtils.initBarChartView(target, week,7,""));
            }

            @Override
            public void onError(int i, String s) {
                Log.d("tttt", "error" +s);
            }
        });
    }

}
