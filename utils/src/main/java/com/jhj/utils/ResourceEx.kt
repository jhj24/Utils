package com.jhj.utils

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.util.DisplayMetrics
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import org.jetbrains.anko.windowManager
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.reflect.ParameterizedType


fun Context.selectedStateListDrawable(@DrawableRes select: Int, @DrawableRes normal: Int): StateListDrawable {
    return DrawableUtils.selected(drawable(select), drawable(normal))
}

fun Context.pressedStateListDrawable(@DrawableRes press: Int, @DrawableRes normal: Int): StateListDrawable {
    return DrawableUtils.pressed(drawable(press), drawable(normal))
}

fun Context.drawable(@DrawableRes id: Int): Drawable {
    return ContextCompat.getDrawable(this, id) ?: resources.getDrawable(id)
}

fun Context.selectedColorStateList(@ColorInt select: Int, @ColorInt normal: Int): ColorStateList {
    return ColorUtils.selected(select, normal)
}

fun Context.pressedColorStateList(@ColorInt select: Int, @ColorInt normal: Int): ColorStateList {
    return ColorUtils.pressed(select, normal)
}

fun Context.color(@ColorRes color: Int): Int {
    return ContextCompat.getColor(this, color)
}


val Context.screenHeight: Int
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val metric = DisplayMetrics()
            windowManager.defaultDisplay.getRealMetrics(metric)
            metric.heightPixels
        } else {
            resources.displayMetrics.heightPixels
        }
    }

val Context.screenWidth: Int
    get() {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            val metric = DisplayMetrics()
            windowManager.defaultDisplay.getRealMetrics(metric)
            metric.widthPixels
        } else {
            resources.displayMetrics.widthPixels
        }
    }


object DrawableUtils {

    fun selected(select: Drawable, normal: Drawable): StateListDrawable {
        val drawable = StateListDrawable()
        drawable.addState(
                intArrayOf(android.R.attr.state_selected, android.R.attr.state_enabled),
                select
        )
        drawable.addState(intArrayOf(), normal)
        return drawable
    }

    fun pressed(press: Drawable, normal: Drawable): StateListDrawable {
        val drawable = StateListDrawable()
        drawable.addState(
                intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled),
                press
        )
        drawable.addState(intArrayOf(), normal)
        return drawable
    }

    fun dot(@ColorInt color: Int): GradientDrawable {
        val backgroundDrawable = GradientDrawable()
        backgroundDrawable.shape = GradientDrawable.OVAL
        backgroundDrawable.setColor(color)
        return backgroundDrawable
    }
}

object ColorUtils {
    fun pressed(@ColorInt pressed: Int, @ColorInt normal: Int): ColorStateList {
        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(android.R.attr.state_pressed, android.R.attr.state_enabled)
        states[1] = intArrayOf()
        val colors = intArrayOf(pressed, normal)
        return ColorStateList(states, colors)
    }

    fun selected(@ColorInt selected: Int, @ColorInt normal: Int): ColorStateList {
        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(android.R.attr.state_selected, android.R.attr.state_enabled)
        states[1] = intArrayOf()
        val colors = intArrayOf(selected, normal)
        return ColorStateList(states, colors)
    }
}


inline fun <reified T> Context.assets(assetsFileName: String, noinline body: (T?) -> Unit): T? {
    try {
        val inputStream = resources.assets.open(assetsFileName)
        val text = inputStream.use {
            val buf = BufferedReader(InputStreamReader(inputStream, "utf-8"))
            return@use buf.readText()
        }
        val interfaceArray = body.javaClass.genericInterfaces
        if (!interfaceArray.isNullOrEmpty()) {
            val type = interfaceArray[0]
            if (type is ParameterizedType) {
                val bean = JsonUtils.parseJson<T>(text, type.actualTypeArguments[0])
                body(bean)
                return bean
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}


fun <T> T?.orNullReplace(): String {
    val default = "-"
    return when {
        this == null -> default
        this == "" -> default
        this == 0 -> default
        this == 0f -> default
        this == 0.0 -> default
        this == "0" -> default
        this == "0.0" -> default
        else -> this.toString()
    }
}