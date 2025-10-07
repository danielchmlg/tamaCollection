package com.danielcuevasdeharo.tamacollection.sqlitedb

import java.sql.Date

data class Adquisicion(var adId: Int?=null, var tamaId: Int, var comId: Int, var date: String, var price: Double) {
}