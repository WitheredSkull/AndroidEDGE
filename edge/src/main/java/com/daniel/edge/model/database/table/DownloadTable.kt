package com.daniel.edge.model.database.table

import android.content.ContentValues
import com.daniel.edge.config.Edge
import com.daniel.edge.management.download.model.DownloadModel
import com.daniel.edge.model.database.sqlite.DatabaseHelper

class DownloadTable {
    var dbHelper: DatabaseHelper

    init {
        dbHelper = DatabaseHelper(Edge.CONTEXT)
    }

    fun insert(model: DownloadModel): Long {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        var id = -1L
        try {
            val values = ContentValues()
            values.put(NAME, model.name)
            values.put(URL, model.url)
            values.put(INS, model.currentTime)
            values.put(STATE, model.state)
            id = db.insert(TABLE_NAME, null, values)
            values.clear()
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
        return id
    }

    fun insertAll(list: ArrayList<DownloadModel>) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            list.forEach {
                val values = ContentValues()
                values.put(NAME, it.name)
                values.put(URL, it.url)
                values.put(INS, it.currentTime)
                values.put(STATE, it.state)
                db.insert(TABLE_NAME, null, values)
                values.clear()
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun queryFromURL(url: String): DownloadModel? {
        val list = arrayListOf<DownloadModel>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(TABLE_NAME, null, "${URL}=?", arrayOf(url), null, null, null)
        while (cursor.moveToNext()) {
            list.add(
                DownloadModel(
                    cursor.getLong(cursor.getColumnIndex(ID)),
                    cursor.getString(cursor.getColumnIndex(NAME)),
                    cursor.getString(cursor.getColumnIndex(URL)),
                    cursor.getInt(cursor.getColumnIndex(STATE)),
                    cursor.getLong(cursor.getColumnIndex(INS))
                )
            )
        }
        cursor.close()
        db.close()
        return list.firstOrNull()
    }


    fun queryAll(): ArrayList<DownloadModel> {
        val list = arrayListOf<DownloadModel>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            list.add(
                DownloadModel(
                    cursor.getLong(cursor.getColumnIndex(ID)),
                    cursor.getString(cursor.getColumnIndex(NAME)),
                    cursor.getString(cursor.getColumnIndex(URL)),
                    cursor.getInt(cursor.getColumnIndex(STATE)),
                    cursor.getLong(cursor.getColumnIndex(INS))
                )
            )
        }
        cursor.close()
        db.close()
        return list
    }

    fun update(model: DownloadModel) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            val values = ContentValues()
            values.put(ID, model.id)
            values.put(NAME, model.name)
            values.put(URL, model.url)
            values.put(STATE, model.state)
            values.put(INS, model.currentTime)
            db.update(TABLE_NAME, values, "${ID}=?", arrayOf("${model.id}"))
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun updateAll(list: ArrayList<DownloadModel>) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            list.forEach {
                var values = ContentValues()
                values.put(NAME, it.name)
                values.put(URL, it.url)
                values.put(STATE, it.state)
                values.put(INS, it.currentTime)
                db.update(TABLE_NAME, values, "${ID}=?", arrayOf("${it.id}"))
                values.clear()
            }
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    object Instance {
        val INSTANCE = DownloadTable()
    }

    companion object {
        val TABLE_NAME = "download"
        val ID = "id"
        val NAME = "name"
        val URL = "url"
        val STATE = "state"
        val INS = "ins"

        val SQL_CREATE = "create table ${TABLE_NAME}(" +
                "${ID} long primary key autoincrement," +
                "${NAME} text," +
                "${URL} text," +
                "${STATE} integer," +
                "${INS} long)"

        fun getInstance() = Instance.INSTANCE
    }
}