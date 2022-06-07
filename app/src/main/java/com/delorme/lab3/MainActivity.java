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
import android.util.Log;
import android.view.Menu;

import com.delorme.lab3.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding _binding;
    private AppService appService;
    private rvAdapter rvAdapter;

    private String SAVEFILENAME = "contacts.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appService = AppService.getInstance();


        //sava data
        String json = null;
        List<Contact> arrayList = new ArrayList<>();
        try{
            json = readFile();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if( json != null){
            Gson gson = new Gson();
            Type type = new TypeToken<List<Contact>>() {}.getType();
            arrayList = gson.fromJson(json, type);
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

        _binding.rvContact.setAdapter(rvAdapter);
        _binding.rvContact.setLayoutManager(new LinearLayoutManager(this));


        //Floating action button
        FloatingActionButton fab = _binding.fab;
        fab.setOnClickListener(view -> {
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

        Gson gson = new Gson();
        List<Contact> arrayList = rvAdapter.getLocalDataSet();
        String json = gson.toJson(arrayList);
        try (FileOutputStream fos = this.openFileOutput(SAVEFILENAME, Context.MODE_PRIVATE)) {
            fos.write(json.getBytes(StandardCharsets.UTF_8));
        }catch(IOException e){
            Log.e(TAG, e.toString() );
        }
    }

    public String readFile() throws FileNotFoundException {
        FileInputStream fis = this.openFileInput(SAVEFILENAME);
        InputStreamReader inputStreamReader = new InputStreamReader(fis, StandardCharsets.UTF_8);
        StringBuilder stringBuilder = new StringBuilder();
        String contents;
        try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
            String line = reader.readLine();
            while (line != null) {
                stringBuilder.append(line).append('\n');
                line = reader.readLine();
            }
        } catch (IOException e) {
            // Error occurred when opening raw file for reading.
        } finally {
            contents = stringBuilder.toString();
        }
        return contents;
    }
}