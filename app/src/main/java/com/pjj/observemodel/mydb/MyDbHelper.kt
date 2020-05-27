package com.pjj.observemodel.mydb

import android.annotation.TargetApi
import android.content.Context
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Build
import com.pjj.PjjApplication
import java.sql.Date

class MyDbHelper : SQLiteOpenHelper {
    val DB_VERSION = 1
    val TABLE_NAME_PART_INFO = "my_table"
    private val CREATE_TABLE_PART_INFO = ("create table if not exists " + TABLE_NAME_PART_INFO + "("
            + "id INTEGER primary key,"
            + "upload_id VARCHAR(255),"
            + "num INTEGER,"
            + "crc64 INTEGER,"
            + "size INTEGER,"
            + "etag VARCHAR(255))")
    //天气
    constructor(context: Context, name: String, version: Int, factory: SQLiteDatabase.CursorFactory? = null, errorHandler: DatabaseErrorHandler? = null) : super(context, name, factory, version, errorHandler)
    @TargetApi(Build.VERSION_CODES.P)
    constructor(context: Context, name: String, version: Int, openParams: SQLiteDatabase.OpenParams = SQLiteDatabase.OpenParams.Builder().build()) : super(context, name, version, openParams)

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE_PART_INFO)
        var myDbHelper = MyDbHelper(PjjApplication.application, "adf", 1)
    }

    override fun onConfigure(db: SQLiteDatabase) {

    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        //super.onDowngrade(db, oldVersion, newVersion)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}