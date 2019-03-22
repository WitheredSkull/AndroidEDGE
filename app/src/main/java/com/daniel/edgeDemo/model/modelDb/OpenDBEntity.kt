package com.daniel.edgeDemo.model.modelDb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "app_info")
data class OpenDBEntity(
    @ColumnInfo(name = "time")
    var time: String,
    @ColumnInfo(name = "openCount")
    var count: Int
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var _id: Int = 0

    override fun toString(): String {
        return "OpenDBEntity(id=$_id, time='$time', count=$count)"
    }
}