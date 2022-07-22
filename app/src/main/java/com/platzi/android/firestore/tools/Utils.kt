package com.platzi.android.firestore.ui.activity.tools

import android.util.Log
import com.platzi.android.firestore.BuildConfig

class Utils {

    fun printLog(log: String) {
        printLog("PlatziFirestore", log)
    }

    fun printLog(tag: String, log: String) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, log)
        }
    }

}