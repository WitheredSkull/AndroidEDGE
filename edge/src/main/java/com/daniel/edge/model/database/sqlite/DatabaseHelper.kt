package com.daniel.edge.model.database.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.daniel.edge.config.EdgeConfig
import com.daniel.edge.model.database.table.DBTDownload

/**
 * @Author:      Daniel
 * @Date:        2018/11/23 15:18
 * @Description:
 *
 */
class DatabaseHelper : SQLiteOpenHelper {
    companion object {
        val EDGE_LIBRARY_NAME = "EdgeLibrary.db"
    }

    constructor(context: Context?) : super(
        context,
        EDGE_LIBRARY_NAME,
        null,
        EdgeConfig.DATABASE_VERSION
    )

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DBTDownload.SQL)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}