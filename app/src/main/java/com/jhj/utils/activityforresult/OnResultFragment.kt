package com.jhj.utils.activityforresult

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment


class OnResultFragment : Fragment() {
    private var body: (Intent?) -> Unit = {}

    fun startActivityForResult(mTargetActivity: Class<out Activity>, intent1: Intent, body: (Intent?) -> Unit) {
        this.body = body
        val intent = Intent(activity, mTargetActivity)
        intent.putExtras(intent1)
        startActivityForResult(intent, ACTIVITY_CODE)
    }

    fun startActivityForResult(intent: Intent, body: (Intent?) -> Unit) {
        this.body = body
        startActivityForResult(intent, ACTIVITY_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTIVITY_CODE && resultCode == Activity.RESULT_OK) {
            body.invoke(data)
        }
    }

    companion object {
        internal var ACTIVITY_CODE = 0x00001000
    }
}
