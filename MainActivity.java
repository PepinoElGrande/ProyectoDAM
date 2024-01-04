package com.example.proyectohuella;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import android.os.Handler;
import android.os.Looper;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;
import android.widget.EditText;


import com.google.android.material.snackbar.Snackbar;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static androidx.biometric.BiometricPrompt.ERROR_NEGATIVE_BUTTON;

import javax.crypto.Cipher;

public class MainActivity extends AppCompatActivity {
    Button prueba;
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.AuthenticationCallback authenticationCallback;
    private DBHelperHuella dbHelperHuella;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        prueba = (Button) findViewById(R.id.prueba);
        Button authenticateButton=findViewById(R.id.authenticate_button);
        executor = Executors.newSingleThreadExecutor();
        dbHelperHuella = new DBHelperHuella(this);

        authenticateButton.setOnClickListener(v -> authenticateWithFingerprint());
        prueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), mainloginActivity.class);
                startActivity(intent);
            }
        });
    }
    private void authenticateWithFingerprint() {
        biometricPrompt = new BiometricPrompt(this, executor, getAuthenticationCallback());
        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación con Huella Dactilar")
                .setSubtitle("Coloca tu dedo en el sensor de huella")
                .setNegativeButtonText("Cancelar")
                .build();
        biometricPrompt.authenticate(promptInfo);
    }

    private BiometricPrompt.AuthenticationCallback getAuthenticationCallback() {
        if (authenticationCallback == null) {
            authenticationCallback = new BiometricPrompt.AuthenticationCallback() {
                @Override
                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                    showToast("Error de autenticación: " + errString);
                }

                @Override
                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                    showToast("Autenticación exitosa");
                    saveFingerprintId("ID_DE_HUELLA"); // Reemplaza con la lógica para obtener un identificador único
                }

                @Override
                public void onAuthenticationFailed() {
                    showToast("Autenticación fallida");
                }
            };
        }
        return authenticationCallback;
    }

    private void showToast(String message) {
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show());
    }

    private void saveFingerprintId(String fingerprintId) {
        // Genera un identificador único basado en la información de la huella
        String uniqueId = generateUniqueIdFromFingerprint(fingerprintId);

        // Verifica si la huella ya existe en la base de datos
        if (dbHelperHuella.isFingerprintExist(uniqueId)) {
            showToast("La huella dactilar ya está registrada en la base de datos");
            return;
        }

        // Guarda el identificador único en la base de datos
        long id = dbHelperHuella.insertFingerprint(uniqueId);

        if (id != -1) {
            showToast("ID de huella guardado en la base de datos: " + id);
        } else {
            showToast("Error al guardar ID de huella en la base de datos");
        }
    }
    private String generateUniqueIdFromFingerprint(String fingerprintId) {
        try {
            // Utiliza SHA-256 para generar un hash de la información de la huella
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(fingerprintId.getBytes());

            // Convierte el hash a una cadena hexadecimal
            StringBuilder hexString = new StringBuilder();
            for (byte hashByte : hashBytes) {
                String hex = Integer.toHexString(0xFF & hashByte);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }



}