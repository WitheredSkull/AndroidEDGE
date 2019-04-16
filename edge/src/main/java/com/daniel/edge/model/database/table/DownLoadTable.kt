package com.daniel.edge.model.database.table

import android.content.ContentValues
import com.daniel.edge.config.Edge
import com.daniel.edge.model.database.sqlite.DatabaseHelper

class DownLoadTable {
    var dbHelper: DatabaseHelper

    init {
        dbHelper = DatabaseHelper(Edge.CONTEXT)
    }


    fun insert(list: ArrayList<DownloadModel>) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        list.forEach {
            var values = ContentValues()
            values.put(DOWNLOAD_NAME, it.name)
            values.put(DOWNLOAD_URL, it.url)
            values.put(THREAD_ID, it.thread_id)
            values.put(THREAD_DOWNLOAD_SIZE, it.thread_download_size)
            values.put(THREAD_TOTAL_SIZE, it.thread_total_size)
            db.insert(TABLE_NAME, null, values)
            values.clear()
        }
        db.endTransaction()
        db.close()
    }

    fun query(): ArrayList<DownloadModel> {
        var list = arrayListOf<DownloadModel>()
        val db = dbHelper.readableDatabase
        var cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            list.add(
                DownloadModel(
                    cursor.getInt(cursor.getColumnIndex(DOWNLOAD_ID)),
                    cursor.getString(cursor.getColumnIndex(DOWNLOAD_NAME)),
                    cursor.getString(cursor.getColumnIndex(DOWNLOAD_URL)),
                    cursor.getInt(cursor.getColumnIndex(THREAD_ID)),
                    cursor.getLong(cursor.getColumnIndex(THREAD_DOWNLOAD_SIZE)),
                    cursor.getLong(cursor.getColumnIndex(THREAD_TOTAL_SIZE))
                )
            )
        }
        cursor.close()
        db.close()
        return list
    }

    object Instance {
        val INSTANCE = DownLoadTable()
    }

    companion object {
        val TABLE_NAME = "download"
        val DOWNLOAD_ID = "id"
        val DOWNLOAD_NAME = "name"
        val DOWNLOAD_URL = "url"
        val THREAD_ID = "thread_id"
        val THREAD_DOWNLOAD_SIZE = "thread_download_size"
        val THREAD_TOTAL_SIZE = "thread_total_size"

        val SQL_CREATE = "create table ${TABLE_NAME}(" +
                "${DOWNLOAD_ID} integer primary key autoincrement," +
                "${DOWNLOAD_NAME} text," +
                "${DOWNLOAD_URL} text," +
                "${THREAD_ID} integer," +
                "${THREAD_DOWNLOAD_SIZE} long," +
                "${THREAD_TOTAL_SIZE} long)"
    }
}