package com.blessapp.blessapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.blessapp.blessapp.Model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FavouriteMainActivity extends AppCompatActivity {

    RecyclerView favrecyclerView;
    BottomNavigationView bottomNav;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_main);

        bottomNav = findViewById(R.id.bottom_navigation);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserid = user.getUid();

        favrecyclerView = findViewById(R.id.favouriteRecycler);
       favrecyclerView.setHasFixedSize(true);
        // favrecyclerView.setHasFixedSize(true);
        favrecyclerView.setLayoutManager(new LinearLayoutManager(this));

        reference = database.getReference("favoriteList").child(currentUserid);

        bottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab_home:
                        startActivity(new Intent(getApplicationContext(), ProductPageActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.tab_explore:
                        startActivity(new Intent(getApplicationContext(), ExploreMainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.tab_favourite:
/*                        startActivity(new Intent(getApplicationContext(), FavouriteMainActivity.class));
                        overridePendingTransition(0,0);*/
                        return true;
                    case R.id.tab_profile:
                        startActivity(new Intent(getApplicationContext(), ProfileMainActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                }
                return false;
            }
        });

        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                        .setQuery(reference, Product.class)
                        .build();

        FirebaseRecyclerAdapter<Product, com.blessapp.blessapp.Product_Viewholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Product, com.blessapp.blessapp.Product_Viewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull com.blessapp.blessapp.Product_Viewholder holder, int position, @NonNull final Product model) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        final String currentUserid = user.getUid();

                        final  String postkey = getRef(position).getKey();

                        holder.setItemFavourite(getApplication(), model.getName(), model.getDescription(), model.getPrice(),
                                model.getImage(), model.getCategory(), model.getPid(),model.getUserid(), model.getDate(), model.getTime(), model.getNameLower());

                        final String name = getItem(position).getName();
                        final String userid = getItem(position).getUserid();
                    }

                    @NonNull
                    @Override
                    public com.blessapp.blessapp.Product_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_item, parent, false);
                        return new com.blessapp.blessapp.Product_Viewholder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();

        favrecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

}
