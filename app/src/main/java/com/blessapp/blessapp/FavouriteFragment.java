package com.blessapp.blessapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blessapp.blessapp.Model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FavouriteFragment extends Fragment {

    RecyclerView favrecyclerView;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference ;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.favourite_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserid = user.getUid();

        favrecyclerView = getActivity().findViewById(R.id.favouriteRecycler);
        favrecyclerView.setHasFixedSize(true);
        favrecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        reference = database.getReference("favoriteList").child(currentUserid);

        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(reference, Product.class)
                .build();

        FirebaseRecyclerAdapter<Product, com.blessapp.blessapp.Product_Viewholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Product, com.blessapp.blessapp.Product_Viewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull com.blessapp.blessapp.Product_Viewholder holder, int position, @NonNull Product model) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        final String currentUserid = user.getUid();

                        final  String postkey = getRef(position).getKey();

                        holder.setItemFavourite(getApplication(), model.getName(), model.getDescription(), model.getPrice(),
                                model.getImage(), model.getCategory(), model.getPid(), model.getDate(), model.getTime(), model.getNameLower());

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
