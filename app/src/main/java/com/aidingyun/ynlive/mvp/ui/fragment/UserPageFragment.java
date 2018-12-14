package com.aidingyun.ynlive.mvp.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aidingyun.ynlive.R;
import com.aidingyun.ynlive.app.inter.IndexRefresh;
import com.aidingyun.ynlive.app.service.ABaseService;
import com.aidingyun.ynlive.app.utils.LoadImage;
import com.aidingyun.ynlive.app.utils.ToastUtils;
import com.aidingyun.ynlive.app.utils.UpdateVersionUtils;
import com.aidingyun.ynlive.di.component.DaggerUserPageComponent;
import com.aidingyun.ynlive.di.module.UserPageModule;
import com.aidingyun.ynlive.mvp.contract.Global;
import com.aidingyun.ynlive.mvp.contract.UserPageContract;
import com.aidingyun.ynlive.mvp.model.entity.CourseOrderInfo;
import com.aidingyun.ynlive.mvp.presenter.UserPagePresenter;
import com.aidingyun.ynlive.mvp.ui.activity.account.LoginActivity;
import com.aidingyun.ynlive.mvp.ui.activity.account.MyGoldActivity;
import com.aidingyun.ynlive.mvp.ui.activity.account.PersonalCenterActivity;
import com.aidingyun.ynlive.mvp.ui.activity.ordermanager.OrderManagerActivity;
import com.aidingyun.ynlive.mvp.ui.adapter.order.RecycleItemAdapterOrder;
import com.aidingyun.ynlive.mvp.ui.widget.BadgeButton;
import com.aidingyun.ynlive.mvp.ui.widget.CircleImageView;
import com.google.gson.Gson;
import com.jess.arms.base.BaseFragment;
import com.jess.arms.di.component.AppComponent;
import com.jess.arms.utils.ArmsUtils;
import com.vector.update_app.HttpManager;

import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.jess.arms.utils.Preconditions.checkNotNull;


public class UserPageFragment extends BaseFragment<UserPagePresenter> implements UserPageContract.View, IndexRefresh, View.OnClickListener {

    private CircleImageView mAvatar;
    /**
     * 登陆/注册
     */
    private TextView mNickname;
    /**
     * 登陆有惊喜
     */
    private TextView mUserId;
    /**
     * 充值
     */
    private TextView mTvRecharge;
    /**
     * 我的资料
     */
    private TextView mUserData;
    /**
     * 我的收藏
     */
    private TextView mUserCollect;
    /**
     * 我的账单
     */
    private TextView mUserBill;
    /**
     * 消息通知
     */
    private TextView mUserNotification;
    private RelativeLayout mUserInfo;
    private LinearLayout mOne;
    private LinearLayout mTwo;
    private LinearLayout mThree;
    private LinearLayout mFour;
    private LinearLayout mFive;
    private LinearLayout mSix;
    private LinearLayout mSeven;
    private LinearLayout mEight;
    private TextView tv_balance;
    private TextView tv_wealth;
    private UpdateVersionUtils updateVersionUtils = null;

    public static UserPageFragment newInstance() {
        UserPageFragment fragment = new UserPageFragment();
        return fragment;
    }

    @Override
    public void setupFragmentComponent(@NonNull AppComponent appComponent) {
        DaggerUserPageComponent //如找不到该类,请编译一下项目
                .builder()
                .appComponent(appComponent)
                .userPageModule(new UserPageModule(this))
                .build()
                .inject(this);
    }

