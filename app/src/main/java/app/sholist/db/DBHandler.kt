package app.sholist.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import app.sholist.model.*

class DBHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_NAME_MAIN (" +
                ID + " INTEGER PRIMARY KEY," +
                NAME + " TEXT," +
                DESC + " TEXT," +
                TYPE_ID + " INT);")
        db.execSQL("CREATE TABLE $TABLE_NAME_SHO (" +
                ID + " INTEGER PRIMARY KEY," +
                COMPL + " TEXT," +
                MAIN_ID + " INT," +
                NAME + " TEXT," +
                UNIT + " TEXT," +
                AMOUNT + " DOUBLE);")
        db.execSQL("CREATE TABLE $TABLE_NAME_TYPES (" +
                ID + " INTEGER PRIMARY KEY," +
                NAME + " TEXT," +
                IMAGE + " TEXT);")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_MAIN")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_SHO")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME_TYPES")
        onCreate(db)
    }


    fun createMainListItem(item: MainListItem): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME, item.name)
        values.put(DESC, item.desc)
        values.put(TYPE_ID, item.typeId)
        val success = db.insert(TABLE_NAME_MAIN, null, values)
        db.close()
        return (Integer.parseInt("$success") != -1)
    }

    fun createShoListItem(item: ShoListItem): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(MAIN_ID, item.mainId)
        values.put(NAME, item.name)
        values.put(UNIT, item.unit)
        values.put(AMOUNT, item.amount)
        values.put(COMPL, item.completed.toString())
        val success = db.insert(TABLE_NAME_SHO, null, values)
        db.close()
        return (Integer.parseInt("$success") != -1)
    }

    fun createListTypeItem(item: ListType): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(NAME, item.name)
        values.put(IMAGE, item.image)
        val success = db.insert(TABLE_NAME_TYPES, null, values)
        db.close()
        return (Integer.parseInt("$success") != -1)
    }


    fun readMainListItem(_id: Int): MainListItem {
        val item = MainListItem()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME_MAIN WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)
        cursor?.moveToFirst()
        item.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
        item.name = cursor.getString(cursor.getColumnIndex(NAME))
        item.desc = cursor.getString(cursor.getColumnIndex(DESC))
        item.typeId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TYPE_ID)))
        cursor.close()
        return item
    }

    fun readListTypeItem(_id: Int): ListType {
        val item = ListType()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME_TYPES WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)
        cursor?.moveToFirst()
        item.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
        item.name = cursor.getString(cursor.getColumnIndex(NAME))
        item.image = cursor.getString(cursor.getColumnIndex(IMAGE))
        cursor.close()
        return item
    }

    fun mainListItems(): ArrayList<MainListItem> {
        val items = ArrayList<MainListItem>()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME_MAIN"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val item = MainListItem()
                    item.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    item.name = cursor.getString(cursor.getColumnIndex(NAME))
                    item.desc = cursor.getString(cursor.getColumnIndex(DESC))
                    item.typeId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(TYPE_ID)))
                    items.add(item)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return items
    }

    fun shoListItemsByMainId(mainId: Int): ArrayList<ShoListItem> {
        val items = ArrayList<ShoListItem>()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME_SHO WHERE $MAIN_ID = $mainId ORDER BY $COMPL"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val item = ShoListItem()
                    item.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    item.mainId = Integer.parseInt(cursor.getString(cursor.getColumnIndex(MAIN_ID)))
                    item.name = cursor.getString(cursor.getColumnIndex(NAME))
                    item.unit = cursor.getString(cursor.getColumnIndex(UNIT))
                    item.completed = cursor.getString(cursor.getColumnIndex(COMPL))!!.toBoolean()
                    item.amount = cursor.getString(cursor.getColumnIndex(AMOUNT)).toDouble()
                    items.add(item)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return items
    }

    fun listTypeItems(): List<ListType> {
        val items = ArrayList<ListType>()
        val db = writableDatabase
        val selectQuery = "SELECT * FROM $TABLE_NAME_TYPES"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val item = ListType()
                    item.id = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ID)))
                    item.name = cursor.getString(cursor.getColumnIndex(NAME))
                    item.image = cursor.getString(cursor.getColumnIndex(IMAGE))
                    items.add(item)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return items
    }


    fun updateMainListItem(item: MainListItem): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put(NAME, item.name)
        values.put(DESC, item.desc)
        values.put(TYPE_ID, item.typeId)
        val success = db.update(TABLE_NAME_MAIN, values, "$ID=?", arrayOf(item.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }

    fun updateShoListItem(item: ShoListItem): Boolean {
        val db = writableDatabase
        val values = ContentValues()
        values.put(MAIN_ID, item.mainId)
        values.put(NAME, item.name)
        values.put(UNIT, item.unit)
        values.put(AMOUNT, item.amount)
        values.put(COMPL, item.completed.toString())
        val success = db.update(TABLE_NAME_SHO, values, "$ID=?", arrayOf(item.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }

    fun deleteMainListItem(_id: Int): Boolean {
        val db = writableDatabase
        val success = db.delete(TABLE_NAME_MAIN, "$ID=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }

    fun deleteShoListItem(_id: Int): Boolean {
        val db = writableDatabase
        val success = db.delete(TABLE_NAME_SHO, "$ID=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }

    fun deleteAllMainListItems(): Boolean {
        val db = writableDatabase
        val success = db.delete(TABLE_NAME_MAIN, null, null).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }

    fun deleteAllShoListItems(): Boolean {
        val db = writableDatabase
        val success = db.delete(TABLE_NAME_SHO, null, null).toLong()
        db.close()
        return Integer.parseInt("$success") != -1
    }

    companion object {
        const val DB_VERSION = 1
        const val DB_NAME = "SHO_LIST_APP"

        const val TABLE_NAME_MAIN = "MAIN_LIST"
        const val TABLE_NAME_SHO = "SHO_LIST"
        const val TABLE_NAME_TYPES = "LIST_TYPES"

        const val ID = "Id"
        const val NAME = "Name"
        const val DESC = "Desc"
        const val IMAGE = "Image"
        const val MAIN_ID = "MainId"
        const val TYPE_ID = "TypeId"
        const val UNIT = "Unit"
        const val AMOUNT = "Amount"
        const val COMPL = "Completed"
    }
}