package com.cy.widgetlibrary.base;

import java.util.ArrayList;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;


/**
 * Caiyuan Huang
 *<p>2015-2-10</p>
 *<p>Fragment列表的适配器</p>
 */
public class FragmentViewPagerAdapter extends FragmentStatePagerAdapter {

	private ArrayList<Fragment> mFragmentsList = null;

	public FragmentViewPagerAdapter(FragmentManager fm,
			ArrayList<Fragment> fragmentsList) {
		super(fm);
		// TODO Auto-generated constructor stub
		mFragmentsList = fragmentsList;
	}

	@Override
	public int getCount() {
		return mFragmentsList.size();
	}

	@Override
	public Fragment getItem(int position) {
		return mFragmentsList.get(position);
	}
//	/**
//	 * @return true if the value returned from {@link #instantiateItem(ViewGroup, int)} is the
//	 * same object as the {@link View} added to the {@link ViewPager}.
//	 */
//	@Override
//	public boolean isViewFromObject(View view, Object o) {
//		return o == view;
//	}
//
//	/**
//	 * Return the title of the item at {@code position}. This is important as what this method
//	 * returns is what is displayed in the {@link SlidingTabLayout}.
//	 * <p/>
//	 * Here we construct one using the position value, but for real application the title should
//	 * refer to the item's contents.
//	 */
	@Override
	public CharSequence getPageTitle(int position) {
		switch (position){
			case 0:
				return "阅之伤感";
			case 1:
				return "原创经典";
			case 2:
				return "阅之爱情";
			case 3:
				return "学生之作";
			case 4:
				return "心情随笔";
			case 5:
				return "励志美文";
		}
		return "";
	}

}
