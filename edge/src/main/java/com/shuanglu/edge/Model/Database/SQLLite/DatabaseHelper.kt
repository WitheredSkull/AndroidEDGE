package com.shuanglu.edge.Model.Database.SQLLite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.daniel.edge.Config.EdgeConfig
import com.shuanglu.edge.Model.Database.Table.DBTDownload

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