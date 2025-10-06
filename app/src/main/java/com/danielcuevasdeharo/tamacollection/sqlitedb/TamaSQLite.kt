package com.danielcuevasdeharo.tamacollection.sqlitedb

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TamaSQLite(context: Context) : SQLiteOpenHelper(context, "tama.db", null, 1) {
    /**
     * Al tratarse de una clase que abstracta es necesario implementar los métodos
     * onCreate y onUpgrade
     */
    override fun onCreate(db: SQLiteDatabase) {

        val createTableQuery = """
            CREATE TABLE tamas(
            id INTEGER PRIMARY KEY,
            name TEXT NOT NULL,
            generation TEXT NOT NULL,
            year INTEGER NOT NULL,
            price REAL NOT NULL);
            
        """.trimIndent()
        //Aseguramos que no se recibirán valores nulos con "!!"
        db.execSQL(createTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}


    /**
     * C R U D
     */
    //Función para insertar datos en nuestra tabla "tamas"
    fun insert(newTama: Tamagotchi): Long {
        //Obtengo base de datos en formato escritura
        val db = writableDatabase
        //Instancia de ContentValues para almacenar un conjunto de valores
        val values = ContentValues()
        //Agregamos los pares clave-valor a nuestro ContentValues
        values.put("id", newTama.id)
        values.put("name", newTama.name)
        values.put("generation", newTama.generation)
        values.put("year", newTama.year)
        values.put("price", newTama.price)
        //Insertamos en la tabla "tamas"
        val tamaIn = db.insert("tamas", null, values)
        db.close()
        return tamaIn

    }

    fun read(idTama: Long): Tamagotchi {
        //Obtenemos base de dato en formato lectura
        val db = readableDatabase
        //Creamos la consulta a la BBDD y la almacenamos en selectQuery
        val selecQuery = "SELECT * FROM tamas WHERE id =?"
        //Instanciamos el cursor leerá los datos de cada columna
        val cursor: Cursor = db.rawQuery(selecQuery, arrayOf(idTama.toString()))
        //creamos el objeto readTama que almacenará los resultados de la consulta
        var readTama = Tamagotchi(0, "", "", 0, 0.0)

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val generation = cursor.getString(cursor.getColumnIndexOrThrow("generation"))
            val year = cursor.getInt(cursor.getColumnIndexOrThrow("year"))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))

            readTama = Tamagotchi(id, name, generation, year, price)
        }
        cursor.close()
        db.close()
        return readTama

    }

    fun update(idTama: Long, newInfoTama: Tamagotchi): Int {
        //Obtenemos la base de datos en formato escritura
        val db = writableDatabase
        //Instanciamos a ContentValues para almacenar el conjunto de valores
        val values = ContentValues()
        values.put("id", newInfoTama.id)
        values.put("name", newInfoTama.name)
        values.put("generation", newInfoTama.generation)
        values.put("year", newInfoTama.year)
        values.put("price", newInfoTama.price)
        //Insertamos en la tabla "tamas" la nueva información
        val updateTama = db.update("tamas", values, "id=?", arrayOf(idTama.toString()))
        db.close()
        return updateTama

    }

    fun delete(idTama: Long): Int {
        //Obtenemos la base de datos en formato escritura
        val db = writableDatabase
        // Llamamos al función delete que almacenaremos en deleteTama
        val deleteTama = db.delete("tamas", "id=?", arrayOf(idTama.toString()))
        db.close()
        return deleteTama
    }

    /**
     * Función para obtener el número de Tamagotchis de la colección
     */

    fun getNumTama(): Int {
        val db = readableDatabase
        val selectQuery = "SELECT count (*) as numTama FROM tamas"
        val cursor: Cursor = db.rawQuery(selectQuery, null)
        var num = 0
        if (cursor.moveToFirst()) {
            num = cursor.getInt(cursor.getColumnIndexOrThrow("numTama"))
        }
        cursor.close()
        db.close()
        return num
    }

    /**
     * Función para recorrer lor almacenado en la tabla tamas
     */
    fun miColection(): Cursor {
        val db = readableDatabase
        val selectQuery = "SELECT name FROM tamas"
        val cursor: Cursor = db.rawQuery(selectQuery, null)
        return cursor
    }

    fun getAllTama(): MutableList<Tamagotchi> {
        //Creamos la MutableList
        val mutableListTama = mutableListOf<Tamagotchi>()
        //Obtenemos base de dato en formato lectura
        val db = this.readableDatabase
        //Creamos la consulta a la BBDD y la almacenamos en selectQuery
        val selecQuery = "SELECT * FROM tamas"
        //Instanciamos el cursor leerá los datos de cada columna
        val cursor: Cursor = db.rawQuery(selecQuery, null)


        if (cursor.moveToFirst()) {
            do {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val generation = cursor.getString(cursor.getColumnIndexOrThrow("generation"))
                val year = cursor.getInt(cursor.getColumnIndexOrThrow("year"))
                val price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))

                val tamagotchi = Tamagotchi(id, name, generation, year, price)
                mutableListTama.add(tamagotchi)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return mutableListTama

    }


}