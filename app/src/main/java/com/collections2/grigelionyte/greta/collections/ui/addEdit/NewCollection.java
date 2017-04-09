package com.collections2.grigelionyte.greta.collections.ui.addEdit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.collections2.grigelionyte.greta.collections.R;
import com.collections2.grigelionyte.greta.collections.ui.main.CollectionsActivity;

public class NewCollection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_collection);

        Button cancel = (Button)findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancel = new Intent(NewCollection.this, CollectionsActivity.class);
                startActivity(cancel);
            }
        });
    }
}
