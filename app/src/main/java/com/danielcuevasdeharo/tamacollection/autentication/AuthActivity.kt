package com.danielcuevasdeharo.tamacollection.autentication

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.danielcuevasdeharo.tamacollection.MenuActivity
import com.danielcuevasdeharo.tamacollection.R
import com.google.firebase.Firebase
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class AuthActivity : AppCompatActivity() {
    //Declaración de nuestro objeto fireBaseAnalytics
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var auth: FirebaseAuth
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Thread.sleep(3000)
        installSplashScreen()
        setContentView(R.layout.activity_auth)

        initComponent()
        //Lanzamiento de eventos personalizados a Google Analytics
        firebaseAnalytics = Firebase.analytics
        val bundle = Bundle()
        bundle.putString("message", "Integración de Firebase completada")
        firebaseAnalytics.logEvent("InitScreen", bundle)


        // Preparación para el login

        initListeners()

    }

    private fun initComponent() {
        auth = Firebase.auth
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
    }

    private fun initListeners() {

        btnRegister.setOnClickListener {
            val email = findViewById<EditText>(R.id.etEmail).text.toString()
            val password = findViewById<EditText>(R.id.etPassword).text.toString()
            register(email, password)
        }
        btnLogin.setOnClickListener {
            val email = findViewById<EditText>(R.id.etEmail).text.toString()
            val password = findViewById<EditText>(R.id.etPassword).text.toString()
            access(email, password)
        }
    }

    private fun register(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Se ha registrado con el email: $email",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigateToMenu()

                } else {
                    alert()
                }
            }
        }

    }

    private fun access(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {
                    navigateToMenu()
                    Toast.makeText(
                        this,
                        "Se ha accedido con el email: $email",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    alert()
                }
            }
        }

    }

    private fun alert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error al acceder/registrar en la aplicación")
        builder.setPositiveButton("Cerrar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun navigateToMenu() {
        intent = Intent(this, MenuActivity::class.java)

        startActivity(intent)

    }

}