package com.collections2.grigelionyte.greta.collections.adapters;

import android.widget.Filter;

import com.collections2.grigelionyte.greta.collections.model.ItemsCollection;

import java.util.ArrayList;


public class MyCustomFilter extends Filter {
    private CardAdapter adapter;
    private ArrayList<ItemsCollection> collectionsListOriginal;

    public MyCustomFilter(ArrayList<ItemsCollection> collections, CardAdapter adapter){
        this.adapter = adapter;
        this.collectionsListOriginal = collections;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
       FilterResults results = new FilterResults();
        if (charSequence != null && charSequence.length() > 0){
            charSequence = charSequence.toString().toUpperCase();

            ArrayList<ItemsCollection> filteredCollections = new ArrayList<>();
            for (int i=0;i<collectionsListOriginal.size();i++)
            {

                if(collectionsListOriginal.get(i).getColTitle().toUpperCase().contains(charSequence))
                {

                    filteredCollections.add(collectionsListOriginal.get(i));
                }
            }
            results.count=filteredCollections.size();
            results.values=filteredCollections;
        }else
        {
            results.count=collectionsListOriginal.size();
            results.values=collectionsListOriginal;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults results) {
        adapter.cardData = (ArrayList<ItemsCollection>) results.values;
        adapter.notifyDataSetChanged();
    }
}
