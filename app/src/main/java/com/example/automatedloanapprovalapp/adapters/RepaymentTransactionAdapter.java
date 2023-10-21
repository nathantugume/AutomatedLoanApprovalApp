package com.example.automatedloanapprovalapp.adapters;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.RepaymentTransaction;

import java.util.List;

public class RepaymentTransactionAdapter extends RecyclerView.Adapter<RepaymentTransactionAdapter.ViewHolder> {
    private List<RepaymentTransaction> transactions;

    public RepaymentTransactionAdapter(List<RepaymentTransaction> transactions) {
        this.transactions = transactions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_monitor_repayment, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RepaymentTransaction transaction = transactions.get(position);
        holder.customerNameTextView.setText(transaction.getCustomerName());
        holder.paidPercentageTextView.setText(transaction.getPaidPercentage() + "% paid");
        holder.progressBar.setProgress(transaction.getPaidPercentage());
        holder.loanAmountTextView.setText("Ugx " + transaction.getLoanAmount());
        holder.paidAmountTextView.setText("Ugx " + transaction.getPaidAmount());
        holder.statusTextView.setText(transaction.getStatus());

    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateList(List<RepaymentTransaction> filteredTransactions) {
        transactions = filteredTransactions;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView customerNameTextView;
        public TextView paidPercentageTextView;
        public ProgressBar progressBar;
        public TextView loanAmountTextView;
        public TextView paidAmountTextView;
        public TextView statusTextView;

        public ViewHolder(View view) {
            super(view);
            customerNameTextView = view.findViewById(R.id.customerNameTxt);
            paidPercentageTextView = view.findViewById(R.id.paid_percentage);
            progressBar = view.findViewById(R.id.progressBar);
            loanAmountTextView = view.findViewById(R.id.loan_amount);
            paidAmountTextView = view.findViewById(R.id.paid_amount);
            statusTextView = view.findViewById(R.id.status);
        }
    }
}

