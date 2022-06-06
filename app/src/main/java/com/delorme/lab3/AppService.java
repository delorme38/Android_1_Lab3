package com.delorme.lab3;

import java.util.ArrayList;

public class AppService {

    private ArrayList<Contact> contacts = new ArrayList<>();
    private rvAdapter rvAdapter;

    private static AppService instance = null;

    public static AppService getInstance(){
        if (instance == null) {
            instance = new AppService();
        }
        return instance;
    }

    private AppService() { initAppService();}


    public ArrayList<Contact> getContacts(){
        return contacts;
    }

    public void addContact(Contact contact) {
        this.rvAdapter.addContact(contact);
    }

    public rvAdapter getRvAdapter() {
        return rvAdapter;
    }

    public AppService setRvAdapter(rvAdapter rvAdapter) {
        this.rvAdapter = rvAdapter;
        return this;
    }

    private void initAppService() {

    }
}
