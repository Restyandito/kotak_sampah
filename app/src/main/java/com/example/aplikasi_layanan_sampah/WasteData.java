package com.example.aplikasi_layanan_sampah;

public class WasteData {
    private String quantity;
    private String category;
    private String wasteType;

    // Konstruktor
    public WasteData(String quantity, String category, String wasteType) {
        this.quantity = quantity;
        this.category = category;
        this.wasteType = wasteType;
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
}
