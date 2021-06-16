package com.example.restaurant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import static android.app.Activity.RESULT_OK;
import static com.theartofdev.edmodo.cropper.CropImage.startPickImageActivity;

public class RestaurantSignUp extends Fragment {

    private TextInputLayout edtRestaurantName, edtRestaurantEmail, edtRestaurantPassword,edtRestaurantCapacity;
    private TextInputLayout edtRestaurantAddress, edtRestaurantPhone;
    private Button btnRestaurantRegister;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;

    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";


   String resName, resEmail,resPassword,resAddress,resPhone, resCapacity;


    FirebaseAuth FAuth;
    String role = "Restaurant";
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.restaurant_sign_up, container, false);

        edtRestaurantName = view.findViewById(R.id.restaurantName);
        edtRestaurantEmail = view.findViewById(R.id.restaurantEmail);
        edtRestaurantPassword = view.findViewById(R.id.restaurantPassword);
        edtRestaurantCapacity = view.findViewById(R.id.restautantCapacity);
        edtRestaurantAddress = view.findViewById(R.id.restaurantAddress);
        edtRestaurantPhone = view.findViewById(R.id.restaurantPhone);
        btnRestaurantRegister = view.findViewById(R.id.restaurantRegister);
        //restimgUploadImage = view.findViewById(R.id.restaurant_image);
        btnRestaurantRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 resEmail = edtRestaurantEmail.getEditText().getText().toString().trim();
                 resPassword = edtRestaurantPassword.getEditText().getText().toString().trim();
                 resName = edtRestaurantName.getEditText().getText().toString().trim();
                 resPhone = edtRestaurantPhone.getEditText().getText().toString().trim();
                 resAddress = edtRestaurantAddress.getEditText().getText().toString().trim();
                 resCapacity = edtRestaurantCapacity.getEditText().getText().toString().trim();
                if (isValid()) {
                    final ProgressDialog mDialog = new ProgressDialog(getContext());
                    mDialog.setCancelable(false);
                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.setMessage("Registering please wait...");
                    mDialog.show();
                    databaseReference = firebaseDatabase.getInstance().getReference("Restaurant");
                    FAuth = FirebaseAuth.getInstance();


                    FAuth.createUserWithEmailAndPassword(resEmail, resPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                String useridd = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                databaseReference = FirebaseDatabase.getInstance().getReference("User").child(useridd);
                                final HashMap<String, String> hashMap = new HashMap<>();
                                hashMap.put("Role", role);
                                User user = new User(role,useridd);
                                user.setRole(role);
                                databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        HashMap<String, String> hashMappp = new HashMap<>();
                                        hashMappp.put("EmailID", resEmail);
                                        hashMappp.put("Name", resName);
                                        hashMappp.put("Phone", resPhone);
                                        hashMappp.put("Password", resPassword);
                                        hashMappp.put("Address", resAddress);
                                        hashMappp.put("Capacity", resCapacity);

                                        firebaseDatabase.getInstance().getReference("Restaurant").child(resAddress)
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(hashMappp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                FAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

                                                            builder.setMessage("Registered Successfully,Please Verify your Email");
                                                            builder.setCancelable(false);
                                                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                                    Intent b = new Intent(getContext(), MainActivity.class);
                                                                    startActivity(b);
                                                                }
                                                            });
                                                            androidx.appcompat.app.AlertDialog alert = builder.create();
                                                            alert.show();
                                                        } else {
                                                            mDialog.dismiss();
                                                            ReusableCodeForAll.ShowAlert(getContext(), "Error", task.getException().getMessage());
                                                        }
                                                    }

                                                });

                                            }

                                        });


                                    }

                                });


                            }

                        }

                    });

                }

            }
        });



        return view;
    }





    public boolean isValid(){
        edtRestaurantName.setErrorEnabled(false);
        edtRestaurantName.setError("");
        edtRestaurantEmail.setErrorEnabled(false);
        edtRestaurantEmail.setError("");
        edtRestaurantPassword.setErrorEnabled(false);
        edtRestaurantPassword.setError("");
        edtRestaurantCapacity.setErrorEnabled(false);
        edtRestaurantCapacity.setError("");
        edtRestaurantAddress.setErrorEnabled(false);
        edtRestaurantAddress.setError("");
        edtRestaurantPhone.setErrorEnabled(false);
        edtRestaurantPhone.setError("");
        boolean isValidname = false,  isValidaddress = false, isValidemail = false, isvalidpassword = false, isvalid = false, isvalidmobileno = false, isvalidcapcity=false;


        if (TextUtils.isEmpty(resName)) {
            edtRestaurantName.setErrorEnabled(true);
            edtRestaurantName.setError("Name is required");
        } else {
            isValidname = true;
        }
        if (TextUtils.isEmpty(resEmail)) {
            edtRestaurantEmail.setErrorEnabled(true);
            edtRestaurantEmail.setError("Email is required");
        }
        else
            {if (resEmail.matches(emailpattern)) {
            isValidemail = true;
        } else {
            edtRestaurantEmail.setErrorEnabled(true);
            edtRestaurantEmail.setError("Enter a valid Email Address");
        }
        }
        if (TextUtils.isEmpty(resPassword)) {
            edtRestaurantPassword.setErrorEnabled(true);
            edtRestaurantPassword.setError("Password is required");
        } else {
            if (resPassword.length() < 6) {
                edtRestaurantPassword.setErrorEnabled(true);
                edtRestaurantPassword.setError("Password too weak");
            } else {
                isvalidpassword = true;
            }
        }
        if (TextUtils.isEmpty(resPhone)) {
            edtRestaurantPhone.setErrorEnabled(true);
            edtRestaurantPhone.setError("Mobile number is required");
        } else {
            if (resPhone.length() < 11) {
                edtRestaurantPhone.setErrorEnabled(true);
                edtRestaurantPhone.setError("Invalid mobile number");
            } else {
                isvalidmobileno = true;
            }
        }
        if (TextUtils.isEmpty(resAddress)) {
            edtRestaurantAddress.setErrorEnabled(true);
            edtRestaurantAddress.setError("City is required");
        } else {
            isValidaddress = true;
        }
        if(TextUtils.isEmpty(resCapacity)){
            edtRestaurantCapacity.setErrorEnabled(true);
            edtRestaurantCapacity.setError("Capacity is required");
        }
        else{
            isvalidcapcity = true;
        }
        isvalid = (isValidname  && isValidemail && isvalidpassword && isvalidmobileno && isValidaddress && isvalidcapcity ) ? true : false;
        return isvalid;
    }

    //      //  resName, resEmail,resPassword,resAddress,resPhone;
    //edtRestaurantName, edtRestaurantEmail, edtRestaurantPassword,edtRestaurantCapacity;
//      // edtRestaurantAddress, edtRestaurantPhone;

//    boolean isEmail(EditText text) {
//        CharSequence email = text.getText().toString();
//        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
//    }
//
//    boolean isEmpty(EditText text) {
//        CharSequence str = text.getText().toString();
//        return TextUtils.isEmpty(str);
//    }
//
//
//    public void isValid(){
//       if(isEmpty(edtRestaurantName)){
//           Toast.makeText(getContext(), "Name is Empty", Toast.LENGTH_SHORT).show();
//       }
//
//}




}
