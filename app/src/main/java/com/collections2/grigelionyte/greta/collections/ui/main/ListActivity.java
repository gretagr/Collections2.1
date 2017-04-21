package com.collections2.grigelionyte.greta.collections.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.collections2.grigelionyte.greta.collections.R;
import com.collections2.grigelionyte.greta.collections.adapters.ListItemAdapter;
import com.collections2.grigelionyte.greta.collections.adapters.ListTouchHelper;
import com.collections2.grigelionyte.greta.collections.model.Item;
import com.collections2.grigelionyte.greta.collections.model.MyDBHandler;
import com.collections2.grigelionyte.greta.collections.ui.addEdit.NewItem;
import com.collections2.grigelionyte.greta.collections.ui.addEdit.UpdateItem;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements ListItemAdapter.ItemClickCallback {
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String EXTRA_QUOTE = "EXTRA_QUOTE";
    private static final String EXTRA_ATTR = "EXTRA_ATTR";
    private static final String EXTRA_ARR = "EXTRA_ARR";
    private static final String EXTRA_CAT1 = "EXTRA_CAT1";
    private static final String EXTRA_CAT2 = "EXTRA_CAT2";
    private static final String EXTRA_ID = "EXTRA_ID";

    private RecyclerView recyclerView;
    private ListItemAdapter adapter;
    private ArrayList listData;
    private Toolbar toolbar;
    String getT;
    FloatingActionButton additem;
    MyDBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        db = new MyDBHandler(getApplicationContext());
        getT = getIntent().getStringExtra("name_of_collection");
        int colId = db.getId(getT);

        listData = (ArrayList) db.getAllItemsFromCollection(colId);


        recyclerView = (RecyclerView) findViewById(R.id.rec_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ListItemAdapter(listData, this);
        recyclerView.setAdapter(adapter);
        adapter.setItemClickCallback(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();
        additem = (FloatingActionButton) findViewById(R.id.fb_add_btn);
        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(ListActivity.this, NewItem.class);
                startActivity(in);
            }
        });
        ItemTouchHelper.Callback callback = new ListTouchHelper(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

        recyclerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Toast.makeText(getApplicationContext(), "longcklick", Toast.LENGTH_LONG).show();
                return true;
            }
        });
    }




    @Override
    public void onItemClick(int p) {
        Item item = (Item) listData.get(p);
        Intent i = new Intent(this, DetailActivity.class);
        String tempT = item.getTitle();
        int id = db.getCollectionIdByName(tempT);
        String collectionName = db.getColName(id);
        Bundle extras = new Bundle();
        extras.putString(EXTRA_QUOTE, item.getTitle());
        extras.putString(EXTRA_ATTR, item.getSubTitle());
        extras.putByteArray(EXTRA_ARR, item.getImage());
        extras.putString(EXTRA_CAT1, item.getCategories());
        extras.putString(EXTRA_CAT2, item.getItemCat());
        extras.putString(EXTRA_ID, getT);
        i.putExtra(BUNDLE_EXTRAS, extras);

        startActivity(i);
    }

    @Override
    public void onSecondaryIconClick(int p) {
        Item item = (Item) listData.get(p);

        if (item.getFavorite() == 1) {
           item.setFavorite(0);
            db.setNotFavorite(item);

        } else  {
            item.setFavorite(1);
            db.setFavoriteItem(item);

       }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEditClick(int p) {
        Item itemToTransfer = (Item) listData.get(p);
        Intent intentEdit = new Intent(this, UpdateItem.class);

        Bundle extras = new Bundle();
        extras.putString(EXTRA_QUOTE, itemToTransfer.getTitle());
        extras.putString(EXTRA_ATTR, itemToTransfer.getSubTitle());
        extras.putByteArray(EXTRA_ARR, itemToTransfer.getImage());
        extras.putString(EXTRA_CAT1, itemToTransfer.getCategories());
        extras.putString(EXTRA_CAT2, itemToTransfer.getItemCat());
        extras.putString(EXTRA_ID, getT);
        intentEdit.putExtra(BUNDLE_EXTRAS, extras);

        startActivity(intentEdit);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_select) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(getT);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        // hiding & showing the title when toolbar expanded & collapsed
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getT);

                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(getT);

                    isShow = false;
                }
            }
        });
    }



}
