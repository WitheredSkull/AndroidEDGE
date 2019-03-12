package com.daniel.edge.model.rxJava

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T> Observable<T>.applyThread(): Observable<T> = this.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())