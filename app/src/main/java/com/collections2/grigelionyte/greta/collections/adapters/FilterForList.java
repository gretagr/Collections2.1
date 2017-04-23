package com.collections2.grigelionyte.greta.collections.adapters;

import android.widget.Filter;

import com.collections2.grigelionyte.greta.collections.model.Item;

import java.util.ArrayList;


public class FilterForList extends Filter {
    private ListItemAdapter adapter;
    private ArrayList<Item> ItemListOriginal;

    public FilterForList(ArrayList<Item> item, ListItemAdapter adapter){
        this.adapter = adapter;
        this.ItemListOriginal = item;
    }

    @Override
    protected FilterResults performFiltering(CharSequence charSequence) {
        FilterResults results = new FilterResults();
        if (charSequence != null && charSequence.length() > 0){
            charSequence = charSequence.toString().toUpperCase();

            ArrayList<Item> filteredList = new ArrayList<>();
            for (int i=0;i<ItemListOriginal.size();i++)
            {

                if(ItemListOriginal.get(i).getTitle().toUpperCase().contains(charSequence))
                {

                    filteredList.add(ItemListOriginal.get(i));
                }
            }
            results.count=filteredList.size();
            results.values=filteredList;
        }else
        {
            results.count=ItemListOriginal.size();
            results.values=ItemListOriginal;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence charSequence, FilterResults results) {
        adapter.listData = (ArrayList<Item>) results.values;
        adapter.notifyDataSetChanged();
    }
}
