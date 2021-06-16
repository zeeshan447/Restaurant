package com.example.restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import static android.content.ContentValues.TAG;

public class SignIn extends AppCompatActivity implements View.OnClickListener {
    private TextInputLayout edtEmail, edtPassword;
    private Button btnLogin;
    private TextView txtforgotpwd;
    private CheckBox chkboxRestaurant;
    FirebaseAuth FAuth;
    Switch active;
    String email, password;
    String emailpattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    String Role;
    String Random = "Restaurant";
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtEmail = findViewById(R.id.login_email);
        edtPassword = findViewById(R.id.login_password);
        txtforgotpwd = findViewById(R.id.login_frgtpwd);
        btnLogin = findViewById(R.id.login_button);
        btnLogin.setOnClickListener(this);
        txtforgotpwd.setOnClickListener(this);
//        active = findViewById(R.id.active);
         chkboxRestaurant = findViewById(R.id.rolecheck);


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_button:

////                DatabaseReference database = FirebaseDatabase.getInstance().getReference();
////                DatabaseReference ref = database.child("User");
////
////                ref.addListenerForSingleValueEvent(new ValueEventListener() {
////                    @Override
////                    public void onDataChange(@NonNull @NotNull DataSnapshot dataSnapshot) {
////                           User user = dataSnapshot.getValue(User.class);
////                           user.getRole();
////
////                            if(user.Role =="Restaurant"){
////                                restaurantSignIn();
////                            }
////
////                            else if(user.Role =="Customer") {
////                                signIn();
////
////                            }
////
////                    }
//
//                    @Override
//                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
//                       // Log.e(TAG, "onCancelled", databaseError.toException());
//
//                    }
//                });
//
//

if(chkboxRestaurant.isChecked()){
    restaurantSignIn();
}
else{
    signIn();
}

                break;

            case R.id.login_frgtpwd:
                Intent a=new Intent(SignIn.this,ForgotPassword.class);
                startActivity(a);

        }
    }

    public void signIn(){

         email = edtEmail.getEditText().getText().toString().trim();
         password = edtPassword.getEditText().getText().toString().trim();
        if(isValid()) {


    FAuth = FirebaseAuth.getInstance();

    final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
    mDialog.setCanceledOnTouchOutside(false);
    mDialog.setCancelable(false);
    mDialog.setMessage("Logging in...");
    mDialog.show();
    FAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
        @Override
        public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
            if (task.isSuccessful()) {
                mDialog.dismiss();
                if (FAuth.getCurrentUser().isEmailVerified()) {
                    Toast.makeText(SignIn.this, "You are logged in", Toast.LENGTH_SHORT).show();
                    Intent z = new Intent(SignIn.this, CustomerDashboard.class);
                    startActivity(z);
                    finish();

                } else {
                    ReusableCodeForAll.ShowAlert(SignIn.this, "", "Please Verify your Email");
                }
            } else {

                mDialog.dismiss();
                ReusableCodeForAll.ShowAlert(SignIn.this, "Error", task.getException().getMessage());
            }
        }
    });
}




    }

    public void restaurantSignIn(){
        String email = edtEmail.getEditText().getText().toString().trim();
        String password = edtPassword.getEditText().getText().toString().trim();

        FAuth = FirebaseAuth.getInstance();

        final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.setMessage("Logging in...");
        mDialog.show();
        FAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    mDialog.dismiss();
                    if (FAuth.getCurrentUser().isEmailVerified()){
                        Toast.makeText(SignIn.this, "You are logged in", Toast.LENGTH_SHORT).show();
                        Intent z = new Intent(SignIn.this, RestaurantDashboard.class);
                        startActivity(z);
                        finish();

                    }
                    else {
                        ReusableCodeForAll.ShowAlert(SignIn.this,"","Please Verify your Email");
                    }
                }
                else {

                    mDialog.dismiss();
                    ReusableCodeForAll.ShowAlert(SignIn.this,"Error",task.getException().getMessage());
                }
            }
        });





    }

    //String email, password;
    //private TextInputLayout edtEmail, edtPassword;

    public boolean isValid() {
        edtEmail.setErrorEnabled(false);
        edtEmail.setError("");
        edtPassword.setErrorEnabled(false);
        edtPassword.setError("");

        boolean isvalidemail=false,isvalidpassword=false,isvalid=false;
        if (TextUtils.isEmpty(email))
        {
            edtEmail.setErrorEnabled(true);
            edtEmail.setError("Email is required");
        }
        else {
            if (email.matches(emailpattern))
            {
                isvalidemail=true;
            }
            else {
                edtEmail.setErrorEnabled(true);
                edtEmail.setError("Enter a valid Email Address");
            }

        }
        if (TextUtils.isEmpty(password))
        {
            edtPassword.setErrorEnabled(true);
            edtPassword.setError("Password is required");
        }
        else {
            isvalidpassword=true;
        }
        isvalid = (isvalidemail && isvalidpassword) ? true : false;
        return isvalid;
    }
}