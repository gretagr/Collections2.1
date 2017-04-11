package com.collections2.grigelionyte.greta.collections.ui.addEdit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.collections2.grigelionyte.greta.collections.R;
import com.collections2.grigelionyte.greta.collections.ui.main.CollectionsActivity;

import java.util.ArrayList;

public class NewCollection extends AppCompatActivity {
    Button cancel;
    EditText addCategoryField;
    Button addCategoyBtn;
    ListView categoriesListview;
    ArrayList<String> categoriesList;
    ArrayAdapter<String> categoriesadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_collection);
        addCategoryField = (EditText)findViewById(R.id.editText);
        addCategoyBtn = (Button)findViewById(R.id.button_addData);
        categoriesListview = (ListView)findViewById(R.id.listView_lv);


        categoriesList = new ArrayList<String>();
        categoriesadapter = new ArrayAdapter<String>(NewCollection.this, android.R.layout.simple_list_item_1, categoriesList);
        categoriesListview.setAdapter(categoriesadapter);


        cancel = (Button)findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancel = new Intent(NewCollection.this, CollectionsActivity.class);
                startActivity(cancel);
            }
        });

        addCat();
        deleteCat();
    }

    public void addCat(){
        addCategoyBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String result = addCategoryField.getText().toString();
                categoriesList.add(result);
                categoriesadapter.notifyDataSetChanged();
                addCategoryField.setText("");

            }

        });
    }
    private void deleteCat() {
        categoriesListview.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> n_adapter, View item, int pos, long id) {
                        categoriesList.remove(pos);
                        categoriesadapter.notifyDataSetChanged();
                        return true;
                    }

                });
    }
}
