package com.example.aplikasi_layanan_sampah;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private FirebaseAuth firebaseAuth;
    private TextView registerRedirectText, forgotPasswordText;

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
                                    ? user.getDisplayName()
                                    : user.getEmail(); // Fallback to email if no displayName

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
}
