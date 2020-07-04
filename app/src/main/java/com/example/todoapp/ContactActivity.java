package com.example.todoapp;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.todoapp.model.Contact;
import com.example.todoapp.model.Todo;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity {
    //define all views
    private Button btnview, btnsave;
    TextView txtname, txtphone;
    static final int PICK_CONTACT = 1;
    final private int REQUEST_MULTIPLE_PERMISSIONS = 1;

    List<Contact> contacts = new ArrayList<Contact>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);

        AccessContact();
        btnview = (Button) findViewById(R.id.btnload);
        btnsave = (Button) findViewById(R.id.save);
        txtname = (TextView) findViewById(R.id.txtname);
        txtphone = (TextView) findViewById(R.id.txtphone);

        final Todo todo = (Todo) getIntent().getSerializableExtra("Todo");

        //for a button click event ,we call Pick_contact intent
        btnview.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });

        btnsave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                todo.setContacts(contacts);
                //startActivityForResult(intent, PICK_CONTACT);
                finish();
            }
        });


    }

    //we pick a contact an place it in a textview
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
            case (PICK_CONTACT):
                if (resultCode == Activity.RESULT_OK) {
                    Contact contact = new Contact();
                    Uri contactData = data.getData();
                    Cursor c = managedQuery(contactData, null, null, null, null);
                    if (c.moveToFirst()) {
                        String id = c.getString(c.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
                        contact.setId(id);
                        String hasPhone = c.getString(c.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
                        try {
                            if (hasPhone.equalsIgnoreCase("1")) {
                                Cursor phones = getContentResolver().query(
                                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
                                        null, null
                                );
                                phones.moveToFirst();
                                String cNumber = phones.getString(phones.getColumnIndex("data1"));
                                System.out.println("number is:" + cNumber);
                                txtphone.setText("Phone Number is:" + cNumber);
                                contact.setTel(cNumber);
                            }
                            String name = c.getString(c.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                            txtname.setText("Name is :" + name);
                            contact.setName(name);
                            contacts.add(contact);
                        } catch (Exception ex) {
                            ex.getMessage();
                        }
                    }
                }
                break;
        }
    }


    //calling the accesscontact function
    private void AccessContact() {
        List<String> permissionsNeeded = new ArrayList<String>();
        final List<String> permissionsList = new ArrayList<String>();
        if (!addPermission(permissionsList, Manifest.permission.READ_CONTACTS))
            permissionsNeeded.add("Read Contacts");
        if (!addPermission(permissionsList, Manifest.permission.WRITE_CONTACTS))
            permissionsNeeded.add("Write Contacts");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {

                String message = "You need to grant access to" + permissionsNeeded.get(0);

                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + "," + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {


                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_MULTIPLE_PERMISSIONS);
                            }
                        }
                );
                return;
            }

            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]), REQUEST_MULTIPLE_PERMISSIONS);
            return;
        }


    }
    //REQUESTING READ AND WRITE CONTACT ADD ALL PERMISSION IN A LIST

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);

            if (!shouldShowRequestPermissionRationale(permission))
                return false;

        }
        return true;
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ContactActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();

    }


}



