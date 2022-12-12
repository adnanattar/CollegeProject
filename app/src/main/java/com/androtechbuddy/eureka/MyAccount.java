package com.androtechbuddy.eureka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;



public class MyAccount extends AppCompatActivity {
    TextView AppUpdateLink,UpdateName,fullName,email,phone,verifyMsg, get_college_name,state, district,department,year, get_paper_name,note_verify, get_remark, get_status, get_CoFullName, get_CoEmail, get_CoCollegeName;
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String userId;
    String UpadatedAppLink, GetLinkFromStorage;
    Button resendCode;
    Button resetPassLocal,changeProfileImage,cochnageProfile;
    FirebaseUser user;
    ImageView profileImage;
    StorageReference storageReference;

    TextView CoFullNameHead,CoEmailHead,CoCollegeNameHaed;
    LinearLayout CoAuthorName_Layout,CoAuthorEmail_Layout,CoAuthorCollegeName_Layout;
    String CoFullName,CoEmail,CoCollegeName;

    private View parent_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);
        getSupportActionBar().setTitle("My Account");

        parent_view = findViewById(android.R.id.content);

        AppUpdateLink = findViewById(R.id.AppUpdateLink);
        UpdateName = findViewById(R.id.UpdateName);

        phone = findViewById(R.id.profilePhone);
        fullName = findViewById(R.id.profileName);
        email    = findViewById(R.id.profileEmail);

        get_paper_name = findViewById(R.id.paper_name);
        get_college_name = findViewById(R.id.college_name);

        get_remark = findViewById(R.id.Remark);
        get_status = findViewById(R.id.status);

        state = findViewById(R.id.state);
        district = findViewById(R.id.district);
        department = findViewById(R.id.department);
        year = findViewById(R.id.year);
        resetPassLocal = findViewById(R.id.resetPasswordLocal);
        profileImage = findViewById(R.id.profileImage);
        changeProfileImage = findViewById(R.id.changeProfile);

        cochnageProfile = findViewById(R.id.co_changeProfile);

        //Co-Author
        get_CoFullName = findViewById(R.id.co_authors_fName);
        get_CoEmail = findViewById(R.id.co_authors_profileEmail);
        get_CoCollegeName = findViewById(R.id.co_author_college_name);
        CoFullNameHead = findViewById(R.id.co_Author_FullName_Heading);
        CoEmailHead = findViewById(R.id.co_Author_Email_Heading);
        CoCollegeNameHaed = findViewById(R.id.co_Author_CollegeName_Heading);

        //LinearLayout FindView
        CoAuthorName_Layout = findViewById(R.id.CoAuthorName_Layout);
        CoAuthorEmail_Layout = findViewById(R.id.CoAuthorEmail_Layout);
        CoAuthorCollegeName_Layout = findViewById(R.id.CoAuthorCollegeName_Layout);

        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        StorageReference profileRef = storageReference.child("users/"+fAuth.getCurrentUser().getUid()+"/profile.jpg");
        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profileImage);
            }
        });

        resendCode = findViewById(R.id.resendCode);
        verifyMsg = findViewById(R.id.verifyMsg);
        note_verify = findViewById(R.id.note_verify);

        userId = fAuth.getCurrentUser().getUid();
        user = fAuth.getCurrentUser();

        if(!user.isEmailVerified() ){
            verifyMsg.setVisibility(View.VISIBLE);
            resendCode.setVisibility(View.VISIBLE);
            note_verify.setVisibility(View.VISIBLE);

            resendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {

                    user.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Snackbar.make(parent_view, "Verification Email Has Been Sent. Please Check Your Mail Box", Snackbar.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.d("tag", "onFailure: Email not sent " + e.getMessage());
                        }
                    });
                }
            });
        }else
        {
            verifyMsg.setVisibility(View.GONE);
            resendCode.setVisibility(View.GONE);
            note_verify.setVisibility(View.GONE);
        }




        DocumentReference documentReference = fStore.collection("users").document(userId);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    phone.setText(documentSnapshot.getString("phone"));
                    fullName.setText(documentSnapshot.getString("fName"));
                    email.setText(documentSnapshot.getString("email"));
                    get_paper_name.setText(documentSnapshot.getString("PaperName"));
                    get_college_name.setText(documentSnapshot.getString("collegeName"));
                    state.setText(documentSnapshot.getString("state"));
                    district.setText(documentSnapshot.getString("district"));
                    department.setText(documentSnapshot.getString("department"));
                    year.setText(documentSnapshot.getString("year"));
                    get_remark.setText(documentSnapshot.getString("Remark"));
                    get_status.setText(documentSnapshot.getString("Status"));


                    //Co-Author
                    get_CoFullName.setText(documentSnapshot.getString("Co_AuthorName"));
                    get_CoEmail.setText(documentSnapshot.getString("Co_Author_Email"));
                    get_CoCollegeName.setText(documentSnapshot.getString("Co_Author_College"));


                }else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });

