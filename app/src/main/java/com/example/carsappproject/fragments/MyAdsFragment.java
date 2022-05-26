package com.example.carsappproject.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.carsappproject.Entities.Ad;
import com.example.carsappproject.R;
import com.example.carsappproject.adapters.MyAdsAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MyAdsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyAdsFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    RecyclerView recview;
    MyAdsAdapter adapter;
    public MyAdsFragment() {

    }

    public static MyAdsFragment newInstance(String param1, String param2) {
        MyAdsFragment fragment = new MyAdsFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_my_ads, container, false);

        recview=view.findViewById(R.id.recviewAds);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));




        FirebaseRecyclerOptions<Ad> options =
                new FirebaseRecyclerOptions.Builder<Ad>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Ads"), Ad.class)
                        .build();

        adapter=new MyAdsAdapter(options);
        recview.setAdapter(adapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void replaceFragment() {
        DescriptionFragment fragment = new DescriptionFragment();
        FragmentManager fragmentManager = getParentFragmentManager();
        int fragmentTransaction = fragmentManager.beginTransaction().replace(R.id.FrameConatiner, fragment).commit();
    }

}