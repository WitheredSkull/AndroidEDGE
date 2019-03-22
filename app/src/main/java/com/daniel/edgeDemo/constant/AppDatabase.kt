package com.daniel.edgeDemo.constant

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.daniel.edgeDemo.constant.App.Companion.DB_NAME
import com.daniel.edgeDemo.model.dao.OpenDao
import com.daniel.edgeDemo.model.modelDb.OpenDBEntity

@Database(entities = [OpenDBEntity::class], version = 1,exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        //更新数据库
        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("DROP TABLE AppInfo")
            }
        }

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also {
                    INSTANCE = it
                }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context,
                AppDatabase::class.java,
                DB_NAME
            )
                .addMigrations(MIGRATION_1_2)
                .addCallback(object : Callback() {
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        //数据库创建完成
                        super.onCreate(db)
                    }

                    override fun onOpen(db: SupportSQLiteDatabase) {
                        //数据库打开完成
                        super.onOpen(db)
                    }
                })
                .build()
    }

    abstract fun getOpenDao(): OpenDao

    //关闭数据库
    override fun close() {
        super.close()
    }
}