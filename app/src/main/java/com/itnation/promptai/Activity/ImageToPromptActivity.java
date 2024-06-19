package com.itnation.promptai.Activity;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.ImageDecoder;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.itnation.promptai.AiModel.MultiAiModel;
import com.itnation.promptai.AiModel.ResponseCallback;
import com.itnation.promptai.R;

public class ImageToPromptActivity extends AppCompatActivity {

    private static final int REQUEST_PERMISSION_CODE = 100;
    private Bitmap image;
    private ImageView backBtn, inputImage;
    private Button generateBtn, copyPromptBtn;
    private EditText ansTxt;
    private ProgressBar progressBar;
    private LinearLayout resultLay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_to_prompt);

        initView();

        // Set status bar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.statusbar_color));
        }

        backBtn.setOnClickListener(v -> onBackPressed());

        inputImage.setOnClickListener(v -> checkAndRequestPermissions());

        generateBtn.setOnClickListener(v -> {
            if (image != null) {
                generateImageToPrompt();
            } else {
                Toast.makeText(ImageToPromptActivity.this, "Please Tap to Select Image", Toast.LENGTH_SHORT).show();
            }
        });

        copyPromptBtn.setOnClickListener(v -> {
            if (ansTxt != null) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setPrimaryClip(ClipData.newPlainText("Prompt Text", ansTxt.getText()));
                Toast.makeText(ImageToPromptActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView() {
        backBtn = findViewById(R.id.backBtn);
        inputImage = findViewById(R.id.inputImage);
        generateBtn = findViewById(R.id.generateBtn);
        ansTxt = findViewById(R.id.ansTxt);
        progressBar = findViewById(R.id.progressBar);
        copyPromptBtn = findViewById(R.id.copyPromptBtn);
        resultLay = findViewById(R.id.resultLay);
    }

    private final ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(
            new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    selectImage();
                } else {
                    Toast.makeText(ImageToPromptActivity.this, "Permission required to access images", Toast.LENGTH_SHORT).show();
                }
            });

    private final ActivityResultLauncher<Intent> selectImageLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    handleImageResult(result.getData());
                }
            });

    private void checkAndRequestPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Android 13+ এর জন্য READ_MEDIA_IMAGES পারমিশন
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED) {
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            } else {
                selectImage();
            }
        } else {
            // Android 13 এর নিচে জন্য READ_EXTERNAL_STORAGE পারমিশন
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_PERMISSION_CODE);
            } else {
                selectImage();
            }
        }
    }

    private void selectImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        selectImageLauncher.launch(intent);
    }

    private void handleImageResult(Intent data) {
        Uri imageUri = data.getData();
        try {
            Bitmap originalImage;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                originalImage = ImageDecoder.decodeBitmap(ImageDecoder.createSource(this.getContentResolver(), imageUri));
            } else {
                originalImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            }

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

    private void generateImageToPrompt() {
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            } else {
                Toast.makeText(this, "Permission required to access images", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
