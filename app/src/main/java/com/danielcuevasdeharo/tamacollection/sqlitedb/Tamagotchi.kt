package com.danielcuevasdeharo.tamacollection.sqlitedb

/**
 * Creamos la data Class de Tamagotchi con la intención de guardar los datos recibidos
 * en las variables del objeto Tamagotchi
 */
data class Tamagotchi(
    var id: Int,
    var name: String,
    var generation: String,
    var year: Int
){}
