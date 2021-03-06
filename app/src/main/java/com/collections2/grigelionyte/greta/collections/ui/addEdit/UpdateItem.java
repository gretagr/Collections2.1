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

public class UpdateItem extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public static final int REQUEST_TAKE_PHOTO = 1;
    ImageView addImage;
    TextView itemTitle;
    TextView itemD;
    Button cancel, save;
    EditText itemName, itemDesc;
    MyDBHandler db;
    String desc;
    String title;
    String collName;
    File imageFile;
    LinearLayout linearLayout;
    String catResult;
    Bitmap image;
    List<String> categoriesList;
    List<String> categoriesList2;
    String catNames;
    String catDetails;
    Uri uri;
    Spinner spinner;
    ArrayList<String> names = new ArrayList<String>();
    ArrayAdapter<String> spAdapter;
    List<EditText> allEditText = new ArrayList<EditText>();
    Item item;

    //------------------------------------------------------------------------------------------------- variables for extras
    private static final String BUNDLE_EXTRAS = "BUNDLE_EXTRAS";
    private static final String EXTRA_QUOTE = "EXTRA_QUOTE";
    private static final String EXTRA_ATTR = "EXTRA_ATTR";
    private static final String EXTRA_ARR = "EXTRA_ARR";
    private static final String EXTRA_CAT1 = "EXTRA_CAT1";
    private static final String EXTRA_CAT2 = "EXTRA_CAT2";
    private static final String EXTRA_ID = "EXTRA_ID";
    //------------------------------------------------------------------------------- other variables (item id, photo holders)
    int itemId;
    byte[] standartCheck;
    byte[] setNoPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);
        //-------------------------------------------------------------------------------------------------------- get extras
        Bundle extras = getIntent().getBundleExtra(BUNDLE_EXTRAS);
        byte[] img = extras.getByteArray(EXTRA_ARR);
        image = BitmapFactory.decodeByteArray(img, 0, img.length);
        desc = extras.getString(EXTRA_ATTR);
        title = extras.getString(EXTRA_QUOTE);
        catNames = extras.getString(EXTRA_CAT1);
        catDetails = extras.getString(EXTRA_CAT2);
        collName = extras.getString(EXTRA_ID);
        //------------------------------------------------------------------------------------------------------------ get db
        db = new MyDBHandler(getApplicationContext());
        //------------------------------------------------------------------------------------------- spinner, spinner adapter
        spinner = (Spinner) findViewById(R.id.spinner_collections);
        spAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, names);
        //----------------------------------------------------------------------------------------------------------- get item
        itemId = db.getItemIdByName(title);
        item = db.getItem(itemId);
        //-------------------------------- split categories string if not null to array and create textviews editTexts for them
        if (catNames != null && catDetails != null) {
            String str[] = catNames.split(", ");
            str = correct(str);
            categoriesList = java.util.Arrays.asList(str);

            for (int i = 0; i < categoriesList.size(); i++) {

                EditText editText = new EditText(getApplicationContext());
                TextView textView = new TextView(getApplicationContext());

                linearLayout = (LinearLayout)findViewById(R.id.linear) ;

                linearLayout.addView(textView);
                textView.setText(categoriesList.get(i));
                textView.setPadding(20,10,20,0);
                textView.setTextColor(Color.parseColor("#353535"));

                String str1[] = catDetails.split(", ");
                str1 = correct(str1);
                categoriesList2 = java.util.Arrays.asList(str1);


                linearLayout.addView(editText);
                editText.setHint(categoriesList2.get(i));
                editText.setPadding(20, 10, 20, 10);
                editText.setTextColor(Color.parseColor("#757575"));
                allEditText.add(editText);
            }

        }


        linearLayout = (LinearLayout) findViewById(R.id.linear);
        itemDesc = (EditText) findViewById(R.id.itemDesc);
        itemName = (EditText) findViewById(R.id.itemName);


        save = (Button) findViewById(R.id.save);
        cancel = (Button) findViewById(R.id.cancel);

        addImage = (ImageView) findViewById(R.id.addImage);
        itemTitle = (EditText)findViewById(R.id.itemName);
        itemD = (EditText)findViewById(R.id.itemDesc);
        addImage.setImageBitmap(image);
        itemD.setText(desc);
        itemTitle.setText(title);

        //--------------------------------------------------------------------------------------------------- CANCEL BUTTON
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = makeIntent(UpdateItem.this, collName);
                startActivity(intent);
                finish();
            }
        });
        //------------------------------------------------------------------------------------ image from image view to byte
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.pink_add_photo);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        standartCheck = stream.toByteArray();
        //--------------------------------------------------------------------- preferred image if photo not captured to byte
        Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(), R.drawable.color_middle_blue);
        ByteArrayOutputStream stream1 = new ByteArrayOutputStream();
        bitmap1.compress(Bitmap.CompressFormat.PNG, 100, stream1);
        setNoPhoto = stream1.toByteArray();
        //------------------------------------------------------------------------------------------------------- SAVE BUTTON
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //-------------------------------------------------------------------if categories not null !! to avoid ( [] )
               if(catNames != null) {
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
                //-----------------------------------------------------------------------check if name and description filled
                if (itemName.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Item not updated You must enter the name.", Toast.LENGTH_SHORT).show();
                } else if (itemDesc.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Item not updated You must enter the description.", Toast.LENGTH_SHORT).show();
                }
                //----------------------------------------------------------------adding this to db if user did not added img
                else if (Arrays.equals(imageViewToByte(addImage), standartCheck)){
                    item.setTitle(itemTitle.getText().toString());
                    item.setSubTitle(itemD.getText().toString());
                    item.setItemCatText(catResult);
                    item.setImage(imageViewToByte(addImage));
                    db.updateItem(item);
                    Toast.makeText(getApplicationContext(), "item updated" + item.getId(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "item updated", Toast.LENGTH_SHORT).show();
                    spAdapter.notifyDataSetChanged();
                    //--------------------------------------------------------------------- start new listActivity with changes
                    Intent intent = makeIntent(UpdateItem.this, collName);
                    startActivity(intent);
                    finish();
                }

                else {
                    //-----------------------------------------------------------------------add this to db if user added photo
                    item.setTitle(itemTitle.getText().toString());
                    item.setSubTitle(itemD.getText().toString());
                    item.setItemCatText(catResult);
                    item.setImage(imageViewToByte(addImage));
                    db.updateItem(item);
                    Toast.makeText(getApplicationContext(), "item updated" + item.getId(), Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "item updated", Toast.LENGTH_SHORT).show();
                    spAdapter.notifyDataSetChanged();
                    //---------------------------------------------------------------------- start new listActivity with changes
                    Intent intent = makeIntent(UpdateItem.this, collName);
                    startActivity(intent);
                    finish();
                }
            }
        });
        //---------------------------------------------------------------------------- call method for spinner to shoe coll names
        populateColNames();
    }
    //------------------------------------------------------------------------------------- method for spinner to shoe coll names
    public void populateColNames() {
        Cursor cursor = db.getAllValues();

        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            names.add(name);
        }
        spinner.setAdapter(spAdapter);
        spinner.setSelection(spAdapter.getPosition(collName));
    }
    //-------------------------------------------------------------------------------------------------- method for taking photo
    public void launchCamera(View view) throws IOException {
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Calendar cal = Calendar.getInstance();
        imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), (cal.getTimeInMillis() + ".jpg"));
        uri.fromFile(imageFile);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        camera.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(camera, REQUEST_TAKE_PHOTO);
    }
    //----------------------------------------------------------------------------------------- what to do wit image capture result
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            addImage.setImageBitmap(photo);
        }
    }
    //--------------------------------------------------------------------------------------------------- convert imageView to byte[]
    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }
    //--------------------------------------------------------------------------------------------------- fix [] in categories arrays
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
    //---------------------------------------------------------------------------------------------------------------- on back pressed
    @Override
    public void onBackPressed(){
        Intent intent = makeIntent(UpdateItem.this, collName);
        startActivity(intent);
        finish();
    }
    //-------------------------------------------------------------------------------------------------- what to do with selected item
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }
    //--------------------------------------------------------------------------------------- what to do if nothing in spinner selected
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    //---------------------------------------------------------------------------------------------- method for intent to list activity
    public static Intent makeIntent(Context context, String nameofcol) {
        Intent intent = new Intent(context, ListActivity.class);
        intent.putExtra("name_of_collection", nameofcol);
        return intent;
    }
}
