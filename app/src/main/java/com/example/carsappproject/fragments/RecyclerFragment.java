package com.example.carsappproject.fragments;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.example.carsappproject.Entities.Ad;
import com.example.carsappproject.R;
import com.example.carsappproject.adapters.MyAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;


public class RecyclerFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    RecyclerView recview;
    MyAdapter adapter;
    public RecyclerFragment() {

    }

    public static RecyclerFragment newInstance(String param1, String param2) {
        RecyclerFragment fragment = new RecyclerFragment();
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

        View view=inflater.inflate(R.layout.fragment_recycler, container, false);
        recview=view.findViewById(R.id.recview);
        recview.setLayoutManager(new LinearLayoutManager(getContext()));

        String s = "";
        FirebaseRecyclerOptions<Ad> options =
                new FirebaseRecyclerOptions.Builder<Ad>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Ads").orderByChild("carBrand").startAt(s).endAt(s+"\uf8ff"), Ad.class)
                        .build();

        adapter=new MyAdapter(options);
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
    public void processSearch(String s) {

        FirebaseRecyclerOptions<Ad> options =
                new FirebaseRecyclerOptions.Builder<Ad>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("Ads").orderByChild("carBrand").startAt(s).endAt(s+"\uf8ff"), Ad.class)
                        .build();

        MyAdapter adapter=new MyAdapter(options);
        //adapter.startListening();
        recview.setAdapter(adapter);
    }
    }