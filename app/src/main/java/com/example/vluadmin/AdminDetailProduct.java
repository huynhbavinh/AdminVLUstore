package com.example.vluadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AdminDetailProduct extends AppCompatActivity {
    EditText pname,price,pdes;
    ImageView img;
    String d_pname,d_price,d_des,d_img;
    Button btnUpload,btnDel;
    String keyProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_detail_product);

        img = findViewById(R.id.imageView2);
        pname = findViewById(R.id.detail_pname);
        pdes = findViewById(R.id.detail_des);
        price = findViewById(R.id.detail_price);
        btnUpload = findViewById(R.id.detail_btnUpdate);
        btnDel= findViewById(R.id.btnDel);

        getData();
        setData();
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadData();
            }
        });
        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DeletedProduct();
            }
        });


    }

    private void getData(){
        try {
            if (getIntent().hasExtra("pname")
                    && getIntent().hasExtra("price")
                    && getIntent().hasExtra("des")
                    && getIntent().hasExtra("img")
                    && getIntent().hasExtra("day_create")
                    && getIntent().hasExtra("time_create"))
            {
                d_pname = getIntent().getStringExtra("pname");
                d_price = getIntent().getStringExtra("price");
                d_des = getIntent().getStringExtra("des");
                d_img = getIntent().getStringExtra("img");
                keyProduct = getIntent().getStringExtra("day_create") + getIntent().getStringExtra("time_create") ;

            }else {
                Log.d("loi", "getData: Fails ");
            }
        }catch (Exception e){
            e.getMessage();
        }
    }
    private void setData(){
        Glide.with(this)
                .load(d_img)
                .into(img);
        pname.setText(d_pname);
        price.setText(d_price);
        pdes.setText(d_des);


    }
    private void uploadData() {
        try {

            FirebaseDatabase database = FirebaseDatabase.getInstance();

            DatabaseReference myRef = database.getReference("Products");

            String id=keyProduct;

            String u_name = pname.getText().toString();
            String u_price = price.getText().toString();
            String u_des = pdes.getText().toString();

            myRef.child(id).child("pname").setValue(u_name);
            myRef.child(id).child("price").setValue(u_price);
            myRef.child(id).child("description").setValue(u_des);
            Toast.makeText(this, "Success for Upload", Toast.LENGTH_SHORT).show();
            finish();
        }catch (Exception e){
            Log.d("loi", "uploadData: "+ e.getMessage());
            Toast.makeText(this, "Failed for Upload", Toast.LENGTH_SHORT).show();
        }

    }
    private void DeletedProduct(){
       try {
           String id=keyProduct;
           FirebaseDatabase database = FirebaseDatabase.getInstance();
           //Kết nối tới node có tên là contacts (node này do ta định nghĩa trong CSDL Firebase)
           DatabaseReference myRef = database.getReference("Products");
           myRef.child(id).removeValue();
           Toast.makeText(this, "Success for deleted", Toast.LENGTH_SHORT).show();
           finish();

       }catch (Exception e){
           Toast.makeText(this, "Failed for deleted", Toast.LENGTH_SHORT).show();
       }

    }

}