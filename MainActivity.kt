package com.example.proyectohuella

import android.hardware.biometrics.BiometricPrompt
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Asegúrate de que tu actividad use el diseño adecuado

        val executor = ContextCompat.getMainExecutor(this)

        // Configura el botón de huella dactilar
        Button() botonHuella=(Button)findViewByid(R.id.botonHuella);

        botonHuella.setOnClickListener {
            val biometricPrompt = BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    // La autenticación de huella dactilar ha tenido éxito, puedes almacenar datos aquí
                    // Por ejemplo, puedes guardar datos en SharedPreferences
                    val sharedPreferences = getSharedPreferences("MiPreferencia", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("clave", "valor") // Reemplaza "clave" y "valor" con los datos que quieras almacenar
                    editor.apply()
                }
            })

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación de huella dactilar")
                .setSubtitle("Coloca tu dedo en el sensor")
                .setNegativeButtonText("Cancelar")
                .build()

            biometricPrompt.authenticate(promptInfo)
        }


        }
    }


