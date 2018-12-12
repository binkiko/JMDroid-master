package com.aidingyun.ynlive.mvp.ui.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;

import com.aidingyun.core.router.Router;
import com.aidingyun.ynlive.BuildConfig;
import com.aidingyun.ynlive.R;
import com.aidingyun.ynlive.app.utils.GlobalUtils;
import com.aidingyun.ynlive.app.utils.ToastUtils;
import com.aidingyun.ynlive.component.log.LogUtils;
import com.aidingyun.ynlive.di.component.DaggerMainComponent;
import com.aidingyun.ynlive.di.module.MainModule;
import com.aidingyun.ynlive.mvp.contract.MainContract;
import com.aidingyun.ynlive.mvp.model.annotation.MainPageId;
import com.aidingyun.ynlive.mvp.presenter.MainPresenter;
import com.aidingyun.ynlive.mvp.ui.fragment.ExpertFragment;
import com.aidingyun.ynlive.mvp.ui.fragment.HomePageFragment;
import com.aidingyun.ynlive.mvp.ui.fragment.SortFragment;
import com.aidingyun.ynlive.mvp.ui.fragment.StandpointFragment;
import com.aidingyun.ynlive.mvp.ui.fragment.UserPageFragment;
import com.aidingyun.ynlive.mvp.ui.widget.ExViewPager;
import com.aidingyun.ynlive.mvp.ui.widget.NavigateItem;
import com.aidingyun.ynlive.mvp.ui.widget.ViewPagerNavigation;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class MainActivity extends BaseActivity<MainPresenter> implements MainContract.View , ViewPagerNavigation.OnNavItemListener{
    private static final String TAG = "wjr";
    protected ViewPagerNavigation mViewPagerNavigation;
    protected ExViewPager mViewPager;

    private HomePageFragment mHomePageFeed;//首页
    private SortFragment mSort;//分类
    private ExpertFragment mExpert;//专家
    private StandpointFragment mStandpoint;//观点
    private UserPageFragment mUserPage;//我的
    private NavigateItem homeItem;
    private NavigateItem sortItem;
    private NavigateItem expertItem;
    private NavigateItem standpointItem;
    private NavigateItem userPageItem;

    public static void start(Context context) {
        Router.newIntent(context)
                .to(MainActivity.class)
                .launch();
    }
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerMainComponent
                .builder()
                .appComponent(appComponent)
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        GlobalUtils.statusBarTint(this);
        mViewPager = findViewById(R.id.view_pager);
        mViewPager.setNoScroll(true);
        mViewPagerNavigation = findViewById(R.id.nav_area);
        mViewPagerNavigation.init(mViewPager, getSupportFragmentManager());
        mViewPagerNavigation.setOnNavItemListener(this);
        initFragmentPages();
        verifyStoragePermissions(this);
    }
    protected void initFragmentPages() {
        mHomePageFeed = HomePageFragment.newInstance();
        mSort = SortFragment.newInstance();
        mExpert = ExpertFragment.newInstance();
        mStandpoint = StandpointFragment.newInstance();
        mUserPage = UserPageFragment.newInstance();

        homeItem = new NavigateItem(MainPageId.HomePage, "首页", getResources().getDrawable(R.drawable.tabbar_icon_home_default), getResources().getDrawable(R.drawable.tabbar_icon_home_selected), this);
        sortItem = new NavigateItem(MainPageId.Sort, "分类", getResources().getDrawable(R.drawable.tabbar_icon_column_default), getResources().getDrawable(R.drawable.tabbar_icon_column_selected), this);
        expertItem = new NavigateItem(MainPageId.Expert, "专家", getResources().getDrawable(R.drawable.tabbar_icon_teacher_default), getResources().getDrawable(R.drawable.tabbar_icon_teacher_selected), this);
        standpointItem = new NavigateItem(MainPageId.Standpoint, "观点", getResources().getDrawable(R.drawable.tabbar_icon_default_selected), getResources().getDrawable(R.drawable.tabbar_icon_viewpoint_selected), this);
        userPageItem = new NavigateItem(MainPageId.User, "我的", getResources().getDrawable(R.drawable.tabbar_icon_center_default), getResources().getDrawable(R.drawable.tabbar_icon_center_selected), this);

        addPage(homeItem, mHomePageFeed);
        addPage(sortItem, mSort);
        addPage(expertItem, mExpert);
        addPage(standpointItem, mStandpoint);
        addPage(userPageItem, mUserPage);

        mViewPagerNavigation.getViewPager().setOffscreenPageLimit(mViewPagerNavigation.getViewPager().getAdapter().getCount());
        mViewPagerNavigation.notifyDataChanged();
        mViewPagerNavigation.setCurrPageIndex(0);
    }
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    public static void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
    protected void addPage(NavigateItem item, Fragment fragment) {
        mViewPagerNavigation.addItem(item, fragment);
    }
//切换图标时操作
    @Override
    public void onItemIndexChanged(int index) {
        LogUtils.d(TAG, "onItemIndexChanged " + index);
    }

    int lastIndex = MainPageId.HomePage;
//点击图标时操作----看业务需不需要刷新
    @Override
    public void onItemClicked(int index) {
        LogUtils.e(TAG, "onItemClicked " + index);
        if (lastIndex == index) {
            switch (index) {
                case MainPageId.HomePage:
                    mHomePageFeed.doUIRefresh();
                    break;
                case MainPageId.Sort:
                    mSort.doUIRefresh();
                    break;
                case MainPageId.Expert:
                    mExpert.doUIRefresh();
                    break;
                case MainPageId.Standpoint:
                    mStandpoint.doUIRefresh();
                    break;
                case MainPageId.User:
                    mUserPage.doUIRefresh();
                    break;
                default:
                    break;
            }
        }
        lastIndex = index;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!BuildConfig.DEBUG) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }
    }


    private long exitTime = 0;

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            ToastUtils.show(this,"再按一次退出程序...");
            exitTime = System.currentTimeMillis();
        } else {
            System.exit(0);
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_HOME) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
        }else if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMessage(@NonNull String message) {
        checkNotNull(message);
        ArmsUtils.snackbarText(message);
    }

    @Override
    public void launchActivity(@NonNull Intent intent) {
        checkNotNull(intent);
        ArmsUtils.startActivity(intent);
    }

    @Override
    public void killMyself() {
        finish();
    }
}
