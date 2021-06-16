package com.example.restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class CustomerProfile extends AppCompatActivity implements View.OnClickListener {
    EditText name;
    EditText Address;
    TextView mobileno, Email;
    Button Update;
    LinearLayout password, LogOut;
    DatabaseReference databaseReference, data;
    FirebaseDatabase firebaseDatabase;
    String address, email, passwordd;
    String[] city = { "Islamabad" ,"Karachi", "Lahore", "Peshawar", "Quetta"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_profile);
        name = findViewById(R.id.fnamee);
        Address = findViewById(R.id.customer_address);
        mobileno = findViewById(R.id.mobilenumber);
        Email = findViewById(R.id.emailID);
        Update = findViewById(R.id.update);
        password = findViewById(R.id.password_layout);
        LogOut = findViewById(R.id.logout_layout);
        Update.setOnClickListener(this);
        String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Customer").child(userid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                final Customer customer = dataSnapshot.getValue(Customer.class);
                name.setText(customer.getName());
                Address.setText(customer.getAddress());
                mobileno.setText(customer.getMobileno());
                Email.setText(customer.getEmailID());


            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

    }



    @Override
    public void onClick(View view) {
        updateInformation();
    }


    private void updateInformation(){
        String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
        data = FirebaseDatabase.getInstance().getReference("Customer").child(useridd);
        data.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {

                Customer customer = dataSnapshot.getValue(Customer.class);
                email = customer.getEmailID();
                passwordd = customer.getPassword();
                long mobilenoo = Long.parseLong(customer.getMobileno());
                String Name = name.getText().toString().trim();
                address = Address.getText().toString();
                HashMap<String, String> hashMappp = new HashMap<>();
                hashMappp.put("Address", address);
                hashMappp.put("EmailID", email);
                hashMappp.put("Name", Name);
                hashMappp.put("Mobileno", String.valueOf(mobilenoo));
                hashMappp.put("Password", passwordd);
                firebaseDatabase.getInstance().getReference("Customer").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(hashMappp);

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
    }
}