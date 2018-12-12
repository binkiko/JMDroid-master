package com.aidingyun.ynlive.mvp.ui.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.jess.arms.base.BaseFragment;

import java.util.List;

public class MainPageAdapter extends FragmentStatePagerAdapter {

    private List<BaseFragment> fragments;
    private List<String> fragmentTitle;

    public MainPageAdapter(FragmentManager fm, List<BaseFragment> fragments, List<String> fragmentTitle) {
        super(fm);
        this.fragments = fragments;
        this.fragmentTitle = fragmentTitle;
    }

    public MainPageAdapter(FragmentManager fm, List<BaseFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitle.get(position);
    }
}
