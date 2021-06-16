package com.example.restaurant;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;

public class RestaurantMenu extends AppCompatActivity implements View.OnClickListener {
     ImageButton imgUploadImage;
     EditText edtFoodName, edtFoodDes,edtFoodPrice;
     Button btnUploadFood;
     String dish,description,price, address;
    Uri imageuri;
     Uri mCropimageuri;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    DatabaseReference dataaa;
    FirebaseAuth FAuth;
    StorageReference ref;
    String RestaurantId;
    String RandomUId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        imgUploadImage = findViewById(R.id.food_image);
        edtFoodName = findViewById(R.id.food_name);
        edtFoodDes = findViewById(R.id.food_description);
        edtFoodPrice = findViewById(R.id.food_price);
        btnUploadFood = findViewById(R.id.food_uploadbutton);
        FAuth = FirebaseAuth.getInstance();
        btnUploadFood.setOnClickListener(this);

        databaseReference = firebaseDatabase.getInstance().getReference("FoodSupplyDetails");
        try{
            String userid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            dataaa = firebaseDatabase.getInstance().getReference("Restaurant").child("Islamabad").child(userid);
            dataaa.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
                    Restaurant restaurant = dataSnapshot.getValue(Restaurant.class);
                            address = restaurant.getAddress();

                    imgUploadImage.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onSelectImageClick(view);



                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull @NotNull DatabaseError error) {

                }
            });

        }
        catch (Exception e) {

            Log.e("Errrrrr: ", e.getMessage());
        }

    }

    private void uploadImage(){
        final ProgressDialog progressDialog = new ProgressDialog(RestaurantMenu.this);
        progressDialog.setTitle("Uploading...");
        progressDialog.show();
        RandomUId = UUID.randomUUID().toString();
        ref = storageReference.child(RandomUId);
        RestaurantId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        ref.putFile(imageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        FoodSupplyDetails info = new FoodSupplyDetails(dish, price, description, String.valueOf(uri), RandomUId, RestaurantId);
                        firebaseDatabase.getInstance().getReference("FoodSupplyDetails").child(address).child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(RandomUId)
                                .setValue(info).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                progressDialog.dismiss();
                                Toast.makeText(RestaurantMenu.this, "Dish posted successfully", Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

                progressDialog.dismiss();
                Toast.makeText(RestaurantMenu.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull @NotNull UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                progressDialog.setMessage("Uploaded " + (int) progress + "%");
                progressDialog.setCanceledOnTouchOutside(false);
            }
        });


    }



    private void onSelectImageClick(View v) {

        CropImage.startPickImageActivity(this);

    }

    @Override
    @SuppressLint("NewApi")

    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            imageuri = CropImage.getPickImageResultUri(this, data);

            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageuri)) {
                mCropimageuri = imageuri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 0);

            } else {

                startCropImageActivity(imageuri);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imgUploadImage.setImageURI(result.getUri());
                Toast.makeText(this, "Cropped Successfully", Toast.LENGTH_SHORT).show();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed" + result.getError(), Toast.LENGTH_SHORT).show();
            }
        }


        super.onActivityResult(requestCode, resultCode, data);    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {

        if (mCropimageuri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startCropImageActivity(mCropimageuri);
        } else {
            Toast.makeText(this, "cancelling,required permission not granted", Toast.LENGTH_SHORT).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }

    private void startCropImageActivity(Uri imageuri) {

        CropImage.activity(imageuri)
                .setGuidelines(CropImageView.Guidelines.ON)
                .setMultiTouchEnabled(true)
                .start(this);


    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.food_uploadbutton:
                dish = edtFoodName.getText().toString().trim();
                description = edtFoodDes.getText().toString().trim();
                price = edtFoodPrice.getText().toString().trim();
                uploadImage();
                break;

        }
    }
}




