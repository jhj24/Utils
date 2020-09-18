package com.jhj.utils.activityforresult

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.fragment.app.FragmentActivity
import java.io.Serializable


fun Intent.fillIntentArguments(vararg params: Pair<String, Any?>) {
    params.forEach {
        when (val value = it.second) {
            null -> this.putExtra(it.first, null as Serializable?)
            is Int -> this.putExtra(it.first, value)
            is Long -> this.putExtra(it.first, value)
            is CharSequence -> this.putExtra(it.first, value)
            is String -> this.putExtra(it.first, value)
            is Float -> this.putExtra(it.first, value)
            is Double -> this.putExtra(it.first, value)
            is Char -> this.putExtra(it.first, value)
            is Short -> this.putExtra(it.first, value)
            is Boolean -> this.putExtra(it.first, value)
            is Serializable -> this.putExtra(it.first, value)
            is Bundle -> this.putExtra(it.first, value)
            is Parcelable -> this.putExtra(it.first, value)
            is Array<*> -> when {
                value.isArrayOf<CharSequence>() -> this.putExtra(it.first, value)
                value.isArrayOf<String>() -> this.putExtra(it.first, value)
                value.isArrayOf<Parcelable>() -> this.putExtra(it.first, value)
                else -> throw Exception("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
            }
            is IntArray -> this.putExtra(it.first, value)
            is LongArray -> this.putExtra(it.first, value)
            is FloatArray -> this.putExtra(it.first, value)
            is DoubleArray -> this.putExtra(it.first, value)
            is CharArray -> this.putExtra(it.first, value)
            is ShortArray -> this.putExtra(it.first, value)
            is BooleanArray -> this.putExtra(it.first, value)
            else -> throw Exception("Intent extra ${it.first} has wrong type ${value.javaClass.name}")
        }
        return@forEach
    }
}

inline fun <reified T : Activity> FragmentActivity?.activityForResult(
    vararg params: Pair<String, Any?>,
    noinline body: (Intent?) -> Unit
) {
    if (this == null) {
        return
    }
    try {
        val intent = Intent(this, T::class.java)
        intent.fillIntentArguments(*params)
        val TAG = this.javaClass.name
        val fragmentManager = this.supportFragmentManager
        var fragment: OnResultFragment? =
            fragmentManager.findFragmentByTag(TAG) as OnResultFragment?
        if (fragment == null) {
            fragment = OnResultFragment()
            fragmentManager
                .beginTransaction()
                .add(fragment, TAG)
                .commitAllowingStateLoss()
            fragmentManager.executePendingTransactions()
        }
        fragment.startActivityForResult(T::class.java, intent, body)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun <T : Activity> T.activityFinish(vararg params: Pair<String, Any?>) {
    val intent = Intent()
    intent.fillIntentArguments(*params)
    setResult(Activity.RESULT_OK, intent)
    finish()
}