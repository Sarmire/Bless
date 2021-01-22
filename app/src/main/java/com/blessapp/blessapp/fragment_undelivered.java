package com.blessapp.blessapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blessapp.blessapp.Model.Orders;
import com.blessapp.blessapp.ViewHolder.UnReceiptViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DecimalFormat;

public class fragment_undelivered extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference deliveredList;
    DecimalFormat df = new DecimalFormat("#.00");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    String currentUserid = user.getUid();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_undelivered, container, false);

        deliveredList= FirebaseDatabase.getInstance().getReference()
                .child("Users")
                .child(currentUserid)
                .child("orderReceipt");

        recyclerView = view.findViewById(R.id.undelivered_list);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Orders> options =
                new FirebaseRecyclerOptions.Builder<Orders>()
                .setQuery(deliveredList.orderByChild("sent").equalTo("Currently preparing..."), Orders.class)
                .build();

        FirebaseRecyclerAdapter<Orders, UnReceiptViewHolder> adapter =
                new FirebaseRecyclerAdapter<Orders, UnReceiptViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull UnReceiptViewHolder holder, int position, @NonNull Orders model) {
                        final String receiptId = getRef(position).getKey();

                        holder.unReceiptId.setText("Receipt ID : " + receiptId);
                        holder.unTotalAmt.setText("Total : RM " + df.format(Float.valueOf(model.getTotalAmount())));
                        holder.unStatus.setText("Status : " + model.getSent());

                        holder.deleteOrder.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RemoveOrder(receiptId);
                            }
                        });

                        holder.unReceiptDetails.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                startActivity(new Intent(getContext(), OrderReceiptDetailsActivity.class).putExtra("Receipt ID", receiptId));
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public UnReceiptViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.undelivered_list_items, parent, false);
                        UnReceiptViewHolder holder = new UnReceiptViewHolder(view);
                        return holder;
                    }
                };
    }

    private void RemoveOrder(final String receiptId) {
        CharSequence options[] = new CharSequence[]{
                "Yes", "No"
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Are you sure to remove this order?");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //If press Yes
                if (which == 0){
                    final DatabaseReference ordersView = FirebaseDatabase.getInstance().getReference().child("Orders").child(receiptId);
                    deliveredList.child(receiptId).removeValue();
                    ordersView.removeValue();
                }
                //else pressed No
                else {
                }
            }
        });
        builder.show();

    }
}
