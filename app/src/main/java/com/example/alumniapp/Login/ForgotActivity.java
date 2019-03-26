package com.example.alumniapp.Login;

import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.alumniapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotActivity extends AppCompatActivity {
  private   EditText editTextForgot;
    Button submit;
    RelativeLayout rellay1,rellay2;
    Handler handler=new Handler();
    Runnable runnable=new Runnable() {
        @Override
        public void run() {
            rellay1.setVisibility(View.VISIBLE);


        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_ACTION_BAR); //will hide the title

        getSupportActionBar().hide(); // hide the title bar

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        setContentView(R.layout.activity_forgot);

        rellay1 = findViewById(R.id.rellay1);
        editTextForgot=findViewById(R.id.edtforgot);
     submit=findViewById(R.id.btnforgot);
        handler.postDelayed(runnable, 1000);
       submit.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               FirebaseAuth auth = FirebaseAuth.getInstance();
String email=editTextForgot.getText().toString().trim();
               auth.sendPasswordResetEmail(email)
                       .addOnCompleteListener(new OnCompleteListener<Void>() {
                           @Override
                           public void onComplete(@NonNull Task<Void> task) {
                               if (task.isSuccessful()) {
                                   // do something when mail was sent successfully
                                   Intent i=new Intent(ForgotActivity.this,LoginActivity.class);
                                   startActivity(i);
                               } else {
                                   // ...
                               }
                           }
                       });
           }
       });

    }
}
