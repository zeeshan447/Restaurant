package com.example.restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

public class CustomerDashboard extends AppCompatActivity implements View.OnClickListener {
    ImageView imgReserve, imgDelivery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_dashboard);
        imgReserve = findViewById(R.id.cdashboardreserve);
        imgDelivery = findViewById(R.id.cdashboarddelivery);
        imgReserve.setOnClickListener(this);
        imgDelivery.setOnClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.logout_menu, menu);
        return true;    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.LogOut){
            logout();
            return true;
        }
        return super .onOptionsItemSelected(item);
    }
    private void logout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.cdashboardreserve:
                Intent intent = new Intent(this, RestaurantListings.class);
                startActivity(intent);
                break;
            case R.id.cdashboarddelivery:
                Intent intent1 = new Intent(this, RestaurantDelivery.class);
                startActivity(intent1);
                break;

        }
    }
}