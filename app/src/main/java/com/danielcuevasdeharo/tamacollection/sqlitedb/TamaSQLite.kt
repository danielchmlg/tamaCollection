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
            year INTEGER NOT NULL,
            user_id TEXT NOT NULL);
            
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

        val createCompraTableQuery = """
            CREATE TABLE compra(
            adId INTEGER PRIMARY KEY AUTOINCREMENT,
            tamaId INTEGER NOT NULL,
            comId INTEGER NOT NULL,
            date TEXT NOT NULL,
            price REAL NOT NULL,
            FOREIGN KEY (tamaId) REFERENCES tamas(id) ON DELETE CASCADE,
            FOREIGN KEY (comId) REFERENCES comercio(comId) ON DELETE CASCADE);
        """.trimIndent()
        db.execSQL(createCompraTableQuery)
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
            values.put("user_id",newTama.userId)
            //Insertamos en la tabla "tamas"
            val tamaIn = db.insert("tamas", null, values)
            return tamaIn
        } finally {
            db?.close()
        }

    }

    fun read(idTama: Long, userId: String): Tamagotchi? { //devuelve un Tamagotchi si existen datos

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            //Obtenemos base de dato en formato lectura
            db = readableDatabase
            //Creamos la consulta a la BBDD y la almacenamos en selectQuery
            val selecQuery = "SELECT * FROM tamas WHERE id =? AND user_id = ?"
            //Instanciamos el cursor leerá los datos de cada columna
            cursor = db.rawQuery(selecQuery, arrayOf(idTama.toString(), userId))
            //creamos el objeto readTama que almacenará los resultados de la consulta
            var readTama = Tamagotchi(0, "", "", 0, "")

            if (cursor.moveToFirst()) {
                val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                val generation = cursor.getString(cursor.getColumnIndexOrThrow("generation"))
                val year = cursor.getInt(cursor.getColumnIndexOrThrow("year"))
                //Devolvemos objeto readTama si el cursor no esta vacío
                readTama = Tamagotchi(id, name, generation, year, userId)
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
     * C R U D compra
     */
    fun insertCompra(compra: Compra): Long {

        var db: SQLiteDatabase? = null
        try {
            db = writableDatabase
            val contentValues = ContentValues()

            //No se incluye "adId" ya que es AUTOINCREMENT
            contentValues.put("tamaId", compra.tamaId)
            contentValues.put("comId", compra.comId)
            contentValues.put("date", compra.date)
            contentValues.put("price", compra.price)
            //Inserta la fila. Retorna el ID de la nueva fila o -1 si es erróneo
            val compraIn = db.insert("compra", null, contentValues)
            if (compraIn > 0) {
                compra.adId = compraIn.toInt()
            }
            return compraIn
        } finally {
            db?.close()
        }

    }

    fun readCompra(adId: Long): Compra? { //Devuelve un objeto Compra si existen datos

        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            //Obtenemos base de dato en formato lectura
            db = readableDatabase
            //Creamos la consulta a la BBDD y la almacenamos en selectQuery
            val selecQuery = "SELECT * FROM compra WHERE adId =?"
            //Instanciamos el cursor leerá los datos de cada columna
            cursor = db.rawQuery(selecQuery, arrayOf(adId.toString()))
            //creamos el objeto readCom que almacenará los resultados de la consulta
            var readCompra = Compra(0, 0, 0, "", 0.0)

            if (cursor.moveToFirst()) {
                val adId = cursor.getInt(cursor.getColumnIndexOrThrow("adId"))
                val tamaIdForeign = cursor.getInt(cursor.getColumnIndexOrThrow("tamaId"))
                val comIdForeign = cursor.getInt(cursor.getColumnIndexOrThrow("comId"))
                val date = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                val price = cursor.getDouble(cursor.getColumnIndexOrThrow("price"))
                readCompra = Compra(adId, tamaIdForeign, comIdForeign, date, price)
                // Si el cursor no está vacío devuelve el objeto readAd
                return readCompra
            }
        } finally {
            //Bloque que siempre se ejecuta
            db?.close()
            cursor?.close()
        }
        //Si el cursor está vacío devuelve null
        return null
    }

    fun update(adId: Long, newInfoAd: Compra): Int {

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
            val updateCompra = db.update("compra", values, "adId=?", arrayOf(adId.toString()))
            return updateCompra
        } finally {
            //Bloque que siemmpre se ejecuta
            db?.close()
        }

    }

    fun deleteCompra(adId: Long): Int {

        var db: SQLiteDatabase? = null
        try {
            //Obtenemos la base de datos en formato escritura
            db = writableDatabase
            // Llamamos al función delete que almacenaremos en deleteCom
            val deleteCompra = db.delete("compra", "adId=?", arrayOf(adId.toString()))
            return deleteCompra
        } finally {
            //Bloque que siempre se ejecuta
            db?.close()
        }
    }

    /**
     * Funciones para recorrer lo almacenado en las tablas
     */


    fun getAllTama(userId: String): MutableList<Tamagotchi> {
        //Creamos la MutableList
        val mutableListTama = mutableListOf<Tamagotchi>()
        var db: SQLiteDatabase? = null
        var cursor: Cursor? = null
        try {
            //Obtenemos base de dato en formato lectura
            db = this.readableDatabase
            //Creamos la consulta a la BBDD y la almacenamos en selectQuery
            val selecQuery = "SELECT * FROM tamas WHERE user_id =?"
            //Instanciamos el cursor leerá los datos de cada columna
            cursor = db.rawQuery(selecQuery, arrayOf(userId))


            if (cursor.moveToFirst()) {
                do {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    val generation = cursor.getString(cursor.getColumnIndexOrThrow("generation"))
                    val year = cursor.getInt(cursor.getColumnIndexOrThrow("year"))
                    val uid = cursor.getString(cursor.getColumnIndexOrThrow("user_id"))


                    val tamagotchi = Tamagotchi(id, name, generation, year, uid)
                    mutableListTama.add(tamagotchi)
                } while (cursor.moveToNext())
            }
        } finally {
            cursor?.close()
            db?.close()
        }
        return mutableListTama

    }


    fun readTamaDetails(tamaId: Int, userId: String): DetallesCompra? {
        var cursor: Cursor? = null
        var tamaDetails: DetallesCompra? = null
        var db: SQLiteDatabase? = null
        try {
            db = this.readableDatabase //Obtenemos la base de datos en modo lectura
            val selectQuery =
                "SELECT T2.comName, T2.ubication, T3.price, T3.date FROM tamas AS T1 JOIN compra AS T3 ON T1.id =T3.tamaId JOIN comercio AS T2 ON T3.comId = T2.comId WHERE T1.id=? AND T1.user_id = ?"
            cursor = db.rawQuery(selectQuery, arrayOf(tamaId.toString(), userId))

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
    fun isIdInUse(id: Int, userId: String): Boolean {
        // Obtenemos la base de datos en formato lectura
        val db = this.readableDatabase

        //Creamos la consulta que nos comprobará si el id ya está en la base de datos registrado
        val query = "SELECT id FROM tamas WHERE id = ? AND user_id = ?"

        //Instanciamos el cursor que leerá los datos de cada columna
        val cursor = db.rawQuery(query, arrayOf(id.toString(), userId))

        /*Comprueba el resultado. Si 'count' es mayor que 0, significa que ha encontrado
        * al menos una fila con ese ID, por lo tanto, el ID ya existe.
        */
        val idExists = cursor.count > 0
        //Libera recursos.
        cursor.close()
        db.close()
        //Devuelve el resultado. 'idExists' será 'true' o 'false'.
        return idExists
    }
    /* Función auxiliar para obtener los IDs relacionados (Compra y Comercio) dado un Tamagotchi
     * Devuelve un Pair: (adId, comId) o null si no encuentra nada
     */
    fun getRelatedIds(tamaId: String, userId: String): Pair<Int, Int>? {
        val db = this.readableDatabase
        var cursor: Cursor? = null
        try {
            // Hacemos JOIN para asegurarnos de que el Tamagotchi pertenece al usuario actual
            val query = """
                SELECT C.adId, C.comId 
                FROM compra C 
                JOIN tamas T ON C.tamaId = T.id 
                WHERE T.id = ? AND T.user_id = ?
            """.trimIndent()

            cursor = db.rawQuery(query, arrayOf(tamaId, userId))

            if (cursor.moveToFirst()) {
                val adId = cursor.getInt(cursor.getColumnIndexOrThrow("adId"))
                val comId = cursor.getInt(cursor.getColumnIndexOrThrow("comId"))
                return Pair(adId, comId)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            cursor?.close()
            db.close()
        }
        return null
    }


}