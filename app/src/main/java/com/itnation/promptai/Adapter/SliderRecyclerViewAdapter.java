package com.itnation.promptai.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itnation.promptai.Activity.PromptViewActivity;
import com.itnation.promptai.ModelClass.SliderRVModel;
import com.itnation.promptai.R;

import java.util.ArrayList;

public class SliderRecyclerViewAdapter extends RecyclerView.Adapter<SliderRecyclerViewAdapter.ViewHolder> {

    Context context;
    ArrayList<SliderRVModel> sliderRVModelArrayList;

    public SliderRecyclerViewAdapter(Context context, ArrayList<SliderRVModel> sliderRVModelArrayList) {
        this.context = context;
        this.sliderRVModelArrayList = sliderRVModelArrayList;
    }

    @NonNull
    @Override
    public SliderRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.landscape_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SliderRecyclerViewAdapter.ViewHolder holder, int position) {

        SliderRVModel sliderRVModel= sliderRVModelArrayList.get(position);


        Glide.with(context)
                .load(sliderRVModel.getImgLink())
                .centerCrop()
                .into(holder.imgThumb);

        holder.promptTxt.setText(sliderRVModel.getPromptTxt());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(context, PromptViewActivity.class);
                intent.putExtra("imageLink", sliderRVModel.getImgLink());
                intent.putExtra("promptTxt", sliderRVModel.getPromptTxt());
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return sliderRVModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgThumb;
        TextView promptTxt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgThumb=itemView.findViewById(R.id.imageThumbLandScape);
            promptTxt=itemView.findViewById(R.id.promptTxtLandScape);
        }
    }
}
