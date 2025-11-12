package com.danielcuevasdeharo.tamacollection.findtama

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.danielcuevasdeharo.tamacollection.R
import com.danielcuevasdeharo.tamacollection.sqlitedb.TamaSQLite
import com.google.firebase.auth.FirebaseAuth

class FindTamaActivity : AppCompatActivity() {
    private lateinit var btnFind: Button
    private lateinit var btnBackFind: Button
    private lateinit var etIdFind: EditText
    private lateinit var db: TamaSQLite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_tama)

        initComponent()
        initListener()

    }

    private fun initComponent() {
        btnFind = findViewById(R.id.btnFind)
        btnBackFind = findViewById(R.id.btnBackFind)
        etIdFind = findViewById(R.id.etIdFind)
        db = TamaSQLite(this)
    }

    private fun initListener() {
        //Configuramos botón de buscar
        btnFind.setOnClickListener {
            //Obtenemos usuario autenticado
            val userId = FirebaseAuth.getInstance().currentUser!!.uid
            //validamos que el campo no esté vacío
            val idText = etIdFind.text.toString().trim()
            if (idText.isEmpty()) {
                etIdFind.error = "Introduzca ID para buscar"
                return@setOnClickListener
            }
            //Validamos que el campo sea numérico
            val idNumber = idText.toIntOrNull()
            if (idNumber == null) {
                etIdFind.error = "El ID debe ser numérico"
                return@setOnClickListener
            }
            //Comprobamos si el id que se quiere registrar ya está registrado
            if ((!db.isIdInUse(idNumber, userId))) {
                Toast.makeText(
                    this,
                    " El ID $idNumber no se encuentra registrado",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }
            val intentFind = Intent(this, FindAuxTamaActivity::class.java)
            intentFind.putExtra("id", idText)
            startActivity(intentFind)
        }
        //Configuramos botón de volver
        btnBackFind.setOnClickListener { finish() }
    }

}