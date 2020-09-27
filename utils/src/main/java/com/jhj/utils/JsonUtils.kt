package com.jhj.utils

import android.util.Log
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import java.lang.reflect.Type


object JsonUtils {

    val TAG = "JsonUtils"
    inline fun <reified T> parseJson(json: String, cls: Class<*>): T? {
        try {
            return Gson().fromJson(json, cls) as T
        } catch (e: JsonSyntaxException) {
            Log.e(TAG, "解析gson字符串失败")
        }
        return null
    }

    inline fun <reified T> parseJson(json: String, cls: Type): T? {
        try {
            return Gson().fromJson(json, cls) as T
        } catch (e: JsonSyntaxException) {
            Log.e(TAG, "解析gson字符串失败")
        }
        return null
    }

    fun <T> toJson(obj: T): String? {
        try {
            return Gson().toJson(obj)
        } catch (e: Exception) {
            Log.e(TAG, "数据生成gson字符串失败")
        }
        return null
    }
}