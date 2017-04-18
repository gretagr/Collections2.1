package com.collections2.grigelionyte.greta.collections.ui.addEdit;

import android.app.Activity;
import android.content.Intent;
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

import java.io.File;
import java.util.ArrayList;


public class NewCollection extends AppCompatActivity {

    public static final int REQUEST_TAKE_PHOTO = 1;
    Button cancel, save, addCategoyBtn;
    EditText addCategoryField, itemName, itemDesc;
    ListView categoriesListview;
    ArrayList<String> categoriesList;
    ArrayAdapter<String> categoriesadapter;
    ImageView addImage;
    String imageName = "collections";
    File imageFile;
    Uri uri;
    MyDBHandler db;
    String ARRAY_DIVIDER = "#a1r2ra5yd2iv1i9der";
    static Uri capturedImageUri;
    String mCurrentPhotoPath;
    private File getImageFile;

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
             Uri tempUri = Uri.fromFile(imageFile);
                   if (itemName.getText().toString().isEmpty()) {
                       Toast.makeText(getApplicationContext(), "Collection not created. You must enter the name.", Toast.LENGTH_SHORT).show();
                   } else if (itemDesc.getText().toString().isEmpty()) {
                       Toast.makeText(getApplicationContext(), "Collection not created. You must enter the description.", Toast.LENGTH_SHORT).show();
                   }
                   else {
                       ItemsCollection collection = new ItemsCollection(String.valueOf(itemName.getText()), String.valueOf(itemDesc.getText()), tempUri, categoriesList.toString());
                       db.addCollection(collection);
                       Toast.makeText(getApplicationContext(), "new collection created", Toast.LENGTH_SHORT).show();
                       Intent home = new Intent(NewCollection.this, CollectionsActivity.class);
                       startActivity(home);
                    }
                }
                });

                addCat();
                deleteCat();

    }
    public void launchCamera(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imageFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "test.jpg");
        Uri tempUri = Uri.fromFile(imageFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, 1);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode==1){
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
            public void addCat() {
                addCategoyBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String result = addCategoryField.getText().toString();
                        categoriesList.add(result);
                        categoriesadapter.notifyDataSetChanged();
                        addCategoryField.setText("");

                    }

                });
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