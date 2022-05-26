package com.example.carsappproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.carsappproject.Entities.Ad;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AdCarActivity extends AppCompatActivity {
    private EditText brand,model, registrationNumber,price,city;
    private Switch aSwitchRental;
    private Button uploadBtn, showAllBtn;
    private TextView textViewAddI;
    private ProgressBar progressBar;
    private DatabaseReference myRef;
    private String userID;
    private FirebaseAuth mAuth;
    private ImageView back;
    //vars
    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("Ads");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    private Uri imageUri;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_car);

        brand=findViewById(R.id.carbrandEUpdate);
        model=findViewById(R.id.inputModelUpdate);
        registrationNumber =findViewById(R.id.inputEnergyUpdate);
        price=findViewById(R.id.inputpriceEUpdate);
        city=findViewById(R.id.inputCityUpdate);
        aSwitchRental=findViewById(R.id.switchRentalUpdate);
        back=findViewById(R.id.imageback);
        uploadBtn = findViewById(R.id.btnUpdate);
        ///showAllBtn = findViewById(R.id.showall_btn);
        progressBar = findViewById(R.id.progressBarAdd);
        textViewAddI = findViewById(R.id.AddImages);

        progressBar.setVisibility(View.INVISIBLE);

        /*showAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity( new Intent(MainActivity.this , ShowActivity.class));
            }
        });
*/
        textViewAddI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent , 2);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(AdCarActivity.this, Home.class);
                startActivity(intent2);
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageUri != null){
                    uploadToFirebase(imageUri);
                    Intent intent2 = new Intent(AdCarActivity.this, Home.class);
                    startActivity(intent2);

                }else{
                    Toast.makeText(AdCarActivity.this, "Please Select Image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==2 && resultCode == RESULT_OK && data != null){

            imageUri = data.getData();
            //textViewAddI.setImageURI(imageUri);

        }
    }

    private void uploadToFirebase(Uri uri){
        String sbrand=brand.getText().toString().trim();
        String smodel=model.getText().toString().trim();
        String sRegNumber=registrationNumber.getText().toString().trim();
        String sprice=price.getText().toString().trim();
        String scity=city.getText().toString().trim();
        boolean forRental=false;
        if(aSwitchRental.isChecked()){
            forRental=true;
        }
        final StorageReference fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        boolean finalForRental = forRental;
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {



                        //get current user role
                        mAuth = FirebaseAuth.getInstance();
                        FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
                        myRef = mFirebaseDatabase.getReference("Users");
                        FirebaseUser user = mAuth.getCurrentUser();
                        userID = user.getUid().toString();
                        //Add ad information
                        Ad model=new Ad(sbrand,smodel,sRegNumber,sprice,scity, finalForRental,uri.toString(),userID);
                        //Ad model = new Ad(uri.toString());
                        String modelId = root.push().getKey();
                        root.child(modelId).setValue(model);
                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(AdCarActivity.this, "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        //textViewAddI.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(AdCarActivity.this, "Uploading Failed !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){

        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }

}