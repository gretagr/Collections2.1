package com.collections2.grigelionyte.greta.collections.ui.addEdit;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.collections2.grigelionyte.greta.collections.R;
import com.collections2.grigelionyte.greta.collections.model.Item;
import com.collections2.grigelionyte.greta.collections.model.MyDBHandler;
import com.collections2.grigelionyte.greta.collections.ui.main.CollectionsActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class UpdateItem extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageView addImage;
    public static final int REQUEST_TAKE_PHOTO = 1;
    Button cancel, save;
    Spinner spinner1;
    EditText itemName, itemDesc;
    ArrayList<String> names = new ArrayList<String>();
    ArrayAdapter<String> spAdapter1;
    MyDBHandler db;
    List<EditText> allEditText = new ArrayList<EditText>();
    String categories;
    String categories2;
    Uri uri;
    String desc;
    String title;
    String collName;
    File imageFile;
    TextView categoriesView;
    LinearLayout linearLayout;
    String catResult = null;
    Bitmap image;

    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String EXTRA_QUOTE = "EXTRA_QUOTE";
    private static final String EXTRA_ATTR = "EXTRA_ATTR";
    private static final String EXTRA_ARR = "EXTRA_ARR";
    private static final String EXTRA_CAT1 = "EXTRA_CAT1";
    private static final String EXTRA_CAT2 = "EXTRA_CAT2";
    private static final String EXTRA_ID = "EXTRA_ID";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        Bundle extras = getIntent().getBundleExtra(BUNDLE_EXTRAS);
        db = new MyDBHandler(getApplicationContext());

        byte[] img = extras.getByteArray(EXTRA_ARR);
        image = BitmapFactory.decodeByteArray(img, 0, img.length);
        desc = extras.getString(EXTRA_ATTR);
        title = extras.getString(EXTRA_QUOTE);
        categories = extras.getString(EXTRA_CAT1);
        categories2 = extras.getString(EXTRA_CAT2);
        collName = extras.getString(EXTRA_ID);

        linearLayout = (LinearLayout) findViewById(R.id.linear);
        itemDesc = (EditText) findViewById(R.id.itemDesc);
        itemName = (EditText) findViewById(R.id.itemName);
        spinner1 = (Spinner) findViewById(R.id.spinner_collections);
        categoriesView = (TextView)findViewById(R.id.textView3);

        spAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, names);

        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
        addImage = (ImageView) findViewById(R.id.addImage);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancel = new Intent(UpdateItem.this, CollectionsActivity.class);
                startActivity(cancel);
            }
        });
        spinner1.setOnItemSelectedListener(this);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(categories != null) {
                    Uri tempUri = Uri.fromFile(imageFile);
                    int size = allEditText.size();
                    String[] strings = new String[size];
                    for (int j = 0; j < size; j++) {
                        strings[j] = allEditText.get(j).getText().toString();
                    }
                    catResult = Arrays.toString(strings);
                    if(catResult.equals("[, ]")){
                        catResult = null;
                    }
                }
                else {
                    catResult = null;
                }
                Toast.makeText(getApplicationContext(), "catresult = " + catResult, Toast.LENGTH_LONG).show();
                String text = spinner1.getSelectedItem().toString();
                if (itemName.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Item not created. You must enter the name.", Toast.LENGTH_SHORT).show();
                } else if (itemDesc.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Item not created. You must enter the description.", Toast.LENGTH_SHORT).show();
                } else {
                    Item item = new Item(String.valueOf(itemName.getText()), String.valueOf(itemDesc.getText()), imageViewToByte(addImage), categories, catResult, db.getId(text));
                    db.addItem(item);
                    Toast.makeText(getApplicationContext(), "new item created", Toast.LENGTH_SHORT).show();
                    Intent home = new Intent(UpdateItem.this, CollectionsActivity.class);
                    startActivity(home);
                }

            }
        });

        populateColNames();
    }

    public void launchCamera(View view) throws IOException {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Calendar cal = Calendar.getInstance();
        imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), (cal.getTimeInMillis() + ".jpg"));
        uri.fromFile(imageFile);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        camera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(camera, REQUEST_TAKE_PHOTO);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            addImage.setImageBitmap(photo);
        }
    }

    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void populateColNames() {
        Cursor cursor = db.getAllValues();

        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            names.add(name);
        }
        spinner1.setAdapter(spAdapter1);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String text = spinner1.getSelectedItem().toString();
        categories = db.getCategories(text);
        if (categories == null) {
            categoriesView.setVisibility(View.INVISIBLE);
            catResult = null;
            linearLayout.removeAllViews();
        }
        else if (categories != null) {
            List<String> categoriesList = new ArrayList<String>();
            String str[] = categories.split(",");
            str = correct(str);
            categoriesList = java.util.Arrays.asList(str);


            linearLayout.removeAllViews();

            for (int y = 0; y < categoriesList.size(); y++) {
                EditText editText = new EditText(getApplicationContext());
                allEditText.add(editText);
                editText.setTextColor(Color.BLACK);
                editText.setPadding(0, 30, 0, 30);
                editText.setHintTextColor(Color.parseColor("#BCBCBC"));
                editText.setHint(categoriesList.get(y));
                linearLayout.addView(editText);
            }
        }
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
