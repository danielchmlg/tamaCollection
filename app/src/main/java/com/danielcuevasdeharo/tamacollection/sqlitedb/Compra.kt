package com.danielcuevasdeharo.tamacollection.sqlitedb

data class Compra(
    var adId: Int? = null,
    var tamaId: Int,
    var comId: Int,
    var date: String,
    var price: Double
)