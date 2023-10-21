package com.example.automatedloanapprovalapp.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.LoanType;

import java.util.List;

public class LoanTypeAdapter extends RecyclerView.Adapter<LoanTypeAdapter.LoanTypeViewHolder> {
    private List<LoanType> loanTypes;
    private OnEditClickListener editClickListener; // Interface instance for handling edit clicks
    private OnDeleteClickListener deleteClickListener; // Interface instance for handling delete clicks



    public LoanTypeAdapter(List<LoanType> loanTypes,OnEditClickListener editClickListener,OnDeleteClickListener deleteClickListener) {
        this.loanTypes = loanTypes;
        this.editClickListener = editClickListener;
        this.deleteClickListener = deleteClickListener;
    }



    @NonNull
    @Override
    public LoanTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_loan_type, parent, false);
        return new LoanTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanTypeViewHolder holder, int position) {
        LoanType loanType = loanTypes.get(position);
        holder.bind(loanType);
    }

    @Override
    public int getItemCount() {
        return loanTypes.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setLoanTypes(List<LoanType> loanTypes) {
        this.loanTypes = loanTypes;
        notifyDataSetChanged();
    }

    public  class LoanTypeViewHolder extends RecyclerView.ViewHolder {
        private final TextView typeTextView;
        private final TextView durationTextView;
        private final TextView interestRateTextView;
        private final TextView statusTextView;
        private final TextView loan_edit_txt;
        private final TextView loan_delete_txt;

        public LoanTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            typeTextView = itemView.findViewById(R.id.txtLoanType);
            durationTextView = itemView.findViewById(R.id.txtDuration);
            interestRateTextView = itemView.findViewById(R.id.txtInterest);
            statusTextView = itemView.findViewById(R.id.txtStatus);
            loan_delete_txt = itemView.findViewById(R.id.delete_loan_type);
            loan_edit_txt = itemView.findViewById(R.id.loan_edit);
        }

        public void bind(LoanType loanType) {
            typeTextView.setText(loanType.getType());
            durationTextView.setText(String.valueOf(loanType.getDuration()));
            interestRateTextView.setText(String.valueOf(loanType.getInterestRate()));
            statusTextView.setText(loanType.getStatus());

            loan_edit_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // When edit button is clicked, invoke the editClickListener interface method

                        editClickListener.onEditClick(loanType);

                }
            });

            loan_delete_txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (deleteClickListener != null) {
                        deleteClickListener.onDeleteClick(loanType);
                    }
                }
            });

        }
    }
    public interface OnDeleteClickListener {
        void onDeleteClick(LoanType loanType);
    }

    public interface OnEditClickListener {
        void onEditClick(LoanType loanType);
    }

}
