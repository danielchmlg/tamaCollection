package com.danielcuevasdeharo.tamacollection.recyclerview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.danielcuevasdeharo.tamacollection.R
import com.danielcuevasdeharo.tamacollection.sqlitedb.Tamagotchi

class TamagotchiAdapter(
    private var tamagotchiList: MutableList<Tamagotchi>,
    private val onDetailsClicked: (Tamagotchi) -> Unit,
    private val onDeleteClicked: (Tamagotchi) -> Unit
) : RecyclerView.Adapter<TamagotchiViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TamagotchiViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_tamagotchi, parent, false)
        return TamagotchiViewHolder(
            view, onDetailsClick = { position ->
                //cuando el ViewHolder nos avise de un click en una posición
                val tamagotchi = tamagotchiList[position] // buscamos el objeo
                onDetailsClicked(tamagotchi) // pasamos el objeto a la Activity
            },
            onDeleteClick = { position ->
                val tamagotchi = tamagotchiList[position]
                onDeleteClicked(tamagotchi)
            }
        )

    }

    // Función para actualizar la lista desde la Activity
    fun updateData(newList: MutableList<Tamagotchi>) {
        this.tamagotchiList = newList
        notifyDataSetChanged() // Notifica al adapter que los datos han cambiado
    }


    override fun onBindViewHolder(
        holder: TamagotchiViewHolder,
        position: Int
    ) {
        val item = tamagotchiList[position]
        holder.render(item)
    }

    override fun getItemCount(): Int = tamagotchiList.size

}