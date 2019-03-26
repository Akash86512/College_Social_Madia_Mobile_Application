package com.example.alumniapp.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.alumniapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity    implements View.OnClickListener {
   private EditText mobile;
    String no;

    private FirebaseAuth mAuth;
    RadioGroup radioGroup;
    RadioButton genderradioButton;
    Button login;
    Button signup;
    private ProgressDialog progressDialog;
    RelativeLayout rellay1,rellay2;
    private DatabaseReference mDatabase;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText editTextName;
    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);
            rellay2.setVisibility(View.VISIBLE);

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR); //will hide the title

        getSupportActionBar().hide(); // hide the title bar

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_signup);

        mobile = findViewById(R.id.editmobile);
        editTextEmail=findViewById(R.id.edtemail);
        editTextPassword=findViewById(R.id.edtpass);
        editTextName=findViewById(R.id.edtname);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        radioGroup=findViewById(R.id.radioGroup);
        login=findViewById(R.id.login);
        signup=findViewById(R.id.btnsignup);
        rellay1 = findViewById(R.id.rellay1);
        rellay2 = findViewById(R.id.rellay2);
        progressDialog = new ProgressDialog(this);
        handler.postDelayed(runnable, 1000);


signup.setOnClickListener((View.OnClickListener) this);



        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SignupActivity.this, LoginActivity.class);
                startActivity(i);
            }
        });

    }


    private void registerUser(){

        //getting email and password from edit texts
        final String email = editTextEmail.getText().toString().trim();
        final String password  = editTextPassword.getText().toString().trim();
        final String name  = editTextName.getText().toString().trim();
        int selectedId = radioGroup.getCheckedRadioButtonId();
        genderradioButton = (RadioButton) findViewById(selectedId);
        final String cat=genderradioButton.getText().toString();
        //checking if email and passwords are empty
        if(TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
            return;
        }

        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
            return;
        }


        progressDialog.setMessage("Registering Please Wait...");
        progressDialog.show();


        //creating a new user
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //checking if success
                        if(task.isSuccessful()){
                            mAuth.getCurrentUser().sendEmailVerification()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){



                                        no = mobile.getText().toString();
                                        validNo(no);
                                        String user_id = mAuth.getCurrentUser().getEmail();
                                        DatabaseReference current_user_db = mDatabase.child(user_id);
                                        current_user_db.child("Category").setValue(cat);
                                        current_user_db.child("Email").setValue(email);
                                        current_user_db.child("Password").setValue(password);
                                        current_user_db.child("mobileno").setValue(no);
                                        current_user_db.child("Name").setValue(name);

                                        Toast.makeText(SignupActivity.this,"Successfully registered.please check your email",Toast.LENGTH_LONG).show();
                                        editTextEmail.setText(" ");
                                        editTextPassword.setText(" ");
                                    }else {
                                        Toast.makeText(SignupActivity.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
                                    }


                                }
                            });

                        }else{

                            Toast.makeText(SignupActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
                        }
                        progressDialog.dismiss();
                    }
                });




    }

    private void validNo(String no){
        if(no.isEmpty() || no.length() < 10){
            mobile.setError("Enter a valid mobile");
            mobile.requestFocus();
            return;
        }
    }

    @Override
    public void onClick(View view) {
        //calling register method on click
        registerUser();
    }
}
