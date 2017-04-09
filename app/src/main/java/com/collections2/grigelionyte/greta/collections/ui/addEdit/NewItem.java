package com.collections2.grigelionyte.greta.collections.ui.addEdit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.collections2.grigelionyte.greta.collections.R;
import com.collections2.grigelionyte.greta.collections.ui.main.CollectionsActivity;

public class NewItem extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_item);

        Button cancel = (Button)findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cancel = new Intent(NewItem.this, CollectionsActivity.class);
                startActivity(cancel);
            }
        });


    }
}
