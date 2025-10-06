package com.danielcuevasdeharo.tamacollection

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.danielcuevasdeharo.tamacollection.addtama.AddTamaActivity
import com.danielcuevasdeharo.tamacollection.autentication.AuthActivity
import com.danielcuevasdeharo.tamacollection.findtama.FindTamaActivity
import com.danielcuevasdeharo.tamacollection.recycleview.RecycleViewActivity
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
        initComponent()
        initListeners()

    }

    private fun initComponent(){
        btnClose= findViewById(R.id.btnClose)
        btnNewTama= findViewById(R.id.btnNewTama)
        btnFindTama= findViewById(R.id.btnFindTama)
        btnColTama= findViewById(R.id.btnColTama)
    }
    private fun initListeners(){
        btnClose.setOnClickListener {
            Toast.makeText(
                this,
                "Sesión cerrada, ¡hasta pronto!",
                Toast.LENGTH_SHORT).show()
            auth.signOut()

            val intent = Intent (this, AuthActivity::class.java)
            startActivity(intent)

        }

        btnNewTama.setOnClickListener {
            val intent= Intent(this, AddTamaActivity::class.java)
            startActivity(intent)
        }
        btnFindTama.setOnClickListener {
            val intent= Intent(this, FindTamaActivity::class.java)
            startActivity(intent)
        }
        btnColTama.setOnClickListener {
            val intent = Intent(this, RecycleViewActivity::class.java)
            startActivity(intent)
        }
    }

}