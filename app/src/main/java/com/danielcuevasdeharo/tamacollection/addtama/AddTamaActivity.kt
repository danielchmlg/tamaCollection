package com.danielcuevasdeharo.tamacollection.addtama

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.danielcuevasdeharo.tamacollection.R
import com.danielcuevasdeharo.tamacollection.sqlitedb.Adquisicion
import com.danielcuevasdeharo.tamacollection.sqlitedb.Comercio
import com.danielcuevasdeharo.tamacollection.sqlitedb.TamaSQLite
import com.danielcuevasdeharo.tamacollection.sqlitedb.Tamagotchi
import java.util.Calendar

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
        val etComName = findViewById<EditText>(R.id.etComName)
        val etComUbi = findViewById<EditText>(R.id.etComUbi)
        val etAdqDate = findViewById<EditText>(R.id.etAdqDate)

        etAdqDate.setOnClickListener {
            viewDatePicker(etAdqDate)
        }

        btnSaveTama.setOnClickListener {
            val idTama = etIdTama.text.toString().toInt()
            val nameTama = etNameTama.text.toString()
            val genTama = etGenTama.text.toString()
            val yearTama = etYearTama.text.toString().toInt()
            val priceTama = etPriceTama.text.toString().toDouble()
            val comName = etComName.text.toString()
            val comUbi = etComUbi.text.toString()
            val adqDate = etAdqDate.text.toString()

            val tamaAdd = Tamagotchi(idTama, nameTama, genTama, yearTama)
            dbAdd.insert(tamaAdd)
            val comAdd = Comercio(comName = comName, ubication = comUbi)
            val idcom = dbAdd.insertComercio(comAdd)

            val adqAdd = Adquisicion(
                tamaId = idTama,
                comId = idcom.toInt(),
                date = adqDate,
                price = priceTama
            )
            dbAdd.insertAdquisicion(adqAdd)


            Toast.makeText(this, "Se ha registrado el id $idTama", Toast.LENGTH_SHORT).show()
            finish()

        }
        btnBackAdd.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                finish()
            }
        })


    }

    private fun initComponent() {
        dbAdd = TamaSQLite(this)
        btnSaveTama = findViewById(R.id.btnSaveTama)
        btnBackAdd = findViewById(R.id.btnBackAdd)
    }

    private fun viewDatePicker(etAdqDate: EditText) {
        val calendar = Calendar.getInstance()

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this, { _, selectedYear, selectedMonth, selectedDay ->
                // El mes aparece del 0 al 11 para corregirlo le sumamos 1
                val correctedMonth = selectedMonth + 1
                //Formateamos la fecha a YYYY-MM-DD para poder guardar como TEXT en la BBDD
                val bbddDate =
                    String.format("%d-%02d-%02d", selectedYear, selectedMonth, selectedDay)

                etAdqDate.setText(bbddDate)
            },
            year, month, day
        )
        datePickerDialog.show()


    }
}