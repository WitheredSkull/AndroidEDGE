package com.daniel.edge

import retrofit2.Call
import retrofit2.http.GET

interface WanService {
    @GET("wxarticle/chapters/json")
    fun GetPublishNumber(): Call<ModelJava>
}