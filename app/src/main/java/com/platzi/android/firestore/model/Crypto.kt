package com.platzi.android.firestore.ui.activity

import java.util.*

class Crypto (
    var name: String = "",
    var imagaUrl: String = "",
    var available: Int = 0
){
    fun getDocumentID(): String{
        return name.toLowerCase(Locale.ROOT)
    }
}