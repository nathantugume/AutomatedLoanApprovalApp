package com.example.automatedloanapprovalapp.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.FirestoreCRUD;
import com.example.automatedloanapprovalapp.classes.LoanType;
import com.example.automatedloanapprovalapp.classes.Transaction;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Collections;
import java.util.List;

public class TransactionViewHolder extends RecyclerView.ViewHolder {
    private final TextView dateTextView;
    private final TextView loanTypeTextView;
    private final TextView paybackAmountTextView;
    private final TextView requestedAmountTextView;
    private final TextView statusTextView;
    private final TextView userNameTextView;
    private final ImageView acceptImage;
    private final ImageView declineImage;
    private List<Transaction> transactions;
    FirestoreCRUD firestoreCRUD = new FirestoreCRUD();
    private TransactionAdapter.OnTransactionClickListener listener;

    public TransactionViewHolder(@NonNull View itemView, List<Transaction> transactions, TransactionAdapter.OnTransactionClickListener listener) {
        super(itemView);
        dateTextView = itemView.findViewById(R.id.dateTextView);
        loanTypeTextView = itemView.findViewById(R.id.loanTypeTextView);
        paybackAmountTextView = itemView.findViewById(R.id.paybackAmountTextView);
        requestedAmountTextView = itemView.findViewById(R.id.requestedAmountTextView);
        statusTextView = itemView.findViewById(R.id.statusTextView);
        userNameTextView = itemView.findViewById(R.id.customerNameTextView);
        acceptImage = itemView.findViewById(R.id.approve);
        declineImage = itemView.findViewById(R.id.decline);
        this.transactions = transactions;
        this.listener = listener;


    }

    public void bind(Transaction transaction) {
        dateTextView.setText(transaction.getDateRequested());
        loanTypeTextView.setText(transaction.getLoanType());
        paybackAmountTextView.setText(String.valueOf(transaction.getPaybackAmount()));
        requestedAmountTextView.setText(String.valueOf(transaction.getRequestedAmount()));
        statusTextView.setText(transaction.getStatus());
        userNameTextView.setText(transaction.getUserName());

        acceptImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String transactionId = transactions.get(getAdapterPosition()).getId();
                if (listener != null) {
                    listener.onTransactionClick(transactionId);
                }
            }
        });

    }
}

