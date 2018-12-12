package com.aidingyun.ynlive.mvp.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.aidingyun.core.router.Router;
import com.aidingyun.ynlive.R;
import com.aidingyun.ynlive.app.utils.GlobalUtils;
import com.aidingyun.ynlive.di.component.DaggerHobbyComponent;
import com.aidingyun.ynlive.di.module.HobbyModule;
import com.aidingyun.ynlive.mvp.contract.HobbyContract;
import com.aidingyun.ynlive.mvp.presenter.HobbyPresenter;
import com.jess.arms.base.BaseActivity;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;

import static com.jess.arms.utils.Preconditions.checkNotNull;


public class HobbyActivity extends BaseActivity<HobbyPresenter> implements HobbyContract.View {
    public static void start(Context context) {
        Router.newIntent(context)
                .to(HobbyActivity.class)
                .launch();
    }
    @Override
    public void setupActivityComponent(@NonNull AppComponent appComponent) {
        DaggerHobbyComponent
                .builder()
                .appComponent(appComponent)
                .hobbyModule(new HobbyModule(this))
                .build()
                .inject(this);
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.activity_hobby;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
        GlobalUtils.statusBarTint(this);
        GlobalUtils.toolBarInit(this, "兴趣爱好", true);

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
