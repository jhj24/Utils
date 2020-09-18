package com.zgdj.lib.utils.permissions

import android.os.Build
import androidx.fragment.app.Fragment


/**
 * 请求权限。
 * Created by jianhaojie on 2017/5/24.
 */

class PermissionsFragment : Fragment() {

    private val mRequestCode = 0x10000000
    private var body: (() -> Unit)? = null

    fun permissionsRequest(allPermissions: Array<String>, body: () -> Unit) {
        this.body = body
        //对被禁止的权限进行请求
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(allPermissions, mRequestCode)
        }
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (mRequestCode == requestCode) {
            body?.invoke()
        }
    }
}
