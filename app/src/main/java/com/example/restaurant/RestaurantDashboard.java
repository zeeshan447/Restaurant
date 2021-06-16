package com.example.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

public class RestaurantDashboard extends AppCompatActivity implements View.OnClickListener {
    ImageView imgAddMenu, imgManageDine, imgManageDelivery, imgRestaurantUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_dashboard);
        imgAddMenu = findViewById(R.id.rdashboardmenu);
        imgManageDine = findViewById(R.id.rdashboardmanagedine);
        imgManageDelivery = findViewById(R.id.rdashboarddelivery);
        imgRestaurantUpload = findViewById(R.id.rdashboarduploadimg);
        imgAddMenu.setOnClickListener(this);
        imgManageDine.setOnClickListener(this);
        imgManageDelivery.setOnClickListener(this);
        imgRestaurantUpload.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rdashboardmenu:
                Intent intent = new Intent(this, RestaurantMenu.class);
                startActivity(intent);
                break;

            case R.id.rdashboardmanagedine:
                Toast.makeText(this, "Dine", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rdashboarddelivery:
                Toast.makeText(this, "Delivery", Toast.LENGTH_SHORT).show();
                break;
            case R.id.rdashboarduploadimg:
                Intent intent1 = new Intent(this, RestaurantImageUpload.class);
                startActivity(intent1);
                break;
        }

    }
}