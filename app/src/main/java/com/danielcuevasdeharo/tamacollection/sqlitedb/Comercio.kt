package com.danielcuevasdeharo.tamacollection.sqlitedb

/**
 * Creamos la data class de comercio dónde estableceremos "comId" como valor aún no asignado
 * por la base de datos.
 */

data class Comercio(var comId: Int? = null, var comName: String, var ubication: String)