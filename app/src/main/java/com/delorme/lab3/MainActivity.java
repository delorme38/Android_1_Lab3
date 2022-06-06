package com.delorme.lab3;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;

import com.delorme.lab3.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding _binding;
    private AppService appService;
    private rvAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appService = AppService.getInstance();

        //Shared Data
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        Gson gson = new Gson();
        String json = sharedPrefs.getString(TAG, "");
        Type type = new TypeToken<List<Contact>>() {}.getType();
        List<Contact> arrayList = gson.fromJson(json, type);

        if (savedInstanceState != null) {
        }

        //Binding
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //Adapter
        if(appService.getRvAdapter()==null){
            appService.setRvAdapter(new rvAdapter(this));
        }

        rvAdapter = appService.getRvAdapter();
        if (arrayList != null) {
            rvAdapter.setLocalDataSet(arrayList);
        }
        Contact batman = Contact.giveMeBatman();
        _binding.rvContact.setAdapter(rvAdapter);
        _binding.rvContact.setLayoutManager(new LinearLayoutManager(this));


        //Floating action button
        FloatingActionButton fab = _binding.fab;
        fab.setOnClickListener(view -> {
            rvAdapter.addContact(batman);
            addContactPage();
        });
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.main_activity_menu, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                rvAdapter.getFilter().filter(query);
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                rvAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    public void addContactPage(){
        Intent intent = new Intent(this, AddContactActivity.class);
//        intent.putParcelableArrayListExtra("list", contraventions);
        startActivity(intent);
    }
    public void loadContact(int pos, Contact contact) {
        Intent intent = new Intent (this,  AddContactActivity.class);
        intent.putExtra("POS", pos);
        intent.putExtra("Contact", contact);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPrefs.edit();
        Gson gson = new Gson();
        List<Contact> arrayList = rvAdapter.getLocalDataSet();
        String json = gson.toJson(arrayList);
        editor.putString(TAG, json);
        editor.commit();
    }
}