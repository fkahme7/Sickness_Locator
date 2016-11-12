package com.a500.sweng.sickness_locator.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.a500.sweng.sickness_locator.fragment.BarGraphFragment;
import com.a500.sweng.sickness_locator.fragment.PieChartFragment;
import com.a500.sweng.sickness_locator.fragment.PredictiveFragment;
import com.a500.sweng.sickness_locator.fragment.TimeLineFragment;

public class TabsPagerAdapter extends FragmentPagerAdapter {

	public TabsPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {

		switch (index) {
		case 0:
			// Top Rated fragment activity
			return new BarGraphFragment();
		case 1:
			// Games fragment activity
			return new TimeLineFragment();
		case 2:
			// Movies fragment activity
			return new PieChartFragment();

		case 3:
			// Movies fragment activity
			return new PredictiveFragment();
		}

		return null;
	}

	@Override
	public int getCount() {
		// get item count - equal to number of tabs
		return 4;
	}

}
