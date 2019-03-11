package com.daniel.edge.utils.clazz

object EdgeClassUtils {
    fun classToText(clazz: Class<*>,vararg arg:String):String{
        var str = StringBuffer()
        arg.forEach {
            str.append(it).append("+")
        }
        str.append(clazz.simpleName)
        return str.toString()
    }
}