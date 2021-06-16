package com.example.restaurant;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.HashMap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class CustomerSignUp extends Fragment implements View.OnClickListener {
    private DatePickerDialog datePickerDialog;
    private Button dateButton, Signup;
    private TextInputLayout cMail, cPassword, cName, cNumber;
    private RadioGroup cGender;
    private FirebaseAuth mAuth;
    private RadioButton Gender;
    DatabaseReference databaseReference;
    FirebaseDatabase firebaseDatabase;
    FirebaseAuth FAuth;
    String role = "Customer";
    ProgressDialog mDialog;
    TextInputLayout city;
    String  email,password,name,Dob,phone,address;
    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";






    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.customersignup, container, false);
        dateButton = view.findViewById(R.id.customerdate);
        initDatePicker();
        dateButton.setText(getTodaysDate());
        dateButton.setOnClickListener(this);
        cMail = view.findViewById(R.id.customerEmail);
        cPassword = view.findViewById(R.id.customerPassword);
        mAuth = FirebaseAuth.getInstance();
        cName = view.findViewById(R.id.customerName);
        cNumber = view.findViewById(R.id.customerPhone);
        Signup = view.findViewById(R.id.customerRegister);
        city = view.findViewById(R.id.customer_city_name);
        mDialog = new ProgressDialog(getContext());
        mDialog.setMessage("Registering please wait...");
        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        Signup.setOnClickListener(this);
        return view;
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                dateButton.setText(date);
            }
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(getContext(), style, dateSetListener, year, month, day);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";

        //default should never happen
        return "JAN";
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.customerdate:
                datePickerDialog.show();
                break;
            case R.id.customerRegister:
                    authentication();

             break;

        }
    }



    public void authentication(){
        try {


            email = cMail.getEditText().getText().toString().trim();
            password = cPassword.getEditText().getText().toString().trim();
            name = cName.getEditText().getText().toString().trim();
            Dob = dateButton.getText().toString().trim();
            phone = cNumber.getEditText().getText().toString().trim();
            address = city.getEditText().getText().toString().trim();

            if (isValid()) {

                databaseReference = firebaseDatabase.getInstance().getReference("Customer");
                FAuth = FirebaseAuth.getInstance();

                mDialog.show();

                FAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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
                                    hashMappp.put("EmailID", email);
                                    hashMappp.put("Name", name);
                                    hashMappp.put("Phone", phone);
                                    hashMappp.put("Password", password);
                                    hashMappp.put("Dob", Dob);
                                    hashMappp.put("Address", address);
                                    firebaseDatabase.getInstance().getReference("Customer")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(hashMappp).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            FAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull @NotNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        mDialog.dismiss();

                                                        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());

                                                        builder.setMessage("Registered Successfully,Please Verify your Email");
                                                        builder.setCancelable(false);
                                                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialog, int i) {
                                                                dialog.dismiss();
                                                                Intent b = new Intent(getContext(), MainActivity.class);
                                                                startActivity(b);
                                                            }
                                                        });
                                                        androidx.appcompat.app.AlertDialog alert = builder.create();
                                                        alert.show();

                                                    }
                                                    else if(!task.isSuccessful()) {
                                                        mDialog.dismiss();
                                                        ReusableCodeForAll.ShowAlert(getContext(), "Error", task.getException().getMessage());
                                                        Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();


                                                    }
                                                }
                                            });
                                        }


                                    });
                                }

                                ;
                            });


                        }
                    }
                });
            }
        }
        catch (Exception e) {
            mDialog.dismiss();
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    //private TextInputLayout cMail, cPassword, cName, cNumber;
   // String  email,password,name,Dob,phone,address;

    public boolean isValid(){
        cName.setErrorEnabled(false);
        cName.setError("");
        cMail.setErrorEnabled(false);
        cMail.setError("");
        cPassword.setErrorEnabled(false);
        cPassword.setError("");
        city.setErrorEnabled(false);
        city.setError("");
        cNumber.setErrorEnabled(false);
        cNumber.setError("");

        boolean isValidname = false,  isValidaddress = false, isValidemail = false, isvalidpassword = false, isvalid = false, isvalidmobileno = false;


        if (TextUtils.isEmpty(name)) {
            cName.setErrorEnabled(true);
            cName.setError("Name is required");
        } else {
            isValidname = true;
        }
        if (TextUtils.isEmpty(email)) {
            cMail.setErrorEnabled(true);
            cMail.setError("Email is required");
        }
        else
        {if (email.matches(emailpattern)) {
            isValidemail = true;
        } else {
            cMail.setErrorEnabled(true);
            cMail.setError("Enter a valid Email Address");
        }
        }
        if (TextUtils.isEmpty(password)) {
            cPassword.setErrorEnabled(true);
            cPassword.setError("Password is required");
        } else {
            if (password.length() < 6) {
                cPassword.setErrorEnabled(true);
                cPassword.setError("Password too weak");
            } else {
                isvalidpassword = true;
            }
        }
        if (TextUtils.isEmpty(phone)) {
            cNumber.setErrorEnabled(true);
            cNumber.setError("Mobile number is required");
        } else {
            if (phone.length() < 11) {
                cNumber.setErrorEnabled(true);
                cNumber.setError("Invalid mobile number");
            } else {
                isvalidmobileno = true;
            }
        }
        if (TextUtils.isEmpty(address)) {
            city.setErrorEnabled(true);
            city.setError("City is required");
        } else {
            isValidaddress = true;
        }

        isvalid = (isValidname  && isValidemail && isvalidpassword && isvalidmobileno && isValidaddress) ? true : false;
        return isvalid;
    }
}
