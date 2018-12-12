package com.aidingyun.ynlive.mvp.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.aidingyun.ynlive.R;
import com.aidingyun.ynlive.app.utils.LoadImage;
import com.aidingyun.ynlive.mvp.model.entity.HomeCourseModel;

import java.util.List;

/**
 * Created by Administrator on 2015/11/24.
 */
public class RecycleItemAdapterCourseList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<HomeCourseModel.TypeDataBean.TypeCourseDataBean> results;


    public RecycleItemAdapterCourseList(Context context, List<HomeCourseModel.TypeDataBean.TypeCourseDataBean> results) {
        this.context = context;
        this.results = results;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.course_list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder){
            bind((ItemViewHolder) holder,position);
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }


    private void bind(ItemViewHolder holder, int position){
        holder.tv_course_title.setText(results.get(position).getScore()+"分");
        holder.tv_time.setText(results.get(position).getCollection());
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_no;
        public TextView tv_course_title;
        public TextView tv_time;


        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_no = (TextView) itemView.findViewById(R.id.tv_no);
            tv_course_title = (TextView) itemView.findViewById(R.id.tv_course_title);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        }
    }
}
