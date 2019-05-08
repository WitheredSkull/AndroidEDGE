package com.daniel.edge.model.database.table

import android.content.ContentValues
import com.daniel.edge.config.Edge
import com.daniel.edge.management.download.constants.DownloadState
import com.daniel.edge.management.download.model.DownloadModel
import com.daniel.edge.model.database.sqlite.DatabaseHelper
import java.lang.Exception

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
            values.put(FILE_PATH, model.filePath)
            values.put(URL, model.url)
            values.put(INS, model.currentTime)
            values.put(LOAD_SIZE, model.loadSize)
            values.put(LENGTH, model.length)
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
                values.put(FILE_PATH, it.filePath)
                values.put(URL, it.url)
                values.put(INS, it.currentTime)
                values.put(LOAD_SIZE, it.loadSize)
                values.put(LENGTH, it.length)
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
                    cursor.getString(cursor.getColumnIndex(FILE_PATH)),
                    cursor.getString(cursor.getColumnIndex(URL)),
                    cursor.getLong(cursor.getColumnIndex(LOAD_SIZE)),
                    cursor.getLong(cursor.getColumnIndex(LENGTH)),
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
                    cursor.getString(cursor.getColumnIndex(FILE_PATH)),
                    cursor.getString(cursor.getColumnIndex(URL)),
                    cursor.getLong(cursor.getColumnIndex(LOAD_SIZE)),
                    cursor.getLong(cursor.getColumnIndex(LENGTH)),
                    cursor.getInt(cursor.getColumnIndex(STATE)),
                    cursor.getLong(cursor.getColumnIndex(INS))
                )
            )
        }
        cursor.close()
        db.close()
        return list
    }

    fun updateStateFromUrl(url: String, state: DownloadState) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            db.execSQL("update ${TABLE_NAME} set ${STATE}=${state.ordinal} where ${URL}='${url}'")
            db.setTransactionSuccessful()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            db.endTransaction()
        }
    }

    fun update(model: DownloadModel) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            val values = ContentValues()
            values.put(ID, model.id)
            values.put(NAME, model.name)
            values.put(FILE_PATH, model.filePath)
            values.put(URL, model.url)
            values.put(LOAD_SIZE, model.loadSize)
            values.put(LENGTH, model.length)
            values.put(STATE, model.state)
            values.put(INS, model.currentTime)
            db.update(TABLE_NAME, values, "${ID}=?", arrayOf("${model.id}"))
            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
        }
    }

    fun updateLoadSize(url: String, loadSize: Long) {
        val model = queryFromURL(url)
        model?.let {
            it.currentTime = System.currentTimeMillis()
            it.loadSize = loadSize
            update(it)
        }
    }

    fun updateAll(list: ArrayList<DownloadModel>) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        try {
            list.forEach {
                var values = ContentValues()
                values.put(NAME, it.name)
                values.put(FILE_PATH, it.filePath)
                values.put(URL, it.url)
                values.put(LOAD_SIZE, it.loadSize)
                values.put(LENGTH, it.length)
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
        val FILE_PATH = "file_path"
        val URL = "url"
        val LOAD_SIZE = "load_size"
        val LENGTH = "length"
        val STATE = "state"
        val INS = "ins"

        val SQL_CREATE = "create table ${TABLE_NAME}(" +
                "${ID} integer primary key autoincrement," +
                "${NAME} text," +
                "${FILE_PATH} text," +
                "${URL} text," +
                "${LOAD_SIZE} long," +
                "${LENGTH} long," +
                "${STATE} integer," +
                "${INS} long)"

        fun getInstance() = Instance.INSTANCE
    }
}