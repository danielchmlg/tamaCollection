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
    private var isEditMode = false
    private var originalTamaId: String? = null
    private var originalAdId: Int? = null
    private var originalComId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_tama)

        initComponent()
        initListener()
        checkEditMode()

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

    private fun checkEditMode() {
        if (intent.getBooleanExtra("EDIT_MODE", false)) {
            isEditMode = true
            // Recuperamos los IDs que nos mandó la otra pantalla
            originalTamaId = intent.getStringExtra("TAMA_ID")
            originalAdId = intent.getIntExtra("AD_ID", -1)
            originalComId = intent.getIntExtra("COM_ID", -1)

            // Cambiamos el texto del botón y título para que el usuario sepa que edita
            btnSaveTama.text = "ACTUALIZAR"
           val title = findViewById<android.widget.TextView>(R.id.twNewAdd)
            title.text = "EDITAR TAMAGOTCHI"

            // Bloqueamos el campo ID para que no lo cambien (es la clave primaria)
            etIdTama.isEnabled = false

            // Cargamos los datos existentes
            loadDataForEdit()
        }
    }
    private fun loadDataForEdit() {
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        //Leemos el Tamagotchi (Tabla 'tamas')
        val tama = dbAdd.read(originalTamaId!!.toLong(), userId)

        //Leemos los detalles (Tablas 'comercio' y 'compra')
        // Usamos la función readTamaDetails
        val details = dbAdd.readTamaDetails(originalTamaId!!.toInt(), userId)

        if (tama != null && details != null) {
            // Rellenamos los campos de texto
            etIdTama.setText(tama.id.toString())
            etNameTama.setText(tama.name)
            etGenTama.setText(tama.generation)
            etYearTama.setText(tama.year.toString())

            // Datos de las otras tablas
            etComName.setText(details.comName)
            etComUbi.setText(details.ubication)
            etPriceTama.setText(details.price.toString())
            etAdqDate.setText(details.date)
        } else {
            Toast.makeText(this, "Error al cargar los datos para editar", Toast.LENGTH_SHORT).show()
            finish() // Si falla, cerramos para no dejar la pantalla vacía
        }
    }

    //Función para commprobar que los campos no estén vacios y se introduzcan los datos correctos
    private fun saveData() {
        // 1. Recogemos los datos de los EditText
        val idString = etIdTama.text.toString()
        val nameTama = etNameTama.text.toString().trim()
        val genTama = etGenTama.text.toString().trim()
        val yearString = etYearTama.text.toString()
        val priceString = etPriceTama.text.toString()
        val comName = etComName.text.toString().trim()
        val comUbi = etComUbi.text.toString().trim()
        val adqDate = etAdqDate.text.toString()

        //Validamos que no haya campos vacíos
        if (listOf(idString, nameTama, genTama, yearString, priceString, comName, comUbi, adqDate).any { it.isEmpty() }) {
            Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show()
            return
        }

        //Convertimos tipos numéricos de forma segura
        val idTama = idString.toIntOrNull()
        val yearTama = yearString.toIntOrNull()
        val priceTama = priceString.toDoubleOrNull()

        if (idTama == null || yearTama == null || priceTama == null) {
            Toast.makeText(this, "Por favor, introduzca números válidos para ID, Año y Precio", Toast.LENGTH_SHORT).show()
            return
        }

        //Validamos ID Duplicado (SOLO SI ES NUEVO)
        // Si estamos editando, es normal que el ID ya exista por lo que nos saltamos este check.
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        if (!isEditMode) {
            if (dbAdd.isIdInUse(idTama, userId)) {
                Toast.makeText(this, "El ID $idTama ya se encuentra registrado", Toast.LENGTH_SHORT).show()
                return
            }
        }

        try {
            //Preparamos los Objetos para guardar
            //Si es edición, usamos los IDs viejos. Si es nuevo, null (para autoincrement) o el ID nuevo.

            val tamaObj = Tamagotchi(idTama, nameTama, genTama, yearTama, userId)

            val comIdToUse = if (isEditMode) originalComId else null
            val comObj = Comercio(comIdToUse, comName, comUbi)

            val adIdToUse = if (isEditMode) originalAdId else null
            // Nota: comId se pone temporalmente a 0 o al viejo, luego se ajusta si es inserción
            val compraObj = Compra(
                adId = adIdToUse,
                tamaId = idTama,
                comId = comIdToUse ?: 0,
                date = adqDate,
                price = priceTama
            )

            //Ejecutamos la Transacción (Update o Insert)
            if (isEditMode) {
                // MODO EDITAR
                // Actualizamos las 3 tablas usando los IDs originales
                dbAdd.update(idTama.toLong(), tamaObj)
                dbAdd.update(originalComId!!.toLong(), comObj)

                // Aseguramos que el objeto compra tenga el ID de comercio correcto antes de actualizar
                compraObj.comId = originalComId!!
                dbAdd.update(originalAdId!!.toLong(), compraObj)

                Toast.makeText(this, "Registro actualizado correctamente", Toast.LENGTH_SHORT).show()

            } else {
                //MODO AÑADIR
                dbAdd.insert(tamaObj)
                val newComId = dbAdd.insertComercio(comObj)

                // Asignamos el ID del nuevo comercio a la compra
                compraObj.comId = newComId.toInt()
                dbAdd.insertCompra(compraObj)

                Toast.makeText(this, "Se ha registrado el id $idTama", Toast.LENGTH_SHORT).show()
            }

            //Cerramos la pantalla para volver
            finish()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error: No se pudo guardar el registro.", Toast.LENGTH_LONG).show()
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