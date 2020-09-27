package com.jhj.utils.ui

import android.Manifest
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.*
import androidx.annotation.RequiresPermission
import androidx.appcompat.app.AppCompatActivity
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.content.PermissionChecker
import com.jhj.utils.R
import com.jhj.utils.permissions.requestPermissions
import kotlinx.android.synthetic.main.activity_base_tool_bar.*
import org.jetbrains.anko.Android

abstract class ToolBarActivity : AppCompatActivity() {

    abstract val title: String

    open val isDefaultToolBar = true
    open val isTransparentDefaultToolBar = false
    private var rightTitle: String = ""
    private var rightDrawable: Drawable? = null
    private var rightMenuItemOnClick: () -> Unit = {}
    private var leftTitle: String = ""
    private var leftDrawable: Drawable? = null
    private var leftMenuItemOnClick: () -> Unit = {}


    override fun setContentView(layoutResID: Int) {
        super.setContentView(R.layout.activity_base_tool_bar)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN or WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)

        val view = LayoutInflater.from(this).inflate(layoutResID, frame_layout, false)
        frame_layout.addView(view)
        tool_bar.apply {
            title = ""
            setSupportActionBar(this) //使用toolbar代替actionbar
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        tool_bar_title.setText(title)

        if (!isDefaultToolBar) {
            val params = frame_layout.layoutParams as CoordinatorLayout.LayoutParams
            params.behavior = null
            app_bar_layout.visibility = View.GONE
        }
        if (isTransparentDefaultToolBar) {
            val params = frame_layout.layoutParams as CoordinatorLayout.LayoutParams
            params.behavior = null
            app_bar_layout.setBackgroundColor(Color.TRANSPARENT)
        }
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            R.id.toolbar_action_right -> rightMenuItemOnClick()
            R.id.toolbar_action_left -> leftMenuItemOnClick()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_action, menu)
        menu?.findItem(R.id.toolbar_action_right)?.apply {
            this.title = rightTitle
            this.icon = rightDrawable
        }
        menu?.findItem(R.id.toolbar_action_left)?.apply {
            this.isVisible = !leftTitle.isBlank() || leftDrawable != null
            this.title = leftTitle
            this.icon = leftDrawable
        }
        return true
    }


    fun rightMenuItem(title: String, body: () -> Unit) {
        this.rightTitle = title
        this.rightMenuItemOnClick = body
    }

    fun rightMenuItem(drawable: Drawable, body: () -> Unit) {
        this.rightDrawable = drawable
        this.rightMenuItemOnClick = body
    }

    fun leftMenuItem(title: String, body: () -> Unit) {
        this.leftTitle = title
        this.leftMenuItemOnClick = body
    }

    fun leftMenuItem(drawable: Drawable, body: () -> Unit) {
        this.leftDrawable = drawable
        this.leftMenuItemOnClick = body
    }

@PermissionChecker.PermissionResult
    @RequiresPermission(Manifest.permission.CAMERA)
    fun requestPermissions(vararg permissions: String, body: () -> Unit) {
        requestPermissions(*permissions) { deniedPermissions, allPermissions ->
            if (deniedPermissions.isEmpty()) {
                body()
            } else {
                val infoList = arrayListOf<String>()
                deniedPermissions.forEach {
                    /*  if (PermissionsUtil.permissionsMap.containsKey(it)) {
                          if (!infoList.contains(PermissionsUtil.permissionsMap[it])) {
                              toast("${PermissionsUtil.permissionsMap[it]}权限请求失败")
                          }
                          infoList.add(PermissionsUtil.permissionsMap[it] ?: "")
                          return@forEach
                      }*/
                }
            }
        }
    }

}