package com.blessapp.blessapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blessapp.blessapp.Interface.ItemClickListener;
import com.blessapp.blessapp.Model.Product;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import static java.security.AccessController.getContext;

public class ProductPageAdapter extends FirebaseRecyclerAdapter<Product, ProductPageAdapter.productViewHolder> {
  //Context mcon;
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ProductPageAdapter(@NonNull FirebaseRecyclerOptions<Product> options) {


        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductPageAdapter.productViewHolder holder, int position, @NonNull final Product model) {

        holder.itemName.setText(model.getName());
        Picasso.get().load(model.getImage()).into(holder.itemImg);
        holder.itemPrice.setText("RM " + model.getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // mcon.startActivity(new Intent(mcon, ProductDetailsActivity.class));

                Intent intent = new Intent(view.getContext(), ProductDetailsActivity.class);
                intent.putExtra("pid", model.getPid());
                view.getContext().startActivity(intent);
            }
        });

    }

    @NonNull
    @Override
    public ProductPageAdapter.productViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view
                = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_item, parent, false);
        return new ProductPageAdapter.productViewHolder(view);
    }

   public class productViewHolder extends RecyclerView.ViewHolder{

        ImageView itemImg, favBtn, favImg, favDeleteBtn;
        TextView itemName, itemPrice, favName, favPrice;
        DatabaseReference favouriteRef;
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        public ItemClickListener listener;

        public productViewHolder(@NonNull View itemView) {
            super(itemView);

            itemName = itemView.findViewById(R.id.productitem_name);
            itemPrice = itemView.findViewById(R.id.productitem_price);
            itemImg = itemView.findViewById(R.id.productitem_image);
            favBtn = itemView.findViewById(R.id.favourite_btn);
        }
    }
}
