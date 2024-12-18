package com.example.aplikasi_layanan_sampah;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ResiActivity extends AppCompatActivity {

    private TextView userNameTextView, totalPaymentTextView, timestampTextView;
    private Button backToMainButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resi);

        // Hubungkan komponen dengan XML
        userNameTextView = findViewById(R.id.userNameTextView);
        totalPaymentTextView = findViewById(R.id.totalPaymentTextView);
        timestampTextView = findViewById(R.id.timestampTextView);
        backToMainButton = findViewById(R.id.backToMainButton);

        // Ambil data dari Intent
        String userName = getIntent().getStringExtra("USER_NAME");
        double totalPayment = getIntent().getDoubleExtra("TOTAL_PAYMENT", 0.0);

        // Format timestamp
        String timestamp = new SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
                .format(new Date());

        // Set data ke TextView
        userNameTextView.setText("Nama: " + userName); // Nama saja, tanpa email
        totalPaymentTextView.setText("Total Pembayaran: Rp " + String.format("%.2f", totalPayment));
        timestampTextView.setText(timestamp); // Menampilkan timestamp tanpa label

        // Set fungsionalitas tombol
        backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pindah ke MainActivity (atau Activity lain yang diinginkan)
                Intent intent = new Intent(ResiActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Menutup ResiActivity
            }
        });
    }
}
