package com.danielcuevasdeharo.tamacollection.findtama

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.danielcuevasdeharo.tamacollection.MenuActivity
import com.danielcuevasdeharo.tamacollection.R
import com.danielcuevasdeharo.tamacollection.sqlitedb.TamaSQLite

class FindAuxTamaActivity : AppCompatActivity() {
    private lateinit var btnDelete: Button
    private lateinit var btnBackData: Button
    private lateinit var explorer: TamaSQLite
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_find_aux_tama)
        val id = intent.getStringExtra("id")
        val idFind = findViewById<TextView>(R.id.tvIdTama)
        val nameFind = findViewById<TextView>(R.id.tvNameTam)
        val genFind = findViewById<TextView>(R.id.tvGenTama)
        val yearFind = findViewById<TextView>(R.id.tvYearTama)
        val priceFind = findViewById<TextView>(R.id.tvPriceTama)
        explorer = TamaSQLite(this)

        idFind.text = id
        nameFind.text = explorer.read(id.toString().toLong()).name
        genFind.text = explorer.read(id.toString().toLong()).generation
        yearFind.text = explorer.read(id.toString().toLong()).year.toString()
        //priceFind.text = explorer.read(id.toString().toLong()).price.toString() estara en adquisicion

        btnDelete = findViewById(R.id.btnDelete)
        btnBackData = findViewById(R.id.btnBackData)

        btnDelete.setOnClickListener {

            explorer.delete(id.toString().toLong())
            Toast.makeText(this, "Se ha borrado el id $id", Toast.LENGTH_SHORT).show()
            navigateToMain()
        }
        btnBackData.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                finish()
            }

        })
    }

    private fun navigateToMain() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
    }
}