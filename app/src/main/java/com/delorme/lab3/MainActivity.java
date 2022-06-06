package com.delorme.lab3;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import com.delorme.lab3.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding _binding;
    private AppService appService;
    private rvAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appService = AppService.getInstance();


        if (savedInstanceState != null) {
        }

        //Binding
        _binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        //Adapter
        if(appService.getRvAdapter()==null){
            appService.setRvAdapter(new rvAdapter(this));
        }

        rvAdapter = appService.getRvAdapter();
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
}