package com.example.contact_jaspreetkaur_c0812273_android.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.contact_jaspreetkaur_c0812273_android.data.ContactRepository;

import java.util.List;

public class ContactViewModel extends AndroidViewModel {
    private ContactRepository repository;
    private final LiveData<List<Contact>> allContacts;

    public ContactViewModel(@NonNull Application application) {
        super(application);

        repository = new ContactRepository(application);
        allContacts = repository.getAllContacts();
    }

    public LiveData<List<Contact>> getAllContacts() {return allContacts;}

    public LiveData<Contact> getContact(int id) {return repository.getContact(id);}

    public void insert(Contact contact) {repository.insert(contact);}

    public void update(Contact contact) {repository.update(contact);}

    public void delete(Contact contact) {repository.delete(contact);}
}
