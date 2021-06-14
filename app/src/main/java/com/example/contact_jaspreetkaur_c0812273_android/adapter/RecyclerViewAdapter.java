package com.example.contact_jaspreetkaur_c0812273_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contact_jaspreetkaur_c0812273_android.MainActivity;
import com.example.contact_jaspreetkaur_c0812273_android.R;
import com.example.contact_jaspreetkaur_c0812273_android.model.Contact;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<Contact> contactList;
    private Context context;
    private OnContactClickListener onContactClickListener;
    private OnContactLongClickListener onContactLongClickListener;

    public RecyclerViewAdapter(List<Contact> contactList, Context context, OnContactClickListener onContactClickListener, OnContactLongClickListener onContactLongClickListener) {
        this.contactList = contactList;
        this.context = context;
        this.onContactClickListener = onContactClickListener;
        this.onContactLongClickListener =  onContactLongClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.firstName.setText(contact.getFirstName());
        holder.lastName.setText(contact.getLastName());
        holder.email.setText(contact.getEmail());
        holder.phoneNumber.setText(contact.getPhoneNumber());
        holder.address.setText(contact.getAddress());

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener , View.OnLongClickListener{
        private TextView firstName;
        private TextView lastName;
        private TextView email;
        private TextView phoneNumber;
        private TextView address;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            firstName = itemView.findViewById(R.id.firstname_row);
            lastName =itemView.findViewById(R.id.lastname_row);
            email = itemView.findViewById(R.id.email_row);
            phoneNumber =itemView.findViewById(R.id.phn_number_row);
            address =itemView.findViewById(R.id.address_row);


            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onContactClickListener.onContactClick(getAdapterPosition());
        }

        @Override
        public boolean onLongClick(View v) {
        onContactLongClickListener.onContactLongClick(getAdapterPosition());
        return false;
        }
    }

    public interface OnContactClickListener {
        void onContactClick(int position);
    }
    public interface OnContactLongClickListener {
        void onContactLongClick(int position);
    }

}
