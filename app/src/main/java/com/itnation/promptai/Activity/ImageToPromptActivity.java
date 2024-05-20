package com.itnation.promptai.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.itnation.promptai.AiModel.AiModel;
import com.itnation.promptai.AiModel.MultiAiModel;
import com.itnation.promptai.AiModel.ResponseCallback;
import com.itnation.promptai.R;

public class ImageToPromptActivity extends AppCompatActivity {

    private static final int SELECT_IMAGE_REQUEST = 1;
    Bitmap image;
    ImageView backBtn, inputImage, copyBtn;
    Button generateBtn;
    EditText ansTxt;
    ProgressBar progressBar;
    RelativeLayout ansLayout;

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
        copyBtn = findViewById(R.id.copyBtn);
        generateBtn = findViewById(R.id.generateBtn);
        ansLayout= findViewById(R.id.ansLayout);
        ansTxt= findViewById(R.id.ansTxt);
        progressBar= findViewById(R.id.progressBar);




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


                if (image != null){
                    generatedImageToPrompt();
                }else {
                    Toast.makeText(ImageToPromptActivity.this, "Please Tap to Select Image", Toast.LENGTH_SHORT).show();
                }





            }
        });

        copyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });


    }//------------close onCreateView ------------------


    private void generatedImageToPrompt(){


        progressBar.setVisibility(View.VISIBLE);


        String promptQuery = "Convert this image from image to prompt text.";


        MultiAiModel model = new MultiAiModel();

        model.getResponse(promptQuery, image, new ResponseCallback() {
            @Override
            public void onResponse(String response) {


                progressBar.setVisibility(View.GONE);
                ansTxt.setText(response);
                ansLayout.setVisibility(View.VISIBLE);

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
    }
}