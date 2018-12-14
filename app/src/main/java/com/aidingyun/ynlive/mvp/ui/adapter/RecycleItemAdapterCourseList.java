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
import com.aidingyun.ynlive.mvp.model.entity.CourseDetailInfo;
import com.aidingyun.ynlive.mvp.model.entity.HomeCourseModel;

import java.util.List;

/**
 * Created by Administrator on 2015/11/24.
 */
public class RecycleItemAdapterCourseList extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<CourseDetailInfo.SectionBean> sectionBeans;


    public RecycleItemAdapterCourseList(Context context, List<CourseDetailInfo.SectionBean> sectionBeans) {
        this.context = context;
        this.sectionBeans = sectionBeans;
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
        return sectionBeans.size();
    }


    private void bind(ItemViewHolder holder, int position){
        holder.tv_course_title.setText(sectionBeans.get(position).getSection_title()+"分");
        holder.tv_time.setText(sectionBeans.get(position).getSection_date()+"  "+sectionBeans.get(position).getSection_time());
        holder.tv_no.setText("0"+position+1);
        if (sectionBeans.get(position).getAllow().equals("0")){
            holder.iv_lock.setVisibility(View.GONE);
        }else if (sectionBeans.get(position).getAllow().equals("1")){
            holder.iv_lock.setVisibility(View.VISIBLE);
        }else if (sectionBeans.get(position).getAllow().equals("2")){
            holder.iv_lock.setVisibility(View.GONE);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_no;
        public TextView tv_course_title;
        public TextView tv_time;
        public ImageView iv_lock;


        public ItemViewHolder(View itemView) {
            super(itemView);
            tv_no = (TextView) itemView.findViewById(R.id.tv_no);
            tv_course_title = (TextView) itemView.findViewById(R.id.tv_course_title);
            tv_time = (TextView) itemView.findViewById(R.id.tv_time);
            iv_lock = (ImageView) itemView.findViewById(R.id.iv_lock);
        }
    }
}
