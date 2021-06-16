package com.example.restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.bumptech.glide.module.AppGlideModule;


public class RestaurantReservation extends AppCompatActivity implements View.OnClickListener {
    ImageView imgReserveRestaurant;
    Button btnReserveNow;
    TextView txtReserveRestaurantName;
    EditText edtDateTime;
    String RestaurantName;
    ImageView RestaurantImage;
    DatabaseReference dataaa, databaseReference;
    String Address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_reservation);
        imgReserveRestaurant = findViewById(R.id.restaurant_image_reservation);
        txtReserveRestaurantName = findViewById(R.id.restaurant_name_reservation);
        btnReserveNow = findViewById(R.id.restaurant_reserve_button);
        edtDateTime = findViewById(R.id.date_time_input);

        RestaurantName = getIntent().getStringExtra("Name");
        txtReserveRestaurantName.setText(RestaurantName);
        btnReserveNow.setOnClickListener(this);
        edtDateTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDateTimeDialog(edtDateTime);

            }
        });

        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = FirebaseDatabase.getInstance().getReference("Restaurant");
        dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                UpdateRestaurant cust = dataSnapshot.getValue(UpdateRestaurant.class);
                Address = cust.getAddress();
               ImageDisplay();
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    private void showDateTimeDialog(final EditText date_time_in){
        final Calendar calendar=Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR,year);
                calendar.set(Calendar.MONTH,month);
                calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);

                TimePickerDialog.OnTimeSetListener timeSetListener=new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY,hourOfDay);
                        calendar.set(Calendar.MINUTE,minute);

                        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yy-MM-dd HH:mm");

                        date_time_in.setText(simpleDateFormat.format(calendar.getTime()));
                    }
                };
                new TimePickerDialog(RestaurantReservation.this,timeSetListener,calendar.get(Calendar.HOUR_OF_DAY),calendar.get(Calendar.MINUTE),false).show();

            }
        };
        new DatePickerDialog(RestaurantReservation.this,dateSetListener,calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();

    }

    private void ImageDisplay(){
        databaseReference = FirebaseDatabase.getInstance().getReference("Restaurant").child("Islamabad");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UpdateRestaurant restaurant = snapshot.getValue(UpdateRestaurant.class);
                    Glide.with(getApplicationContext()).load(restaurant.getImageURL()).into(imgReserveRestaurant);



                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
    private void isValid(){
        String dateTime = edtDateTime.getText().toString();
        if (dateTime.length() == 0){
            Toast.makeText(this, "Please Enter Date And Time", Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent(this, CustomerDashboard.class);
            startActivity(intent);
            Toast.makeText(this, "Reservation Made", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.restaurant_reserve_button:
                    isValid();


        }
    }
}