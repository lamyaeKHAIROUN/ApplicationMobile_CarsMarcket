package com.example.carsappproject.fragments;



import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.carsappproject.AdCarActivity;
import com.example.carsappproject.Home;
import com.example.carsappproject.R;

public class DescriptionFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String brand, model, price,registrationNumber,city, purl;
    private boolean forRental;

    Button btnCall;

    public DescriptionFragment() {

    }

    public DescriptionFragment(String brand, String model, String price, String purl) {
        this.brand = brand;
        this.model = model;
        this.price =price;
        this.purl=purl;
    }

    public DescriptionFragment(String brand, String model, String price, String registrationNumber,String city, Boolean forRental,String purl) {
        this.brand = brand;
        this.model = model;
        this.price =price;
        this.registrationNumber=registrationNumber;
        this.city=city;
        this.forRental=forRental;
        this.purl=purl;
    }



    public static DescriptionFragment newInstance(String param1, String param2) {
        DescriptionFragment fragment = new DescriptionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_description, container, false);
        btnCall=view.findViewById(R.id.btnCall);
        ImageView imageholder=view.findViewById(R.id.imagegholder);
        TextView brandholder=view.findViewById(R.id.brandholder);
        TextView modelholder=view.findViewById(R.id.modelholder);
        TextView priceholder=view.findViewById(R.id.priceholder);
        TextView registrationHolder=view.findViewById(R.id.registrationHolder);
        TextView cityHolder=view.findViewById(R.id.cityholder);
        TextView forRentalHolder=view.findViewById(R.id.forRentalholder);

        brandholder.setText(brand);
        modelholder.setText("Model: "+model);
        priceholder.setText("Price: "+price+" £");
        registrationHolder.setText("Registration number: : "+registrationNumber);
        cityHolder.setText("Location: "+city);
        if(forRental==true){
            forRentalHolder.setText("This car is for rental");

        }
        Glide.with(getContext()).load(purl).into(imageholder);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent i= new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+"+33612345"));
            startActivity(i);
            }
        });


        return  view;
    }

    public void onBackPressed()
    {
        AppCompatActivity activity=(AppCompatActivity)getContext();
        activity.getSupportFragmentManager().beginTransaction().replace(R.id.wrapper,new RecyclerFragment()).addToBackStack(null).commit();

    }

}