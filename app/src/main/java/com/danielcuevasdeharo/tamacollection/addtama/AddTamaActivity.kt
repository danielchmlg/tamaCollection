package com.danielcuevasdeharo.tamacollection.addtama

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.danielcuevasdeharo.tamacollection.MenuActivity
import com.danielcuevasdeharo.tamacollection.R
import com.danielcuevasdeharo.tamacollection.sqlitedb.TamaSQLite
import com.danielcuevasdeharo.tamacollection.sqlitedb.Tamagotchi

private lateinit var dbAdd: TamaSQLite

class AddTamaActivity : AppCompatActivity() {
    private lateinit var btnSaveTama: Button
    private lateinit var btnBackAdd: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_tama)

        initComponent()

        val etIdTama = findViewById<EditText>(R.id.etIdTama)
        val etNameTama = findViewById<EditText>(R.id.etNameTam)
        val etGenTama = findViewById<EditText>(R.id.etGenTama)
        val etYearTama = findViewById<EditText>(R.id.etYearTama)
        val etPriceTama = findViewById<EditText>(R.id.etPriceTama)

        btnSaveTama.setOnClickListener {
                val idTama = etIdTama.text.toString().toInt()
                val nameTama = etNameTama.text.toString()
                val genTama = etGenTama.text.toString()
                val yearTama = etYearTama.text.toString().toInt()
                val priceTama = etPriceTama.text.toString().toDouble()

                val tamaAdd = Tamagotchi(idTama, nameTama, genTama, yearTama)

                dbAdd.insert(tamaAdd)

                Toast.makeText(this, "Se ha registrado el id $idTama", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MenuActivity::class.java)
                startActivity(intent)

        }
        btnBackAdd.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                finish()
            }
        })


    }
    private fun initComponent(){
        dbAdd = TamaSQLite(this)
        btnSaveTama = findViewById(R.id.btnSaveTama)
        btnBackAdd = findViewById(R.id.btnBackAdd)
    }
}