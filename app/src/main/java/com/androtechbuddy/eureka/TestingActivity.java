package com.androtechbuddy.eureka;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

public class TestingActivity extends AppCompatActivity {

    public static final String TAG = "TAG";
    ImageView upload;
    Uri imageuri = null;

    FirebaseAuth fAuth;
    FirebaseFirestore db;
    String userId;
    FirebaseUser document;
    StorageReference storageReference;

    Button upload_pdf;
    TextView failed,successful;



    String myurl = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testing);
        getSupportActionBar().setTitle("Upload Abstract");

        upload = findViewById(R.id.uploadpdf);


        fAuth = FirebaseAuth.getInstance();
        document = fAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();


        successful = findViewById(R.id.successful);
        failed = findViewById(R.id.failed);

//        Email = findViewById(R.id.received_email);
//        pdf_url = findViewById(R.id.pdf_url_txt);
        upload_pdf = findViewById(R.id.upload_pdf_btn);
        if(myurl == null){
               upload_pdf.setEnabled(false);
            upload_pdf.setBackgroundResource(R.drawable.btn_rounded_disabled);
        }

        String dbValue = db.toString();
        upload_pdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebasedataupdate();
            }
        });
        Intent data = getIntent();
        String email = data.getStringExtra("email");
//        String paper_url = data.getStringExtra("Paper_URL");



        // After Clicking on this we will be
        // redirected to choose pdf
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                // We will be redirected to choose pdf
                galleryIntent.setType("application/pdf");
                startActivityForResult(galleryIntent, 1);
            }
        });

    }

    ProgressDialog dialog;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {

            // Here we are initialising the progress dialog box
            dialog = new ProgressDialog(this);
            dialog.setTitle("File is Selected");
            dialog.setMessage("Please Wait...");

            // this will show message uploading
            // while pdf is uploading
            dialog.show();
            imageuri = data.getData();
//            final String timestamp = "" + System.currentTimeMillis();


//            String paper_url = imageuri.toString();


            final String pdfname = "users-pdf/"+ fAuth.getCurrentUser().getUid() +"/paperpdf";


            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            final String messagePushID = pdfname;
            //Toast.makeText(TestingActivity.this, imageuri.toString(), Toast.LENGTH_SHORT).show();


            // Here we are uploading the pdf in firebase storage with the name of current time
            final StorageReference filepath = storageReference.child(messagePushID + "." + "pdf");
            Toast.makeText(TestingActivity.this, filepath.getName(), Toast.LENGTH_SHORT).show();



            filepath.putFile(imageuri).continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return filepath.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        // After uploading is done it progress
                        // dialog box will be dismissed
                        dialog.dismiss();
                        Uri uri = task.getResult();

                        myurl = uri.toString();
                        upload_pdf.setEnabled(true);
                        upload_pdf.setBackgroundResource(R.drawable.btn_rounded);
//                        Toast.makeText(TestingActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        dialog.dismiss();
                        //Toast.makeText(TestingActivity.this, "UploadedFailed", Toast.LENGTH_LONG).show();
                        failed.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }
    private void firebasedataupdate() {


//        db.collection("users").document(user.id).update("Remark", user);

        db.collection("users")
                .document(document.getUid())
                .update("Paper_URL", myurl);
        //Toast.makeText(TestingActivity.this, "Uploaded Successfully", Toast.LENGTH_LONG).show();

        final ProgressDialog progress = new ProgressDialog(TestingActivity.this);
        progress.setTitle("Profile Data Updating...");
        progress.setMessage("Please wait..");
        progress.show();

        Runnable progressRunnable = new Runnable() {
            @Override
            public void run() {
                progress.cancel();
                successful.setVisibility(View.VISIBLE);
            }
        };
        Handler pdCanceller = new Handler();
        pdCanceller.postDelayed(progressRunnable, 3000);


    }

}
