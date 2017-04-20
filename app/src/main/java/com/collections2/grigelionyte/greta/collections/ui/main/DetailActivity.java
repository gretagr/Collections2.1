package com.collections2.grigelionyte.greta.collections.ui.main;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.collections2.grigelionyte.greta.collections.R;
import com.collections2.grigelionyte.greta.collections.ui.addEdit.NewCollection;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String EXTRA_QUOTE = "EXTRA_QUOTE";
    private static final String EXTRA_ATTR = "EXTRA_ATTR";
    private static final String EXTRA_ARR = "EXTRA_ARR";
    private static final String EXTRA_CAT1 = "EXTRA_CAT1";
    private static final String EXTRA_CAT2 = "EXTRA_CAT2";
    ImageView image;
    ListView listView;
    ArrayAdapter<String> categoriesadapter;
    List<String> categoriesList = null;
    List<String> categoriesList2 = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        image = (ImageView)findViewById(R.id.im_detail_image);
        image.requestLayout();
       //image.getLayoutParams().height = RelativeLayout.LayoutParams.MATCH_PARENT;


        Bundle extras = getIntent().getBundleExtra(BUNDLE_EXTRAS);
        byte[] img = extras.getByteArray(EXTRA_ARR);
        Bitmap bitmap = BitmapFactory.decodeByteArray(img, 0, img.length);

        String categories = extras.getString(EXTRA_CAT1);
        String categoriesItems = extras.getString(EXTRA_CAT2);


        listView = (ListView)findViewById(R.id.myList);
        categoriesList = new ArrayList<String>();
        String str[] = categories.split(",");
        str = correct(str);

        categoriesList2 = new ArrayList<String>();
        String str1[] = categoriesItems.split(", ");
        str = correct(str1);


        categoriesList2 = java.util.Arrays.asList(str1);

        categoriesadapter = new ArrayAdapter<String>(DetailActivity.this, android.R.layout.simple_list_item_1, android.R.id.text1, categoriesList2);
        listView.setAdapter(categoriesadapter);



        ((TextView)findViewById(R.id.lbl_quote_text)).setText(extras.getString(EXTRA_QUOTE));
        ((TextView)findViewById(R.id.lbl_quote_attribution)).setText(extras.getString(EXTRA_ATTR));
        ((ImageView)findViewById(R.id.im_detail_image)).setImageBitmap(bitmap);
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
}
