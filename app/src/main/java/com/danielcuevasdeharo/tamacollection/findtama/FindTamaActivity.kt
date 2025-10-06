package com.danielcuevasdeharo.tamacollection.findtama

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.danielcuevasdeharo.tamacollection.R

class FindTamaActivity : AppCompatActivity() {
    private lateinit var btnFind: Button
    private lateinit var btnBackFind: Button
    private lateinit var idFind: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_tama)
        btnFind = findViewById(R.id.btnFind)
        btnBackFind = findViewById(R.id.btnBackFind)
        idFind = findViewById(R.id.etIdFind)

        btnFind.setOnClickListener {

            val id = idFind.text
            val intentFind = Intent(this, FindAuxTamaActivity::class.java)
            intentFind.putExtra("id", id.toString())
            startActivity(intentFind)
        }

        btnBackFind.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                finish()
            }
        })

    }

}