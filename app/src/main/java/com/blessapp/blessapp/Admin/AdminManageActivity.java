package com.blessapp.blessapp.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.blessapp.blessapp.R;

public class AdminManageActivity extends AppCompatActivity {

    CardView additem, manage_order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_manage);

        additem = findViewById(R.id.addItem);
        manage_order = findViewById(R.id.manageOrder);

        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminManageActivity.this, AdminAddNewItemActivity.class);
                startActivity(intent);
            }
        });

        manage_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AdminManageActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });
    }
}
