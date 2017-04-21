package com.collections2.grigelionyte.greta.collections.ui.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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

public class CollectionsActivity extends AppCompatActivity implements CardAdapter.ItemClickCallback{

    private Toolbar toolbar;
    private RecyclerView recView;
    private CardAdapter adapter;
    private ArrayList cardData;
    MyDBHandler db;
    int colCount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);

        db = new MyDBHandler(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initCollapsingToolbar();
        // SETTING CARD VIEW ----------------------------------------------------------------------------------------------------------------------
        cardData = (ArrayList) db.getAllCollections();

        recView = (RecyclerView)findViewById(R.id.rec_list);
        recView.setItemAnimator(new DefaultItemAnimator());
        recView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new CardAdapter(cardData, this);
        recView.setAdapter(adapter);
        adapter.setItemClickCallback(this);

        colCount = db.getCollectionsCount();


        // FLOATING ACTION BUTTON START-------------------------------------------------------------------------------------------------------------


        FloatingActionButton add = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        FloatingActionButton addItem = (FloatingActionButton) findViewById(R.id.floatingActionButtonItem);
        FloatingActionButton addCol = (FloatingActionButton) findViewById(R.id.floatingActionButtonCol);
        final LinearLayout itemLayout = (LinearLayout) findViewById(R.id.addNew);
        final LinearLayout colLayout = (LinearLayout) findViewById(R.id.collection);
        final Animation mShowButton = AnimationUtils.loadAnimation(CollectionsActivity.this, R.anim.show_button);
        final Animation mHideButton = AnimationUtils.loadAnimation(CollectionsActivity.this, R.anim.hide_button);
        final Animation mShowLayout = AnimationUtils.loadAnimation(CollectionsActivity.this, R.anim.show_layout);
        final Animation mHideLayout = AnimationUtils.loadAnimation(CollectionsActivity.this, R.anim.hide_layout);

        // floating button main action-------------------------------------------------------------------------------------------------------------
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
        // floating button add item action-----------------------------------------------------------------------------------------------------------
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
        // floating button add collection action---------------------------------------------------------------------------------------------------------
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
        // FLOATING ACTION BUTTON END---------------------------------------------------------------------------------------------------------------------

    }   // ON CREATE END ----------------------------------------------------------------------------------------------------------------------------------

        // MENU ------------------------------------------------------------------------------------------------------------------------------------------
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

    @Override
    public void onItemClick(int p) {
        ItemsCollection col = (ItemsCollection) cardData.get(p);

        Intent intent = makeIntent(CollectionsActivity.this, col.getColTitle());
        startActivity(intent);
    }

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

    @Override
    public void onDeleteClick(int p) {
        ItemsCollection itemsCollection = (ItemsCollection)cardData.get(p);
        db.deleteCollection(itemsCollection);
        adapter.remove(p);
    }

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
    public static Intent makeIntent(Context context, String nameofcol) {
        Intent intent = new Intent(context, ListActivity.class);
        intent.putExtra("name_of_collection", nameofcol);
        return intent;
    }

}
