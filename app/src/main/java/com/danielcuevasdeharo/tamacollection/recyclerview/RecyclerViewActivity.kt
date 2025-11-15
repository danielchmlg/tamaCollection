package com.danielcuevasdeharo.tamacollection.recyclerview

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.danielcuevasdeharo.tamacollection.DetailsActivity
import com.danielcuevasdeharo.tamacollection.R
import com.danielcuevasdeharo.tamacollection.recyclerview.adapter.TamagotchiAdaptar
import com.danielcuevasdeharo.tamacollection.sqlitedb.TamaSQLite
import com.danielcuevasdeharo.tamacollection.sqlitedb.Tamagotchi
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth


class RecyclerViewActivity : AppCompatActivity() {
    private lateinit var mlTama: TamaSQLite
    private lateinit var fabBack: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_recycler_view)
        initComponent()
        initListener()
        initRecyclerView()


    }

    private fun initComponent() {
        mlTama = TamaSQLite(this)
        fabBack = findViewById(R.id.fabReturn)
    }

    private fun initListener() {
        fabBack.setOnClickListener { finish() }
    }

    private fun initRecyclerView() {

        val recyclerView = findViewById<RecyclerView>(R.id.recycleTama)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val userId = FirebaseAuth.getInstance().currentUser!!.uid
        val onDetailsClicked: (Tamagotchi) -> Unit = { tamagotchi ->
            navigateToDetails(tamagotchi.id) //usamos el id del objeto recibido por la fución lambda

        }
        val onDeleteClicked: (Tamagotchi) -> Unit = { tamagotchi ->
            //Borra el tamagotchi de la BBDD
            mlTama.delete(tamagotchi.id.toLong())
            //Obtenemos la lista actualizada desde la base de datos
            val updatedList = mlTama.getAllTama(userId)
            //Actualiza el adaptador con la nueva lista
            (recyclerView.adapter as? TamagotchiAdaptar)?.updateData(updatedList)

        }

        recyclerView.adapter =
            TamagotchiAdaptar(mlTama.getAllTama(userId), onDetailsClicked, onDeleteClicked)
    }

    private fun navigateToDetails(tamaId: Int) {
        val intent = Intent(this, DetailsActivity::class.java)
        // Empaquetamos el ID del Tamagotchi
        intent.putExtra("TAMA_ID", tamaId.toString())
        startActivity(intent)


    }
}

