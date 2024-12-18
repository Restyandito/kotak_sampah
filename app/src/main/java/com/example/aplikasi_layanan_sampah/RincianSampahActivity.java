package com.example.aplikasi_layanan_sampah;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class RincianSampahActivity extends AppCompatActivity {

    private TextView dynamicRincianTextView, totalCostTextView;
    private Button btnAddMoreWaste, btnPay;

    // List untuk menyimpan rincian sampah yang sudah diinput
    private ArrayList<WasteData> wasteDataList = new ArrayList<>();
    private double totalCost = 0;
    private static final double PRICE_PER_KG = 500; // Harga per kilogram

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rincian_sampah);

        // Hubungkan komponen ke layout XML
        dynamicRincianTextView = findViewById(R.id.dynamicRincianTextView);
        totalCostTextView = findViewById(R.id.totalCostTextView);
        btnAddMoreWaste = findViewById(R.id.btnAddMoreWaste);
        btnPay = findViewById(R.id.btnPay);

        // Ambil data dari SharedPreferences (jika ada)
        loadWasteData();

        // Ambil data dari InputSampahActivity
        Intent intent = getIntent();
        String quantity = intent.getStringExtra("QUANTITY");
        String category = intent.getStringExtra("CATEGORY");
        String wasteType = intent.getStringExtra("WASTE_TYPE");

        // Jika ada data yang dikirim, tambahkan ke daftar dan hitung total biaya
        if (quantity != null && category != null && wasteType != null) {
            WasteData newWaste = new WasteData(quantity, category, wasteType);
            wasteDataList.add(newWaste);
            totalCost += Double.parseDouble(quantity) * PRICE_PER_KG;

            // Simpan data ke SharedPreferences
            saveWasteData();
        }

        // Tampilkan semua rincian sampah yang sudah ditambahkan
        displayAllWasteDetails();

        // Tombol untuk kembali ke InputSampahActivity dan menambahkan data baru
        btnAddMoreWaste.setOnClickListener(v -> {
            Intent inputIntent = new Intent(RincianSampahActivity.this, InputSampahActivity.class);
            startActivity(inputIntent);
        });

        btnPay.setOnClickListener(v -> {
            // Link pembayaran yang dituju
            String paymentLink = "https://app.sandbox.midtrans.com/payment-links/KOTAK-1734448722757";

            // Ambil nama pengguna dari SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
            String userName = sharedPreferences.getString("USER_NAME", "Nama Tidak Ditemukan");

            // Membuka browser dengan link pembayaran
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paymentLink));
            startActivity(browserIntent);

            // Setelah 7 detik, pindah ke halaman Resi
            new Handler().postDelayed(() -> {
                // Intent ke ResiActivity
                Intent resiIntent = new Intent(RincianSampahActivity.this, ResiActivity.class);
                resiIntent.putExtra("USER_NAME", userName); // Kirim nama user
                resiIntent.putExtra("TOTAL_PAYMENT", totalCost); // Kirim total pembayaran
                startActivity(resiIntent);

                // Kosongkan daftar sampah dan reset total biaya
                wasteDataList.clear();  // Menghapus semua data sampah
                totalCost = 0;  // Reset total biaya

                // Hapus data dari SharedPreferences
                SharedPreferences wasteSharedPreferences = getSharedPreferences("waste_data", MODE_PRIVATE);
                SharedPreferences.Editor editor = wasteSharedPreferences.edit();
                editor.clear();  // Menghapus semua data
                editor.apply();

                // Perbarui tampilan dengan data kosong
                displayAllWasteDetails(); // Menampilkan rincian yang kosong

                // Mengakhiri RincianSampahActivity agar tidak kembali ke sini
                finish();
            }, 7000); // Delay 7 detik
        });


    }

    // Fungsi untuk menampilkan semua rincian sampah dalam TextView
    private void displayAllWasteDetails() {
        StringBuilder detailsBuilder = new StringBuilder();

        // Loop untuk menambahkan setiap item ke StringBuilder
        for (int i = 0; i < wasteDataList.size(); i++) {
            WasteData waste = wasteDataList.get(i);
            detailsBuilder.append("Sampah ke-").append(i + 1).append("\n")
                    .append("Kategori: ").append(waste.getCategory()).append("\n")
                    .append("Jenis: ").append(waste.getWasteType()).append("\n")
                    .append("Jumlah: ").append(waste.getQuantity()).append(" kg\n\n");
        }

        // Tampilkan rincian di dynamicRincianTextView
        dynamicRincianTextView.setText(detailsBuilder.toString());

        // Tampilkan total biaya di totalCostTextView
        totalCostTextView.setText("Total Biaya: Rp " + String.format("%.2f", totalCost));
    }

    // Menyimpan data sampah ke SharedPreferences
    private void saveWasteData() {
        SharedPreferences sharedPreferences = getSharedPreferences("waste_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Menyimpan data dalam format JSON atau bisa juga dalam format lain sesuai kebutuhan
        StringBuilder data = new StringBuilder();
        for (WasteData waste : wasteDataList) {
            data.append(waste.getQuantity()).append(",")
                    .append(waste.getCategory()).append(",")
                    .append(waste.getWasteType()).append("\n");
        }
        editor.putString("waste_list", data.toString());
        editor.putFloat("total_cost", (float) totalCost);
        editor.apply();
    }

    // Memuat data sampah dari SharedPreferences
    private void loadWasteData() {
        SharedPreferences sharedPreferences = getSharedPreferences("waste_data", MODE_PRIVATE);
        String wasteList = sharedPreferences.getString("waste_list", "");
        if (!wasteList.isEmpty()) {
            String[] wasteItems = wasteList.split("\n");
            for (String wasteItem : wasteItems) {
                String[] parts = wasteItem.split(",");
                if (parts.length == 3) {
                    String quantity = parts[0];
                    String category = parts[1];
                    String wasteType = parts[2];
                    wasteDataList.add(new WasteData(quantity, category, wasteType));
                    totalCost += Double.parseDouble(quantity) * PRICE_PER_KG;
                }
            }
        }
    }

    // Class untuk merepresentasikan data sampah
    public static class WasteData {
        private String quantity;
        private String category;
        private String wasteType;

        public WasteData(String quantity, String category, String wasteType) {
            this.quantity = quantity;
            this.category = category;
            this.wasteType = wasteType;
        }

        public String getQuantity() {
            return quantity;
        }

        public String getCategory() {
            return category;
        }

        public String getWasteType() {
            return wasteType;
        }
    }
}

