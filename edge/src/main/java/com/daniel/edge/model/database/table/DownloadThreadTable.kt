package com.daniel.edge.model.database.table

import android.content.ContentValues
import com.daniel.edge.config.Edge
import com.daniel.edge.management.download.model.DownloadThreadModel
import com.daniel.edge.model.database.sqlite.DatabaseHelper

class DownloadThreadTable {
    var dbHelper: DatabaseHelper

    init {
        dbHelper = DatabaseHelper(Edge.CONTEXT)
    }


    fun insert(model: DownloadThreadModel):Long {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        var values = ContentValues()
        values.put(DOWNLOAD_ID, model.downloadId)
        values.put(SIZE, model.size)
        values.put(TOTAL_SIZE, model.totalSize)
        values.put(START_POSITION, model.startPosition)
        values.put(END_POSITION, model.endPosition)
        var id = db.insert(TABLE_NAME,null,values)
        values.clear()
        db.endTransaction()
        db.close()
        return id
    }

    fun insertAll(list: ArrayList<DownloadThreadModel>) {
        val db = dbHelper.writableDatabase
        db.beginTransaction()
        list.forEach {
            val values = ContentValues()
            values.put(DOWNLOAD_ID, it.downloadId)
            values.put(SIZE, it.size)
            values.put(TOTAL_SIZE, it.totalSize)
            values.put(START_POSITION, it.startPosition)
            values.put(END_POSITION, it.endPosition)
            db.insert(TABLE_NAME,null,values)
            values.clear()
        }
        db.endTransaction()
        db.close()
    }

    fun query(downloadId: Int): ArrayList<DownloadThreadModel> {
        val list = arrayListOf<DownloadThreadModel>()
        val db = dbHelper.readableDatabase
        val cursor = db.query(TABLE_NAME, null, "${DOWNLOAD_ID}=?", arrayOf("${downloadId}"), null, null, null)
        while (cursor.moveToNext()) {
            list.add(
                DownloadThreadModel(
                    cursor.getInt(cursor.getColumnIndex(ID)),
                    cursor.getInt(cursor.getColumnIndex(DOWNLOAD_ID)),
                    cursor.getInt(cursor.getColumnIndex(START_POSITION)),
                    cursor.getInt(cursor.getColumnIndex(END_POSITION)),
                    cursor.getInt(cursor.getColumnIndex(SIZE)),
                    cursor.getInt(cursor.getColumnIndex(TOTAL_SIZE))
                )
            )
        }
        cursor.close()
        db.close()
        return list
    }

    fun queryAll(): ArrayList<DownloadThreadModel> {
        var list = arrayListOf<DownloadThreadModel>()
        val db = dbHelper.readableDatabase
        var cursor = db.query(TABLE_NAME, null, null, null, null, null, null)
        while (cursor.moveToNext()) {
            list.add(
                DownloadThreadModel(
                    cursor.getInt(cursor.getColumnIndex(ID)),
                    cursor.getInt(cursor.getColumnIndex(DOWNLOAD_ID)),
                    cursor.getInt(cursor.getColumnIndex(START_POSITION)),
                    cursor.getInt(cursor.getColumnIndex(END_POSITION)),
                    cursor.getInt(cursor.getColumnIndex(SIZE)),
                    cursor.getInt(cursor.getColumnIndex(TOTAL_SIZE))
                )
            )
        }
        cursor.close()
        db.close()
        return list
    }

    fun update(threadModel: DownloadThreadModel) {
        val db = dbHelper.writableDatabase
        var values = ContentValues()
        values.put(ID, threadModel.id)
        values.put(SIZE, threadModel.size)
        values.put(TOTAL_SIZE, threadModel.totalSize)
        values.put(START_POSITION, threadModel.startPosition)
        values.put(END_POSITION, threadModel.endPosition)
        db.update(TABLE_NAME, values, "${ID}=?", arrayOf("${threadModel.id}"))
    }

    fun updateAll(list: ArrayList<DownloadThreadModel>) {
        val db = dbHelper.writableDatabase
        list.forEach {
            var values = ContentValues()
            values.put(ID, it.id)
            values.put(DOWNLOAD_ID, it.downloadId)
            values.put(SIZE, it.size)
            values.put(TOTAL_SIZE, it.totalSize)
            values.put(START_POSITION, it.startPosition)
            values.put(END_POSITION, it.endPosition)
            db.update(TABLE_NAME, values, "${ID}=?", arrayOf("${it.id}"))
            values.clear()
        }
    }

    object Instance {
        val INSTANCE = DownloadThreadTable()
    }

    companion object {
        val TABLE_NAME = "download_thread"
        val ID = "id"
        val DOWNLOAD_ID = "download_id"
        val SIZE = "size"
        val TOTAL_SIZE = "total_size"
        val START_POSITION = "start_size"
        val END_POSITION = "end_size"

        val SQL_CREATE = "create table ${TABLE_NAME}(" +
                "${ID} integer primary key autoincrement," +
                "${SIZE} long," +
                "${END_POSITION} long)"

        fun getInstance() = Instance.INSTANCE
    }
}