package com.itnation.promptai.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.itnation.promptai.Activity.CreatePromptActivity;
import com.itnation.promptai.Activity.ImageToPromptActivity;
import com.itnation.promptai.R;


public class GenerateFragment extends Fragment {



    RelativeLayout createPromptBtn, imageToPromptBtn;
    ImageView mainBanner;
    RecyclerView historyRv;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_generate, container, false);

        createPromptBtn = view.findViewById(R.id.createPromptBtn);
        imageToPromptBtn = view.findViewById(R.id.imageToPromptBtn);
        mainBanner = view.findViewById(R.id.mainBanner);



        createPromptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), CreatePromptActivity.class);
                startActivity(intent);

            }
        });


        imageToPromptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity( new Intent(getActivity(), ImageToPromptActivity.class));



            }
        });





        return view;
    }//-------- close OnCreateView-------------------------------------------






}