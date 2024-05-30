package com.itnation.promptai.Activity;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.itnation.promptai.AiModel.AiModel;
import com.itnation.promptai.AiModel.ResponseCallback;
import com.itnation.promptai.R;

public class CreatePromptActivity extends AppCompatActivity {

    EditText promptTitleTxt, ansTxt;
    ImageView backBtn;
    Button generateBtn, copyBtn;
    Spinner promptTypeSpinner, aiListSpinner;
    LinearLayout ansLayout;
    ProgressBar progressBar;

    String[] promptType = {
            "Select Prompt Type", "Image", "Video", "Video Reel", "Content"
    };

    String[] aiName = {
            "Select Ai", "Chat GPT", "Gemini", "DALL-E", "Midjourney", "Leonardo AI", "Nightcafe Creator", "Imagen", "Synthesia", "D-ID", "HeyGen", "Pictory"
    };

    String promptTypeSpinnerTxt;
    String aiListSpinnerTxt;


    AdView adView;
    int bannerAdsClick=0;
    LinearLayout banner_container;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_prompt);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //statusbar white Icon---------------------

        getWindow().getDecorView().setSystemUiVisibility(0);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar_color));
        }

        //-------------------


        //promptViewModel= ViewModelProviders.of(this).get(PromptViewModel.class);





        promptTitleTxt = findViewById(R.id.promptTitleTxt);
        ansTxt = findViewById(R.id.ansTxt);
        backBtn = findViewById(R.id.backBtn);
        copyBtn=findViewById(R.id.copyBtn);
        generateBtn = findViewById(R.id.generateBtn);
        promptTypeSpinner = findViewById(R.id.promptTypeSpinner);
        aiListSpinner= findViewById(R.id.aiListSpinner);
        progressBar= findViewById(R.id.progressBar);
        ansLayout = findViewById(R.id.ansLayout);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


        getSpinnerTxt();


        loadBannerAds();
        fullScreenAds();


        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (interstitialAd!=null && interstitialAd.isAdLoaded()){
                    interstitialAd.show();
                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                if (promptTitleTxt.getText().toString().length() >= 5) {

                    String inputTxt = promptTitleTxt.getText().toString();

                    generatePrompt(inputTxt);


                } else {
                    promptTitleTxt.setError("Please write about your prompt");
                }




            }
        });


        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ClipboardManager cm = (ClipboardManager)CreatePromptActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(ansTxt.getText());
                Toast.makeText(CreatePromptActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();

            }
        });

    }//----------close OnCreateView-------------------

    private void generatePrompt(String inputTxt) {



        progressBar.setVisibility(View.VISIBLE);
        ansLayout.setVisibility(View.GONE);


        String promptQuery = "I want to generate an " + promptTypeSpinnerTxt + " through" + aiListSpinnerTxt + "Ai. The "+ promptTypeSpinnerTxt +" I want is: "+  inputTxt +
                ". Now customize this prompt of mine in an interesting and advance way. As if "+ aiListSpinnerTxt + " Ai gives me high quality image";


        AiModel model = new AiModel();

        model.getResponse(promptQuery, new ResponseCallback() {
            @Override
            public void onResponse(String response) {


                progressBar.setVisibility(View.GONE);
                ansTxt.setText(response);
                ansLayout.setVisibility(View.VISIBLE);

            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(CreatePromptActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });


    }


    private void getSpinnerTxt(){



        ArrayAdapter<String> promptTypeAdapter = new ArrayAdapter<>(CreatePromptActivity.this, R.layout.spinner_layout, promptType);
        promptTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        promptTypeSpinner.setAdapter(promptTypeAdapter);


        ArrayAdapter<String> aiNameAdapter = new ArrayAdapter<>(CreatePromptActivity.this, R.layout.spinner_layout, aiName);
        aiNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        aiListSpinner.setAdapter(aiNameAdapter);


        promptTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (parent.getItemAtPosition(position).toString() == "Select Prompt Type") {

                    promptTypeSpinnerTxt = "";

                } else {

                    promptTypeSpinnerTxt = parent.getItemAtPosition(position).toString();

                    Toast.makeText(CreatePromptActivity.this, promptTypeSpinnerTxt, Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        aiListSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if (parent.getItemAtPosition(position).toString() == "Select Ai") {

                    aiListSpinnerTxt = "";

                } else {

                    aiListSpinnerTxt = parent.getItemAtPosition(position).toString();

                    Toast.makeText(CreatePromptActivity.this, aiListSpinnerTxt, Toast.LENGTH_SHORT).show();

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




    }//---------------




    //banner ads
    private void loadBannerAds(){

        adView = new AdView(this, "", AdSize.BANNER_HEIGHT_50);

// Find the Ad Container
        banner_container= findViewById(R.id.banner_container);

// Add the ad view to your activity layout
        banner_container.addView(adView);


        AdListener adListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {


            }

            @Override
            public void onAdLoaded(Ad ad) {
// Ad loaded callback
            }

            @Override
            public void onAdClicked(Ad ad) {

                bannerAdsClick++;

                if (bannerAdsClick>=2){

                    if (adView!=null){
                        adView.destroy();
                        banner_container.setVisibility(View.GONE);
                    }
                }
// Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
// Ad impression logged callback
            }
        };

        adView.loadAd(adView.buildLoadAdConfig().withAdListener(adListener).build());
    }
    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }

        if (interstitialAd != null) {
            interstitialAd.destroy();
        }
        super.onDestroy();
    }


    //====================


    InterstitialAd interstitialAd;

    private void fullScreenAds(){


        interstitialAd = new InterstitialAd(CreatePromptActivity.this, "YOUR_PLACEMENT_ID");
        // Create listeners for the Interstitial Ad
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(com.facebook.ads.Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(com.facebook.ads.Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");

                fullScreenAds();


            }

            @Override
            public void onError(com.facebook.ads.Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(com.facebook.ads.Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");




            }

            @Override
            public void onAdClicked(com.facebook.ads.Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");


            }

            @Override
            public void onLoggingImpression(com.facebook.ads.Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());




    }



}