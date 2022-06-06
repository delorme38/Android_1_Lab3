package com.delorme.lab3;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.delorme.lab3.databinding.ActivityAddContactBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddContactActivity extends AppCompatActivity {

    final private String CONTACT = "contact";
    final private String FIELDSCOLOR = "fieldsColor";

    private ActivityAddContactBinding _binding;
    private AppService appService;
    private Contact contact;
    private boolean update = false;

    FieldsColor fieldsColor = new FieldsColor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_add_contact);
        appService = AppService.getInstance();

        Contact lookIfUpdate = getIntent().getParcelableExtra("Contact");
        if (lookIfUpdate != null) {
            contact = lookIfUpdate;
            update = true;
        } else if(savedInstanceState != null) {
            contact = savedInstanceState.getParcelable(CONTACT);
            fieldsColor = savedInstanceState.getParcelable(FIELDSCOLOR);

        }else{
            contact = new Contact();
        }
        _binding.setContact(contact);
        _binding.setColor(fieldsColor);

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {

        savedInstanceState.putParcelable(CONTACT, _binding.getContact());
        savedInstanceState.putParcelable(FIELDSCOLOR, fieldsColor);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_contact_menu, menu);
        if (update) {
            menu.getItem(0).setTitle("UPDATE");
        } else {
            menu.getItem(0).setTitle("SAVE");
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.idMenuSave:
                if (update) {
                    updateContact();
                } else {
                    addContact();
                }
                return true;
            case R.id.idMenuClear:
                deleteEntries();
                return true;
            case R.id.idMenuDelete:
                deleteContact();
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    public void deleteEntries() {
        contact = new Contact();
        _binding.setContact(contact);
        update = false;
        invalidateOptionsMenu();
    }

    private void addContact() {
        if (validate()) {
            appService.getRvAdapter().addContact(contact);
            backToHome();

        } else {
            Log.d(TAG, "addContact: erreur validation");
        }
    }

    private void updateContact() {
        int pos = getIntent().getIntExtra("POS", -1);
        if (pos != -1 && update && validate()) {
            appService.getRvAdapter().updateContact(contact, pos);
            update = false;
            invalidateOptionsMenu();
            backToHome();
        }
    }

    private void deleteContact() {
        if (update) {
            int pos = getIntent().getIntExtra("POS", -1);
            if (pos != -1) {
                appService.getRvAdapter().deleteContact(pos);
                invalidateOptionsMenu();
                backToHome();
            }
        }
    }

    private void backToHome() {
        super.onBackPressed();
    }

    private Boolean validate() {

        fieldsColor = new FieldsColor();
        Boolean valid = true;
        String lastName = contact.getLastName();
        String firstName = contact.getFirstName();
        String phoneNumber = contact.getPhoneNumber();
        String email = contact.getEmail();

        if (lastName.isEmpty()){
            valid = false;
            fieldsColor.lastName = R.color.red;
        }
        if (firstName.isEmpty()){
            valid = false;
            fieldsColor.firstName = R.color.red;
        }
        if (phoneNumber.isEmpty() || !phoneNumber.matches("[0-9+() -]{7,10}")){
            valid = false;
            fieldsColor.phone = R.color.red;
        }
        if (email.isEmpty() && !isEmail(email)){
            valid = false;
            fieldsColor.email = R.color.red;
        }
        _binding.setColor(fieldsColor);

        return valid;
    }


    private boolean isEmail(String s) {
        return s.matches("^[-0-9a-zA-Z.+_]+@[-0-9a-zA-Z.+_]+\\.[a-zA-Z]");
    }

}