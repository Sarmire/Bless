package com.blessapp.blessapp.ViewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blessapp.blessapp.R;

public class UnReceiptViewHolder extends RecyclerView.ViewHolder {
    public TextView unReceiptId, unStatus, unTotalAmt;
    public Button unReceiptDetails, deleteOrder;


    public UnReceiptViewHolder(@NonNull View itemView) {
        super(itemView);

        unReceiptId = itemView.findViewById(R.id.un_receipt_id);
        unTotalAmt = itemView.findViewById(R.id.unTotalAmt);
        unStatus = itemView.findViewById(R.id.un_status);
        unReceiptDetails = itemView.findViewById(R.id.un_receipt_details);
        deleteOrder = itemView.findViewById(R.id.delete_order);
    }
}
