package com.daniel.edgeDemo.model.dao

import androidx.room.*
import com.daniel.edgeDemo.model.modelDb.OpenDBEntity

@Dao
interface OpenDao {
    @Query("SELECT * From app_info")
    fun queryAll():MutableList<OpenDBEntity>

    @Query("SELECT * From app_info WHERE id == :id LIMIT 1")
    fun query(id:Int):OpenDBEntity

    @Insert
    fun insert(model:OpenDBEntity)

    @Insert
    fun insertAll(vararg models:OpenDBEntity)

    @Update
    fun update(vararg model:OpenDBEntity)

    @Delete
    fun delete(model:OpenDBEntity)
}