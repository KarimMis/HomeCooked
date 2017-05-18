package com.homecooked.karim.homecooked.CookerInsertsData;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;

import com.homecooked.karim.homecooked.R;
import com.homecooked.karim.homecooked.model.FoodDish;

public class InsertCookerDishInfo extends AppCompatActivity implements View.OnClickListener {


    private ImageView imageView;

    private EditText dish_name;
    private EditText dish_weight;
    private EditText dish_prepared;
    private EditText dish_price;
    private EditText dish_ingredients;
    private EditText dish_about;
    private EditText dish_serves;

    private Button buttonChoose;
    private Button buttonUpload;

    private Bitmap bitmap;
    Uri imgUri;

    private int PICK_IMAGE_REQUEST = 1;
    private ProgressDialog progressDialog;

    private StorageReference filepath;
    private StorageReference mStorage;
    private DatabaseReference mDatabaseRef;

    public static final String FIREBASE_STORAGE_PATH = "dishphoto/"; // name of storage file of images
    public static final String FIREBASE_DATABASE_PATH = "CookerInfo";  // name of the parent root of ur db file

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_cooker_dish_info);


        mStorage= FirebaseStorage.getInstance().getReference(); // get firebase STORAGE reference
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FIREBASE_DATABASE_PATH); // get firebase DB refrence

        progressDialog = new ProgressDialog(this);
        buttonChoose = (Button) findViewById(R.id.buttonChoose_dishimage);
        buttonUpload = (Button) findViewById(R.id.buttonUpload_dishimage);

        dish_name = (EditText) findViewById(R.id.dish_name);
        dish_ingredients = (EditText) findViewById(R.id.dish_ingredients);
        dish_about = (EditText) findViewById(R.id.dish_about);
        dish_serves = (EditText) findViewById(R.id.dish_serve);
        dish_weight = (EditText) findViewById(R.id.dish_weight);
        dish_prepared = (EditText) findViewById(R.id.dish_prepared);
        dish_price = (EditText) findViewById(R.id.dish_price);

        imageView = (ImageView) findViewById(R.id.imageView);


        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        if (v == buttonChoose) {
            showFileChooser();
        }

        if (v == buttonUpload) {
            if (imgUri != null) { // لو فيه كل الداتا نفذ
                InsertDishInfo();
            }
        }
    }


    // بيفتح الليسته عشان المستخدم يختار الصوره منين
    //Now we will create a method to choose image from gallery. Create the following method.
    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }



    // اختار المستخدم الصوره
    //To complete the image choosing process we need to override the following method.
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            imgUri = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imgUri);
                //Setting the Bitmap to ImageView
                imageView.setImageBitmap(bitmap);



            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }




    @SuppressWarnings("VisibleForTests")
    public void InsertDishInfo(){

        // put the image in the firebas STORAGE with unique name for image
        filepath = mStorage.child(FIREBASE_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(imgUri));

        progressDialog.setMessage("Uploading");
        progressDialog.setCancelable(false);
        progressDialog.show();
        // check if image uploades successufuly to firebase
        filepath.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {


            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                // get cooker info from views
                FoodDish foodDishInfo = new FoodDish(

                        dish_name.getText().toString(),
                        dish_ingredients.getText().toString(),
                        dish_about.getText().toString(),
                        dish_serves.getText().toString(),
                        dish_weight.getText().toString(),
                        dish_price.getText().toString(),
                        dish_prepared.getText().toString(),
                        taskSnapshot.getDownloadUrl().toString()
                        );

                String userId = getIntent().getStringExtra("USER ID");



                String key = mDatabaseRef.child("cookerDishes").push().getKey();
                mDatabaseRef.child(userId).child("cookerDishes").child(key).setValue(foodDishInfo);


                progressDialog.dismiss();

                Toast.makeText(InsertCookerDishInfo.this,"Success",Toast.LENGTH_LONG).show();

                finish();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                //Dimiss dialog when error
                progressDialog.dismiss();
                //Display err toast msg
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                //Show upload progress

                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                progressDialog.setMessage("Uploading  " + (int) progress + "%");
            }
        });


    }

    public String getImageExt(Uri uri) { // give unique name for the image
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
}