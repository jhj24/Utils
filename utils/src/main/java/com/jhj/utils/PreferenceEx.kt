package com.jhj.utils

import android.content.Context
import java.io.Serializable
import java.util.ArrayList


fun <T : Any> Context.spSave(clazz: T, key: String): Boolean {
    val prefFileName = clazz::class.java.name
    val sp = getSharedPreferences(prefFileName, 0)
    val et = sp.edit()
    val json = JsonUtils.toJson(clazz)
    et.putString(key, json)
    return et.commit()
}


inline fun <reified T> Context.spFind(key: String, clazz: Class<T>): T? {
    val prefFileName = clazz.name
    val sp = getSharedPreferences(prefFileName, 0)
    val json = sp.getString(key, null) ?: return null
    return JsonUtils.parseJson<T>(json, clazz)
}

fun <T> Context.spDelete(key: String, clazz: Class<T>) {
    val prefFileName = clazz.name
    val sp = getSharedPreferences(prefFileName, 0)
    if (sp.contains(key)) {
        sp.edit().remove(key).apply()
    }
}


inline fun <reified T> Context.spFindAll(clazz: Class<T>): List<T> {
    val prefFileName = clazz.name
    val sp = getSharedPreferences(prefFileName, 0)
    val values = sp.all as Map<String, String>?
    val results = ArrayList<T>()
    if (values == null || values.isEmpty())
        return results
    for (json in values.values) {
        val bean = JsonUtils.parseJson<T>(json, clazz)
        bean?.let { results.add(it) }
    }
    return results
}

fun <T : Serializable> Context.spDeleteAll(clazz: Class<T>) {
    val prefFileName = clazz.name
    val sp = getSharedPreferences(prefFileName, 0)
    sp.edit().clear().apply()
}

