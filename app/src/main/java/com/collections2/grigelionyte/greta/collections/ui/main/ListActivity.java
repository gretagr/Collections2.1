package com.collections2.grigelionyte.greta.collections.ui.main;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.collections2.grigelionyte.greta.collections.R;
import com.collections2.grigelionyte.greta.collections.adapters.ListItemAdapter;
import com.collections2.grigelionyte.greta.collections.adapters.ListTouchHelper;
import com.collections2.grigelionyte.greta.collections.model.MyDBHandler;
import com.collections2.grigelionyte.greta.collections.ui.addEdit.NewItem;
import com.collections2.grigelionyte.greta.collections.ui.addEdit.UpdateItem;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity implements ListItemAdapter.ItemClickCallback, SearchView.OnQueryTextListener {
    //-------------------------------------------------------------------- detail activity extras
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String EXTRA_QUOTE = "EXTRA_QUOTE";
    private static final String EXTRA_ATTR = "EXTRA_ATTR";
    private static final String EXTRA_ARR = "EXTRA_ARR";
    private static final String EXTRA_CAT1 = "EXTRA_CAT1";
    private static final String EXTRA_CAT2 = "EXTRA_CAT2";
    private static final String EXTRA_ID = "EXTRA_ID";
    private static final String EXTRA_ID_DESC = "EXTRA_ID_DESC";
    //------------------------------------------------------------- recycler and recycler adapter
    private RecyclerView recyclerView;
    private ListItemAdapter adapter;
    //-------------------------------------------------------------------------- listData from db
    private ArrayList listData;
    private Toolbar toolbar;
    private MyDBHandler db;
    //---------------------------------------------------------------- collection details from db
    String getT;
    String getD;
    int colId;
    //-------------------------------------------------------------------- floating action button
    FloatingActionButton additem;
    //------------------------------------------------------------ request code ActivityForResult
    int REQ_CODE_INTENT = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        db = new MyDBHandler(getApplicationContext());
        //--------------------------------------------------------------getting extras for intent
        Bundle extras = getIntent().getBundleExtra(BUNDLE_EXTRAS);
        getT = getIntent().getStringExtra("name_of_collection");
        getD = getIntent().getStringExtra("description_of_collection");

        //------------------------------------------------------------------get list data from db
        colId = db.getId(getT);
        listData = (ArrayList) db.getAllItemsFromCollection(colId);
        //--------------------------------------------------------------------setting rec adapter
        recyclerView = (RecyclerView) findViewById(R.id.rec_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ListItemAdapter(listData, this);
        recyclerView.setAdapter(adapter);
        adapter.setItemClickCallback(this);
        //-------------------------------------------------------------toolbar, collapsingToolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initCollapsingToolbar();
        //-----------------------------------------------------------------floating Action button
        additem = (FloatingActionButton) findViewById(R.id.fb_add_btn);
        additem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = newItemIntent(ListActivity.this, getT, getD);
                startActivity(intent);
                finish();
            }
        });
        //--------------------------------------------------------------------------swap to delete
        ItemTouchHelper.Callback callback = new ListTouchHelper(adapter);
        ItemTouchHelper helper = new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);


    }
    //-------------------------------------------------------------------- end of onCreate method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){

            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
    //------------------------------------------------------------------ starting detail activity
        @Override
        public void onItemClick(int p) {

        Intent i = new Intent(this, DetailActivity.class);
        Bundle extras = new Bundle();
        extras.putString(EXTRA_QUOTE, adapter.listData.get(p).getTitle());
        extras.putString(EXTRA_ATTR, adapter.listData.get(p).getSubTitle());
        extras.putByteArray(EXTRA_ARR, adapter.listData.get(p).getImage());
        extras.putString(EXTRA_CAT1, adapter.listData.get(p).getCategories());
        extras.putString(EXTRA_CAT2, adapter.listData.get(p).getItemCat());
        extras.putString(EXTRA_ID, getT);
            extras.putString(EXTRA_ID_DESC, getD);

        i.putExtra(BUNDLE_EXTRAS, extras);
            startActivity(i);
            finish();
    }
    //------------------------------------------------------------------------- setting favorites
    @Override
    public void onSecondaryIconClick(int p) {
        //Item item = (Item) listData.get(p);

        if (adapter.listData.get(p).getFavorite() == 1) {
            adapter.listData.get(p).setFavorite(0);
            db.setNotFavorite(adapter.listData.get(p));

        } else  {
            adapter.listData.get(p).setFavorite(1);
            db.setFavoriteItem(adapter.listData.get(p));

       }
        adapter.notifyDataSetChanged();
    }
    //--------------------------------------------------------------- starting edit item activity
    @Override
    public void onEditClick(int p) {
        Intent intentEdit = new Intent(this, UpdateItem.class);
        Bundle extras = new Bundle();
        extras.putString(EXTRA_QUOTE, adapter.listData.get(p).getTitle());
        extras.putString(EXTRA_ATTR, adapter.listData.get(p).getSubTitle());
        extras.putByteArray(EXTRA_ARR, adapter.listData.get(p).getImage());
        extras.putString(EXTRA_CAT1, adapter.listData.get(p).getCategories());
        extras.putString(EXTRA_CAT2, adapter.listData.get(p).getItemCat());
        extras.putString(EXTRA_ID, getT);
        intentEdit.putExtra(BUNDLE_EXTRAS, extras);

        startActivity(intentEdit);
        finish();
    }
    //---------------------------------------------------------------------------- inflating menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_list, menu);
        MenuItem search = menu.findItem(R.id.action_search);
        SearchManager manager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        searchView.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(this);
        return true;
    }
    //--------------------------------------------------------------- what to do if item selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        String description = db.getCollectionDescByName(getT);
        if (id == R.id.action_info) {
            AlertDialog.Builder infoAlert = new AlertDialog.Builder(ListActivity.this);
            infoAlert.setTitle("About " + getT);
            infoAlert.setMessage(description);

            infoAlert.create();
            infoAlert.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    //--------------------------------------------------------------- handling collapsing toolbar
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
    //----------------------------------------------------- method for starting new item activity
    public static Intent newItemIntent(Context context, String nameOfCol, String descCol) {
        Intent intent = new Intent(context, NewItem.class);
        intent.putExtra("name_of_collection_to_send", nameOfCol);
        intent.putExtra("desc_of_collection_to_send", descCol);
        return intent;
    }

    @Override
    public void onRestart(){
        super.onRestart();

    }
    @Override
    public void onResume(){
        super.onResume();

    }

    //-------------------------------------------------------------------------- search filtering
    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        adapter.getFilter().filter(newText);
        return false;
    }
}
