package com.example.automatedloanapprovalapp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.RepaymentItem;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RepaymentAdapter extends RecyclerView.Adapter<RepaymentAdapter.RepaymentViewHolder> {
    private ArrayList<RepaymentItem> repaymentItems;

    public RepaymentAdapter(ArrayList<RepaymentItem> repaymentItems) {
        this.repaymentItems = repaymentItems;
    }

    @NonNull
    @Override
    public RepaymentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_repayment, parent, false);
        return new RepaymentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RepaymentViewHolder holder, int position) {
        RepaymentItem currentItem = repaymentItems.get(position);
        holder.paidAmountTxt.setText(String.valueOf(currentItem.getRepaidAmount()) );
        holder.paybackDate.setText(currentItem.getRepaymentDate());
        holder.balanceTxt.setText(String.valueOf(currentItem.getPaybackAmount()));
        holder.statusTxt.setText(currentItem.getStatus());

    }

    @Override
    public int getItemCount() {
        return repaymentItems.size();
    }

    class RepaymentViewHolder extends RecyclerView.ViewHolder {
        private TextView paidAmountTxt;
        private TextView balanceTxt;
        private TextView paybackDate;
        private TextView statusTxt;

        public RepaymentViewHolder(@NonNull View itemView) {
            super(itemView);
            paidAmountTxt = itemView.findViewById(R.id.repaidAmount);
            balanceTxt = itemView.findViewById(R.id.paybackAmount);
            paybackDate = itemView.findViewById(R.id.paybackDate);
            statusTxt = itemView.findViewById(R.id.repay_status);

        }
    }
}
