package com.blessapp.blessapp.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.blessapp.blessapp.HomeActivity;
import com.blessapp.blessapp.LoginActivity;
import com.blessapp.blessapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class AdminAuthenticationActivity extends AppCompatActivity {

    Button adminloginButton;
    EditText uname, upass;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    CheckBox  showpass;
    TextView forgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_authentication);

        adminloginButton = findViewById(R.id.admin_login);
        uname = findViewById(R.id.adminlogin_username_input);
        upass = findViewById(R.id.adminlogin_password_input);
        progressBar = findViewById(R.id.progressbar);
        showpass = findViewById(R.id.adminlogin_checkbox);
        forgotPass = findViewById(R.id.forget_password_link);

        mAuth = FirebaseAuth.getInstance();

        //show password
        showpass.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    upass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    upass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        //admin login
        adminloginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adminLogin();
            }
        });

    }

    private void adminLogin() {

        // show the visibility of progress bar to show loading
        progressBar.setVisibility(View.VISIBLE);

        // Take the value of two edit texts in Strings
        String email, password;
        email = uname.getText().toString();
        password = upass.getText().toString();

        // Validations for input email and password
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter email!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(),
                    "Please enter password!!",
                    Toast.LENGTH_LONG)
                    .show();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(),
                            "Registration successful!",
                            Toast.LENGTH_LONG)
                            .show();

                    // hide the progress bar
                    progressBar.setVisibility(View.GONE);

                    // if the user created intent to login activity
                    Intent intent
                            = new Intent(AdminAuthenticationActivity.this,
                            AdminManageActivity.class);
                    startActivity(intent);
                }
                else {

                    // Registration failed
                    Toast.makeText(
                            getApplicationContext(),
                            "Registration failed!!"
                                    + " Please try again later",
                            Toast.LENGTH_LONG)
                            .show();

                    // hide the progress bar
                    progressBar.setVisibility(View.GONE);
                }
            }
        });
    }
}
