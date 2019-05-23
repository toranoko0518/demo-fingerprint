package io.github.toranoko0518.demo.fingerprint

import android.app.KeyguardManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricPrompt
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth.setOnClickListener {
            if (isBiometricEnabled(this)) {
                val executor = Executors.newSingleThreadExecutor()

                val biometricPrompt = BiometricPrompt(this, executor, callback)

                val promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Title")
                    .setSubtitle("Sub title")
                    .setDescription("Description")
                    .setNegativeButtonText("Cancel")
                    .build()

                biometricPrompt.authenticate(promptInfo)
            }
        }
    }

    /**
     * Decide if biometrics is enabled
     *
     * @param context Context
     * @return True when biometric enabled
     */
    private fun isBiometricEnabled(context: Context): Boolean =
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && context.getSystemService(KeyguardManager::class.java).isKeyguardSecure
                && context.packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)

    /**
     * Authentication callback
     */
    private val callback = object : BiometricPrompt.AuthenticationCallback() {
        override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
            super.onAuthenticationError(errorCode, errString)
            Log.d("Biometric", "Error")
        }

        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
            super.onAuthenticationSucceeded(result)
            Log.d("Biometric", "Succeeded")
        }

        override fun onAuthenticationFailed() {
            super.onAuthenticationFailed()
            Log.d("Biometric", "Failed")
        }
    }
}
