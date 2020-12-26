package com.example.vluadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView imgBook,imgNeck,imgBalo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        matching();

        imgBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddnewProduct.class);
                intent.putExtra("category","Books");
                startActivity(intent);
            }
        });
        imgNeck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddnewProduct.class);
                intent.putExtra("category","Necks");
                startActivity(intent);
            }
        });
        imgBalo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this,AdminAddnewProduct.class);
                intent.putExtra("category","Balos");
                startActivity(intent);
            }
        });

    }

    private void matching() {
        imgBalo = (ImageView) findViewById(R.id.imgBalo);
        imgNeck = (ImageView) findViewById(R.id.imgNeck);
        imgBook = (ImageView) findViewById(R.id.imgBook);
    }

}