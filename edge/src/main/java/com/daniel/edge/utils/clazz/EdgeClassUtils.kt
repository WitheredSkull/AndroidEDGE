package com.daniel.edge.utils.clazz

object EdgeClassUtils {
    /**
     * @return 获取类名和其他属性结合的名字，可以用于HashMap防止直接保存key导致的内存泄漏等等
     */
    fun classToText(clazz: Class<*>,vararg arg:String):String{
        var str = StringBuffer()
        arg.forEach {
            str.append(it).append("+")
        }
        str.append(clazz.simpleName)
        return str.toString()
    }
}