    @Override
    public View initView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_page, container, false);
        TextView title = view.findViewById(R.id.title);
        title.setText("我的");
        ABaseService.preferences = getActivity().getSharedPreferences(getActivity().getPackageName(),Context.MODE_PRIVATE);
        ABaseService.islogin =ABaseService.preferences.getBoolean("islogin",false);
        updateVersionUtils = new UpdateVersionUtils();
        mAvatar = (CircleImageView) view.findViewById(R.id.avatar);
        mAvatar.setOnClickListener(this);
        mNickname = (TextView) view.findViewById(R.id.nickname);
        mNickname.setOnClickListener(this);
        mUserId = (TextView) view.findViewById(R.id.userId);
        mUserId.setOnClickListener(this);
        mUserInfo = (RelativeLayout) view.findViewById(R.id.user_info);
        mUserInfo.setOnClickListener(this);
        mTvRecharge = (TextView) view.findViewById(R.id.tv_recharge);
        mTvRecharge.setOnClickListener(this);
        mUserData = (TextView) view.findViewById(R.id.user_data);
        mUserData.setOnClickListener(this);
        mUserCollect = (TextView) view.findViewById(R.id.user_collect);
        mUserCollect.setOnClickListener(this);
        mUserBill = (TextView) view.findViewById(R.id.user_bill);
        mUserBill.setOnClickListener(this);
        mUserNotification = (BadgeButton) view.findViewById(R.id.user_notification);
        mUserNotification.setOnClickListener(this);
        mOne = (LinearLayout) view.findViewById(R.id.one);
        mOne.setOnClickListener(this);
        mTwo = (LinearLayout) view.findViewById(R.id.two);
        mTwo.setOnClickListener(this);
        mThree = (LinearLayout) view.findViewById(R.id.three);
        mThree.setOnClickListener(this);
        mFour = (LinearLayout) view.findViewById(R.id.four);
        mFour.setOnClickListener(this);
        mFive = (LinearLayout) view.findViewById(R.id.five);
        mFive.setOnClickListener(this);
        mSix = (LinearLayout) view.findViewById(R.id.six);
        mSix.setOnClickListener(this);
        mSeven = (LinearLayout) view.findViewById(R.id.seven);
        mSeven.setOnClickListener(this);
        mEight = (LinearLayout) view.findViewById(R.id.eight);
        mEight.setOnClickListener(this);
        tv_wealth = (TextView) view.findViewById(R.id.tv_wealth);
        tv_balance = (TextView) view.findViewById(R.id.tv_balance);
        return view;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {
//        if (ABaseService.islogin) {
//            LoadImage.loadCircleImage(getActivity(), ABaseService.loginInfo.getIcon(), mAvatar);
//            mNickname.setText(ABaseService.loginInfo.getUsername());
//            mUserId.setText(ABaseService.loginInfo.getUserid());
//        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (ABaseService.islogin) {
            LoadImage.loadCircleImage(getActivity(), ABaseService.loginInfo.getIcon(), mAvatar);
            mNickname.setText(ABaseService.loginInfo.getUsername());
            mUserId.setText(ABaseService.loginInfo.getUserid());
            getAccount("user",ABaseService.loginInfo.getUserid());
        }else{
            LoadImage.loadCircleImagedefult(getActivity(), R.drawable.avatar_default, mAvatar);
            mNickname.setText("未登录");
            mUserId.setText("未登录");
        }
    }

    @Override
    public void setData(@Nullable Object data) {

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

    }


    /**
     *
     * @param userid user：用户 organization：机构
     * @param accountid  op=user，用户id。 op=organization，机构id。
     */
    private void getAccount(String userid,String accountid) {
        Map<String, String> reqBody = new ConcurrentSkipListMap<>();
        reqBody.put("op", userid);
        reqBody.put("accountid", accountid);
        updateVersionUtils.postByName(Global.GET_ACCOUNT_SERVICE_NAME, reqBody, new HttpManager.Callback() {
            @Override
            public void onResponse(String result) {
                Log.e("NoPaymentFragment","result++++++++++++++++"+result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("SUCCESS")){
                        JSONObject data = jsonObject.getJSONObject("data");
                        tv_wealth.setText(data.getString("wealth"));
                        tv_balance.setText(data.getString("current_coin"));
                    }else{
                        ToastUtils.showError(getActivity(),jsonObject.getString("msg"));
                    }
                }catch (Exception e){
                    e.getMessage();
                }

            }

            @Override
            public void onError(String error) {
                Log.e("NoPaymentFragment","error++++++++++++++++"+error);
            }
        });

    }



    @Override
    public void doUIRefresh() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.avatar:
            case R.id.nickname:
            case R.id.userId:
            case R.id.user_info:
                if (ABaseService.islogin) {
                    //todo 未登录----跳转登录界面、 已登录---跳转个人中心界面PersonalCenter
                    PersonalCenterActivity.start(getActivity());
                } else {
                    startActivityForResult(new Intent(getActivity(),LoginActivity.class),Activity.RESULT_OK);
//                LoginActivity.start(getActivity());
                }
                break;
            case R.id.tv_recharge:
