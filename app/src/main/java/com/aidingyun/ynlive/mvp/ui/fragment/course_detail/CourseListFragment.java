package com.aidingyun.ynlive.mvp.ui.fragment.course_detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.aidingyun.ynlive.R;
import com.aidingyun.ynlive.app.utils.ToastUtils;
import com.aidingyun.ynlive.app.utils.UpdateVersionUtils;
import com.aidingyun.ynlive.mvp.contract.Global;
import com.aidingyun.ynlive.mvp.ui.adapter.RecycleAdapter;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.vector.update_app.HttpManager;

import org.json.JSONObject;

import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;


public class CourseListFragment extends Fragment {
    private int mIndex;
    private String type_id="";
    RecyclerView rlChannels;
    SmartRefreshLayout refreshLayout;
    private RelativeLayout network_fail;
    private RecycleAdapter adapter;
    private int offset = 0;

    RecyclerView recycler_view;

    private UpdateVersionUtils updateVersionUtils = null;

    public static CourseListFragment newInstance(String isLoad) {
        Bundle args = new Bundle();
        args.putString("isLoad", isLoad);
        CourseListFragment fragment = new CourseListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.course_fragment_list_item, container, false);
        updateVersionUtils = new UpdateVersionUtils();
        recycler_view = view.findViewById(R.id.recycler_view);
        recycler_view.setLayoutManager(new GridLayoutManager(recycler_view.getContext(), 1, GridLayoutManager.VERTICAL, false));
//        network_fail = view.findViewById(R.id.network_fail);//网络连接失败布局
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState); //获取参数
        Bundle arguments = getArguments(); //改变值
//        mTv.setText(arguments.getString("tag"));
//        searchCourseDetail(arguments.getString("courseid"));
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
//                        Gson gson = new Gson();
//                        ABaseService.homeCourseModel = gson.fromJson(result,HomeCourseModel.class);
//                        rlChannels.setLayoutManager(new GridLayoutManager(rlChannels.getContext(), 2, GridLayoutManager.VERTICAL, false));
//                        adapter = new RecycleAdapter(getActivity(),ABaseService.homeCourseModel);
//                        rlChannels.setAdapter(adapter);
                    }else{
                        ToastUtils.showError(getActivity(),jsonObject.getString("msg"));
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
