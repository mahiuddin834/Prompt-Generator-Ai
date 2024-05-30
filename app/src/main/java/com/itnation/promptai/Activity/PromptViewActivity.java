package com.itnation.promptai.Activity;

import static android.content.ContentValues.TAG;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.itnation.promptai.R;

public class PromptViewActivity extends AppCompatActivity {

    ImageView backBtn, mainImageView;
    TextView promptTxtView;
    Button copyPromptBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_prompt_view);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        backBtn= findViewById(R.id.backBtn);
        mainImageView= findViewById(R.id.mainImageView);
        promptTxtView= findViewById(R.id.promptTxt);
        copyPromptBtn = findViewById(R.id.copyPromptBtn);


        //-----------------------------------
        fullScreenAds();



//-----------------------------




        //statusbar white Icon---------------------

        getWindow().getDecorView().setSystemUiVisibility(0);
        if (Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(this.getResources().getColor(R.color.statusbar_color));
        }

        //-------------------



        String imgLink = getIntent().getStringExtra("imageLink");
        String promptTxt = getIntent().getStringExtra("promptTxt");



        Glide.with(PromptViewActivity.this)
                .load(imgLink)
                .fitCenter()
                .placeholder(R.drawable.loading_img)
                .into(mainImageView);


        promptTxtView.setText(promptTxt);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();

            }
        });

        copyPromptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (interstitialAd!=null && interstitialAd.isAdLoaded()){
                    interstitialAd.show();
                }


                if (promptTxtView != null){

                    ClipboardManager cm = (ClipboardManager)PromptViewActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(promptTxtView.getText());
                    Toast.makeText(PromptViewActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }//------------close onCreateView ---------------


    InterstitialAd interstitialAd;

    private void fullScreenAds(){


        interstitialAd = new InterstitialAd(PromptViewActivity.this, "YOUR_PLACEMENT_ID");
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




    @Override
    protected void onDestroy() {
        if (interstitialAd != null) {
            interstitialAd.destroy();
        }

        super.onDestroy();
    }


    //=========================================



}