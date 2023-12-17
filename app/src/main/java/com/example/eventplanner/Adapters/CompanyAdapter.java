package com.example.eventplanner.Adapters;


import android.app.Dialog;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.eventplanner.Objects.Company;
import com.example.eventplanner.Objects.User;
import com.example.eventplanner.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.ArrayList;
public class CompanyAdapter extends RecyclerView.Adapter<CompanyAdapter.ViewHolder> {
    ArrayList<Company>localDataset;
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView name,phoneNumber,email;
        CompanyAdapter adapter;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.userName);
            phoneNumber=itemView.findViewById(R.id.userPhoneNumber);
            email=itemView.findViewById(R.id.userEmail);
            itemView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showConfirmationDialog();
                }
            });
        }

        private void showConfirmationDialog() {
            final Dialog dialog = new Dialog(itemView.getContext());
            dialog.setContentView(R.layout.confirmation_dialog);

            TextView messageTextView = dialog.findViewById(R.id.messageTextView);
            Button yesButton = dialog.findViewById(R.id.yesButton);
            Button noButton = dialog.findViewById(R.id.noButton);

            messageTextView.setText("Are you sure you want to remove this User?");

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    Log.d("ID", "onComplete: "+adapter.localDataset.get(position).getCompanyId());
                    if (position != RecyclerView.NO_POSITION) {
                        FirebaseFirestore db=FirebaseFirestore.getInstance();
                        db.collection("Company").document(adapter.localDataset.get(position).getCompanyId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(itemView.getContext(), "User Data Has Been Deleted Successfully!", Toast.LENGTH_SHORT).show();
                            }
                        });adapter.localDataset.remove(position);
                        adapter.notifyItemRemoved(position);
                    }
                    dialog.dismiss();
                }
            });

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

        public TextView getName() {
            return name;
        }

        public TextView getPhoneNumber() {
            return phoneNumber;
        }

        public TextView getEmail() {
            return email;
        }
        public ViewHolder linkAdapter(CompanyAdapter adapter) {
            this.adapter = adapter;
            return this;
        }
    }

    public CompanyAdapter(ArrayList<Company> localDataset) {
        this.localDataset = localDataset;
    }

    @NonNull
    @Override
    public CompanyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_card_model, parent, false);
    return new ViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyAdapter.ViewHolder holder, int position) {
        holder.getName().setText(localDataset.get(position).getCompanyName());
        holder.getEmail().setText(localDataset.get(position).getEmail());
        holder.getPhoneNumber().setText(localDataset.get(position).getPhoneNumberOne());
    }

    @Override
    public int getItemCount() {
        return localDataset.size();
    }
}

