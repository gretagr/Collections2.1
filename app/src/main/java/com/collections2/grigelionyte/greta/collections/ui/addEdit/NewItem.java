package com.collections2.grigelionyte.greta.collections.ui.addEdit;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.collections2.grigelionyte.greta.collections.R;
import com.collections2.grigelionyte.greta.collections.model.Item;
import com.collections2.grigelionyte.greta.collections.model.MyDBHandler;
import com.collections2.grigelionyte.greta.collections.ui.main.CollectionsActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class NewItem extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageView addImage;
    public static final int REQUEST_TAKE_PHOTO = 1;
    Button cancel, save;
    Spinner spinner;
    EditText itemName, itemDesc;
    ArrayList<String> names = new ArrayList<String>();
    ArrayAdapter<String> spAdapter;
    MyDBHandler db;
    List<EditText> allEditText = new ArrayList<EditText>();
    String categories, catResult;
    static Uri capturedImageUri = null;
    private File getImageFile;
    File imageFile;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);


        itemDesc = (EditText) findViewById(R.id.itemDesc);
        itemName = (EditText) findViewById(R.id.itemName);
        spinner = (Spinner) findViewById(R.id.spinner_collections);
        // colNme = spinner.getSelectedItem().toString();

        db = new MyDBHandler(getApplicationContext());

        spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, names);

        save = (Button) findViewById(R.id.save);

        cancel = (Button) findViewById(R.id.cancel);

        addImage = (ImageView) findViewById(R.id.addImage);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancel = new Intent(NewItem.this, CollectionsActivity.class);
                startActivity(cancel);
            }
        });
        spinner.setOnItemSelectedListener(this);


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri tempUri = Uri.fromFile(imageFile);
                int size = allEditText.size();
                String[] strings = new String[size];
                for (int j = 0; j < size; j++) {
                    strings[j] = allEditText.get(j).getText().toString();
                }
                catResult = Arrays.toString(strings);

                String text = spinner.getSelectedItem().toString();
                if (itemName.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Item not created. You must enter the name.", Toast.LENGTH_SHORT).show();
                } else if (itemDesc.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Item not created. You must enter the description.", Toast.LENGTH_SHORT).show();
                } else {
                    Item item = new Item(String.valueOf(itemName.getText()), String.valueOf(itemDesc.getText()), tempUri, categories, catResult, db.getId(text));
                    db.addItem(item);
                    Toast.makeText(getApplicationContext(), "new item created", Toast.LENGTH_SHORT).show();
                    Intent home = new Intent(NewItem.this, CollectionsActivity.class);
                    startActivity(home);
                }

            }
        });

        populateColNames();


    }
    public void launchCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Calendar cal = Calendar.getInstance();
        imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), (cal.getTimeInMillis() + ".jpg"));
        Uri tempUri = Uri.fromFile(imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, 0);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==0){
            switch (resultCode){
                case Activity.RESULT_OK:
                    if(imageFile.exists())
                    {
                        //Bitmap photo = (Bitmap) data.getExtras().get("data");
                        //addImage.setImageBitmap(photo);
                    }
                    else{
                        Toast.makeText(this, "image file was not created", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case Activity.RESULT_CANCELED:
                    break;
                default:
                    break;
            }
        }
    }

    public void populateColNames() {
        Cursor cursor = db.getAllValues();

        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            names.add(name);
        }
        spinner.setAdapter(spAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String text = spinner.getSelectedItem().toString();
        categories = db.getCategories(text);
        List<String> categoriesList = new ArrayList<String>();
        String str[] = categories.split(",");
        str = correct(str);
        categoriesList = java.util.Arrays.asList(str);

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linear);
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




