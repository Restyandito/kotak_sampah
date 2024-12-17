package com.example.aplikasi_layanan_sampah;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager2.widget.ViewPager2;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Referensi TextView
        TextView userNameTextView = findViewById(R.id.userNameTextView);
        TextView currentDateTextView = findViewById(R.id.currentDateTextView);

        // Ambil nama pengguna dari Intent
        String userEmail = getIntent().getStringExtra("USER_NAME");

        // Ekstrak nama dari email (jika userName berisi email)
        String userName = extractUserName(userEmail);

        // Dapatkan sapaan berdasarkan waktu
        String greeting = getGreeting();

        // Tampilkan sapaan dan nama pengguna
        userNameTextView.setText(greeting + ", " + userName + "!");

        // Dapatkan tanggal saat ini
        String currentDate = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date());
        currentDateTextView.setText(currentDate);

        // Tombol Menu Input Sampah
        Button inputSampahButton = findViewById(R.id.inputSampahButton);
        inputSampahButton.setOnClickListener(v -> {
            // Navigasi ke activity Input Sampah
            Intent intent = new Intent(MainActivity.this, InputSampahActivity.class);
            startActivity(intent);
        });

        // Tombol Jadwal Pengambilan
        CardView scheduleCardView = findViewById(R.id.scheduleCardView);
        scheduleCardView.setOnClickListener(v -> {
            // Navigasi ke activity Jadwal Pengambilan
            Intent intent = new Intent(MainActivity.this, JadwalPengambilanActivity.class);
            startActivity(intent);
        });

        // Tombol Edukasi Pengguna
        CardView educationCardView = findViewById(R.id.educationCardView);
        educationCardView.setOnClickListener(v -> {
            // Navigasi ke activity Edukasi Pengguna
            Intent intent = new Intent(MainActivity.this, EdukasiPenggunaActivity.class);
            startActivity(intent);
        });

        // Tombol Kategori Sampah Organik
        CardView organicCardView = findViewById(R.id.organicCardView);
        organicCardView.setOnClickListener(v -> {
            // Navigasi ke activity untuk kategori sampah Organik
            Intent intent = new Intent(MainActivity.this, OrganikActivity.class);
            startActivity(intent);
        });

        // Tombol Kategori Sampah Non-Organik
        CardView nonOrganicCardView = findViewById(R.id.nonOrganicCardView);
        nonOrganicCardView.setOnClickListener(v -> {
            // Navigasi ke activity untuk kategori sampah Non-Organik
            Intent intent = new Intent(MainActivity.this, NonOrganikActivity.class);
            startActivity(intent);
        });

        // Tombol Kategori Sampah B3
        CardView b3CardView = findViewById(R.id.b3CardView);
        b3CardView.setOnClickListener(v -> {
            // Navigasi ke activity untuk kategori sampah B3
            Intent intent = new Intent(MainActivity.this, B3Activity.class);
            startActivity(intent);
        });

        // Setup image slider
        ViewPager2 viewPager2 = findViewById(R.id.imageSlider);
        int[] images = {
                R.drawable.image1,
                R.drawable.image2,
                R.drawable.image3
        };

        ImageAdapter imageAdapter = new ImageAdapter(images);
        viewPager2.setAdapter(imageAdapter);

        // Set timer untuk slider otomatis
        startAutoSlider(viewPager2, imageAdapter);
    }

    /**
     * Metode untuk menentukan sapaan berdasarkan waktu perangkat
     *
     * @return String sapaan (Selamat Pagi, Siang, Sore, atau Malam)
     */
    private String getGreeting() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);

        if (hour >= 4 && hour < 12) {
            return "Selamat Pagi";
        } else if (hour >= 12 && hour < 15) {
            return "Selamat Siang";
        } else if (hour >= 15 && hour < 18) {
            return "Selamat Sore";
        } else {
            return "Selamat Malam";
        }
    }

    /**
     * Metode untuk mengambil nama pengguna dari email
     *
     * @param email String email pengguna
     * @return String nama pengguna
     */
    private String extractUserName(String email) {
        if (email != null && email.contains("@")) {
            return email.substring(0, email.indexOf("@"));
        } else if (email != null && !email.isEmpty()) {
            return email; // Jika bukan email, gunakan string apa adanya
        }
        return "User"; // Default jika tidak ada nama
    }

    /**
     * Fungsi untuk memulai slider otomatis
     */
    private void startAutoSlider(ViewPager2 viewPager2, ImageAdapter imageAdapter) {
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int currentPage = 0;

            @Override
            public void run() {
                if (currentPage == imageAdapter.getItemCount()) {
                    currentPage = 0;
                }
                viewPager2.setCurrentItem(currentPage++, true);
                handler.postDelayed(this, 3000);  // Set interval 3 detik
            }
        };

        handler.postDelayed(runnable, 3000);  // Mulai slider otomatis setelah 3 detik
    }
}