//                RechargeActivity.start(getActivity());
                if (ABaseService.islogin) {
                    //todo 未登录----跳转登录界面、 已登录---跳转充值界面
                MyGoldActivity.start(getActivity());
                } else {
                    startActivityForResult(new Intent(getActivity(),LoginActivity.class),Activity.RESULT_OK);
//                LoginActivity.start(getActivity());
                }
                break;
            case R.id.user_data:
                if (ABaseService.islogin) {
                    //todo 未登录----跳转登录界面、 已登录---跳转个人中心界面PersonalCenter
                } else {
                    startActivityForResult(new Intent(getActivity(),LoginActivity.class),Activity.RESULT_OK);
//                LoginActivity.start(getActivity());
                }
                break;
            case R.id.user_collect:
                if (ABaseService.islogin) {
                    //todo 未登录----跳转登录界面、 已登录---跳转个人中心界面PersonalCenter
                } else {
                    startActivityForResult(new Intent(getActivity(),LoginActivity.class),Activity.RESULT_OK);
//                LoginActivity.start(getActivity());
                }
                break;
            case R.id.user_bill:
                if (ABaseService.islogin) {
                    //todo 未登录----跳转登录界面、 已登录---跳转个人中心界面PersonalCenter
                } else {
                    startActivityForResult(new Intent(getActivity(),LoginActivity.class),Activity.RESULT_OK);
//                LoginActivity.start(getActivity());
                }
                break;
            case R.id.user_notification:
                if (ABaseService.islogin) {
                    //todo 未登录----跳转登录界面、 已登录---跳转个人中心界面PersonalCenter
                } else {
                    startActivityForResult(new Intent(getActivity(),LoginActivity.class),Activity.RESULT_OK);
//                LoginActivity.start(getActivity());
                }
                break;
            case R.id.one:
                if (ABaseService.islogin) {
                    //todo 未登录----跳转登录界面、 已登录---跳转个人中心界面PersonalCenter
                } else {
                    startActivityForResult(new Intent(getActivity(),LoginActivity.class),Activity.RESULT_OK);
//                LoginActivity.start(getActivity());
                }
                break;
            case R.id.two:
                if (ABaseService.islogin) {
                    //todo 未登录----跳转登录界面、 已登录---跳转个人中心界面PersonalCenter
                } else {
                    startActivityForResult(new Intent(getActivity(),LoginActivity.class),Activity.RESULT_OK);
//                LoginActivity.start(getActivity());
                }
                break;
            case R.id.three:
                if (ABaseService.islogin) {
                    //todo 未登录----跳转登录界面、 已登录---跳转个人中心界面PersonalCenter
                } else {
                    startActivityForResult(new Intent(getActivity(),LoginActivity.class),Activity.RESULT_OK);
//                LoginActivity.start(getActivity());
                }
                break;
            case R.id.four:
                if (ABaseService.islogin) {
                    //todo 未登录----跳转登录界面、 已登录---跳转个人中心界面PersonalCenter
                    OrderManagerActivity.start(getActivity());
                } else {
                    startActivityForResult(new Intent(getActivity(),LoginActivity.class),Activity.RESULT_OK);
//                LoginActivity.start(getActivity());
                }
                break;
            case R.id.five:
                if (ABaseService.islogin) {
                    //todo 未登录----跳转登录界面、 已登录---跳转个人中心界面PersonalCenter
                } else {
                    startActivityForResult(new Intent(getActivity(),LoginActivity.class),Activity.RESULT_OK);
//                LoginActivity.start(getActivity());
                }
                break;
            case R.id.six:
                if (ABaseService.islogin) {
                    //todo 未登录----跳转登录界面、 已登录---跳转个人中心界面PersonalCenter
                } else {
                    startActivityForResult(new Intent(getActivity(),LoginActivity.class),Activity.RESULT_OK);
//                LoginActivity.start(getActivity());
                }
                break;
            case R.id.seven:
                if (ABaseService.islogin) {
                    //todo 未登录----跳转登录界面、 已登录---跳转个人中心界面PersonalCenter
                } else {
                    startActivityForResult(new Intent(getActivity(),LoginActivity.class),Activity.RESULT_OK);
//                LoginActivity.start(getActivity());
                }
                break;
            case R.id.eight:
                if (ABaseService.islogin) {
                    //todo 未登录----跳转登录界面、 已登录---跳转个人中心界面PersonalCenter
                } else {
                    startActivityForResult(new Intent(getActivity(),LoginActivity.class),Activity.RESULT_OK);
//                LoginActivity.start(getActivity());
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }
}
