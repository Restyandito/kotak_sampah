package com.example.aplikasi_layanan_sampah;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MenuActivity extends AppCompatActivity {

    private Button loginButton, registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        // Inisialisasi tombol
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // Button Login
        loginButton.setOnClickListener(v -> {
            // Arahkan pengguna ke LoginActivity
            Intent intent = new Intent(MenuActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        // Button Register
        registerButton.setOnClickListener(v -> {
            // Arahkan pengguna ke RegisterActivity
            Intent intent = new Intent(MenuActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }
}
