package com.androtechbuddy.eureka;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Registration extends AppCompatActivity {

    public static final String TAG = "TAG";
    Spinner choose_paper_name, current_year, choose_Department;
    String selected_year, selected_department;

    private View parent_view;

    EditText mFullName, mEmail, mPassword, mPhone, mcollegename, mstate, mdistrict, mRemark, mSelected,mPaper, CoFullName, CoEmail, CoCollegeName, CoPic;
    Button mRegisterBtn;
    TextView mLoginBtn;
    FirebaseAuth fAuth;
    ProgressBar progressBar;
    FirebaseFirestore fStore;
    String userID;

    ImageView profileImageView;

    ArrayList<String> arrayList_mech, arrayList_firstyear, arrayList_CSE, arrayList_civil, arrayList_ENTC, arrayList_chem, arrayList_null;
    ArrayAdapter arrayAdapter_child;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);


        choose_paper_name = (Spinner) findViewById(R.id.choose_paper_name);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        progressBar = findViewById(R.id.progressBar);
        mFullName   = findViewById(R.id.fullName);
        mEmail      = findViewById(R.id.Email);
        mPassword   = findViewById(R.id.password);
        mPhone      = findViewById(R.id.phone);
        mcollegename = findViewById(R.id.college_name);


        CoFullName = findViewById(R.id.co_fullName);
        CoEmail = findViewById(R.id.co_author_email);
        CoCollegeName = findViewById(R.id.co_college_name);

        mstate = findViewById(R.id.state);
        mdistrict = findViewById(R.id.district);

        if(fAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), Login.class));
            finish();
        }



        //Select Year Spinner
        current_year = (Spinner) findViewById(R.id.choose_year);
        List<String> list_year = new ArrayList<String>();
        list_year.add("Select Year");
        list_year.add("First Year");
        list_year.add("Second Year");
        list_year.add("Third Year");
        list_year.add("Forth Year");

        ArrayAdapter<String> arrayYear = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list_year);
        arrayYear.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        current_year.setAdapter(arrayYear);
        current_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long i) {
                current_year.setSelection(position);
            }
            
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        selected_year = current_year.getSelectedItem().toString();


        //Select Department Spinner
        choose_Department = (Spinner) findViewById(R.id.choose_department);
        List<String> choose_department = new ArrayList<String>();
        choose_department.add("Select Department");
        choose_department.add("First Year");
        choose_department.add("Mechanical");
        choose_department.add("Computer");
        choose_department.add("Civil");
        choose_department.add("E&ampTC");
        choose_department.add("Chemical");

        ArrayAdapter<String> arraydepartment = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, choose_department);
        arraydepartment.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.choose_Department.setAdapter(arraydepartment);

        String choose_Department_string = choose_Department.getSelectedItem().toString();

        //---------------Child Spinner----------------//
        arrayList_null = new ArrayList<>();
        arrayList_null.add("Please Select Proper Department");

        arrayList_firstyear = new ArrayList<>();
        arrayList_firstyear.add("The World after COVID-19 Pandemic");
        arrayList_firstyear.add("Nanocomposites and Nano Structure");
        arrayList_firstyear.add("Globalisation and its Impact");
        arrayList_firstyear.add("Introduction to data Science");
        arrayList_firstyear.add("Robotics and Automation");
        arrayList_firstyear.add("Artificial intelligence");

        arrayList_mech = new ArrayList<>();
        arrayList_mech.add("Industry 4.0");
        arrayList_mech.add("Application of Artificial Intelligence in Mech. Engg.");
        arrayList_mech.add("Robotics, Automation and Manufacturing");
        arrayList_mech.add("Current scenario in simulation techniques");
        arrayList_mech.add("Electric vehicles");
        arrayList_mech.add("Modern Trends in Non-conventional Energy Soures");

        arrayList_CSE = new ArrayList<>();
        arrayList_CSE.add("Neuralink : The Brain Magical Future");
        arrayList_CSE.add("Blue Eyes Technology");
        arrayList_CSE.add("Quantum Computing");
        arrayList_CSE.add("Block Chain");
        arrayList_CSE.add("Deep Learning");
        arrayList_CSE.add("AI and Machine Learning");

        arrayList_civil = new ArrayList<>();
        arrayList_civil.add("Building Information Modeling(BIM)");
        arrayList_civil.add("Demand for sustainable design in Civil Engineering");
        arrayList_civil.add("Use of drone in the field of Civil Engg.");
        arrayList_civil.add("Use of 3D printing in construction");
        arrayList_civil.add("Recent Trends in Civil Engg.");
        arrayList_civil.add("High speed construction methods, structures & challenges");

        arrayList_ENTC = new ArrayList<>();
        arrayList_ENTC.add("5G network and communication");
        arrayList_ENTC.add("Robotics and automation systems");
        arrayList_ENTC.add("Wireless sensor networks");
        arrayList_ENTC.add("Need of electronics in health care");
        arrayList_ENTC.add("Security in embedded systems");
        arrayList_ENTC.add("Bio Batteries");

        arrayList_chem = new ArrayList<>();
        arrayList_chem.add("Advance seperation techniques in chemical industry");
        arrayList_chem.add("Energy conservation in chemical industry");
        arrayList_chem.add("Modelling &amp simulation in chemical Engg.");
        arrayList_chem.add("Noval effluent treatment in chemical industry");
        arrayList_chem.add("Advanced process or techniques in oxygen production");
        arrayList_chem.add("Fire and safety chemical industry");
        //---------------Child Spinner----------------//

        this.choose_Department.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long i) {

                if(position==0)
                {
                    arrayAdapter_child=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,arrayList_null);
                }

                if(position==1)
                {
                    arrayAdapter_child=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_firstyear);
                }

                if(position==2)
                {
                    arrayAdapter_child=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, arrayList_mech);
                }

                if(position==3)
                {
                    arrayAdapter_child=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,arrayList_CSE);
                }

                if(position==4)
                {
                    arrayAdapter_child=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,arrayList_civil);
                }

                if(position==5)
                {
                    arrayAdapter_child=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,arrayList_ENTC);
                }

                if(position==6)
                {
                    arrayAdapter_child=new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item,arrayList_chem);
                }
                choose_paper_name.setAdapter(arrayAdapter_child);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        selected_department = this.choose_Department.getSelectedItem().toString();



        parent_view = findViewById(android.R.id.content);
        mRegisterBtn = findViewById(R.id.registerBtn);
        mRegisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Snackbar.make(parent_view, "This is Demo Button, Please Click On Login Here Text", Snackbar.LENGTH_SHORT).show();
                final String email = mEmail.getText().toString().trim();
                String password = mPassword.getText().toString().trim();
                final String fullName = mFullName.getText().toString();
                final String phone    = mPhone.getText().toString();
                final String collegeName = mcollegename.getText().toString();

