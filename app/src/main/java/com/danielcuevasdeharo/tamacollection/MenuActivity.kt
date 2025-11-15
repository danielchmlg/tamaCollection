package com.danielcuevasdeharo.tamacollection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.danielcuevasdeharo.tamacollection.addtama.AddTamaActivity
import com.danielcuevasdeharo.tamacollection.autentication.AuthActivity
import com.danielcuevasdeharo.tamacollection.findtama.FindTamaActivity
import com.danielcuevasdeharo.tamacollection.recyclerview.RecyclerViewActivity
import com.google.firebase.auth.FirebaseAuth


class MenuActivity : AppCompatActivity() {
    private lateinit var btnClose: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var btnNewTama: Button
    private lateinit var btnFindTama: Button
    private lateinit var btnColTama: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        //inicializamos auth antes de usar
        auth = FirebaseAuth.getInstance()
        initComponent()
        initListeners()

    }

    private fun initComponent() {
        btnClose = findViewById(R.id.btnClose)
        btnNewTama = findViewById(R.id.btnNewTama)
        btnFindTama = findViewById(R.id.btnFindTama)
        btnColTama = findViewById(R.id.btnColTama)
    }

    /*Cómo usaremos varios setOnClickListener, vamos a crear una función para simplificarlo
   *Esta función recibirá por parámetros una clase java y un boolean inicializado en false
   * Este boolean va a permitir establecer "banderas" para determinar si es necesario que las activities
   * anteriores queden accesibles o no ya que en el caso de cerrar sesión nos interesa que no queden
   * accesibles para que no puedan volver atrás y accedan a pantallas privadas de una sesión.
   */
    private fun navigate(to: Class<*>, clearTask: Boolean = false) {
        Intent(this, to).apply {
            //si clear task es true, entonces usaremos las banderas para dejar inaccesible las activities anteriores
            if (clearTask) {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            startActivity(this)
        }

    }

    private fun initListeners() {
        btnClose.setOnClickListener {
            //Para evitar doble click sobre el botón
            it.isEnabled = false
            //mensaje al cerrar sesión
            Toast.makeText(
                this,
                "Sesión cerrada, ¡hasta pronto!",
                Toast.LENGTH_SHORT
            ).show()
            //Cierre de sesión
            auth.signOut()
            //Navegamos a la pantalla de inicio de sesión
            navigate(AuthActivity::class.java, true)

        }

        btnNewTama.setOnClickListener { navigate(AddTamaActivity::class.java) }
        btnFindTama.setOnClickListener { navigate(FindTamaActivity::class.java) }
        btnColTama.setOnClickListener { navigate(RecyclerViewActivity::class.java) }
    }


}