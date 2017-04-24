package com.collections2.grigelionyte.greta.collections.ui.main;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.collections2.grigelionyte.greta.collections.R;
import com.collections2.grigelionyte.greta.collections.model.MyDBHandler;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String EXTRA_QUOTE = "EXTRA_QUOTE";
    private static final String EXTRA_ATTR = "EXTRA_ATTR";
    private static final String EXTRA_ARR = "EXTRA_ARR";
    private static final String EXTRA_CAT1 = "EXTRA_CAT1";
    private static final String EXTRA_CAT2 = "EXTRA_CAT2";
    private static final String EXTRA_ID = "EXTRA_ID";
    private static final String EXTRA_ID_DESC = "EXTRA_ID_DESC";


    ArrayAdapter<String> categoriesadapter;
    List<String> categoriesList;
    List<String> categoriesList2;
    private Toolbar toolbar;
    MyDBHandler db;
    String catNames;
    String catDetails;
    LinearLayout linearLayout;
    String name, collectionName, collectionDescription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        db = new MyDBHandler(getApplicationContext());

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initCollapsingToolbar();
        linearLayout = (LinearLayout) findViewById(R.id.list);
        linearLayout.setPadding(0,20,0,0);
        Bundle extras = getIntent().getBundleExtra(BUNDLE_EXTRAS);
        collectionName = extras.getString(EXTRA_ID);
        collectionDescription = extras.getString(EXTRA_ID_DESC);
        name = extras.getString(EXTRA_QUOTE);
        byte[] img = extras.getByteArray(EXTRA_ARR);
        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);


        catNames = extras.getString(EXTRA_CAT1);
        catDetails = extras.getString(EXTRA_CAT2);

        if (catNames != null && catDetails != null) {
            String str[] = catNames.split(", ");
            str = correct(str);
            categoriesList = java.util.Arrays.asList(str);

            for (int i = 0; i < categoriesList.size(); i++) {

                TextView textView2 = new TextView(getApplicationContext());
                TextView textView = new TextView(getApplicationContext());

                linearLayout.addView(textView);
                textView.setText(categoriesList.get(i));
                textView.setPadding(20,10,20,0);
                textView.setTextColor(Color.parseColor("#353535"));

                        String str1[] = catDetails.split(", ");
                        str1 = correct(str1);
                        categoriesList2 = java.util.Arrays.asList(str1);


                    linearLayout.addView(textView2);
                    textView2.setText(categoriesList2.get(i));
                    textView2.setPadding(20, 10, 20, 10);
                    textView2.setTextColor(Color.parseColor("#757575"));

            }

        }
        ((TextView)findViewById(R.id.lbl_quote_text)).setText(extras.getString(EXTRA_QUOTE));
        ((TextView)findViewById(R.id.lbl_quote_attribution)).setText(extras.getString(EXTRA_ATTR));
        ((ImageView)findViewById(R.id.backdrop)).setImageBitmap(bitmap);
    }

    public String[] correct(String[] categoriesList) {
        int length = categoriesList.length;
        String str1 = categoriesList[0];
        str1 = str1.substring(1);
        String str2 = categoriesList[length - 1];
        int li = str2.length() - 1;
        str2 = str2.substring(0, li);
        categoriesList[0] = str1;
        categoriesList[length - 1] = str2;
        return categoriesList;
    }
    private void initCollapsingToolbar() {
        final CollapsingToolbarLayout collapsingToolbar =
                (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbar.setTitle(name);
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
                    collapsingToolbar.setTitle(name);

                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(name);

                    isShow = false;
                }
            }
        });
    }
    @Override
    public void onBackPressed(){
        Intent intent = makeIntent(DetailActivity.this, collectionName, collectionDescription);
        startActivity(intent);
        finish();
    }
    public static Intent makeIntent(Context context, String nameofcol, String desc) {
        Intent intent = new Intent(context, ListActivity.class);
        intent.putExtra("name_of_collection", nameofcol);
        intent.putExtra("description_of_collection", desc);
        return intent;
    }
}
