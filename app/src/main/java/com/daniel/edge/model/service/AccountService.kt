package com.daniel.edge.model.service

import io.reactivex.Observable
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AccountService {
    /**
     * 登陆账号
     * @param userName 用户名
     * @param pwd 密码
     */
    @FormUrlEncoded
    @POST("user/login")
    fun login(@Field("username") userName: String, @Field("password") pwd: String): Observable<String>

    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("user/register")
    fun register(@Field("username") userName: String, @Field("password") pwd: String, @Field("repassword") rePwd: String): Observable<String>
}