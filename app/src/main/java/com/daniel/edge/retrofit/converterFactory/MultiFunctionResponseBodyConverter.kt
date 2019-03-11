package com.daniel.edge.retrofit.converterFactory

import com.daniel.edge.Utils.Log.EdgeLog
import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonToken
import okhttp3.ResponseBody
import okio.BufferedSource
import retrofit2.Converter
import java.io.IOException
import java.io.InputStream
import java.io.Reader
import java.lang.reflect.Type

class MultiFunctionResponseBodyConverter<T>(private var type: Type) : Converter<ResponseBody, T> {

    @Throws(IOException::class)
    override fun convert(value: ResponseBody): T? {
        var response: T? = null
        try {
            when (type) {
                String::class.java,
                Boolean::class.java,
                Float::class.java,
                Double::class.java,
                Int::class.java,
                Short::class.java,
                Char::class.java,
                Long::class.java
                -> {
                    response = value.string() as T
                }
                InputStream::class.java -> {
                    response = value.byteStream() as T
                }
                ByteArray::class.java -> {
                    response = value.bytes() as T
                }
                BufferedSource::class.java -> {
                    response = value.source() as T
                }
                Reader::class.java -> {
                    response = value.charStream() as T
                }
            }
        } finally {
            if (response != null) {
                value.close()
                EdgeLog.show(javaClass, "实体类型:${type}\n返回结果\n${response}")
                return response
            }
        }
        val gson = Gson()
        val adapter = gson.getAdapter(TypeToken.get(type))
        val jsonReader = gson.newJsonReader(value.charStream())
        try {
            response = adapter?.read(jsonReader) as T
            if (jsonReader?.peek() != JsonToken.END_DOCUMENT) {
                throw JsonIOException("JSON document was not fully consumed.")
            }
        } finally {
            value.close()
            return response
        }
    }
}