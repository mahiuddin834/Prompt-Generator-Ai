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
import com.itnation.promptai.ModelClass.MostPopularRVModel;
import com.itnation.promptai.R;

import java.util.ArrayList;

public class MostPopularRVAdapter extends RecyclerView.Adapter<MostPopularRVAdapter.ViewHolder> {

    Context context;
    ArrayList<MostPopularRVModel> mostPopularRVModelArrayList;

    public MostPopularRVAdapter(Context context, ArrayList<MostPopularRVModel> mostPopularRVModelArrayList) {
        this.context = context;
        this.mostPopularRVModelArrayList = mostPopularRVModelArrayList;
    }

    @NonNull
    @Override
    public MostPopularRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.circle_rv_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MostPopularRVAdapter.ViewHolder holder, int position) {

        MostPopularRVModel mostPopularRVModel = mostPopularRVModelArrayList.get(position);

        Glide.with(context)
                .load(mostPopularRVModel.getImgLink())
                .centerCrop()
                .into(holder.imgThumb);

        holder.promptTxt.setText(mostPopularRVModel.getPromptTxt());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(context, PromptViewActivity.class);
                intent.putExtra("imageLink", mostPopularRVModel.getImgLink());
                intent.putExtra("promptTxt", mostPopularRVModel.getPromptTxt());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return mostPopularRVModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgThumb;
        TextView promptTxt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgThumb=itemView.findViewById(R.id.imageThumbCircle);
            promptTxt=itemView.findViewById(R.id.promptTxtCircle);
        }
    }
}
