package com.danielcuevasdeharo.tamacollection.recycleview.adapter

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.danielcuevasdeharo.tamacollection.R
import com.danielcuevasdeharo.tamacollection.sqlitedb.Tamagotchi

class TamagotchiViewHolder(view: View, onDetailsClick: (Int) -> Unit, onDeleteClick: (position: Int) -> Unit) : RecyclerView.ViewHolder(view) {

    val id = view.findViewById<TextView>(R.id.rvId)
    val name = view.findViewById<TextView>(R.id.rvName)
    val gen = view.findViewById<TextView>(R.id.rvGen)
    val year = view.findViewById<TextView>(R.id.rvYear)
    val btnDetails = view.findViewById<Button>(R.id.btnDetails)
    val btnDeleteItem = view.findViewById<Button>(R.id.btnDelteItem)

    init {
        btnDetails.setOnClickListener {
            // Comprobación de seguridad
            if (bindingAdapterPosition != RecyclerView.NO_POSITION) {
                //Avisa del clic en la posición actual
                onDetailsClick(bindingAdapterPosition)
            }
        }
        btnDeleteItem.setOnClickListener {
            //comprobación de seguridad
            if (bindingAdapterPosition != RecyclerView.NO_POSITION){
                //Avisa del click en la posición actual
                onDeleteClick(bindingAdapterPosition)
            }
        }
    }

    fun render(tamagotchi: Tamagotchi) {
        id.text = tamagotchi.id.toString()
        name.text = tamagotchi.name
        gen.text = tamagotchi.generation
        year.text = tamagotchi.year.toString()

    }

}