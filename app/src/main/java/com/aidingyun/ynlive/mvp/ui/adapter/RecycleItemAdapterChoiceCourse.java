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
import com.aidingyun.ynlive.app.utils.ToastUtils;
import com.aidingyun.ynlive.mvp.model.entity.CurriculumTypeModel;
import com.aidingyun.ynlive.mvp.model.entity.HomeCourseModel;
import com.aidingyun.ynlive.mvp.ui.activity.course_detail.CourseDetailActivity;
import com.bumptech.glide.Glide;


import java.util.List;

/**
 * Created by Administrator on 2015/11/24.
 */
public class RecycleItemAdapterChoiceCourse extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<HomeCourseModel.ChoiceCourseDataBean> choiceCourseDataBeanList;

    public RecycleItemAdapterChoiceCourse(Context context, List<HomeCourseModel.ChoiceCourseDataBean> choiceCourseDataBeanList) {
        this.context = context;
        this.choiceCourseDataBeanList = choiceCourseDataBeanList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_item_type2_item, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder){
            bind((ItemViewHolder) holder,position);
        }
    }

    @Override
    public int getItemCount() {
        return choiceCourseDataBeanList.size();
    }


    private void bind(ItemViewHolder holder, int position){
//        Glide.with(context)
//                .load(choiceCourseDataBeanList.get(position).getPhoto())
//                .into(holder.iv_head);
        LoadImage.loadNormalImage(context,choiceCourseDataBeanList.get(position).getPhoto(),holder.iv_head);
        holder.tv_title.setText(choiceCourseDataBeanList.get(position).getTitle());
        holder.tv_start.setText(choiceCourseDataBeanList.get(position).getScore()+"分");
        holder.tv_collent.setText(choiceCourseDataBeanList.get(position).getCollection()+"人收藏");
        if (choiceCourseDataBeanList.get(position).getAllow().equals("0")){
            holder.tv_price.setText("免费");
            holder.tv_unit.setVisibility(View.GONE);
        }else if (choiceCourseDataBeanList.get(position).getAllow().equals("1")){
            holder.tv_price.setText("加密");
            holder.tv_unit.setVisibility(View.GONE);
        }else if (choiceCourseDataBeanList.get(position).getAllow().equals("2")){
            holder.tv_price.setText(choiceCourseDataBeanList.get(position).getPrice());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CourseDetailActivity.start(context,choiceCourseDataBeanList.get(position).getCourseid());
//                ToastUtils.show(holder.mImageView.getContext(), "" + position);
            }
        });
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_head;
        TextView tv_title;
        TextView tv_start;
        TextView tv_collent;
        TextView tv_price;
        TextView tv_unit;

        public ItemViewHolder(View itemView) {
            super(itemView);
            iv_head = (ImageView) itemView.findViewById(R.id.iv_head);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_start = (TextView) itemView.findViewById(R.id.tv_start);
            tv_collent = (TextView) itemView.findViewById(R.id.tv_collent);
            tv_price = (TextView) itemView.findViewById(R.id.tv_price);
            tv_unit = (TextView) itemView.findViewById(R.id.tv_unit);
        }
    }
}
