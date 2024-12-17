package com.example.aplikasi_layanan_sampah;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText oldPasswordEditText, newPasswordEditText, confirmPasswordEditText, emailEditText;
    private Button changePasswordButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        oldPasswordEditText = findViewById(R.id.oldPasswordEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        confirmPasswordEditText = findViewById(R.id.confirmPasswordEditText);
        emailEditText = findViewById(R.id.emailEditText);  // Menambahkan email input
        changePasswordButton = findViewById(R.id.changePasswordButton);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        changePasswordButton.setOnClickListener(v -> {
            String oldPassword = oldPasswordEditText.getText().toString().trim();
            String newPassword = newPasswordEditText.getText().toString().trim();
            String confirmPassword = confirmPasswordEditText.getText().toString().trim();
            String email = emailEditText.getText().toString().trim();  // Ambil email dari input

            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
                Toast.makeText(ForgotPasswordActivity.this, "Semua kolom harus diisi", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                Toast.makeText(ForgotPasswordActivity.this, "Kata sandi baru tidak cocok", Toast.LENGTH_SHORT).show();
                return;
            }

            // Autentikasi ulang dengan password lama
            AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), oldPassword);

            currentUser.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    // Perbarui email jika berhasil
                    currentUser.updateEmail(email).addOnCompleteListener(updateEmailTask -> {
                        if (updateEmailTask.isSuccessful()) {
                            // Perbarui kata sandi setelah email berhasil diubah
                            currentUser.updatePassword(newPassword).addOnCompleteListener(updatePasswordTask -> {
                                if (updatePasswordTask.isSuccessful()) {
                                    Toast.makeText(ForgotPasswordActivity.this, "Email dan kata sandi berhasil diperbarui", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(ForgotPasswordActivity.this, "Gagal memperbarui kata sandi: " + updatePasswordTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(ForgotPasswordActivity.this, "Gagal memperbarui email: " + updateEmailTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Kata sandi lama salah", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
