package com.collections2.grigelionyte.greta.collections.ui.addEdit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.ListView;
import android.widget.Toast;

import com.collections2.grigelionyte.greta.collections.R;
import com.collections2.grigelionyte.greta.collections.model.ItemsCollection;
import com.collections2.grigelionyte.greta.collections.model.MyDBHandler;
import com.collections2.grigelionyte.greta.collections.ui.main.CollectionsActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class NewCollection extends AppCompatActivity {

    public static final int REQUEST_TAKE_PHOTO = 1;
    Button cancel, save, addCategoyBtn;
    EditText addCategoryField, itemName, itemDesc;
    ListView categoriesListview;
    ArrayList<String> categoriesList = null;
    ArrayAdapter<String> categoriesadapter;
    ImageView addImage;
    String imageName = "collections";
    File imageFile;
    Uri uri;
    MyDBHandler db;
    String result = null;
    String catList = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_collection);

        db = new MyDBHandler(this);
        addCategoryField = (EditText) findViewById(R.id.editText);
        addCategoyBtn = (Button) findViewById(R.id.button_addData);
        categoriesListview = (ListView) findViewById(R.id.listView_lv);
        addImage = (ImageView) findViewById(R.id.addColImg);

        categoriesList = new ArrayList<String>();
        categoriesadapter = new ArrayAdapter<String>(NewCollection.this, android.R.layout.simple_list_item_1, categoriesList);
        categoriesListview.setAdapter(categoriesadapter);


        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancel = new Intent(NewCollection.this, CollectionsActivity.class);
                startActivity(cancel);
            }
        });
        itemDesc = (EditText)findViewById(R.id.colDesc);
        itemName = (EditText)findViewById(R.id.colName);
        save = (Button) findViewById(R.id.save);

        save.setOnClickListener(new View.OnClickListener() {
         @Override
              public void onClick(View v) {
                 catList = categoriesList.toString();
                    if (categoriesList.toString() == "[]") {
                        catList = null;
                    }
                    else{
                        catList = categoriesList.toString();
                    }
                   if (itemName.getText().toString().isEmpty()) {
                       Toast.makeText(getApplicationContext(), "Collection not created. You must enter the name.", Toast.LENGTH_SHORT).show();
                   } else if (itemDesc.getText().toString().isEmpty()) {
                       Toast.makeText(getApplicationContext(), "Collection not created. You must enter the description.", Toast.LENGTH_SHORT).show();
                   }
                   else {
                       ItemsCollection collection = new ItemsCollection(String.valueOf(itemName.getText()), String.valueOf(itemDesc.getText()), imageViewToByte(addImage), catList);
                       db.addCollection(collection);
                       Toast.makeText(getApplicationContext(), "new collection created", Toast.LENGTH_SHORT).show();
                       Intent home = new Intent(NewCollection.this, CollectionsActivity.class);
                       startActivity(home);
                       Toast.makeText(getApplicationContext(), "cat = " + catList, Toast.LENGTH_LONG).show();
                    }
                }
                });

                addCat();
                deleteCat();

    }
    private String getCurrentDateAndTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
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
            public void addCat() {
                addCategoyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!addCategoryField.getText().equals("")) {
                            result = addCategoryField.getText().toString();
                        }
                        categoriesList.add(result);
                        categoriesadapter.notifyDataSetChanged();
                        addCategoryField.setText("");

                    }

                });
            }
    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
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