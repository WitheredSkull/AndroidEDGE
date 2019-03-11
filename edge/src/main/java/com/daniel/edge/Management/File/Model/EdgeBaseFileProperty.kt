package com.daniel.edge.Management.File.Model

/**
 * 创建人 Daniel
 * 时间   2018/11/7.
 * 简介   文件基础属性
 */
class EdgeBaseFileProperty {
    var fileName: String? = null
    var filePath: String? = null
    var isDirectory: Boolean = false
    override fun toString(): String {
        return "EdgeBaseFileProperty(fileName=$fileName, filePath=$filePath, isDirectory=$isDirectory)"
    }


}