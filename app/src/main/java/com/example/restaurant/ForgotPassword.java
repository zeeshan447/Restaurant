package com.example.restaurant;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import org.jetbrains.annotations.NotNull;

public class ForgotPassword extends AppCompatActivity implements View.OnClickListener {

    private EditText edtForgotEmail;
    private Button btnForgotSubmit;
    FirebaseAuth FAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        edtForgotEmail = findViewById(R.id.forgotPasswordEmail);
        btnForgotSubmit = findViewById(R.id.forgotPasswordSubmit);
        btnForgotSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        passwordReset();

    }
    public void passwordReset(){

        final ProgressDialog mDialog = new ProgressDialog(ForgotPassword.this);
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.setCancelable(false);
        mDialog.setMessage("Logging in...");
        mDialog.show();
        FAuth.sendPasswordResetEmail(edtForgotEmail.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                if (task.isSuccessful()) {
                    mDialog.dismiss();
                    ReusableCodeForAll.ShowAlert(ForgotPassword.this,"","Password has been sent to your Email");
                } else {
                    mDialog.dismiss();
                    ReusableCodeForAll.ShowAlert(ForgotPassword.this,"Error",task.getException().getMessage());
                }
            }
        });
    }
}