package com.daniel.edge.retrofit.converterFactory

import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Converter
import java.io.IOException

class MultiFunctionRequestBodyConverter<T> : Converter<T, RequestBody> {
    private val MEDIA_TYPE = MediaType.get("text/plain; charset=UTF-8")


    @Throws(IOException::class)
    override fun convert(value: T): RequestBody {
        return RequestBody.create(MEDIA_TYPE, value.toString())
    }
}