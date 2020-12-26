package com.example.vluadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class AdminAddnewProduct extends AppCompatActivity {
    private String categoryName,productName,productDes,productPrice,saveCurrentDate, saveCurrentTime;

    private EditText txtName,txtPrice,txtDes;
    private ImageView inputIMG;
    private Button btnAdd;
    private static int GALLERY_PICK = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;

    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;

    public AdminAddnewProduct() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_addnew_product);

        categoryName = getIntent().getExtras().get("category").toString();

        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        matching();

        inputIMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });

    }

    private void openGallery() {
        Intent GalleryIntent = new Intent();
        GalleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        GalleryIntent.setType("image/*");
        startActivityForResult(GalleryIntent,GALLERY_PICK);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==GALLERY_PICK  &&  resultCode==RESULT_OK  &&  data!=null)
        {
            ImageUri = data.getData();
            inputIMG.setImageURI(ImageUri);
        }
    }
    private void ValidateProductData()
    {
        productName = txtName.getText().toString();
        productPrice = txtPrice.getText().toString();
        productDes = txtDes.getText().toString();

        if (ImageUri == null)
        {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(productDes))
        {
            Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(productPrice))
        {
            Toast.makeText(this, "Please write product Price...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(productName))
        {
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInformation();
        }
    }
    private void StoreProductInformation()
    {

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e)
            {
                String message = e.toString();
                Toast.makeText(AdminAddnewProduct.this, "Error: " + message, Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                Toast.makeText(AdminAddnewProduct.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful())
                        {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AdminAddnewProduct.this, "got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });

    }
    private void SaveProductInfoToDatabase()
    {
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", productDes);
        productMap.put("image", downloadImageUrl);
        productMap.put("category", categoryName);
        productMap.put("price", productPrice);
        productMap.put("pname", productName);

        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful())
                        {
                            Intent intent = new Intent(AdminAddnewProduct.this, AdminCategoryActivity.class);
                            startActivity(intent);

                            Toast.makeText(AdminAddnewProduct.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddnewProduct.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void matching(){
        txtName = (EditText) findViewById(R.id.txtProductname);
        txtPrice = (EditText) findViewById(R.id.txtProductPrice);
        txtDes = (EditText) findViewById(R.id.txtProductDes);
        btnAdd = (Button) findViewById(R.id.btnAdd);
        inputIMG = (ImageView) findViewById(R.id.input_img);

    }

}