package com.itnation.promptai.Fragment;

import android.os.Bundle;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itnation.promptai.Adapter.ExploreCategoryAdapter;
import com.itnation.promptai.Adapter.ExploreMainRVAdapter;
import com.itnation.promptai.Adapter.ForYouRVAdapter;
import com.itnation.promptai.MainActivity;
import com.itnation.promptai.ModelClass.ExploreCategoryModel;
import com.itnation.promptai.ModelClass.ExploreMainRVModel;
import com.itnation.promptai.ModelClass.ForYouRVModel;
import com.itnation.promptai.R;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ExploreFragment extends Fragment implements ExploreCategoryAdapter.categoryClickInterface{

    ProgressBar progressBar;

    DatabaseReference databaseReference;

    RecyclerView categoryRV, mainRV;
    EditText searchEditTxt;

    ExploreCategoryModel exploreCategoryModel;
    ExploreCategoryAdapter exploreCategoryAdapter;
    ArrayList<ExploreCategoryModel> exploreCategoryModelArrayList;


    ShimmerFrameLayout shimmerLayout;
    LinearLayout rootLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_explore, container, false);

        categoryRV = view.findViewById(R.id.categoryRV);
        mainRV = view.findViewById(R.id.mainRV);
        searchEditTxt = view.findViewById(R.id.searchEditTxt);
        progressBar = view.findViewById(R.id.progressBar);
        shimmerLayout = view.findViewById(R.id.shimmerLayout);
        rootLayout = view.findViewById(R.id.exploreRootLay);

        rootLayout.setVisibility(View.GONE);
        shimmerLayout.startShimmer();


        loadCategory();
        loadMainRV("sliderHome");

        String searchQuery = searchEditTxt.getText().toString();


        return view;
    }//-------------close onCreateView--------------------------




    private void loadCategory() {


        databaseReference = FirebaseDatabase.getInstance().getReference("exploreCategory");

        categoryRV.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
        exploreCategoryModelArrayList = new ArrayList<>();
        exploreCategoryAdapter = new ExploreCategoryAdapter(getActivity(), exploreCategoryModelArrayList, this::onCategoryClick);
        categoryRV.setAdapter(exploreCategoryAdapter);


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    exploreCategoryModel = dataSnapshot.getValue(ExploreCategoryModel.class);
                    exploreCategoryModelArrayList.add(exploreCategoryModel);


                }

                exploreCategoryAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }



    @Override
    public void onCategoryClick(int position) {


        String category =exploreCategoryModelArrayList.get(position).getCategoryName();
        loadMainRV(category);


    }


    ExploreMainRVAdapter exploreMainRVAdapter;
    ExploreMainRVModel exploreMainRVModel;
    ArrayList<ExploreMainRVModel> exploreMainRVModelArrayList;
    String questiono = "Apple";


    private void loadMainRV(String dataBaseName){


        databaseReference = FirebaseDatabase.getInstance().getReference(dataBaseName);

        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

        mainRV.setLayoutManager(staggeredGridLayoutManager);
        exploreMainRVModelArrayList= new ArrayList<>();
        exploreMainRVAdapter = new ExploreMainRVAdapter(getActivity(), exploreMainRVModelArrayList);
        mainRV.setAdapter(exploreMainRVAdapter);




        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot :snapshot.getChildren()){


                    exploreMainRVModel = dataSnapshot.getValue(ExploreMainRVModel.class);
                    exploreMainRVModelArrayList.add(exploreMainRVModel);


                }

                exploreCategoryAdapter.notifyDataSetChanged();

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




    /*       =============================================
    private void loadMainRVwithLexica() {


        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);

        mainRV.setLayoutManager(staggeredGridLayoutManager);
        exploreMainRVModelArrayList = new ArrayList<>();
        exploreMainRVAdapter = new ExploreMainRVAdapter(getActivity(), exploreMainRVModelArrayList);
        mainRV.setAdapter(exploreMainRVAdapter);

        String url = "https://lexica.art/api/v1/search?q=" + questiono;
        // Request a string response from the provided URL.
//        imageModel.add(new MessageModelClass("Typing....", MessageModelClass.SENT_BY_GPT));
        progressBar.setVisibility(View.VISIBLE);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                JSONArray jsonArray;
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    jsonArray = jsonObject.getJSONArray("images");

                    for (int i = 0; i < jsonArray.length(); i++) {

                        String promptTxt = jsonArray.getJSONObject(i).getString("prompt");
                        String image = jsonArray.getJSONObject(i).getString("src");
                        exploreMainRVModel = new ExploreMainRVModel(image, promptTxt);

                        //imageModel1 = new ImageModel(jsonArray.getJSONObject(i).getString("srcSmall"));

                        exploreMainRVModelArrayList.add(exploreMainRVModel);
                    }

                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }


                progressBar.setVisibility(View.GONE);
                exploreMainRVAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), "" + error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("q", questiono);
                return map;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(request);








  */


}




