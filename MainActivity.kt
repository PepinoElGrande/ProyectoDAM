import android.hardware.biometrics.BiometricPrompt
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val executor = ContextCompat.getMainExecutor(this)

        // Configura el botón de huella dactilar
        botonHuella.setOnClickListener {
            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Autenticación de huella dactilar")
                .setSubtitle("Coloca tu dedo en el sensor")
                .setNegativeButtonText("Cancelar")
                .build()

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

            biometricPrompt.authenticate(promptInfo)
        }
    }
}
