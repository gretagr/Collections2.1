package com.collections2.grigelionyte.greta.collections.ui.addEdit;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;


public class NewCollection extends AppCompatActivity {

    public static final int REQUEST_TAKE_PHOTO = 1;
    Button cancel, save, addCategoyBtn;
    EditText addCategoryField, itemName, itemDesc;
    ListView categoriesListview;
    ArrayList<String> categoriesList = null;
    ArrayAdapter<String> categoriesadapter;
    ImageView addImage;
    File imageFile;
    Uri uri;
    MyDBHandler db;
    String result = null;
    String catList = null;
    byte[] standartCheck;
    byte[] setNoPhoto;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_collection);
        //------------------------------------------------------------------------------------------------------------------------- get db
        db = new MyDBHandler(this);
        //--------------------------------------------------------------------------------------------------------------------- find views
        addCategoryField = (EditText) findViewById(R.id.editText);
        addCategoyBtn = (Button) findViewById(R.id.button_addData);
        categoriesListview = (ListView) findViewById(R.id.listView_lv);
        addImage = (ImageView) findViewById(R.id.addColImg);
        //-------------------------------------------------------------------------------------------------------------------- set adapters
        categoriesList = new ArrayList<String>();
        categoriesadapter = new ArrayAdapter<String>(NewCollection.this, android.R.layout.simple_list_item_1, categoriesList);
        categoriesListview.setAdapter(categoriesadapter);
        //--------------------------------------------------------------------------------------------------- image from image view to byte
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pink_add_photo);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        standartCheck = stream.toByteArray();
        //------------------------------------------------------------------------------------ preferred image if photo not captured to byte
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.color_middle_blue);
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream1);
        setNoPhoto = stream1.toByteArray();
        //------------------------------------------------------------------------------------------------------------------ CANCEL BUTTON
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancel = new Intent(NewCollection.this, CollectionsActivity.class);
                startActivity(cancel);
                finish();
            }
        });
        //--------------------------------------------------------------------------------------------------------------------- find views
        itemDesc = (EditText)findViewById(R.id.colDesc);
        itemName = (EditText)findViewById(R.id.colName);
        //---------------------------------------------------------------------------------------------------------------------- SAVE BUTTON
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //----------------------------------------------------------------------------------------------------------- get categories
                catList = categoriesList.toString();
                if (categoriesList.toString() == "[]") {
                    catList = null;
                }
                else{
                    catList = categoriesList.toString();
                }
                //---------------------------------------------------------------------------------------check if name and description filled
                if (itemName.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Collection not created. You must enter the name.", Toast.LENGTH_SHORT).show();
                } else if (itemDesc.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Collection not created. You must enter the description.", Toast.LENGTH_SHORT).show();
                }
                //--------------------------------------------------------------------------------adding this to db if user did not added img
                else if (Arrays.equals(imageViewToByte(addImage), standartCheck)){
                    ItemsCollection collection = new ItemsCollection(
                            String.valueOf(itemName.getText()),
                            String.valueOf(itemDesc.getText()),
                            setNoPhoto,
                            catList, 0);
                    db.addCollection(collection);
                    Toast.makeText(getApplicationContext(), "new collection created", Toast.LENGTH_SHORT).show();
                    //--------------------------------------------------------------------------- start new collections activity with changes
                    Intent home = new Intent(NewCollection.this, CollectionsActivity.class);
                    startActivity(home);
                    finish();
                }
                //----------------------------------------------------------------------------------------add this to db if user added photo
                else {
                    ItemsCollection collection = new ItemsCollection(
                            String.valueOf(itemName.getText()),
                            String.valueOf(itemDesc.getText()),
                            imageViewToByte(addImage),
                            catList, 0);
                    db.addCollection(collection);

                    Toast.makeText(getApplicationContext(), "new collection created", Toast.LENGTH_SHORT).show();
                    //--------------------------------------------------------------------------- start new collections activity with changes
                    Intent home = new Intent(NewCollection.this, CollectionsActivity.class);
                    startActivity(home);
                    finish();
                }
            }
        });
        //------------------------------------------------------------------------------------------------------- call methods for categories
        addCat();
        deleteCat();

    }
    //-------------------------------------------------------------------------------------------------------------- method for taking photo
    public void launchCamera(View view) throws IOException {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Calendar cal = Calendar.getInstance();
        imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), (cal.getTimeInMillis() + ".jpg"));
        uri.fromFile(imageFile);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        camera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(camera, REQUEST_TAKE_PHOTO);
    }
    //---------------------------------------------------------------------------------------------------- what to do wit image capture result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            addImage.setImageBitmap(photo);
        }
    }
    //------------------------------------------------------------------------------------------------------------- method for adding categories
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
    //------------------------------------------------------------------------------------------------------------- convert imageView to byte[]
    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
    //------------------------------------------------------------------------------------------------------ method for deleting inserted category
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