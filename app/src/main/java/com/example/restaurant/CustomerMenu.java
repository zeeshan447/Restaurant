package com.example.restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomerMenu extends AppCompatActivity {
    String RandomId, RestaurantID, randomid;
    ImageView imageView;
    ElegantNumberButton additem;
    TextView Foodname, FoodDescription, FoodPrice;
    DatabaseReference databaseReference, dataaa, Restaurantdata, reference, data, dataref;
    String State, City, Sub, dishname;
    int dishprice;
    String custID;
    FirebaseDatabase firebaseDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_menu);
        Foodname = findViewById(R.id.customer_menu_food_name);
        FoodDescription = findViewById(R.id.customer_menu_food_description);
        FoodPrice = findViewById(R.id.customer_menu_food_price);
        imageView = findViewById(R.id.customer_menu_food_image);
        additem = findViewById(R.id.number_btn);
        final String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        dataaa = FirebaseDatabase.getInstance().getReference("Customer").child(userid);

        RestaurantID= getIntent().getStringExtra("restaurantID");
        FoodSupplyDetails foodSupplyDetails = new FoodSupplyDetails(RandomId);
        randomid = foodSupplyDetails.RandomUID;
        databaseReference = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(RestaurantID).child(randomid);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                FoodModel foodModel = dataSnapshot.getValue(FoodModel.class);
                Foodname.setText(foodModel.getName());
                String ss = "<b>" + "Description: " + "</b>" + foodModel.getDescription();
                FoodDescription.setText(Html.fromHtml(ss));
                String pri = "<b>" + "Price: Rs " + "</b>" + foodModel.getPrice();
                FoodPrice.setText(Html.fromHtml(pri));
                Glide.with(CustomerMenu.this).load(foodModel.getImageURL()).into(imageView);

                Restaurantdata = FirebaseDatabase.getInstance().getReference("Restaurant").child(RestaurantID);
                Restaurantdata.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                        databaseReference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                Cart cart = dataSnapshot.getValue(Cart.class);
                                if (dataSnapshot.exists()) {
                                    additem.setNumber(cart.getDishQuantity());
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull @NotNull DatabaseError error) {

                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });




            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });
        additem.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataref = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                dataref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                        Cart cart1 = null;
                        if (dataSnapshot.exists()) {
                            int totalcount = 0;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                totalcount++;
                            }
                            int i = 0;
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                i++;
                                if (i == totalcount) {
                                    cart1 = snapshot.getValue(Cart.class);
                                }
                            }

                            if (RestaurantID.equals(cart1.getRestaurantId())) {
                                data = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(RestaurantID).child(RandomId);
                                data.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                        FoodModel update = dataSnapshot.getValue(FoodModel.class);
                                        dishname = update.getQuantity();
                                        dishprice = Integer.parseInt(update.getPrice());
                                        int num = Integer.parseInt(additem.getNumber());
                                        int totalprice = num * dishprice;
                                        if (num != 0) {
                                            HashMap<String, String> hashMap = new HashMap<>();
                                            hashMap.put("DishName", dishname);
                                            hashMap.put("DishID", RandomId);
                                            hashMap.put("DishQuantity", String.valueOf(num));
                                            hashMap.put("Price", String.valueOf(dishprice));
                                            hashMap.put("Totalprice", String.valueOf(totalprice));
                                            hashMap.put("RestaurantId", RestaurantID);
                                            custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                            reference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                                            reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(CustomerMenu.this, "Added to cart", Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        } else {
                                            firebaseDatabase.getInstance().getReference("Cart").child(custID).child(RandomId).removeValue();

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                    }
                                });
                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(CustomerMenu.this);
                                builder.setMessage("You can't add food items of multiple chef at a time. Try to add items of same chef");
                                builder.setCancelable(false);
                                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int i) {
                                        dialog.dismiss();
                                        Toast.makeText(CustomerMenu.this, "Haha", Toast.LENGTH_SHORT).show();

                                    }
                                });
                                AlertDialog alert = builder.create();
                                alert.show();
                            }
                        } else {
                            data = FirebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(RestaurantID).child(RandomId);
                            data.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                                    FoodModel update = dataSnapshot.getValue(FoodModel.class);
                                    dishname = update.getDishes();
                                    dishprice = Integer.parseInt(update.getPrice());
                                    int num = Integer.parseInt(additem.getNumber());
                                    int totalprice = num * dishprice;
                                    if(num!=0){
                                        HashMap<String, String> hashMap = new HashMap<>();
                                        hashMap.put("DishName", dishname);
                                        hashMap.put("DishID", RandomId);
                                        hashMap.put("DishQuantity", String.valueOf(num));
                                        hashMap.put("Price", String.valueOf(dishprice));
                                        hashMap.put("Totalprice", String.valueOf(totalprice));
                                        hashMap.put("ChefId", RestaurantID);
                                        custID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                        reference = FirebaseDatabase.getInstance().getReference("Cart").child("CartItems").child(custID).child(RandomId);
                                        reference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(CustomerMenu.this, "Added to cart", Toast.LENGTH_SHORT).show();

                                            }
                                        });
                                    }
                                    else {

                                        firebaseDatabase.getInstance().getReference("Cart").child(custID).child(RandomId).removeValue();
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                                }
                            });
                        }

                    }



                        @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
            }
        });

    }

}