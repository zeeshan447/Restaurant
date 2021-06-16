package com.example.restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class RestaurantDelivery extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    RecyclerView recyclerView;
    private List<UpdateRestaurant> restaurantList;
    private RestaurantDeliveryAdapter adapter;
    String Address;
    DatabaseReference dataaa, databaseReference;
    SwipeRefreshLayout swipeRefreshLayout;
    SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_delivery);
        recyclerView = findViewById(R.id.recycle_delivery_restaurant);
        recyclerView.setHasFixedSize(true);
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.move);
        recyclerView.startAnimation(animation);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        restaurantList = new ArrayList<>();
        swipeRefreshLayout = findViewById(R.id.delivery_swipelayout);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark, R.color.green);
        adapter = new RestaurantDeliveryAdapter(getApplicationContext(), restaurantList);
        recyclerView.setAdapter(adapter);
        swipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(true);
                String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                dataaa = FirebaseDatabase.getInstance().getReference("Customer").child(userid);
                dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                        Customer cust = dataSnapshot.getValue(Customer.class);
                        Address = cust.getAddress();
                        restaurantDisplay();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        });
    }

    @Override
    public void onRefresh() {
        restaurantDisplay();

    }
    private void restaurantDisplay() {
        swipeRefreshLayout.setRefreshing(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("Restaurant").child(Address);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                restaurantList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UpdateRestaurant restaurant = snapshot.getValue(UpdateRestaurant.class);
                    restaurantList.add(restaurant);
                    adapter = new RestaurantDeliveryAdapter(getApplicationContext(), restaurantList);
                    recyclerView.setAdapter(adapter);
                    swipeRefreshLayout.setRefreshing(false);

                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                swipeRefreshLayout.setRefreshing(false);

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                search(newText);
                return true;
            }
        });

    }
    private void search(final String searchtext) {

        ArrayList<UpdateRestaurant> mylist = new ArrayList<>();
        for (UpdateRestaurant object : restaurantList) {
            if (object.getName().toLowerCase().contains(searchtext.toLowerCase())) {
                mylist.add(object);
            }
        }
        adapter = new RestaurantDeliveryAdapter(this, mylist);
        recyclerView.setAdapter(adapter);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);

        MenuItem menuItem = menu.findItem(R.id.SearchRestaurant);
        searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search Restaurant");
        return true;    }

}
