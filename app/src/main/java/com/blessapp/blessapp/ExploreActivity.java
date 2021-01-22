package com.blessapp.blessapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExploreActivity extends AppCompatActivity {

    Button backtoMainBtn;
    CircleImageView userprofileimg;
    TextView words, congrats;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore_item);

        userprofileimg = findViewById(R.id.userProfileImg);
    backtoMainBtn = findViewById(R.id.backtomainbtn);
    words = findViewById(R.id.wordsId);
    congrats = findViewById(R.id.congratsId);
    backtoMainBtn.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(ExploreActivity.this, ProductPageActivity.class);
            startActivity(intent);
        }
    });


    }
}
