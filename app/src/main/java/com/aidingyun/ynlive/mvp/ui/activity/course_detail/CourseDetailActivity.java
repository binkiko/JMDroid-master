package com.aidingyun.ynlive.mvp.ui.activity.course_detail;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aidingyun.core.router.Router;
import com.aidingyun.ynlive.R;
import com.aidingyun.ynlive.app.service.ABaseService;
import com.aidingyun.ynlive.app.utils.ToastUtils;
import com.aidingyun.ynlive.app.utils.UpdateVersionUtils;
import com.aidingyun.ynlive.mvp.contract.Global;
import com.aidingyun.ynlive.mvp.model.entity.CourseDetailInfo;
import com.aidingyun.ynlive.mvp.ui.adapter.MyPagerAdapter;
import com.aidingyun.ynlive.mvp.ui.fragment.course_detail.CourseCommentListFragment;
import com.aidingyun.ynlive.mvp.ui.fragment.course_detail.CourseListFragment;
import com.aidingyun.ynlive.mvp.ui.fragment.course_detail.CourseReferralFragment;
import com.aidingyun.ynlive.mvp.ui.widget.DefinitionController;
import com.aidingyun.ynlive.mvp.ui.widget.DefinitionIjkVideoView;
import com.androidkun.xtablayout.XTabLayout;
import com.dou361.ijkplayer.widget.IRenderView;
import com.dueeeke.videoplayer.player.IjkPlayer;
import com.dueeeke.videoplayer.player.PlayerConfig;
import com.google.gson.Gson;
import com.vector.update_app.HttpManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;

import tv.danmaku.ijk.media.player.IjkMediaPlayer;

public class CourseDetailActivity extends FragmentActivity {
    private XTabLayout mTabLayout;
    private ViewPager mViewPager;
    static String course_id = "";
    List<Fragment> fragmentList = new ArrayList<>();
    List<String> titles = new ArrayList<>();
    String isLoad = "";
    LinearLayout tipTextView;
    TextView tv_buy_course_btn;
    CheckBox tv_study;
    CheckBox iv_collect_btn;
    private DefinitionIjkVideoView surface_view;
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private int currentPosition = 0;
    private boolean isPlaying;

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
        mViewPager = findViewById(R.id.view_pager);
        surface_view = findViewById(R.id.surface_view);
        tv_buy_course_btn = findViewById(R.id.tv_buy_course_btn);
        tv_study = findViewById(R.id.tv_study);
        iv_collect_btn = findViewById(R.id.iv_collect_btn);
        tipTextView = findViewById(R.id.tipTextView);
        ((ImageView)findViewById(R.id.iv_back)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ((TextView)findViewById(R.id.tv_title)).setText("我的订单");
        DefinitionController controller = new DefinitionController(this);
        surface_view.setPlayerConfig(new PlayerConfig.Builder()
                .setCustomMediaPlayer(new IjkPlayer(this) {
                    @Override
                    public void setOptions() {
                        //精准seek
                        mMediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "enable-accurate-seek", 1);
                    }
                })
                .autoRotate()//自动旋转屏幕
                .build());

        LinkedHashMap<String, String> videos = new LinkedHashMap<>();
        videos.put("标清", "http://mov.bn.netease.com/open-movie/nos/flv/2017/07/24/SCP786QON_sd.flv");
        videos.put("高清", "http://mov.bn.netease.com/open-movie/nos/flv/2017/07/24/SCP786QON_hd.flv");
        videos.put("超清", "http://mov.bn.netease.com/open-movie/nos/flv/2017/07/24/SCP786QON_shd.flv");
        surface_view.setDefinitionVideos(videos);
        surface_view.setVideoController(controller);
        surface_view.setTitle("韩雪：积极的悲观主义者");
        surface_view.start();
        searchCourseDetail(course_id);
        tipTextView.setVisibility(View.GONE);//下节课程介绍
        iv_collect_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                }
            }
        });
        tv_study.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){

                }
            }
        });

        tv_buy_course_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CourseDetailActivity.this,ConfirmOrderActivity.class));
            }
        });



    }

    private void initViewPager() {
         fragmentList.add(CourseReferralFragment.newInstance(isLoad));//
        fragmentList.add(CourseListFragment.newInstance(isLoad));//
        fragmentList.add(CourseCommentListFragment.newInstance("","",""));//
//        fragmentList.add(OtherFragment.newInstance());//其他

        mTabLayout.setTabMode(TabLayout.MODE_FIXED);
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), fragmentList, titles);
        mViewPager.setOffscreenPageLimit(titles.size());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(adapter);//给Tabs设置适配器
//        mTabLayout.getTabAt(0).select();
        // 这样可以自定义tab的布局与内容了

        // 设置监听
//        mTabLayout.setOnTabSelectedListener(new XTabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(XTabLayout.Tab tab) {
//                String text = (String) tab.getText();
//                ToastUtils.show(CourseDetailActivity.this,"选中" + text);
//            }
//
//
//            @Override
//            public void onTabUnselected(XTabLayout.Tab tab) {
//                String text = (String) tab.getText();
//                ToastUtils.show(CourseDetailActivity.this,"不要" + text + "了");
//            }
//
//
//            @Override
//            public void onTabReselected(XTabLayout.Tab tab) {
//                String text = (String) tab.getText();
//                ToastUtils.show(CourseDetailActivity.this,"再次点击" + text);
//            }
//        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        surface_view.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        surface_view.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        surface_view.release();
    }


    @Override
    public void onBackPressed() {
        if (!surface_view.onBackPressed()) {
            super.onBackPressed();
        }
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
                        initViewPager();
                        if (ABaseService.courseDetailInfo.getData().getAllow().equals("0")){
                            tv_buy_course_btn.setEnabled(false);
                            tv_buy_course_btn.setBackgroundResource(R.color.material_grey_400);
                        }
//                        surface_view.setAspectRatio(IRenderView.AR_ASPECT_FIT_PARENT);
//                        surface_view.setVideoURI(Uri.parse("http://mov.bn.netease.com/open-movie/nos/flv/2017/01/03/SC8U8K7BC_hd.flv"));
//                        surface_view.start();
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
