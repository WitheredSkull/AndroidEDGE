package com.daniel.edge.model.rxJava

import com.daniel.edge.utils.log.EdgeLog
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