package com.jhj.utils

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.jetbrains.anko.inputMethodManager
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast


fun <V : ImageView> V.colorFilter(color: Int) = setColorFilter(color, PorterDuff.Mode.SRC_IN)

fun <V : View> V.backgroundPressedStateList(@DrawableRes pressed: Int, @DrawableRes normal: Int) {
    background = context.pressedStateListDrawable(pressed, normal)
}

fun <V : View> V.backgroundSelectedStateList(@DrawableRes selected: Int, @DrawableRes normal: Int) {
    background = context.selectedStateListDrawable(selected, normal)
}

fun <V : View> V.background(@DrawableRes drawableRes: Int) {
    background = context.drawable(drawableRes)
}

fun <V : View> V.backgroundOval(@ColorInt color: Int) {
    background = DrawableUtils.dot(color)
}

fun <V : TextView> V.textPressedColorStateList(@ColorInt pressed: Int, @ColorInt normal: Int) {
    setTextColor(context.pressedColorStateList(pressed, normal))
}

fun <V : TextView> V.textSelectedColorStateList(@ColorInt selected: Int, @ColorInt normal: Int) {
    setTextColor(context.selectedColorStateList(selected, normal))
}

fun <V : TextView> V.textColor(@ColorRes color: Int) {
    setTextColor(context.color(color))
}


fun <V : View> V.clicked(isClicked: Boolean) {
    isFocusable = isClicked
    isClickable = isClicked
    if (this is EditText && isClicked) {
        isFocusableInTouchMode = true
    }
}


fun <V : View> V.openKeyboard() {
    val imm = context.inputMethodManager
    imm.showSoftInput(this, InputMethodManager.RESULT_SHOWN)
    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
}

fun <V : View> V.closeKeyboard() {
    val imm = context.inputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}


fun <V : TextView> V.inputLimit(maxSize: Int = 200, msg: String = "") {
    var text = ""
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            var s = editable.toString()
            if (s.length > maxSize) {
                s = s.substring(0, s.length - 1)
                if (text != s) context.toast("${msg}最多输入${maxSize}个字符")
                editable?.replace(0, editable.length, s.trim { it <= ' ' })
                text = s
            } else {
                text = ""
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }
    })
}


/**
 * 解决RecyclerView嵌套RecyclerView滑动卡顿的问题
 */
fun RecyclerView.preventStuck() {
    layoutManager = object : LinearLayoutManager(context) {
        //如果你的RecyclerView是水平滑动的话可以重写canScrollHorizontally方法
        override fun canScrollVertically() = false
    }
    //解决数据加载不完的问题
    isNestedScrollingEnabled = false
    setHasFixedSize(true)
    //解决数据加载完成后, 没有停留在顶部的问题
    isFocusable = false
}


fun <T : View> T.singleClick(time: Long = 800, block: (T) -> Unit) {
    var lastClickTime = 0L
    onClick {
        val currentTimeMillis = System.currentTimeMillis()
        if (currentTimeMillis - lastClickTime < 500) {//判断系统时间差是否小于点击间隔时间
            return@onClick
        }
        block.invoke(this@singleClick)
        lastClickTime = currentTimeMillis
    }
}

