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

        val createTamasTableQuery = """
            CREATE TABLE tamas(
            id INTEGER PRIMARY KEY,
            name TEXT NOT NULL,
            generation TEXT NOT NULL,
            year INTEGER NOT NULL);
            
        """.trimIndent()
        //Aseguramos que no se recibirán valores nulos con "!!"
        db.execSQL(createTamasTableQuery)

        val createComercioTableQuery = """
            CREATE TABLE comercio(
            comId INTEGER PRIMARY KEY AUTOINCREMENT,
            comName TEXT NOT NULL,
            ubication TEXT NOT NULL
            );
        """.trimIndent()
        db.execSQL(createComercioTableQuery)

        val createAdquisicionTableQuery = """
            CREATE TABLE adquisicion(
            adId INTEGER PRIMARY KEY AUTOINCREMENT,
            tamaId INTEGER NOT NULL,
            comId INTEGER NOT NULL,
            date TEXT NOT NULL,
            price REAL NOT NULL,
            FOREIGN KEY (tamaId) REFERENCES tamas(id) ON DELETE CASCADE,
            FOREIGN KEY (comId) REFERENCES comercio(comId) ON DELETE CASCADE);
        """.trimIndent()
        db.execSQL(createAdquisicionTableQuery)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}


    /**
     * C R U D tamas
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
        var readTama = Tamagotchi(0, "", "", 0)

        if (cursor.moveToFirst()) {
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
            val generation = cursor.getString(cursor.getColumnIndexOrThrow("generation"))
            val year = cursor.getInt(cursor.getColumnIndexOrThrow("year"))

            readTama = Tamagotchi(id, name, generation, year)
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
     * C R U D comercio
     */

    fun insertComercio(comercio: Comercio): Long {
        val db = writableDatabase
        val contentValues = ContentValues()

        //No se incluye "comId" ya que es AUTOINCREMENT
        contentValues.put("comName", comercio.comName)
        contentValues.put("ubication", comercio.ubication)

        //Inserta la fila. Retorna el ID de la nueva fila o -1 si es erróneo
        val comercioIn = db.insert("comercio", null, contentValues)
        db.close()
        if (comercioIn > 0) {
            //Actualizar el objeto con el ID que le asignó la DB
            comercio.comId = comercioIn.toInt()
        }
        return comercioIn

    }

    fun readComercio(comId: Long): Comercio {
        //Obtenemos base de dato en formato lectura
        val db = readableDatabase
        //Creamos la consulta a la BBDD y la almacenamos en selectQuery
        val selecQuery = "SELECT * FROM comercio WHERE comId =?"
        //Instanciamos el cursor leerá los datos de cada columna
        val cursor: Cursor = db.rawQuery(selecQuery, arrayOf(comId.toString()))
        //creamos el objeto readCom que almacenará los resultados de la consulta
        var readCom = Comercio(0, "", "")

        if (cursor.moveToFirst()) {
            val comId = cursor.getInt(cursor.getColumnIndexOrThrow("comId"))
            val comName = cursor.getString(cursor.getColumnIndexOrThrow("comName"))
            val ubication = cursor.getString(cursor.getColumnIndexOrThrow("ubication"))


            readCom = Comercio(comId, comName, ubication)
        }
        cursor.close()
        db.close()
        return readCom

    }

    fun update(comId: Long, newInfoCom: Comercio): Int {
        //Obtenemos la base de datos en formato escritura
        val db = writableDatabase
        //Instanciamos a ContentValues para almacenar el conjunto de valores
        val values = ContentValues()
        //no incluimos comId ya que lo usamos en la cláusula where
        values.put("comName", newInfoCom.comName)
        values.put("ubication", newInfoCom.ubication)

        //Insertamos en la tabla "comercio" la nueva información
        val updateTama = db.update("comercio", values, "comId=?", arrayOf(comId.toString()))
        db.close()
        return updateTama

    }

    fun deleteComercio(comId: Long): Int {
        //Obtenemos la base de datos en formato escritura
        val db = writableDatabase
        // Llamamos al función delete que almacenaremos en deleteCom
        val deleteCom = db.delete("comercio", "comId=?", arrayOf(comId.toString()))
        db.close()
        return deleteCom
    }

    /**
     * C R U D adquisición
     */
    fun insertAdquisicion(adquisicion: Adquisicion): Long {
        val db = writableDatabase
        val contentValues = ContentValues()

        //No se incluye "adId" ya que es AUTOINCREMENT
        contentValues.put("tamaId", adquisicion.tamaId)
        contentValues.put("comId", adquisicion.comId)
        contentValues.put("date", adquisicion.date)
        contentValues.put("price", adquisicion.price)
        //Inserta la fila. Retorna el ID de la nueva fila o -1 si es erróneo
        val adquisicionIn = db.insert("adquisicion", null, contentValues)
        db.close()
        if (adquisicionIn > 0) {
            adquisicion.adId = adquisicionIn.toInt()
        }
        return adquisicionIn

    }

    fun readAdquisicion(adId: Long): Adquisicion {
        //Obtenemos base de dato en formato lectura
        val db = readableDatabase
        //Creamos la consulta a la BBDD y la almacenamos en selectQuery
        val selecQuery = "SELECT * FROM adquisicion WHERE adId =?"
        //Instanciamos el cursor leerá los datos de cada columna
        val cursor: Cursor = db.rawQuery(selecQuery, arrayOf(adId.toString()))
        //creamos el objeto readCom que almacenará los resultados de la consulta
        var readAd = Adquisicion(0, 0, 0, "", 0.0)

        if (cursor.moveToFirst()) {
            val adId = cursor.getInt(cursor.getColumnIndexOrThrow("adId"))
            val tamaIdForeign = cursor.getInt(cursor.getColumnIndexOrThrow("tamaId"))
            val comIdForeign = cursor.getInt(cursor.getColumnIndexOrThrow("comId"))
            val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
            val price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))


            readAd = Adquisicion(adId, tamaIdForeign, comIdForeign, date, price)
        }
        cursor.close()
        db.close()
        return readAd

    }

    fun update(adId: Long, newInfoAd: Adquisicion): Int {
        //Obtenemos la base de datos en formato escritura
        val db = writableDatabase
        //Instanciamos a ContentValues para almacenar el conjunto de valores
        val values = ContentValues()
        values.put("tamaId", newInfoAd.tamaId)
        values.put("comId", newInfoAd.comId)
        values.put("date", newInfoAd.date)
        values.put("price", newInfoAd.price)

        //Insertamos en la tabla "adquisicion" la nueva información
        val updateTama = db.update("adquisicion", values, "adId=?", arrayOf(adId.toString()))
        db.close()
        return updateTama

    }

    fun deleteAdquisicion(adId: Long): Int {
        //Obtenemos la base de datos en formato escritura
        val db = writableDatabase
        // Llamamos al función delete que almacenaremos en deleteCom
        val deleteAd = db.delete("adquisicion", "adId=?", arrayOf(adId.toString()))
        db.close()
        return deleteAd
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


                val tamagotchi = Tamagotchi(id, name, generation, year)
                mutableListTama.add(tamagotchi)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return mutableListTama

    }


}