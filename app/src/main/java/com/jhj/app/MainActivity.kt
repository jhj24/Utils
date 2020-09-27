package com.jhj.app

import android.graphics.Color
import android.os.Bundle
import com.jhj.utils.*
import com.jhj.utils.ui.ToolBarActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.sdk27.coroutines.onClick
import org.jetbrains.anko.toast

class MainActivity : ToolBarActivity() {

    override val title: String
        get() = "测试"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_insert.backgroundOval(Color.RED)
        btn_insert.textColor(R.color.colorPrimary)
        btn_insert.onClick {
            val data = UserBean(1, "张三", "男", 18)
            val a = spSave(data, "aaa")
            toast(a.toString())

        }
        btn_find.textPressedColorStateList(color(R.color.colorAccent), color(R.color.colorPrimary))
        btn_find.backgroundPressedStateList(R.color.colorPrimary, R.color.colorAccent)
        btn_find.onClick {
            val a = spFind("aaa", UserBean::class.java)
            toast(a?.name.orEmpty())
        }
        btn_delete.onClick {
            spDelete("aaa", UserBean::class.java)
        }
        btn_assets.onClick {
            assets<UserBean>("user.json") {
                toast(it?.name.orEmpty())
            }
        }


    }


}