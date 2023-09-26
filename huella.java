import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.proyectohuella.BiometricPrompt;

import java.util.concurrent.Executor;

public class huella extends AppCompatActivity {

    private Button botonHuella;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botonHuella = findViewById(R.id.botonHuella);

        // Configura un executor para manejar la autenticación de huella digital
        Executor executor = ContextCompat.getMainExecutor(this);

        // Configura el diálogo de autenticación de huella digital
        BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                // La autenticación de huella dactilar ha tenido éxito, realiza acciones aquí
                mostrarMensaje("Autenticación exitosa");
            }

            @Override
            public void onAuthenticationError(int errorCode, CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                // Se ha producido un error en la autenticación de huella digital
                mostrarMensaje("Error de autenticación: " + errString);
            }
        });

        // Maneja el clic en el botón de huella digital
        botonHuella.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Autenticación de huella digital")
                        .setSubtitle("Coloca tu dedo en el sensor")
                        .setNegativeButtonText("Cancelar")
                        .build();

                biometricPrompt.authenticate(promptInfo);
            }
        });
    }

    private void mostrarMensaje(String mensaje) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();
    }
}
