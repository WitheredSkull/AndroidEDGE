package com.daniel.edgeDemo.retrofit.converterFactory

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class MultiFunctionConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, Any>? {
        return MultiFunctionResponseBodyConverter(type)
    }

    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<Annotation>,
        methodAnnotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<Any, RequestBody>? {
        return MultiFunctionRequestBodyConverter()
    }
//
//    override fun stringConverter(
//        type: Type,
//        annotations: Array<Annotation>,
//        retrofit: Retrofit
//    ): Converter<*, String>? {
//        return super.stringConverter(type, annotations, retrofit)
//    }

    companion object {
        fun create(): MultiFunctionConverterFactory {
            return MultiFunctionConverterFactory()
        }
    }
}