//                final  String state = "";
//                final  String district = "";
                final  String department = choose_Department.getSelectedItem().toString();;
                final  String learning_year = current_year.getSelectedItem().toString();;
                final  String remark = "No Remark Yet!!";
                final  String status = "We will inform you shortly";
                final  String paper_url = "";
                final  String paperName = choose_paper_name.getSelectedItem().toString();
                final  String Profile_IMG = "";

                //Co-Author
                final String Co_Author_Profile_IMG = "";
                final String CofullName = CoFullName.getText().toString();
                final String Coemail = CoEmail.getText().toString();
                final String CocollegeName = CoCollegeName.getText().toString();


                final String State = mstate.getText().toString();
                final String District = mdistrict.getText().toString();

                //Date And Time
                Date date = Calendar.getInstance().getTime();
                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
                String PaperDate = df.format(date);


                if(fullName.isEmpty()){
                    mFullName.setError("Full Name is Required");
                    return;
                }

                if(collegeName.isEmpty()){
                    mcollegename.setError("College Name is Required");
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    mEmail.setError("Email is Required.");
                    return;
                }

                if(phone.isEmpty()){
                    mPhone.setError("Mobile Number is Required");
                    return;
                }

                if(TextUtils.isEmpty(password)){
                    mPassword.setError("Password is Required.");
                    return;
                }

                if(password.length() < 6){
                    mPassword.setError("Password must contains minimum 6 Characters");
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // register the user in firebase


                fAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            // send verification link

                            FirebaseUser fuser = fAuth.getCurrentUser();
                            fuser.sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(Registration.this, "Verification Email Has been Sent.", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: Email not sent " + e.getMessage());
                                }
                            });

                            Toast.makeText(Registration.this, "User Created.", Toast.LENGTH_SHORT).show();
                            userID = fAuth.getCurrentUser().getUid();
                            DocumentReference documentReference = fStore.collection("users").document(userID);
                            Map<String,Object> user = new HashMap<>();

                            user.put("fName",fullName);
                            user.put("email",email);
                            user.put("phone",phone);
                            user.put("collegeName",collegeName);

                            user.put("state",State);
                            user.put("district",District);
                            user.put("department",department);
                            user.put("year",learning_year);
                            user.put("Remark",remark);
                            user.put("Status",status);
                            user.put("PaperName",paperName);
                            user.put("Paper_URL",paper_url);

                            user.put("Profile_IMG",Profile_IMG);

                            //Date And Time
                            user.put("PaperDate",PaperDate);

                            user.put("ProfileID",userID);

                            //Co Author Info
                            user.put("Co_AuthorName", CofullName);
                            user.put("Co_Author_Email", Coemail);
                            user.put("Co_Author_College", CocollegeName);
                            user.put("Co_Author_Profile_IMG",Co_Author_Profile_IMG);


                            documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "onSuccess: user Profile is created for "+ userID);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d(TAG, "onFailure: " + e.toString());
                                }
                            });
                            Intent i = new Intent(getApplicationContext(),MyAccount.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            i.putExtra("EXIT", true);
                            startActivity(i);
                            finish();


                        }else {
                            Toast.makeText(Registration.this, "Error ! " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

            }
        });

        //Go to Login Page
        mLoginBtn = findViewById(R.id.GotoLogin);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Login.class));
            }
        });


    }
}