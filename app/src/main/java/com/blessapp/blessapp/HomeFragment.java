package com.blessapp.blessapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blessapp.blessapp.Model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class HomeFragment extends Fragment {


    RecyclerView homerecycler;
    FirebaseDatabase db = FirebaseDatabase.getInstance();
    DatabaseReference dbreference, favRef, favListRef;
    Boolean fvrtChecker = false;

    Product productItem;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.home_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String currentUserid = user.getUid();

        homerecycler = getActivity().findViewById(R.id.HomeRecycler);
        homerecycler.setHasFixedSize(true);
        homerecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        dbreference = db.getReference("AllProducts");
        productItem = new Product();
        favRef = db.getReference("favourites");
        favListRef = db.getReference("favouriteList").child(currentUserid);


        FirebaseRecyclerOptions<Product> options =
                new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(dbreference, Product.class)
                .build();


        FirebaseRecyclerAdapter<Product, com.blessapp.blessapp.Product_Viewholder> firebaseRecyclerAdapter =
                new FirebaseRecyclerAdapter<Product, com.blessapp.blessapp.Product_Viewholder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull com.blessapp.blessapp.Product_Viewholder holder, int position, @NonNull final Product product) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        final String currentUserid = user.getUid();

                        final  String postkey = getRef(position).getKey();

                        holder.setitem(getActivity(),product.getName(),product.getDescription(), product.getPrice(), product.getImage(),
                                product.getCategory(), product.getPid(), product.getUserid(),product.getDate(),product.getTime(), product.getNameLower());

                        final String productName = getItem(position).getName();
                        final String productDescription = getItem(position).getDescription();
                        final String url = getItem(position).getImage();
                        final String productPrice = getItem(position).getPrice();
                        final String productTime = getItem(position).getTime();
                        final String productDate = getItem(position).getDate();
                        final String productid = getItem(position).getPid();


                        holder.favouriteChecker(postkey);
                        holder.favBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                fvrtChecker = true;

                                favRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(fvrtChecker.equals(true)){
                                            if(snapshot.child(postkey).hasChild(currentUserid)){
                                                favRef.child(postkey).child(currentUserid).removeValue();
                                                delete(productTime);
                                                Toast.makeText(getActivity(),"Removed From Favourite", Toast.LENGTH_SHORT).show();
                                                fvrtChecker = false;
                                            }else{
                                                favRef.child(postkey).child(currentUserid).setValue(true);
                                                productItem.setName(productName);
                                                productItem.setDescription(productDescription);
                                                productItem.setPid(productid);
                                                productItem.setPrice(productPrice);
                                                productItem.setImage(url);
                                                productItem.setTime(productTime);
                                                productItem.setDate(productDate);


                                                favListRef.child(postkey).setValue(productItem);
                                                fvrtChecker = false;

                                                Toast.makeText(getActivity(), "Added to favourite", Toast.LENGTH_SHORT).show();

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        });





                    }

                    @NonNull
                    @Override
                    public com.blessapp.blessapp.Product_Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item, parent, false);
                        return new com.blessapp.blessapp.Product_Viewholder(view);
                    }
                };

        firebaseRecyclerAdapter.startListening();
        homerecycler.setAdapter(firebaseRecyclerAdapter);

    }

    void delete(String time){
        Query query = favListRef.orderByChild("time").equalTo(time);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot1 : snapshot.getChildren()){
                    dataSnapshot1.getRef().removeValue();

                    Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
