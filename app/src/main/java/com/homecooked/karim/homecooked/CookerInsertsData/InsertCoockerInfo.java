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

import com.homecooked.karim.homecooked.ShowSingleCookerInfo;
import com.homecooked.karim.homecooked.R;
import com.homecooked.karim.homecooked.model.Cooker;

public class InsertCoockerInfo extends AppCompatActivity implements View.OnClickListener {

    private Button buttonChoose;
    private Button buttonUpload;
    private ProgressDialog progressDialog;
    private ImageView imageView;

    private EditText coooker_name;
    private EditText cooker_phoneNumber;
    private EditText cooker_available;
    private EditText cooker_about;
    private EditText cooker_address;

    private Bitmap bitmap;
     public  String uploadId;
    public static final String FIREBASE_STORAGE_PATH = "cookerPhoto/"; // name of storage file of images
    public static final String FIREBASE_DATABASE_PATH = "CookerInfo";  // name of the parent root of ur db file
    Uri imgUri; //image context path

    private StorageReference filepath;
    private StorageReference mStorage;
    private DatabaseReference mDatabaseRef;


    private int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.data_insert_cooker);

        mStorage= FirebaseStorage.getInstance().getReference(); // get firebase STORAGE reference
        mDatabaseRef = FirebaseDatabase.getInstance().getReference(FIREBASE_DATABASE_PATH); // get firebase DB refrence

        progressDialog = new ProgressDialog(this);
        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);

        coooker_name = (EditText) findViewById(R.id.cooker_name);
        cooker_phoneNumber = (EditText) findViewById(R.id.cooker_number);
        cooker_available = (EditText) findViewById(R.id.cooker_avaiabilty);
        cooker_about = (EditText) findViewById(R.id.cooker_about);
        cooker_address = (EditText) findViewById(R.id.cooker_address);

        imageView = (ImageView) findViewById(R.id.imageView);

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);
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



    @Override
    public void onClick(View v) {

        if(v == buttonChoose){
            showFileChooser();
        }

        if(v == buttonUpload){
            if (imgUri != null  ) { // لو فيه كل الداتا نفذ
                uploadImageTofireBase();
            }
            else if (imgUri == null ) { // لو مفيش داتا
                Toast.makeText(InsertCoockerInfo.this,"please insert all the required",Toast.LENGTH_LONG).show();
            }
        }
    }




        @SuppressWarnings("VisibleForTests")
        public void uploadImageTofireBase(){

           // put the image in the firebas STORAGE with unique name for image
           filepath = mStorage.child(FIREBASE_STORAGE_PATH + System.currentTimeMillis() + "." + getImageExt(imgUri));

            progressDialog.setMessage("Uploading");
            progressDialog.setCancelable(false);
            progressDialog.show();

            // the USER AUTHENTICATION ID
            final String userID = getIntent().getStringExtra("USER-ID");


            // check if image uploades successufuly to firebase
            filepath.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    // get cooker info from views
                    Cooker cookerInfo = new Cooker(
                            userID,
                            taskSnapshot.getDownloadUrl().toString(),
                            coooker_name.getText().toString(),
                            cooker_phoneNumber.getText().toString(),
                            cooker_available.getText().toString(),
                            cooker_about.getText().toString(),
                            cooker_address.getText().toString()
                           );


                   // Make our DB ID equal To USER AUTHENTICATION ID
                    mDatabaseRef.child(userID).setValue(cookerInfo);

                    // Send the DB ID
                    uploadId= userID;

                    progressDialog.dismiss();

                    Toast.makeText(InsertCoockerInfo.this,"Success",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(InsertCoockerInfo.this, ShowSingleCookerInfo.class);
                    intent.putExtra("InsertedIDtoDB",uploadId);
                    startActivity(intent);
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