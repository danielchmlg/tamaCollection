package com.danielcuevasdeharo.tamacollection.recycleview.adapter

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.danielcuevasdeharo.tamacollection.R
import com.danielcuevasdeharo.tamacollection.sqlitedb.Tamagotchi

class TamagotchiViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val id = view.findViewById<TextView>(R.id.rvId)
    val name = view.findViewById<TextView>(R.id.rvName)
    val gen = view.findViewById<TextView>(R.id.rvGen)
    val year = view.findViewById<TextView>(R.id.rvYear)
    val price = view.findViewById<TextView>(R.id.rvPrice)

    fun render(tamagotchi: Tamagotchi) {
        id.text = tamagotchi.id.toString()
        name.text = tamagotchi.name
        gen.text = tamagotchi.generation
        year.text = tamagotchi.year.toString()
        price.text = tamagotchi.price.toString()
    }

}