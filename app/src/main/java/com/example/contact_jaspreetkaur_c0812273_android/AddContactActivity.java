package com.example.contact_jaspreetkaur_c0812273_android;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.contact_jaspreetkaur_c0812273_android.model.Contact;
import com.example.contact_jaspreetkaur_c0812273_android.model.ContactViewModel;

import java.util.Arrays;

public class AddContactActivity extends AppCompatActivity {
    public static final String FIRST_NAME = "firstName";
    public static final String LAST_NAME = "lastname";
    public static final String EMAIL = "email";
    public static final String PHONE_NUMBER = "123456789";
    public static final String ADDRESS = "address";

    private EditText firstName_text,lastName_text,email_text,phnNumber_text,address_text;

    private boolean isEditing = false;
    private int contactId = 0;
    private Contact updateContact;

    private ContactViewModel contactViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_contact_activity);

        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(ContactViewModel.class);
        firstName_text =findViewById(R.id.first_name);
        lastName_text =findViewById(R.id.last_name);
        email_text =findViewById(R.id.email);
        phnNumber_text =findViewById(R.id.phn_number);
        address_text =findViewById(R.id.address);




        Button saveContact = findViewById(R.id.add_btn);

        saveContact.setOnClickListener(v -> {
            saveContact();
        });

        if (getIntent().hasExtra(MainActivity.CONTACT_ID)) {
            contactId = getIntent().getIntExtra(MainActivity.CONTACT_ID, 0);

            contactViewModel.getContact(contactId).observe(this, contact -> {
                if (contact != null) {
                    firstName_text.setText(contact.getFirstName());
                    lastName_text.setText(contact.getLastName());
                    email_text.setText(contact.getEmail());
                    phnNumber_text.setText(contact.getPhoneNumber());
                    address_text.setText(contact.getAddress());

                    updateContact = contact;
                }
            });
            TextView label = findViewById(R.id.label);
            isEditing = true;
            label.setText("Update Contact");
            saveContact.setText("Update");
        }
    }

    private void saveContact() {

        String firstName = firstName_text.getText().toString().trim();
        String lastName = lastName_text.getText().toString().trim();
        String email = email_text.getText().toString().trim();
        String phn_number = phnNumber_text.getText().toString().trim();
        String address = address_text.getText().toString().trim();


        if (firstName.isEmpty()) {
            firstName_text.setError("Enter a valid first name");
            firstName_text.requestFocus();
            return;
        }
        if (lastName.isEmpty()) {
            lastName_text.setError("Enter a valid last name");
            lastName_text.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            email_text.setError("Enter a valid email");
            email_text.requestFocus();
            return;
        }

        if (phn_number.isEmpty()) {
            phnNumber_text.setError("Enter a phone number");
            phnNumber_text.requestFocus();
            return;
        }

        if (address.isEmpty()) {
            address_text.setError("Enter a valid address");
            address_text.requestFocus();
            return;
        }


        if (isEditing) {
            Contact contact = new Contact();
            contact.setId(contactId);
            contact.setFirstName(firstName);
            contact.setLastName(lastName);
            contact.setEmail(email);
            contact.setPhoneNumber(phn_number);
            contact.setAddress(address);

            contactViewModel.update(contact);
            Toast.makeText(this, "Contact successfully updated", Toast.LENGTH_LONG).show();
        } else {
            Intent replyIntent = new Intent();
            replyIntent.putExtra(FIRST_NAME, firstName);
            replyIntent.putExtra(LAST_NAME, lastName);
            replyIntent.putExtra(EMAIL, email);
            replyIntent.putExtra(PHONE_NUMBER, phn_number);
            replyIntent.putExtra(ADDRESS, address);

            setResult(RESULT_OK, replyIntent);

           Toast.makeText(this, "Contact successfully added", Toast.LENGTH_LONG).show();
        }

        finish();
    }

}
