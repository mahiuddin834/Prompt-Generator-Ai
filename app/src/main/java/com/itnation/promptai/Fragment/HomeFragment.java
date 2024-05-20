package com.itnation.promptai.Fragment;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itnation.promptai.Adapter.ForYouRVAdapter;
import com.itnation.promptai.Adapter.MostPopularRVAdapter;
import com.itnation.promptai.Adapter.SliderRecyclerViewAdapter;
import com.itnation.promptai.ModelClass.ForYouRVModel;
import com.itnation.promptai.ModelClass.MostPopularRVModel;
import com.itnation.promptai.ModelClass.SliderRVModel;
import com.itnation.promptai.R;

import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends Fragment {


    ShimmerFrameLayout shimmerLayout;
    NestedScrollView rootLayout;

    RecyclerView sliderRv, mostPopularRv, forYouRv;
    SliderRecyclerViewAdapter sliderRecyclerViewAdapter;
    ArrayList<SliderRVModel> sliderRVModelArrayList;
    SliderRVModel sliderRVModel;
    DatabaseReference databaseReference;

    MostPopularRVAdapter mostPopularRVAdapter;
    MostPopularRVModel mostPopularRVModel;
    ArrayList<MostPopularRVModel> mostPopularRVModelArrayList;

    ForYouRVAdapter forYouRVAdapter;
    ForYouRVModel forYouRVModel;
    ArrayList<ForYouRVModel> forYouRVModelArrayList;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_home, container, false);


        sliderRv=view.findViewById(R.id.sliderRv);
        mostPopularRv=view.findViewById(R.id.mostPopularRv);
        forYouRv=view.findViewById(R.id.forYouRv);
        rootLayout = view.findViewById(R.id.rootLayout);
        shimmerLayout = view.findViewById(R.id.shimmerLayout);

        rootLayout.setVisibility(View.GONE);
        shimmerLayout.startShimmer();





        loadSlider();
        loadMostPopular();
        loadForYou();



        return view;
    }//------close onCreateView-------------------------------------

    private void loadForYou() {

        databaseReference = FirebaseDatabase.getInstance().getReference("sliderHome");
        forYouRv.setLayoutManager(new LinearLayoutManager(getActivity()));

        forYouRVModelArrayList= new ArrayList<>();

        forYouRVAdapter = new ForYouRVAdapter(getActivity(), forYouRVModelArrayList);
        forYouRv.setAdapter(forYouRVAdapter);



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot :snapshot.getChildren()){


                    forYouRVModel = dataSnapshot.getValue(ForYouRVModel.class);
                    forYouRVModelArrayList.add(forYouRVModel);


                }

                forYouRVAdapter.notifyDataSetChanged();


                shimmerLayout.stopShimmer();
                rootLayout.setVisibility(View.VISIBLE);
                shimmerLayout.setVisibility(View.GONE);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void loadMostPopular() {

        databaseReference = FirebaseDatabase.getInstance().getReference("sliderHome");
        mostPopularRv.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));

        mostPopularRVModelArrayList = new ArrayList<>();

        mostPopularRVAdapter = new MostPopularRVAdapter(getActivity(), mostPopularRVModelArrayList);
        mostPopularRv.setAdapter(mostPopularRVAdapter);



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot :snapshot.getChildren()){


                    mostPopularRVModel = dataSnapshot.getValue(MostPopularRVModel.class);
                    mostPopularRVModelArrayList.add(mostPopularRVModel);


                }

                mostPopularRVAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });


    }


    private void loadSlider(){

        databaseReference = FirebaseDatabase.getInstance().getReference("sliderHome");

        LinearLayoutManager layoutManager =new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        sliderRv.setLayoutManager(layoutManager);

        sliderRVModelArrayList = new ArrayList<>();

        sliderRecyclerViewAdapter = new SliderRecyclerViewAdapter(getActivity(), sliderRVModelArrayList);
        sliderRv.setAdapter(sliderRecyclerViewAdapter);



        //Recyclerview Auto Scrolling start-------------------------------
        LinearSnapHelper linearSnapHelper= new LinearSnapHelper();
        linearSnapHelper.attachToRecyclerView(sliderRv);

        Timer timer= new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if (layoutManager.findLastCompletelyVisibleItemPosition() < (sliderRecyclerViewAdapter.getItemCount() -1)){

                    layoutManager.smoothScrollToPosition(sliderRv, new RecyclerView.State(), layoutManager.findLastCompletelyVisibleItemPosition() +1);

                }else if (layoutManager.findLastCompletelyVisibleItemPosition() < (sliderRecyclerViewAdapter.getItemCount() -1)){

                    layoutManager.smoothScrollToPosition(sliderRv, new RecyclerView.State(), 0);
                }

            }
        }, 0, 3000);

        //RecyclerView Auto Scrolling End---------------------------


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot :snapshot.getChildren()){


                    sliderRVModel = dataSnapshot.getValue(SliderRVModel.class);
                    sliderRVModelArrayList.add(sliderRVModel);


                }

                sliderRecyclerViewAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });



    }


    /*

    private void popular(){


        databaseReference = FirebaseDatabase.getInstance().getReference("Popular");
        popular_recycler.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));

        popularModelArrayList = new ArrayList<>();

        popularAdapter = new PopularAdapter(getActivity(), popularModelArrayList);
        popular_recycler.setAdapter(popularAdapter);



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot :snapshot.getChildren()){


                    PopularModel popularModel = dataSnapshot.getValue(PopularModel.class);
                    popularModelArrayList.add(popularModel);


                }

                popularAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), error.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });


    }

    //-----------------------------------------------------------
     */


}