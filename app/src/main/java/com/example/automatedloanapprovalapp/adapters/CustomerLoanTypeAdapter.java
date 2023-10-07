package com.example.automatedloanapprovalapp.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.automatedloanapprovalapp.R;
import com.example.automatedloanapprovalapp.classes.LoanType;
import com.example.automatedloanapprovalapp.fragments.ApplicationFormDialogFragment;

import java.util.List;

public class CustomerLoanTypeAdapter extends RecyclerView.Adapter<CustomerLoanTypeAdapter.LoanTypeViewHolder> {
    private List<LoanType> loanTypes;
    private FragmentManager fragmentManager;

    public CustomerLoanTypeAdapter(List<LoanType> loanTypes,FragmentManager fragmentManager) {
        this.loanTypes = loanTypes;
        this.fragmentManager = fragmentManager;

    }

    @NonNull
    @Override
    public LoanTypeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_item_loan_type, parent, false);
        return new LoanTypeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LoanTypeViewHolder holder, int position) {
        LoanType selectedLoanType = loanTypes.get(position);
        holder.bind(selectedLoanType);
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

    public class LoanTypeViewHolder extends RecyclerView.ViewHolder {
        private final TextView typeTextView;
        private final TextView durationTextView;
        private final TextView interestRateTextView;
        private final TextView applyNowTextView;

        public LoanTypeViewHolder(@NonNull View itemView) {
            super(itemView);
            typeTextView = itemView.findViewById(R.id.txtLoanType);
            durationTextView = itemView.findViewById(R.id.txtDuration);
            interestRateTextView = itemView.findViewById(R.id.txtInterest);
            applyNowTextView = itemView.findViewById(R.id.apply_now);
        }

        public void bind(LoanType loanType) {
            typeTextView.setText(loanType.getType());
            durationTextView.setText(String.valueOf(loanType.getDuration()));
            interestRateTextView.setText(String.valueOf(loanType.getInterestRate()));

            applyNowTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        ApplicationFormDialogFragment dialogFragment = new ApplicationFormDialogFragment(loanType);
                        dialogFragment.show(fragmentManager, "ApplicationFormDialogFragment");
                    } catch (Exception e) {
                        Toast.makeText(view.getContext(), "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
