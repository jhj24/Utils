package com.jhj.utils

import android.content.Context
import android.net.wifi.p2p.WifiP2pManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.anko.connectivityManager
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

fun Context.isNetworkConnected(): Boolean {
    val manager = this.connectivityManager
    val networkInfo = manager.activeNetworkInfo
    if (networkInfo != null) {
        return networkInfo.isConnected
    }
    return false
}


/**
 * 返回文件的size
 *
 * 如果是Url，请求网络返回大小，本地文件直接返回大小
 *
 * @see isUrl
 */
@Throws(Exception::class)
suspend fun String.fileSize(): Long {
    return withContext(Dispatchers.IO) {
        if (isUrl()) {
            val url = URL(this@fileSize)
            val urlConnection = url.openConnection() as HttpURLConnection
            urlConnection.connectTimeout = 5 * 1000
            urlConnection.requestMethod = "GET"
            urlConnection.connect()
            urlConnection.contentLength.toLong()
        } else {
            val file = File(this@fileSize)
            file.length()
        }
    }
}




