package com.jhj.utils.permissions

import android.Manifest
import android.os.Build
import androidx.fragment.app.FragmentActivity

fun <T:FragmentActivity> T.requestPermissions(vararg permissions: String, body: (Array<String>, Array<String>) -> Unit){
    val mPermissions = permissions.toList().toTypedArray()
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) { //Android 6.0之前不用检测
        var deniedArray = arrayOf<String>()
        for (permission in mPermissions) {
            if (Manifest.permission.CAMERA == permission && PermissionsUtil.isCameraDenied) {
                deniedArray = arrayOf(Manifest.permission.CAMERA)
            }
        }
        body(deniedArray, mPermissions)
    } else if (PermissionsUtil.getDeniedPermissions(
            this,
            mPermissions
        ).isNotEmpty()) { //如果有权限被禁止，进行权限请求
        val TAG = javaClass.name
        val fragmentManager = this.supportFragmentManager
        var fragment: PermissionsFragment? = fragmentManager.findFragmentByTag(TAG) as PermissionsFragment?

        if (fragment == null) {
            fragment = PermissionsFragment()
            fragmentManager
                .beginTransaction()
                .add(fragment, TAG)
                .commitAllowingStateLoss()
            fragmentManager.executePendingTransactions()
        }
        fragment.permissionsRequest(mPermissions){
            val permissionList =
                PermissionsUtil.getPermissionDenied(
                    this,
                    mPermissions
                )
            body(permissionList,mPermissions)
        }
    } else { // 所有权限都被允许，使用原生权限管理检验
        val permissionList =
            PermissionsUtil.getPermissionDenied(
                this,
                mPermissions
            )
        body(permissionList,mPermissions)
    }
}
