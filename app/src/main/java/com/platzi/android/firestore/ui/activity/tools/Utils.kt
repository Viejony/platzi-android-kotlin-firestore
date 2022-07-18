package com.platzi.android.firestore.ui.activity.tools

import android.util.Log
import com.platzi.android.firestore.BuildConfig

class Utils {

    fun printLog(log: String) {
        if (BuildConfig.DEBUG) {
            Log.e("SPACE: ", log)
        }
    }

}