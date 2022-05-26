package com.example.carsappproject.adapters;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.carsappproject.Entities.Ad;
import com.example.carsappproject.R;
import com.example.carsappproject.fragments.DescriptionFragment;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import java.util.HashMap;
import java.util.Map;

public class MyAdsAdapter extends FirebaseRecyclerAdapter<Ad,MyAdsAdapter.myviewholder>
{

    public MyAdsAdapter(@NonNull FirebaseRecyclerOptions<Ad> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, @SuppressLint("RecyclerView") int position, @NonNull final Ad ad) {
        holder.brand.setText(ad.getCarBrand());
        holder.modle.setText(ad.getCarModel());
        holder.price.setText(ad.getPrice());
        //set image
        Glide.with(holder.imageCar.getContext()).load(ad.getUrlImage()).into(holder.imageCar);

        holder.imageCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppCompatActivity activity=(AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.FrameConatiner,new DescriptionFragment(ad.getCarBrand(),ad.getCarModel(),ad.getPrice(),ad.getRegistrationNumber(),ad.getCity(),ad.isForRental(), ad.getUrlImage())).addToBackStack(null).commit();
            }
        });


        //update ad
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus=DialogPlus.newDialog(holder.imageCar.getContext())
                        .setContentHolder(new ViewHolder(R.layout.update))
                        .setExpanded(true,1100)
                        .create();

                View myview=dialogPlus.getHolderView();
                final TextView imageUpdate=myview.findViewById(R.id.AddImagesUpdate);
                final EditText brandUpdate=myview.findViewById(R.id.carbrandEUpdate);
                final EditText modelUpdate=myview.findViewById(R.id.inputModelUpdate);
                final EditText priceUpdate=myview.findViewById(R.id.inputEnergyUpdate);
                final EditText registrationNUpdate=myview.findViewById(R.id.inputpriceEUpdate);
                // final ImageView imageViewUpdate=myview.findViewById(R.id.inputCityUpdate);
                final EditText cityUpdate=myview.findViewById(R.id.inputCityUpdate);
                final Switch switchUpdate=myview.findViewById(R.id.switchRentalUpdate);

                //update an ad
                Button submit=myview.findViewById(R.id.btnUpdate);

                brandUpdate.setText(ad.getCarBrand());
                modelUpdate.setText(ad.getCarModel());
                priceUpdate.setText(ad.getRegistrationNumber());
                registrationNUpdate.setText(ad.getPrice());
                cityUpdate.setText(ad.getCity());
                Boolean forRental=ad.isForRental();
                if(forRental){
                    switchUpdate.setChecked(true);
                    if(switchUpdate.isChecked()){
                        ad.setForRental(true);}
                    else ad.setForRental(false);
                }
                else { switchUpdate.setChecked(false);
                    ad.setForRental(false);
                    if(switchUpdate.isChecked()){
                        ad.setForRental(true);}
                    else ad.setForRental(false);

                }

                //show update panel
                dialogPlus.show();

                //valider la modification
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,Object> map=new HashMap<>();
                        map.put("carBrand",brandUpdate.getText().toString());
                        map.put("carModel",modelUpdate.getText().toString());
                        map.put("registrationNumber",registrationNUpdate.getText().toString());
                        map.put("price",priceUpdate.getText().toString());

                        FirebaseDatabase.getInstance().getReference().child("Ads")
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });



            }});

        //delete ad
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder=new AlertDialog.Builder(holder.imageCar.getContext());
                builder.setTitle("Delete Panel");
                builder.setMessage("Delete...?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child("Ads")
                                .child(getRef(position).getKey()).removeValue();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                builder.show();
            }
        });
    }


    //liaision avec l'item.xml
    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {
        TextView brand , modle , email,registrationNumber,price,city;
        ImageView imageCar;
        RelativeLayout relativeLayout;
        ImageView edit;
        ImageView delete;

        public myviewholder(@NonNull View itemView) {
            super(itemView);
            relativeLayout=itemView.findViewById(R.id.relHolder);
            brand = itemView.findViewById(R.id.textBrand);
            modle = itemView.findViewById(R.id.textModel);
            price = itemView.findViewById(R.id.textPrice);
            imageCar=itemView.findViewById(R.id.imageofcar);

            edit=(ImageView)itemView.findViewById(R.id.editicon);
            delete=(ImageView)itemView.findViewById(R.id.deleteicon);




        }
    }




}
