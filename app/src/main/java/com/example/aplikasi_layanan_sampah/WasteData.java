package com.example.aplikasi_layanan_sampah;

import com.google.firebase.firestore.FieldValue;

public class WasteData {
    private String quantity;
    private String category;
    private String wasteType;
    private Object timestamp;  // Field untuk timestamp

    // Konstruktor
    public WasteData(String quantity, String category, String wasteType) {
        this.quantity = quantity;
        this.category = category;
        this.wasteType = wasteType;
        this.timestamp = FieldValue.serverTimestamp();  // Menambahkan timestamp menggunakan server timestamp
    }

    // Getter dan Setter
    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getWasteType() {
        return wasteType;
    }

    public void setWasteType(String wasteType) {
        this.wasteType = wasteType;
    }

    public Object getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Object timestamp) {
        this.timestamp = timestamp;
    }
}
