package com.example.aplikasi_layanan_sampah;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.imageview.ShapeableImageView;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {

    private int[] images;

    // Konstruktor untuk menerima array gambar
    public ImageAdapter(int[] images) {
        this.images = images;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Gunakan LayoutInflater untuk menghindari kesalahan
        ShapeableImageView shapeableImageView = new ShapeableImageView(parent.getContext());
        shapeableImageView.setScaleType(ShapeableImageView.ScaleType.CENTER_CROP);
        shapeableImageView.setLayoutParams(new ViewPager2.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        // Set border melengkung pada gambar
        shapeableImageView.setShapeAppearanceModel(
                shapeableImageView.getShapeAppearanceModel()
                        .toBuilder()
                        .setTopLeftCornerSize(30f) // Atur sesuai kebutuhan (menggunakan px)
                        .setTopRightCornerSize(30f) // Atur sesuai kebutuhan
                        .setBottomLeftCornerSize(30f) // Atur sesuai kebutuhan
                        .setBottomRightCornerSize(30f) // Atur sesuai kebutuhan
                        .build()
        );

        return new ImageViewHolder(shapeableImageView);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        // Menampilkan gambar berdasarkan posisi
        holder.shapeableImageView.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return images.length;
    }

    // ViewHolder untuk ShapeableImageView
    public static class ImageViewHolder extends RecyclerView.ViewHolder {

        ShapeableImageView shapeableImageView;

        public ImageViewHolder(View itemView) {
            super(itemView);
            shapeableImageView = (ShapeableImageView) itemView;
        }
    }
}
