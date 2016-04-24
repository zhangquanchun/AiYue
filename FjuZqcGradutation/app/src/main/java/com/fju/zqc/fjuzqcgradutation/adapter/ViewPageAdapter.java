package com.fju.zqc.fjuzqcgradutation.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fju.zqc.fjuzqcgradutation.R;

import java.util.ArrayList;

/**
 * Created by ejianshen on 15/10/9.
 */
public class ViewPageAdapter extends PagerAdapter {
    SparseArray<View> views = new SparseArray<View>();
    private Context context;
    private ArrayList<Fragment> fragments=new ArrayList<>();
    public ViewPageAdapter(Context context,ArrayList<Fragment> fragments){
        this.context=context;
        this.fragments=fragments;
    }

    /**
     * @return the number of pages to display
     */
    @Override
    public int getCount() {
        return fragments.size();
    }

    /**
     * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
     * same object as the {@link View} added to the {@link ViewPager}.
     */
    @Override
    public boolean isViewFromObject(View view, Object o) {
        return o == view;
    }

    /**
     * Return the title of the item at {@code position}. This is important as what this method
     * returns is what is displayed in the {@link SlidingTabLayout}.
     * <p/>
     * Here we construct one using the position value, but for real application the title should
     * refer to the item's contents.
     */
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + (position + 1);
    }

    /**
     * Instantiate the {@link View} which should be displayed at {@code position}. Here we
     * inflate a layout from the apps resources and then change the text view to signify the position.
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // Inflate a new layout from our resources
        LayoutInflater mInflater=LayoutInflater.from(context);
        View view = mInflater.inflate(R.layout.fragment_web_article,
                container, false);
//        TextView txt = (TextView) view.findViewById(R.id.tvText);
//        txt.setText("Content: " + (position + 1));
        // Add the newly created View to the ViewPager
        container.addView(view);

        views.put(position, view);

        // Return the View
        return view;
    }

    /**
     * Destroy the item from the {@link ViewPager}. In our case this is simply removing the
     * {@link View}.
     */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
        views.remove(position);
    }

    @Override
    public void notifyDataSetChanged() {
        int position = 0;
        for (int i = 0; i < views.size(); i++) {
            position = views.keyAt(i);
            View view = views.get(position);
            // Change the content of this view
            TextView txt = (TextView) view.findViewById(R.id.item_subtitle);
            txt.setText("This Page " + (position + 1) + " has been refreshed");
        }
        super.notifyDataSetChanged();
    }

}

