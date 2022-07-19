package com.platzi.android.firestore.network

interface Callback<T> {

    fun onSuccess(result: T?)

    fun onFailed(exception: Exception)
}