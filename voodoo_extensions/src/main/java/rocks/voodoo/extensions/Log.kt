package rocks.voodoo.extensions

import android.util.Log

fun Any.logD(message: String) {
    Log.d(tag(), message)
}

fun Any.logD(tag: String, message: String) {
    Log.d(tag(), "$tag: $message")
}

fun Any.logE(message: String) {
    Log.e(tag(), message)
}

fun Any.logE(tag: String, message: String) {
    Log.e(tag(), "$tag: $message")
}