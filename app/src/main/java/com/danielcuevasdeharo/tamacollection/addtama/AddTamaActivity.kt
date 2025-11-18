package com.danielcuevasdeharo.tamacollection.addtama

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.danielcuevasdeharo.tamacollection.R
import com.danielcuevasdeharo.tamacollection.sqlitedb.Compra
import com.danielcuevasdeharo.tamacollection.sqlitedb.Comercio
import com.danielcuevasdeharo.tamacollection.sqlitedb.TamaSQLite
import com.danielcuevasdeharo.tamacollection.sqlitedb.Tamagotchi
import com.google.firebase.auth.FirebaseAuth
import java.util.Calendar

private lateinit var dbAdd: TamaSQLite

class AddTamaActivity : AppCompatActivity() {
    private lateinit var btnSaveTama: Button
    private lateinit var btnBackAdd: Button
    private lateinit var etIdTama: EditText
    private lateinit var etNameTama: EditText
    private lateinit var etGenTama: EditText
    private lateinit var etYearTama: EditText
    private lateinit var etPriceTama: EditText
    private lateinit var etComName: EditText
    private lateinit var etComUbi: EditText
    private lateinit var etAdqDate: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_tama)

        initComponent()
        initListener()
    }
    //función para iniciar los componentes
    private fun initComponent() {
        dbAdd = TamaSQLite(this)
        btnSaveTama = findViewById(R.id.btnSaveTama)
        btnBackAdd = findViewById(R.id.btnBackAdd)
        etIdTama = findViewById(R.id.etIdTama)
        etNameTama = findViewById(R.id.etNameTam)
        etGenTama = findViewById(R.id.etGenTama)
        etYearTama = findViewById(R.id.etYearTama)
        etPriceTama = findViewById(R.id.etPriceTama)
        etComName = findViewById(R.id.etComName)
        etComUbi = findViewById(R.id.etComUbi)
        etAdqDate = findViewById(R.id.etAdqDate)
    }
    //Función para iniciar los Listeners
    private fun initListener() {
        etAdqDate.setOnClickListener { viewDatePicker() }
        btnBackAdd.setOnClickListener { finish() }
        btnSaveTama.setOnClickListener { saveData() }
    }

    //Función para commprobar que los campos no estén vacios y se introduzcan los datos correctos
    private fun saveData() {

        val idString = etIdTama.text.toString()
        val nameTama = etNameTama.text.toString().trim()
        val genTama = etGenTama.text.toString().trim()
        val yearString = etYearTama.text.toString()
        val priceString = etPriceTama.text.toString()
        val comName = etComName.text.toString().trim()
        val comUbi = etComUbi.text.toString().trim()
        val adqDate = etAdqDate.text.toString()

        if (listOf(
                idString,
                nameTama,
                genTama,
                yearString,
                priceString,
                comName,
                comUbi,
                adqDate
            ).any { it.isEmpty() }
        ) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }
        //convertimos las variables a números de forma segura:
        val idTama = idString.toIntOrNull()
        val yearTama = yearString.toIntOrNull()
        val priceTama = priceString.toDoubleOrNull()
        //Comprobamos que se introduzca números en los editText del id, año y precio
        if (idTama == null || yearTama == null || priceTama == null) {
            Toast.makeText(
                this,
                " Por favor, intruduzca un número válido para id, año y precio",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        //Comprobamos si el id que se quiere registrar ya está registrado
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        if (dbAdd.isIdInUse(idTama, userId)) {
            Toast.makeText(
                this,
                " El ID $idTama ya se encuentra registrado. Por favor use uno nuevo",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        try {
            //introducimos en la tabla tama
            val tamaAdd = Tamagotchi(idTama, nameTama, genTama, yearTama, userId)
            dbAdd.insert(tamaAdd)
            //Introducimos en la tabla comercio
            val comAdd = Comercio(comName = comName, ubication = comUbi)
            val idcom = dbAdd.insertComercio(comAdd)
            //Introducimos en la tabla compra
            val compraAdd = Compra(
                tamaId = idTama,
                comId = idcom.toInt(),
                date = adqDate,
                price = priceTama
            )
            dbAdd.insertCompra(compraAdd)

            //Mensaje de registro satisfactorio
            Toast.makeText(this, "Se ha registrado el id $idTama", Toast.LENGTH_SHORT).show()
            finish()
        } catch (e: Exception) {
            //Si cualquiera de las operaciones dentro del try falla la ejecución parará aquí
            e.printStackTrace()

            //Lanzamos mensaje de error
            Toast.makeText(
                this,
                "Error: No se pudo guardar el registro.",
                Toast.LENGTH_LONG
            ).show()
        }

    }

    private fun viewDatePicker() {
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
                    String.format("%d-%02d-%02d", selectedYear, correctedMonth, selectedDay)

                etAdqDate.setText(bbddDate)
            },
            year, month, day
        )
        datePickerDialog.show()


    }
}