package com.example.aplikasi_layanan_sampah;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.AdapterView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class InputSampahActivity extends AppCompatActivity {

    private EditText quantityEditText;
    private Spinner wasteCategorySpinner;
    private Spinner wasteTypeSpinner;
    private EditText otherWasteEditText;
    private Button submitButton;

    // Inisialisasi Firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_sampah);

        // Mengambil referensi elemen UI
        quantityEditText = findViewById(R.id.quantityEditText);
        wasteCategorySpinner = findViewById(R.id.wasteCategorySpinner);
        wasteTypeSpinner = findViewById(R.id.typeSpinner);
        otherWasteEditText = findViewById(R.id.otherWasteEditText);
        submitButton = findViewById(R.id.submitButton);

        // Menambahkan kategori sampah ke Spinner kategori
        ArrayAdapter<CharSequence> categoryAdapter = ArrayAdapter.createFromResource(this,
                R.array.waste_categories, android.R.layout.simple_spinner_item);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wasteCategorySpinner.setAdapter(categoryAdapter);

        // Menambahkan event listener untuk spinner kategori sampah
        wasteCategorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Menyesuaikan jenis sampah berdasarkan kategori yang dipilih
                String selectedCategory = parentView.getItemAtPosition(position).toString();
                updateWasteTypeSpinner(selectedCategory, wasteTypeSpinner);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Kosongkan spinner jenis sampah jika tidak ada kategori yang dipilih
                updateWasteTypeSpinner("Pilih Kategori", wasteTypeSpinner);
            }
        });

        // Menambahkan event listener untuk spinner jenis sampah
        wasteTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedWasteType = parentView.getItemAtPosition(position).toString();
                if (selectedWasteType.equals("Lainnya")) {
                    // Tampilkan EditText untuk input jenis sampah lainnya
                    otherWasteEditText.setVisibility(View.VISIBLE);
                } else {
                    // Sembunyikan EditText jika jenis sampah bukan "Lainnya"
                    otherWasteEditText.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {}
        });

        submitButton.setOnClickListener(v -> {
            // Ambil input dari pengguna
            String quantity = quantityEditText.getText().toString().trim();
            String selectedCategory = wasteCategorySpinner.getSelectedItem().toString();
            String selectedWasteType = wasteTypeSpinner.getSelectedItem().toString();
            String otherWaste = otherWasteEditText.getText().toString().trim();

            // Validasi input
            if (quantity.isEmpty()) {
                Toast.makeText(InputSampahActivity.this, "Jumlah tidak boleh kosong", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedCategory.equals("Pilih Kategori")) {
                Toast.makeText(InputSampahActivity.this, "Pilih kategori sampah", Toast.LENGTH_SHORT).show();
                return;
            }
            if (selectedWasteType.equals("Pilih Jenis Sampah")) {
                Toast.makeText(InputSampahActivity.this, "Pilih jenis sampah", Toast.LENGTH_SHORT).show();
                return;
            }

            // Jika jenis sampah adalah "Lainnya", pastikan inputan nama sampah lainnya tidak kosong
            if (selectedWasteType.equals("Lainnya") && otherWaste.isEmpty()) {
                Toast.makeText(InputSampahActivity.this, "Harap masukkan nama sampah lainnya", Toast.LENGTH_SHORT).show();
                return;
            }

            // Kirim data ke Firebase
            saveDataToFirebase(quantity, selectedCategory, selectedWasteType, otherWaste);
        });
    }

    // Fungsi untuk memperbarui spinner jenis sampah sesuai dengan kategori yang dipilih
    private void updateWasteTypeSpinner(String selectedCategory, Spinner wasteTypeSpinner) {
        int wasteTypeArrayId = 0;

        // Tentukan array jenis sampah berdasarkan kategori yang dipilih
        switch (selectedCategory) {
            case "Organik":
                wasteTypeArrayId = R.array.organic_waste_types;
                break;
            case "Non-Organik":
                wasteTypeArrayId = R.array.non_organic_waste_types;
                break;
            case "B3":
                wasteTypeArrayId = R.array.b3_waste_types;
                break;
            default:
                wasteTypeArrayId = R.array.waste_categories;  // Kategori tidak valid
                break;
        }

        // Update spinner dengan array jenis sampah yang sesuai
        ArrayAdapter<CharSequence> wasteTypeAdapter = ArrayAdapter.createFromResource(this,
                wasteTypeArrayId, android.R.layout.simple_spinner_item);
        wasteTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        wasteTypeSpinner.setAdapter(wasteTypeAdapter);
    }

    // Fungsi untuk mengirim data ke Firebase Firestore
    private void saveDataToFirebase(String quantity, String category, String wasteType, String otherWaste) {
        // Menentukan jenis sampah
        String waste = wasteType.equals("Lainnya") ? otherWaste : wasteType;

        // Membuat object data untuk disimpan
        WasteData wasteData = new WasteData(quantity, category, waste);

        // Menyimpan data ke Firebase Cloud Firestore
        db.collection("waste_entries")
                .add(wasteData)
                .addOnSuccessListener(documentReference -> {
                    // Data berhasil disimpan, pindah ke RincianSampahActivity
                    Intent intent = new Intent(InputSampahActivity.this, RincianSampahActivity.class);
                    intent.putExtra("CATEGORY", category);
                    intent.putExtra("WASTE_TYPE", waste);
                    intent.putExtra("QUANTITY", quantity);
                    startActivity(intent);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(InputSampahActivity.this, "Gagal menyimpan data", Toast.LENGTH_SHORT).show();
                });
    }

}
