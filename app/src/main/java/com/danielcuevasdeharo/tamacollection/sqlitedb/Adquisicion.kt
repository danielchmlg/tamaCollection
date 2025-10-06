package com.danielcuevasdeharo.tamacollection.sqlitedb

import java.sql.Date

data class Adquisicion(var adId: Int, var tamaId: Int, var comId: Int, var date: Date, var price: Double) {
}