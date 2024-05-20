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
import com.itnation.promptai.ModelClass.ExploreMainRVModel;
import com.itnation.promptai.R;

import java.util.ArrayList;

public class ExploreMainRVAdapter extends RecyclerView.Adapter<ExploreMainRVAdapter.ViewHolder> {

    Context context;
    ArrayList<ExploreMainRVModel> exploreMainRVModelArrayList;

    public ExploreMainRVAdapter(Context context, ArrayList<ExploreMainRVModel> exploreMainRVModelArrayList) {
        this.context = context;
        this.exploreMainRVModelArrayList = exploreMainRVModelArrayList;
    }

    @NonNull
    @Override
    public ExploreMainRVAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.explore_main_rv_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreMainRVAdapter.ViewHolder holder, int position) {

        ExploreMainRVModel exploreMainRVModel = exploreMainRVModelArrayList.get(position);


        Glide.with(context)
                .load(exploreMainRVModel.getImgLink())
                .centerInside()
                .placeholder(R.drawable.loading_img)
                .into(holder.imageThumbMain);

        holder.promptTxtMain.setText(exploreMainRVModel.getPromptTxt());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(context, PromptViewActivity.class);
                intent.putExtra("imageLink", exploreMainRVModel.getImgLink());
                intent.putExtra("promptTxt", exploreMainRVModel.getPromptTxt());
                context.startActivity(intent);
            }
        });



    }

    @Override
    public int getItemCount() {
        return exploreMainRVModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageThumbMain;
        TextView promptTxtMain;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageThumbMain=itemView.findViewById(R.id.imageThumbMain);
            promptTxtMain=itemView.findViewById(R.id.promptTxtMain);


        }
    }
}
