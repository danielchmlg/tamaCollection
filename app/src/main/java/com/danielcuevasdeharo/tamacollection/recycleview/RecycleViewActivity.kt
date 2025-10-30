package com.danielcuevasdeharo.tamacollection.recycleview

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danielcuevasdeharo.tamacollection.DetailsActivity
import com.danielcuevasdeharo.tamacollection.R
import com.danielcuevasdeharo.tamacollection.recycleview.adapter.TamagotchiAdaptar
import com.danielcuevasdeharo.tamacollection.sqlitedb.TamaSQLite



class RecycleViewActivity : AppCompatActivity() {
    private lateinit var mlTama: TamaSQLite
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recycle_view)
        initRecyclerView()

    }

    private fun initRecyclerView() {
        mlTama = TamaSQLite(this)
        val recyclerView = findViewById<RecyclerView>(R.id.recycleTama)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val onTamaClicked: (Int) ->Unit = { tamaId->
            navigateToDetails(tamaId)

        }
        recyclerView.layoutManager = LinearLayoutManager(this)

        recyclerView.adapter = TamagotchiAdaptar(mlTama.getAllTama(), onTamaClicked)
    }
    private fun navigateToDetails(tamaId: Int) {
        val intent = Intent(this, DetailsActivity::class.java).apply {
            // Empaquetamos el ID del Tamagotchi
            putExtra("TAMA_ID", tamaId)
        }
        startActivity(intent)
    }
}

