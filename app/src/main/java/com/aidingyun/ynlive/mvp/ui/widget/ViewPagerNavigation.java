package com.aidingyun.ynlive.mvp.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.aidingyun.ynlive.component.log.LogUtils;
import com.aidingyun.ynlive.mvp.model.annotation.MainPageId;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerNavigation extends LinearLayout {

    private ViewPager mViewPager;
    private ViewPagerAdapter mViewPagerAdapter;

    private List<NavigateItem> mNavItemList = new ArrayList<NavigateItem>();
    private NavigateItem mCurrNavigateItem;

    private TabClickListener mClickListener = new TabClickListener();

    private OnNavItemListener mOnNavItemListener;

    public ViewPagerNavigation(Context context) {
        super(context);
    }

    public ViewPagerNavigation(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewPagerNavigation(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(ViewPager viewPager, FragmentManager fm) {
        mViewPager = viewPager;
        mViewPagerAdapter = new ViewPagerAdapter(fm);
        mViewPager.setAdapter(mViewPagerAdapter);
    }

    public void notifyDataChanged() {
        if (mViewPagerAdapter != null) {
            mViewPagerAdapter.notifyDataSetChanged();
        }
    }

    public void setCurrPageIndex(int index) {
        NavigateItem newItem = null;
        for (int i = 0; i < mNavItemList.size(); i++) {
            if (mNavItemList.get(i).mFragmentId == index) {
                newItem = mNavItemList.get(i);
                break;
            }
        }

        if (newItem != mCurrNavigateItem && mOnNavItemListener != null && mCurrNavigateItem != null) {
            mOnNavItemListener.onItemIndexChanged(index);
        }

        if (mCurrNavigateItem != null) {
            mCurrNavigateItem.setFocus(false, getContext());
        }
        newItem.setFocus(true, getContext());
        mCurrNavigateItem = newItem;
    }

    public void addItem(NavigateItem item, Fragment fragment) {
        if (fragment != null && mViewPagerAdapter != null) {
            mViewPagerAdapter.addFragment(fragment);
        }

        mNavItemList.add(item);
        View child = item.getItemView();
        child.setTag(item.mFragmentId);
        child.setOnClickListener(mClickListener);
        this.addView(item.getItemView());
    }

    public void setOnNavItemListener(OnNavItemListener listener) {
        mOnNavItemListener = listener;
    }

    public ViewPager getViewPager() {
        return mViewPager;
    }

    public interface OnNavItemListener {
        /**
         * 只有改变才会触发
         */
        void onItemIndexChanged(int index);

        /**
         * 点击必定触发
         */
        void onItemClicked(int index);
    }

    private class TabClickListener implements OnClickListener {

        @Override
        public void onClick(View view) {
            int id = (Integer) view.getTag();
            if (id != MainPageId.INDEX_ACTIVITY) {
                int oldIndex = mViewPager.getCurrentItem();
                if (oldIndex != id) {
                    try {
                        Fragment oldFragment = mViewPagerAdapter.getItem(oldIndex);
                        Fragment newFragment = mViewPagerAdapter.getItem(id);
                        oldFragment.onPause();
                        newFragment.onResume();
                    } catch (Exception e) {
                        LogUtils.e("ViewPager", "", e);
                    }
                }
                if (mViewPager != null) {
                    mViewPager.setCurrentItem(id);
                }
                setCurrPageIndex(id);//1.触发index changed 事件 2.点击切换与否的图片
            }
            if (mOnNavItemListener != null) {
                mOnNavItemListener.onItemClicked(id);//1.触发 item clicked 事件
            }
        }
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> mPages = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment) {
            mPages.add(fragment);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int arg0) {
            return (mPages == null || mPages.size() == 0) ? null : mPages.get(arg0);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public int getCount() {
            return mPages == null ? 0 : mPages.size();
        }
    }

}
