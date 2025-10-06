package com.danielcuevasdeharo.tamacollection.recycleview

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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

        recyclerView.adapter = TamagotchiAdaptar(mlTama.getAllTama())
    }
}

