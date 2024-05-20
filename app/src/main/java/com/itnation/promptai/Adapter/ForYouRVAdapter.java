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
import com.itnation.promptai.ModelClass.ForYouRVModel;
import com.itnation.promptai.R;

import java.util.ArrayList;

public class ForYouRVAdapter extends RecyclerView.Adapter<ForYouRVAdapter.ViewHloder> {

    Context context;
    ArrayList<ForYouRVModel> forYouRVModelArrayList;

    public ForYouRVAdapter(Context context, ArrayList<ForYouRVModel> forYouRVModelArrayList) {
        this.context = context;
        this.forYouRVModelArrayList = forYouRVModelArrayList;
    }

    @NonNull
    @Override
    public ForYouRVAdapter.ViewHloder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.landscape_rv_item, parent, false);
        return new ViewHloder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ForYouRVAdapter.ViewHloder holder, int position) {

        ForYouRVModel forYouRVModel= forYouRVModelArrayList.get(position);

        Glide.with(context)
                .load(forYouRVModel.getImgLink())
                .centerCrop()
                .into(holder.imgThumb);

        holder.promptTxt.setText(forYouRVModel.getPromptTxt());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(context, PromptViewActivity.class);
                intent.putExtra("imageLink", forYouRVModel.getImgLink());
                intent.putExtra("promptTxt", forYouRVModel.getPromptTxt());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return forYouRVModelArrayList.size();
    }

    public static class ViewHloder extends RecyclerView.ViewHolder {

        ImageView imgThumb;
        TextView promptTxt;
        public ViewHloder(@NonNull View itemView) {
            super(itemView);

            imgThumb=itemView.findViewById(R.id.imageThumbLandScape);
            promptTxt=itemView.findViewById(R.id.promptTxtLandScape);


        }
    }
}
