package com.blessapp.blessapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfileActivity extends AppCompatActivity {

    private CircleImageView profileImg;
    private EditText fullNameEdit, userPhoneEdit, emailEdit,birthdateEdit;
    private TextView changeBtn, saveBtn;
    private Uri imgUri;
    private String myUrl = "";
    private StorageReference storageProfilePictureReference;
    private String checker = "";
    private StorageTask uploadTask;
    private Button secureQuestBtn;
    private ProgressDialog progressDialog;

    private ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        storageProfilePictureReference = FirebaseStorage.getInstance().getReference().child("Profile Pictures");

        profileImg = findViewById(R.id.settings_profile_image);

        userPhoneEdit = findViewById(R.id.settings_phone_number);
        fullNameEdit = findViewById(R.id.settings_full_name);
        emailEdit = findViewById(R.id.settings_email);
        birthdateEdit = findViewById(R.id.settings_birthdate);

        changeBtn = findViewById(R.id.profile_image_change_btn);
        backBtn = findViewById(R.id.back_btn_setting);
        saveBtn = findViewById(R.id.update_account_settings_btn);

        progressDialog = new ProgressDialog(this);

        //secureQuestBtn = findViewById(R.id.security_quest_btn);

        userInfoDisplay(profileImg, fullNameEdit, userPhoneEdit, emailEdit, birthdateEdit);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //To change/upload profile picture
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When user click this button/text, it will update the checker from
                //null to clicked which will execute savedBtn
                checker = "clicked";

                CropImage.activity(imgUri)
                        .setAspectRatio(1,1)
                        .start(UpdateProfileActivity.this);
            }
        });

        //To update new profile info
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //We have two things here because:
                //1) for user who updated both their info and profile picture
                //2) for user who update only their info
                progressDialog.setTitle("Update Profile");
                progressDialog.setMessage("Updating account information...");
                progressDialog.setCanceledOnTouchOutside(false);

                if (checker.equals("clicked")){
                    //this is when user update their profile picture
                    userInfoSaved();
                }
                else {
                    //this is when user only update their info, to prevent picture from being overwrite
                    updateOnlyUserInfo();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK && data!=null){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imgUri = result.getUri();

            profileImg.setImageURI(imgUri);
        }
        else{
            Toast.makeText(this, "Error occurred, please try again.", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(UpdateProfileActivity.this, UpdateProfileActivity.class));
            finish();
        }
    }

    private void updateOnlyUserInfo() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUserid = user.getUid();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("user");

        HashMap<String, Object> userMap = new HashMap<>();

        userMap.put("phone", userPhoneEdit.getText().toString().trim());//For contact purposes
        userMap.put("name", fullNameEdit.getText().toString().trim());
        userMap.put("email", emailEdit.getText().toString().trim());
        userMap.put("birthdate", birthdateEdit.getText().toString().trim());
        ref.child(currentUserid).updateChildren(userMap);

        progressDialog.dismiss();
        Toast.makeText(UpdateProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

        startActivity(new Intent(UpdateProfileActivity.this, ProfileMainActivity.class));
        finish();
    }

    private void userInfoSaved() {
        if (TextUtils.isEmpty(fullNameEdit.getText().toString())) {
            Toast.makeText(this, "Please enter your full name.",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(userPhoneEdit.getText().toString())) {
            Toast.makeText(this, "Please enter your phone number.",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(emailEdit.getText().toString())) {
            Toast.makeText(this, "Please enter your email.",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(birthdateEdit.getText().toString())) {
            Toast.makeText(this, "Please enter your birthdate.",Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked")){
            uploadImage();
        }
    }

    private void uploadImage() {

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUserid = user.getUid();
        //Storing image into Firebase Storage
        if (imgUri!=null){
            final StorageReference fileRef = storageProfilePictureReference
                    .child(currentUserid + ".jpg");

            uploadTask = fileRef.putFile(imgUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()){
                        //If not, the app will throw the code exception for debugging
                        throw task.getException();
                    }

                    //Return download url of image as reference
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if (task.isSuccessful()) {

                        //Getting result, which is from the return value above
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String, Object> userMap = new HashMap<>();

                        userMap.put("image", myUrl);

                        userMap.put("phone", userPhoneEdit.getText().toString().trim());//For contact purposes
                        userMap.put("name", fullNameEdit.getText().toString().trim());
                        userMap.put("email", emailEdit.getText().toString().trim());
                        userMap.put("birthdate", birthdateEdit.getText().toString().trim());
                        //To update the current online user remember me
                        ref.child(currentUserid).updateChildren(userMap);
                        //ref.child(currentUserid.updateChildren(userMap));

                        progressDialog.dismiss();

                        Toast.makeText(UpdateProfileActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(UpdateProfileActivity.this, ProfileMainActivity.class));
                        finish();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateProfileActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(UpdateProfileActivity.this, "Image is not selected", Toast.LENGTH_SHORT).show();
        }

    }

    private void userInfoDisplay(final CircleImageView profileImg, final EditText fullNameEdit, final EditText userPhoneEdit, final EditText emailEdit, final EditText birthdateEdit) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String currentUserid = user.getUid();

        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserid);

        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name, phone, birthdate;

                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("image").exists()){

                        String image = dataSnapshot.child("image").getValue().toString();

                        if (dataSnapshot.child("name").exists()){
                            name = dataSnapshot.child("name").getValue().toString();
                        }
                        else {
                            name = dataSnapshot.child("email").getValue().toString();
                        }

                        phone = dataSnapshot.child("phone").getValue().toString();
                        birthdate = dataSnapshot.child("birthdate").getValue().toString();

                        //String pass = dataSnapshot.child("password").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();

                        Picasso.get().load(image).into(profileImg);
                        fullNameEdit.setText(name);
                        userPhoneEdit.setText(phone);
                        emailEdit.setText(email);
                        birthdateEdit.setText(birthdate);

                    }
                    else if (!dataSnapshot.child("image").exists()) {
                        if (dataSnapshot.child("name").exists()) {
                            if (dataSnapshot.child("phone").exists()){

                                name = dataSnapshot.child("name").getValue().toString();
                                phone = dataSnapshot.child("phone").getValue().toString();

                                fullNameEdit.setText(name);
                                userPhoneEdit.setText(phone);

                            }
                        } else {
                            name = dataSnapshot.child("email").getValue().toString();
                            fullNameEdit.setText(name);
                        }
                    }
                }
                else{
                    Toast.makeText(UpdateProfileActivity.this, "Your information is empty.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(UpdateProfileActivity.this, ProfileMainActivity.class);
        startActivity(intent);
        finish();
    }
}
