package com.shuanglu.edge.Model.Database.Table

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.daniel.edge.Config.EdgeConfig
import com.shuanglu.edge.Management.Download.Model.EdgeDownloadModel
import com.shuanglu.edge.Model.Database.SQLLite.DatabaseHelper

/**
 * @Author:      Daniel
 * @Date:        2018/11/23 14:54
 * @Description:
 *
 */
class DBTDownload {
    private var db: SQLiteDatabase

    companion object {
        val TABLE_NAME = "Download"

        //field
        val ID = "_id"
        val FILE_NAME = "NAME"
        val FILE_LOCAL_PATH = "LOCAL_PATH"
        val FILE_TEMP_PATH = "TEMP_PATH"
        val FILE_TOTAL_SIZE = "TOTAL_SIZE"
        val FILE_ALREAD_DOWN_SIZE = "ALREADY_DOWN_SIZE"

        val SQL = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + " ( " +
                ID + " INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , " +
                FILE_NAME + "VARCHAR, " +
                FILE_LOCAL_PATH + "VARCHAR" +
                FILE_TEMP_PATH + "VARCHAR, " +
                FILE_TOTAL_SIZE + "Long, " +
                FILE_ALREAD_DOWN_SIZE + "Long, " +
                ")"

        fun getInstance() = Instance.INSTANCE
    }


    init {
        db = DatabaseHelper(EdgeConfig.CONTEXT).writableDatabase
    }


    private object Instance {
        val INSTANCE = DBTDownload()
    }

    fun isExists(edgeDownloadModel: EdgeDownloadModel): Boolean {
        var model = query(edgeDownloadModel)
        if (model == null) {
            return false
        }else{
            return true
        }
    }

    fun insert(edgeDownloadModel: EdgeDownloadModel) {
        var cv = getContentValue(edgeDownloadModel)
        db.insert(TABLE_NAME, null, cv)
        cv.clear()
    }

    fun update(edgeDownloadModel: EdgeDownloadModel) {
        var cv = getContentValue(edgeDownloadModel)
        db.update(TABLE_NAME, cv, ID + " = ?", arrayOf(edgeDownloadModel.id.toString()))
        cv.clear()
    }

    fun delete(edgeDownloadModel: EdgeDownloadModel) {
        db.delete(
            TABLE_NAME,
            ID + " = ? and " + FILE_NAME + " = ?",
            arrayOf(edgeDownloadModel.id.toString(), edgeDownloadModel.name)
        )
    }

    fun query(edgeDownloadModel: EdgeDownloadModel): EdgeDownloadModel? {
        var cursor = db.query(
            TABLE_NAME,
            arrayOf<String>(TABLE_NAME),
            TABLE_NAME + " = ?",
            arrayOf<String>(edgeDownloadModel.name!!),
            null,
            null,
            null
        )
        var list = arrayListOf<EdgeDownloadModel>()
        try {
            while (cursor.moveToNext()) {
                var model = EdgeDownloadModel(
                    cursor.getString(cursor.getColumnIndex(FILE_NAME)),
                    cursor.getString(cursor.getColumnIndex(FILE_LOCAL_PATH)),
                    cursor.getString(cursor.getColumnIndex(FILE_TEMP_PATH))
                )
                model.id = cursor.getInt(cursor.getColumnIndex(ID))
                model.totalSize = cursor.getLong(cursor.getColumnIndex(FILE_TOTAL_SIZE))
                model.alreadyDownSize = cursor.getLong(cursor.getColumnIndex(FILE_ALREAD_DOWN_SIZE))
                list.add(edgeDownloadModel)
            }
        } finally {
            cursor.close()
        }
        list.filter {
            if (it.totalSize == edgeDownloadModel.totalSize){
                true
            }else{
                false
            }
        }
        return if (list.size>0){
            list.get(0)
        }else{
            null
        }
    }


    fun getContentValue(edgeDownloadModel: EdgeDownloadModel): ContentValues {
        var cv = ContentValues()
        cv.put(FILE_NAME, edgeDownloadModel.name)
        cv.put(FILE_LOCAL_PATH, edgeDownloadModel.localPath)
        cv.put(FILE_TEMP_PATH, edgeDownloadModel.tempPath)
        cv.put(FILE_TOTAL_SIZE, edgeDownloadModel.totalSize)
        cv.put(FILE_ALREAD_DOWN_SIZE, edgeDownloadModel.alreadyDownSize)
        return cv
    }
}