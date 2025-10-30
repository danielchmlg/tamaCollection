package com.danielcuevasdeharo.tamacollection

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.danielcuevasdeharo.tamacollection.sqlitedb.TamaSQLite

class DetailsActivity : AppCompatActivity() {
    private lateinit var btnBackDetails : Button
    private lateinit var explorer: TamaSQLite

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details)
        val id = intent.getStringExtra("TAMA_ID")

        val price = findViewById<TextView>(R.id.tvPrice)
        val name = findViewById<TextView>(R.id.tvName)
        val ubi = findViewById<TextView>(R.id.tvUbi)
        val date = findViewById<TextView>(R.id.tvDate)

        explorer = TamaSQLite(this)
        price.text= explorer.readTamaDetails(id.toString().toInt())!!.price.toString()
        name.text =explorer.readTamaDetails(id.toString().toInt())!!.comName
        ubi.text= explorer.readTamaDetails(id.toString().toInt())!!.ubication
        date.text=explorer.readTamaDetails(id.toString().toInt())!!.date

        btnBackDetails= findViewById<Button>(R.id.btnBackDetails)

        btnBackDetails.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {

                finish()
            }

        })

    }

}