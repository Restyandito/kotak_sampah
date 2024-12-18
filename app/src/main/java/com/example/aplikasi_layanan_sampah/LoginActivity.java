package com.example.aplikasi_layanan_sampah;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private FirebaseAuth firebaseAuth;
    private TextView registerRedirectText, forgotPasswordText;
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); // Firebase Firestore instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerRedirectText = findViewById(R.id.registerRedirectText);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);

        // Initialize Firebase Auth
        firebaseAuth = FirebaseAuth.getInstance();

        // Login functionality
        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Email and Password must not be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Firebase sign-in
            firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            // Get user details
                            FirebaseUser user = firebaseAuth.getCurrentUser();
                            String userName = (user != null && user.getDisplayName() != null)
                                    ? user.getDisplayName() // Menggunakan displayName (nama lengkap)
                                    : user.getEmail().split("@")[0];

                            // Save user data to SharedPreferences
                            SharedPreferences sharedPreferences = getSharedPreferences("user_data", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("USER_NAME", userName);
                            editor.apply();

                            // Save user data to Firestore
                            saveUserToFirestore(user);

                            Toast.makeText(LoginActivity.this, "Selamat Datang!", Toast.LENGTH_SHORT).show();

                            // Send user details to MainActivity
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("USER_NAME", userName); // Passing user name to MainActivity
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Login Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        // Redirect to Registration Activity if the user doesn't have an account
        registerRedirectText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);  // Use RegisterActivity for registration
            startActivity(intent);
        });

        // Redirect to Forgot Password Activity
        forgotPasswordText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class); // ForgotPasswordActivity for reset password
            startActivity(intent);
        });
    }

    // Function to save user data to Firestore
    private void saveUserToFirestore(FirebaseUser user) {
        if (user != null) {
            // Membuat objek UserData untuk menyimpan data pengguna
            UserData userData = new UserData(user.getUid(), user.getEmail(), user.getDisplayName());

            // Menyimpan data pengguna ke Firestore
            db.collection("users")
                    .document(user.getUid())  // Menggunakan UID sebagai ID dokumen
                    .set(userData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(LoginActivity.this, "User data saved to Firestore", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(LoginActivity.this, "Error saving user data", Toast.LENGTH_SHORT).show();
                    });
        }
    }
}
