package com.blessapp.blessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blessapp.blessapp.Model.Users;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileMainActivity extends AppCompatActivity {


    Toolbar toolbar;
    TextView username, emailInput, phoneNumber, birthdate, address;
    Button editBtn;
    CircleImageView userImg;
    String userID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUserid = user.getUid();

        toolbar = findViewById(R.id.toolbar_profilepage);


        username =findViewById(R.id.username);
        emailInput = findViewById(R.id.emailName);
        phoneNumber = findViewById(R.id.phoneNum);
        birthdate = findViewById(R.id.birthdate);
        editBtn = findViewById(R.id.editProfileBtn);
        userImg = findViewById(R.id.userProfileImg);
        address = findViewById(R.id.addressname);

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
            }
        });

        userInfoDisplay(currentUserid);

    }

    private void userInfoDisplay(String currentUserid) {


        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("user");
        userRef.child(currentUserid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    Users user = dataSnapshot.getValue(Users.class);

                    Picasso.get().load(user.getImage()).into(userImg);
                    username.setText(user.getUsername());
                    phoneNumber.setText(user.getPhone());
                    emailInput.setText(user.getEmail());
                    birthdate.setText(user.getBirthdate());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void editProfile() {
        Intent intent = new Intent(ProfileMainActivity.this, UpdateProfileActivity.class);
        startActivity(intent);
    }
}
