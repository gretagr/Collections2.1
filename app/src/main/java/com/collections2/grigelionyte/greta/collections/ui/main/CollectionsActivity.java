package com.collections2.grigelionyte.greta.collections.ui.main;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.collections2.grigelionyte.greta.collections.R;
import com.collections2.grigelionyte.greta.collections.adapters.CardAdapter;
import com.collections2.grigelionyte.greta.collections.model.ItemsCollection;
import com.collections2.grigelionyte.greta.collections.model.MyDBHandler;
import com.collections2.grigelionyte.greta.collections.ui.addEdit.NewCollection;
import com.collections2.grigelionyte.greta.collections.ui.addEdit.NewItem;

import java.util.ArrayList;

public class CollectionsActivity extends AppCompatActivity implements CardAdapter.ItemClickCallback, SearchView.OnQueryTextListener {
    //---------------------------------------------------------------------------------- toolBar
    private Toolbar toolbar;
    MenuItem count;
    //------------------------------------------------------------ recycler anr recycler adapter
    private RecyclerView recView;
    private CardAdapter adapter;
    ItemsCollection itemToDelete;
    //----------------------------------------------------------------- data from db to recycler
    private ArrayList<ItemsCollection>cardData;
    MyDBHandler db;
    int colCount;
    //------------------------------------------------------------ GridLayoutManager column count
    GridLayoutManager grid1;
    GridLayoutManager grid2;
    int num = 0;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);
        db = new MyDBHandler(getApplicationContext());
        //-------------------------------------------------------- GridLayoutManager column count
        grid1 = new GridLayoutManager(this, 1);
        grid2 = new GridLayoutManager(this, 2);
        //-------------------------------------------------------------toolbar, collapsingToolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initCollapsingToolbar();
        //------------------------------------------------------------------get list data from db
        cardData = (ArrayList) db.getAllCollections();
        colCount = db.getCollectionsCount();
        //--------------------------------------------------------------------setting rec adapter
        recView = (RecyclerView)findViewById(R.id.rec_list);
        recView.setItemAnimator(new DefaultItemAnimator());
        recView.setLayoutManager(grid2);
        adapter = new CardAdapter(cardData, this);
        recView.setAdapter(adapter);
        adapter.setItemClickCallback(this);
        //------------------------------------------------------------- main floatingActionButton
        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.floatingActionButton);
        //--------------------------------------------------------- add item floatingActionButton
        FloatingActionButton addItem = (FloatingActionButton) findViewById(R.id.floatingActionButtonItem);
        //--------------------------------------------------- add collection floatingActionButton
        FloatingActionButton addCol = (FloatingActionButton) findViewById(R.id.floatingActionButtonCol);
        //--------------------------------------------------- animations for floatingActionButton
        final LinearLayout itemLayout = (LinearLayout) findViewById(R.id.addNew);
        final LinearLayout colLayout = (LinearLayout) findViewById(R.id.collection);
        final Animation mShowButton = AnimationUtils.loadAnimation(CollectionsActivity.this, R.anim.show_button);
        final Animation mHideButton = AnimationUtils.loadAnimation(CollectionsActivity.this, R.anim.hide_button);
        final Animation mShowLayout = AnimationUtils.loadAnimation(CollectionsActivity.this, R.anim.show_layout);
        final Animation mHideLayout = AnimationUtils.loadAnimation(CollectionsActivity.this, R.anim.hide_layout);
        //----------------------------------------------------- main floatingActionButton actions
        add.setOnClickListener(new View.OnClickListener() {
            View bc = findViewById(R.id.floating_bc);
            @Override
            public void onClick(View view) {
                FloatingActionButton add = (FloatingActionButton) findViewById(R.id.floatingActionButton);
                if (itemLayout.getVisibility() == View.VISIBLE && colLayout.getVisibility() == View.VISIBLE) {
                    itemLayout.setVisibility(View.GONE);
                    colLayout.setVisibility(View.GONE);
                    colLayout.startAnimation(mHideLayout);
                    itemLayout.startAnimation(mHideLayout);
                    add.startAnimation(mHideButton);
                    bc.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
                else {
                    itemLayout.setVisibility(View.VISIBLE);
                    colLayout.setVisibility(View.VISIBLE);
                    colLayout.startAnimation(mShowLayout);
                    itemLayout.startAnimation(mShowLayout);
                    add.startAnimation(mShowButton);
                    bc.setBackgroundColor(getResources().getColor(R.color.trans));
                }
            }
        });
        //------------------------------------------------ add item floatingActionButton actions
        if (colCount < 1) {
            addItem.setEnabled(false);
        }
        else {
            addItem.setOnClickListener(new View.OnClickListener() {
                FloatingActionButton add = (FloatingActionButton) findViewById(R.id.floatingActionButton);
                @Override
                public void onClick(View view) {
                    itemLayout.setVisibility(View.GONE);
                    colLayout.setVisibility(View.GONE);
                    colLayout.startAnimation(mHideLayout);
                    itemLayout.startAnimation(mHideLayout);
                    add.startAnimation(mHideButton);
                    Intent addItemActive = new Intent(CollectionsActivity.this, NewItem.class);
                    startActivity(addItemActive);
                }
            });
        }
        //------------------------------------------- add collection floatingActionButton actions
        addCol.setOnClickListener(new View.OnClickListener() {
            FloatingActionButton add = (FloatingActionButton) findViewById(R.id.floatingActionButton);
            @Override
            public void onClick(View view) {
                itemLayout.setVisibility(View.GONE);
                colLayout.setVisibility(View.GONE);
                colLayout.startAnimation(mHideLayout);
                itemLayout.startAnimation(mHideLayout);
                add.startAnimation(mHideButton);
                Intent addColActive = new Intent(CollectionsActivity.this, NewCollection.class);
                startActivity(addColActive);
            }
        });
    }
    //-------------------------------------------------------------------- end of onCreate method
    //---------------------------------------------------------------------------- inflating menu
        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_main, menu);
            count = menu.findItem(R.id.action_change_view);
            MenuItemCompat.getActionView(count);
            count.setIcon(R.drawable.ic_view_stream_white_24dp);
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
            if (id == R.id.action_search) {

                return true;
            }
            else if (id == R.id.action_change_view) {
                if (num % 2 == 0){
                    recView.setLayoutManager(grid1);
                    count.setIcon(R.drawable.ic_view_module_white_24dp);
                    num++;
                }
                else if (num % 2 != 0) {
                    recView.setLayoutManager(grid2);
                    count.setIcon(R.drawable.ic_view_stream_white_24dp);
                    num++;
                }
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    //-------------------------------------------------------------------- starting list activity
    @Override
    public void onItemClick(int p) {
        Intent intent = makeIntent(CollectionsActivity.this, adapter.cardData.get(p).getColTitle(), adapter.cardData.get(p).getSubTitle());
        startActivity(intent);
    }
    //------------------------------------------------------------------------- setting favorites
    @Override
    public void onSecondaryIconClick(int p) {
        ItemsCollection item = (ItemsCollection) cardData.get(p);

        if (item.getFavoriteCol() == 1) {
            item.setFavoriteCol(0);
            db.setNotFavoriteCol(item);
        } else  {
            item.setFavoriteCol(1);
           db.setFavoriteCol(item);
        }
        adapter.notifyDataSetChanged();
    }
    //------------------------------------------------------------------------------- delete item
    @Override
    public void onDeleteClick(final int p) {
        itemToDelete = (ItemsCollection)cardData.get(p);
        AlertDialog.Builder deleteAlert = new AlertDialog.Builder(CollectionsActivity.this);
        deleteAlert.setMessage("When collection deleted, all items in it will be deleted too.")
                .setTitle("Delete collection?")
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        db.deleteCollection(itemToDelete);
                        adapter.remove(p);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        deleteAlert.create();
        deleteAlert.show();
    }
    //--------------------------------------------------------------- handling collapsing toolbar
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
        (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(" ");
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        appBarLayout.setExpanded(false);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    collapsingToolbar.setTitle(getString(R.string.app_name));
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");
                    isShow = false;
                }
            }
        });
    }

    @Override
    protected void onResume() {
      super.onResume();
        View bc = findViewById(R.id.floating_bc);
        bc.setBackgroundColor(getResources().getColor(android.R.color.transparent));
    }
    //--------------------------------------------------------- method for starting list activity
    public static Intent makeIntent(Context context, String nameofcol, String desc) {
        Intent intent = new Intent(context, ListActivity.class);
        intent.putExtra("name_of_collection", nameofcol);
        intent.putExtra("description_of_collection", desc);
        return intent;
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
