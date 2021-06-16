package com.example.restaurant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnDineIn, btnSignIn, btnSignUp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnDineIn = findViewById(R.id.btn_dineIn);
        btnSignUp = findViewById(R.id.btn_signUp);
        btnSignIn = findViewById(R.id.btn_signIn);

        btnSignIn.setOnClickListener(this);
        btnDineIn.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_signUp:
                Intent intent = new Intent(this, SignUp.class);
                startActivity(intent);
                break;
            case R.id.btn_signIn:
                Intent intent1 = new Intent(this, SignIn.class);
                startActivity(intent1);
                break;
            case R.id.btn_dineIn:
                Toast.makeText(this, "Dine In", Toast.LENGTH_SHORT).show();
                break;

        }

    }
}