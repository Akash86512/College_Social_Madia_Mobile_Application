package com.example.alumniapp.Login;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.alumniapp.FrontActivity;
import com.example.alumniapp.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity   implements View.OnClickListener {


    private static final int RC_SIGN_IN = 234;

    //Tag for the logs optional
    private static final String TAG = "simplifiedcoding";

    //creating a GoogleSignInClient object
    GoogleSignInClient mGoogleSignInClient;

    //And also a Firebase Auth object

    private FirebaseAuth mAuth;
    Button signup;
    Button signin;
    Button forgot;
    RelativeLayout rellay1,rellay2;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private ProgressDialog progressDialog;
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

        setContentView(R.layout.activity_login);

        editTextEmail=findViewById(R.id.edtemail);
        editTextPassword=findViewById(R.id.edtpass);
        mAuth = FirebaseAuth.getInstance();
        signup=findViewById(R.id.btn_signup);
        signin=findViewById(R.id.signin);
        forgot=findViewById(R.id.forgot);
        rellay1=findViewById(R.id.rellay1);
        rellay2=findViewById(R.id.rellay2);

        progressDialog = new ProgressDialog(this);
        handler.postDelayed(runnable, 1000);




        signin.setOnClickListener((View.OnClickListener) this);





        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });


        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this, ForgotActivity.class);
                startActivity(i);
            }
        });
    }







    @Override
    public void onClick(View view) {
        //calling register method on click
        signinUser();
    }

    private void signinUser() {

        String email = editTextEmail.getText().toString().trim();
        String password  = editTextPassword.getText().toString().trim();
        progressDialog.setMessage("Login Please Wait...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            if (mAuth.getCurrentUser().isEmailVerified()){


                                Intent i=new Intent(LoginActivity.this, FrontActivity.class);
                                startActivity(i);




                            }else {
                                Toast.makeText(LoginActivity.this, "please verify your email.",
                                        Toast.LENGTH_SHORT).show();
                            }


                        } else {
                            // If sign in fails, display a message to the user.

                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }


                    }
                });

    }

    @Override
    public void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser() != null&& mAuth.getCurrentUser().isEmailVerified()) {
            Intent i=new Intent(LoginActivity.this, FrontActivity.class);
            startActivity(i);
        }
        // Check if user is signed in (non-null) and update UI accordingly.

    }




}
