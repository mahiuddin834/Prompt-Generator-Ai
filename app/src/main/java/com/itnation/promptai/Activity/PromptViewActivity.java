package com.itnation.promptai.Activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
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
import com.itnation.promptai.R;

public class PromptViewActivity extends AppCompatActivity {

    ImageView backBtn, mainImageView;
    TextView promptTxtView;
    Button saveImgBtn, copyPromptBtn;

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
        saveImgBtn= findViewById(R.id.saveImgBtn);
        copyPromptBtn = findViewById(R.id.copyPromptBtn);



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

                if (promptTxtView != null){

                    ClipboardManager cm = (ClipboardManager)PromptViewActivity.this.getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(promptTxtView.getText());
                    Toast.makeText(PromptViewActivity.this, "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }//------------close onCreateView ---------------
}