//        CoFullName = get_CoFullName.getText().toString();
//
//        CoEmail = get_CoEmail.getText().toString();
//
//        CoCollegeName = get_CoCollegeName.getText().toString();
//
//
//
//        if (CoFullName == "" ){
//            get_CoFullName.setVisibility(View.GONE);
//            CoFullNameHead.setVisibility(View.GONE);
//            CoAuthorName_Layout.setVisibility(View.GONE);
//        }if(CoEmail == ""){
//            get_CoEmail.setVisibility(View.GONE);
//            CoEmailHead.setVisibility(View.GONE);
//            CoAuthorEmail_Layout.setVisibility(View.GONE);
//        }if(CoCollegeName == ""){
//            get_CoCollegeName.setVisibility(View.GONE);
//            CoCollegeNameHaed.setVisibility(View.GONE);
//            CoAuthorCollegeName_Layout.setVisibility(View.GONE);
//        }

        DocumentReference updateReferenace = fStore.collection("updates").document("AppUpdate");
        updateReferenace.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                if(documentSnapshot.exists()){
                    AppUpdateLink.setText(documentSnapshot.getString("LatestUpdateLink"));
                    UpdateName.setText(documentSnapshot.getString("UpdateName"));
                }else {
                    Log.d("tag", "onEvent: Document do not exists");
                }
            }
        });

        UpadatedAppLink = AppUpdateLink.getText().toString();

        resetPassLocal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText resetPassword = new EditText(v.getContext());

                final AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                passwordResetDialog.setTitle("Reset Password");
                passwordResetDialog.setMessage("Enter new password it must contains at least 6 characters");
                passwordResetDialog.setView(resetPassword);

                passwordResetDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // extract the email and send reset link
                        String newPassword = resetPassword.getText().toString();
                        user.updatePassword(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(MyAccount.this, "Password Reset Successfully.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(MyAccount.this, "Password Reset Failed.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // close
                    }
                });

                passwordResetDialog.create().show();

            }
        });

        changeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open gallery
                Intent i = new Intent(v.getContext(),EditProfile.class);
                i.putExtra("fName",fullName.getText().toString());
                i.putExtra("email",email.getText().toString());
                i.putExtra("phone",phone.getText().toString());

                i.putExtra("PaperName", get_paper_name.getText().toString());
                i.putExtra("collegeName", get_college_name.getText().toString());
                i.putExtra("state",state.getText().toString());
                i.putExtra("district", district.getText().toString());
                i.putExtra("department",department.getText().toString());
                i.putExtra("year",year.getText().toString());

                i.putExtra("Remark",get_remark.getText().toString());
                i.putExtra("Status",get_status.getText().toString());

                //Co-Author
                i.putExtra("Co_AuthorName",get_CoFullName.getText().toString());
                i.putExtra("Co_Author_Email",get_CoEmail.getText().toString());
                i.putExtra("Co_Author_College",get_CoCollegeName.getText().toString());
                startActivity(i);
            }
        });

        cochnageProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // open gallery
                Intent i = new Intent(v.getContext(),CoAuthor_profile_update.class);

                i.putExtra("email",email.getText().toString());
                //Co-Author
                i.putExtra("Co_AuthorName",get_CoFullName.getText().toString());
                i.putExtra("Co_Author_Email",get_CoEmail.getText().toString());
                i.putExtra("Co_Author_College",get_CoCollegeName.getText().toString());
                startActivity(i);
            }
        });
    }

    public void UpdateApp(View view){
//        final String Checkupdate = UpadatedAppLink;
//        if(UpadatedAppLink != Checkupdate) {
        if (UpadatedAppLink != null) {

//            Intent intent = new Intent( MyAccount.this, WebView.class);
//            intent.putExtra("links", AppUpdateLink.getText().toString());
//            startActivity(intent);

            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(Color.parseColor("#FFBB86FC"));
            openCustomTabs(MyAccount.this,builder.build(),Uri.parse(AppUpdateLink.getText().toString()));
        } else
            Toast.makeText(MyAccount.this, "No Any Update Available", Toast.LENGTH_LONG).show();
//        }
    }


    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();//logout
        Intent i =new Intent(getApplicationContext(),Login.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra("EXIT", true);
        startActivity(i);
        finish();
    }

    //Custome Tab
    public static void openCustomTabs(Activity activity, CustomTabsIntent customTabsIntent, Uri uri){
        String PackageName = "com.android.chrome";
        if(PackageName != null){
            customTabsIntent.intent.setPackage(PackageName);
            customTabsIntent.launchUrl(activity,uri);
        }else{
            activity.startActivity(new Intent(Intent.ACTION_VIEW, uri));
        }
    }

}
