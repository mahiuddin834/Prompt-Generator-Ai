package com.itnation.promptai.Activity;

import static android.content.ContentValues.TAG;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
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
import com.itnation.promptai.AiModel.MultiAiModel;
import com.itnation.promptai.AiModel.ResponseCallback;
import com.itnation.promptai.R;

public class ImageToPromptActivity extends AppCompatActivity {

    private static final int SELECT_IMAGE_REQUEST = 1;
    Bitmap image;
    ImageView backBtn, inputImage;
    Button generateBtn, copyPromptBtn;
    EditText ansTxt;
    ProgressBar progressBar;
    LinearLayout resultLay;

    AdView adView;
    int bannerAdsClick=0;
    LinearLayout banner_container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_image_to_prompt);
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


        backBtn= findViewById(R.id.backBtn);
        inputImage= findViewById(R.id.inputImage);
        generateBtn = findViewById(R.id.generateBtn);
        ansTxt= findViewById(R.id.ansTxt);
        progressBar= findViewById(R.id.progressBar);
        copyPromptBtn= findViewById(R.id.copyPromptBtn);
        resultLay= findViewById(R.id.resultLay);


        loadBannerAds();
        fullScreenAds();




        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                onBackPressed();
            }
        });


        inputImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, SELECT_IMAGE_REQUEST);


            }
        });


        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (interstitialAd!=null && interstitialAd.isAdLoaded()){
                    interstitialAd.show();
                }

                if (image != null){
                    generatedImageToPrompt();
                }else {
                    Toast.makeText(ImageToPromptActivity.this, "Please Tap to Select Image", Toast.LENGTH_SHORT).show();
                }



            }
        });

        copyPromptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (ansTxt != null){

                    ClipboardManager cm = (ClipboardManager)ImageToPromptActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(ansTxt.getText());
                    Toast.makeText(ImageToPromptActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }//------------close onCreateView ------------------


    private void generatedImageToPrompt(){


        progressBar.setVisibility(View.VISIBLE);

        resultLay.setVisibility(View.GONE);

        String promptQuery = "Convert this image from image to prompt text.";


        MultiAiModel model = new MultiAiModel();

        model.getResponse(promptQuery, image, new ResponseCallback() {
            @Override
            public void onResponse(String response) {


                progressBar.setVisibility(View.GONE);
                ansTxt.setText(response);
                resultLay.setVisibility(View.VISIBLE);

            }

            @Override
            public void onError(Throwable throwable) {
                Toast.makeText(ImageToPromptActivity.this, "Error: " + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });





    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == SELECT_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap originalImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                int originalWidth = originalImage.getWidth();
                int originalHeight = originalImage.getHeight();

                int targetWidth = (int) (originalWidth * 0.5);
                int targetHeight = (int) (originalHeight * 0.5);

                image = Bitmap.createScaledBitmap(originalImage, targetWidth, targetHeight, false);
                inputImage.setImageBitmap(originalImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }//-------------





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


        interstitialAd = new InterstitialAd(ImageToPromptActivity.this, "YOUR_PLACEMENT_ID");
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