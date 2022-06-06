package com.delorme.lab3;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.delorme.lab3.databinding.ActivityAddContactBinding;

public class AddContactActivity extends AppCompatActivity {
    private ActivityAddContactBinding _binding;
    private AppService appService;
    private Contact contact;
    private boolean update = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_add_contact);
        appService = AppService.getInstance();

        Contact lookIfUpdate = getIntent().getParcelableExtra("Contact");
        if (lookIfUpdate != null) {
            contact = lookIfUpdate;
            update = true;
        } else {
            contact = new Contact();
        }
        _binding.setContact(contact);
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
                }else {
                    addContact();
                }
                return true;
            case R.id.idMenuClear:
                deleteEntries();
                return true;
            case R.id.idMenuDelete:
                deleteStudent();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void addContact() {
        if (validate()){
            appService.getRvAdapter().addContact(contact);
        } else {
            Log.d(TAG, "addContact: erreur validation");
        }
    }

    private void updateContact() {
        int pos = getIntent().getIntExtra("POS", -1);
        if (pos != -1 && update && validate())  {
            appService.getRvAdapter().updateContact(contact, pos);
            update = false;
            invalidateOptionsMenu();
            backToHome();
        }
    }

    private void backToHome() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}