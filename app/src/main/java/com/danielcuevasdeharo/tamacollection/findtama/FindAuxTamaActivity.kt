package com.danielcuevasdeharo.tamacollection.findtama

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.danielcuevasdeharo.tamacollection.R
import com.danielcuevasdeharo.tamacollection.sqlitedb.TamaSQLite
import com.google.firebase.auth.FirebaseAuth

class FindAuxTamaActivity : AppCompatActivity() {
    private lateinit var btnDelete: Button
    private lateinit var btnBackData: Button
    private lateinit var db: TamaSQLite
    private lateinit var tvId: TextView
    private lateinit var tvName: TextView
    private lateinit var tvGen: TextView
    private lateinit var tvYear: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvComName: TextView
    private lateinit var tvComUbi: TextView
    private lateinit var tvAdqDate: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_find_aux_tama)

        initComponent()
        initListener()
        loadData()

    }

    private fun initComponent() {
        db = TamaSQLite(this)
        btnDelete = findViewById(R.id.btnDelete)
        btnBackData = findViewById(R.id.btnBackData)
        tvId = findViewById(R.id.tvIdTama)
        tvName = findViewById(R.id.tvNameTam)
        tvGen = findViewById(R.id.tvGenTama)
        tvYear = findViewById(R.id.tvYearTama)
        tvPrice = findViewById(R.id.tvPriceTama)
        tvComName = findViewById(R.id.tvComName)
        tvComUbi = findViewById(R.id.tvComUbi)
        tvAdqDate = findViewById(R.id.tvAdqDate)

    }

    private fun initListener() {
        btnBackData.setOnClickListener { finish() }

        btnDelete.setOnClickListener {
            //Usamos delete la tabla tama, al usar ON DELETE CASCADE el resto de tablas se borraran.
            val id = tvId.text.toString()
            //validamos que el Id no esté vacío
            if (id.isNotEmpty()) {
                db.delete(id.toLong())
                Toast.makeText(this, "Se ha borrado el id $id", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "Error: No se pudo obtener el ID", Toast.LENGTH_SHORT).show()

            }

        }

    }

    private fun loadData() {

        //Obtenemos el usuario autenticado

        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        //Obtenemos el ID que nos envió el Intent y los datos de los TextView
        val id = intent.getStringExtra("id")
        tvId.text = id
        tvName.text = db.read(id.toString().toLong(), userId)!!.name
        tvGen.text = db.read(id.toString().toLong(), userId)!!.generation
        tvYear.text = db.read(id.toString().toLong(), userId)!!.year.toString()
        tvPrice.text = db.readTamaDetails(id.toString().toInt(), userId)!!.price.toString()
        tvComName.text = db.readTamaDetails(id.toString().toInt(), userId)!!.comName
        tvComUbi.text = db.readTamaDetails(id.toString().toInt(), userId)!!.ubication
        tvAdqDate.text = db.readTamaDetails(id.toString().toInt(), userId)!!.date

    }
}