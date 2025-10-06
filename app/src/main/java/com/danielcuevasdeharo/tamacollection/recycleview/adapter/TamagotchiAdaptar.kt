package com.danielcuevasdeharo.tamacollection.recycleview.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.danielcuevasdeharo.tamacollection.R
import com.danielcuevasdeharo.tamacollection.sqlitedb.Tamagotchi

class TamagotchiAdaptar( private val tamagotchiList : MutableList<Tamagotchi>): RecyclerView.Adapter<TamagotchiViewHolder> (){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TamagotchiViewHolder {

        val layoutInflater= LayoutInflater.from(parent.context)
        return TamagotchiViewHolder(layoutInflater.inflate(R.layout.item_tamagotchi, parent, false))


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