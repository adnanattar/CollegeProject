package com.androtechbuddy.eureka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class CoAuthor_profile_update extends AppCompatActivity {

    public static final String TAG = "TAG";
    EditText profileCoFullName, profileCoEmail, profileCoCollegeName, CoPic;

    static ImageView profileImageView;
    Button saveBtn;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    FirebaseUser user;
    StorageReference storageReference;
    String  CoProfile_IMG;
    TextView profileEmail;
    private View parent_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_co_author_profile_update);
        getSupportActionBar().setTitle("Edit Co-Author Profile");

        Intent data = getIntent();
        String email = data.getStringExtra("email");
        //Co Athour

        parent_view = findViewById(android.R.id.content);
        profileCoFullName = findViewById(R.id.co_fullName_edit);
        profileCoEmail = findViewById(R.id.co_author_email_edit);
        profileCoCollegeName = findViewById(R.id.co_college_name_edit);
        profileImageView = findViewById(R.id.profileImageView);

        saveBtn = findViewById(R.id.saveProfileInfo);
        profileEmail = findViewById(R.id.Email);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        user = fAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        //Co-Author
        String CoFullName = data.getStringExtra("Co_AuthorName");
        String CoEmail = data.getStringExtra("Co_Author_Email");
        String CoCollegeName = data.getStringExtra("Co_Author_College");


        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/coprofile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImageView);
                CoProfile_IMG = String.valueOf(uri);
            }
        });

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent,1000);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = profileEmail.getText().toString();
                user.updateEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        DocumentReference docRef = fStore.collection("users").document(user.getUid());
                        Map<String, Object> edited = new HashMap<>();
                        edited.put("email", email);
                        edited.put("Co_AuthorName", profileCoFullName.getText().toString());
                        edited.put("Co_Author_Email", profileCoEmail.getText().toString());
                        edited.put("Co_Author_College", profileCoCollegeName.getText().toString());
                        edited.put("Co_Author_Profile_IMG", CoProfile_IMG);

                        docRef.update(edited).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                //Toast.makeText(EditProfile.this, "Profile Image Updated", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(getApplicationContext(),my_account.class));
//                                finish();
                            }
                        });
                     Snackbar.make(parent_view, "Profile Data Updated Successfully", Snackbar.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CoAuthor_profile_update.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        profileEmail.setText(email);
        profileCoFullName.setText(CoFullName);
        profileCoEmail.setText(CoEmail);
        profileCoCollegeName.setText(CoCollegeName);

        Log.d(TAG, "onCreate: " + email + " " + CoFullName + " " + CoEmail + " " + CoCollegeName + " " + CoProfile_IMG);
    }


        protected void onActivityResult(int requestCode, int resultCode, @androidx.annotation.Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == 1000){
                if(resultCode == Activity.RESULT_OK){
                    Uri uri = data.getData();
//                Uri imageUri = data1.getData();
                    //profileImage.setImageURI(imageUri);

                    uploadImageToFirebase(uri);

                }
            }
        }

        private void uploadImageToFirebase(Uri uri) {
            // uplaod image to firebase storage
//        if(profileImageView.isSelected()){
            StorageReference fileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/coprofile.jpg");
            fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Picasso.get().load(uri).into(profileImageView);
                        }
                    });
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "Failed.", Toast.LENGTH_SHORT).show();
                }
            });

        }


}