package com.example.carsappproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.carsappproject.Entities.Ad;
import com.example.carsappproject.adapters.MyAdapter;
import com.example.carsappproject.fragments.ProfileFragment;
import com.example.carsappproject.fragments.RecyclerFragment;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;


public class BayerHome extends AppCompatActivity {


    private BottomNavigationView bnv;
    RecyclerView recview;
    Fragment temp=null;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bayer_home);
        recview=(RecyclerView)findViewById(R.id.recview);

        getSupportFragmentManager().beginTransaction().replace(R.id.FrameConatiner,new RecyclerFragment()).commit();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.bottombar_nav);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {

                switch (item.getItemId())
                {
                    case R.id.home_item : temp=new RecyclerFragment();
                        break;
                    case R.id.profile_item :
                        temp=new ProfileFragment();
                        break;
                    case R.id.search_item : temp=new RecyclerFragment();
                        break;
                    case R.id.logout :
                        Intent intent = new Intent(BayerHome.this, LoginActivity.class);
                        startActivity(intent);
                        break;

                }

                getSupportFragmentManager().beginTransaction().replace(R.id.FrameConatiner,temp).commit();
                return true;
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.bottom_nav_menu,menu);

       /* MenuItem item=menu.findItem(R.id.search_item);

        SearchView searchView=(SearchView)item.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                temp=new RecyclerFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.FrameConatiner,temp).commit();

                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                temp=new RecyclerFragment();
                getSupportFragmentManager().beginTransaction().replace(R.id.FrameConatiner,temp).commit();

                //processsearch(s);
                return false;
            }
        });*/

        return true;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int id= item.getItemId();
        if(id==R.id.profile_item){
            Toast.makeText(this, "profil item", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(BayerHome.this, ProfileFragment.class);
            startActivity(intent2);
            return true;
        }
        return super.onContextItemSelected(item);
    }

    public void processSearch(String s) {

        FirebaseRecyclerOptions<Ad> options =
                new FirebaseRecyclerOptions.Builder<Ad>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Ads").orderByChild("carBrand").startAt(s).endAt(s+"\uf8ff"), Ad.class)
                        .build();

        MyAdapter adapter=new MyAdapter(options);
        adapter.startListening();
        recview.setAdapter(adapter);
    }

}