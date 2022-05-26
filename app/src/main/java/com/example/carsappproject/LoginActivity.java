package com.example.carsappproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    private EditText email,password;
    private Button btnLogin;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private TextView backToRegister, forgotPassword;
    private String userRole;
    private DatabaseReference myRef;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        email=findViewById(R.id.carbrandEUpdate);
        password=findViewById(R.id.inputPasswordLog);
        btnLogin=findViewById(R.id.btnLogin);
        mAuth = FirebaseAuth.getInstance();
        progressBar=findViewById(R.id.progressBarLog);
        progressBar.setVisibility(View.INVISIBLE);
        backToRegister=findViewById(R.id.backToRegister);
        forgotPassword=findViewById(R.id.aj);


        backToRegister.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent2 = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent2);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent2 = new Intent(LoginActivity.this, ResetPassword.class);
                startActivity(intent2);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                login();
            }


        });
    }

    private void login() {
        String smail=email.getText().toString().trim();
        String spassword=password.getText().toString().trim();
        if(smail.isEmpty()){
            email.setError("Email is required!");
            email.requestFocus();
            return;
        }
        if(spassword.isEmpty()){
            email.setError("Password is required!");
            email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(smail).matches()){
            email.setError("Please provide a valid email");
            email.requestFocus();
            return;
        }
        if(spassword.length()<6){
            password.setError("min length password must be 6 characters");
            password.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(smail,spassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.VISIBLE);
                    FirebaseDatabase.getInstance().getReference("Users").
                            child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                    //get current user role
                    mAuth = FirebaseAuth.getInstance();
                    FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
                     myRef = mFirebaseDatabase.getReference("Users");
                    FirebaseUser user = mAuth.getCurrentUser();
                     userID = user.getUid();

                    myRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            //get user role
                            User uInfo = new User();
                            String userRole=dataSnapshot.child(userID).getValue(User.class).getUserRole();
                            if(userRole.equals("1")){
                                Toast.makeText(LoginActivity.this,"You're login as Annoncer!",Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(LoginActivity.this, Home.class);
                                startActivity(intent1);
                            }
                            else if(userRole.equals("2")){
                                Toast.makeText(LoginActivity.this,"You're login as Bayer!",Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(LoginActivity.this, BayerHome.class);
                                startActivity(intent1);

                            }

                            else  Toast.makeText(LoginActivity.this,"I cannot get role!",Toast.LENGTH_SHORT).show();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
                else {
                    Toast.makeText(LoginActivity.this,"Failde to login! Please check your email and password!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


}