package com.daniel.edgeDemo.model.rxJava

import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class AppObserver<T>:Observer<T> {
    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {
    }

    override fun onError(e: Throwable) {
        e.printStackTrace()
    }
}