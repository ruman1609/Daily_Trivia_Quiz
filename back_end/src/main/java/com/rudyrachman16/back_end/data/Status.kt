package com.rudyrachman16.back_end.data

sealed class Status<T>(val data: T? = null, val error: String? = null) {
    class Success<T>(data: T) : Status<T>(data = data)
    class Error<T>(error: String) : Status<T>(error = error)
    class Loading<T> : Status<T>()
}