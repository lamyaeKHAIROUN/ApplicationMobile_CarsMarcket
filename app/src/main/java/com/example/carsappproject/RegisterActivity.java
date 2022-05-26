package com.example.carsappproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carsappproject.Entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    private EditText fullName, psudo,email,tel,password;
    private TextView alredyHaveAcount;
    private Button btnRegister;
    private FirebaseAuth mAuth;
    private User user;
    private TextView alresyhaveacount;
    private ProgressBar progressBar;
    private CheckBox cbBayer,cbAnnoncer;
    private String  userRole;
    private DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReferenceFromUrl("https://myfirebase-8991d-default-rtdb.firebaseio.com/" );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        psudo =findViewById(R.id.inputPseudo);
        fullName  =findViewById(R.id.inputName);
        email=findViewById(R.id.carbrandEUpdate);
        tel=findViewById(R.id.inputTel);
        password=findViewById(R.id.inputPasswordLog);
        alredyHaveAcount=findViewById(R.id.aj);
        btnRegister=findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        alresyhaveacount=findViewById(R.id.aj);
        cbAnnoncer=findViewById(R.id.isAnnoncer);
        cbBayer=findViewById(R.id.isBayer);

        alresyhaveacount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent1 = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent1);
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                createUser();
            }


        });
    }
    public void createUser() {

        String sfullName=fullName.getText().toString().trim();
        String spseudo=psudo.getText().toString().trim();
        String semail=email.getText().toString().trim();
        String stel=tel.getText().toString().trim();
        String spassword=password.getText().toString().trim();
        Boolean isBayer=cbBayer.isChecked();
        Boolean isAnnoncer=cbAnnoncer.isChecked();


        if(isAnnoncer){
            userRole="1";
            userRole.trim();
        }

        if(isBayer){
            userRole="2";
            userRole.trim();

        }

      /*  if(sfullName.isEmpty()||spseudo.isEmpty()||semail.isEmpty()||stel.isEmpty()||spassword.isEmpty()){
            Toast.makeText(RegisterActivity.this,"Please fill all feilds",Toast.LENGTH_SHORT).show();
        }*/
        if(semail.isEmpty()){
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }
        if(sfullName.isEmpty()){
            fullName.setError("full name is required!");
            fullName.requestFocus();
            return;
        }
        if(spseudo.isEmpty()){
            psudo.setError("Pseudo is required!");
            psudo.requestFocus();
            return;
        }
        if(stel.isEmpty()){
            tel.setError("Phone number is required!");
            tel.requestFocus();
            return;
        }
        if(spassword.isEmpty()){
            password.setError("Password is required!");
            password.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(semail).matches()){
            email.setError("Please provide a valid email");
            email.requestFocus();
            return;
        }
        if(spassword.length()<6){
            password.setError("min length password must be 6 characters");
            password.requestFocus();
            return;
        }
        if(cbBayer.isChecked()==false&&cbAnnoncer.isChecked()==false){
            Toast.makeText(RegisterActivity.this,"You should choice an option Annoncer/Bayer",
                    Toast.LENGTH_SHORT).show();
        }
       else{
            mAuth.createUserWithEmailAndPassword(semail,spassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        user =new User(sfullName,semail,spseudo,stel,userRole);
                         FirebaseDatabase.getInstance().getReference("Users").
                         child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                         setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         if(task.isSuccessful()){
                             progressBar.setVisibility(View.VISIBLE);
                             FirebaseUser fuser = mAuth.getCurrentUser();
                             fuser.sendEmailVerification();
                             Toast.makeText(RegisterActivity.this,"User has been registred succesfully and Verification Email Has been Sent",Toast.LENGTH_SHORT).show();
                             Intent intent2 = new Intent(RegisterActivity.this, LoginActivity.class);
                             startActivity(intent2);
                         }
                         else
                             Toast.makeText(RegisterActivity.this,"Failed to register! Try again",Toast.LENGTH_SHORT).show();


                     }
                 });

                        }
                }
            });

        }
            }

    }