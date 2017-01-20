package com.pubmatic.sample;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Sagar on 12/20/2016.
 */
public class HomeActivity extends FragmentActivity  {

    PMPagerAdapter mPMPagerAdapter;
    ViewPager mViewPager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mViewPager = (ViewPager) findViewById(R.id.pager);

        final ActionBar actionBar = getActionBar();

        // Specify that tabs should be displayed in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create a tab listener that is called when the user changes tabs.
        ActionBar.TabListener tabListener = new ActionBar.TabListener() {
            @Override
            public void onTabSelected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {

            }

            @Override
            public void onTabReselected(ActionBar.Tab tab, android.app.FragmentTransaction fragmentTransaction) {

            }
        };

        actionBar.addTab(actionBar.newTab()
                .setIcon(getResources().getDrawable(R.drawable.home))
                .setTabListener(tabListener));

        actionBar.addTab(actionBar.newTab()
                .setIcon(getResources().getDrawable(R.drawable.settings))
                .setTabListener(tabListener));

        mPMPagerAdapter = new PMPagerAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mPMPagerAdapter);
        mViewPager.setOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        getActionBar().setSelectedNavigationItem(position);
                    }
                });
    }
}