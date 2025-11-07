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
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
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
        installSplashScreen()
        setContentView(R.layout.activity_auth)
        //Iniciamos los componentes y Google Analytics
        initComponent()
        logAnalytics()

       //Comprobamos si el usuario ya esta logado
        if(auth.currentUser != null){
            //si lo está vamos al menú
            navigateToMenu()
        }else{
            //si no lo está iniciamos los listener de los botones
            initListeners()
        }
    }
    //Función para iniciar los componentes
    private fun initComponent() {
        auth = Firebase.auth
        firebaseAnalytics = Firebase.analytics
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
    }
    //Función para iniciar los listeners de los botones
    private fun initListeners() {

        btnRegister.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            register(email, password)
        }
        btnLogin.setOnClickListener {
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            access(email, password)
        }
    }
    //Función para iniciar Google Analytics
    private fun logAnalytics(){
        //Lanzamiento de eventos personalizados a Google Analytics
        val bundle = Bundle()
        bundle.putString("message", "Integración de Firebase completada")
        firebaseAnalytics.logEvent("InitScreen", bundle)

    }
    //Función para registrarse en Firebase
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
                    alert(it.exception)
                }
            }
        }

    }
    //Función para hacer login en Firebase
    private fun access(email: String, password: String) {
        if (email.isNotEmpty() && password.isNotEmpty()) {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful) {

                    Toast.makeText(
                        this,
                        "Se ha accedido con el email: $email",
                        Toast.LENGTH_SHORT
                    ).show()
                    navigateToMenu()
                } else {
                    alert(it.exception)
                }
            }
        }

    }

    //Función para mostrar mensaje de error a la hora de hacer login o registrarse
    private fun alert(exception: Exception?){
        val message = when(exception){
            is FirebaseAuthInvalidCredentialsException -> "La contraseña es incorrecta o la cuenta de usuario no existe."
            is FirebaseAuthUserCollisionException->"El email ya está registrado por otro usuario."
            else -> "Se ha producido un error al autenticar al usuario."
        }

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error de autenticación")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }
    /*Función para navegar al menú asegurando que el usuario, si pulsa el botón atrás
     *de su dispositivo no le mandará a hacer login de nuevo gracias al uso de "banderas"
     */
    private fun navigateToMenu() {
        val intent = Intent(this, MenuActivity::class.java).apply{
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        startActivity(intent)

    }

}