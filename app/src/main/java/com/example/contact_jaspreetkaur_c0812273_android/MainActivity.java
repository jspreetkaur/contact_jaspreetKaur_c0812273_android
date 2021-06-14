package com.example.contact_jaspreetkaur_c0812273_android;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.contact_jaspreetkaur_c0812273_android.adapter.RecyclerViewAdapter;
import com.example.contact_jaspreetkaur_c0812273_android.model.Contact;
import com.example.contact_jaspreetkaur_c0812273_android.model.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnContactClickListener,RecyclerViewAdapter.OnContactLongClickListener{

    public static final String CONTACT_ID = "contactId";
    private static final int REQUEST_PHONE_CALL = 1;

    // declaration of employeeViewModel
    private ContactViewModel contactViewModel;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private Contact delContact;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // instantiating the employeeViewModel
        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication())
                .create(ContactViewModel.class);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        contactViewModel.getAllContacts().observe(this, contacts -> {
            // set adapter
            recyclerViewAdapter = new RecyclerViewAdapter(contacts, this, this,this);
            recyclerView.setAdapter(recyclerViewAdapter);
        });

        FloatingActionButton addContact = findViewById(R.id.addContactBtn);
        addContact.setOnClickListener(v -> {

          //  Toast.makeText(this, "Contact successfully updated", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
            /*startActivityForResult(intent, ADD_EMPLOYEE_REQUEST_CODE);*/
            // the following approach as startActivityForResult is deprecated
          //  startActivity(intent);
            launcher.launch(intent);

        });

        // attach the itemTouchHelper to my recyclerView
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Contact contact = contactViewModel.getAllContacts().getValue().get(position);
            switch (direction) {
                case ItemTouchHelper.LEFT:
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Do you want delete?");
                    builder.setPositiveButton("Yes", (dialog, which) -> {
                        delContact = contact;
                        contactViewModel.delete(contact);
                        Snackbar.make(recyclerView, delContact.getFirstName() + " is deleted!", Snackbar.LENGTH_LONG)
                                .setAction("Undo", v -> contactViewModel.insert(delContact)).show();
                    });
                    builder.setNegativeButton("No", (dialog, which) -> recyclerViewAdapter.notifyDataSetChanged());
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                    break;
                case ItemTouchHelper.RIGHT:
                    Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
                    intent.putExtra(CONTACT_ID, contact.getId());
                    startActivity(intent);
                    break;
            }
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {


            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        }
    };

    /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EMPLOYEE_REQUEST_CODE && resultCode == RESULT_OK) {
            Log.d(TAG, "onActivityResult: " + data.getStringExtra(AddEmployeeActivity.NAME_REPLY));

            String name = data.getStringExtra(AddEmployeeActivity.NAME_REPLY);
            String salary = data.getStringExtra(AddEmployeeActivity.SALARY_REPLY);
            String department = data.getStringExtra(AddEmployeeActivity.DEPARTMENT_REPLY);
            // getting the current date
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
            String joiningDate = sdf.format(cal.getTime());

            Employee employee = new Employee(name, department, joiningDate, Double.parseDouble(salary));
            employeeViewModel.insert(employee);

        }
    }
*/
    ActivityResultLauncher<Intent> launcher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    String firstName = data.getStringExtra(AddContactActivity.FIRST_NAME);
                    String lastName = data.getStringExtra(AddContactActivity.LAST_NAME);
                    String address = data.getStringExtra(AddContactActivity.ADDRESS);
                    String email = data.getStringExtra(AddContactActivity.EMAIL);
                    String phoneNumber = data.getStringExtra(AddContactActivity.PHONE_NUMBER);

                    Contact contact = new Contact(firstName, lastName, email, phoneNumber,address);
                    contactViewModel.insert(contact);
                }
            });


    @Override
    public void onContactClick(int position) {

        Contact contact = contactViewModel.getAllContacts().getValue().get(position);
        Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
        intent.putExtra(CONTACT_ID, contact.getId());
        startActivity(intent);
    }

    @Override
    public void onContactLongClick(int position)
    {

        //making a phone call
        Intent intent = new Intent(Intent.ACTION_CALL);
        Contact contact = contactViewModel.getAllContacts().getValue().get(position);
       String  mobilePhone = contact.getPhoneNumber();

        intent.setData(Uri.parse("tel:" + mobilePhone));
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CALL_PHONE},REQUEST_PHONE_CALL);
        }
        else
        {
            startActivity(intent);
        }
    }


}
