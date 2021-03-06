package com.collections2.grigelionyte.greta.collections.ui.addEdit;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.collections2.grigelionyte.greta.collections.R;
import com.collections2.grigelionyte.greta.collections.model.Item;
import com.collections2.grigelionyte.greta.collections.model.MyDBHandler;
import com.collections2.grigelionyte.greta.collections.ui.main.ListActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
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
    String categories;
    Uri uri;
    File imageFile;
    TextView categoriesView;
    LinearLayout linearLayout;
    String catResult = null;
    String colTitle;
    byte[] standartCheck;
    byte[] setNoPhoto;
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        //--------------------------------------------------------------------------------------------------------------------- get extras
        Bundle extras = getIntent().getBundleExtra(BUNDLE_EXTRAS);
        colTitle = getIntent().getStringExtra("name_of_collection_to_send");
        //--------------------------------------------------------------------------------------------------------------------- find views
        linearLayout = (LinearLayout) findViewById(R.id.linear);
        itemDesc = (EditText) findViewById(R.id.itemDesc);
        itemName = (EditText) findViewById(R.id.itemName);
        spinner = (Spinner) findViewById(R.id.spinner_collections);
        categoriesView = (TextView)findViewById(R.id.textView3);
        //------------------------------------------------------------------------------------------------------------------------- get db
        db = new MyDBHandler(getApplicationContext());
        //--------------------------------------------------------------------------------------------------------------------- find views
        spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, names);
        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);
        addImage = (ImageView) findViewById(R.id.addImage);
        //------------------------------------------------------------------------------------------------------------------ CANCEL BUTTON
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = makeIntent(NewItem.this, spinner.getSelectedItem().toString());
                startActivity(intent);
                finish();
            }
        });
        //----------------------------------------------------------------------------------------------- look for selected item in spinner
        spinner.setOnItemSelectedListener(this);
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
        //---------------------------------------------------------------------------------------------------------------------- SAVE BUTTON
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //---------------------------------------------------------------------------------if categories not null !! to avoid ( [] )
                if(categories != null) {
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
                //-------------------------------------------------------------------------------------------get collection name from spinner
                String text = spinner.getSelectedItem().toString();
                //---------------------------------------------------------------------------------------check if name and description filled
                if (itemName.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Item not created. You must enter the name.", Toast.LENGTH_SHORT).show();
                } else if (itemDesc.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Item not created. You must enter the description.", Toast.LENGTH_SHORT).show();
                }
                //--------------------------------------------------------------------------------adding this to db if user did not added img
                else if (Arrays.equals(imageViewToByte(addImage), standartCheck)){
                    Item item = new Item(
                            String.valueOf(itemName.getText()),
                            String.valueOf(itemDesc.getText()),
                            setNoPhoto,
                            categories,
                            catResult,
                            db.getId(text), 0);

                    db.addItem(item);
                    Toast.makeText(getApplicationContext(), "new item created", Toast.LENGTH_SHORT).show();
                    //--------------------------------------------------------------------------------- start new listActivity with changes
                    Intent intent = makeIntent(NewItem.this, text);
                    startActivity(intent);
                    finish();
                }
                //----------------------------------------------------------------------------------------add this to db if user added photo
                else {
                    Item item = new Item(
                            String.valueOf(itemName.getText()),
                            String.valueOf(itemDesc.getText()),
                            imageViewToByte(addImage),
                            categories,
                            catResult,
                            db.getId(text), 0);

                    db.addItem(item);
                    Toast.makeText(getApplicationContext(), "new item created", Toast.LENGTH_SHORT).show();
                    //--------------------------------------------------------------------------------- start new listActivity with changes
                    Intent intent = makeIntent(NewItem.this, text);
                    startActivity(intent);
                    finish();
                }

            }
        });
        //--------------------------------------------------------------------------------------- call method for spinner to shoe coll names
        populateColNames();


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
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            addImage.setImageBitmap(photo);
        }
    }
    //------------------------------------------------------------------------------------------------------------- convert imageView to byte[]
    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
    //--------------------------------------------------------------------------------------------------- method for spinner to shoe coll names
    public void populateColNames() {
        Cursor cursor = db.getAllValues();

        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            names.add(name);
        }
        spinner.setAdapter(spAdapter);
        spinner.setSelection(spAdapter.getPosition(colTitle));
    }
    //------------------------------------------------------------------------------------------------ what to do if nothing in spinner selected
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    //------------------------------------------------------------------------------------------------------------ what to do with selected item
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        String text = spinner.getSelectedItem().toString();
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

    //--------------------------------------------------------------------------------------------------------------- fix [] in categories arrays
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
    @Override
    //--------------------------------------------------------------------------------------------------------------------------- on back pressed
    public void onBackPressed(){
        Intent intent = makeIntent(NewItem.this, spinner.getSelectedItem().toString());
        startActivity(intent);
        finish();
    }
    //------------------------------------------------------------------------------------------------------- method for intent to list activity
    public static Intent makeIntent(Context context, String nameofcol) {
        Intent intent = new Intent(context, ListActivity.class);
        intent.putExtra("name_of_collection", nameofcol);
        return intent;
    }

}




