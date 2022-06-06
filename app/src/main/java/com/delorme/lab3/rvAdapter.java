
package com.delorme.lab3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class rvAdapter extends RecyclerView.Adapter<rvAdapter.ViewHolder> implements Filterable {

    private List<Contact> localDataSet;
    private MainActivity mainActivity;


    public rvAdapter(Context context) {
        this.mainActivity = (MainActivity) context;
        this.localDataSet = AppService.getInstance().getContacts();
    }

    public List<Contact> getLocalDataSet() {
        return localDataSet;
    }

    public rvAdapter setLocalDataSet(List<Contact> localDataSet) {
        this.localDataSet = localDataSet;
        notifyDataSetChanged();
        return this;
    }

    public void updateContact(Contact contact, int pos) {
        localDataSet.set(pos, contact);
        notifyDataSetChanged();
    }

    public void deleteContact(Contact contact){
        int index = localDataSet.indexOf(contact);
        localDataSet.remove(index);
        notifyItemRemoved(index);
    }

    public void deleteContact(int pos) {
        localDataSet.remove(pos);
        notifyItemRemoved(pos);
    }

    public void addContact(Contact student){
        localDataSet.add(0, student);
        notifyItemInserted(0);
    }

    @Override
    public Filter getFilter() {
        return null;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView textName;
        public TextView textPhoneNumber;
        private MainActivity mainActivity;

        public ViewHolder(View view, MainActivity mainActivity) {
            super(view);
            // Define click listener for the ViewHolder's View
            itemView.setOnClickListener(this);
            textName = view.findViewById(R.id.idRecyclerName);
            textPhoneNumber = view.findViewById(R.id.idRecyclerPhone);
            this.mainActivity = mainActivity;
        }

        @Override
        public void onClick(View view) {
            int pos = getAdapterPosition();
            Contact contact = localDataSet.get(pos);
            mainActivity.loadContact(pos, contact);
        }
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_template, viewGroup, false);
        return new ViewHolder(view, mainActivity);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Contact contact = localDataSet.get(position);
        holder.textName.setText(contact.getFirstName()+", "+contact.getLastName());
        holder.textPhoneNumber.setText(contact.getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }
}
