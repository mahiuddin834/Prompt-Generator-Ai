package com.itnation.promptai.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.itnation.promptai.ModelClass.ExploreCategoryModel;
import com.itnation.promptai.R;

import java.util.ArrayList;

public class ExploreCategoryAdapter extends RecyclerView.Adapter<ExploreCategoryAdapter.ViewHolder> {

    Context context;
    ArrayList<ExploreCategoryModel> exploreCategoryModelArrayList;

    categoryClickInterface categoryClickInterface;

    public ExploreCategoryAdapter(Context context, ArrayList<ExploreCategoryModel> exploreCategoryModelArrayList, ExploreCategoryAdapter.categoryClickInterface categoryClickInterface) {
        this.context = context;
        this.exploreCategoryModelArrayList = exploreCategoryModelArrayList;
        this.categoryClickInterface = categoryClickInterface;
    }

    @NonNull
    @Override
    public ExploreCategoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.explore_category_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExploreCategoryAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {

        ExploreCategoryModel exploreCategoryModel= exploreCategoryModelArrayList.get(position);

        holder.categoryName.setText(exploreCategoryModel.getCategoryName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                categoryClickInterface.onCategoryClick(position);


            }
        });




    }

    @Override
    public int getItemCount() {
        return exploreCategoryModelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView categoryName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryName = itemView.findViewById(R.id.categoryName);

        }

    }


    public interface categoryClickInterface{

        void onCategoryClick(int position);


    }
}
