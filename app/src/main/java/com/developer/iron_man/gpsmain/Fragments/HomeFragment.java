package com.developer.iron_man.gpsmain.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.developer.iron_man.gpsmain.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sagar on 29/7/17.
 */

public class HomeFragment extends Fragment {

    private View view;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    TextView tabOne;
    TextView tabTwo;
    TextView tabThree;
    TextView tabfour;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.activity_main_layout,container,false);

        viewPager = (ViewPager)view.findViewById(R.id.view_pager);
        createViewPager(viewPager);

        tabLayout = (TabLayout)view.findViewById(R.id.tab_host);
        tabLayout.setupWithViewPager(viewPager);
        setUpText();

        return view;
    }

    public void setUpText() {

        //setting up the custom text for the tabs
        tabOne = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabOne.setText("Map");
        tabOne.setTextColor(Color.BLACK);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        tabTwo = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabTwo.setText("Scan");
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        tabThree = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabThree.setText("History");
        tabLayout.getTabAt(2).setCustomView(tabThree);

        tabfour = (TextView) LayoutInflater.from(getActivity()).inflate(R.layout.custom_tab, null);
        tabfour.setText("Profile");
        tabLayout.getTabAt(3).setCustomView(tabfour);
    }

    private void createViewPager(ViewPager viewPager) {

        //filling the adapter with fragments
       ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(new MapFragment(), "Map");
        adapter.addFrag(new QRScanFragment(), "Two");
        adapter.addFrag(new HistoryFragment(), "Three");
        adapter.addFrag(new ProfileFragment(), "Four");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
