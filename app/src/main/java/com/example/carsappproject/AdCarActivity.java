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








    /*private EditText brand,model, registrationNumber,price,city;
    private Switch aSwitchRental;
    private Button btnAdd;
    private static final int Pick_Image_Request=1;
    private TextView addImages;
    private ImageView mImageView,imageCar1,imageCar2,imageCar3;
    private Uri mImageUri;
    ActivityMainBinding binding;
    private FirebaseStorage firebaseStorage;
    private StorageReference storageReference;
    private FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
    private DatabaseReference databaseReference=firebaseDatabase.getReference("Ads");
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_car);

        brand=findViewById(R.id.carbrandE);
        model=findViewById(R.id.inputModel);
        registrationNumber =findViewById(R.id.inputEnergy);
        price=findViewById(R.id.inputpriceE);
        city=findViewById(R.id.inputCity);
        aSwitchRental=findViewById(R.id.switchRental);
        btnAdd=findViewById(R.id.btnAddCar);
        addImages=findViewById(R.id.AddImages);
        mImageView=findViewById(R.id.imageRecycleV);

        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        progressBar=findViewById(R.id.progressBarAdd);
        addImages.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openFileChooser();
            }
        });


        btnAdd.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                addingCar();
            }
        });
        

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Pick_Image_Request && resultCode==RESULT_OK
    && data!=null && data.getData()!=null){
             mImageUri = data.getData();
             imageCar1.setImageURI(mImageUri);
            // uploadImages();

        }

    }

    private void uploadImages() {
        StorageReference fileRef=storageReference.child(System.currentTimeMillis()+"."+getFileExtension(mImageUri));

        if(mImageUri!=null){
        fileRef.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
            fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Toast.makeText(AdCarActivity.this, "Uploaded successefuly", Toast.LENGTH_SHORT).show();
                }
            });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
            //progresse bar
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AdCarActivity.this, "Upload filed!!", Toast.LENGTH_SHORT).show();

            }
        });

        }
        else{
            Toast.makeText(this, "Please select images!", Toast.LENGTH_SHORT).show();
        }

    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void openFileChooser() {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,Pick_Image_Request);
    }

    private void addingCar() {
        String sbrand=brand.getText().toString().trim();
        String smodel=model.getText().toString().trim();
        String sRegNumber=registrationNumber.getText().toString().trim();
        String sprice=price.getText().toString().trim();
        String scity=city.getText().toString().trim();
        String imageUri=mImageUri.toString();
        boolean forRental=false;
        if(aSwitchRental.isChecked()){
            forRental=true;
        }
        uploadImages();
        Ad ad=new Ad(sbrand,smodel,sRegNumber,sprice,scity,forRental,imageUri);
        databaseReference.setValue(ad);

    }*/
}