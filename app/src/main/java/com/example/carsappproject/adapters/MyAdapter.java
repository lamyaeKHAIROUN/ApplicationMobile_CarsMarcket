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

public class MyAdapter extends FirebaseRecyclerAdapter<Ad,MyAdapter.myviewholder>
{

    FirebaseRecyclerOptions<Ad> backup;
    public MyAdapter(@NonNull FirebaseRecyclerOptions<Ad> options) {
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


    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_without_options,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder
    {
        TextView brand , modle , email,registrationNumber,price,city;
        ImageView imageCar;
        RelativeLayout relativeLayout;


        public myviewholder(@NonNull View itemView) {
            super(itemView);
            relativeLayout=itemView.findViewById(R.id.relHolder);
            brand = itemView.findViewById(R.id.textBrand);
            modle = itemView.findViewById(R.id.textModel);
            price = itemView.findViewById(R.id.textPrice);
            imageCar=itemView.findViewById(R.id.imageofcar);






        }
    }




}
