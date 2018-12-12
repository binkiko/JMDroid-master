package com.aidingyun.ynlive.mvp.ui.activity.course_detail;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.aidingyun.core.router.Router;
import com.aidingyun.ynlive.R;
import com.aidingyun.ynlive.app.service.ABaseService;
import com.aidingyun.ynlive.app.utils.ToastUtils;
import com.aidingyun.ynlive.app.utils.UpdateVersionUtils;
import com.aidingyun.ynlive.mvp.contract.Global;
import com.aidingyun.ynlive.mvp.model.entity.CourseDetailInfo;
import com.aidingyun.ynlive.mvp.ui.adapter.MyPagerAdapter;
import com.aidingyun.ynlive.mvp.ui.fragment.course_detail.CourseListFragment;
import com.aidingyun.ynlive.mvp.ui.fragment.course_detail.CourseReferralFragment;
import com.androidkun.xtablayout.XTabLayout;
import com.google.gson.Gson;
import com.vector.update_app.HttpManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

public class CourseDetailActivity extends FragmentActivity {
    private XTabLayout mTabLayout;
    private ViewPager mViewPager;
    static String course_id = "";
    List<Fragment> fragmentList = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    String isLoad = "";

    private UpdateVersionUtils updateVersionUtils = null;

    {//构造代码块
        titles.add("课程介绍");
        titles.add("课程列表");
        titles.add("学员评价");
    }


    public static void start(Context context, String courseid) {
        course_id = courseid;
        Router.newIntent(context)
                .putString("courseid",courseid)
                .to(CourseDetailActivity.class)
                .launch();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_detail);
        updateVersionUtils = new UpdateVersionUtils();
        mTabLayout = findViewById(R.id.xTablayout);
//        mViewPager = findViewById(R.id.view_pager);
        searchCourseDetail(course_id);
        initViewPager();
    }

    private void initViewPager() {
//         fragmentList.add(CourseReferralFragment.newInstance(isLoad));//财经
//        fragmentList.add(CourseListFragment.newInstance(isLoad));//教育
//        fragmentList.add(LegalFragment.newInstance());//法律
//        fragmentList.add(OtherFragment.newInstance());//其他
//        for (int i = 0; i < titles.size(); i++) {
//                fragmentList.add(CourseReferralFragment.newInstance());
//        }
        mTabLayout.addTab(mTabLayout.newTab().setText("课程介绍"));
        mTabLayout.addTab(mTabLayout.newTab().setText("课程列表"));
        mTabLayout.addTab(mTabLayout.newTab().setText("学员评价"));
        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
//        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList, titles);
//        mViewPager.setOffscreenPageLimit(titles.size());
//        mViewPager.setAdapter(adapter);
//        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(1).select();
        // 这样可以自定义tab的布局与内容了
        //mTabLayout.getTabAt(0).setCustomView(R.layout.tab_custom_view);
//        mTabLayout.getTabAt(0).getCustomView();
        // 设置监听
        mTabLayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(XTabLayout.Tab tab) {
                String text = (String) tab.getText();
                ToastUtils.show(CourseDetailActivity.this,"选中" + text);
            }


            @Override
            public void onTabUnselected(XTabLayout.Tab tab) {
                String text = (String) tab.getText();
                ToastUtils.show(CourseDetailActivity.this,"不要" + text + "了");
            }


            @Override
            public void onTabReselected(XTabLayout.Tab tab) {
                String text = (String) tab.getText();
                ToastUtils.show(CourseDetailActivity.this,"再次点击" + text);
            }
        });
    }


    private void searchCourseDetail(String courseid) {
        Map<String, String> reqBody = new ConcurrentSkipListMap<>();
        reqBody.put("courseid", courseid);
        updateVersionUtils.postByName(Global.GET_COURSE_DETAIL_SERVICE_NAME, reqBody, new HttpManager.Callback() {
            @Override
            public void onResponse(String result) {
                Log.e("SearchCourseActivity","updateVersionUtils++++++++++++++++"+result);
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String code = jsonObject.getString("code");
                    if (code.equals("success")){
                        Gson gson = new Gson();
                        ABaseService.courseDetailInfo = gson.fromJson(result,CourseDetailInfo.class);
                        isLoad = "true";
//                        rlChannels.setLayoutManager(new GridLayoutManager(rlChannels.getContext(), 2, GridLayoutManager.VERTICAL, false));
//                        adapter = new RecycleAdapter(getActivity(),ABaseService.homeCourseModel);
//                        rlChannels.setAdapter(adapter);
                    }else{
                        ToastUtils.showError(CourseDetailActivity.this,jsonObject.getString("msg"));
                    }
                }catch (Exception e){
                    e.getMessage();
                }

            }

            @Override
            public void onError(String error) {
                Log.e("SearchCourseActivity","updateVersionUtils++++++++++++++++"+error);
            }
        });

    }
}
