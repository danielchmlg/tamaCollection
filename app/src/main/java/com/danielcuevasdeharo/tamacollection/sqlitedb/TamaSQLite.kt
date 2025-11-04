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
    //Función para que se tengan en cuenta las restricciones de id foráneas
    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

    /**
     * C R U D tamas
     */
    //Función para insertar datos en nuestra tabla "tamas"
    fun insert(newTama: Tamagotchi): Long {
        var db: SQLiteDatabase? = null
        try {
            //Obtengo base de datos en formato escritura
            db = writableDatabase
            //Instancia de ContentValues para almacenar un conjunto de valores
            val values = ContentValues()
            //Agregamos los pares clave-valor a nuestro ContentValues
            values.put("id", newTama.id)
            values.put("name", newTama.name)
            values.put("generation", newTama.generation)
            values.put("year", newTama.year)
            //Insertamos en la tabla "tamas"
            val tamaIn = db.insert("tamas", null, values)
            return tamaIn
        } finally {
            db?.close()
        }

    }

    fun read(idTama: Long): Tamagotchi? { //devuelve un Tamagotchi si existen datos

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            //Obtenemos base de dato en formato lectura
            db = readableDatabase
            //Creamos la consulta a la BBDD y la almacenamos en selectQuery
            val selecQuery = "SELECT * FROM tamas WHERE id =?"
            //Instanciamos el cursor leerá los datos de cada columna
            cursor = db.rawQuery(selecQuery, arrayOf(idTama.toString()))
            //creamos el objeto readTama que almacenará los resultados de la consulta
            var readTama = Tamagotchi(0, "", "", 0)

            if (cursor.moveToFirst()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val generation = cursor.getString(cursor.getColumnIndexOrThrow("generation"))
                val year = cursor.getInt(cursor.getColumnIndexOrThrow("year"))
                //Devolvemos objeto readTama si el cursor no esta vacío
                readTama = Tamagotchi(id, name, generation, year)
                return readTama
            }

        } finally {
            //bloque que siempre se va a ejecutar
            cursor?.close()
            db?.close()
        }
        //Si el cursor esta vacío devuelve nulo
        return null

    }

    fun update(idTama: Long, newInfoTama: Tamagotchi): Int {

        var db: SQLiteDatabase? = null
        //Obtenemos la base de datos en formato escritura
        try {
            db = writableDatabase
            //Instanciamos a ContentValues para almacenar el conjunto de valores
            val values = ContentValues()
            values.put("id", newInfoTama.id)
            values.put("name", newInfoTama.name)
            values.put("generation", newInfoTama.generation)
            values.put("year", newInfoTama.year)

            //Insertamos en la tabla "tamas" la nueva información
            val updateTama = db.update("tamas", values, "id=?", arrayOf(idTama.toString()))

            return updateTama
        } finally {
            db?.close()
        }

    }

    fun delete(idTama: Long): Int {
        var db: SQLiteDatabase? = null
        try {
            //Obtenemos la base de datos en formato escritura
            db = writableDatabase
            // Llamamos al función delete que almacenaremos en deleteTama
            val deleteTama = db.delete("tamas", "id=?", arrayOf(idTama.toString()))
            return deleteTama
        } finally {
            db?.close()
        }
    }

    /**
     * C R U D comercio
     */

    fun insertComercio(comercio: Comercio): Long {
        var db: SQLiteDatabase? = null
        try {
            db = writableDatabase
            val contentValues = ContentValues()

            //No se incluye "comId" ya que es AUTOINCREMENT
            contentValues.put("comName", comercio.comName)
            contentValues.put("ubication", comercio.ubication)

            //Inserta la fila. Retorna el ID de la nueva fila o -1 si es erróneo
            val comercioIn = db.insert("comercio", null, contentValues)
            if (comercioIn > 0) {
                //Actualizar el objeto con el ID que le asignó la DB
                comercio.comId = comercioIn.toInt()
            }
            return comercioIn
        } finally {
            db?.close()
        }

    }

    fun readComercio(comId: Long): Comercio? { //Devuelve un objeto Comercio si existen datos

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            //Obtenemos base de dato en formato lectura
            db = readableDatabase
            //Creamos la consulta a la BBDD y la almacenamos en selectQuery
            val selecQuery = "SELECT * FROM comercio WHERE comId =?"
            //Instanciamos el cursor leerá los datos de cada columna
            cursor = db.rawQuery(selecQuery, arrayOf(comId.toString()))
            //creamos el objeto readCom que almacenará los resultados de la consulta
            var readCom = Comercio(0, "", "")

            if (cursor.moveToFirst()) {
                val comId = cursor.getInt(cursor.getColumnIndexOrThrow("comId"))
                val comName = cursor.getString(cursor.getColumnIndexOrThrow("comName"))
                val ubication = cursor.getString(cursor.getColumnIndexOrThrow("ubication"))
                readCom = Comercio(comId, comName, ubication)
                //Si el cursor no está vacío devuelve el objeto readCom
                return readCom
            }

        } finally {
            //BLoque que siempre se ejecuta
            cursor?.close()
            db?.close()
        }
        return null

    }

    fun update(comId: Long, newInfoCom: Comercio): Int {

        var db: SQLiteDatabase? = null
        try {
            //Obtenemos la base de datos en formato escritura
            db = writableDatabase
            //Instanciamos a ContentValues para almacenar el conjunto de valores
            val values = ContentValues()
            //no incluimos comId ya que lo usamos en la cláusula where
            values.put("comName", newInfoCom.comName)
            values.put("ubication", newInfoCom.ubication)

            //Insertamos en la tabla "comercio" la nueva información
            val updateCom = db.update("comercio", values, "comId=?", arrayOf(comId.toString()))
            return updateCom
        } finally {
            //Bloque que siempre se ejecuta
            db?.close()
        }

    }

    fun deleteComercio(comId: Long): Int {

        var db: SQLiteDatabase? = null
        try {
            //Obtenemos la base de datos en formato escritura
            db = writableDatabase
            // Llamamos al función delete que almacenaremos en deleteCom
            val deleteCom = db.delete("comercio", "comId=?", arrayOf(comId.toString()))
            return deleteCom
        } finally {
            //Bloque que siempre se ejecuta
            db?.close()
        }
    }

    /**
     * C R U D adquisición
     */
    fun insertAdquisicion(adquisicion: Adquisicion): Long {

        var db: SQLiteDatabase? = null
        try {
            db = writableDatabase
            val contentValues = ContentValues()

            //No se incluye "adId" ya que es AUTOINCREMENT
            contentValues.put("tamaId", adquisicion.tamaId)
            contentValues.put("comId", adquisicion.comId)
            contentValues.put("date", adquisicion.date)
            contentValues.put("price", adquisicion.price)
            //Inserta la fila. Retorna el ID de la nueva fila o -1 si es erróneo
            val adquisicionIn = db.insert("adquisicion", null, contentValues)
            if (adquisicionIn > 0) {
                adquisicion.adId = adquisicionIn.toInt()
            }
            return adquisicionIn
        } finally {
            db?.close()
        }

    }

    fun readAdquisicion(adId: Long): Adquisicion? { //Devuelve un objeto Adquisicion si existen datos

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            //Obtenemos base de dato en formato lectura
            db = readableDatabase
            //Creamos la consulta a la BBDD y la almacenamos en selectQuery
            val selecQuery = "SELECT * FROM adquisicion WHERE adId =?"
            //Instanciamos el cursor leerá los datos de cada columna
            cursor = db.rawQuery(selecQuery, arrayOf(adId.toString()))
            //creamos el objeto readCom que almacenará los resultados de la consulta
            var readAd = Adquisicion(0, 0, 0, "", 0.0)

            if (cursor.moveToFirst()) {
                val adId = cursor.getInt(cursor.getColumnIndexOrThrow("adId"))
                val tamaIdForeign = cursor.getInt(cursor.getColumnIndexOrThrow("tamaId"))
                val comIdForeign = cursor.getInt(cursor.getColumnIndexOrThrow("comId"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                val price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
                readAd = Adquisicion(adId, tamaIdForeign, comIdForeign, date, price)
                // Si el cursor no está vacío devuelve el objeto readAd
                return readAd
            }
        } finally {
            //Bloque que siempre se ejecuta
            db?.close()
            cursor?.close()
        }
        //Si el cursor está vacío devuelve null
        return null
    }

    fun update(adId: Long, newInfoAd: Adquisicion): Int {

        var db: SQLiteDatabase? = null
        try {
            //Obtenemos la base de datos en formato escritura
            db = writableDatabase
            //Instanciamos a ContentValues para almacenar el conjunto de valores
            val values = ContentValues()
            values.put("tamaId", newInfoAd.tamaId)
            values.put("comId", newInfoAd.comId)
            values.put("date", newInfoAd.date)
            values.put("price", newInfoAd.price)
            //Insertamos en la tabla "adquisicion" la nueva información
            val updateAdq = db.update("adquisicion", values, "adId=?", arrayOf(adId.toString()))
            return updateAdq
        } finally {
            //Bloque que siemmpre se ejecuta
            db?.close()
        }

    }

    fun deleteAdquisicion(adId: Long): Int {

        var db: SQLiteDatabase? = null
        try {
            //Obtenemos la base de datos en formato escritura
            db = writableDatabase
            // Llamamos al función delete que almacenaremos en deleteCom
            val deleteAd = db.delete("adquisicion", "adId=?", arrayOf(adId.toString()))
            return deleteAd
        } finally {
            //Bloque que siempre se ejecuta
            db?.close()
        }
    }


    /**
     * Función para obtener el número de Tamagotchis de la colección
     */

    fun getNumTama(): Int {
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {

            //Obtenemos la base de datos en formato lectura
            db = readableDatabase
            val selectQuery = "SELECT count (*) as numTama FROM tamas"
            //Instanciamos el cursor que leerá los datos de cada columna
            cursor = db.rawQuery(selectQuery, null)
            var num = 0
            if (cursor.moveToFirst()) {
                num = cursor.getInt(cursor.getColumnIndexOrThrow("numTama"))

            }
            return num

        } finally {
            cursor?.close()
            db?.close()
        }
    }

    /**
     * Funciones para recorrer lo almacenado en las tablas
     */


    fun getAllTama(): MutableList<Tamagotchi> {
        //Creamos la MutableList
        val mutableListTama = mutableListOf<Tamagotchi>()
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            //Obtenemos base de dato en formato lectura
            db = this.readableDatabase
            //Creamos la consulta a la BBDD y la almacenamos en selectQuery
            val selecQuery = "SELECT * FROM tamas"
            //Instanciamos el cursor leerá los datos de cada columna
            cursor = db.rawQuery(selecQuery, null)


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
        } finally {
            cursor?.close()
            db?.close()
        }
        return mutableListTama

    }

    fun getPurchasingDetails(tamaId: Int): MutableList<DetallesCompra> {

        val detailsList = mutableListOf<DetallesCompra>()
        val db = this.readableDatabase //Obtenemos la base de datos en modo lectura
        val selectQuery =
            "SELECT T2.comName, T2.ubication, T3.price, T3.date FROM tamas AS T1 JOIN adquisicion AS T3 ON T1.id =T3.tamaId JOIN comercio AS T2 ON T3.comId = T2.comId WHERE T1.id=?"
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery(selectQuery, arrayOf(tamaId.toString()))//añado el arrayOF

            if (cursor.moveToFirst()) {

                do { //obtenemos los valores a partir del nombre de las columnas
                    val comName = cursor.getString(cursor.getColumnIndexOrThrow("comName"))
                    val ubication = cursor.getString(cursor.getColumnIndexOrThrow("ubication"))
                    val price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
                    val date = cursor.getString((cursor.getColumnIndexOrThrow("date")))
                    detailsList.add(DetallesCompra(comName, ubication, price, date))

                } while (cursor.moveToNext())

            }
        } catch (e: Exception) {
            e.printStackTrace() //Manejamos la excepción, en este caso un Log
        } finally {

            //Nos aseguramos cerrar siempre el cursor y la base de datos
            cursor?.close()
            db.close()
        }
        return detailsList
    }

    fun readTamaDetails(tamaId: Int): DetallesCompra? {
        var cursor: Cursor? = null
        var tamaDetails: DetallesCompra? = null
        var db: SQLiteDatabase? = null
        try {
            db = this.readableDatabase //Obtenemos la base de datos en modo lectura
            val selectQuery =
                "SELECT T2.comName, T2.ubication, T3.price, T3.date FROM tamas AS T1 JOIN adquisicion AS T3 ON T1.id =T3.tamaId JOIN comercio AS T2 ON T3.comId = T2.comId WHERE T1.id=?"
            cursor = db.rawQuery(selectQuery, arrayOf(tamaId.toString()))

            if (cursor.moveToFirst()) {

                //obtenemos los valores a partir del nombre de las columnas
                val comName = cursor.getString(cursor.getColumnIndexOrThrow("comName"))
                val ubication = cursor.getString(cursor.getColumnIndexOrThrow("ubication"))
                val price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
                val date = cursor.getString((cursor.getColumnIndexOrThrow("date")))
                tamaDetails = DetallesCompra(comName, ubication, price, date)
                return tamaDetails
            }
        } catch (e: Exception) {
            android.util.Log.e(
                "TamaSQLite",
                "Error al leer detalles del tama: $tamaId",
                e
            ) //Manejamos la excepción, en este caso un Log
        } finally {
            //Nos aseguramos cerrar siempre el cursor y la base de datos
            cursor?.close()
            db?.close()
        }
        return null
    }


}